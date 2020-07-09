package ru.storage.server.model.domain.dto.dtos;

import ru.storage.server.model.domain.dto.DTO;
import ru.storage.server.model.domain.entity.entities.worker.person.Location;
import ru.storage.server.model.domain.entity.exceptions.ValidationException;

public final class LocationDTO implements DTO<Location> {
  public static final String ID_COLUMN = "id";
  public static final String OWNER_ID_COLUMN = "owner_id";
  public static final String TABLE_NAME = "locations";
  public static final String ADDRESS_COLUMN = "address";
  public static final String LATITUDE_COLUMN = "latitude";
  public static final String LONGITUDE_COLUMN = "longitude";

  public final long id;
  public final long ownerId;
  public final String address;
  public final Double latitude;
  public final Double longitude;

  public LocationDTO(long id, long ownerId, String address, Double latitude, Double longitude) {
    this.id = id;
    this.ownerId = ownerId;
    this.address = address;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  @Override
  public Location toEntity() throws ValidationException {
    return new Location(id, ownerId, address, latitude, longitude);
  }

  @Override
  public String toString() {
    return "LocationDTO{"
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
