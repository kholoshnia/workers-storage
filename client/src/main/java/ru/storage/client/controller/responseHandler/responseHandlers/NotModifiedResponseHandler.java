package ru.storage.client.controller.responseHandler.responseHandlers;

import ru.storage.client.controller.responseHandler.ResponseHandler;
import ru.storage.client.controller.responseHandler.formatter.Formatter;
import ru.storage.common.transfer.response.Status;

public final class NotModifiedResponseHandler extends ResponseHandler {
  private final Formatter stringFormatter;

  public NotModifiedResponseHandler(Formatter stringFormatter) {
    this.stringFormatter = stringFormatter;
  }

  @Override
  protected String process() {
    if (!status.equals(Status.NOT_MODIFIED)) {
      return null;
    }

    return String.format("%s:", stringFormatter.makeCyan(status.toString()))
        + System.lineSeparator()
        + answer;
  }
}
