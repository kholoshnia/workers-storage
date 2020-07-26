package ru.storage.server.controller.controllers;

import ru.storage.common.transfer.Request;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;
import ru.storage.server.controller.Controller;

import java.util.ResourceBundle;

/** Checks if got correct request to continue handling. */
public final class CheckController implements Controller {
  private static final String GOT_NULL_REQUEST_ANSWER;
  private static final String GOT_NULL_LOCALE_ANSWER;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.CheckController");

    GOT_NULL_REQUEST_ANSWER = resourceBundle.getString("answers.gotNullRequest");
    GOT_NULL_LOCALE_ANSWER = resourceBundle.getString("answers.gotNullLocale");
  }

  @Override
  public Response handle(Request request) {
    if (request == null) {
      return new Response(Status.BAD_REQUEST, GOT_NULL_REQUEST_ANSWER);
    }

    if (request.getLocale() == null) {
      return new Response(Status.BAD_REQUEST, GOT_NULL_LOCALE_ANSWER);
    }

    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("localized.CheckController", request.getLocale());

    String gotNullCommandAnswer = resourceBundle.getString("answers.gotNullCommand");
    String gotNullArgumentsAnswer = resourceBundle.getString("answers.gotNullArguments");
    String gotNullTokenAnswer = resourceBundle.getString("answers.gotNullToken");
    String gotNullLoginAnswer = resourceBundle.getString("answers.gotNullLogin");

    if (request.getCommand() == null) {
      return new Response(Status.BAD_REQUEST, gotNullCommandAnswer);
    }

    if (request.getArguments() == null) {
      return new Response(Status.BAD_REQUEST, gotNullArgumentsAnswer);
    }

    if (request.getLogin() == null) {
      return new Response(Status.BAD_REQUEST, gotNullLoginAnswer);
    }

    if (request.getToken() == null) {
      return new Response(Status.BAD_REQUEST, gotNullTokenAnswer);
    }

    return null;
  }
}
