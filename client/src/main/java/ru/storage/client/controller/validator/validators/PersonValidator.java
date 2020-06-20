package ru.storage.client.controller.validator.validators;

import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.client.controller.validator.exceptions.ValidationException;

import java.util.Locale;
import java.util.ResourceBundle;

public final class PersonValidator implements LocaleListener {
  private String wrongNameException;
  private String wrongPassportIDException;

  public PersonValidator() {
    changeLocale();
  }

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("localized.PersonValidator", Locale.getDefault());

    wrongNameException = resourceBundle.getString("exceptions.wrongName");
    wrongPassportIDException = resourceBundle.getString("exceptions.wrongPassportID");
  }

  public void checkName(String nameString) throws ValidationException {
    if (nameString == null || nameString.length() < 2 || nameString.length() > 100) {
      throw new ValidationException(wrongNameException);
    }
  }

  public void checkPassportID(String passportIDString) throws ValidationException {
    if (passportIDString == null
        || passportIDString.length() < 10
        || passportIDString.length() > 40) {
      throw new ValidationException(wrongPassportIDException);
    }
  }
}
