package ru.storage.server.model.domain.repository.repositories.workerRepository;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.server.model.dao.DAO;
import ru.storage.server.model.dao.exceptions.DAOException;
import ru.storage.server.model.domain.dto.dtos.WorkerDTO;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.entity.exceptions.ValidationException;
import ru.storage.server.model.domain.repository.Query;
import ru.storage.server.model.domain.repository.Repository;
import ru.storage.server.model.domain.repository.exceptions.RepositoryException;
import ru.storage.server.model.domain.repository.repositories.workerRepository.exceptions.WorkerRepositoryException;
import ru.storage.server.model.source.exceptions.DataSourceException;

import javax.annotation.Nonnull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

/**
 * Worker repository class contains workers from the database. Can be used to update data in the
 * database if necessary. All main methods are synchronized in case of concurrent use.
 *
 * @see Repository
 */
public final class WorkerRepository implements Repository<Worker> {
  private static final String WORKER_NOT_FOUND_USING_DAO_EXCEPTION;
  private static final String WORKER_NOT_FOUND_IN_COLLECTION_EXCEPTION;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.WorkerRepository");

    WORKER_NOT_FOUND_USING_DAO_EXCEPTION =
        resourceBundle.getString("exceptions.workerNotFoundUsingDAO");
    WORKER_NOT_FOUND_IN_COLLECTION_EXCEPTION =
        resourceBundle.getString("exceptions.workerNotFoundInCollection");
  }

  private final Logger logger;
  private final DAO<Long, WorkerDTO> workerDAO;
  private final List<Worker> workers;
  private final Class<?> type;
  private final ZonedDateTime initTime;
  private long size;

  @Inject
  public WorkerRepository(@Nonnull DAO<Long, WorkerDTO> workerDAO)
      throws WorkerRepositoryException {
    logger = LogManager.getLogger(WorkerRepository.class);
    this.workerDAO = workerDAO;
    workers = initWorkersList();
    initTime = ZonedDateTime.now();
    type = workers.getClass();
    size = workers.size();
  }

  /**
   * Returns type of the collection.
   *
   * @return type of the collection
   */
  public Class<?> getType() {
    return type;
  }

  /**
   * Returns initialization time of the collection.
   *
   * @return initialization time of the collection
   */
  public ZonedDateTime getInitTime() {
    return initTime;
  }

  /**
   * Returns current size of the collection.
   *
   * @return current size of the collection
   */
  public long getSize() {
    return size;
  }

  /**
   * Initializes list of workers. Loads data using DAO. If there is no data in the DAO, creates an
   * empty list.
   *
   * @return list of workers
   * @throws WorkerRepositoryException - in case of data loading or validation exceptions.
   */
  private List<Worker> initWorkersList() throws WorkerRepositoryException {
    List<Worker> workers = new CopyOnWriteArrayList<>();
    List<WorkerDTO> allWorkerDTOs;

    try {
      allWorkerDTOs = workerDAO.getAll();
    } catch (DAOException | DataSourceException e) {
      throw new WorkerRepositoryException(e);
    }

    if (allWorkerDTOs.isEmpty()) {
      logger.warn(() -> "No workers were found using DAO, the collection was not filled.");
      return workers;
    }

    try {
      for (WorkerDTO workerDTO : allWorkerDTOs) {
        workers.add(workerDTO.toEntity());
        logger.debug("Added worker from DAO: {}.", () -> workerDTO);
      }
    } catch (ValidationException e) {
      logger.error(() -> "Validation error was caught during creating worker entity.", e);
      throw new WorkerRepositoryException(e);
    }

    return workers;
  }

  @Override
  public synchronized List<Worker> get(@Nonnull Query<Worker> query) throws RepositoryException {
    List<Worker> result = query.execute(workers);

    logger.debug("Worker query: {} was executed.", () -> query);
    return result;
  }

  @Override
  public synchronized void insert(@Nonnull Worker worker) throws WorkerRepositoryException {
    WorkerDTO result;

    try {
      result = workerDAO.insert(worker.toDTO());
      logger.info(() -> "Got worker with id from DAO.");
    } catch (DAOException | DataSourceException e) {
      logger.error(() -> "Cannot add new worker to the collection.", e);
      throw new WorkerRepositoryException(e);
    }

    try {
      workers.add(result.toEntity());
    } catch (ValidationException e) {
      logger.error(() -> "Validation error was caught during creating worker entity.", e);
      throw new WorkerRepositoryException(e);
    }

    size = workers.size();
    logger.info(() -> "Worker was added to the collection.");
  }

  @Override
  public synchronized void update(@Nonnull Worker worker) throws WorkerRepositoryException {
    Worker found;

    try {
      WorkerDTO result = workerDAO.getByKey(worker.getId());

      if (result == null) {
        logger.error(
            "Cannot update worker, no such worker found using DAO, target worker: {}.",
            () -> worker);
        throw new WorkerRepositoryException(WORKER_NOT_FOUND_USING_DAO_EXCEPTION);
      }

      workerDAO.update(worker.toDTO());
      found = result.toEntity();
      logger.info(() -> "Worker was updated in DAO.");
    } catch (DAOException | DataSourceException | ValidationException e) {
      logger.error(
          "Cannot update worker in DAO, target worker: {}.", (Supplier<?>) () -> worker, e);
      throw new WorkerRepositoryException(e);
    }

    if (!workers.remove(found)) {
      logger.error(
          "Cannot delete worker, no such worker in the collection, target worker: {}.",
          () -> worker);
      throw new WorkerRepositoryException(WORKER_NOT_FOUND_IN_COLLECTION_EXCEPTION);
    }

    workers.add(worker);

    logger.info(() -> "Worker was updated in the collection.");
  }

  @Override
  public synchronized void delete(@Nonnull Worker worker) throws WorkerRepositoryException {
    try {
      WorkerDTO result = workerDAO.getByKey(worker.getId());

      if (result == null) {
        logger.error(
            "Cannot delete worker. No such worker found using DAO, target worker: {}.",
            () -> worker);
        throw new WorkerRepositoryException(WORKER_NOT_FOUND_USING_DAO_EXCEPTION);
      }

      workerDAO.delete(worker.toDTO());
      logger.info("Worker was deleted from DAO.");
    } catch (DAOException | DataSourceException e) {
      logger.error(
          "Cannot delete worker using DAO, target worker: {}.", (Supplier<?>) () -> worker, e);
      throw new WorkerRepositoryException(e);
    }

    if (!workers.remove(worker)) {
      logger.error(
          "Cannot delete worker, no such worker in the collection, target worker: {}.",
          () -> worker);
      throw new WorkerRepositoryException(WORKER_NOT_FOUND_IN_COLLECTION_EXCEPTION);
    }

    size = workers.size();
    logger.info(() -> "Worker was deleted from the collection.");
  }
}
