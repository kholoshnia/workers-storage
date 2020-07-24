package ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.argumentFormers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.exceptions.WrongArgumentsException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class WorkerFormer extends Former {
  private final Logger logger;

  private final List<String> workerOffers;
  private final List<String> coordinatesOffers;
  private final List<String> personOffers;
  private final List<String> locationOffers;

  public WorkerFormer(ArgumentMediator argumentMediator) {
    super(argumentMediator);
    logger = LogManager.getLogger(WorkerFormer.class);
    workerOffers = initWorkerArguments();
    coordinatesOffers = initCoordinatesArguments();
    personOffers = initPersonArguments();
    locationOffers = initLocationArguments();
  }

  private List<String> initWorkerArguments() {
    return new ArrayList<String>() {
      {
        add(argumentMediator.WORKER_SALARY);
        add(argumentMediator.WORKER_STATUS);
        add(argumentMediator.WORKER_START_DATE);
        add(argumentMediator.WORKER_END_DATE);
      }
    };
  }

  private List<String> initCoordinatesArguments() {
    return new ArrayList<String>() {
      {
        add(argumentMediator.COORDINATES_X);
        add(argumentMediator.COORDINATES_Y);
        add(argumentMediator.COORDINATES_Z);
      }
    };
  }

  private List<String> initPersonArguments() {
    return new ArrayList<String>() {
      {
        add(argumentMediator.PERSON_NAME);
        add(argumentMediator.PERSON_PASSPORT_ID);
      }
    };
  }

  private List<String> initLocationArguments() {
    return new ArrayList<String>() {
      {
        add(argumentMediator.LOCATION_ADDRESS);
        add(argumentMediator.LOCATION_LATITUDE);
        add(argumentMediator.LOCATION_LONGITUDE);
      }
    };
  }

  protected final Map<String, String> formWorker(Iterator<String> script)
      throws WrongArgumentsException {
    Map<String, String> allArguments = readArguments(workerOffers, script);
    logger.info(() -> "Worker arguments were formed.");

    Map<String, String> coordinatesArguments = readArguments(coordinatesOffers, script);
    logger.info(() -> "Coordinates arguments were formed.");
    allArguments.putAll(coordinatesArguments);

    Map<String, String> personArguments = readArguments(personOffers, script);
    logger.info(() -> "Person arguments were formed.");
    allArguments.putAll(personArguments);

    Map<String, String> locationArguments = readArguments(locationOffers, script);
    logger.info(() -> "Location arguments were formed.");
    allArguments.putAll(locationArguments);

    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
