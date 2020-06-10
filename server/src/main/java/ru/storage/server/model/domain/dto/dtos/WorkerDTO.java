package ru.storage.server.model.domain.dto.dtos;

import ru.storage.common.api.dto.DTO;
import ru.storage.common.api.dto.exceptions.ValidationException;
import ru.storage.server.model.domain.dto.OwnableDTO;
import ru.storage.server.model.domain.dto.Parser;
import ru.storage.server.model.domain.entity.ID;
import ru.storage.server.model.domain.entity.entities.worker.Coordinates;
import ru.storage.server.model.domain.entity.entities.worker.Status;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.entity.entities.worker.person.Person;

import java.time.LocalDateTime;

public final class WorkerDTO extends OwnableDTO implements DTO<Worker> {
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
    super(id, ownerID);
    this.creationDate = creationDate;
    this.salary = salary;
    this.status = status;
    this.startDate = startDate;
    this.endDate = endDate;
    this.coordinatesDTO = coordinatesDTO;
    this.personDTO = personDTO;
  }

  public WorkerDTO(
      String salaryString,
      String statusString,
      String startDateString,
      String endDateString,
      DTO<Coordinates> coordinatesDTO,
      DTO<Person> personDTO)
      throws ValidationException {
    super(ID.DEFAULT, ID.DEFAULT);
    this.creationDate = LocalDateTime.now();
    this.salary = Parser.parseDouble(salaryString);
    this.status = Parser.parseStatus(statusString);
    this.startDate = Parser.parseLocalDateTime(startDateString);
    this.endDate = Parser.parseLocalDateTime(endDateString);
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
