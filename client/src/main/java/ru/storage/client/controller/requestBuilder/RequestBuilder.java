package ru.storage.client.controller.requestBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.argumentFormer.ArgumentFormer;
import ru.storage.client.controller.argumentFormer.argumentFormers.NewWorkerFormer;
import ru.storage.client.controller.argumentFormer.argumentFormers.NewWorkerIDFormer;
import ru.storage.client.controller.argumentFormer.argumentFormers.NoArgumentsFormer;
import ru.storage.client.controller.argumentFormer.argumentFormers.WorkerIDFormer;
import ru.storage.client.controller.requestBuilder.exceptions.BuildingException;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;
import ru.storage.common.transfer.request.Request;

import java.util.*;

/** Class is responsible for requests building. */
public final class RequestBuilder {
  private final Logger logger;
  private final Map<String, ArgumentFormer> argumentFormerMap;

  private String command;
  private List<String> arguments;
  private Locale locale;

  public RequestBuilder(CommandMediator commandMediator, ArgumentMediator argumentMediator) {
    this.logger = LogManager.getLogger(RequestBuilder.class);
    ArgumentFormer noArgumentsFormer = new NoArgumentsFormer(argumentMediator);
    ArgumentFormer workerIDFormer = new WorkerIDFormer(argumentMediator);
    ArgumentFormer newWorkerFormer = new NewWorkerFormer(argumentMediator);
    ArgumentFormer newWorkerID = new NewWorkerIDFormer(argumentMediator);

    this.argumentFormerMap =
        new HashMap<String, ArgumentFormer>() {
          {
            put(commandMediator.LOGIN, noArgumentsFormer);
            put(commandMediator.LOGOUT, noArgumentsFormer);
            put(commandMediator.REGISTER, noArgumentsFormer);
            put(commandMediator.SHOW_HISTORY, noArgumentsFormer);
            put(commandMediator.CLEAR_HISTORY, noArgumentsFormer);
            put(commandMediator.ADD, newWorkerFormer);
            put(commandMediator.REMOVE, noArgumentsFormer);
            put(commandMediator.UPDATE, newWorkerID);
            put(commandMediator.EXIT, workerIDFormer);
            put(commandMediator.HELP, noArgumentsFormer);
            put(commandMediator.INFO, noArgumentsFormer);
            put(commandMediator.SHOW, noArgumentsFormer);
          }
        };

    logger.debug(() -> "Argument former map was created");
  }

  /**
   * Sets command. NOTE: if not set on {@link RequestBuilder#build()} it throws {@link
   * BuildingException}.
   *
   * @param command concrete command
   * @return this request builder
   */
  public RequestBuilder setCommand(String command) {
    this.command = command;
    return this;
  }

  /**
   * Sets command arguments. NOTE: if locale is not set on {@link RequestBuilder#build()} it sets
   * empty arguments.
   *
   * @param arguments concrete command arguments
   * @return this request builder
   */
  public RequestBuilder setArguments(List<String> arguments) {
    this.arguments = arguments;
    return this;
  }

  /**
   * Sets locale. NOTE: if locale is not set on {@link RequestBuilder#build()} it sets system
   * default locale.
   *
   * @param locale concrete locale
   * @return this request builder
   */
  public RequestBuilder setLocale(Locale locale) {
    this.locale = locale;
    return this;
  }

  /**
   * Builds request from set parameters.
   *
   * @return request
   * @throws BuildingException - if parameters were not set or required were empty
   */
  public Request build() throws BuildingException {
    if (command == null) {
      logger.error("Command was not set.");
      throw new BuildingException();
    }

    if (arguments == null) {
      logger.info("Arguments was not set, setting empty arguments.");
      arguments = new ArrayList<>();
    }

    if (locale == null) {
      logger.info("Locale was not set, setting system default locale.");
      locale = Locale.getDefault();
    }

    ArgumentFormer argumentFormer = argumentFormerMap.get(command);
    logger.info(
        "Got argument former {}, for command {}.",
        () -> argumentFormer.getClass().getSimpleName(),
        () -> command);

    argumentFormer.check(arguments);
    Map<String, String> allArguments = argumentFormer.form(arguments);
    return new Request(command, allArguments, locale);
  }
}
