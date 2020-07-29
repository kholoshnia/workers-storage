package ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.argumentFormers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.services.script.Script;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.exceptions.WrongArgumentsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class WorkerFormer extends Former {
  private static final Logger logger = LogManager.getLogger(WorkerFormer.class);

  private final List<String> arguments;

  public WorkerFormer(ArgumentMediator argumentMediator) {
    super(argumentMediator);
    arguments = initArguments();
  }

  private List<String> initArguments() {
    return new ArrayList<String>() {
      {
        add(argumentMediator.worker);
        add(argumentMediator.workerSalary);
        add(argumentMediator.workerStatus);
        add(argumentMediator.workerStartDate);
        add(argumentMediator.workerEndDate);

        add(argumentMediator.coordinates);
        add(argumentMediator.coordinatesX);
        add(argumentMediator.coordinatesY);
        add(argumentMediator.coordinatesZ);

        add(argumentMediator.person);
        add(argumentMediator.personName);
        add(argumentMediator.personPassportId);

        add(argumentMediator.location);
        add(argumentMediator.locationAddress);
        add(argumentMediator.locationLatitude);
        add(argumentMediator.locationLongitude);
      }
    };
  }

  protected final Map<String, String> formWorker(Script script) throws WrongArgumentsException {
    Map<String, String> allArguments = readArguments(arguments, script);

    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
