package ru.storage.server.model.domain.dto.dtos;

import ru.storage.common.api.dto.DTO;
import ru.storage.common.api.dto.exceptions.ValidationException;
import ru.storage.server.model.domain.dto.OwnableDTO;
import ru.storage.server.model.domain.dto.Parser;
import ru.storage.server.model.domain.entity.ID;
import ru.storage.server.model.domain.entity.entities.worker.person.Location;

public final class LocationDTO extends OwnableDTO implements DTO<Location> {
  public final String address;

  public final Double latitude;

  public final Double longitude;

  public LocationDTO(long id, long ownerID, String address, Double latitude, Double longitude) {
    super(id, ownerID);
    this.address = address;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public LocationDTO(String addressString, String latitudeString, String longitudeString)
      throws ValidationException {
    super(ID.DEFAULT, ID.DEFAULT);
    this.address = Parser.parseString(addressString);
    this.latitude = Parser.parseDouble(latitudeString);
    this.longitude = Parser.parseDouble(longitudeString);
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
