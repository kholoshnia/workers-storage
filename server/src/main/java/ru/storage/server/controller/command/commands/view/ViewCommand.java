package ru.storage.server.controller.command.commands.view;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.command.Command;
import ru.storage.server.controller.services.format.currency.CurrencyFormat;
import ru.storage.server.controller.services.format.status.StatusFormat;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.repository.repositories.workerRepository.WorkerRepository;

import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public abstract class ViewCommand extends Command {
  protected final Locale locale;
  protected final WorkerRepository workerRepository;
  protected final DateTimeFormatter dateFormat;
  protected final NumberFormat numberFormat;
  protected final StatusFormat statusFormat;
  protected final CurrencyFormat currencyFormat;

  private final String WORKER_PREFIX;
  private final String WORKER_ID_PREFIX;
  private final String WORKER_OWNER_ID_PREFIX;
  private final String WORKER_CREATION_DATE_PREFIX;
  private final String WORKER_SALARY_PREFIX;
  private final String WORKER_STATUS_PREFIX;
  private final String WORKER_START_DATE_PREFIX;
  private final String WORKER_END_DATE_PREFIX;
  private final String COORDINATES_PREFIX;
  private final String COORDINATES_OWNER_ID_PREFIX;
  private final String COORDINATES_ID_PREFIX;
  private final String COORDINATES_X_PREFIX;
  private final String COORDINATES_Y_PREFIX;
  private final String COORDINATES_Z_PREFIX;
  private final String PERSON_PREFIX;
  private final String PERSON_ID_PREFIX;
  private final String PERSON_OWNER_ID_PREFIX;
  private final String PERSON_NAME_PREFIX;
  private final String PERSON_PASSWORD_ID_PREFIX;
  private final String LOCATION_PREFIX;
  private final String LOCATION_ID_PREFIX;
  private final String LOCATION_OWNER_ID_PREFIX;
  private final String LOCATION_ADDRESS_PREFIX;
  private final String LOCATION_LATITUDE_PREFIX;
  private final String LOCATION_LONGITUDE_PREFIX;

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
    dateFormat =
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)
            .withLocale(locale)
            .withZone(ZoneId.systemDefault());
    statusFormat = StatusFormat.getStatusInstance(locale);
    currencyFormat = CurrencyFormat.getCurrencyInstance(locale);

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
    stringBuilder
        .append(String.format("%s: ", WORKER_PREFIX))
        .append(System.lineSeparator())
        .append(String.format("\t%s: %d", WORKER_ID_PREFIX, worker.getId()))
        .append(System.lineSeparator())
        .append(String.format("\t%s: %d", WORKER_OWNER_ID_PREFIX, worker.getOwnerId()))
        .append(System.lineSeparator())
        .append(
            String.format(
                "\t%s: %s",
                WORKER_CREATION_DATE_PREFIX, dateFormat.format(worker.getCreationDate())))
        .append(System.lineSeparator())
        .append(
            String.format(
                "\t%s: %s", WORKER_SALARY_PREFIX, currencyFormat.format(worker.getSalary())))
        .append(System.lineSeparator())
        .append(
            String.format(
                "\t%s: %s", WORKER_STATUS_PREFIX, statusFormat.format(worker.getStatus())))
        .append(System.lineSeparator())
        .append(
            String.format(
                "\t%s: %s", WORKER_START_DATE_PREFIX, dateFormat.format(worker.getStartDate())))
        .append(System.lineSeparator())
        .append(
            String.format(
                "\t%s: %s", WORKER_END_DATE_PREFIX, dateFormat.format(worker.getEndDate())))
        .append(System.lineSeparator())
        .append(String.format("\t%s: ", COORDINATES_PREFIX))
        .append(System.lineSeparator())
        .append(String.format("\t\t%s: %d", COORDINATES_ID_PREFIX, worker.getCoordinates().getId()))
        .append(System.lineSeparator())
        .append(String.format("\t\t%s: %d", COORDINATES_OWNER_ID_PREFIX, worker.getOwnerId()))
        .append(System.lineSeparator())
        .append(
            String.format(
                "\t\t%s: %s",
                COORDINATES_X_PREFIX, numberFormat.format(worker.getCoordinates().getX())))
        .append(System.lineSeparator())
        .append(
            String.format(
                "\t\t%s: %s",
                COORDINATES_Y_PREFIX, numberFormat.format(worker.getCoordinates().getY())))
        .append(System.lineSeparator())
        .append(
            String.format(
                "\t\t%s: %s",
                COORDINATES_Z_PREFIX, numberFormat.format(worker.getCoordinates().getZ())))
        .append(System.lineSeparator())
        .append(String.format("\t\t%s: ", PERSON_PREFIX))
        .append(System.lineSeparator())
        .append(String.format("\t\t\t%s: %d", PERSON_ID_PREFIX, worker.getPerson().getId()))
        .append(System.lineSeparator())
        .append(
            String.format("\t\t\t%s: %d", PERSON_OWNER_ID_PREFIX, worker.getPerson().getOwnerId()))
        .append(System.lineSeparator())
        .append(String.format("\t\t\t%s: %s", PERSON_NAME_PREFIX, worker.getPerson().getName()))
        .append(System.lineSeparator())
        .append(
            String.format(
                "\t\t\t%s: %s", PERSON_PASSWORD_ID_PREFIX, worker.getPerson().getPassportId()))
        .append(System.lineSeparator())
        .append(String.format("\t\t\t%s: ", LOCATION_PREFIX))
        .append(System.lineSeparator())
        .append(
            String.format(
                "\t\t\t\t%s: %d", LOCATION_ID_PREFIX, worker.getPerson().getLocation().getId()))
        .append(System.lineSeparator())
        .append(
            String.format(
                "\t\t\t\t%s: %d",
                LOCATION_OWNER_ID_PREFIX, worker.getPerson().getLocation().getOwnerId()))
        .append(System.lineSeparator())
        .append(
            String.format(
                "\t\t\t\t%s: %s",
                LOCATION_ADDRESS_PREFIX, worker.getPerson().getLocation().getAddress()))
        .append(System.lineSeparator())
        .append(
            String.format(
                "\t\t\t\t%s: %s",
                LOCATION_LATITUDE_PREFIX,
                numberFormat.format(worker.getPerson().getLocation().getLatitude())))
        .append(System.lineSeparator())
        .append(
            String.format(
                "\t\t\t\t%s: %s",
                LOCATION_LONGITUDE_PREFIX,
                numberFormat.format(worker.getPerson().getLocation().getLongitude())));

    logger.info(() -> "Worker was appended.");
  }
}
