package ru.storage.server.model.dao.daos;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.server.model.dao.DAO;
import ru.storage.server.model.dao.adapter.Adapter;
import ru.storage.server.model.dao.adapter.exceptions.AdapterException;
import ru.storage.server.model.dao.exceptions.DAOException;
import ru.storage.server.model.domain.dto.dtos.CoordinatesDTO;
import ru.storage.server.model.domain.dto.dtos.PersonDTO;
import ru.storage.server.model.domain.dto.dtos.WorkerDTO;
import ru.storage.server.model.domain.entity.entities.worker.Status;
import ru.storage.server.model.source.DataSource;
import ru.storage.server.model.source.exceptions.DataSourceException;

import javax.annotation.Nonnull;
import java.sql.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class WorkerDAO implements DAO<Long, WorkerDTO> {
  private static final String SELECT_ALL = "SELECT * FROM " + WorkerDTO.TABLE_NAME;

  private static final String SELECT_BY_ID = SELECT_ALL + " WHERE " + WorkerDTO.ID_COLUMN + " = ?";

  private static final String INSERT =
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

  private static final String UPDATE =
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

  private static final String DELETE =
      "DELETE FROM " + WorkerDTO.TABLE_NAME + " WHERE " + WorkerDTO.ID_COLUMN + " = ?";

  private static final String GET_ALL_WORKER_EXCEPTION;
  private static final String GET_WORKER_BY_ID_EXCEPTION;
  private static final String INSERT_WORKER_EXCEPTION;
  private static final String GET_GENERATED_WORKER_ID;
  private static final String UPDATE_WORKER_EXCEPTION;
  private static final String DELETE_WORKER_EXCEPTION;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.WorkerDAO");

    GET_ALL_WORKER_EXCEPTION = resourceBundle.getString("exceptions.getAllWorkers");
    GET_WORKER_BY_ID_EXCEPTION = resourceBundle.getString("exceptions.getWorkerById");
    INSERT_WORKER_EXCEPTION = resourceBundle.getString("exceptions.insertWorker");
    GET_GENERATED_WORKER_ID = resourceBundle.getString("exceptions.getGeneratedWorkerId");
    UPDATE_WORKER_EXCEPTION = resourceBundle.getString("exceptions.updateWorker");
    DELETE_WORKER_EXCEPTION = resourceBundle.getString("exceptions.deleteWorker");
  }

  private final Logger logger;
  private final DataSource dataSource;
  private final Adapter<Status, String> statusAdapter;
  private final Adapter<ZonedDateTime, Timestamp> zonedDateTimeAdapter;
  private final DAO<Long, CoordinatesDTO> coordinatesDAO;
  private final DAO<Long, PersonDTO> personDAO;

  @Inject
  public WorkerDAO(
      DataSource dataSource,
      Adapter<Status, String> statusAdapter,
      Adapter<ZonedDateTime, Timestamp> zonedDateTimeAdapter,
      DAO<Long, CoordinatesDTO> coordinatesDAO,
      DAO<Long, PersonDTO> personDAO) {
    logger = LogManager.getLogger(WorkerDAO.class);
    this.dataSource = dataSource;
    this.statusAdapter = statusAdapter;
    this.zonedDateTimeAdapter = zonedDateTimeAdapter;
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
            zonedDateTimeAdapter.from(resultSet.getTimestamp(WorkerDTO.CREATION_DATE_COLUMN));
        Float salary = resultSet.getObject(WorkerDTO.SALARY_COLUMN, Float.class);
        Status status =
            statusAdapter.from(resultSet.getObject(WorkerDTO.STATUS_COLUMN, String.class));
        ZonedDateTime startDate =
            zonedDateTimeAdapter.from(resultSet.getTimestamp(WorkerDTO.START_DATE_COLUMN));
        ZonedDateTime endDate =
            zonedDateTimeAdapter.from(resultSet.getTimestamp(WorkerDTO.END_DATE_COLUMN));

        long coordinatesId = resultSet.getLong(WorkerDTO.COORDINATES_COLUMN);
        CoordinatesDTO coordinatesDTO = coordinatesDAO.getByKey(coordinatesId);

        long personId = resultSet.getLong(WorkerDTO.PERSON_COLUMN);
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
    } catch (SQLException | AdapterException e) {
      logger.error(() -> "Cannot get all workers.", e);
      throw new DAOException(GET_ALL_WORKER_EXCEPTION, e);
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
      preparedStatement.setLong(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        long ownerId = resultSet.getLong(WorkerDTO.OWNER_ID_COLUMN);
        ZonedDateTime creationDate =
            zonedDateTimeAdapter.from(resultSet.getTimestamp(WorkerDTO.CREATION_DATE_COLUMN));
        Float salary = resultSet.getObject(WorkerDTO.SALARY_COLUMN, Float.class);
        Status status =
            statusAdapter.from(resultSet.getObject(WorkerDTO.STATUS_COLUMN, String.class));
        ZonedDateTime startDate =
            zonedDateTimeAdapter.from(resultSet.getTimestamp(WorkerDTO.START_DATE_COLUMN));
        ZonedDateTime endDate =
            zonedDateTimeAdapter.from(resultSet.getTimestamp(WorkerDTO.END_DATE_COLUMN));

        long coordinatesId = resultSet.getLong(WorkerDTO.COORDINATES_COLUMN);
        CoordinatesDTO coordinatesDTO = coordinatesDAO.getByKey(coordinatesId);

        long personId = resultSet.getLong(WorkerDTO.PERSON_COLUMN);
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
    } catch (SQLException | AdapterException e) {
      logger.error(() -> "Cannot get worker by id.", e);
      throw new DAOException(GET_WORKER_BY_ID_EXCEPTION, e);
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
      preparedStatement.setTimestamp(2, zonedDateTimeAdapter.to(workerDTO.creationDate));
      preparedStatement.setDouble(3, workerDTO.salary);
      preparedStatement.setString(4, statusAdapter.to(workerDTO.status));
      preparedStatement.setTimestamp(5, zonedDateTimeAdapter.to(workerDTO.startDate));
      preparedStatement.setTimestamp(6, zonedDateTimeAdapter.to(workerDTO.endDate));

      if (workerDTO.coordinatesDTO != null) {
        coordinatesDTO = coordinatesDAO.insert(workerDTO.coordinatesDTO);
        preparedStatement.setLong(7, coordinatesDTO.id);
      } else {
        coordinatesDTO = null;
        preparedStatement.setNull(7, Types.INTEGER);
      }

      if (workerDTO.personDTO != null) {
        personDTO = personDAO.insert(workerDTO.personDTO);
        preparedStatement.setLong(8, personDTO.id);
      } else {
        personDTO = null;
        preparedStatement.setNull(8, Types.INTEGER);
      }

      preparedStatement.execute();

      ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
      if (generatedKeys.next()) {
        resultId = generatedKeys.getLong(1);
      } else {
        logger.error(() -> "Cannot get generated worker id.");
        throw new DAOException(GET_GENERATED_WORKER_ID);
      }
    } catch (SQLException | AdapterException e) {
      logger.error(() -> "Cannot insert worker.", e);
      throw new DAOException(INSERT_WORKER_EXCEPTION, e);
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
    WorkerDTO previous = getByKey(workerDTO.id);
    CoordinatesDTO coordinatesDTO;
    PersonDTO personDTO;
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(UPDATE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, workerDTO.ownerId);
      preparedStatement.setTimestamp(2, zonedDateTimeAdapter.to(workerDTO.creationDate));
      preparedStatement.setDouble(3, workerDTO.salary);
      preparedStatement.setString(4, statusAdapter.to(workerDTO.status));
      preparedStatement.setTimestamp(5, zonedDateTimeAdapter.to(workerDTO.startDate));
      preparedStatement.setTimestamp(6, zonedDateTimeAdapter.to(workerDTO.endDate));

      if (previous.coordinatesDTO == null && workerDTO.coordinatesDTO != null) {
        coordinatesDTO = coordinatesDAO.insert(workerDTO.coordinatesDTO);
        preparedStatement.setLong(7, coordinatesDTO.id);
      } else if (previous.coordinatesDTO != null && workerDTO.coordinatesDTO == null) {
        coordinatesDTO = null;
        preparedStatement.setNull(7, Types.INTEGER);
      } else if (previous.coordinatesDTO != null) {
        coordinatesDTO = coordinatesDAO.update(workerDTO.coordinatesDTO);
        preparedStatement.setLong(7, coordinatesDTO.id);
      } else {
        coordinatesDTO = null;
        preparedStatement.setNull(7, Types.INTEGER);
      }

      if (previous.personDTO == null && workerDTO.personDTO != null) {
        personDTO = personDAO.insert(workerDTO.personDTO);
        preparedStatement.setLong(8, personDTO.id);
      } else if (previous.personDTO != null && workerDTO.personDTO == null) {
        personDTO = null;
        preparedStatement.setNull(8, Types.INTEGER);
      } else if (previous.personDTO != null) {
        personDTO = personDAO.update(workerDTO.personDTO);
        preparedStatement.setLong(8, personDTO.id);
      } else {
        personDTO = null;
        preparedStatement.setNull(8, Types.INTEGER);
      }

      preparedStatement.setLong(9, workerDTO.id);

      preparedStatement.execute();

      if (previous.coordinatesDTO != null && workerDTO.coordinatesDTO == null) {
        coordinatesDAO.delete(previous.coordinatesDTO);
      }

      if (previous.personDTO != null && workerDTO.personDTO == null) {
        personDAO.delete(previous.personDTO);
      }
    } catch (SQLException | AdapterException e) {
      logger.error(() -> "Cannot update worker.", e);
      throw new DAOException(UPDATE_WORKER_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Worker was updated.");
    return new WorkerDTO(
        workerDTO.id,
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
  public void delete(@Nonnull WorkerDTO workerDTO) throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(DELETE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, workerDTO.id);

      preparedStatement.execute();

      if (workerDTO.coordinatesDTO != null) {
        coordinatesDAO.delete(workerDTO.coordinatesDTO);
      }

      if (workerDTO.personDTO != null) {
        personDAO.delete(workerDTO.personDTO);
      }

    } catch (SQLException e) {
      logger.error(() -> "Cannot delete worker.", e);
      throw new DAOException(DELETE_WORKER_EXCEPTION, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Worker was deleted.");
  }
}
