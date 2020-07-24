package ru.storage.client.view.console;

import ru.storage.client.controller.localeManager.LocaleListener;

import java.util.Locale;
import java.util.ResourceBundle;

public final class MessageMediator implements LocaleListener {
  private String badRequestMessage;
  private String conflictMessage;
  private String internalServerErrorMessage;
  private String unauthorizedMessage;

  @Override
  public void changeLocale(Locale locale) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.MessageMediator", locale);

    badRequestMessage = resourceBundle.getString("messages.badRequest");
    conflictMessage = resourceBundle.getString("messages.conflict");
    internalServerErrorMessage = resourceBundle.getString("messages.internalServerError");
    unauthorizedMessage = resourceBundle.getString("messages.unauthorizedMessage");
  }

  public String getBadRequestMessage() {
    return badRequestMessage;
  }

  public String getConflictMessage() {
    return conflictMessage;
  }

  public String getInternalServerErrorMessage() {
    return internalServerErrorMessage;
  }

  public String getUnauthorizedMessage() {
    return unauthorizedMessage;
  }
}
