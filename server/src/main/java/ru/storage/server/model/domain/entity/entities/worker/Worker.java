package ru.storage.server.model.domain.entity.entities.worker;

import ru.storage.server.model.domain.dto.dtos.WorkerDTO;
import ru.storage.server.model.domain.entity.Entity;
import ru.storage.server.model.domain.entity.entities.worker.person.Person;
import ru.storage.server.model.domain.entity.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ResourceBundle;

public final class Worker implements Cloneable, Entity {
  public static final long DEFAULT_ID = 0L;
  public static final long DEFAULT_OWNER_ID = 0L;

  private static final String WRONG_ID_EXCEPTION;
  private static final String WRONG_OWNER_ID_EXCEPTION;
  private static final String WRONG_CREATION_DATE_EXCEPTION;
  private static final String WRONG_SALARY_EXCEPTION;
  private static final String WRONG_STATUS_EXCEPTION;
  private static final String WRONG_START_DATE_EXCEPTION;
  private static final String WRONG_END_DATE_EXCEPTION;
  private static final String WRONG_COORDINATES_EXCEPTION;
  private static final String WRONG_PERSON_EXCEPTION;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.Worker");

    WRONG_ID_EXCEPTION = resourceBundle.getString("exceptions.wrongId");
    WRONG_OWNER_ID_EXCEPTION = resourceBundle.getString("exceptions.wrongOwnerId");
    WRONG_CREATION_DATE_EXCEPTION = resourceBundle.getString("exceptions.wrongCreationDate");
    WRONG_SALARY_EXCEPTION = resourceBundle.getString("exceptions.wrongSalary");
    WRONG_STATUS_EXCEPTION = resourceBundle.getString("exceptions.wrongStatus");
    WRONG_START_DATE_EXCEPTION = resourceBundle.getString("exceptions.wrongStartDate");
    WRONG_END_DATE_EXCEPTION = resourceBundle.getString("exceptions.wrongEndDate");
    WRONG_COORDINATES_EXCEPTION = resourceBundle.getString("exceptions.wrongCoordinates");
    WRONG_PERSON_EXCEPTION = resourceBundle.getString("exceptions.wrongPerson");
  }

  private long id;
  private long ownerID;
  private LocalDateTime creationDate;
  private Double salary;
  private Status status;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private Coordinates coordinates;
  private Person person;

  public Worker(
      long id,
      long ownerID,
      LocalDateTime creationDate,
      Double salary,
      Status status,
      LocalDateTime startDate,
      LocalDateTime endDate,
      Coordinates coordinates,
      Person person)
      throws ValidationException {
    checkId(id);
    this.id = id;

    checkOwnerID(ownerID);
    this.ownerID = ownerID;

    checkCreationDate(creationDate);
    this.creationDate = creationDate;

    checkSalary(salary);
    this.salary = salary;

    checkStatus(status);
    this.status = status;

    checkStartDate(startDate);
    this.startDate = startDate;

    checkEndDate(endDate);
    this.endDate = endDate;

    checkCoordinates(coordinates);
    this.coordinates = coordinates;

    checkPerson(person);
    this.person = person;
  }

  @Override
  public WorkerDTO toDTO() {
    return new WorkerDTO(
        this.id,
        this.ownerID,
        this.creationDate,
        this.salary,
        this.status,
        this.startDate,
        this.endDate,
        this.coordinates.toDTO(),
        this.person.toDTO());
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

    throw new ValidationException(WRONG_ID_EXCEPTION);
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

    throw new ValidationException(WRONG_OWNER_ID_EXCEPTION);
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDateTime creationDate) throws ValidationException {
    checkCreationDate(creationDate);
    this.creationDate = creationDate;
  }

  private void checkCreationDate(LocalDateTime creationDate) throws ValidationException {
    if (creationDate != null) {
      return;
    }

    throw new ValidationException(WRONG_CREATION_DATE_EXCEPTION);
  }

  public Double getSalary() {
    return salary;
  }

  public void setSalary(Double salary) throws ValidationException {
    checkSalary(salary);
    this.salary = salary;
  }

  private void checkSalary(Double salary) throws ValidationException {
    if (salary != null && salary > 0.0) {
      return;
    }

    throw new ValidationException(WRONG_SALARY_EXCEPTION);
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) throws ValidationException {
    checkStatus(status);
    this.status = status;
  }

  private void checkStatus(Status status) throws ValidationException {
    if (status != null) {
      return;
    }

    throw new ValidationException(WRONG_STATUS_EXCEPTION);
  }

  public LocalDateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDateTime startDate) throws ValidationException {
    checkStartDate(startDate);
    this.startDate = startDate;
  }

  private void checkStartDate(LocalDateTime startDate) throws ValidationException {
    if (startDate != null) {
      return;
    }

    throw new ValidationException(WRONG_START_DATE_EXCEPTION);
  }

  public LocalDateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDateTime endDate) throws ValidationException {
    checkEndDate(endDate);
    this.endDate = endDate;
  }

  private void checkEndDate(LocalDateTime endDate) throws ValidationException {
    if (endDate != null) {
      return;
    }

    throw new ValidationException(WRONG_END_DATE_EXCEPTION);
  }

  public Coordinates getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(Coordinates coordinates) throws ValidationException {
    checkCoordinates(coordinates);
    this.coordinates = coordinates;
  }

  private void checkCoordinates(Coordinates coordinates) throws ValidationException {
    if (coordinates != null) {
      return;
    }

    throw new ValidationException(WRONG_COORDINATES_EXCEPTION);
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
          this.id,
          this.ownerID,
          this.creationDate,
          this.salary,
          this.status,
          this.startDate,
          this.endDate,
          this.coordinates,
          this.person);
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
