package ru.storage.server.controller.controllers.argument.validator.validators;

import com.google.inject.Inject;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.controllers.argument.validator.ArgumentValidator;

import java.util.ArrayList;

public final class RegisterValidator extends ArgumentValidator {
  @Inject
  public RegisterValidator(ArgumentMediator argumentMediator) {
    super(
        new ArrayList<String>() {
          {
            add(argumentMediator.userName);
            add(argumentMediator.userLogin);
            add(argumentMediator.userPassword);
          }
        });
  }
}
