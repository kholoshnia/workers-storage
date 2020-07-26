package ru.storage.client.controller.responseHandler.responseHandlers;

import ru.storage.client.controller.responseHandler.ResponseHandler;
import ru.storage.client.controller.responseHandler.formatter.StringFormatter;
import ru.storage.common.transfer.response.Status;

public final class CreatedResponseHandler extends ResponseHandler {
  private final StringFormatter stringFormatter;

  public CreatedResponseHandler(StringFormatter stringFormatter) {
    this.stringFormatter = stringFormatter;
  }

  @Override
  protected String process() {
    if (!status.equals(Status.CREATED)) {
      return null;
    }

    return stringFormatter.makeGreen(answer);
  }
}
