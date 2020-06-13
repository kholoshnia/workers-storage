package ru.storage.client.view.validator.validators;

import ru.storage.client.view.localeListener.LocaleListener;
import ru.storage.client.view.validator.exceptions.ValidationException;

import java.util.Locale;
import java.util.ResourceBundle;

public final class PersonValidator implements LocaleListener {
  private String wrongNameExceptionMessage;
  private String wrongPassportIDExceptionMessage;

  public PersonValidator() {
    changeLocale();
  }

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("localized.PersonValidator", Locale.getDefault());

    wrongNameExceptionMessage = resourceBundle.getString("exceptionMessages.wrongName");
    wrongPassportIDExceptionMessage = resourceBundle.getString("exceptionMessages.wrongPassportID");
  }

  public void checkName(String nameString) throws ValidationException {
    if (nameString == null || nameString.length() < 2 || nameString.length() > 100) {
      throw new ValidationException(wrongNameExceptionMessage);
    }
  }

  public void checkPassportID(String passportIDString) throws ValidationException {
    if (passportIDString == null
        || passportIDString.length() < 10
        || passportIDString.length() > 40) {
      throw new ValidationException(wrongPassportIDExceptionMessage);
    }
  }
}
