package ru.storage.client.view.validator.validators;

import ru.storage.client.view.localeListener.LocaleListener;
import ru.storage.client.view.validator.exceptions.ValidationException;

import java.util.Locale;
import java.util.ResourceBundle;

public final class CoordinatesValidator implements LocaleListener {
  private String wrongXExceptionMessage;
  private String wrongYExceptionMessage;
  private String wrongZExceptionMessage;

  public CoordinatesValidator() {
    changeLocale();
  }

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("localized.CoordinatesValidator", Locale.getDefault());

    wrongXExceptionMessage = resourceBundle.getString("exceptionMessages.wrongX");
    wrongYExceptionMessage = resourceBundle.getString("exceptionMessages.wrongY");
    wrongZExceptionMessage = resourceBundle.getString("exceptionMessages.wrongZ");
  }

  public void checkX(String xString) throws ValidationException {
    double x;

    try {
      x = Double.parseDouble(xString);
    } catch (NumberFormatException | NullPointerException e) {
      throw new ValidationException(wrongXExceptionMessage);
    }

    if (x < -500.0 || x > 500.0) {
      throw new ValidationException(wrongXExceptionMessage);
    }
  }

  public void checkY(String yString) throws ValidationException {
    double y;

    try {
      y = Double.parseDouble(yString);
    } catch (NumberFormatException | NullPointerException e) {
      throw new ValidationException(wrongYExceptionMessage);
    }

    if (y < -500.0 || y > 500.0) {
      throw new ValidationException(wrongYExceptionMessage);
    }
  }

  public void checkZ(String zString) throws ValidationException {
    double z;

    try {
      z = Double.parseDouble(zString);
    } catch (NumberFormatException | NullPointerException e) {
      throw new ValidationException(wrongZExceptionMessage);
    }

    if (z < -500.0 || z > 500.0) {
      throw new ValidationException(wrongZExceptionMessage);
    }
  }
}
