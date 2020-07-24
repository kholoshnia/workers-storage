package ru.storage.client.controller.responseHandler.responseHandlers;

import ru.storage.client.controller.responseHandler.ResponseHandler;
import ru.storage.client.controller.responseHandler.formatter.StringFormatter;
import ru.storage.client.view.console.MessageMediator;
import ru.storage.common.transfer.response.Status;

public final class CreatedResponseHandler extends ResponseHandler {
  private final MessageMediator messageMediator;
  private final StringFormatter stringFormatter;

  public CreatedResponseHandler(MessageMediator messageMediator, StringFormatter stringFormatter) {
    this.messageMediator = messageMediator;
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
