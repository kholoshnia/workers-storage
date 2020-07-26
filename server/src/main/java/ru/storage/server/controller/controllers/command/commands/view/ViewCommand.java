package ru.storage.server.controller.controllers.command.commands.view;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.controllers.command.Command;
import ru.storage.server.controller.services.format.date.DateFormat;
import ru.storage.server.controller.services.format.number.NumberFormat;
import ru.storage.server.controller.services.format.status.StatusFormat;
import ru.storage.server.model.domain.entity.entities.worker.Coordinates;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.entity.entities.worker.person.Location;
import ru.storage.server.model.domain.entity.entities.worker.person.Person;
import ru.storage.server.model.domain.repository.repositories.workerRepository.WorkerRepository;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public abstract class ViewCommand extends Command {
  private static final String WORKER_PREFIX_PATTERN = "%s:";
  private static final String COORDINATES_PREFIX_PATTERN = "\t%s:";
  private static final String PERSON_PREFIX_PATTERN = "\t\t%s:";
  private static final String LOCATION_PREFIX_PATTERN = "\t\t\t%s:";
  private static final String WORKER_PATTERN = "\t%s: %s";
  private static final String COORDINATES_PATTERN = "\t\t%s: %s";
  private static final String PERSON_PATTERN = "\t\t\t%s: %s";
  private static final String LOCATION_PATTERN = "\t\t\t\t%s: %s";

  protected final Locale locale;
  protected final WorkerRepository workerRepository;
  protected final DateFormat dateFormat;
  protected final NumberFormat numberFormat;
  protected final StatusFormat statusFormat;
  protected final NumberFormat currencyFormat;

  protected final String WORKER_PREFIX;
  protected final String WORKER_ID_PREFIX;
  protected final String WORKER_OWNER_ID_PREFIX;
  protected final String WORKER_CREATION_DATE_PREFIX;
  protected final String WORKER_SALARY_PREFIX;
  protected final String WORKER_STATUS_PREFIX;
  protected final String WORKER_START_DATE_PREFIX;
  protected final String WORKER_END_DATE_PREFIX;
  protected final String COORDINATES_PREFIX;
  protected final String COORDINATES_OWNER_ID_PREFIX;
  protected final String COORDINATES_ID_PREFIX;
  protected final String COORDINATES_X_PREFIX;
  protected final String COORDINATES_Y_PREFIX;
  protected final String COORDINATES_Z_PREFIX;
  protected final String PERSON_PREFIX;
  protected final String PERSON_ID_PREFIX;
  protected final String PERSON_OWNER_ID_PREFIX;
  protected final String PERSON_NAME_PREFIX;
  protected final String PERSON_PASSWORD_ID_PREFIX;
  protected final String LOCATION_PREFIX;
  protected final String LOCATION_ID_PREFIX;
  protected final String LOCATION_OWNER_ID_PREFIX;
  protected final String LOCATION_ADDRESS_PREFIX;
  protected final String LOCATION_LATITUDE_PREFIX;
  protected final String LOCATION_LONGITUDE_PREFIX;

  private final Logger logger;

  public ViewCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      WorkerRepository workerRepository) {
    super(configuration, argumentMediator, arguments);
    logger = LogManager.getLogger(ViewCommand.class);
    this.locale = locale;
    this.workerRepository = workerRepository;
    numberFormat = NumberFormat.getNumberInstance(locale);
    dateFormat = DateFormat.getDateInstance(locale);
    statusFormat = StatusFormat.getStatusInstance(locale);
    currencyFormat = NumberFormat.getCurrencyInstance(locale);

    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.ViewCommand", locale);

    WORKER_PREFIX = resourceBundle.getString("prefixes.worker");
    WORKER_ID_PREFIX = resourceBundle.getString("prefixes.worker.id");
    WORKER_OWNER_ID_PREFIX = resourceBundle.getString("prefixes.worker.ownerId");
    WORKER_CREATION_DATE_PREFIX = resourceBundle.getString("prefixes.worker.creationDate");
    WORKER_SALARY_PREFIX = resourceBundle.getString("prefixes.worker.salary");
    WORKER_STATUS_PREFIX = resourceBundle.getString("prefixes.worker.status");
    WORKER_START_DATE_PREFIX = resourceBundle.getString("prefixes.worker.startDate");
    WORKER_END_DATE_PREFIX = resourceBundle.getString("prefixes.worker.endDate");
    COORDINATES_PREFIX = resourceBundle.getString("prefixes.coordinates");
    COORDINATES_ID_PREFIX = resourceBundle.getString("prefixes.coordinates.Id");
    COORDINATES_OWNER_ID_PREFIX = resourceBundle.getString("prefixes.coordinates.ownerId");
    COORDINATES_X_PREFIX = resourceBundle.getString("prefixes.coordinates.x");
    COORDINATES_Y_PREFIX = resourceBundle.getString("prefixes.coordinates.y");
    COORDINATES_Z_PREFIX = resourceBundle.getString("prefixes.coordinates.z");
    PERSON_PREFIX = resourceBundle.getString("prefixes.person");
    PERSON_ID_PREFIX = resourceBundle.getString("prefixes.person.id");
    PERSON_OWNER_ID_PREFIX = resourceBundle.getString("prefixes.person.ownerId");
    PERSON_NAME_PREFIX = resourceBundle.getString("prefixes.person.name");
    PERSON_PASSWORD_ID_PREFIX = resourceBundle.getString("prefixes.person.passwordId");
    LOCATION_PREFIX = resourceBundle.getString("prefixes.location");
    LOCATION_ID_PREFIX = resourceBundle.getString("prefixes.location.id");
    LOCATION_OWNER_ID_PREFIX = resourceBundle.getString("prefixes.location.ownerId");
    LOCATION_ADDRESS_PREFIX = resourceBundle.getString("prefixes.location.address");
    LOCATION_LATITUDE_PREFIX = resourceBundle.getString("prefixes.location.latitude");
    LOCATION_LONGITUDE_PREFIX = resourceBundle.getString("prefixes.location.longitude");
  }

  protected final void appendWorker(StringBuilder stringBuilder, Worker worker) {
    if (worker == null) {
      return;
    }

    stringBuilder
        .append(String.format(WORKER_PREFIX_PATTERN, WORKER_PREFIX))
        .append(System.lineSeparator())
        .append(String.format(WORKER_PATTERN, WORKER_ID_PREFIX, worker.getId()))
        .append(System.lineSeparator())
        .append(String.format(WORKER_PATTERN, WORKER_OWNER_ID_PREFIX, worker.getOwnerId()))
        .append(System.lineSeparator());

    if (worker.getCreationDate() != null) {
      stringBuilder
          .append(
              String.format(
                  WORKER_PATTERN,
                  WORKER_CREATION_DATE_PREFIX,
                  dateFormat.format(worker.getCreationDate())))
          .append(System.lineSeparator());
    }

    if (worker.getSalary() != null) {
      stringBuilder
          .append(
              String.format(
                  WORKER_PATTERN, WORKER_SALARY_PREFIX, currencyFormat.format(worker.getSalary())))
          .append(System.lineSeparator());
    }

    if (worker.getStatus() != null) {
      stringBuilder
          .append(
              String.format(
                  WORKER_PATTERN, WORKER_STATUS_PREFIX, statusFormat.format(worker.getStatus())))
          .append(System.lineSeparator());
    }

    if (worker.getStartDate() != null) {
      stringBuilder
          .append(
              String.format(
                  WORKER_PATTERN,
                  WORKER_START_DATE_PREFIX,
                  dateFormat.format(worker.getStartDate())))
          .append(System.lineSeparator());
    }

    if (worker.getEndDate() != null) {
      stringBuilder
          .append(
              String.format(
                  WORKER_PATTERN, WORKER_END_DATE_PREFIX, dateFormat.format(worker.getEndDate())))
          .append(System.lineSeparator());
    }

    appendCoordinates(stringBuilder, worker.getCoordinates());
    appendPerson(stringBuilder, worker.getPerson());

    logger.info(() -> "Worker was appended.");
  }

  protected final void appendCoordinates(StringBuilder stringBuilder, Coordinates coordinates) {
    if (coordinates == null) {
      return;
    }

    stringBuilder
        .append(String.format(COORDINATES_PREFIX_PATTERN, COORDINATES_PREFIX))
        .append(System.lineSeparator())
        .append(String.format(COORDINATES_PATTERN, COORDINATES_ID_PREFIX, coordinates.getId()))
        .append(System.lineSeparator())
        .append(
            String.format(
                COORDINATES_PATTERN, COORDINATES_OWNER_ID_PREFIX, coordinates.getOwnerId()))
        .append(System.lineSeparator());

    if (coordinates.getX() != null) {
      stringBuilder
          .append(String.format(COORDINATES_PATTERN, COORDINATES_X_PREFIX, coordinates.getX()))
          .append(System.lineSeparator());
    }

    if (coordinates.getY() != null) {
      stringBuilder
          .append(String.format(COORDINATES_PATTERN, COORDINATES_Y_PREFIX, coordinates.getY()))
          .append(System.lineSeparator());
    }

    if (coordinates.getZ() != null) {
      stringBuilder
          .append(String.format(COORDINATES_PATTERN, COORDINATES_Z_PREFIX, coordinates.getZ()))
          .append(System.lineSeparator());
    }

    logger.info(() -> "Coordinates was appended.");
  }

  protected final void appendPerson(StringBuilder stringBuilder, Person person) {
    if (person == null) {
      return;
    }

    stringBuilder
        .append(String.format(PERSON_PREFIX_PATTERN, PERSON_PREFIX))
        .append(System.lineSeparator())
        .append(String.format(PERSON_PATTERN, PERSON_ID_PREFIX, person.getId()))
        .append(System.lineSeparator())
        .append(String.format(PERSON_PATTERN, PERSON_OWNER_ID_PREFIX, person.getOwnerId()))
        .append(System.lineSeparator());

    if (person.getName() != null) {
      stringBuilder
          .append(String.format(PERSON_PATTERN, PERSON_NAME_PREFIX, person.getName()))
          .append(System.lineSeparator());
    }

    if (person.getPassportId() != null) {
      stringBuilder
          .append(String.format(PERSON_PATTERN, PERSON_PASSWORD_ID_PREFIX, person.getPassportId()))
          .append(System.lineSeparator());
    }

    appendLocation(stringBuilder, person.getLocation());

    logger.info(() -> "Person was appended.");
  }

  protected final void appendLocation(StringBuilder stringBuilder, Location location) {
    if (location == null) {
      return;
    }

    stringBuilder
        .append(String.format(LOCATION_PREFIX_PATTERN, LOCATION_PREFIX))
        .append(System.lineSeparator())
        .append(String.format(LOCATION_PATTERN, LOCATION_ID_PREFIX, location.getId()))
        .append(System.lineSeparator())
        .append(String.format(LOCATION_PATTERN, LOCATION_OWNER_ID_PREFIX, location.getOwnerId()))
        .append(System.lineSeparator());

    if (location.getAddress() != null) {
      stringBuilder
          .append(String.format(LOCATION_PATTERN, LOCATION_ADDRESS_PREFIX, location.getAddress()))
          .append(System.lineSeparator());
    }

    if (location.getLatitude() != null) {
      stringBuilder
          .append(String.format(LOCATION_PATTERN, LOCATION_LATITUDE_PREFIX, location.getLatitude()))
          .append(System.lineSeparator());
    }

    if (location.getLongitude() != null) {
      stringBuilder
          .append(
              String.format(LOCATION_PATTERN, LOCATION_LONGITUDE_PREFIX, location.getLongitude()))
          .append(System.lineSeparator());
    }

    logger.info(() -> "Location was appended.");
  }
}
