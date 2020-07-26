package ru.storage.server.model.dao.daos;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.server.model.dao.DAO;
import ru.storage.server.model.dao.exceptions.DAOException;
import ru.storage.server.model.domain.dto.dtos.CoordinatesDTO;
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

public class CoordinatesDAO implements DAO<Long, CoordinatesDTO> {
  private static final String SELECT_ALL = "SELECT * FROM " + CoordinatesDTO.TABLE_NAME;

  private static final String SELECT_BY_ID =
      SELECT_ALL + " WHERE " + CoordinatesDTO.ID_COLUMN + " = ?";

  private static final String INSERT =
      "INSERT INTO "
          + CoordinatesDTO.TABLE_NAME
          + " ("
          + CoordinatesDTO.OWNER_ID_COLUMN
          + ", "
          + CoordinatesDTO.X_COLUMN
          + ", "
          + CoordinatesDTO.Y_COLUMN
          + ", "
          + CoordinatesDTO.Z_COLUMN
          + ") VALUES (?, ?, ?, ?)";

  private static final String UPDATE =
      "UPDATE "
          + CoordinatesDTO.TABLE_NAME
          + " SET "
          + CoordinatesDTO.OWNER_ID_COLUMN
          + " = ?, "
          + CoordinatesDTO.X_COLUMN
          + " = ?, "
          + CoordinatesDTO.Y_COLUMN
          + " = ?, "
          + CoordinatesDTO.Z_COLUMN
          + " = ? WHERE "
          + CoordinatesDTO.ID_COLUMN
          + " = ?";

  private static final String DELETE =
      "DELETE FROM " + CoordinatesDTO.TABLE_NAME + " WHERE " + CoordinatesDTO.ID_COLUMN + " = ?";

  private static final String GET_ALL_COORDINATES_EXCEPTION;
  private static final String GET_COORDINATES_BY_ID_EXCEPTION;
  private static final String INSERT_COORDINATES_EXCEPTION;
  private static final String GET_GENERATED_COORDINATES_ID;
  private static final String UPDATE_COORDINATES_EXCEPTION;
  private static final String DELETE_COORDINATES_EXCEPTION;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.CoordinatesDAO");

    GET_ALL_COORDINATES_EXCEPTION = resourceBundle.getString("exceptions.getAllCoordinates");
    GET_COORDINATES_BY_ID_EXCEPTION = resourceBundle.getString("exceptions.getCoordinatesById");
    INSERT_COORDINATES_EXCEPTION = resourceBundle.getString("exceptions.insertCoordinates");
    GET_GENERATED_COORDINATES_ID = resourceBundle.getString("exceptions.getGeneratedCoordinatesId");
    UPDATE_COORDINATES_EXCEPTION = resourceBundle.getString("exceptions.updateCoordinates");
    DELETE_COORDINATES_EXCEPTION = resourceBundle.getString("exceptions.deleteCoordinates");
  }

  private final Logger logger;
  private final DataSource dataSource;

  @Inject
  public CoordinatesDAO(DataSource dataSource) {
    logger = LogManager.getLogger(CoordinatesDAO.class);
    this.dataSource = dataSource;
  }

  @Override
  public List<CoordinatesDTO> getAll() throws DAOException, DataSourceException {
    List<CoordinatesDTO> allCoordinateDTOs = new ArrayList<>();
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(SELECT_ALL, Statement.NO_GENERATED_KEYS);

    try {
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        long id = resultSet.getLong(CoordinatesDTO.ID_COLUMN);
        long ownerId = resultSet.getLong(CoordinatesDTO.OWNER_ID_COLUMN);
        Double x = resultSet.getObject(CoordinatesDTO.X_COLUMN, Double.class);
        Double y = resultSet.getObject(CoordinatesDTO.Y_COLUMN, Double.class);
        Double z = resultSet.getObject(CoordinatesDTO.Z_COLUMN, Double.class);

        CoordinatesDTO coordinatesDTO = new CoordinatesDTO(id, ownerId, x, y, z);
        allCoordinateDTOs.add(coordinatesDTO);
      }
    } catch (SQLException e) {
      logger.error(() -> "Cannot get all coordinates.", e);
      throw new DAOException(GET_ALL_COORDINATES_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Got all coordinates.");
    return allCoordinateDTOs;
  }

  @Override
  public CoordinatesDTO getByKey(@Nonnull Long id) throws DAOException, DataSourceException {
    CoordinatesDTO coordinatesDTO = null;
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(SELECT_BY_ID, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        long ownerId = resultSet.getLong(CoordinatesDTO.OWNER_ID_COLUMN);
        Double x = resultSet.getObject(CoordinatesDTO.X_COLUMN, Double.class);
        Double y = resultSet.getObject(CoordinatesDTO.Y_COLUMN, Double.class);
        Double z = resultSet.getObject(CoordinatesDTO.Z_COLUMN, Double.class);

        coordinatesDTO = new CoordinatesDTO(id, ownerId, x, y, z);
      }
    } catch (SQLException e) {
      logger.error(() -> "Cannot get coordinates by id.", e);
      throw new DAOException(GET_COORDINATES_BY_ID_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Got coordinates by id.");
    return coordinatesDTO;
  }

  @Override
  public CoordinatesDTO insert(@Nonnull CoordinatesDTO coordinatesDTO)
      throws DAOException, DataSourceException {
    long resultId;
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

    try {
      preparedStatement.setDouble(1, coordinatesDTO.ownerId);
      preparedStatement.setDouble(2, coordinatesDTO.x);
      preparedStatement.setDouble(3, coordinatesDTO.y);
      preparedStatement.setDouble(4, coordinatesDTO.z);

      preparedStatement.execute();

      ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
      if (generatedKeys.next()) {
        resultId = generatedKeys.getLong(1);
      } else {
        logger.error(() -> "Cannot get generated coordinates id.");
        throw new DAOException(GET_GENERATED_COORDINATES_ID);
      }
    } catch (SQLException e) {
      logger.error(() -> "Cannot insert coordinates.", e);
      throw new DAOException(INSERT_COORDINATES_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Coordinates were inserted.");
    return new CoordinatesDTO(
        resultId, coordinatesDTO.ownerId, coordinatesDTO.x, coordinatesDTO.y, coordinatesDTO.z);
  }

  @Override
  public CoordinatesDTO update(@Nonnull CoordinatesDTO coordinatesDTO)
      throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(UPDATE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setDouble(1, coordinatesDTO.ownerId);
      preparedStatement.setDouble(2, coordinatesDTO.x);
      preparedStatement.setDouble(3, coordinatesDTO.y);
      preparedStatement.setDouble(4, coordinatesDTO.z);

      preparedStatement.setLong(5, coordinatesDTO.id);

      preparedStatement.execute();
    } catch (SQLException e) {
      logger.error(() -> "Cannot update coordinates.", e);
      throw new DAOException(UPDATE_COORDINATES_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Coordinates were updated.");
    return new CoordinatesDTO(
        coordinatesDTO.id,
        coordinatesDTO.ownerId,
        coordinatesDTO.x,
        coordinatesDTO.y,
        coordinatesDTO.z);
  }

  @Override
  public void delete(@Nonnull CoordinatesDTO coordinatesDTO)
      throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(DELETE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, coordinatesDTO.id);

      preparedStatement.execute();
    } catch (SQLException e) {
      logger.error(() -> "Cannot delete coordinates.", e);
      throw new DAOException(DELETE_COORDINATES_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Coordinates were deleted.");
  }
}
