package ru.storage.client.controller.validator.validators;

import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.client.controller.validator.exceptions.ValidationException;

import java.util.Locale;
import java.util.ResourceBundle;

public final class PersonValidator implements LocaleListener {
  private String wrongNameException;
  private String wrongPassportIdException;

  @Override
  public void changeLocale(Locale locale) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.PersonValidator");

    wrongNameException = resourceBundle.getString("exceptions.wrongName");
    wrongPassportIdException = resourceBundle.getString("exceptions.wrongPassportId");
  }

  public void checkName(String nameString) throws ValidationException {
    if (nameString == null || nameString.length() < 2 || nameString.length() > 100) {
      throw new ValidationException(wrongNameException);
    }
  }

  public void checkPassportId(String passportIdString) throws ValidationException {
    if (passportIdString == null
        || passportIdString.length() < 10
        || passportIdString.length() > 40) {
      throw new ValidationException(wrongPassportIdException);
    }
  }
}
