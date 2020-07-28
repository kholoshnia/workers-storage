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
  private static final Logger logger = LogManager.getLogger(ViewCommand.class);

  private static final String NAME_PATTERN = "%s:";
  private static final String EMPTY_PATTERN = "\t%s:";
  private static final String PREFIX_PATTERN = "\t%s: %s";

  protected final Locale locale;
  protected final WorkerRepository workerRepository;
  protected final DateFormat dateFormat;
  protected final NumberFormat numberFormat;
  protected final StatusFormat statusFormat;
  protected final NumberFormat currencyFormat;

  protected final String workerPrefix;
  protected final String workerIdPrefix;
  protected final String workerOwnerIdPrefix;
  protected final String workerCreationDatePrefix;
  protected final String workerSalaryPrefix;
  protected final String workerStatusPrefix;
  protected final String workerStartDatePrefix;
  protected final String workerEndDatePrefix;

  protected final String coordinatesPrefix;
  protected final String coordinatesOwnerIdPrefix;
  protected final String coordinatesIdPrefix;
  protected final String coordinatesXPrefix;
  protected final String coordinatesYPrefix;
  protected final String coordinatesZPrefix;

  protected final String personPrefix;
  protected final String personIdPrefix;
  protected final String personOwnerIdPrefix;
  protected final String personNamePrefix;
  protected final String personPassportIdPrefix;

  protected final String locationPrefix;
  protected final String locationIdPrefix;
  protected final String locationOwnerIdPrefix;
  protected final String locationAddressPrefix;
  protected final String locationLatitudePrefix;
  protected final String locationLongitudePrefix;

  public ViewCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      WorkerRepository workerRepository) {
    super(configuration, argumentMediator, arguments);
    this.locale = locale;
    this.workerRepository = workerRepository;
    numberFormat = NumberFormat.getNumberInstance(locale);
    dateFormat = DateFormat.getDateInstance(locale);
    statusFormat = StatusFormat.getStatusInstance(locale);
    currencyFormat = NumberFormat.getCurrencyInstance(locale);

    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.ViewCommand", locale);

    workerPrefix = resourceBundle.getString("prefixes.worker");
    workerIdPrefix = resourceBundle.getString("prefixes.worker.id");
    workerOwnerIdPrefix = resourceBundle.getString("prefixes.worker.ownerId");
    workerCreationDatePrefix = resourceBundle.getString("prefixes.worker.creationDate");
    workerSalaryPrefix = resourceBundle.getString("prefixes.worker.salary");
    workerStatusPrefix = resourceBundle.getString("prefixes.worker.status");
    workerStartDatePrefix = resourceBundle.getString("prefixes.worker.startDate");
    workerEndDatePrefix = resourceBundle.getString("prefixes.worker.endDate");

    coordinatesPrefix = resourceBundle.getString("prefixes.coordinates");
    coordinatesIdPrefix = resourceBundle.getString("prefixes.coordinates.Id");
    coordinatesOwnerIdPrefix = resourceBundle.getString("prefixes.coordinates.ownerId");
    coordinatesXPrefix = resourceBundle.getString("prefixes.coordinates.x");
    coordinatesYPrefix = resourceBundle.getString("prefixes.coordinates.y");
    coordinatesZPrefix = resourceBundle.getString("prefixes.coordinates.z");

    personPrefix = resourceBundle.getString("prefixes.person");
    personIdPrefix = resourceBundle.getString("prefixes.person.id");
    personOwnerIdPrefix = resourceBundle.getString("prefixes.person.ownerId");
    personNamePrefix = resourceBundle.getString("prefixes.person.name");
    personPassportIdPrefix = resourceBundle.getString("prefixes.person.passportId");

    locationPrefix = resourceBundle.getString("prefixes.location");
    locationIdPrefix = resourceBundle.getString("prefixes.location.id");
    locationOwnerIdPrefix = resourceBundle.getString("prefixes.location.ownerId");
    locationAddressPrefix = resourceBundle.getString("prefixes.location.address");
    locationLatitudePrefix = resourceBundle.getString("prefixes.location.latitude");
    locationLongitudePrefix = resourceBundle.getString("prefixes.location.longitude");
  }

  protected final String workerToString(Worker worker) {
    StringBuilder stringBuilder = new StringBuilder(String.format(NAME_PATTERN, workerPrefix));

    if (worker == null) {
      return stringBuilder.toString();
    }

    stringBuilder
        .append(System.lineSeparator())
        .append(String.format(PREFIX_PATTERN, workerIdPrefix, worker.getId()))
        .append(System.lineSeparator())
        .append(String.format(PREFIX_PATTERN, workerOwnerIdPrefix, worker.getOwnerId()))
        .append(System.lineSeparator());

    if (worker.getCreationDate() != null) {
      stringBuilder
          .append(
              String.format(
                  PREFIX_PATTERN,
                  workerCreationDatePrefix,
                  dateFormat.format(worker.getCreationDate())))
          .append(System.lineSeparator());
    } else {
      stringBuilder
          .append(String.format(EMPTY_PATTERN, workerCreationDatePrefix))
          .append(System.lineSeparator());
    }

    if (worker.getSalary() != null) {
      stringBuilder
          .append(
              String.format(
                  PREFIX_PATTERN, workerSalaryPrefix, currencyFormat.format(worker.getSalary())))
          .append(System.lineSeparator());
    } else {
      stringBuilder
          .append(String.format(EMPTY_PATTERN, workerSalaryPrefix))
          .append(System.lineSeparator());
    }

    if (worker.getStatus() != null) {
      stringBuilder
          .append(
              String.format(
                  PREFIX_PATTERN, workerStatusPrefix, statusFormat.format(worker.getStatus())))
          .append(System.lineSeparator());
    } else {
      stringBuilder
          .append(String.format(EMPTY_PATTERN, workerStatusPrefix))
          .append(System.lineSeparator());
    }

    if (worker.getStartDate() != null) {
      stringBuilder
          .append(
              String.format(
                  PREFIX_PATTERN, workerStartDatePrefix, dateFormat.format(worker.getStartDate())))
          .append(System.lineSeparator());
    } else {
      stringBuilder
          .append(String.format(EMPTY_PATTERN, workerStartDatePrefix))
          .append(System.lineSeparator());
    }

    if (worker.getEndDate() != null) {
      stringBuilder
          .append(
              String.format(
                  PREFIX_PATTERN, workerEndDatePrefix, dateFormat.format(worker.getEndDate())))
          .append(System.lineSeparator());
    } else {
      stringBuilder
          .append(String.format(EMPTY_PATTERN, workerEndDatePrefix))
          .append(System.lineSeparator());
    }

    String coordinatesString =
        coordinatesToString(worker.getCoordinates())
            .replaceAll(System.lineSeparator(), System.lineSeparator() + '\t');

    stringBuilder.append('\t').append(coordinatesString).append(System.lineSeparator());

    String personString =
        personToString(worker.getPerson())
            .replaceAll(System.lineSeparator(), System.lineSeparator() + '\t');

    stringBuilder.append('\t').append(personString);

    logger.info(() -> "Worker was converted to string.");
    return stringBuilder.toString();
  }

  protected final String coordinatesToString(Coordinates coordinates) {
    StringBuilder stringBuilder = new StringBuilder(String.format(NAME_PATTERN, coordinatesPrefix));

    if (coordinates == null) {
      return stringBuilder.toString();
    }

    stringBuilder
        .append(System.lineSeparator())
        .append(String.format(PREFIX_PATTERN, coordinatesIdPrefix, coordinates.getId()))
        .append(System.lineSeparator())
        .append(String.format(PREFIX_PATTERN, coordinatesOwnerIdPrefix, coordinates.getOwnerId()))
        .append(System.lineSeparator());

    if (coordinates.getX() != null) {
      stringBuilder
          .append(
              String.format(
                  PREFIX_PATTERN, coordinatesXPrefix, numberFormat.format(coordinates.getX())))
          .append(System.lineSeparator());
    } else {
      stringBuilder
          .append(String.format(EMPTY_PATTERN, coordinatesXPrefix))
          .append(System.lineSeparator());
    }

    if (coordinates.getY() != null) {
      stringBuilder
          .append(
              String.format(
                  PREFIX_PATTERN, coordinatesYPrefix, numberFormat.format(coordinates.getY())))
          .append(System.lineSeparator());
    } else {
      stringBuilder
          .append(String.format(EMPTY_PATTERN, coordinatesYPrefix))
          .append(System.lineSeparator());
    }

    if (coordinates.getZ() != null) {
      stringBuilder.append(
          String.format(
              PREFIX_PATTERN, coordinatesZPrefix, numberFormat.format(coordinates.getZ())));
    } else {
      stringBuilder.append(String.format(EMPTY_PATTERN, coordinatesZPrefix));
    }

    logger.info(() -> "Coordinates was converted to string.");
    return stringBuilder.toString();
  }

  protected final String personToString(Person person) {
    StringBuilder stringBuilder = new StringBuilder(String.format(NAME_PATTERN, personPrefix));

    if (person == null) {
      return stringBuilder.toString();
    }

    stringBuilder
        .append(System.lineSeparator())
        .append(String.format(PREFIX_PATTERN, personIdPrefix, person.getId()))
        .append(System.lineSeparator())
        .append(String.format(PREFIX_PATTERN, personOwnerIdPrefix, person.getOwnerId()))
        .append(System.lineSeparator());

    if (person.getName() != null) {
      stringBuilder
          .append(String.format(PREFIX_PATTERN, personNamePrefix, person.getName()))
          .append(System.lineSeparator());
    } else {
      stringBuilder
          .append(String.format(EMPTY_PATTERN, personNamePrefix))
          .append(System.lineSeparator());
    }

    if (person.getPassportId() != null) {
      stringBuilder
          .append(String.format(PREFIX_PATTERN, personPassportIdPrefix, person.getPassportId()))
          .append(System.lineSeparator());
    } else {
      stringBuilder
          .append(String.format(EMPTY_PATTERN, personPassportIdPrefix))
          .append(System.lineSeparator());
    }

    String locationString =
        locationToString(person.getLocation())
            .replaceAll(System.lineSeparator(), System.lineSeparator() + '\t');

    stringBuilder.append('\t').append(locationString);

    logger.info(() -> "Person was converted to string.");
    return stringBuilder.toString();
  }

  protected final String locationToString(Location location) {
    StringBuilder stringBuilder = new StringBuilder(String.format(NAME_PATTERN, locationPrefix));

    if (location == null) {
      return stringBuilder.toString();
    }

    stringBuilder
        .append(System.lineSeparator())
        .append(String.format(PREFIX_PATTERN, locationIdPrefix, location.getId()))
        .append(System.lineSeparator())
        .append(String.format(PREFIX_PATTERN, locationOwnerIdPrefix, location.getOwnerId()))
        .append(System.lineSeparator());

    if (location.getAddress() != null) {
      stringBuilder
          .append(String.format(PREFIX_PATTERN, locationAddressPrefix, location.getAddress()))
          .append(System.lineSeparator());
    } else {
      stringBuilder
          .append(String.format(EMPTY_PATTERN, locationAddressPrefix))
          .append(System.lineSeparator());
    }

    if (location.getLatitude() != null) {
      stringBuilder
          .append(
              String.format(
                  PREFIX_PATTERN,
                  locationLatitudePrefix,
                  numberFormat.format(location.getLatitude())))
          .append(System.lineSeparator());
    } else {
      stringBuilder
          .append(String.format(EMPTY_PATTERN, locationLatitudePrefix))
          .append(System.lineSeparator());
    }

    if (location.getLongitude() != null) {
      stringBuilder.append(
          String.format(
              PREFIX_PATTERN,
              locationLongitudePrefix,
              numberFormat.format(location.getLongitude())));
    } else {
      stringBuilder.append(String.format(EMPTY_PATTERN, locationLongitudePrefix));
    }

    logger.info(() -> "Location was converted to string.");
    return stringBuilder.toString();
  }
}
