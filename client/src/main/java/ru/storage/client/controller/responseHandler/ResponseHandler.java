package ru.storage.client.controller.responseHandler;

import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;

public abstract class ResponseHandler {
  protected Status status;
  protected String answer;

  public final String handle(Response response) {
    status = response.getStatus();
    answer = response.getAnswer();

    return process();
  }

  protected abstract String process();
}
