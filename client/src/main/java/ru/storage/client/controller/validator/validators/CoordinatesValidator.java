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
    if (xString == null || xString.isEmpty()) {
      throw new ValidationException(wrongXException);
    }

    double x;

    try {
      x = Double.parseDouble(xString);
    } catch (NumberFormatException e) {
      throw new ValidationException(wrongXException, e);
    }

    if (x < -500.0 || x > 500.0) {
      throw new ValidationException(wrongXException);
    }
  }

  public void checkY(String yString) throws ValidationException {
    if (yString == null || yString.isEmpty()) {
      throw new ValidationException(wrongYException);
    }

    double y;

    try {
      y = Double.parseDouble(yString);
    } catch (NumberFormatException e) {
      throw new ValidationException(wrongYException, e);
    }

    if (y < -500.0 || y > 500.0) {
      throw new ValidationException(wrongYException);
    }
  }

  public void checkZ(String zString) throws ValidationException {
    if (zString == null || zString.isEmpty()) {
      return;
    }

    double z;

    try {
      z = Double.parseDouble(zString);
    } catch (NumberFormatException e) {
      throw new ValidationException(wrongZException, e);
    }

    if (z < -500.0 || z > 500.0) {
      throw new ValidationException(wrongZException);
    }
  }
}
