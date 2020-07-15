package ru.storage.client.controller.argumentFormer.argumentFormers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.argumentFormer.ArgumentFormer;
import ru.storage.client.controller.argumentFormer.ArgumentValidator;
import ru.storage.client.controller.validator.exceptions.ValidationException;
import ru.storage.client.view.console.Console;
import ru.storage.common.ArgumentMediator;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public abstract class WorkerFormer extends ArgumentFormer {
  protected final ArgumentMediator argumentMediator;

  private final Logger logger;
  private final Console console;

  private Map<String, String> workerOffers;
  private Map<String, String> coordinatesOffers;
  private Map<String, String> personOffers;
  private Map<String, String> locationOffers;

  public WorkerFormer(
      Map<String, ArgumentValidator> argumentValidatorMap,
      ArgumentMediator argumentMediator,
      Console console) {
    super(argumentValidatorMap);
    logger = LogManager.getLogger(WorkerFormer.class);
    this.argumentMediator = argumentMediator;
    this.console = console;
  }

  private Map<String, String> initWorkerOffers(ResourceBundle resourceBundle) {
    return new LinkedHashMap<String, String>() {
      {
        put(argumentMediator.WORKER_SALARY, resourceBundle.getString("offers.worker.salary"));
        put(argumentMediator.WORKER_STATUS, resourceBundle.getString("offers.worker.status"));
        put(
            argumentMediator.WORKER_START_DATE,
            resourceBundle.getString("offers.worker.startDate"));
        put(argumentMediator.WORKER_END_DATE, resourceBundle.getString("offers.worker.endDate"));
      }
    };
  }

  private Map<String, String> initCoordinatesOffers(ResourceBundle resourceBundle) {
    return new LinkedHashMap<String, String>() {
      {
        put(argumentMediator.COORDINATES_X, resourceBundle.getString("offers.coordinates.x"));
        put(argumentMediator.COORDINATES_Y, resourceBundle.getString("offers.coordinates.y"));
        put(argumentMediator.COORDINATES_Z, resourceBundle.getString("offers.coordinates.z"));
      }
    };
  }

  private Map<String, String> initPersonOffers(ResourceBundle resourceBundle) {
    return new LinkedHashMap<String, String>() {
      {
        put(argumentMediator.PERSON_NAME, resourceBundle.getString("offers.person.name"));
        put(
            argumentMediator.PERSON_PASSPORT_ID,
            resourceBundle.getString("offers.person.passportId"));
      }
    };
  }

  private Map<String, String> initLocationOffers(ResourceBundle resourceBundle) {
    return new LinkedHashMap<String, String>() {
      {
        put(argumentMediator.LOCATION_ADDRESS, resourceBundle.getString("offers.location.address"));
        put(
            argumentMediator.LOCATION_LATITUDE,
            resourceBundle.getString("offers.location.latitude"));
        put(
            argumentMediator.LOCATION_LONGITUDE,
            resourceBundle.getString("offers.location.longitude"));
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
    Map<String, String> allArguments = new HashMap<>();

    workerOffers.putAll(coordinatesOffers); // TODO: Ability not to enter field
    workerOffers.putAll(personOffers);
    workerOffers.putAll(locationOffers);

    for (Map.Entry<String, String> offerEntry : workerOffers.entrySet()) {
      String argument = offerEntry.getKey();
      String offer = offerEntry.getValue();

      console.writeLine(offer);
      logger.info("Offered user input: {}.", () -> offer);

      String input = console.readLine(null, null);

      try {
        checkArgument(argument, input);
      } catch (ValidationException e) {
        console.writeLine(e.getMessage());
      }
    }

    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
