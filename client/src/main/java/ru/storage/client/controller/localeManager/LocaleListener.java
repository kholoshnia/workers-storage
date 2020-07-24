package ru.storage.client.controller.localeManager;

import java.util.Locale;

public interface LocaleListener {
  /**
   * Changes locale. Loads new data in accordance with the specified locale.
   *
   * @param locale new locale
   */
  void changeLocale(Locale locale);
}
