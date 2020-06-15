package ru.storage.client.controller.requestBuilder.requestBuilders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.localeListener.LocaleListener;
import ru.storage.client.controller.requestBuilder.RequestBuilder;
import ru.storage.client.controller.requestBuilder.exceptions.WrongArgumentsException;
import ru.storage.common.ArgumentMediator;

import java.util.Map;
import java.util.ResourceBundle;

public final class NoArgumentsRequestBuilder extends RequestBuilder implements LocaleListener {
  private final Logger logger;
  private String wrongArgumentsNumberException;

  public NoArgumentsRequestBuilder(ArgumentMediator argumentMediator, String command) {
    super(argumentMediator, command);
    this.logger = LogManager.getLogger(NoArgumentsRequestBuilder.class);
    changeLocale();
  }

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.NoArgumentsRequestBuilder");

    wrongArgumentsNumberException = resourceBundle.getString("exceptions.wrongArgumentsNumber");
  }

  @Override
  protected void check(Map<String, String> arguments) throws WrongArgumentsException {
    if (arguments.size() != 0) {
      logger.warn("Got wrong arguments number.");
      throw new WrongArgumentsException(wrongArgumentsNumberException);
    }
  }
}
