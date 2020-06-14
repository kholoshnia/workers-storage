package ru.storage.client.controller.responseHandler;

import ru.storage.client.controller.localeListener.LocaleListener;
import ru.storage.common.transfer.response.Response;

import java.util.ResourceBundle;

public abstract class ResponseHandler implements LocaleListener {
  protected String serverAnswerPrefix;
  protected String OKResponse;
  protected String createdResponse;
  protected String noContentResponse;
  protected String notModifiedResponse;
  protected String badRequestResponse;
  protected String unauthorizedResponse;
  protected String NotFoundResponse;
  protected String forbiddenResponse;
  protected String conflictResponse;
  protected String internalServerErrorResponse;

  public ResponseHandler() {
    changeLocale();
  }

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.ResponseHandler");

    serverAnswerPrefix = resourceBundle.getString("prefixes.serverAnswer");
    OKResponse = resourceBundle.getString("responses.OK");
    createdResponse = resourceBundle.getString("responses.created");
    noContentResponse = resourceBundle.getString("responses.noContent");
    notModifiedResponse = resourceBundle.getString("responses.notModified");
    badRequestResponse = resourceBundle.getString("responses.badRequest");
    unauthorizedResponse = resourceBundle.getString("responses.unauthorized");
    NotFoundResponse = resourceBundle.getString("responses.notFound");
    forbiddenResponse = resourceBundle.getString("responses.forbidden");
    conflictResponse = resourceBundle.getString("responses.conflict");
    internalServerErrorResponse = resourceBundle.getString("responses.internalServerError");
  }

  public abstract String handle(Response response);
}
