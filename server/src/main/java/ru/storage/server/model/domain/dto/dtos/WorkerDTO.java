package ru.storage.server.model.domain.dto.dtos;

import ru.storage.server.model.domain.entity.exceptions.ValidationException;
import ru.storage.server.model.domain.entity.entities.worker.Coordinates;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.entity.entities.worker.person.Person;
import ru.storage.server.model.domain.dto.DTO;
import ru.storage.server.model.domain.entity.entities.worker.Status;

import java.time.LocalDateTime;

public final class WorkerDTO implements DTO<Worker> {
  public final long id;
  public final long ownerID;
  public final LocalDateTime creationDate;
  public final Double salary;
  public final Status status;
  public final LocalDateTime startDate;
  public final LocalDateTime endDate;
  public final DTO<Coordinates> coordinatesDTO;
  public final DTO<Person> personDTO;

  public WorkerDTO(
      long id,
      long ownerID,
      LocalDateTime creationDate,
      Double salary,
      Status status,
      LocalDateTime startDate,
      LocalDateTime endDate,
      DTO<Coordinates> coordinatesDTO,
      DTO<Person> personDTO) {
    this.id = id;
    this.ownerID = ownerID;
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
    return new Worker(
        this.id,
        this.ownerID,
        this.creationDate,
        this.salary,
        this.status,
        this.startDate,
        this.endDate,
        this.coordinatesDTO.toEntity(),
        this.personDTO.toEntity());
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
