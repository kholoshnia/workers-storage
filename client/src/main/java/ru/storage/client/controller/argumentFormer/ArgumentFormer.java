package ru.storage.client.controller.argumentFormer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.argumentFormer.exceptions.CancelException;
import ru.storage.client.controller.argumentFormer.exceptions.FormingException;
import ru.storage.client.controller.argumentFormer.exceptions.WrongArgumentsException;

import java.util.List;
import java.util.Map;

public abstract class ArgumentFormer {
  private final Logger logger;

  public ArgumentFormer() {
    logger = LogManager.getLogger(ArgumentFormer.class);
  }

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
   * @return formed arguments
   * @throws FormingException - in case of any exceptions during forming
   * @throws CancelException - if forming was canceled
   */
  protected abstract Map<String, String> form(List<String> arguments)
      throws FormingException, CancelException;

  /**
   * Checks the specified arguments and adds other required arguments.
   *
   * @param arguments command arguments
   * @return all arguments
   * @throws WrongArgumentsException - if arguments are incorrect
   * @throws FormingException - in case of any exceptions during forming
   * @throws CancelException - if forming was canceled
   */
  public final Map<String, String> formArguments(List<String> arguments)
      throws WrongArgumentsException, FormingException, CancelException {
    check(arguments);
    logger.info(() -> "Arguments were checked.");

    Map<String, String> allArguments = form(arguments);
    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
