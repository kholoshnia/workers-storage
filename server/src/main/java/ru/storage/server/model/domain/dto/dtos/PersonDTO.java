package ru.storage.server.model.domain.dto.dtos;

import ru.storage.common.api.dto.DTO;
import ru.storage.common.api.dto.exceptions.ValidationException;
import ru.storage.server.model.domain.dto.OwnableDTO;
import ru.storage.server.model.domain.dto.Parser;
import ru.storage.server.model.domain.entity.ID;
import ru.storage.server.model.domain.entity.entities.worker.person.Location;
import ru.storage.server.model.domain.entity.entities.worker.person.Person;

import java.util.Objects;

public final class PersonDTO extends OwnableDTO implements DTO<Person> {
  public final String name;

  public final String passportID;

  public final DTO<Location> locationDTO;

  public PersonDTO(
      long id, long ownerID, String name, String passportID, DTO<Location> locationDTO) {
    super(id, ownerID);
    this.name = name;
    this.passportID = passportID;
    this.locationDTO = locationDTO;
  }

  public PersonDTO(String nameString, String passportIDString, DTO<Location> locationDTO) {
    super(ID.DEFAULT, ID.DEFAULT);
    this.name = Parser.parseString(nameString);
    this.passportID = Parser.parseString(passportIDString);
    this.locationDTO = locationDTO;
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
