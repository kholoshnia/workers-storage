package ru.storage.client.view.console;

import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.common.transfer.response.Status;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public final class MessageMediator implements LocaleListener {
  private String badRequestMessage;
  private String conflictMessage;
  private String internalServerErrorMessage;
  private String unauthorizedMessage;

  private Map<Status, String> statusPrefixMap;

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.MessageMediator");

    statusPrefixMap =
        new HashMap<Status, String>() {
          {
            put(Status.OK, resourceBundle.getString("prefixes.ok"));
            put(Status.CREATED, resourceBundle.getString("prefixes.created"));
            put(Status.NO_CONTENT, resourceBundle.getString("prefixes.noContent"));
            put(Status.NOT_MODIFIED, resourceBundle.getString("prefixes.notModified"));
            put(Status.BAD_REQUEST, resourceBundle.getString("prefixes.badRequest"));
            put(Status.UNAUTHORIZED, resourceBundle.getString("prefixes.unauthorized"));
            put(Status.NOT_FOUND, resourceBundle.getString("prefixes.notFound"));
            put(Status.FORBIDDEN, resourceBundle.getString("prefixes.forbidden"));
            put(Status.CONFLICT, resourceBundle.getString("prefixes.conflict"));
            put(
                Status.INTERNAL_SERVER_ERROR,
                resourceBundle.getString("prefixes.internalServerError"));
          }
        };

    badRequestMessage = resourceBundle.getString("messages.badRequest");
    conflictMessage = resourceBundle.getString("messages.conflict");
    internalServerErrorMessage = resourceBundle.getString("messages.internalServerError");
    unauthorizedMessage = resourceBundle.getString("messages.unauthorizedMessage");
  }

  public String getStatusPrefix(Status status) {
    return statusPrefixMap.get(status);
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
