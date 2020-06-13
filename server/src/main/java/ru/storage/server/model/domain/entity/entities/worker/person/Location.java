package ru.storage.server.model.domain.entity.entities.worker.person;

import ru.storage.server.model.domain.dto.DTO;
import ru.storage.server.model.domain.dto.dtos.LocationDTO;
import ru.storage.server.model.domain.dto.exceptions.ValidationException;
import ru.storage.server.model.domain.entity.Entity;

import java.util.Objects;
import java.util.ResourceBundle;

public final class Location implements Cloneable, Entity {
  public static final long DEFAULT_ID = 0L;
  public static final long DEFAULT_OWNER_ID = 0L;

  public static final String ID_COLUMN = "id";
  public static final String OWNER_ID_COLUMN = "owner_id";
  public static final String TABLE_NAME = "locations";
  public static final String ADDRESS_COLUMN = "address";
  public static final String LATITUDE_COLUMN = "latitude";
  public static final String LONGITUDE_COLUMN = "longitude";

  private static final String WRONG_ID_EXCEPTION_MESSAGE;
  private static final String WRONG_OWNER_ID_EXCEPTION_MESSAGE;
  private static final String WRONG_ADDRESS_EXCEPTION_MESSAGE;
  private static final String WRONG_LATITUDE_EXCEPTION_MESSAGE;
  private static final String WRONG_LONGITUDE_EXCEPTION_MESSAGE;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.Location");

    WRONG_ID_EXCEPTION_MESSAGE = resourceBundle.getString("exceptionMessages.wrongId");
    WRONG_OWNER_ID_EXCEPTION_MESSAGE = resourceBundle.getString("exceptionMessages.wrongOwnerId");
    WRONG_ADDRESS_EXCEPTION_MESSAGE = resourceBundle.getString("exceptionMessages.wrongAddress");
    WRONG_LATITUDE_EXCEPTION_MESSAGE = resourceBundle.getString("exceptionMessages.wrongLatitude");
    WRONG_LONGITUDE_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.wrongLongitude");
  }

  private long id;
  private long ownerID;
  private String address;
  private Double latitude;
  private Double longitude;

  public Location(long id, long ownerID, String address, Double latitude, Double longitude)
      throws ValidationException {
    checkId(id);
    this.id = id;

    checkOwnerID(ownerID);
    this.ownerID = ownerID;

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

  public final long getID() {
    return id;
  }

  public final void setID(long id) throws ValidationException {
    checkId(id);
    this.id = id;
  }

  private void checkId(long id) throws ValidationException {
    if (id > 0 || id == DEFAULT_ID) {
      return;
    }

    throw new ValidationException(WRONG_ID_EXCEPTION_MESSAGE);
  }

  public final long getOwnerID() {
    return ownerID;
  }

  public final void setOwnerID(long ownerID) throws ValidationException {
    checkOwnerID(ownerID);
    this.ownerID = ownerID;
  }

  private void checkOwnerID(long ownerId) throws ValidationException {
    if (ownerId > 0 || ownerId == DEFAULT_OWNER_ID) {
      return;
    }

    throw new ValidationException(WRONG_OWNER_ID_EXCEPTION_MESSAGE);
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
