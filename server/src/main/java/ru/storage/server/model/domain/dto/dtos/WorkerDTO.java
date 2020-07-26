package ru.storage.server.model.domain.dto.dtos;

import ru.storage.server.model.domain.dto.DTO;
import ru.storage.server.model.domain.entity.entities.worker.Coordinates;
import ru.storage.server.model.domain.entity.entities.worker.Status;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.entity.entities.worker.person.Person;
import ru.storage.server.model.domain.entity.exceptions.ValidationException;

import java.time.ZonedDateTime;

public final class WorkerDTO implements DTO<Worker> {
  public static final String ID_COLUMN = "id";
  public static final String OWNER_ID_COLUMN = "owner_id";
  public static final String TABLE_NAME = "workers";
  public static final String CREATION_DATE_COLUMN = "creation_date";
  public static final String SALARY_COLUMN = "salary";
  public static final String STATUS_COLUMN = "status";
  public static final String START_DATE_COLUMN = "start_date";
  public static final String END_DATE_COLUMN = "end_date";
  public static final String COORDINATES_COLUMN = "coordinates";
  public static final String PERSON_COLUMN = "person";

  public final long id;
  public final long ownerId;
  public final ZonedDateTime creationDate;
  public final Float salary;
  public final Status status;
  public final ZonedDateTime startDate;
  public final ZonedDateTime endDate;
  public final CoordinatesDTO coordinatesDTO;
  public final PersonDTO personDTO;

  public WorkerDTO(
      long id,
      long ownerId,
      ZonedDateTime creationDate,
      Float salary,
      Status status,
      ZonedDateTime startDate,
      ZonedDateTime endDate,
      CoordinatesDTO coordinatesDTO,
      PersonDTO personDTO) {
    this.id = id;
    this.ownerId = ownerId;
    this.creationDate = creationDate;
    this.salary = salary;
    this.status = status;
    this.startDate = startDate;
    this.endDate = endDate;
    this.coordinatesDTO = coordinatesDTO;
    this.personDTO = personDTO;
  }

  @Override
  public Worker toEntity() throws ValidationException {
    Coordinates coordinates;

    if (coordinatesDTO != null) {
      coordinates = coordinatesDTO.toEntity();
    } else {
      coordinates = null;
    }

    Person person;

    if (personDTO != null) {
      person = personDTO.toEntity();
    } else {
      person = null;
    }

    return new Worker(
        id, ownerId, creationDate, salary, status, startDate, endDate, coordinates, person);
  }

  @Override
  public String toString() {
    return "WorkerDTO{"
        + "salary="
        + salary
        + ", status="
        + status
        + ", startDate="
        + startDate
        + ", endDate="
        + endDate
        + ", coordinatesDTO="
        + coordinatesDTO
        + ", personDTO="
        + personDTO
        + '}';
  }
}
