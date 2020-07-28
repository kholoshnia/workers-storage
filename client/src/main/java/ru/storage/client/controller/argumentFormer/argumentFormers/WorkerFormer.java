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
  private static final Logger logger = LogManager.getLogger(WorkerFormer.class);

  protected final ArgumentMediator argumentMediator;

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
    this.argumentMediator = argumentMediator;
  }

  private Map<String, String> initWorkerOffers(ResourceBundle resourceBundle) {
    return new LinkedHashMap<String, String>() {
      {
        put(
            argumentMediator.workerSalary,
            String.format("%s: ", resourceBundle.getString("offers.worker.salary")));
        put(
            argumentMediator.workerStatus,
            String.format("%s: ", resourceBundle.getString("offers.worker.status")));
        put(
            argumentMediator.workerStartDate,
            String.format("%s: ", resourceBundle.getString("offers.worker.startDate")));
        put(
            argumentMediator.workerEndDate,
            String.format("%s: ", resourceBundle.getString("offers.worker.endDate")));
      }
    };
  }

  private Map<String, String> initCoordinatesOffers(ResourceBundle resourceBundle) {
    return new LinkedHashMap<String, String>() {
      {
        put(
            argumentMediator.coordinatesX,
            String.format("%s: ", resourceBundle.getString("offers.coordinates.x")));
        put(
            argumentMediator.coordinatesY,
            String.format("%s: ", resourceBundle.getString("offers.coordinates.y")));
        put(
            argumentMediator.coordinatesZ,
            String.format("%s: ", resourceBundle.getString("offers.coordinates.z")));
      }
    };
  }

  private Map<String, String> initPersonOffers(ResourceBundle resourceBundle) {
    return new LinkedHashMap<String, String>() {
      {
        put(
            argumentMediator.personName,
            String.format("%s: ", resourceBundle.getString("offers.person.name")));
        put(
            argumentMediator.personPassportId,
            String.format("%s: ", resourceBundle.getString("offers.person.passportId")));
      }
    };
  }

  private Map<String, String> initLocationOffers(ResourceBundle resourceBundle) {
    return new LinkedHashMap<String, String>() {
      {
        put(
            argumentMediator.locationAddress,
            String.format("%s: ", resourceBundle.getString("offers.location.address")));
        put(
            argumentMediator.locationLatitude,
            String.format("%s: ", resourceBundle.getString("offers.location.latitude")));
        put(
            argumentMediator.locationLongitude,
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
    allArguments.put(argumentMediator.worker, argumentMediator.included);

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
      coordinatesArguments.put(argumentMediator.coordinates, argumentMediator.included);
      coordinatesArguments.putAll(readArguments(coordinatesOffers));
      logger.info(() -> "Coordinates arguments were formed.");
    } else {
      coordinatesArguments.put(argumentMediator.coordinates, null);
      logger.info(() -> "Coordinates arguments were not formed.");
    }

    return coordinatesArguments;
  }

  protected final Map<String, String> formPerson() throws CancelException {
    Map<String, String> personArguments = new HashMap<>();

    console.writeLine(personOffer);

    personArguments.put(argumentMediator.person, argumentMediator.included);
    personArguments.putAll(readArguments(personOffers));
    logger.info(() -> "Person arguments were formed.");

    personArguments.putAll(formLocation());
    return personArguments;
  }

  protected final Map<String, String> formLocation() throws CancelException {
    Map<String, String> locationArguments = new HashMap<>();

    if (readArgumentQuestion(locationOffer)) {
      locationArguments.put(argumentMediator.location, argumentMediator.included);
      locationArguments.putAll(readArguments(locationOffers));
      logger.info(() -> "Person arguments were formed.");
    } else {
      locationArguments.put(argumentMediator.location, null);
      logger.info(() -> "Person arguments were not formed.");
    }

    return locationArguments;
  }
}
