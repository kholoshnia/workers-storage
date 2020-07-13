package ru.storage.client.controller.argumentFormer;

import ru.storage.client.controller.validator.exceptions.ValidationException;

public interface ArgumentValidator {
  void check(String input) throws ValidationException;
}
