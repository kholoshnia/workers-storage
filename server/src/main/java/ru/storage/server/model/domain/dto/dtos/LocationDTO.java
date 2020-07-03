package ru.storage.server.model.domain.dto.dtos;

import ru.storage.server.model.domain.dto.DTO;
import ru.storage.server.model.domain.entity.exceptions.ValidationException;
import ru.storage.server.model.domain.entity.entities.worker.person.Location;

public final class LocationDTO implements DTO<Location> {
  public final long id;
  public final long ownerID;
  public final String address;
  public final Double latitude;
  public final Double longitude;

  public LocationDTO(long id, long ownerID, String address, Double latitude, Double longitude) {
    this.id = id;
    this.ownerID = ownerID;
    this.address = address;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  @Override
  public Location toEntity() throws ValidationException {
    return new Location(this.id, this.ownerID, this.address, this.latitude, this.longitude);
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
