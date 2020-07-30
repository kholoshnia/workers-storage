package ru.storage.server.controller.services.script.scriptExecutor.argumentFormer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.server.controller.services.script.Script;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.exceptions.FormingException;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.exceptions.WrongArgumentsException;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class ArgumentFormer {
  private static final Logger logger = LogManager.getLogger(ArgumentFormer.class);

  /**
   * Loads resources in accordance with the specified locale.
   *
   * @param locale script locale
   */
  protected abstract void changeLocale(Locale locale);

  /**
   * Checks number of command arguments and its values.
   *
   * @param arguments command arguments
   * @throws WrongArgumentsException - if arguments are incorrect
   */
  protected abstract void check(List<String> arguments) throws WrongArgumentsException;

  /**
   * Forms all arguments with specified arguments.
   *
   * @param arguments command arguments
   * @param script script containing arguments
   * @return formed arguments
   * @throws FormingException - in case of any exceptions during forming
   */
  protected abstract Map<String, String> form(List<String> arguments, Script script)
      throws FormingException;

  /**
   * Checks the specified arguments and adds other required arguments.
   *
   * @param arguments command arguments
   * @param script script containing arguments
   * @return all arguments
   * @throws WrongArgumentsException - if arguments are incorrect
   * @throws FormingException - in case of any exceptions during forming
   */
  public final Map<String, String> formArguments(List<String> arguments, Script script)
      throws WrongArgumentsException, FormingException {
    changeLocale(script.getLocale());
    logger.info(() -> "Locale was changed.");

    check(arguments);
    logger.info(() -> "Arguments were checked.");

    Map<String, String> allArguments = form(arguments, script);
    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
