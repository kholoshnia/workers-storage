package ru.storage.server.controller.services.exitManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.server.controller.services.exitManager.exceptions.ExitingException;

import java.util.List;

/** Observer class that is used to call exit method of all subscribed listeners. */
public class ExitManager {
  private final Logger logger;
  private final List<ExitListener> listeners;

  public ExitManager(List<ExitListener> listeners) {
    logger = LogManager.getLogger(this);
    this.listeners = listeners;
  }

  public void subscribe(ExitListener listener) {
    listeners.add(listener);
  }

  public void unsubscribe(ExitListener listener) {
    listeners.remove(listener);
  }

  public void exit() throws ExitingException {
    for (ExitListener listener : listeners) {
      listener.exit();
      logger.debug("A " + listener.getClass().getSimpleName() + " was notified.");
    }

    logger.debug("All listeners of exiting manager were notified SUCCESSFULLY.");
  }
}
