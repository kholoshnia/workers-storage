package ru.storage.client.controller.validator.validators;

import ru.storage.client.controller.localeListener.LocaleListener;
import ru.storage.client.controller.validator.exceptions.ValidationException;

import java.util.Locale;
import java.util.ResourceBundle;

public final class LocationValidator implements LocaleListener {
  private String wrongAddressException;
  private String wrongLatitudeException;
  private String wrongLongitudeException;

  public LocationValidator() {
    changeLocale();
  }

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("localized.LocationValidator", Locale.getDefault());

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
    double latitude;

    try {
      latitude = Double.parseDouble(latitudeString);
    } catch (NumberFormatException | NullPointerException e) {
      throw new ValidationException(wrongLatitudeException);
    }

    if (latitude < -85.0 || latitude > 85.0) {
      throw new ValidationException(wrongLatitudeException);
    }
  }

  public void checkLongitude(String longitudeString) throws ValidationException {
    double longitude;

    try {
      longitude = Double.parseDouble(longitudeString);
    } catch (NumberFormatException | NullPointerException e) {
      throw new ValidationException(wrongLongitudeException);
    }

    if (longitude < -180.0 || longitude > 180.0) {
      throw new ValidationException(wrongLongitudeException);
    }
  }
}
