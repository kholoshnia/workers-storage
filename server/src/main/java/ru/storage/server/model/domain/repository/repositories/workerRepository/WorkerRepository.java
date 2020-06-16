package ru.storage.server.model.domain.repository.repositories.workerRepository;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.server.model.dao.DAO;
import ru.storage.server.model.dao.exceptions.DAOException;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.repository.Query;
import ru.storage.server.model.domain.repository.Repository;
import ru.storage.server.model.domain.repository.exceptions.RepositoryException;
import ru.storage.server.model.domain.repository.repositories.workerRepository.exceptions.WorkerRepositoryException;
import ru.storage.server.model.source.exceptions.DataSourceException;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

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
  private final DAO<Long, Worker> workerDAO;
  private final List<Worker> workers;
  private final Class<?> type;
  private final LocalDateTime initTime;
  private long size;

  @Inject
  public WorkerRepository(@Nonnull DAO<Long, Worker> workerDAO) throws WorkerRepositoryException {
    this.logger = LogManager.getLogger(WorkerRepository.class);
    this.workerDAO = workerDAO;
    this.workers = initWorkersList();
    this.initTime = LocalDateTime.now();
    this.type = workers.getClass();
    this.size = workers.size();
  }

  /** Returns type of the collection. */
  public Class<?> getType() {
    return type;
  }

  /** Returns initialization time of the collection. */
  public LocalDateTime getInitTime() {
    return initTime;
  }

  /** Returns current size of the collection. */
  public long getSize() {
    return size;
  }

  /**
   * Initializes list of workers. Loads data using DAO. If there is no data in the DAO, creates an
   * empty list.
   *
   * @return list of workers
   * @throws WorkerRepositoryException - in case of corrupted data or exceptions while loading data.
   */
  private List<Worker> initWorkersList() throws WorkerRepositoryException {
    List<Worker> workers = new CopyOnWriteArrayList<>();

    try {
      List<Worker> allWorkers = workerDAO.getAll();

      if (allWorkers.isEmpty()) {
        logger.warn(() -> "No workers were found using DAO, the collection was not filled.");
        return workers;
      }

      for (Worker worker : allWorkers) {
        workers.add(worker);
        logger.debug("Added worker from DAO: {}.", () -> worker);
      }

      return workers;
    } catch (DAOException | DataSourceException e) {
      throw new WorkerRepositoryException(e);
    }
  }

  @Override
  public synchronized List<Worker> get(@Nonnull Query<Worker> query) throws RepositoryException {
    List<Worker> result = query.execute(workers);

    logger.debug("WorkerQuery: {} was executed.", () -> query);
    return result;
  }

  @Override
  public synchronized void insert(@Nonnull Worker worker) throws WorkerRepositoryException {
    Worker result;

    try {
      result = workerDAO.insert(worker);
      logger.info("Got worker with id from DAO.");
    } catch (DAOException | DataSourceException e) {
      logger.error(() -> "Cannot add new worker to the collection.", e);
      throw new WorkerRepositoryException(e);
    }

    workers.add(result);

    size = workers.size();
    logger.info(() -> "Worker was added to the collection.");
  }

  @Override
  public synchronized void update(@Nonnull Worker worker) throws WorkerRepositoryException {
    try {
      Worker result = workerDAO.getByKey(worker.getID());

      if (result == null) {
        logger.error(
            "Cannot update worker, no such worker found using DAO, target worker: {}.",
            () -> worker);
        throw new WorkerRepositoryException(WORKER_NOT_FOUND_USING_DAO_EXCEPTION);
      }

      workerDAO.update(result);
      logger.info("Worker was updated in DAO.");
    } catch (DAOException | DataSourceException e) {
      logger.error("Cannot update worker in DAO, target worker: %s." + worker, e);
      throw new WorkerRepositoryException(e);
    }

    if (workers.remove(worker)) {
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
      Worker result = workerDAO.getByKey(worker.getID());

      if (result == null) {
        logger.error(
            "Cannot delete worker. No such worker found using DAO, target worker: {}.",
            () -> worker);
        throw new WorkerRepositoryException(WORKER_NOT_FOUND_USING_DAO_EXCEPTION);
      }

      workerDAO.delete(worker);
      logger.info("Worker was deleted from DAO.");
    } catch (DAOException | DataSourceException e) {
      logger.error(
          "Cannot delete worker using DAO, target worker: {}.", (Supplier<?>) () -> worker, e);
      throw new WorkerRepositoryException(e);
    }

    if (workers.remove(worker)) {
      logger.error(
          "Cannot delete worker, no such worker in the collection, target worker: {}.",
          () -> worker);
      throw new WorkerRepositoryException(WORKER_NOT_FOUND_IN_COLLECTION_EXCEPTION);
    }

    size = workers.size();
    logger.info(() -> "Worker was deleted from the collection.");
  }
}
