package ru.storage.server.model.dao.daos;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.server.model.domain.dto.exceptions.ValidationException;
import ru.storage.server.model.dao.DAO;
import ru.storage.server.model.dao.exceptions.DAOException;
import ru.storage.server.model.domain.entity.entities.worker.person.Location;
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

public class LocationDAO implements DAO<Long, Location> {
  private static final String CANNOT_GET_ALL_LOCATION_EXCEPTION_MESSAGE;
  private static final String CANNOT_GET_LOCATION_BY_ID_EXCEPTION_MESSAGE;
  private static final String CANNOT_INSERT_LOCATION_EXCEPTION_MESSAGE;
  private static final String CANNOT_UPDATE_LOCATION_EXCEPTION_MESSAGE;
  private static final String CANNOT_DELETE_LOCATION_EXCEPTION_MESSAGE;

  static {
    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("internal.LocationDAO");

    CANNOT_GET_ALL_LOCATION_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.cannotGetAllLocations");
    CANNOT_GET_LOCATION_BY_ID_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.cannotGetLocationById");
    CANNOT_INSERT_LOCATION_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.cannotInsertLocation");
    CANNOT_UPDATE_LOCATION_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.cannotUpdateLocation");
    CANNOT_DELETE_LOCATION_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.cannotDeleteLocation");
  }

  private final String SELECT_ALL = "SELECT * FROM " + Location.TABLE_NAME;

  private final String SELECT_BY_ID = SELECT_ALL + " WHERE " + Location.ID_COLUMN + " = ?";

  private final String INSERT =
      "INSERT INTO "
          + Location.TABLE_NAME
          + " ("
          + Location.OWNER_ID_COLUMN
          + ", "
          + Location.ADDRESS_COLUMN
          + ", "
          + Location.LATITUDE_COLUMN
          + ", "
          + Location.LONGITUDE_COLUMN
          + ") VALUES (?, ?, ?, ?)";

  private final String UPDATE =
      "UPDATE "
          + Location.TABLE_NAME
          + " SET "
          + Location.OWNER_ID_COLUMN
          + " = ?, "
          + Location.ADDRESS_COLUMN
          + " = ?, "
          + Location.LATITUDE_COLUMN
          + " = ?, "
          + Location.LONGITUDE_COLUMN
          + " = ? WHERE "
          + Location.ID_COLUMN
          + " = ?";

  private final String DELETE =
      "DELETE FROM " + Location.TABLE_NAME + " WHERE " + Location.ID_COLUMN + " = ?";

  private final Logger logger;
  private final DataSource dataSource;

  public LocationDAO(DataSource dataSource) {
    this.logger = LogManager.getLogger(LocationDAO.class);
    this.dataSource = dataSource;
  }

  @Override
  public List<Location> getAll() throws DAOException, DataSourceException {
    List<Location> allLocations = new ArrayList<>();
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(SELECT_ALL, Statement.NO_GENERATED_KEYS);

    try {
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        Long id = resultSet.getLong(Location.ID_COLUMN);
        Long ownerId = resultSet.getLong(Location.OWNER_ID_COLUMN);
        String address = resultSet.getString(Location.ADDRESS_COLUMN);
        Double latitude = resultSet.getDouble(Location.LATITUDE_COLUMN);
        Double longitude = resultSet.getDouble(Location.LONGITUDE_COLUMN);

        Location location = new Location(id, ownerId, address, latitude, longitude);
        allLocations.add(location);
      }
    } catch (SQLException | ValidationException e) {
      logger.error("Cannot get all locations.", e);
      throw new DAOException(CANNOT_GET_ALL_LOCATION_EXCEPTION_MESSAGE, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info("Got all locations SUCCESSFULLY.");
    return allLocations;
  }

  @Override
  public Location getByKey(@Nonnull Long id) throws DAOException, DataSourceException {
    Location location = null;
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(SELECT_BY_ID, Statement.NO_GENERATED_KEYS);

    try {
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        Long ownerId = resultSet.getLong(Location.OWNER_ID_COLUMN);
        String address = resultSet.getString(Location.ADDRESS_COLUMN);
        Double latitude = resultSet.getDouble(Location.LATITUDE_COLUMN);
        Double longitude = resultSet.getDouble(Location.LONGITUDE_COLUMN);

        location = new Location(id, ownerId, address, latitude, longitude);
      }
    } catch (SQLException | ValidationException e) {
      logger.error("Cannot get location by id.", e);
      throw new DAOException(CANNOT_GET_LOCATION_BY_ID_EXCEPTION_MESSAGE, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info("Got location by id SUCCESSFULLY.");
    return location;
  }

  @Override
  public Location insert(@Nonnull Location location) throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, location.getOwnerID());
      preparedStatement.setString(2, location.getAddress());
      preparedStatement.setDouble(3, location.getLatitude());
      preparedStatement.setDouble(4, location.getLongitude());

      preparedStatement.execute();

      ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
      if (generatedKeys.next()) {
        location.setID(generatedKeys.getLong(1));
      }
    } catch (SQLException | ValidationException e) {
      logger.error("Cannot insert location.", e);
      throw new DAOException(CANNOT_INSERT_LOCATION_EXCEPTION_MESSAGE, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info("Location was inserted SUCCESSFULLY.");
    return location;
  }

  @Override
  public Location update(@Nonnull Location location) throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(UPDATE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, location.getOwnerID());
      preparedStatement.setString(2, location.getAddress());
      preparedStatement.setDouble(3, location.getLatitude());
      preparedStatement.setDouble(4, location.getLongitude());
      preparedStatement.setLong(5, location.getID());

      preparedStatement.execute();
    } catch (SQLException e) {
      logger.error("Cannot update location.", e);
      throw new DAOException(CANNOT_UPDATE_LOCATION_EXCEPTION_MESSAGE, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info("Location was updated SUCCESSFULLY.");
    return location;
  }

  @Override
  public void delete(@Nonnull Location location) throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(DELETE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, location.getID());

      preparedStatement.execute();
    } catch (SQLException e) {
      logger.error("Cannot delete location.", e);
      throw new DAOException(CANNOT_DELETE_LOCATION_EXCEPTION_MESSAGE, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info("Location was deleted SUCCESSFULLY.");
  }
}
