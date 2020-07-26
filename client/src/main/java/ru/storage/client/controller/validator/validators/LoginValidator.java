package ru.storage.client.controller.validator.validators;

import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.client.controller.validator.exceptions.ValidationException;

import java.util.Locale;
import java.util.ResourceBundle;

public final class LoginValidator implements LocaleListener {
  private String wrongLoginException;
  private String wrongPasswordException;

  @Override
  public void changeLocale(Locale locale) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.LoginValidator");

    wrongLoginException = resourceBundle.getString("exceptions.wrongLogin");
    wrongPasswordException = resourceBundle.getString("exceptions.wrongPassword");
  }

  public void checkLogin(String login) throws ValidationException {
    if (login == null || login.isEmpty()) {
      throw new ValidationException(wrongLoginException);
    }
  }

  public void checkPassword(String password) throws ValidationException {
    if (password == null || password.isEmpty()) {
      throw new ValidationException(wrongPasswordException);
    }
  }
}
