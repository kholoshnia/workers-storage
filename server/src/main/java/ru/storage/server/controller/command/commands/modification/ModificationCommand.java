package ru.storage.server.controller.command.commands.modification;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.command.Command;
import ru.storage.server.controller.services.parser.Parser;
import ru.storage.server.controller.services.parser.exceptions.ParserException;
import ru.storage.server.model.domain.dto.DTO;
import ru.storage.server.model.domain.dto.dtos.CoordinatesDTO;
import ru.storage.server.model.domain.dto.dtos.LocationDTO;
import ru.storage.server.model.domain.dto.dtos.PersonDTO;
import ru.storage.server.model.domain.dto.dtos.WorkerDTO;
import ru.storage.server.model.domain.entity.entities.worker.Coordinates;
import ru.storage.server.model.domain.entity.entities.worker.Status;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.entity.entities.worker.person.Location;
import ru.storage.server.model.domain.entity.entities.worker.person.Person;
import ru.storage.server.model.domain.repository.Repository;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public abstract class ModificationCommand extends Command {
  protected final String WRONG_ID_ANSWER;
  protected final String WORKER_NOT_FOUND_ANSWER;
  protected final String COLLECTION_IS_EMPTY_ANSWER;
  protected final String WRONG_WORKER_FORMAT_ANSWER;
  protected final String WRONG_WORKER_DATA_ANSWER;
  protected final Repository<Worker> workerRepository;
  protected final Parser parser;
  private final Logger logger;

  public ModificationCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      Repository<Worker> workerRepository,
      Parser parser) {
    super(configuration, argumentMediator, arguments);
    this.logger = LogManager.getLogger(ModificationCommand.class);
    this.workerRepository = workerRepository;
    this.parser = parser;

    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("localized.ModificationCommand", locale);

    WRONG_ID_ANSWER = resourceBundle.getString("answers.wrongID");
    WORKER_NOT_FOUND_ANSWER = resourceBundle.getString("answers.workerNotFound");
    COLLECTION_IS_EMPTY_ANSWER = resourceBundle.getString("answers.collectionIsEmpty");
    WRONG_WORKER_FORMAT_ANSWER = resourceBundle.getString("answers.wrongWorkerFormat");
    WRONG_WORKER_DATA_ANSWER = resourceBundle.getString("answers.wrongWorkerData");
  }

  protected final CoordinatesDTO createCoordinatesDTO(Map<String, String> arguments)
      throws ParserException {
    Double x = parser.parseDouble(arguments.get(argumentMediator.COORDINATES_X));
    Double y = parser.parseDouble(arguments.get(argumentMediator.COORDINATES_Y));
    Double z = parser.parseDouble(arguments.get(argumentMediator.COORDINATES_Z));

    CoordinatesDTO coordinatesDTO =
        new CoordinatesDTO(Coordinates.DEFAULT_ID, Coordinates.DEFAULT_OWNER_ID, x, y, z);
    logger.info(() -> "CoordinatesDTO hsa been created.");
    return coordinatesDTO;
  }

  protected final DTO<Location> createLocationDTO(Map<String, String> arguments)
      throws ParserException {
    String address = parser.parseString(arguments.get(argumentMediator.LOCATION_ADDRESS));
    Double latitude = parser.parseDouble(arguments.get(argumentMediator.LOCATION_LONGITUDE));
    Double longitude = parser.parseDouble(arguments.get(argumentMediator.LOCATION_LATITUDE));

    DTO<Location> locationDTO =
        new LocationDTO(
            Location.DEFAULT_ID, Location.DEFAULT_OWNER_ID, address, latitude, longitude);
    logger.info(() -> "LocationDTO has been created.");
    return locationDTO;
  }

  protected final DTO<Person> createPersonDTO(Map<String, String> arguments)
      throws ParserException {
    String name = parser.parseString(arguments.get(argumentMediator.PERSON_NAME));
    String passportID = parser.parseString(arguments.get(argumentMediator.PERSON_PASSPORT_ID));
    DTO<Location> locationDTO = createLocationDTO(arguments);

    DTO<Person> personDTO =
        new PersonDTO(Person.DEFAULT_ID, Person.DEFAULT_OWNER_ID, name, passportID, locationDTO);
    logger.info(() -> "PersonDTO has been created.");
    return personDTO;
  }

  protected final DTO<Worker> createWorkerDTO(Map<String, String> arguments)
      throws ParserException {
    Double salary = parser.parseDouble(arguments.get(argumentMediator.WORKER_SALARY));
    Status status = parser.parseStatus(arguments.get(argumentMediator.WORKER_STATUS));
    LocalDateTime startDate =
        parser.parseLocalDateTime(arguments.get(argumentMediator.WORKER_START_DATE));
    LocalDateTime endDate =
        parser.parseLocalDateTime(arguments.get(argumentMediator.WORKER_END_DATE));
    DTO<Coordinates> coordinatesDTO = createCoordinatesDTO(arguments);
    DTO<Person> personDTO = createPersonDTO(arguments);

    DTO<Worker> workerDTO =
        new WorkerDTO(
            Worker.DEFAULT_ID,
            Worker.DEFAULT_OWNER_ID,
            LocalDateTime.now(),
            salary,
            status,
            startDate,
            endDate,
            coordinatesDTO,
            personDTO);
    logger.info(() -> "WorkerDTO has been created.");
    return workerDTO;
  }
}
