package ru.storage.server.model.dao.daos;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.server.model.dao.DAO;
import ru.storage.server.model.dao.adapter.Adapter;
import ru.storage.server.model.dao.adapter.exceptions.AdapterException;
import ru.storage.server.model.dao.exceptions.DAOException;
import ru.storage.server.model.domain.dto.dtos.UserDTO;
import ru.storage.server.model.domain.entity.entities.user.Role;
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
  private static final String SELECT_ALL = "SELECT * FROM " + UserDTO.TABLE_NAME;

  private static final String SELECT_BY_ID = SELECT_ALL + " WHERE " + UserDTO.ID_COLUMN + " = ?";

  private static final String INSERT =
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

  private static final String UPDATE =
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

  private static final String DELETE =
      "DELETE FROM " + UserDTO.TABLE_NAME + " WHERE " + UserDTO.ID_COLUMN + " = ?";

  private static final String GET_ALL_USER_EXCEPTION;
  private static final String GET_USER_BY_ID_EXCEPTION;
  private static final String INSERT_USER_EXCEPTION;
  private static final String GET_GENERATED_USER_ID;
  private static final String UPDATE_USER_EXCEPTION;
  private static final String DELETE_USER_EXCEPTION;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.UserDAO");

    GET_ALL_USER_EXCEPTION = resourceBundle.getString("exceptions.getAllUsers");
    GET_USER_BY_ID_EXCEPTION = resourceBundle.getString("exceptions.getUserById");
    INSERT_USER_EXCEPTION = resourceBundle.getString("exceptions.insertUser");
    GET_GENERATED_USER_ID = resourceBundle.getString("exceptions.getGeneratedUserId");
    UPDATE_USER_EXCEPTION = resourceBundle.getString("exceptions.updateUser");
    DELETE_USER_EXCEPTION = resourceBundle.getString("exceptions.deleteUser");
  }

  private final Logger logger;
  private final DataSource dataSource;
  private final Adapter<Role, String> roleAdapter;

  @Inject
  public UserDAO(DataSource dataSource, Adapter<Role, String> roleAdapter) {
    logger = LogManager.getLogger(UserDAO.class);
    this.dataSource = dataSource;
    this.roleAdapter = roleAdapter;
  }

  @Override
  public List<UserDTO> getAll() throws DAOException, DataSourceException {
    List<UserDTO> allUserDTOs = new ArrayList<>();
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(SELECT_ALL, Statement.NO_GENERATED_KEYS);

    try {
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        long id = resultSet.getLong(UserDTO.ID_COLUMN);
        String name = resultSet.getObject(UserDTO.NAME_COLUMN, String.class);
        String login = resultSet.getObject(UserDTO.LOGIN_COLUMN, String.class);
        String password = resultSet.getObject(UserDTO.PASSWORD_COLUMN, String.class);
        Role role = roleAdapter.from(resultSet.getObject(UserDTO.ROLE_COLUMN, String.class));

        UserDTO userDTO = new UserDTO(id, name, login, password, role);
        allUserDTOs.add(userDTO);
      }
    } catch (SQLException | AdapterException e) {
      logger.error(() -> "Cannot get all users.", e);
      throw new DAOException(GET_ALL_USER_EXCEPTION, e);
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
      preparedStatement.setString(1, login);
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        long id = resultSet.getLong(UserDTO.ID_COLUMN);
        String name = resultSet.getObject(UserDTO.NAME_COLUMN, String.class);
        String password = resultSet.getObject(UserDTO.PASSWORD_COLUMN, String.class);
        Role role = roleAdapter.from(resultSet.getObject(UserDTO.ROLE_COLUMN, String.class));

        userDTO = new UserDTO(id, name, login, password, role);
      }
    } catch (SQLException | AdapterException e) {
      logger.error(() -> "Cannot get user by id.", e);
      throw new DAOException(GET_USER_BY_ID_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Got user by id.");
    return userDTO;
  }

  @Override
  public UserDTO insert(@Nonnull UserDTO userDTO) throws DAOException, DataSourceException {
    long resultId;
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

    try {
      preparedStatement.setString(1, userDTO.name);
      preparedStatement.setString(2, userDTO.login);
      preparedStatement.setString(3, userDTO.password);
      preparedStatement.setString(4, roleAdapter.to(userDTO.role));

      preparedStatement.execute();

      ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
      if (generatedKeys.next()) {
        resultId = generatedKeys.getLong(1);
      } else {
        logger.error(() -> "Cannot get generated user id.");
        throw new DAOException(GET_GENERATED_USER_ID);
      }
    } catch (SQLException | AdapterException e) {
      logger.error(() -> "Cannot insert user.", e);
      throw new DAOException(INSERT_USER_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "User was inserted.");
    return new UserDTO(resultId, userDTO.name, userDTO.login, userDTO.password, userDTO.role);
  }

  @Override
  public UserDTO update(@Nonnull UserDTO userDTO) throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(UPDATE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setString(1, userDTO.name);
      preparedStatement.setString(2, userDTO.login);
      preparedStatement.setString(3, userDTO.password);
      preparedStatement.setString(4, roleAdapter.to(userDTO.role));

      preparedStatement.setLong(6, userDTO.id);

      preparedStatement.execute();
    } catch (SQLException | AdapterException e) {
      logger.error(() -> "Cannot update user.", e);
      throw new DAOException(UPDATE_USER_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "User was updated.");
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
      throw new DAOException(DELETE_USER_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "User was deleted.");
  }
}
