package ru.storage.server.app.concurrent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.server.app.concurrent.exceptions.ExecutorServicesException;

import java.util.concurrent.Executor;

/** Executes tasks in accordance with concrete {@link Executor}; */
public final class ExecutorService {
  private final Logger logger;
  private final Executor readExecutor;
  private final Executor handleExecutor;
  private final Executor sendExecutor;

  public ExecutorService(Executor readExecutor, Executor handleExecutor, Executor sendExecutor) {
    logger = LogManager.getLogger(ExecutorService.class);
    this.readExecutor = readExecutor;
    this.handleExecutor = handleExecutor;
    this.sendExecutor = sendExecutor;
  }

  /**
   * Executes read task using specified {@link Executor}.
   *
   * @param readTask concrete read task
   */
  public void read(Runnable readTask) throws ExecutorServicesException {
    logger.info("Executing read task...");

    try {
      readExecutor.execute(readTask);
    } catch (Exception e) {
      logger.error(() -> "Exception was caught during reading.", e);
      throw new ExecutorServicesException(e);
    }
  }

  /**
   * Executes handle task using specified {@link Executor}.
   *
   * @param handleTask concrete handle task
   */
  public void handle(Runnable handleTask) throws ExecutorServicesException {
    logger.info("Executing handle task...");

    try {
      handleExecutor.execute(handleTask);
    } catch (Exception e) {
      logger.error(() -> "Exception was caught during handling.", e);
      throw new ExecutorServicesException(e);
    }
  }

  /**
   * Executes send task using specified {@link Executor}.
   *
   * @param sendTask concrete send task
   */
  public void send(Runnable sendTask) throws ExecutorServicesException {
    logger.info("Executing send task...");

    try {
      sendExecutor.execute(sendTask);
    } catch (Exception e) {
      logger.error(() -> "Exception was caught during handling.", e);
      throw new ExecutorServicesException(e);
    }
  }
}
