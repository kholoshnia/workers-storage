package ru.storage.server.model.domain.entity.entities.worker.person;

import ru.storage.server.model.domain.dto.DTO;
import ru.storage.server.model.domain.dto.dtos.PersonDTO;
import ru.storage.server.model.domain.entity.exceptions.ValidationException;
import ru.storage.server.model.domain.entity.Entity;

import java.util.Objects;
import java.util.ResourceBundle;

public final class Person implements Cloneable, Entity {
  public static final long DEFAULT_ID = 0L;
  public static final long DEFAULT_OWNER_ID = 0L;

  public static final String ID_COLUMN = "id";
  public static final String OWNER_ID_COLUMN = "owner_id";
  public static final String TABLE_NAME = "persons";
  public static final String NAME_COLUMN = "name";
  public static final String PASSPORT_ID_COLUMN = "passport_id";
  public static final String LOCATION_COLUMN = "location";

  private static final String WRONG_ID_EXCEPTION_MESSAGE;
  private static final String WRONG_OWNER_ID_EXCEPTION_MESSAGE;
  private static final String WRONG_NAME_EXCEPTION_MESSAGE;
  private static final String WRONG_PASSPORT_ID_EXCEPTION_MESSAGE;
  private static final String WRONG_LOCATION_EXCEPTION_MESSAGE;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.Person");

    WRONG_ID_EXCEPTION_MESSAGE = resourceBundle.getString("exceptionMessages.wrongId");
    WRONG_OWNER_ID_EXCEPTION_MESSAGE = resourceBundle.getString("exceptionMessages.wrongOwnerId");
    WRONG_NAME_EXCEPTION_MESSAGE = resourceBundle.getString("exceptionMessages.wrongName");
    WRONG_PASSPORT_ID_EXCEPTION_MESSAGE =
        resourceBundle.getString("exceptionMessages.wrongPassportID");
    WRONG_LOCATION_EXCEPTION_MESSAGE = resourceBundle.getString("exceptionMessages.wrongLocation");
  }

  private long id;
  private long ownerID;
  private String name;
  private String passportID;
  private Location location;

  public Person(long id, long ownerID, String name, String passportID, Location location)
      throws ValidationException {
    checkId(id);
    this.id = id;

    checkOwnerID(ownerID);
    this.ownerID = ownerID;

    checkName(name);
    this.name = name;

    checkPassportID(passportID);
    this.passportID = passportID;

    checkLocation(location);
    this.location = location;
  }

  @Override
  public DTO<Person> toDTO() {
    return new PersonDTO(this.id, this.ownerID, this.name, this.passportID, this.location.toDTO());
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

    throw new ValidationException(WRONG_NAME_EXCEPTION_MESSAGE);
  }

  public String getPassportID() {
    return passportID;
  }

  public void setPassportID(String passportID) throws ValidationException {
    checkPassportID(passportID);
    this.passportID = passportID;
  }

  private void checkPassportID(String passportID) throws ValidationException {
    if (passportID != null && passportID.length() >= 10 && passportID.length() <= 40) {

      return;
    }

    throw new ValidationException(WRONG_PASSPORT_ID_EXCEPTION_MESSAGE);
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) throws ValidationException {
    checkLocation(location);
    this.location = location;
  }

  private void checkLocation(Location location) throws ValidationException {
    if (location != null) {
      return;
    }

    throw new ValidationException(WRONG_LOCATION_EXCEPTION_MESSAGE);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Person person = (Person) o;
    return Objects.equals(name, person.name)
        && Objects.equals(passportID, person.passportID)
        && Objects.equals(location, person.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, passportID, location);
  }

  @Override
  public Person clone() {
    try {
      return new Person(this.id, this.ownerID, this.name, this.passportID, this.location);
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
        + ", passportID='"
        + passportID
        + '\''
        + ", location="
        + location
        + '}';
  }
}
