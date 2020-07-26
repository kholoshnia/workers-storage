package ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.argumentFormers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.exceptions.WrongArgumentsException;

import java.util.*;

public abstract class WorkerFormer extends Former {
  private final Logger logger;

  private final List<String> workerArguments;
  private final List<String> coordinatesArguments;
  private final List<String> personArguments;
  private final List<String> locationArguments;

  public WorkerFormer(ArgumentMediator argumentMediator) {
    super(argumentMediator);
    logger = LogManager.getLogger(WorkerFormer.class);
    workerArguments = initWorkerArguments();
    coordinatesArguments = initCoordinatesArguments();
    personArguments = initPersonArguments();
    locationArguments = initLocationArguments();
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
    Map<String, String> allArguments = new HashMap<>();

    if (readArgument(argumentMediator.WORKER, script).equals(argumentMediator.INCLUDED)) {
      allArguments.put(argumentMediator.WORKER, argumentMediator.INCLUDED);
      allArguments.putAll(readArguments(workerArguments, script));
      logger.info(() -> "Worker arguments were formed.");
    }

    if (readArgument(argumentMediator.COORDINATES, script).equals(argumentMediator.INCLUDED)) {
      allArguments.put(argumentMediator.COORDINATES, argumentMediator.INCLUDED);
      allArguments.putAll(readArguments(coordinatesArguments, script));
      logger.info(() -> "Coordinates arguments were formed.");
    }

    if (readArgument(argumentMediator.PERSON, script).equals(argumentMediator.INCLUDED)) {
      allArguments.put(argumentMediator.PERSON, argumentMediator.INCLUDED);
      allArguments.putAll(readArguments(personArguments, script));
      logger.info(() -> "Person arguments were formed.");
    }

    if (readArgument(argumentMediator.LOCATION, script).equals(argumentMediator.INCLUDED)) {
      allArguments.put(argumentMediator.LOCATION, argumentMediator.INCLUDED);
      allArguments.putAll(readArguments(locationArguments, script));
      logger.info(() -> "Location arguments were formed.");
    }

    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
