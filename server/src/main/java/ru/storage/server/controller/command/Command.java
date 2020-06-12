package ru.storage.server.controller.command;

import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.transfer.response.Response;

import java.util.Locale;
import java.util.Map;

/** Command abstract class */
public abstract class Command {
  protected final Configuration configuration;
  protected final ArgumentMediator argumentMediator;
  protected final Map<String, String> arguments;
  protected final Locale locale;

  /**
   * Takes parameters to then use them during execution.
   *
   * @param configuration concrete configuration
   * @param arguments command arguments
   * @param locale response locale
   */
  public Command(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale) {
    this.configuration = configuration;
    this.argumentMediator = argumentMediator;
    this.arguments = arguments;
    this.locale = locale;
  }

  /**
   * Executes command using given parameters in constructor.
   *
   * @return command execution response
   */
  public abstract Response executeCommand();
}
