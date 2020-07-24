package ru.storage.client.controller.responseHandler.responseHandlers;

import ru.storage.client.controller.responseHandler.ResponseHandler;
import ru.storage.client.controller.responseHandler.formatter.StringFormatter;
import ru.storage.client.view.console.MessageMediator;
import ru.storage.common.transfer.response.Status;

public final class ConflictResponseHandler extends ResponseHandler {
  private final MessageMediator messageMediator;
  private final StringFormatter stringFormatter;

  public ConflictResponseHandler(MessageMediator messageMediator, StringFormatter stringFormatter) {
    this.messageMediator = messageMediator;
    this.stringFormatter = stringFormatter;
  }

  @Override
  protected String process() {
    if (!status.equals(Status.CONFLICT)) {
      return null;
    }

    return String.format(
            "%s (%s):",
            stringFormatter.makeYellow(status.toString()),
            messageMediator.getConflictMessage())
        + System.lineSeparator()
        + answer;
  }
}
