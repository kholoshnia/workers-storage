package ru.storage.server.controller.check;

import ru.storage.common.transfer.request.Request;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;
import ru.storage.server.controller.Controller;

import java.util.ResourceBundle;

public final class CheckController implements Controller {
  private static String GOT_NULL_REQUEST_ANSWER;
  private static String GOT_NULL_LOCALE_ANSWER;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.CheckController");

    GOT_NULL_REQUEST_ANSWER = resourceBundle.getString("answers.gotNullRequest");
    GOT_NULL_LOCALE_ANSWER = resourceBundle.getString("answers.gotNullLocale");
  }

  private String gotNullCommandAnswer;
  private String gotNullArgumentsAnswer;
  private String gotNullTokenAnswer;

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

    gotNullCommandAnswer = resourceBundle.getString("answers.gotNullCommand");
    gotNullArgumentsAnswer = resourceBundle.getString("answers.gotNullArguments");
    gotNullTokenAnswer = resourceBundle.getString("answers.gotNullToken");

    if (request.getCommand() == null) {
      return new Response(Status.BAD_REQUEST, gotNullCommandAnswer);
    }

    if (request.getArguments() == null) {
      return new Response(Status.BAD_REQUEST, gotNullArgumentsAnswer);
    }

    if (request.getToken() == null) {
      return new Response(Status.BAD_REQUEST, gotNullTokenAnswer);
    }

    return null;
  }
}
