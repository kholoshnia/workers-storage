package ru.storage.server.controller.command.commands.modification;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.command.Command;
import ru.storage.server.model.domain.dto.dtos.CoordinatesDTO;
import ru.storage.server.model.domain.dto.dtos.LocationDTO;
import ru.storage.server.model.domain.dto.dtos.PersonDTO;
import ru.storage.server.model.domain.dto.dtos.WorkerDTO;
import ru.storage.server.model.domain.dto.exceptions.ValidationException;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.repository.Repository;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public abstract class ModificationCommand extends Command {
  protected final String WRONG_ID_ANSWER;
  protected final String WORKER_NOT_FOUND_ANSWER;
  protected final String COLLECTION_IS_EMPTY_ANSWER;
  protected final Repository<Worker> workerRepository;
  private final Logger logger;

  public ModificationCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      Repository<Worker> workerRepository) {
    super(configuration, argumentMediator, arguments, locale);
    this.logger = LogManager.getLogger(ModificationCommand.class);
    this.workerRepository = workerRepository;

    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("localized.ModificationCommand", locale);

    WRONG_ID_ANSWER = resourceBundle.getString("answers.wrongID");
    WORKER_NOT_FOUND_ANSWER = resourceBundle.getString("answers.workerNotFound");
    COLLECTION_IS_EMPTY_ANSWER = resourceBundle.getString("answers.collectionIsEmpty");
  }

  protected final WorkerDTO createWorkerDTO(Map<String, String> arguments)
      throws ValidationException {
    CoordinatesDTO coordinatesDTO =
        new CoordinatesDTO(
            arguments.get(argumentMediator.COORDINATES_X),
            arguments.get(argumentMediator.COORDINATES_Y),
            arguments.get(argumentMediator.COORDINATES_Z));
    logger.info("CoordinatesDTO was created SUCCESSFULLY.");

    LocationDTO locationDTO =
        new LocationDTO(
            arguments.get(argumentMediator.LOCATION_ADDRESS),
            arguments.get(argumentMediator.LOCATION_LATITUDE),
            arguments.get(argumentMediator.LOCATION_LONGITUDE));
    logger.info("LocationDTO was created SUCCESSFULLY.");

    PersonDTO personDTO =
        new PersonDTO(
            arguments.get(argumentMediator.PERSON_NAME),
            arguments.get(argumentMediator.PERSON_PASSPORT_ID),
            locationDTO);
    logger.info("PersonDTO was created SUCCESSFULLY.");

    WorkerDTO workerDTO =
        new WorkerDTO(
            arguments.get(argumentMediator.WORKER_SALARY),
            arguments.get(argumentMediator.WORKER_STATUS),
            arguments.get(argumentMediator.WORKER_START_DATE),
            arguments.get(argumentMediator.WORKER_END_DATE),
            coordinatesDTO,
            personDTO);
    logger.info("WorkerDTO was created SUCCESSFULLY.");

    return workerDTO;
  }
}
