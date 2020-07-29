package ru.storage.server.controller.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.transfer.Request;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;
import ru.storage.server.controller.Controller;

import java.util.Locale;
import java.util.ResourceBundle;

/** Checks if got correct request to continue handling. */
public final class CheckController implements Controller {
  private static final Logger logger = LogManager.getLogger(CheckController.class);

  private static final String GOT_NULL_REQUEST_ANSWER;
  private static final String GOT_NULL_LOCALE_ANSWER;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.CheckController");

    GOT_NULL_REQUEST_ANSWER = resourceBundle.getString("answers.gotNullRequest");
    GOT_NULL_LOCALE_ANSWER = resourceBundle.getString("answers.gotNullLocale");
  }

  private String gotNullCommandAnswer;
  private String gotNullArgumentsAnswer;
  private String gotNullTokenAnswer;
  private String gotNullLoginAnswer;

  private void changeLocale(Locale locale) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.CheckController", locale);

    gotNullCommandAnswer = resourceBundle.getString("answers.gotNullCommand");
    gotNullArgumentsAnswer = resourceBundle.getString("answers.gotNullArguments");
    gotNullTokenAnswer = resourceBundle.getString("answers.gotNullToken");
    gotNullLoginAnswer = resourceBundle.getString("answers.gotNullLogin");
  }

  @Override
  public Response handle(Request request) {
    if (request == null) {
      logger.warn(() -> "Got null request.");
      return new Response(Status.BAD_REQUEST, GOT_NULL_REQUEST_ANSWER);
    }

    if (request.getLocale() == null) {
      logger.warn(() -> "Got null locale.");
      return new Response(Status.BAD_REQUEST, GOT_NULL_LOCALE_ANSWER);
    }

    changeLocale(request.getLocale());

    if (request.getCommand() == null) {
      logger.warn(() -> "Got null command");
      return new Response(Status.BAD_REQUEST, gotNullCommandAnswer);
    }

    if (request.getArguments() == null) {
      logger.warn(() -> "Got null arguments.");
      return new Response(Status.BAD_REQUEST, gotNullArgumentsAnswer);
    }

    if (request.getLogin() == null) {
      logger.warn(() -> "Got null login.");
      return new Response(Status.BAD_REQUEST, gotNullLoginAnswer);
    }

    if (request.getToken() == null) {
      logger.warn(() -> "Got null token.");
      return new Response(Status.BAD_REQUEST, gotNullTokenAnswer);
    }

    return null;
  }
}
