package ru.storage.client.controller.validator.validators;

import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.client.controller.validator.exceptions.ValidationException;

import java.util.Locale;
import java.util.ResourceBundle;

public final class LocationValidator implements LocaleListener {
  private String wrongAddressException;
  private String wrongLatitudeException;
  private String wrongLongitudeException;

  @Override
  public void changeLocale(Locale locale) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.LocationValidator");

    wrongAddressException = resourceBundle.getString("exceptions.wrongAddress");
    wrongLatitudeException = resourceBundle.getString("exceptions.wrongLatitude");
    wrongLongitudeException = resourceBundle.getString("exceptions.wrongLongitude");
  }

  public void checkAddress(String addressString) throws ValidationException {
    if (addressString == null || addressString.length() < 10 || addressString.length() > 100) {
      throw new ValidationException(wrongAddressException);
    }
  }

  public void checkLatitude(String latitudeString) throws ValidationException {
    if (latitudeString == null || latitudeString.isEmpty()) {
      return;
    }

    double latitude;

    try {
      latitude = Double.parseDouble(latitudeString);
    } catch (NumberFormatException e) {
      throw new ValidationException(wrongLatitudeException, e);
    }

    if (latitude < -85.0 || latitude > 85.0) {
      throw new ValidationException(wrongLatitudeException);
    }
  }

  public void checkLongitude(String longitudeString) throws ValidationException {
    if (longitudeString == null || longitudeString.isEmpty()) {
      return;
    }

    double longitude;

    try {
      longitude = Double.parseDouble(longitudeString);
    } catch (NumberFormatException e) {
      throw new ValidationException(wrongLongitudeException, e);
    }

    if (longitude < -180.0 || longitude > 180.0) {
      throw new ValidationException(wrongLongitudeException);
    }
  }
}
