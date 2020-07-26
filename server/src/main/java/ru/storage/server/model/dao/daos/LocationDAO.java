package ru.storage.server.model.dao.daos;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.server.model.dao.DAO;
import ru.storage.server.model.dao.exceptions.DAOException;
import ru.storage.server.model.domain.dto.dtos.LocationDTO;
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

public class LocationDAO implements DAO<Long, LocationDTO> {
  private static final String SELECT_ALL = "SELECT * FROM " + LocationDTO.TABLE_NAME;

  private static final String SELECT_BY_ID =
      SELECT_ALL + " WHERE " + LocationDTO.ID_COLUMN + " = ?";

  private static final String INSERT =
      "INSERT INTO "
          + LocationDTO.TABLE_NAME
          + " ("
          + LocationDTO.OWNER_ID_COLUMN
          + ", "
          + LocationDTO.ADDRESS_COLUMN
          + ", "
          + LocationDTO.LATITUDE_COLUMN
          + ", "
          + LocationDTO.LONGITUDE_COLUMN
          + ") VALUES (?, ?, ?, ?)";

  private static final String UPDATE =
      "UPDATE "
          + LocationDTO.TABLE_NAME
          + " SET "
          + LocationDTO.OWNER_ID_COLUMN
          + " = ?, "
          + LocationDTO.ADDRESS_COLUMN
          + " = ?, "
          + LocationDTO.LATITUDE_COLUMN
          + " = ?, "
          + LocationDTO.LONGITUDE_COLUMN
          + " = ? WHERE "
          + LocationDTO.ID_COLUMN
          + " = ?";

  private static final String DELETE =
      "DELETE FROM " + LocationDTO.TABLE_NAME + " WHERE " + LocationDTO.ID_COLUMN + " = ?";

  private static final String GET_ALL_LOCATION_EXCEPTION;
  private static final String GET_LOCATION_BY_ID_EXCEPTION;
  private static final String INSERT_LOCATION_EXCEPTION;
  private static final String GET_GENERATED_LOCATION_ID;
  private static final String UPDATE_LOCATION_EXCEPTION;
  private static final String DELETE_LOCATION_EXCEPTION;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.LocationDAO");

    GET_ALL_LOCATION_EXCEPTION = resourceBundle.getString("exceptions.getAllLocations");
    GET_LOCATION_BY_ID_EXCEPTION = resourceBundle.getString("exceptions.getLocationById");
    INSERT_LOCATION_EXCEPTION = resourceBundle.getString("exceptions.insertLocation");
    GET_GENERATED_LOCATION_ID = resourceBundle.getString("exceptions.getGeneratedLocationId");
    UPDATE_LOCATION_EXCEPTION = resourceBundle.getString("exceptions.updateLocation");
    DELETE_LOCATION_EXCEPTION = resourceBundle.getString("exceptions.deleteLocation");
  }

  private final Logger logger;
  private final DataSource dataSource;

  @Inject
  public LocationDAO(DataSource dataSource) {
    logger = LogManager.getLogger(LocationDAO.class);
    this.dataSource = dataSource;
  }

  @Override
  public List<LocationDTO> getAll() throws DAOException, DataSourceException {
    List<LocationDTO> allLocationDTOs = new ArrayList<>();
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(SELECT_ALL, Statement.NO_GENERATED_KEYS);

    try {
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        long id = resultSet.getLong(LocationDTO.ID_COLUMN);
        long ownerId = resultSet.getLong(LocationDTO.OWNER_ID_COLUMN);
        String address = resultSet.getObject(LocationDTO.ADDRESS_COLUMN, String.class);
        Double latitude = resultSet.getObject(LocationDTO.LATITUDE_COLUMN, Double.class);
        Double longitude = resultSet.getObject(LocationDTO.LONGITUDE_COLUMN, Double.class);

        LocationDTO location = new LocationDTO(id, ownerId, address, latitude, longitude);
        allLocationDTOs.add(location);
      }
    } catch (SQLException e) {
      logger.error(() -> "Cannot get all locations.", e);
      throw new DAOException(GET_ALL_LOCATION_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Got all locations.");
    return allLocationDTOs;
  }

  @Override
  public LocationDTO getByKey(@Nonnull Long id) throws DAOException, DataSourceException {
    LocationDTO locationDTO = null;
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(SELECT_BY_ID, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        long ownerId = resultSet.getLong(LocationDTO.OWNER_ID_COLUMN);
        String address = resultSet.getObject(LocationDTO.ADDRESS_COLUMN, String.class);
        Double latitude = resultSet.getObject(LocationDTO.LATITUDE_COLUMN, Double.class);
        Double longitude = resultSet.getObject(LocationDTO.LONGITUDE_COLUMN, Double.class);

        locationDTO = new LocationDTO(id, ownerId, address, latitude, longitude);
      }
    } catch (SQLException e) {
      logger.error(() -> "Cannot get location by id.", e);
      throw new DAOException(GET_LOCATION_BY_ID_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Got location by id.");
    return locationDTO;
  }

  @Override
  public LocationDTO insert(@Nonnull LocationDTO locationDTO)
      throws DAOException, DataSourceException {
    long resultId;
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, locationDTO.ownerId);
      preparedStatement.setString(2, locationDTO.address);
      preparedStatement.setDouble(3, locationDTO.latitude);
      preparedStatement.setDouble(4, locationDTO.longitude);

      preparedStatement.execute();

      ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
      if (generatedKeys.next()) {
        resultId = generatedKeys.getLong(1);
      } else {
        logger.error(() -> "Cannot get generated location id.");
        throw new DAOException(GET_GENERATED_LOCATION_ID);
      }
    } catch (SQLException e) {
      logger.error(() -> "Cannot insert location.", e);
      throw new DAOException(INSERT_LOCATION_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Location was inserted.");
    return new LocationDTO(
        resultId,
        locationDTO.ownerId,
        locationDTO.address,
        locationDTO.latitude,
        locationDTO.longitude);
  }

  @Override
  public LocationDTO update(@Nonnull LocationDTO locationDTO)
      throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(UPDATE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, locationDTO.ownerId);
      preparedStatement.setString(2, locationDTO.address);
      preparedStatement.setDouble(3, locationDTO.latitude);
      preparedStatement.setDouble(4, locationDTO.longitude);

      preparedStatement.setLong(5, locationDTO.id);

      preparedStatement.execute();
    } catch (SQLException e) {
      logger.error(() -> "Cannot update location.", e);
      throw new DAOException(UPDATE_LOCATION_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Location was updated.");
    return new LocationDTO(
        locationDTO.id,
        locationDTO.ownerId,
        locationDTO.address,
        locationDTO.latitude,
        locationDTO.longitude);
  }

  @Override
  public void delete(@Nonnull LocationDTO locationDTO) throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(DELETE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, locationDTO.id);

      preparedStatement.execute();
    } catch (SQLException e) {
      logger.error(() -> "Cannot delete location.", e);
      throw new DAOException(DELETE_LOCATION_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Location was deleted.");
  }
}
