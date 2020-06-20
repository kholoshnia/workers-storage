package ru.storage.client.controller.argumentFormer;

import ru.storage.client.controller.argumentFormer.exceptions.WrongArgumentsException;
import ru.storage.client.controller.requestBuilder.exceptions.BuildingException;
import ru.storage.common.ArgumentMediator;

import java.util.List;
import java.util.Map;

public abstract class ArgumentFormer {
  protected final ArgumentMediator argumentMediator;

  public ArgumentFormer(ArgumentMediator argumentMediator) {
    this.argumentMediator = argumentMediator;
  }

  /**
   * Checks number of arguments and its values.
   *
   * @param arguments command arguments
   * @throws WrongArgumentsException - if arguments are incorrect
   */
  public abstract void check(List<String> arguments) throws WrongArgumentsException;

  /**
   * Forms all arguments with specified arguments.
   *
   * @param arguments command arguments
   * @return formed arguments
   * @throws WrongArgumentsException - in case of forming exception
   */
  public abstract Map<String, String> form(List<String> arguments) throws BuildingException;
}
