package ru.storage.server.model.dao.daos;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.server.model.dao.DAO;
import ru.storage.server.model.dao.exceptions.DAOException;
import ru.storage.server.model.domain.dto.exceptions.ValidationException;
import ru.storage.server.model.domain.entity.entities.user.Role;
import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.source.DataSource;
import ru.storage.server.model.source.exceptions.DataSourceException;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UserDAO implements DAO<String, User> {
  private static final String CANNOT_GET_ALL_USER_EXCEPTION_MESSAGE;
  private static final String CANNOT_GET_USER_BY_ID_EXCEPTION_MESSAGE;
  private static final String CANNOT_INSERT_USER_EXCEPTION_MESSAGE;
  private static final String CANNOT_UPDATE_USER_EXCEPTION_MESSAGE;
  private static final String CANNOT_DELETE_USER_EXCEPTION_MESSAGE;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.UserDAO");

    CANNOT_GET_ALL_USER_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.cannotGetAllUsers");
    CANNOT_GET_USER_BY_ID_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.cannotGetUserById");
    CANNOT_INSERT_USER_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.cannotInsertUser");
    CANNOT_UPDATE_USER_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.cannotUpdateUser");
    CANNOT_DELETE_USER_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.cannotDeleteUser");
  }

  private final String SELECT_ALL = "SELECT * FROM " + User.TABLE_NAME;

  private final String SELECT_BY_ID = SELECT_ALL + " WHERE " + User.ID_COLUMN + " = ?";

  private final String INSERT =
      "INSERT INTO "
          + User.TABLE_NAME
          + " ("
          + User.NAME_COLUMN
          + ", "
          + User.LOGIN_COLUMN
          + ", "
          + User.PASSWORD_COLUMN
          + ", "
          + User.ROLE_COLUMN
          + ", "
          + User.STATE_COLUMN
          + ") VALUES (?, ?, ?, ?, ?)";

  private final String UPDATE =
      "UPDATE "
          + User.TABLE_NAME
          + " SET "
          + User.NAME_COLUMN
          + " = ?, "
          + User.LOGIN_COLUMN
          + " = ?, "
          + User.PASSWORD_COLUMN
          + " = ?, "
          + User.ROLE_COLUMN
          + " = ?, "
          + User.STATE_COLUMN
          + " = ? WHERE "
          + User.ID_COLUMN
          + " = ?";

  private final String DELETE =
      "DELETE FROM " + User.TABLE_NAME + " WHERE " + User.ID_COLUMN + " = ?";

  private final Logger logger;
  private final DataSource dataSource;

  public UserDAO(DataSource dataSource) {
    this.logger = LogManager.getLogger(UserDAO.class);
    this.dataSource = dataSource;
  }

  @Override
  public List<User> getAll() throws DAOException, DataSourceException {
    List<User> allUsers = new ArrayList<>();
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(SELECT_ALL, Statement.NO_GENERATED_KEYS);

    try {
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        Long id = resultSet.getLong(User.ID_COLUMN);
        String name = resultSet.getString(User.NAME_COLUMN);
        String login = resultSet.getString(User.LOGIN_COLUMN);
        String password = resultSet.getString(User.PASSWORD_COLUMN);
        Role role = Role.getRole(resultSet.getString(User.ROLE_COLUMN));
        Boolean state = resultSet.getBoolean(User.STATE_COLUMN);

        User user = new User(id, name, login, password, role, state);
        allUsers.add(user);
      }
    } catch (SQLException | ValidationException e) {
      logger.error(() -> "Cannot get all users.", e);
      throw new DAOException(CANNOT_GET_ALL_USER_EXCEPTION_MESSAGE, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Got all users.");
    return allUsers;
  }

  @Override
  public User getByKey(@Nonnull String login) throws DAOException, DataSourceException {
    User user = null;
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(SELECT_BY_ID, Statement.NO_GENERATED_KEYS);

    try {
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        Long id = resultSet.getLong(User.ID_COLUMN);
        String name = resultSet.getString(User.NAME_COLUMN);
        String password = resultSet.getString(User.PASSWORD_COLUMN);
        Role role = Role.getRole(resultSet.getString(User.ROLE_COLUMN));
        Boolean state = resultSet.getBoolean(User.STATE_COLUMN);

        user = new User(id, name, login, password, role, state);
      }
    } catch (SQLException | ValidationException e) {
      logger.error(() -> "Cannot get user by id.", e);
      throw new DAOException(CANNOT_GET_USER_BY_ID_EXCEPTION_MESSAGE, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Got user by id.");
    return user;
  }

  @Override
  public User insert(@Nonnull User user) throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

    try {
      preparedStatement.setString(1, user.getName());
      preparedStatement.setString(2, user.getLogin());
      preparedStatement.setString(3, user.getPassword());
      preparedStatement.setString(4, user.getRole().toString());
      preparedStatement.setBoolean(5, user.getState());

      preparedStatement.execute();

      ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
      if (generatedKeys.next()) {
        user.setID(generatedKeys.getLong(1));
      }
    } catch (SQLException | ValidationException e) {
      logger.error(() -> "Cannot insert user.", e);
      throw new DAOException(CANNOT_INSERT_USER_EXCEPTION_MESSAGE, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "User was inserted.");
    return user;
  }

  @Override
  public User update(@Nonnull User user) throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(UPDATE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setString(1, user.getName());
      preparedStatement.setString(2, user.getLogin());
      preparedStatement.setString(3, user.getPassword());
      preparedStatement.setString(4, user.getRole().toString());
      preparedStatement.setBoolean(5, user.getState());
      preparedStatement.setLong(6, user.getID());

      preparedStatement.execute();
    } catch (SQLException e) {
      logger.error(() -> "Cannot update user.", e);
      throw new DAOException(CANNOT_UPDATE_USER_EXCEPTION_MESSAGE, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "User was updated.");
    return user;
  }

  @Override
  public void delete(@Nonnull User user) throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(DELETE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, user.getID());

      preparedStatement.execute();
    } catch (SQLException e) {
      logger.error(() -> "Cannot delete user.", e);
      throw new DAOException(CANNOT_DELETE_USER_EXCEPTION_MESSAGE, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "User was deleted.");
  }
}
