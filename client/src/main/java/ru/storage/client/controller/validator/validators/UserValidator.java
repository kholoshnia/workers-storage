package ru.storage.client.controller.validator.validators;

import ru.storage.client.controller.localeListener.LocaleListener;
import ru.storage.client.controller.validator.exceptions.ValidationException;

import java.util.Locale;
import java.util.ResourceBundle;

public final class UserValidator implements LocaleListener {
  private String wrongNameExceptionMessage;
  private String wrongLoginExceptionMessage;
  private String wrongPasswordExceptionMessage;

  public UserValidator() {
    changeLocale();
  }

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("localized.UserValidator", Locale.getDefault());

    wrongNameExceptionMessage = resourceBundle.getString("exceptionMessages.wrongName");
    wrongLoginExceptionMessage = resourceBundle.getString("exceptionMessages.wrongLogin");
    wrongPasswordExceptionMessage = resourceBundle.getString("exceptionMessages.wrongPassword");
  }

  public void checkName(String nameString) throws ValidationException {
    if (nameString == null || nameString.length() < 2 || nameString.length() > 100) {
      throw new ValidationException(wrongNameExceptionMessage);
    }
  }

  public void checkLogin(String loginString) throws ValidationException {
    if (loginString == null || loginString.length() < 2 || loginString.length() > 100) {
      throw new ValidationException(wrongLoginExceptionMessage);
    }
  }

  public void checkPassword(String passwordString) throws ValidationException {
    if (passwordString == null || passwordString.length() < 8 || passwordString.length() > 100) {
      throw new ValidationException(wrongPasswordExceptionMessage);
    }
  }
}
