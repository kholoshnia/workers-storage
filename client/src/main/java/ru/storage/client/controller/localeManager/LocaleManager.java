package ru.storage.client.controller.localeManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Locale;

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

  public void changeLocale(Locale locale) {
    Locale.setDefault(locale);

    for (LocaleListener listener : listeners) {
      listener.changeLocale(locale);
      logger.debug("A " + listener.getClass().getSimpleName() + " was notified.");
    }

    logger.debug("All listeners of change locale manager were notified.");
  }
}
