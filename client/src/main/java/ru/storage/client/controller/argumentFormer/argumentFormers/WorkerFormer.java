package ru.storage.client.controller.argumentFormer.argumentFormers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.argumentFormer.ArgumentValidator;
import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.client.view.console.Console;
import ru.storage.common.ArgumentMediator;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public abstract class WorkerFormer extends Former implements LocaleListener {
  protected final ArgumentMediator argumentMediator;

  private final Logger logger;

  private Map<String, String> workerOffers;
  private Map<String, String> coordinatesOffers;
  private Map<String, String> personOffers;
  private Map<String, String> locationOffers;

  public WorkerFormer(
      Console console,
      Map<String, ArgumentValidator> validatorMap,
      ArgumentMediator argumentMediator) {
    super(console, validatorMap);
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
            String.format("%s: ", resourceBundle.getString("offers.worker.startDate")));
        put(
            argumentMediator.WORKER_END_DATE,
            String.format("%s: ", resourceBundle.getString("offers.worker.endDate")));
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
  public void changeLocale() {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.WorkerFormer");

    workerOffers = initWorkerOffers(resourceBundle);
    coordinatesOffers = initCoordinatesOffers(resourceBundle);
    personOffers = initPersonOffers(resourceBundle);
    locationOffers = initLocationOffers(resourceBundle);
  }

  protected final Map<String, String> formWorker() {
    Map<String, String> allArguments = new HashMap<>(); // TODO: Ability not to enter field

    Map<String, String> workerArguments = readArguments(workerOffers, null, null);
    logger.info(() -> "Worker arguments were formed.");
    allArguments.putAll(workerArguments);

    Map<String, String> coordinatesArguments = readArguments(coordinatesOffers, null, null);
    logger.info(() -> "Coordinates arguments were formed.");
    allArguments.putAll(coordinatesArguments);

    Map<String, String> personArguments = readArguments(personOffers, null, null);
    logger.info(() -> "Person arguments were formed.");
    allArguments.putAll(personArguments);

    Map<String, String> locationArguments = readArguments(locationOffers, null, null);
    logger.info(() -> "Location arguments were formed.");
    allArguments.putAll(locationArguments);

    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
