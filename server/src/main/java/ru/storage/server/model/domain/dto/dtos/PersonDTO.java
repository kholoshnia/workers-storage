package ru.storage.server.model.domain.dto.dtos;

import ru.storage.server.model.domain.dto.DTO;
import ru.storage.server.model.domain.dto.exceptions.ValidationException;
import ru.storage.server.model.domain.entity.entities.worker.person.Location;
import ru.storage.server.model.domain.entity.entities.worker.person.Person;

import java.util.Objects;

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

  public PersonDTO(String nameString, String passportIDString, DTO<Location> locationDTO) {
    this.id = Person.DEFAULT_ID;
    this.ownerID = Person.DEFAULT_OWNER_ID;
    this.name = parseString(nameString);
    this.passportID = parseString(passportIDString);
    this.locationDTO = locationDTO;
  }

  private String parseString(String value) {
    if (value == null || value.trim().isEmpty()) {
      return null;
    }

    return value;
  }

  @Override
  public Person toEntity() throws ValidationException {
    return new Person(
        this.id, this.ownerID, this.name, this.passportID, this.locationDTO.toEntity());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PersonDTO personDTO = (PersonDTO) o;
    return Objects.equals(name, personDTO.name)
        && Objects.equals(passportID, personDTO.passportID)
        && Objects.equals(locationDTO, personDTO.locationDTO);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, passportID, locationDTO);
  }
}
