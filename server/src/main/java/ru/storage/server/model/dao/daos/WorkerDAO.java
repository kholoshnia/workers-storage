package ru.storage.server.model.dao.daos;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.server.model.dao.DAO;
import ru.storage.server.model.dao.exceptions.DAOException;
import ru.storage.server.model.domain.dto.dtos.CoordinatesDTO;
import ru.storage.server.model.domain.dto.dtos.PersonDTO;
import ru.storage.server.model.domain.dto.dtos.WorkerDTO;
import ru.storage.server.model.domain.entity.entities.worker.Status;
import ru.storage.server.model.source.DataSource;
import ru.storage.server.model.source.exceptions.DataSourceException;

import javax.annotation.Nonnull;
import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class WorkerDAO implements DAO<Long, WorkerDTO> {
  private static final String CANNOT_GET_ALL_WORKER_EXCEPTION;
  private static final String CANNOT_GET_WORKER_BY_ID_EXCEPTION;
  private static final String CANNOT_INSERT_WORKER_EXCEPTION;
  private static final String CANNOT_GET_GENERATED_WORKER_ID;
  private static final String CANNOT_UPDATE_WORKER_EXCEPTION;
  private static final String CANNOT_DELETE_WORKER_EXCEPTION;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.WorkerDAO");

    CANNOT_GET_ALL_WORKER_EXCEPTION = resourceBundle.getString("exceptions.cannotGetAllWorkers");
    CANNOT_GET_WORKER_BY_ID_EXCEPTION = resourceBundle.getString("exceptions.cannotGetWorkerById");
    CANNOT_INSERT_WORKER_EXCEPTION = resourceBundle.getString("exceptions.cannotInsertWorker");
    CANNOT_GET_GENERATED_WORKER_ID =
        resourceBundle.getString("exceptions.cannotGetGeneratedWorkerId");
    CANNOT_UPDATE_WORKER_EXCEPTION = resourceBundle.getString("exceptions.cannotUpdateWorker");
    CANNOT_DELETE_WORKER_EXCEPTION = resourceBundle.getString("exceptions.cannotDeleteWorker");
  }

  private final String SELECT_ALL = "SELECT * FROM " + WorkerDTO.TABLE_NAME;

  private final String SELECT_BY_ID = SELECT_ALL + " WHERE " + WorkerDTO.ID_COLUMN + " = ?";

  private final String INSERT =
      "INSERT INTO "
          + WorkerDTO.TABLE_NAME
          + " ("
          + WorkerDTO.OWNER_ID_COLUMN
          + ", "
          + WorkerDTO.CREATION_DATE_COLUMN
          + ", "
          + WorkerDTO.SALARY_COLUMN
          + ", "
          + WorkerDTO.STATUS_COLUMN
          + ", "
          + WorkerDTO.START_DATE_COLUMN
          + ", "
          + WorkerDTO.END_DATE_COLUMN
          + ", "
          + WorkerDTO.COORDINATES_COLUMN
          + ", "
          + WorkerDTO.PERSON_COLUMN
          + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

  private final String UPDATE =
      "UPDATE "
          + WorkerDTO.TABLE_NAME
          + " SET "
          + WorkerDTO.OWNER_ID_COLUMN
          + " = ?, "
          + WorkerDTO.CREATION_DATE_COLUMN
          + " = ?, "
          + WorkerDTO.SALARY_COLUMN
          + " = ?, "
          + WorkerDTO.STATUS_COLUMN
          + " = ?, "
          + WorkerDTO.START_DATE_COLUMN
          + " = ?, "
          + WorkerDTO.END_DATE_COLUMN
          + " = ?, "
          + WorkerDTO.COORDINATES_COLUMN
          + " = ?, "
          + WorkerDTO.PERSON_COLUMN
          + " = ? WHERE "
          + WorkerDTO.ID_COLUMN
          + " = ?";

  private final String DELETE =
      "DELETE FROM " + WorkerDTO.TABLE_NAME + " WHERE " + WorkerDTO.ID_COLUMN + " = ?";

  private final Logger logger;
  private final DataSource dataSource;
  private final DAO<Long, CoordinatesDTO> coordinatesDAO;
  private final DAO<Long, PersonDTO> personDAO;

  @Inject
  public WorkerDAO(
      DataSource dataSource,
      DAO<Long, CoordinatesDTO> coordinatesDAO,
      DAO<Long, PersonDTO> personDAO) {
    logger = LogManager.getLogger(WorkerDAO.class);
    this.dataSource = dataSource;
    this.coordinatesDAO = coordinatesDAO;
    this.personDAO = personDAO;
  }

  @Override
  public List<WorkerDTO> getAll() throws DAOException, DataSourceException {
    List<WorkerDTO> allWorkerDTOs = new ArrayList<>();
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(SELECT_ALL, Statement.NO_GENERATED_KEYS);

    try {
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        long id = resultSet.getLong(WorkerDTO.ID_COLUMN);
        long ownerId = resultSet.getLong(WorkerDTO.OWNER_ID_COLUMN);
        ZonedDateTime creationDate =
            resultSet
                .getTimestamp(WorkerDTO.CREATION_DATE_COLUMN)
                .toInstant()
                .atZone(ZoneId.systemDefault());
        Double salary = resultSet.getObject(WorkerDTO.SALARY_COLUMN, Double.class);
        Status status =
            Status.getStatus(resultSet.getObject(WorkerDTO.STATUS_COLUMN, String.class));
        ZonedDateTime startDate =
            resultSet
                .getTimestamp(WorkerDTO.START_DATE_COLUMN)
                .toInstant()
                .atZone(ZoneId.systemDefault());
        ZonedDateTime endDate =
            resultSet
                .getTimestamp(WorkerDTO.END_DATE_COLUMN)
                .toInstant()
                .atZone(ZoneId.systemDefault());

        Long coordinatesId = resultSet.getObject(WorkerDTO.COORDINATES_COLUMN, Long.class);
        CoordinatesDTO coordinatesDTO = coordinatesDAO.getByKey(coordinatesId);

        Long personId = resultSet.getObject(WorkerDTO.PERSON_COLUMN, Long.class);
        PersonDTO personDTO = personDAO.getByKey(personId);

        WorkerDTO workerDTO =
            new WorkerDTO(
                id,
                ownerId,
                creationDate,
                salary,
                status,
                startDate,
                endDate,
                coordinatesDTO,
                personDTO);
        allWorkerDTOs.add(workerDTO);
      }
    } catch (SQLException e) {
      logger.error(() -> "Cannot get all workers.", e);
      throw new DAOException(CANNOT_GET_ALL_WORKER_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Got all workers.");
    return allWorkerDTOs;
  }

  @Override
  public WorkerDTO getByKey(@Nonnull Long id) throws DAOException, DataSourceException {
    WorkerDTO workerDTO = null;
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(SELECT_BY_ID, Statement.NO_GENERATED_KEYS);

    try {
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        long ownerId = resultSet.getLong(WorkerDTO.OWNER_ID_COLUMN);
        ZonedDateTime creationDate =
            resultSet
                .getTimestamp(WorkerDTO.CREATION_DATE_COLUMN)
                .toInstant()
                .atZone(ZoneId.systemDefault());
        Double salary = resultSet.getObject(WorkerDTO.SALARY_COLUMN, Double.class);
        Status status =
            Status.getStatus(resultSet.getObject(WorkerDTO.STATUS_COLUMN, String.class));
        ZonedDateTime startDate =
            resultSet
                .getTimestamp(WorkerDTO.START_DATE_COLUMN)
                .toInstant()
                .atZone(ZoneId.systemDefault());
        ZonedDateTime endDate =
            resultSet
                .getTimestamp(WorkerDTO.END_DATE_COLUMN)
                .toInstant()
                .atZone(ZoneId.systemDefault());

        Long coordinatesId = resultSet.getObject(WorkerDTO.COORDINATES_COLUMN, Long.class);
        CoordinatesDTO coordinatesDTO = coordinatesDAO.getByKey(coordinatesId);

        Long personId = resultSet.getObject(WorkerDTO.PERSON_COLUMN, Long.class);
        PersonDTO personDTO = personDAO.getByKey(personId);

        workerDTO =
            new WorkerDTO(
                id,
                ownerId,
                creationDate,
                salary,
                status,
                startDate,
                endDate,
                coordinatesDTO,
                personDTO);
      }
    } catch (SQLException e) {
      logger.error(() -> "Cannot get worker by id.", e);
      throw new DAOException(CANNOT_GET_WORKER_BY_ID_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Got worker by id.");
    return workerDTO;
  }

  @Override
  public WorkerDTO insert(@Nonnull WorkerDTO workerDTO) throws DAOException, DataSourceException {
    long resultId;
    CoordinatesDTO coordinatesDTO;
    PersonDTO personDTO;
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, workerDTO.ownerId);
      preparedStatement.setTimestamp(2, Timestamp.from(workerDTO.creationDate.toInstant()));
      preparedStatement.setDouble(3, workerDTO.salary);
      preparedStatement.setString(4, workerDTO.status.toString());
      preparedStatement.setTimestamp(5, Timestamp.from(workerDTO.startDate.toInstant()));
      preparedStatement.setTimestamp(6, Timestamp.from(workerDTO.endDate.toInstant()));

      coordinatesDTO = coordinatesDAO.insert(workerDTO.coordinatesDTO);
      preparedStatement.setLong(6, coordinatesDTO.id);

      personDTO = personDAO.insert(workerDTO.personDTO);
      preparedStatement.setLong(6, personDTO.id);

      preparedStatement.execute();

      ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
      if (generatedKeys.next()) {
        resultId = generatedKeys.getLong(1);
      } else {
        logger.error(() -> "Cannot get generated worker id.");
        throw new DAOException(CANNOT_GET_GENERATED_WORKER_ID);
      }
    } catch (SQLException e) {
      logger.error(() -> "Cannot insert worker.", e);
      throw new DAOException(CANNOT_INSERT_WORKER_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Worker was inserted.");
    return new WorkerDTO(
        resultId,
        workerDTO.ownerId,
        workerDTO.creationDate,
        workerDTO.salary,
        workerDTO.status,
        workerDTO.startDate,
        workerDTO.endDate,
        coordinatesDTO,
        personDTO);
  }

  @Override
  public WorkerDTO update(@Nonnull WorkerDTO workerDTO) throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(UPDATE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, workerDTO.ownerId);
      preparedStatement.setTimestamp(2, Timestamp.from(workerDTO.creationDate.toInstant()));
      preparedStatement.setDouble(3, workerDTO.salary);
      preparedStatement.setString(4, workerDTO.status.toString());
      preparedStatement.setTimestamp(5, Timestamp.from(workerDTO.startDate.toInstant()));
      preparedStatement.setTimestamp(6, Timestamp.from(workerDTO.endDate.toInstant()));

      CoordinatesDTO coordinatesDTO = coordinatesDAO.insert(workerDTO.coordinatesDTO);
      preparedStatement.setLong(6, coordinatesDTO.id);

      PersonDTO personDTO = personDAO.insert(workerDTO.personDTO);
      preparedStatement.setLong(6, personDTO.id);

      preparedStatement.setLong(7, workerDTO.id);

      preparedStatement.execute();
    } catch (SQLException e) {
      logger.error(() -> "Cannot update worker.", e);
      throw new DAOException(CANNOT_UPDATE_WORKER_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Worker was updated.");
    return workerDTO;
  }

  @Override
  public void delete(@Nonnull WorkerDTO workerDTO) throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(DELETE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, workerDTO.id);

      coordinatesDAO.delete(workerDTO.coordinatesDTO);
      personDAO.delete(workerDTO.personDTO);

      preparedStatement.execute();
    } catch (SQLException e) {
      logger.error(() -> "Cannot delete worker.", e);
      throw new DAOException(CANNOT_DELETE_WORKER_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Worker was deleted.");
  }
}
