package ru.storage.server.controller.controllers.argument.validator.validators;

import com.google.inject.Inject;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.controllers.argument.validator.ArgumentValidator;

import java.util.ArrayList;
import java.util.List;

public final class LoginValidator extends ArgumentValidator {
  @Inject
  public LoginValidator(ArgumentMediator argumentMediator) {
    requiredArguments = initRequiredArguments(argumentMediator);
  }

  private List<String> initRequiredArguments(ArgumentMediator argumentMediator) {
    return new ArrayList<String>() {
      {
        add(argumentMediator.USER_LOGIN);
        add(argumentMediator.USER_PASSWORD);
      }
    };
  }
}
