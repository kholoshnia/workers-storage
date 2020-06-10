package ru.storage.server.model.dao.daos;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.api.dto.exceptions.ValidationException;
import ru.storage.server.model.dao.DAO;
import ru.storage.server.model.dao.exceptions.DAOException;
import ru.storage.server.model.domain.entity.entities.worker.Coordinates;
import ru.storage.server.model.source.DataSource;
import ru.storage.server.model.source.exceptions.DataSourceException;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class CoordinatesDAO implements DAO<Long, Coordinates> {
  private static final String CANNOT_GET_ALL_COORDINATES_EXCEPTION_MESSAGE;
  private static final String CANNOT_GET_COORDINATES_BY_ID_EXCEPTION_MESSAGE;
  private static final String CANNOT_INSERT_COORDINATES_EXCEPTION_MESSAGE;
  private static final String CANNOT_UPDATE_COORDINATES_EXCEPTION_MESSAGE;
  private static final String CANNOT_DELETE_COORDINATES_EXCEPTION_MESSAGE;

  static {
    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("internal.CoordinatesDAO", Locale.ENGLISH);

    CANNOT_GET_ALL_COORDINATES_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.cannotGetAllCoordinates");
    CANNOT_GET_COORDINATES_BY_ID_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.cannotGetCoordinatesById");
    CANNOT_INSERT_COORDINATES_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.cannotInsertCoordinates");
    CANNOT_UPDATE_COORDINATES_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.cannotUpdateCoordinates");
    CANNOT_DELETE_COORDINATES_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.cannotDeleteCoordinates");
  }

  private final String SELECT_ALL = "SELECT * FROM " + Coordinates.TABLE_NAME;

  private final String SELECT_BY_ID = SELECT_ALL + " WHERE " + Coordinates.ID_COLUMN + " = ?";

  private final String INSERT =
      "INSERT INTO "
          + Coordinates.TABLE_NAME
          + " ("
          + Coordinates.OWNER_ID_COLUMN
          + ", "
          + Coordinates.X_COLUMN
          + ", "
          + Coordinates.Y_COLUMN
          + ", "
          + Coordinates.Z_COLUMN
          + ") VALUES (?, ?, ?, ?)";

  private final String UPDATE =
      "UPDATE "
          + Coordinates.TABLE_NAME
          + " SET "
          + Coordinates.OWNER_ID_COLUMN
          + " = ?, "
          + Coordinates.X_COLUMN
          + " = ?, "
          + Coordinates.Y_COLUMN
          + " = ?, "
          + Coordinates.Z_COLUMN
          + " = ? WHERE "
          + Coordinates.ID_COLUMN
          + " = ?";

  private final String DELETE =
      "DELETE FROM " + Coordinates.TABLE_NAME + " WHERE " + Coordinates.ID_COLUMN + " = ?";

  private final Logger logger;
  private final DataSource dataSource;

  public CoordinatesDAO(DataSource dataSource) {
    this.logger = LogManager.getLogger(CoordinatesDAO.class);
    this.dataSource = dataSource;
  }

  @Override
  public List<Coordinates> getAll() throws DAOException, DataSourceException {
    List<Coordinates> allCoordinates = new ArrayList<>();
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(SELECT_ALL, Statement.NO_GENERATED_KEYS);

    try {
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        Long id = resultSet.getLong(Coordinates.ID_COLUMN);
        Long ownerId = resultSet.getLong(Coordinates.OWNER_ID_COLUMN);
        Double x = resultSet.getDouble(Coordinates.X_COLUMN);
        Double y = resultSet.getDouble(Coordinates.Y_COLUMN);
        Double z = resultSet.getDouble(Coordinates.Z_COLUMN);

        Coordinates coordinates = new Coordinates(id, ownerId, x, y, z);
        allCoordinates.add(coordinates);
      }
    } catch (SQLException | ValidationException e) {
      logger.error("Cannot get all coordinates.", e);
      throw new DAOException(CANNOT_GET_ALL_COORDINATES_EXCEPTION_MESSAGE, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info("Got all coordinates SUCCESSFULLY.");
    return allCoordinates;
  }

  @Override
  public Coordinates getByKey(@Nonnull Long id) throws DAOException, DataSourceException {
    Coordinates coordinates = null;
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(SELECT_BY_ID, Statement.NO_GENERATED_KEYS);

    try {
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        Long ownerId = resultSet.getLong(Coordinates.OWNER_ID_COLUMN);
        Double x = resultSet.getDouble(Coordinates.X_COLUMN);
        Double y = resultSet.getDouble(Coordinates.Y_COLUMN);
        Double z = resultSet.getDouble(Coordinates.Z_COLUMN);

        coordinates = new Coordinates(id, ownerId, x, y, z);
      }
    } catch (SQLException | ValidationException e) {
      logger.error("Cannot get coordinates by id.", e);
      throw new DAOException(CANNOT_GET_COORDINATES_BY_ID_EXCEPTION_MESSAGE, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info("Got coordinates by id SUCCESSFULLY.");
    return coordinates;
  }

  @Override
  public Coordinates insert(@Nonnull Coordinates coordinates)
      throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

    try {
      preparedStatement.setDouble(1, coordinates.getOwnerID());
      preparedStatement.setDouble(2, coordinates.getX());
      preparedStatement.setDouble(3, coordinates.getY());
      preparedStatement.setDouble(4, coordinates.getZ());

      preparedStatement.execute();

      ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
      if (generatedKeys.next()) {
        coordinates.setID(generatedKeys.getLong(1));
      }
    } catch (SQLException | ValidationException e) {
      logger.error("Cannot insert coordinates.", e);
      throw new DAOException(CANNOT_INSERT_COORDINATES_EXCEPTION_MESSAGE, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info("Coordinates was inserted SUCCESSFULLY.");
    return coordinates;
  }

  @Override
  public Coordinates update(@Nonnull Coordinates coordinates)
      throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(UPDATE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setDouble(1, coordinates.getOwnerID());
      preparedStatement.setDouble(2, coordinates.getX());
      preparedStatement.setDouble(3, coordinates.getY());
      preparedStatement.setDouble(4, coordinates.getZ());
      preparedStatement.setLong(5, coordinates.getID());

      preparedStatement.execute();
    } catch (SQLException e) {
      logger.error("Cannot update coordinates.", e);
      throw new DAOException(CANNOT_UPDATE_COORDINATES_EXCEPTION_MESSAGE, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info("Coordinates was updated SUCCESSFULLY.");
    return coordinates;
  }

  @Override
  public void delete(@Nonnull Coordinates coordinates) throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(DELETE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, coordinates.getID());

      preparedStatement.execute();
    } catch (SQLException e) {
      logger.error("Cannot delete coordinates.", e);
      throw new DAOException(CANNOT_DELETE_COORDINATES_EXCEPTION_MESSAGE, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info("Coordinates was deleted SUCCESSFULLY.");
  }
}
