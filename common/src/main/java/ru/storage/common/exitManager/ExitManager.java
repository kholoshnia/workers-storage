package ru.storage.common.exitManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.exitManager.exceptions.ExitingException;

import java.util.List;

/** Observer class that is used to call exit method of all subscribed listeners. */
public class ExitManager {
  private final Logger logger;
  private final List<ExitListener> listeners;

  public ExitManager(List<ExitListener> listeners) {
    logger = LogManager.getLogger(ExitManager.class);
    this.listeners = listeners;
  }

  public void subscribe(ExitListener listener) {
    listeners.add(listener);
    logger.debug("A {} was subscribed.", () -> listener.getClass().getSimpleName());
  }

  public void unsubscribe(ExitListener listener) {
    listeners.remove(listener);
    logger.debug("A {} was unsubscribed.", () -> listener.getClass().getSimpleName());
  }

  public void exit() throws ExitingException {
    for (ExitListener listener : listeners) {
      listener.exit();
      logger.debug("A {} was notified.", () -> listener.getClass().getSimpleName());
    }

    logger.debug(() -> "All listeners of exiting manager were notified.");
  }
}
