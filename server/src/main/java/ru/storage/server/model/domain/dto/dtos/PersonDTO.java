package ru.storage.server.model.domain.dto.dtos;

import ru.storage.server.model.domain.dto.DTO;
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
  public final long ownerID;
  public final String name;
  public final String passportID;
  public final LocationDTO locationDTO;

  public PersonDTO(long id, long ownerID, String name, String passportID, LocationDTO locationDTO) {
    this.id = id;
    this.ownerID = ownerID;
    this.name = name;
    this.passportID = passportID;
    this.locationDTO = locationDTO;
  }

  @Override
  public Person toEntity() throws ValidationException {
    return new Person(
        this.id, this.ownerID, this.name, this.passportID, this.locationDTO.toEntity());
  }

  @Override
  public String toString() {
    return "PersonDTO{"
        + "id="
        + id
        + ", ownerID="
        + ownerID
        + ", name='"
        + name
        + '\''
        + ", passportID='"
        + passportID
        + '\''
        + ", locationDTO="
        + locationDTO
        + '}';
  }
}
