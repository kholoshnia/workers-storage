package ru.storage.client.controller.argumentFormer;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public final class FormerMediator {
  private static final Logger logger = LogManager.getLogger(FormerMediator.class);

  private final Map<String, ArgumentFormer> argumentFormerMap;

  @Inject
  public FormerMediator(Map<String, ArgumentFormer> argumentFormerMap) {
    this.argumentFormerMap = argumentFormerMap;
  }

  public ArgumentFormer getArgumentFormer(String command) {
    ArgumentFormer argumentFormer = argumentFormerMap.get(command);

    logger.info("Got argument former {}, for command {}.", () -> argumentFormer, () -> command);
    return argumentFormer;
  }
}
