package ru.storage.server.model.dao.daos;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.server.model.dao.DAO;
import ru.storage.server.model.dao.exceptions.DAOException;
import ru.storage.server.model.domain.dto.dtos.UserDTO;
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

public class UserDAO implements DAO<String, UserDTO> {
  private static final String CANNOT_GET_ALL_USER_EXCEPTION;
  private static final String CANNOT_GET_USER_BY_ID_EXCEPTION;
  private static final String CANNOT_INSERT_USER_EXCEPTION;
  private static final String CANNOT_UPDATE_USER_EXCEPTION;
  private static final String CANNOT_DELETE_USER_EXCEPTION;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.UserDAO");

    CANNOT_GET_ALL_USER_EXCEPTION = resourceBundle.getString("exceptions.cannotGetAllUsers");
    CANNOT_GET_USER_BY_ID_EXCEPTION = resourceBundle.getString("exceptions.cannotGetUserById");
    CANNOT_INSERT_USER_EXCEPTION = resourceBundle.getString("exceptions.cannotInsertUser");
    CANNOT_UPDATE_USER_EXCEPTION = resourceBundle.getString("exceptions.cannotUpdateUser");
    CANNOT_DELETE_USER_EXCEPTION = resourceBundle.getString("exceptions.cannotDeleteUser");
  }

  private final String SELECT_ALL = "SELECT * FROM " + UserDTO.TABLE_NAME;

  private final String SELECT_BY_ID = SELECT_ALL + " WHERE " + UserDTO.ID_COLUMN + " = ?";

  private final String INSERT =
      "INSERT INTO "
          + UserDTO.TABLE_NAME
          + " ("
          + UserDTO.NAME_COLUMN
          + ", "
          + UserDTO.LOGIN_COLUMN
          + ", "
          + UserDTO.PASSWORD_COLUMN
          + ", "
          + UserDTO.ROLE_COLUMN
          + ") VALUES (?, ?, ?, ?)";

  private final String UPDATE =
      "UPDATE "
          + UserDTO.TABLE_NAME
          + " SET "
          + UserDTO.NAME_COLUMN
          + " = ?, "
          + UserDTO.LOGIN_COLUMN
          + " = ?, "
          + UserDTO.PASSWORD_COLUMN
          + " = ?, "
          + UserDTO.ROLE_COLUMN
          + " = ?, WHERE "
          + UserDTO.ID_COLUMN
          + " = ?";

  private final String DELETE =
      "DELETE FROM " + UserDTO.TABLE_NAME + " WHERE " + UserDTO.ID_COLUMN + " = ?";

  private final Logger logger;
  private final DataSource dataSource;

  @Inject
  public UserDAO(DataSource dataSource) {
    this.logger = LogManager.getLogger(UserDAO.class);
    this.dataSource = dataSource;
  }

  @Override
  public List<UserDTO> getAll() throws DAOException, DataSourceException {
    List<UserDTO> allUserDTOs = new ArrayList<>();
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(SELECT_ALL, Statement.NO_GENERATED_KEYS);

    try {
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        Long id = resultSet.getObject(UserDTO.ID_COLUMN, Long.class);
        String name = resultSet.getObject(UserDTO.NAME_COLUMN, String.class);
        String login = resultSet.getObject(UserDTO.LOGIN_COLUMN, String.class);
        String password = resultSet.getObject(UserDTO.PASSWORD_COLUMN, String.class);
        Role role = Role.getRole(resultSet.getObject(UserDTO.ROLE_COLUMN, String.class));

        UserDTO userDTO = new UserDTO(id, name, login, password, role);
        allUserDTOs.add(userDTO);
      }
    } catch (SQLException e) {
      logger.error(() -> "Cannot get all users.", e);
      throw new DAOException(CANNOT_GET_ALL_USER_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Got all users.");
    return allUserDTOs;
  }

  @Override
  public UserDTO getByKey(@Nonnull String login) throws DAOException, DataSourceException {
    UserDTO userDTO = null;
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(SELECT_BY_ID, Statement.NO_GENERATED_KEYS);

    try {
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        Long id = resultSet.getObject(UserDTO.ID_COLUMN, Long.class);
        String name = resultSet.getObject(UserDTO.NAME_COLUMN, String.class);
        String password = resultSet.getObject(UserDTO.PASSWORD_COLUMN, String.class);
        Role role = Role.getRole(resultSet.getObject(UserDTO.ROLE_COLUMN, String.class));

        userDTO = new UserDTO(id, name, login, password, role);
      }
    } catch (SQLException e) {
      logger.error(() -> "Cannot get user by id.", e);
      throw new DAOException(CANNOT_GET_USER_BY_ID_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Got user by id.");
    return userDTO;
  }

  @Override
  public UserDTO insert(@Nonnull UserDTO userDTO) throws DAOException, DataSourceException {
    UserDTO result;
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

    try {
      preparedStatement.setString(1, userDTO.name);
      preparedStatement.setString(2, userDTO.login);
      preparedStatement.setString(3, userDTO.password);
      preparedStatement.setString(4, userDTO.role.toString());

      preparedStatement.execute();

      ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
      if (generatedKeys.next()) {
        result =
            new UserDTO(
                generatedKeys.getLong(1),
                userDTO.name,
                userDTO.login,
                userDTO.password,
                userDTO.role);

      } else {
        result =
            new UserDTO(
                User.DEFAULT_ID, userDTO.name, userDTO.login, userDTO.password, userDTO.role);
      }
    } catch (SQLException e) {
      logger.error(() -> "Cannot insert user.", e);
      throw new DAOException(CANNOT_INSERT_USER_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "User has been inserted.");
    return result;
  }

  @Override
  public UserDTO update(@Nonnull UserDTO userDTO) throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(UPDATE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setString(1, userDTO.name);
      preparedStatement.setString(2, userDTO.login);
      preparedStatement.setString(3, userDTO.password);
      preparedStatement.setString(4, userDTO.role.toString());

      preparedStatement.setLong(6, userDTO.id);

      preparedStatement.execute();
    } catch (SQLException e) {
      logger.error(() -> "Cannot update user.", e);
      throw new DAOException(CANNOT_UPDATE_USER_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "User has been updated.");
    return userDTO;
  }

  @Override
  public void delete(@Nonnull UserDTO userDTO) throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(DELETE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, userDTO.id);

      preparedStatement.execute();
    } catch (SQLException e) {
      logger.error(() -> "Cannot delete user.", e);
      throw new DAOException(CANNOT_DELETE_USER_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "User has been deleted.");
  }
}
