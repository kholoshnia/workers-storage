package ru.storage.client.controller.argumentFormer.argumentFormers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.argumentFormer.ArgumentValidator;
import ru.storage.client.controller.argumentFormer.exceptions.CancelException;
import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.client.view.console.Console;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;

import java.util.*;

public abstract class WorkerFormer extends Former implements LocaleListener {
  protected final ArgumentMediator argumentMediator;

  private final Logger logger;

  private String workerOffer;
  private String coordinatesOffer;
  private String personOffer;
  private String locationOffer;

  private Map<String, String> workerOffers;
  private Map<String, String> coordinatesOffers;
  private Map<String, String> personOffers;
  private Map<String, String> locationOffers;

  public WorkerFormer(
      CommandMediator commandMediator,
      Console console,
      Map<String, ArgumentValidator> validatorMap,
      ArgumentMediator argumentMediator) {
    super(commandMediator, console, validatorMap);
    logger = LogManager.getLogger(WorkerFormer.class);
    this.argumentMediator = argumentMediator;
  }

  private Map<String, String> initWorkerOffers(ResourceBundle resourceBundle) {
    return new LinkedHashMap<String, String>() {
      {
        put(
            argumentMediator.WORKER_SALARY,
            String.format("%s: ", resourceBundle.getString("offers.worker.salary")));
        put(
            argumentMediator.WORKER_STATUS,
            String.format("%s: ", resourceBundle.getString("offers.worker.status")));
        put(
            argumentMediator.WORKER_START_DATE,
            String.format(
                "%s: (%s): ",
                resourceBundle.getString("offers.worker.startDate"),
                resourceBundle.getString("offers.dateFormat")));
        put(
            argumentMediator.WORKER_END_DATE,
            String.format(
                "%s: (%s): ",
                resourceBundle.getString("offers.worker.endDate"),
                resourceBundle.getString("offers.dateFormat")));
      }
    };
  }

  private Map<String, String> initCoordinatesOffers(ResourceBundle resourceBundle) {
    return new LinkedHashMap<String, String>() {
      {
        put(
            argumentMediator.COORDINATES_X,
            String.format("%s: ", resourceBundle.getString("offers.coordinates.x")));
        put(
            argumentMediator.COORDINATES_Y,
            String.format("%s: ", resourceBundle.getString("offers.coordinates.y")));
        put(
            argumentMediator.COORDINATES_Z,
            String.format("%s: ", resourceBundle.getString("offers.coordinates.z")));
      }
    };
  }

  private Map<String, String> initPersonOffers(ResourceBundle resourceBundle) {
    return new LinkedHashMap<String, String>() {
      {
        put(
            argumentMediator.PERSON_NAME,
            String.format("%s: ", resourceBundle.getString("offers.person.name")));
        put(
            argumentMediator.PERSON_PASSPORT_ID,
            String.format("%s: ", resourceBundle.getString("offers.person.passportId")));
      }
    };
  }

  private Map<String, String> initLocationOffers(ResourceBundle resourceBundle) {
    return new LinkedHashMap<String, String>() {
      {
        put(
            argumentMediator.LOCATION_ADDRESS,
            String.format("%s: ", resourceBundle.getString("offers.location.address")));
        put(
            argumentMediator.LOCATION_LATITUDE,
            String.format("%s: ", resourceBundle.getString("offers.location.latitude")));
        put(
            argumentMediator.LOCATION_LONGITUDE,
            String.format("%s: ", resourceBundle.getString("offers.location.longitude")));
      }
    };
  }

  @Override
  public void changeLocale(Locale locale) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.WorkerFormer");

    workerOffer = resourceBundle.getString("offers.worker");
    coordinatesOffer = resourceBundle.getString("offers.coordinates");
    personOffer = resourceBundle.getString("offers.person");
    locationOffer = resourceBundle.getString("offers.location");

    workerOffers = initWorkerOffers(resourceBundle);
    coordinatesOffers = initCoordinatesOffers(resourceBundle);
    personOffers = initPersonOffers(resourceBundle);
    locationOffers = initLocationOffers(resourceBundle);
  }

  protected final Map<String, String> formWorker() throws CancelException {
    console.writeLine(workerOffer);

    Map<String, String> allArguments = new HashMap<>();
    allArguments.put(argumentMediator.WORKER, argumentMediator.INCLUDED);

    allArguments.putAll(readArguments(workerOffers));
    logger.info(() -> "Worker arguments were formed.");

    allArguments.putAll(formCoordinates());
    allArguments.putAll(formPerson());

    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }

  protected final Map<String, String> formCoordinates() throws CancelException {
    Map<String, String> coordinatesArguments = new HashMap<>();

    if (readArgumentQuestion(coordinatesOffer)) {
      coordinatesArguments.put(argumentMediator.COORDINATES, argumentMediator.INCLUDED);
      coordinatesArguments.putAll(readArguments(coordinatesOffers));
      logger.info(() -> "Coordinates arguments were formed.");
    } else {
      coordinatesArguments.put(argumentMediator.COORDINATES, null);
      logger.info(() -> "Coordinates arguments were not formed.");
    }

    return coordinatesArguments;
  }

  protected final Map<String, String> formPerson() throws CancelException {
    Map<String, String> personArguments = new HashMap<>();

    console.writeLine(personOffer);

    personArguments.put(argumentMediator.PERSON, argumentMediator.INCLUDED);
    personArguments.putAll(readArguments(personOffers));
    logger.info(() -> "Person arguments were formed.");

    personArguments.putAll(formLocation());
    return personArguments;
  }

  protected final Map<String, String> formLocation() throws CancelException {
    Map<String, String> locationArguments = new HashMap<>();

    if (readArgumentQuestion(locationOffer)) {
      locationArguments.put(argumentMediator.LOCATION, argumentMediator.INCLUDED);
      locationArguments.putAll(readArguments(locationOffers));
      logger.info(() -> "Person arguments were formed.");
    } else {
      locationArguments.put(argumentMediator.LOCATION, null);
      logger.info(() -> "Person arguments were not formed.");
    }

    return locationArguments;
  }
}
