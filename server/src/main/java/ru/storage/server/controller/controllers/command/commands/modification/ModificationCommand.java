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
  protected final String WRONG_ID_ANSWER;
  protected final String WORKER_NOT_FOUND_ANSWER;
  protected final String COLLECTION_IS_EMPTY_ANSWER;
  protected final String WRONG_WORKER_FORMAT_ANSWER;
  protected final String WRONG_WORKER_DATA_ANSWER;
  protected final String NOT_OWNER_ANSWER;

  protected final Locale locale;
  protected final Repository<Worker> workerRepository;
  protected final Parser parser;
  protected final User user;

  private final Logger logger;

  public ModificationCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      Repository<Worker> workerRepository,
      Parser parser,
      User user) {
    super(configuration, argumentMediator, arguments);
    logger = LogManager.getLogger(ModificationCommand.class);
    this.locale = locale;
    this.workerRepository = workerRepository;
    this.parser = parser;
    this.user = user;

    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("localized.ModificationCommand", locale);

    WRONG_ID_ANSWER = resourceBundle.getString("answers.wrongId");
    WORKER_NOT_FOUND_ANSWER = resourceBundle.getString("answers.workerNotFound");
    COLLECTION_IS_EMPTY_ANSWER = resourceBundle.getString("answers.collectionIsEmpty");
    WRONG_WORKER_FORMAT_ANSWER = resourceBundle.getString("answers.wrongWorkerFormat");
    WRONG_WORKER_DATA_ANSWER = resourceBundle.getString("answers.wrongWorkerData");
    NOT_OWNER_ANSWER = resourceBundle.getString("answers.notOwner");
  }

  protected final CoordinatesDTO createCoordinatesDTO(Map<String, String> arguments)
      throws ParserException {
    if (arguments.get(argumentMediator.COORDINATES) == null
        || !arguments.get(argumentMediator.COORDINATES).equals(argumentMediator.INCLUDED)) {
      return null;
    }

    Double x = parser.parseDouble(arguments.get(argumentMediator.COORDINATES_X));
    Double y = parser.parseDouble(arguments.get(argumentMediator.COORDINATES_Y));
    Double z = parser.parseDouble(arguments.get(argumentMediator.COORDINATES_Z));

    CoordinatesDTO coordinatesDTO =
        new CoordinatesDTO(Coordinates.DEFAULT_ID, Coordinates.DEFAULT_OWNER_ID, x, y, z);
    logger.info(() -> "Coordinates DTO was created.");
    return coordinatesDTO;
  }

  protected final LocationDTO createLocationDTO(Map<String, String> arguments)
      throws ParserException {
    if (arguments.get(argumentMediator.LOCATION) == null
        || !arguments.get(argumentMediator.LOCATION).equals(argumentMediator.INCLUDED)) {
      return null;
    }

    String address = parser.parseString(arguments.get(argumentMediator.LOCATION_ADDRESS));
    Double latitude = parser.parseDouble(arguments.get(argumentMediator.LOCATION_LONGITUDE));
    Double longitude = parser.parseDouble(arguments.get(argumentMediator.LOCATION_LATITUDE));

    LocationDTO locationDTO =
        new LocationDTO(
            Location.DEFAULT_ID, Location.DEFAULT_OWNER_ID, address, latitude, longitude);
    logger.info(() -> "Location DTO was created.");
    return locationDTO;
  }

  protected final PersonDTO createPersonDTO(Map<String, String> arguments) throws ParserException {
    if (arguments.get(argumentMediator.PERSON) == null
        || !arguments.get(argumentMediator.PERSON).equals(argumentMediator.INCLUDED)) {
      return null;
    }

    String name = parser.parseString(arguments.get(argumentMediator.PERSON_NAME));
    String passportId = parser.parseString(arguments.get(argumentMediator.PERSON_PASSPORT_ID));
    LocationDTO locationDTO = createLocationDTO(arguments);

    PersonDTO personDTO =
        new PersonDTO(Person.DEFAULT_ID, Person.DEFAULT_OWNER_ID, name, passportId, locationDTO);
    logger.info(() -> "Person DTO was created.");
    return personDTO;
  }

  /**
   * Creates new worker DTO instance using specified arguments.
   *
   * @param arguments user arguments
   * @return new worker DTO instance
   * @throws ParserException - in case of user arguments parse errors
   * @see #createCoordinatesDTO(Map)
   * @see #createLocationDTO(Map)
   * @see #createPersonDTO(Map)
   */
  protected final WorkerDTO createWorkerDTO(Map<String, String> arguments) throws ParserException {
    if (arguments.get(argumentMediator.WORKER) == null
        || !arguments.get(argumentMediator.WORKER).equals(argumentMediator.INCLUDED)) {
      return null;
    }

    Float salary = parser.parseFloat(arguments.get(argumentMediator.WORKER_SALARY));
    Status status = parser.parseStatus(arguments.get(argumentMediator.WORKER_STATUS), locale);
    ZonedDateTime startDate =
        parser.parseLocalDateTime(arguments.get(argumentMediator.WORKER_START_DATE));
    ZonedDateTime endDate =
        parser.parseLocalDateTime(arguments.get(argumentMediator.WORKER_END_DATE));
    CoordinatesDTO coordinatesDTO = createCoordinatesDTO(arguments);
    PersonDTO personDTO = createPersonDTO(arguments);

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
   * @param worker concrete worker
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
