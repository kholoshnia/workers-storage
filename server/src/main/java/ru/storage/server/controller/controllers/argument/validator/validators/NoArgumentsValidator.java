package ru.storage.server.controller.controllers.argument.validator.validators;

import com.google.inject.Inject;
import ru.storage.server.controller.controllers.argument.validator.ArgumentValidator;

import java.util.ArrayList;

public final class NoArgumentsValidator extends ArgumentValidator {
  @Inject
  public NoArgumentsValidator() {
    requiredArguments = new ArrayList<>();
  }
}
