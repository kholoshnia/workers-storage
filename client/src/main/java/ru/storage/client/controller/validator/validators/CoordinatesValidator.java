package ru.storage.client.controller.validator.validators;

import ru.storage.client.controller.localeListener.LocaleListener;
import ru.storage.client.controller.validator.exceptions.ValidationException;

import java.util.Locale;
import java.util.ResourceBundle;

public final class CoordinatesValidator implements LocaleListener {
  private String wrongXException;
  private String wrongYException;
  private String wrongZException;

  public CoordinatesValidator() {
    changeLocale();
  }

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("localized.CoordinatesValidator", Locale.getDefault());

    wrongXException = resourceBundle.getString("exceptions.wrongX");
    wrongYException = resourceBundle.getString("exceptions.wrongY");
    wrongZException = resourceBundle.getString("exceptions.wrongZ");
  }

  public void checkX(String xString) throws ValidationException {
    double x;

    try {
      x = Double.parseDouble(xString);
    } catch (NumberFormatException | NullPointerException e) {
      throw new ValidationException(wrongXException);
    }

    if (x < -500.0 || x > 500.0) {
      throw new ValidationException(wrongXException);
    }
  }

  public void checkY(String yString) throws ValidationException {
    double y;

    try {
      y = Double.parseDouble(yString);
    } catch (NumberFormatException | NullPointerException e) {
      throw new ValidationException(wrongYException);
    }

    if (y < -500.0 || y > 500.0) {
      throw new ValidationException(wrongYException);
    }
  }

  public void checkZ(String zString) throws ValidationException {
    double z;

    try {
      z = Double.parseDouble(zString);
    } catch (NumberFormatException | NullPointerException e) {
      throw new ValidationException(wrongZException);
    }

    if (z < -500.0 || z > 500.0) {
      throw new ValidationException(wrongZException);
    }
  }
}
