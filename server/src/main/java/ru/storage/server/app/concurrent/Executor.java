package ru.storage.server.app.concurrent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.server.app.concurrent.exceptions.ExecutorServicesException;

/** Executes tasks in accordance with concrete {@link java.util.concurrent.Executor}; */
public final class Executor {
  private final Logger logger;
  private final java.util.concurrent.Executor readExecutor;
  private final java.util.concurrent.Executor handleExecutor;
  private final java.util.concurrent.Executor sendExecutor;

  public Executor(java.util.concurrent.Executor readExecutor, java.util.concurrent.Executor handleExecutor, java.util.concurrent.Executor sendExecutor) {
    this.logger = LogManager.getLogger(Executor.class);
    this.readExecutor = readExecutor;
    this.handleExecutor = handleExecutor;
    this.sendExecutor = sendExecutor;
  }

  /** Executes read task using specified {@link java.util.concurrent.Executor}. */
  public void read(Runnable readTask) throws ExecutorServicesException {
    logger.info("Executing read task...");
    readExecutor.execute(readTask);
  }

  /** Executes handle task using specified {@link java.util.concurrent.Executor}. */
  public void handle(Runnable handleTask) throws ExecutorServicesException {
    logger.info("Executing handle task...");
    handleExecutor.execute(handleTask);
  }

  /** Executes send task using specified {@link java.util.concurrent.Executor}. */
  public void send(Runnable sendTask) throws ExecutorServicesException {
    logger.info("Executing send task...");
    sendExecutor.execute(sendTask);
  }
}
