package ru.storage.client.controller.responseHandler.responseHandlers;

import ru.storage.client.controller.responseHandler.ResponseHandler;
import ru.storage.client.controller.responseHandler.formatter.StringFormatter;
import ru.storage.client.view.console.MessageMediator;
import ru.storage.common.transfer.response.Status;

public final class ForbiddenResponseHandler extends ResponseHandler {
  private final MessageMediator messageMediator;
  private final StringFormatter stringFormatter;

  public ForbiddenResponseHandler(
      MessageMediator messageMediator, StringFormatter stringFormatter) {
    this.messageMediator = messageMediator;
    this.stringFormatter = stringFormatter;
  }

  @Override
  protected String process() {
    if (!status.equals(Status.FORBIDDEN)) {
      return null;
    }

    return String.format(
        "%s:\n%s", stringFormatter.makeMagenta(messageMediator.getStatusPrefix(status)), answer);
  }
}
