package ru.storage.server.app.multithreading;

import ru.storage.server.app.multithreading.exceptions.ExecutorServicesException;
import ru.storage.server.app.multithreading.tasks.HandleTask;
import ru.storage.server.app.multithreading.tasks.ReadTask;
import ru.storage.server.app.multithreading.tasks.SendTask;

import java.util.concurrent.Executor;

/** Executes tasks in accordance with concrete {@link Executor}; */
public final class ExecutorServices {
  private final Executor readExecutor;
  private final Executor handleExecutor;
  private final Executor sendExecutor;

  public ExecutorServices(Executor readExecutor, Executor handleExecutor, Executor sendExecutor) {
    this.readExecutor = readExecutor;
    this.handleExecutor = handleExecutor;
    this.sendExecutor = sendExecutor;
  }

  /** Executes {@link ReadTask} using specified {@link Executor}. */
  public void read(ReadTask readTask) throws ExecutorServicesException {
    readExecutor.execute(readTask);
  }

  /** Executes {@link HandleTask} using specified {@link Executor}. */
  public void process(HandleTask handleTask) throws ExecutorServicesException {
    handleExecutor.execute(handleTask);
  }

  /** Executes {@link SendTask} using specified {@link Executor}. */
  public void send(SendTask sendTask) throws ExecutorServicesException {
    sendExecutor.execute(sendTask);
  }
}
