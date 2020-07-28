package ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.argumentFormers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.exceptions.WrongArgumentsException;

import java.util.*;

public abstract class WorkerFormer extends Former {
  private static final Logger logger = LogManager.getLogger(WorkerFormer.class);

  private final List<String> workerArguments;
  private final List<String> coordinatesArguments;
  private final List<String> personArguments;
  private final List<String> locationArguments;

  public WorkerFormer(ArgumentMediator argumentMediator) {
    super(argumentMediator);
    workerArguments = initWorkerArguments();
    coordinatesArguments = initCoordinatesArguments();
    personArguments = initPersonArguments();
    locationArguments = initLocationArguments();
  }

  private List<String> initWorkerArguments() {
    return new ArrayList<String>() {
      {
        add(argumentMediator.workerSalary);
        add(argumentMediator.workerStatus);
        add(argumentMediator.workerStartDate);
        add(argumentMediator.workerEndDate);
      }
    };
  }

  private List<String> initCoordinatesArguments() {
    return new ArrayList<String>() {
      {
        add(argumentMediator.coordinatesX);
        add(argumentMediator.coordinatesY);
        add(argumentMediator.coordinatesZ);
      }
    };
  }

  private List<String> initPersonArguments() {
    return new ArrayList<String>() {
      {
        add(argumentMediator.personName);
        add(argumentMediator.personPassportId);
      }
    };
  }

  private List<String> initLocationArguments() {
    return new ArrayList<String>() {
      {
        add(argumentMediator.locationAddress);
        add(argumentMediator.locationLatitude);
        add(argumentMediator.locationLongitude);
      }
    };
  }

  protected final Map<String, String> formWorker(Iterator<String> script)
      throws WrongArgumentsException {
    Map<String, String> allArguments = new HashMap<>();

    if (readArgument(argumentMediator.worker, script).equals(argumentMediator.included)) {
      allArguments.put(argumentMediator.worker, argumentMediator.included);
      allArguments.putAll(readArguments(workerArguments, script));
      logger.info(() -> "Worker arguments were formed.");
    }

    if (readArgument(argumentMediator.coordinates, script).equals(argumentMediator.included)) {
      allArguments.put(argumentMediator.coordinates, argumentMediator.included);
      allArguments.putAll(readArguments(coordinatesArguments, script));
      logger.info(() -> "Coordinates arguments were formed.");
    }

    if (readArgument(argumentMediator.person, script).equals(argumentMediator.included)) {
      allArguments.put(argumentMediator.person, argumentMediator.included);
      allArguments.putAll(readArguments(personArguments, script));
      logger.info(() -> "Person arguments were formed.");
    }

    if (readArgument(argumentMediator.location, script).equals(argumentMediator.included)) {
      allArguments.put(argumentMediator.location, argumentMediator.included);
      allArguments.putAll(readArguments(locationArguments, script));
      logger.info(() -> "Location arguments were formed.");
    }

    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
