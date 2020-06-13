package ru.storage.client.view.validator.validators;

import ru.storage.client.view.localeListener.LocaleListener;
import ru.storage.client.view.validator.exceptions.ValidationException;

import java.util.Locale;
import java.util.ResourceBundle;

public final class LocationValidator implements LocaleListener {
  private String wrongAddressExceptionMessage;
  private String wrongLatitudeExceptionMessage;
  private String wrongLongitudeExceptionMessage;

  public LocationValidator() {
    changeLocale();
  }

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("localized.LocationValidator", Locale.getDefault());

    wrongAddressExceptionMessage = resourceBundle.getString("exceptionMessages.wrongAddress");
    wrongLatitudeExceptionMessage = resourceBundle.getString("exceptionMessages.wrongLatitude");
    wrongLongitudeExceptionMessage = resourceBundle.getString("exceptionMessages.wrongLongitude");
  }

  public void checkAddress(String addressString) throws ValidationException {
    if (addressString == null || addressString.length() < 10 || addressString.length() > 100) {
      throw new ValidationException(wrongAddressExceptionMessage);
    }
  }

  public void checkLatitude(String latitudeString) throws ValidationException {
    double latitude;

    try {
      latitude = Double.parseDouble(latitudeString);
    } catch (NumberFormatException | NullPointerException e) {
      throw new ValidationException(wrongLatitudeExceptionMessage);
    }

    if (latitude < -85.0 || latitude > 85.0) {
      throw new ValidationException(wrongLatitudeExceptionMessage);
    }
  }

  public void checkLongitude(String longitudeString) throws ValidationException {
    double longitude;

    try {
      longitude = Double.parseDouble(longitudeString);
    } catch (NumberFormatException | NullPointerException e) {
      throw new ValidationException(wrongLongitudeExceptionMessage);
    }

    if (longitude < -180.0 || longitude > 180.0) {
      throw new ValidationException(wrongLongitudeExceptionMessage);
    }
  }
}
