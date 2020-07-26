package ru.storage.server.model.domain.entity.entities.worker.person;

import ru.storage.server.model.domain.dto.dtos.LocationDTO;
import ru.storage.server.model.domain.dto.dtos.PersonDTO;
import ru.storage.server.model.domain.entity.Entity;
import ru.storage.server.model.domain.entity.exceptions.ValidationException;

import java.util.Objects;
import java.util.ResourceBundle;

public final class Person implements Cloneable, Entity {
  public static final long DEFAULT_ID = 0L;
  public static final long DEFAULT_OWNER_ID = 0L;

  private static final String WRONG_ID_EXCEPTION;
  private static final String WRONG_OWNER_ID_EXCEPTION;
  private static final String WRONG_NAME_EXCEPTION;
  private static final String WRONG_PASSPORT_ID_EXCEPTION;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.Person");

    WRONG_ID_EXCEPTION = resourceBundle.getString("exceptions.wrongId");
    WRONG_OWNER_ID_EXCEPTION = resourceBundle.getString("exceptions.wrongOwnerId");
    WRONG_NAME_EXCEPTION = resourceBundle.getString("exceptions.wrongName");
    WRONG_PASSPORT_ID_EXCEPTION = resourceBundle.getString("exceptions.wrongPassportId");
  }

  private long id;
  private long ownerId;
  private String name;
  private String passportId;
  private Location location;

  public Person(long id, long ownerId, String name, String passportId, Location location)
      throws ValidationException {
    checkId(id);
    this.id = id;

    checkOwnerId(ownerId);
    this.ownerId = ownerId;

    checkName(name);
    this.name = name;

    checkPassportId(passportId);
    this.passportId = passportId;

    this.location = location;
  }

  @Override
  public PersonDTO toDTO() {
    LocationDTO locationDTO;

    if (location != null) {
      locationDTO = location.toDTO();
    } else {
      locationDTO = null;
    }

    return new PersonDTO(id, ownerId, name, passportId, locationDTO);
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

  public String getName() {
    return name;
  }

  public void setName(String name) throws ValidationException {
    checkName(name);
    this.name = name;
  }

  private void checkName(String name) throws ValidationException {
    if (name != null && name.length() >= 2 && name.length() <= 100) {
      return;
    }

    throw new ValidationException(WRONG_NAME_EXCEPTION);
  }

  public String getPassportId() {
    return passportId;
  }

  public void setPassportId(String passportId) throws ValidationException {
    checkPassportId(passportId);
    this.passportId = passportId;
  }

  private void checkPassportId(String passportId) throws ValidationException {
    if (passportId != null && passportId.length() >= 10 && passportId.length() <= 40) {

      return;
    }

    throw new ValidationException(WRONG_PASSPORT_ID_EXCEPTION);
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Person person = (Person) o;
    return Objects.equals(name, person.name)
        && Objects.equals(passportId, person.passportId)
        && Objects.equals(location, person.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, passportId, location);
  }

  @Override
  public Person clone() {
    try {
      return new Person(id, ownerId, name, passportId, location);
    } catch (ValidationException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString() {
    return "Person{"
        + "name='"
        + name
        + '\''
        + ", passportId='"
        + passportId
        + '\''
        + ", location="
        + location
        + '}';
  }
}
