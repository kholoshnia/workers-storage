package ru.storage.client.controller.validator.validators;

import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.client.controller.validator.exceptions.ValidationException;

import java.util.Locale;
import java.util.ResourceBundle;

public final class LoginValidator implements LocaleListener {
  private String wrongLoginException;
  private String wrongPasswordException;

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("localized.LoginValidator", Locale.getDefault());

    wrongLoginException = resourceBundle.getString("exceptions.wrongLogin");
    wrongPasswordException = resourceBundle.getString("exceptions.wrongPassword");
  }

  public void checkLogin(String login) throws ValidationException {
    if (login == null || login.trim().isEmpty()) {
      throw new ValidationException(wrongLoginException);
    }
  }

  public void checkPassword(String password) throws ValidationException {
    if (password == null || password.trim().isEmpty()) {
      throw new ValidationException(wrongPasswordException);
    }
  }
}
