package ru.storage.client.controller.responseHandler.responseHandlers;

import ru.storage.client.controller.responseHandler.ResponseHandler;
import ru.storage.client.controller.responseHandler.formatter.Formatter;
import ru.storage.common.transfer.response.Status;

public final class NotFoundResponseHandler extends ResponseHandler {
  private final Formatter stringFormatter;

  public NotFoundResponseHandler(Formatter stringFormatter) {
    this.stringFormatter = stringFormatter;
  }

  @Override
  protected String process() {
    if (!status.equals(Status.NOT_FOUND)) {
      return null;
    }

    return String.format("%s:", stringFormatter.makeCyan(status.toString()))
        + System.lineSeparator()
        + answer;
  }
}
