package ru.storage.client.controller.responseHandler.responseHandlers;

import ru.storage.client.controller.responseHandler.ResponseHandler;
import ru.storage.client.controller.responseHandler.formatter.StringFormatter;
import ru.storage.common.transfer.response.Status;

public final class ForbiddenResponseHandler extends ResponseHandler {
  private final StringFormatter stringFormatter;

  public ForbiddenResponseHandler(StringFormatter stringFormatter) {
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
