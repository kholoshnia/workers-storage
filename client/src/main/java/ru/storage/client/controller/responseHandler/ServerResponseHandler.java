package ru.storage.client.controller.responseHandler;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public final class ServerResponseHandler implements ResponseHandler {
  private final Logger logger;
  private final Map<Status, String> statuses;

  private String serverAnswerPrefix;
  private String OKResponse;
  private String createdResponse;
  private String noContentResponse;
  private String notModifiedResponse;
  private String badRequestResponse;
  private String unauthorizedResponse;
  private String notFoundResponse;
  private String forbiddenResponse;
  private String conflictResponse;
  private String internalServerErrorResponse;

  @Inject
  public ServerResponseHandler() {
    this.logger = LogManager.getLogger(ServerResponseHandler.class);

    this.statuses =
        new HashMap<Status, String>() {
          {
            put(Status.OK, OKResponse);
            put(Status.CREATED, createdResponse);
            put(Status.NO_CONTENT, noContentResponse);
            put(Status.NOT_MODIFIED, notModifiedResponse);
            put(Status.BAD_REQUEST, badRequestResponse);
            put(Status.UNAUTHORIZED, unauthorizedResponse);
            put(Status.NOT_FOUND, notFoundResponse);
            put(Status.FORBIdDEN, forbiddenResponse);
            put(Status.CONFLICT, conflictResponse);
            put(Status.INTERNAL_SERVER_ERROR, internalServerErrorResponse);
          }
        };

    logger.debug(() -> "Response handler map was created.");
  }

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.ServerResponseHandler");

    serverAnswerPrefix = resourceBundle.getString("prefixes.serverAnswer");
    OKResponse = resourceBundle.getString("responses.OK");
    createdResponse = resourceBundle.getString("responses.created");
    noContentResponse = resourceBundle.getString("responses.noContent");
    notModifiedResponse = resourceBundle.getString("responses.notModified");
    badRequestResponse = resourceBundle.getString("responses.badRequest");
    unauthorizedResponse = resourceBundle.getString("responses.unauthorized");
    notFoundResponse = resourceBundle.getString("responses.notFound");
    forbiddenResponse = resourceBundle.getString("responses.forbidden");
    conflictResponse = resourceBundle.getString("responses.conflict");
    internalServerErrorResponse = resourceBundle.getString("responses.internalServerError");
  }

  @Override
  public String handle(Response response) {
    String result =
        String.format(
            "%s (%s): %s",
            serverAnswerPrefix, statuses.get(response.getStatus()), response.getAnswer());

    logger.info("Got string: {}, for response: {}.", () -> response, () -> result);
    return result;
  }
}
