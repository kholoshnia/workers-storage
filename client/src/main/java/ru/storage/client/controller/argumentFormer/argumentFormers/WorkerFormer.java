package ru.storage.client.controller.argumentFormer.argumentFormers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.argumentFormer.ArgumentFormer;
import ru.storage.client.controller.argumentFormer.ArgumentValidator;
import ru.storage.client.controller.validator.exceptions.ValidationException;

import java.util.Map;

public abstract class WorkerFormer implements ArgumentFormer {
  private final Logger logger;
  private final Map<String, ArgumentValidator> argumentValidatorMap;

  public WorkerFormer(Map<String, ArgumentValidator> argumentValidatorMap) {
    logger = LogManager.getLogger(WorkerFormer.class);
    this.argumentValidatorMap = argumentValidatorMap;
  }

  protected final void checkArgument(String argument, String input) throws ValidationException {
    ArgumentValidator argumentValidator = argumentValidatorMap.get(argument);

    logger.info(
        "Got argument validator: {}, for argument: {}.", () -> argumentValidator, () -> argument);
    argumentValidator.check(input);
  }
}
