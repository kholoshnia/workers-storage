package ru.storage.server.model.domain.entity.entities.worker;

import ru.storage.server.model.domain.dto.dtos.CoordinatesDTO;
import ru.storage.server.model.domain.dto.dtos.PersonDTO;
import ru.storage.server.model.domain.dto.dtos.WorkerDTO;
import ru.storage.server.model.domain.entity.Entity;
import ru.storage.server.model.domain.entity.entities.worker.person.Person;
import ru.storage.server.model.domain.entity.exceptions.ValidationException;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.ResourceBundle;

public final class Worker implements Cloneable, Entity {
  public static final long DEFAULT_ID = 0L;
  public static final long DEFAULT_OWNER_ID = 0L;

  private static final String WRONG_ID_EXCEPTION;
  private static final String WRONG_OWNER_ID_EXCEPTION;
  private static final String WRONG_CREATION_DATE_EXCEPTION;
  private static final String WRONG_SALARY_EXCEPTION;
  private static final String WRONG_START_DATE_EXCEPTION;
  private static final String WRONG_PERSON_EXCEPTION;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.Worker");

    WRONG_ID_EXCEPTION = resourceBundle.getString("exceptions.wrongId");
    WRONG_OWNER_ID_EXCEPTION = resourceBundle.getString("exceptions.wrongOwnerId");
    WRONG_CREATION_DATE_EXCEPTION = resourceBundle.getString("exceptions.wrongCreationDate");
    WRONG_SALARY_EXCEPTION = resourceBundle.getString("exceptions.wrongSalary");
    WRONG_START_DATE_EXCEPTION = resourceBundle.getString("exceptions.wrongStartDate");
    WRONG_PERSON_EXCEPTION = resourceBundle.getString("exceptions.wrongPerson");
  }

  private long id;
  private long ownerId;
  private ZonedDateTime creationDate;
  private Float salary;
  private Status status;
  private ZonedDateTime startDate;
  private ZonedDateTime endDate;
  private Coordinates coordinates;
  private Person person;

  public Worker(
      long id,
      long ownerId,
      ZonedDateTime creationDate,
      Float salary,
      Status status,
      ZonedDateTime startDate,
      ZonedDateTime endDate,
      Coordinates coordinates,
      Person person)
      throws ValidationException {
    checkId(id);
    this.id = id;

    checkOwnerId(ownerId);
    this.ownerId = ownerId;

    checkCreationDate(creationDate);
    this.creationDate = creationDate;

    checkSalary(salary);
    this.salary = salary;

    this.status = status;

    checkStartDate(startDate);
    this.startDate = startDate;

    this.endDate = endDate;

    this.coordinates = coordinates;

    checkPerson(person);
    this.person = person;
  }

  @Override
  public WorkerDTO toDTO() {
    CoordinatesDTO coordinatesDTO;

    if (coordinates != null) {
      coordinatesDTO = coordinates.toDTO();
    } else {
      coordinatesDTO = null;
    }

    PersonDTO personDTO;

    if (person != null) {
      personDTO = person.toDTO();
    } else {
      personDTO = null;
    }

    return new WorkerDTO(
        id, ownerId, creationDate, salary, status, startDate, endDate, coordinatesDTO, personDTO);
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

  public ZonedDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(ZonedDateTime creationDate) throws ValidationException {
    checkCreationDate(creationDate);
    this.creationDate = creationDate;
  }

  private void checkCreationDate(ZonedDateTime creationDate) throws ValidationException {
    if (creationDate != null) {
      return;
    }

    throw new ValidationException(WRONG_CREATION_DATE_EXCEPTION);
  }

  public Float getSalary() {
    return salary;
  }

  public void setSalary(Float salary) throws ValidationException {
    checkSalary(salary);
    this.salary = salary;
  }

  private void checkSalary(Float salary) throws ValidationException {
    if (salary != null && salary > 0.0) {
      return;
    }

    throw new ValidationException(WRONG_SALARY_EXCEPTION);
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public ZonedDateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(ZonedDateTime startDate) throws ValidationException {
    checkStartDate(startDate);
    this.startDate = startDate;
  }

  private void checkStartDate(ZonedDateTime startDate) throws ValidationException {
    if (startDate != null) {
      return;
    }

    throw new ValidationException(WRONG_START_DATE_EXCEPTION);
  }

  public ZonedDateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(ZonedDateTime endDate) {
    this.endDate = endDate;
  }

  public Coordinates getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(Coordinates coordinates) {
    this.coordinates = coordinates;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) throws ValidationException {
    checkPerson(person);
    this.person = person;
  }

  private void checkPerson(Person person) throws ValidationException {
    if (person != null) {
      return;
    }

    throw new ValidationException(WRONG_PERSON_EXCEPTION);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Worker worker = (Worker) o;
    return Objects.equals(salary, worker.salary)
        && status == worker.status
        && Objects.equals(startDate, worker.startDate)
        && Objects.equals(endDate, worker.endDate)
        && Objects.equals(coordinates, worker.coordinates)
        && Objects.equals(person, worker.person);
  }

  @Override
  public int hashCode() {
    return Objects.hash(salary, status, startDate, endDate, coordinates, person);
  }

  @Override
  public Worker clone() {
    try {
      return new Worker(
          id, ownerId, creationDate, salary, status, startDate, endDate, coordinates, person);
    } catch (ValidationException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString() {
    return "Worker{"
        + "salary="
        + salary
        + ", status="
        + status
        + ", startDate="
        + startDate
        + ", endDate="
        + endDate
        + ", coordinates="
        + coordinates
        + ", person="
        + person
        + '}';
  }
}
