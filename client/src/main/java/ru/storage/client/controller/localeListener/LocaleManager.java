package ru.storage.client.controller.localeListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/** Observer class that is used to call change locale method of all subscribed listeners. */
public class LocaleManager {
  private final Logger logger;
  private final List<LocaleListener> listeners;

  public LocaleManager(List<LocaleListener> listeners) {
    logger = LogManager.getLogger(LocaleManager.class);
    this.listeners = listeners;
  }

  public void subscribe(LocaleListener listener) {
    listeners.add(listener);
  }

  public void unsubscribe(LocaleListener listener) {
    listeners.remove(listener);
  }

  public void exit() {
    for (LocaleListener listener : listeners) {
      listener.changeLocale();
      logger.debug("A " + listener.getClass().getSimpleName() + " was notified.");
    }

    logger.debug("All listeners of change locale manager were notified.");
  }
}
