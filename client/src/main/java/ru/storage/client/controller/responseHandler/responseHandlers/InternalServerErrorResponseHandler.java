package ru.storage.client.controller.responseHandler.responseHandlers;

import ru.storage.client.controller.responseHandler.ResponseHandler;
import ru.storage.client.controller.responseHandler.formatter.StringFormatter;
import ru.storage.client.view.console.MessageMediator;
import ru.storage.common.transfer.response.Status;

public final class InternalServerErrorResponseHandler extends ResponseHandler {
  private final MessageMediator messageMediator;
  private final StringFormatter stringFormatter;

  public InternalServerErrorResponseHandler(
      MessageMediator messageMediator, StringFormatter stringFormatter) {
    this.messageMediator = messageMediator;
    this.stringFormatter = stringFormatter;
  }

  @Override
  protected String process() {
    if (!status.equals(Status.INTERNAL_SERVER_ERROR)) {
      return null;
    }

    return String.format(
            "%s (%s):",
            stringFormatter.makeRed(status.toString()),
            messageMediator.getInternalServerErrorMessage())
        + System.lineSeparator()
        + stringFormatter.makeRed(answer);
  }
}
