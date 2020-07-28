package ru.storage.client.controller.responseHandler.responseHandlers;

import ru.storage.client.controller.responseHandler.ResponseHandler;
import ru.storage.client.controller.responseHandler.formatter.Formatter;
import ru.storage.common.transfer.response.Status;

public final class NoContentResponseHandler extends ResponseHandler {
  private final Formatter stringFormatter;

  public NoContentResponseHandler(Formatter stringFormatter) {
    this.stringFormatter = stringFormatter;
  }

  @Override
  protected String process() {
    if (!status.equals(Status.NO_CONTENT)) {
      return null;
    }

    return stringFormatter.makeCyan(answer);
  }
}
