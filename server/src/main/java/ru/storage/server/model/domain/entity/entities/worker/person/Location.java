package ru.storage.server.model.domain.entity.entities.worker.person;

import ru.storage.common.api.dto.DTO;
import ru.storage.common.api.dto.Entity;
import ru.storage.common.api.dto.exceptions.ValidationException;
import ru.storage.server.model.domain.dto.dtos.LocationDTO;
import ru.storage.server.model.domain.entity.Ownable;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public final class Location extends Ownable implements Cloneable, Entity {
  public static final String TABLE_NAME = "locations";
  public static final String ADDRESS_COLUMN = "address";
  public static final String LATITUDE_COLUMN = "latitude";
  public static final String LONGITUDE_COLUMN = "longitude";

  private static final String WRONG_ADDRESS_EXCEPTION_MESSAGE;
  private static final String WRONG_LATITUDE_EXCEPTION_MESSAGE;
  private static final String WRONG_LONGITUDE_EXCEPTION_MESSAGE;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.Location", Locale.ENGLISH);

    WRONG_ADDRESS_EXCEPTION_MESSAGE = resourceBundle.getString("exceptionMessages.wrongAddress");
    WRONG_LATITUDE_EXCEPTION_MESSAGE = resourceBundle.getString("exceptionMessages.wrongLatitude");
    WRONG_LONGITUDE_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.wrongLongitude");
  }

  private String address;
  private Double latitude;
  private Double longitude;

  public Location(long id, long ownerID, String address, Double latitude, Double longitude)
      throws ValidationException {
    super(id, ownerID);

    checkAddress(address);
    this.address = address;

    checkLatitude(latitude);
    this.latitude = latitude;

    checkLongitude(longitude);
    this.longitude = longitude;
  }

  @Override
  public DTO<Location> toDTO() {
    return new LocationDTO(this.id, this.ownerID, this.address, this.latitude, this.longitude);
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) throws ValidationException {
    checkAddress(address);
    this.address = address;
  }

  private void checkAddress(String address) throws ValidationException {
    if (address != null && address.length() >= 10 && address.length() <= 100) {

      return;
    }

    throw new ValidationException(WRONG_ADDRESS_EXCEPTION_MESSAGE);
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) throws ValidationException {
    checkLatitude(latitude);
    this.latitude = latitude;
  }

  private void checkLatitude(Double latitude) throws ValidationException {
    if (latitude != null && latitude >= -85.0 && latitude <= 85.0) {
      return;
    }

    throw new ValidationException(WRONG_LATITUDE_EXCEPTION_MESSAGE);
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) throws ValidationException {
    checkLongitude(longitude);
    this.longitude = longitude;
  }

  private void checkLongitude(Double longitude) throws ValidationException {
    if (longitude != null && longitude >= -180.0 && longitude <= 180.0) {
      return;
    }

    throw new ValidationException(WRONG_LONGITUDE_EXCEPTION_MESSAGE);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Location location = (Location) o;
    return Objects.equals(address, location.address)
        && Objects.equals(latitude, location.latitude)
        && Objects.equals(longitude, location.longitude);
  }

  @Override
  public int hashCode() {
    return Objects.hash(address, latitude, longitude);
  }

  @Override
  public Location clone() {
    try {
      return new Location(this.id, this.ownerID, this.address, this.latitude, this.longitude);
    } catch (ValidationException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString() {
    return "Location{"
        + "address='"
        + address
        + '\''
        + ", latitude="
        + latitude
        + ", longitude="
        + longitude
        + '}';
  }
}
