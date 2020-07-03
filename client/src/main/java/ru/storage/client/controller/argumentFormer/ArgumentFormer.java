package ru.storage.client.controller.argumentFormer;

import ru.storage.client.controller.argumentFormer.exceptions.WrongArgumentsException;
import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.client.controller.requestBuilder.exceptions.BuildingException;

import java.util.List;
import java.util.Map;

public interface ArgumentFormer extends LocaleListener {
  /**
   * Checks number of arguments and its values.
   *
   * @param arguments command arguments
   * @throws WrongArgumentsException - if arguments are incorrect
   */
  void check(List<String> arguments) throws WrongArgumentsException;

  /**
   * Forms all arguments with specified arguments.
   *
   * @param arguments command arguments
   * @return formed arguments
   * @throws WrongArgumentsException - in case of forming exception
   */
  Map<String, String> form(List<String> arguments) throws BuildingException;
}
