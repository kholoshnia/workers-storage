package ru.storage.server.controller.controllers.command.commands.modification;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.controllers.command.Command;
import ru.storage.server.controller.services.parser.Parser;
import ru.storage.server.controller.services.parser.exceptions.ParserException;
import ru.storage.server.model.domain.dto.dtos.CoordinatesDTO;
import ru.storage.server.model.domain.dto.dtos.LocationDTO;
import ru.storage.server.model.domain.dto.dtos.PersonDTO;
import ru.storage.server.model.domain.dto.dtos.WorkerDTO;
import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.entity.entities.worker.Coordinates;
import ru.storage.server.model.domain.entity.entities.worker.Status;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.entity.entities.worker.person.Location;
import ru.storage.server.model.domain.entity.entities.worker.person.Person;
import ru.storage.server.model.domain.entity.exceptions.ValidationException;
import ru.storage.server.model.domain.repository.Repository;

import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public abstract class ModificationCommand extends Command {
  private static final Logger logger = LogManager.getLogger(ModificationCommand.class);

  protected final String wrongIdAnswer;
  protected final String workerNotFoundAnswer;
  protected final String wrongWorkerFormatAnswer;
  protected final String wrongWorkerDataAnswer;
  protected final String notOwnerAnswer;

  protected final Locale locale;
  protected final User user;
  protected final Repository<Worker> workerRepository;
  protected final Parser parser;

  public ModificationCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      User user,
      Repository<Worker> workerRepository,
      Parser parser) {
    super(configuration, argumentMediator, arguments);
    this.locale = locale;
    this.user = user;
    this.workerRepository = workerRepository;
    this.parser = parser;

    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("localized.ModificationCommand", locale);

    wrongIdAnswer = resourceBundle.getString("answers.wrongId");
    workerNotFoundAnswer = resourceBundle.getString("answers.workerNotFound");
    wrongWorkerFormatAnswer = resourceBundle.getString("answers.wrongWorkerFormat");
    wrongWorkerDataAnswer = resourceBundle.getString("answers.wrongWorkerData");
    notOwnerAnswer = resourceBundle.getString("answers.notOwner");
  }

  /**
   * Creates new coordinates DTO instance using the arguments.
   *
   * @return new coordinates DTO instance
   * @throws ParserException - in case of user arguments parse errors
   */
  protected final CoordinatesDTO createCoordinatesDTO() throws ParserException {
    if (arguments.get(argumentMediator.coordinates) == null
        || !arguments.get(argumentMediator.coordinates).equals(argumentMediator.include)) {
      return null;
    }

    Double x = parser.parseDouble(arguments.get(argumentMediator.coordinatesX));
    Double y = parser.parseDouble(arguments.get(argumentMediator.coordinatesY));
    Double z = parser.parseDouble(arguments.get(argumentMediator.coordinatesZ));

    CoordinatesDTO coordinatesDTO =
        new CoordinatesDTO(Coordinates.DEFAULT_ID, Coordinates.DEFAULT_OWNER_ID, x, y, z);
    logger.info(() -> "Coordinates DTO was created.");
    return coordinatesDTO;
  }

  /**
   * Creates new location DTO instance using the arguments.
   *
   * @return new location DTO instance
   * @throws ParserException - in case of user arguments parse errors
   */
  protected final LocationDTO createLocationDTO() throws ParserException {
    if (arguments.get(argumentMediator.location) == null
        || !arguments.get(argumentMediator.location).equals(argumentMediator.include)) {
      return null;
    }

    String address = parser.parseString(arguments.get(argumentMediator.locationAddress));
    Double latitude = parser.parseDouble(arguments.get(argumentMediator.locationLongitude));
    Double longitude = parser.parseDouble(arguments.get(argumentMediator.locationLatitude));

    LocationDTO locationDTO =
        new LocationDTO(
            Location.DEFAULT_ID, Location.DEFAULT_OWNER_ID, address, latitude, longitude);
    logger.info(() -> "Location DTO was created.");
    return locationDTO;
  }

  /**
   * Creates new person DTO instance using the arguments.
   *
   * @return new person DTO instance
   * @throws ParserException - in case of user arguments parse errors
   * @see #createLocationDTO
   */
  protected final PersonDTO createPersonDTO() throws ParserException {
    if (arguments.get(argumentMediator.person) == null
        || !arguments.get(argumentMediator.person).equals(argumentMediator.include)) {
      return null;
    }

    String name = parser.parseString(arguments.get(argumentMediator.personName));
    String passportId = parser.parseString(arguments.get(argumentMediator.personPassportId));
    LocationDTO locationDTO = createLocationDTO();

    PersonDTO personDTO =
        new PersonDTO(Person.DEFAULT_ID, Person.DEFAULT_OWNER_ID, name, passportId, locationDTO);
    logger.info(() -> "Person DTO was created.");
    return personDTO;
  }

  /**
   * Creates new worker DTO instance using the arguments.
   *
   * @return new worker DTO instance
   * @throws ParserException - in case of user arguments parse errors
   * @see #createCoordinatesDTO
   * @see #createPersonDTO
   */
  protected final WorkerDTO createWorkerDTO() throws ParserException {
    if (arguments.get(argumentMediator.worker) == null
        || !arguments.get(argumentMediator.worker).equals(argumentMediator.include)) {
      return null;
    }

    Float salary = parser.parseFloat(arguments.get(argumentMediator.workerSalary));
    Status status = parser.parseStatus(arguments.get(argumentMediator.workerStatus), locale);
    ZonedDateTime startDate =
        parser.parseLocalDateTime(arguments.get(argumentMediator.workerStartDate));
    ZonedDateTime endDate =
        parser.parseLocalDateTime(arguments.get(argumentMediator.workerEndDate));
    CoordinatesDTO coordinatesDTO = createCoordinatesDTO();
    PersonDTO personDTO = createPersonDTO();

    WorkerDTO workerDTO =
        new WorkerDTO(
            Worker.DEFAULT_ID,
            Worker.DEFAULT_OWNER_ID,
            ZonedDateTime.now(),
            salary,
            status,
            startDate,
            endDate,
            coordinatesDTO,
            personDTO);
    logger.info(() -> "Worker DTO was created.");
    return workerDTO;
  }

  /**
   * Sets worker owner id.
   *
   * @param worker worker to set id
   * @throws ValidationException - in case of id validation errors
   */
  protected final void setOwnerId(Worker worker) throws ValidationException {
    worker.setOwnerId(user.getId());

    if (worker.getCoordinates() != null) {
      worker.getCoordinates().setOwnerId(user.getId());
    }

    if (worker.getPerson() != null) {
      worker.getPerson().setOwnerId(user.getId());

      if (worker.getPerson().getLocation() != null) {
        worker.getPerson().getLocation().setOwnerId(user.getId());
      }
    }
  }

  /**
   * Sets new id for worker based on the old worker.
   *
   * @param oldWorker old worker
   * @param newWorker new worker
   * @throws ValidationException - in case of id validation errors
   */
  protected final void setId(Worker oldWorker, Worker newWorker) throws ValidationException {
    newWorker.setId(oldWorker.getId());

    if (newWorker.getCoordinates() != null) {
      newWorker.getCoordinates().setId(oldWorker.getId());
    }

    if (newWorker.getPerson() != null) {
      newWorker.getPerson().setId(oldWorker.getId());

      if (newWorker.getPerson().getLocation() != null) {
        newWorker.getPerson().getLocation().setId(oldWorker.getId());
      }
    }
  }
}
