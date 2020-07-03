package ru.storage.server.model.domain.dto.dtos;

import ru.storage.server.model.domain.entity.exceptions.ValidationException;
import ru.storage.server.model.domain.entity.entities.worker.person.Location;
import ru.storage.server.model.domain.entity.entities.worker.person.Person;
import ru.storage.server.model.domain.dto.DTO;

public final class PersonDTO implements DTO<Person> {
  public final long id;
  public final long ownerID;
  public final String name;
  public final String passportID;
  public final DTO<Location> locationDTO;

  public PersonDTO(
      long id, long ownerID, String name, String passportID, DTO<Location> locationDTO) {
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
