package ru.storage.server.model.domain.entity.entities.worker.person;

import ru.storage.server.model.domain.dto.dtos.LocationDTO;
import ru.storage.server.model.domain.entity.Entity;
import ru.storage.server.model.domain.entity.exceptions.ValidationException;

import java.util.Objects;
import java.util.ResourceBundle;

public final class Location implements Cloneable, Entity {
  public static final long DEFAULT_ID = 0L;
  public static final long DEFAULT_OWNER_ID = 0L;

  private static final String WRONG_ID_EXCEPTION;
  private static final String WRONG_OWNER_ID_EXCEPTION;
  private static final String WRONG_ADDRESS_EXCEPTION;
  private static final String WRONG_LATITUDE_EXCEPTION;
  private static final String WRONG_LONGITUDE_EXCEPTION;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.Location");

    WRONG_ID_EXCEPTION = resourceBundle.getString("exceptions.wrongId");
    WRONG_OWNER_ID_EXCEPTION = resourceBundle.getString("exceptions.wrongOwnerId");
    WRONG_ADDRESS_EXCEPTION = resourceBundle.getString("exceptions.wrongAddress");
    WRONG_LATITUDE_EXCEPTION = resourceBundle.getString("exceptions.wrongLatitude");
    WRONG_LONGITUDE_EXCEPTION = resourceBundle.getString("exceptions.wrongLongitude");
  }

  private long id;
  private long ownerId;
  private String address;
  private Double latitude;
  private Double longitude;

  public Location(long id, long ownerId, String address, Double latitude, Double longitude)
      throws ValidationException {
    checkId(id);
    this.id = id;

    checkOwnerId(ownerId);
    this.ownerId = ownerId;

    checkAddress(address);
    this.address = address;

    checkLatitude(latitude);
    this.latitude = latitude;

    checkLongitude(longitude);
    this.longitude = longitude;
  }

  @Override
  public LocationDTO toDTO() {
    return new LocationDTO(id, ownerId, address, latitude, longitude);
  }

  public final long getId() {
    return id;
  }

  public final void setId(long id) throws ValidationException {
    checkId(id);
    this.id = id;
  }

  private void checkId(long id) throws ValidationException {
    if (id > 0 || id == DEFAULT_ID) {
      return;
    }

    throw new ValidationException(WRONG_ID_EXCEPTION);
  }

  public final long getOwnerId() {
    return ownerId;
  }

  public final void setOwnerId(long ownerId) throws ValidationException {
    checkOwnerId(ownerId);
    this.ownerId = ownerId;
  }

  private void checkOwnerId(long ownerId) throws ValidationException {
    if (ownerId > 0 || ownerId == DEFAULT_OWNER_ID) {
      return;
    }

    throw new ValidationException(WRONG_OWNER_ID_EXCEPTION);
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

    throw new ValidationException(WRONG_ADDRESS_EXCEPTION);
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) throws ValidationException {
    checkLatitude(latitude);
    this.latitude = latitude;
  }

  private void checkLatitude(Double latitude) throws ValidationException {
    if (latitude == null || latitude >= -85.0 && latitude <= 85.0) {
      return;
    }

    throw new ValidationException(WRONG_LATITUDE_EXCEPTION);
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) throws ValidationException {
    checkLongitude(longitude);
    this.longitude = longitude;
  }

  private void checkLongitude(Double longitude) throws ValidationException {
    if (longitude == null || longitude >= -180.0 && longitude <= 180.0) {
      return;
    }

    throw new ValidationException(WRONG_LONGITUDE_EXCEPTION);
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
      return new Location(id, ownerId, address, latitude, longitude);
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
