package ru.storage.client.controller.validator.validators;

import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.client.controller.validator.exceptions.ValidationException;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegisterValidator implements LocaleListener {
  public static final Pattern NAME_REGEX = Pattern.compile("\\p{Lu}\\p{L}[^#&<>\"~;$^%{}?]{0,20}$");
  public static final Pattern LOGIN_REGEX =
      Pattern.compile(
          "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
  public static final Pattern PASSWORD_REGEX =
      Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,32}$");

  private String wrongNameException;
  private String wrongLoginException;
  private String wrongPasswordException;

  @Override
  public void changeLocale(Locale locale) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.RegisterValidator");

    wrongNameException = resourceBundle.getString("exceptions.wrongName");
    wrongLoginException = resourceBundle.getString("exceptions.wrongLogin");
    wrongPasswordException = resourceBundle.getString("exceptions.wrongPassword");
  }

  public void checkName(String nameString) throws ValidationException {
    if (nameString == null || nameString.length() < 2 || nameString.length() > 100) {
      throw new ValidationException(wrongNameException);
    }

    Matcher matcher = NAME_REGEX.matcher(nameString);
    if (!matcher.matches()) {
      throw new ValidationException(wrongNameException);
    }
  }

  public void checkLogin(String loginString) throws ValidationException {
    if (loginString == null || loginString.length() < 2 || loginString.length() > 100) {
      throw new ValidationException(wrongLoginException);
    }

    Matcher matcher = LOGIN_REGEX.matcher(loginString);
    if (!matcher.matches()) {
      throw new ValidationException(wrongLoginException);
    }
  }

  public void checkPassword(String passwordString) throws ValidationException {
    if (passwordString == null || passwordString.length() < 8 || passwordString.length() > 100) {
      throw new ValidationException(wrongPasswordException);
    }

    Matcher matcher = PASSWORD_REGEX.matcher(passwordString);
    if (!matcher.matches()) {
      throw new ValidationException(wrongPasswordException);
    }
  }
}
