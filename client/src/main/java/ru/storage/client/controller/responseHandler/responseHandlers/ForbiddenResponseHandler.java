package ru.storage.client.controller.responseHandler.responseHandlers;

import ru.storage.client.controller.responseHandler.ResponseHandler;
import ru.storage.client.controller.responseHandler.formatter.Formatter;
import ru.storage.common.transfer.response.Status;

public final class ForbiddenResponseHandler extends ResponseHandler {
  private final Formatter stringFormatter;

  public ForbiddenResponseHandler(Formatter stringFormatter) {
    this.stringFormatter = stringFormatter;
  }

  @Override
  protected String process() {
    if (!status.equals(Status.FORBIDDEN)) {
      return null;
    }

    return String.format("%s:", stringFormatter.makeMagenta(status.toString()))
        + System.lineSeparator()
        + answer;
  }
}
