package ru.storage.server.controller.controllers.command;

import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.transfer.response.Response;

import java.util.Map;

/** Command abstract class */
public abstract class Command {
  protected final Configuration configuration;
  protected final ArgumentMediator argumentMediator;
  protected final Map<String, String> arguments;

  /**
   * Takes parameters to then use them during execution.
   *
   * @param configuration server configuration
   * @param argumentMediator argument key mediator
   * @param arguments command arguments
   * @see ArgumentMediator
   */
  public Command(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments) {
    this.configuration = configuration;
    this.argumentMediator = argumentMediator;
    this.arguments = arguments;
  }

  /**
   * Executes command using given parameters in constructor.
   *
   * @return command execution response
   * @see Response
   */
  public abstract Response executeCommand();
}
