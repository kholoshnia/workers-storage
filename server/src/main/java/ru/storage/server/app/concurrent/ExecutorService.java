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
    this.logger = LogManager.getLogger(ExecutorService.class);
    this.readExecutor = readExecutor;
    this.handleExecutor = handleExecutor;
    this.sendExecutor = sendExecutor;
  }

  /** Executes read task using specified {@link Executor}. */
  public void read(Runnable readTask) throws ExecutorServicesException {
    logger.info("Executing read task...");
    readExecutor.execute(readTask);
  }

  /** Executes handle task using specified {@link Executor}. */
  public void handle(Runnable handleTask) throws ExecutorServicesException {
    logger.info("Executing handle task...");
    handleExecutor.execute(handleTask);
  }

  /** Executes send task using specified {@link Executor}. */
  public void send(Runnable sendTask) throws ExecutorServicesException {
    logger.info("Executing send task...");
    sendExecutor.execute(sendTask);
  }
}
