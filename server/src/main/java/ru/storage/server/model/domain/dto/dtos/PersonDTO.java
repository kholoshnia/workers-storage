package ru.storage.server.model.domain.dto.dtos;

import ru.storage.server.model.domain.dto.DTO;
import ru.storage.server.model.domain.entity.entities.worker.person.Location;
import ru.storage.server.model.domain.entity.entities.worker.person.Person;
import ru.storage.server.model.domain.entity.exceptions.ValidationException;

public final class PersonDTO implements DTO<Person> {
  public static final String ID_COLUMN = "id";
  public static final String OWNER_ID_COLUMN = "owner_id";
  public static final String TABLE_NAME = "persons";
  public static final String NAME_COLUMN = "name";
  public static final String PASSPORT_ID_COLUMN = "passport_id";
  public static final String LOCATION_COLUMN = "location";

  public final long id;
  public final long ownerId;
  public final String name;
  public final String passportId;
  public final LocationDTO locationDTO;

  public PersonDTO(long id, long ownerId, String name, String passportId, LocationDTO locationDTO) {
    this.id = id;
    this.ownerId = ownerId;
    this.name = name;
    this.passportId = passportId;
    this.locationDTO = locationDTO;
  }

  @Override
  public Person toEntity() throws ValidationException {
    Location location;

    if (locationDTO != null) {
      location = locationDTO.toEntity();
    } else {
      location = null;
    }

    return new Person(id, ownerId, name, passportId, location);
  }

  @Override
  public String toString() {
    return "PersonDTO{"
        + "id="
        + id
        + ", ownerId="
        + ownerId
        + ", name='"
        + name
        + '\''
        + ", passportId='"
        + passportId
        + '\''
        + ", locationDTO="
        + locationDTO
        + '}';
  }
}
