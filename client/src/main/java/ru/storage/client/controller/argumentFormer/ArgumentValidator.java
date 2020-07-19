package ru.storage.client.controller.argumentFormer;

import ru.storage.client.controller.validator.exceptions.ValidationException;

public interface ArgumentValidator {
  /**
   * Checks user input.
   *
   * @param input user input
   * @throws ValidationException - in case of validation errors.
   */
  void check(String input) throws ValidationException;
}
