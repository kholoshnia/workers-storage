package ru.storage.client.controller.validator.validators;

import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.client.controller.validator.exceptions.ValidationException;

import java.util.Locale;
import java.util.ResourceBundle;

public final class RegisterValidator implements LocaleListener {
  private String wrongNameException;
  private String wrongLoginException;
  private String wrongPasswordException;

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("localized.RegisterValidator", Locale.getDefault());

    wrongNameException = resourceBundle.getString("exceptions.wrongName");
    wrongLoginException = resourceBundle.getString("exceptions.wrongLogin");
    wrongPasswordException = resourceBundle.getString("exceptions.wrongPassword");
  }

  public void checkName(String nameString) throws ValidationException {
    if (nameString == null || nameString.length() < 2 || nameString.length() > 100) {
      throw new ValidationException(wrongNameException);
    }
  }

  public void checkLogin(String loginString) throws ValidationException {
    if (loginString == null || loginString.length() < 2 || loginString.length() > 100) {
      throw new ValidationException(wrongLoginException);
    }
  }

  public void checkPassword(String passwordString) throws ValidationException {
    if (passwordString == null || passwordString.length() < 8 || passwordString.length() > 100) {
      throw new ValidationException(wrongPasswordException);
    }
  }
}
