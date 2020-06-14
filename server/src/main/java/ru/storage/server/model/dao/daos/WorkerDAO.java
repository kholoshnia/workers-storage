package ru.storage.server.model.dao.daos;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.server.model.dao.DAO;
import ru.storage.server.model.dao.exceptions.DAOException;
import ru.storage.server.model.domain.dto.exceptions.ValidationException;
import ru.storage.server.model.domain.entity.entities.worker.Coordinates;
import ru.storage.server.model.domain.entity.entities.worker.Status;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.entity.entities.worker.person.Person;
import ru.storage.server.model.source.DataSource;
import ru.storage.server.model.source.exceptions.DataSourceException;

import javax.annotation.Nonnull;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class WorkerDAO implements DAO<Long, Worker> {
  private static final String CANNOT_GET_ALL_WORKER_EXCEPTION_MESSAGE;
  private static final String CANNOT_GET_WORKER_BY_ID_EXCEPTION_MESSAGE;
  private static final String CANNOT_INSERT_WORKER_EXCEPTION_MESSAGE;
  private static final String CANNOT_UPDATE_WORKER_EXCEPTION_MESSAGE;
  private static final String CANNOT_DELETE_WORKER_EXCEPTION_MESSAGE;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.WorkerDAO");

    CANNOT_GET_ALL_WORKER_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.cannotGetAllWorkers");
    CANNOT_GET_WORKER_BY_ID_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.cannotGetWorkerById");
    CANNOT_INSERT_WORKER_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.cannotInsertWorker");
    CANNOT_UPDATE_WORKER_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.cannotUpdateWorker");
    CANNOT_DELETE_WORKER_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.cannotDeleteWorker");
  }

  private final String SELECT_ALL = "SELECT * FROM " + Worker.TABLE_NAME;

  private final String SELECT_BY_ID = SELECT_ALL + " WHERE " + Worker.ID_COLUMN + " = ?";

  private final String INSERT =
      "INSERT INTO "
          + Worker.TABLE_NAME
          + " ("
          + Worker.OWNER_ID_COLUMN
          + ", "
          + Worker.CREATION_DATE_COLUMN
          + ", "
          + Worker.SALARY_COLUMN
          + ", "
          + Worker.STATUS_COLUMN
          + ", "
          + Worker.START_DATE_COLUMN
          + ", "
          + Worker.END_DATE_COLUMN
          + ", "
          + Worker.COORDINATES_COLUMN
          + ", "
          + Worker.PERSON_COLUMN
          + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

  private final String UPDATE =
      "UPDATE "
          + Worker.TABLE_NAME
          + " SET "
          + Worker.OWNER_ID_COLUMN
          + " = ?, "
          + Worker.CREATION_DATE_COLUMN
          + " = ?, "
          + Worker.SALARY_COLUMN
          + " = ?, "
          + Worker.STATUS_COLUMN
          + " = ?, "
          + Worker.START_DATE_COLUMN
          + " = ?, "
          + Worker.END_DATE_COLUMN
          + " = ?, "
          + Worker.COORDINATES_COLUMN
          + " = ?, "
          + Worker.PERSON_COLUMN
          + " = ? WHERE "
          + Worker.ID_COLUMN
          + " = ?";

  private final String DELETE =
      "DELETE FROM " + Worker.TABLE_NAME + " WHERE " + Worker.ID_COLUMN + " = ?";

  private final Logger logger;
  private final DataSource dataSource;
  private final DAO<Long, Coordinates> coordinatesDAO;
  private final DAO<Long, Person> personDAO;

  public WorkerDAO(
      DataSource dataSource, DAO<Long, Coordinates> coordinatesDAO, DAO<Long, Person> personDAO) {
    this.logger = LogManager.getLogger(WorkerDAO.class);
    this.dataSource = dataSource;
    this.coordinatesDAO = coordinatesDAO;
    this.personDAO = personDAO;
  }

  @Override
  public List<Worker> getAll() throws DAOException, DataSourceException {
    List<Worker> allWorkers = new ArrayList<>();
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(SELECT_ALL, Statement.NO_GENERATED_KEYS);

    try {
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        Long id = resultSet.getLong(Worker.ID_COLUMN);
        Long ownerId = resultSet.getLong(Worker.OWNER_ID_COLUMN);
        LocalDateTime creationDate =
            resultSet.getTimestamp(Worker.CREATION_DATE_COLUMN).toLocalDateTime();
        Double salary = resultSet.getDouble(Worker.SALARY_COLUMN);
        Status status = Status.getStatus(resultSet.getString(Worker.STATUS_COLUMN));
        LocalDateTime startDate =
            resultSet.getTimestamp(Worker.START_DATE_COLUMN).toLocalDateTime();
        LocalDateTime endDate = resultSet.getTimestamp(Worker.END_DATE_COLUMN).toLocalDateTime();
        Coordinates coordinates =
            coordinatesDAO.getByKey(resultSet.getLong(Worker.COORDINATES_COLUMN));
        Person person = personDAO.getByKey(resultSet.getLong(Worker.PERSON_COLUMN));

        Worker worker =
            new Worker(
                id, ownerId, creationDate, salary, status, startDate, endDate, coordinates, person);
        allWorkers.add(worker);
      }
    } catch (SQLException | ValidationException e) {
      logger.error(() -> "Cannot get all workers.", e);
      throw new DAOException(CANNOT_GET_ALL_WORKER_EXCEPTION_MESSAGE, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Got all workers.");
    return allWorkers;
  }

  @Override
  public Worker getByKey(@Nonnull Long id) throws DAOException, DataSourceException {
    Worker worker = null;
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(SELECT_BY_ID, Statement.NO_GENERATED_KEYS);

    try {
      ResultSet resultSet = preparedStatement.executeQuery();

      while (resultSet.next()) {
        Long ownerId = resultSet.getLong(Worker.OWNER_ID_COLUMN);
        LocalDateTime creationDate =
            resultSet.getTimestamp(Worker.CREATION_DATE_COLUMN).toLocalDateTime();
        Double salary = resultSet.getDouble(Worker.SALARY_COLUMN);
        Status status = Status.getStatus(resultSet.getString(Worker.STATUS_COLUMN));
        LocalDateTime startDate =
            resultSet.getTimestamp(Worker.START_DATE_COLUMN).toLocalDateTime();
        LocalDateTime endDate = resultSet.getTimestamp(Worker.END_DATE_COLUMN).toLocalDateTime();
        Coordinates coordinates =
            coordinatesDAO.getByKey(resultSet.getLong(Worker.COORDINATES_COLUMN));
        Person person = personDAO.getByKey(resultSet.getLong(Worker.PERSON_COLUMN));

        worker =
            new Worker(
                id, ownerId, creationDate, salary, status, startDate, endDate, coordinates, person);
      }
    } catch (SQLException | ValidationException e) {
      logger.error(() -> "Cannot get worker by id.", e);
      throw new DAOException(CANNOT_GET_WORKER_BY_ID_EXCEPTION_MESSAGE, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Got worker by id.");
    return worker;
  }

  @Override
  public Worker insert(@Nonnull Worker worker) throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, worker.getOwnerID());
      preparedStatement.setTimestamp(2, Timestamp.valueOf(worker.getCreationDate()));
      preparedStatement.setDouble(3, worker.getSalary());
      preparedStatement.setString(4, worker.getStatus().toString());
      preparedStatement.setTimestamp(5, Timestamp.valueOf(worker.getStartDate()));
      preparedStatement.setTimestamp(6, Timestamp.valueOf(worker.getEndDate()));

      Coordinates coordinates = coordinatesDAO.insert(worker.getCoordinates());
      worker.setCoordinates(coordinates);
      preparedStatement.setLong(6, worker.getCoordinates().getID());

      Person person = personDAO.insert(worker.getPerson());
      worker.setPerson(person);
      preparedStatement.setLong(6, worker.getPerson().getID());

      preparedStatement.execute();

      ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
      if (generatedKeys.next()) {
        worker.setID(generatedKeys.getLong(1));
      }
    } catch (SQLException | ValidationException e) {
      logger.error(() -> "Cannot insert worker.", e);
      throw new DAOException(CANNOT_INSERT_WORKER_EXCEPTION_MESSAGE, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Worker was inserted.");
    return worker;
  }

  @Override
  public Worker update(@Nonnull Worker worker) throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(UPDATE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, worker.getOwnerID());
      preparedStatement.setTimestamp(2, Timestamp.valueOf(worker.getCreationDate()));
      preparedStatement.setDouble(3, worker.getSalary());
      preparedStatement.setString(4, worker.getStatus().toString());
      preparedStatement.setTimestamp(5, Timestamp.valueOf(worker.getStartDate()));
      preparedStatement.setTimestamp(6, Timestamp.valueOf(worker.getEndDate()));

      Coordinates coordinates = coordinatesDAO.update(worker.getCoordinates());
      worker.setCoordinates(coordinates);
      preparedStatement.setLong(6, worker.getCoordinates().getID());

      Person person = personDAO.update(worker.getPerson());
      worker.setPerson(person);
      preparedStatement.setLong(6, worker.getPerson().getID());

      preparedStatement.setLong(4, worker.getID());

      preparedStatement.execute();
    } catch (SQLException | ValidationException e) {
      logger.error(() -> "Cannot update worker.", e);
      throw new DAOException(CANNOT_UPDATE_WORKER_EXCEPTION_MESSAGE, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Worker was updated.");
    return worker;
  }

  @Override
  public void delete(@Nonnull Worker worker) throws DAOException, DataSourceException {
    PreparedStatement preparedStatement =
        dataSource.getPrepareStatement(DELETE, Statement.NO_GENERATED_KEYS);

    try {
      preparedStatement.setLong(1, worker.getID());

      coordinatesDAO.delete(worker.getCoordinates());
      personDAO.delete(worker.getPerson());

      preparedStatement.execute();
    } catch (SQLException e) {
      logger.error(() -> "Cannot delete worker.", e);
      throw new DAOException(CANNOT_DELETE_WORKER_EXCEPTION_MESSAGE, e);
    } finally {
      dataSource.closePrepareStatement(preparedStatement);
    }

    logger.info(() -> "Worker was deleted.");
  }
}
