package ru.storage.client.controller.responseHandler.responseHandlers;

import ru.storage.client.controller.responseHandler.MessageMediator;
import ru.storage.client.controller.responseHandler.ResponseHandler;
import ru.storage.client.controller.responseHandler.formatter.Formatter;
import ru.storage.common.transfer.response.Status;

public final class ConflictResponseHandler extends ResponseHandler {
  private final Formatter stringFormatter;
  private final MessageMediator messageMediator;

  public ConflictResponseHandler(Formatter stringFormatter, MessageMediator messageMediator) {
    this.stringFormatter = stringFormatter;
    this.messageMediator = messageMediator;
  }

  @Override
  protected String process() {
    if (!status.equals(Status.CONFLICT)) {
      return null;
    }

    return String.format(
            "%s (%s):",
            stringFormatter.makeYellow(status.toString()), messageMediator.getConflictMessage())
        + System.lineSeparator()
        + answer;
  }
}
