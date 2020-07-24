package ru.storage.client.controller.validator.validators;

import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.client.controller.validator.exceptions.ValidationException;

import java.util.Locale;
import java.util.ResourceBundle;

public final class CoordinatesValidator implements LocaleListener {
  private String wrongXException;
  private String wrongYException;
  private String wrongZException;

  @Override
  public void changeLocale(Locale locale) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.CoordinatesValidator");

    wrongXException = resourceBundle.getString("exceptions.wrongX");
    wrongYException = resourceBundle.getString("exceptions.wrongY");
    wrongZException = resourceBundle.getString("exceptions.wrongZ");
  }

  public void checkX(String xString) throws ValidationException {
    if (xString == null) {
      throw new ValidationException(wrongXException);
    }

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
    if (yString == null) {
      throw new ValidationException(wrongYException);
    }

    double y;

    try {
      y = Double.parseDouble(yString);
    } catch (NumberFormatException e) {
      throw new ValidationException(wrongYException);
    }

    if (y < -500.0 || y > 500.0) {
      throw new ValidationException(wrongYException);
    }
  }

  public void checkZ(String zString) throws ValidationException {
    if (zString == null) {
      throw new ValidationException(wrongZException);
    }

    double z;

    try {
      z = Double.parseDouble(zString);
    } catch (NumberFormatException e) {
      throw new ValidationException(wrongZException);
    }

    if (z < -500.0 || z > 500.0) {
      throw new ValidationException(wrongZException);
    }
  }
}
