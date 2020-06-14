package ru.storage.client.controller.responseHandler.responseHandlers;

import ru.storage.client.controller.responseHandler.ResponseHandler;
import ru.storage.common.transfer.response.Response;

public final class CreatedResponseHandler extends ResponseHandler {
  @Override
  public String handle(Response response) {
    return createdResponse
        + System.lineSeparator()
        + String.format("%s: %s", serverAnswerPrefix, response.getAnswer());
  }
}
