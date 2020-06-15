package ru.storage.client.controller.requestBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.requestBuilder.exceptions.WrongArgumentsException;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.transfer.request.Request;

import java.util.Locale;
import java.util.Map;

/** Class is responsible for requests building. */
public abstract class RequestBuilder {
  protected final ArgumentMediator argumentMediator;

  private final Logger logger;
  private final String command;

  public RequestBuilder(ArgumentMediator argumentMediator, String command) {
    this.logger = LogManager.getLogger(RequestBuilder.class);
    this.argumentMediator = argumentMediator;
    this.command = command;
  }

  /**
   * Checks number of arguments and its values.
   *
   * @param arguments command arguments
   * @throws WrongArgumentsException - if arguments are incorrect
   */
  protected abstract void check(Map<String, String> arguments) throws WrongArgumentsException;

  /**
   * Builds request from arguments and locale.
   *
   * @param arguments command arguments
   * @param locale user locale
   * @return built request
   * @throws WrongArgumentsException - if arguments are incorrect
   */
  public final Request build(Map<String, String> arguments, Locale locale)
      throws WrongArgumentsException {
    check(arguments);
    logger.info("Checked arguments.");

    logger.info("Request was built.");
    return new Request(command, arguments, locale);
  }
}
