package ru.storage.client.controller.responseHandler.responseHandlers;

import ru.storage.client.controller.responseHandler.ResponseHandler;
import ru.storage.common.transfer.response.Status;

public final class OkResponseHandler extends ResponseHandler {
  @Override
  protected String process() {
    if (!status.equals(Status.OK)) {
      return null;
    }

    return answer;
  }
}
