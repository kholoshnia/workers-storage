package ru.storage.client.controller.requestBuilder.requestBuilders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.localeListener.LocaleListener;
import ru.storage.client.controller.requestBuilder.RequestBuilder;
import ru.storage.client.controller.requestBuilder.exceptions.WrongArgumentsException;
import ru.storage.common.ArgumentMediator;

import java.util.Map;
import java.util.ResourceBundle;

public final class WorkerIDArgumentRequestBuilder extends RequestBuilder implements LocaleListener {
  private final Logger logger;
  private String wrongArgumentsNumberException;
  private String wrongArgumentValueException;

  public WorkerIDArgumentRequestBuilder(ArgumentMediator argumentMediator, String command) {
    super(argumentMediator, command);
    this.logger = LogManager.getLogger(WorkerIDArgumentRequestBuilder.class);
    changeLocale();
  }

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("localized.WorkerIDArgumentRequestBuilder");

    wrongArgumentsNumberException = resourceBundle.getString("exceptions.wrongArgumentsNumber");
    wrongArgumentValueException = resourceBundle.getString("exceptions.wrongArgumentValue");
  }

  @Override
  protected void check(Map<String, String> arguments) throws WrongArgumentsException {
    if (arguments.size() != 1) {
      logger.warn("Got wrong arguments number.");
      throw new WrongArgumentsException(wrongArgumentsNumberException);
    }

    String idArgument = arguments.get(argumentMediator.WORKER_ID);

    long id;

    try {
      id = Long.parseLong(idArgument);
    } catch (NumberFormatException | NullPointerException exception) {
      logger.warn("Got wrong argument value.");
      throw new WrongArgumentsException(wrongArgumentValueException);
    }

    if (id < 0) {
      logger.warn("Got wrong argument value.");
      throw new WrongArgumentsException(wrongArgumentValueException);
    }
  }
}
