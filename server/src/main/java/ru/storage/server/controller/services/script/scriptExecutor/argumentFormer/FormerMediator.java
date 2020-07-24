package ru.storage.server.controller.services.script.scriptExecutor.argumentFormer;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public final class FormerMediator {
  private final Logger logger;
  private final Map<String, ArgumentFormer> argumentFormerMap;

  @Inject
  public FormerMediator(Map<String, ArgumentFormer> argumentFormerMap) {
    logger = LogManager.getLogger(FormerMediator.class);
    this.argumentFormerMap = argumentFormerMap;
  }

  public ArgumentFormer getArgumentFormer(String command) {
    ArgumentFormer argumentFormer = argumentFormerMap.get(command);

    logger.info("Got argument former {}, for command {}.", () -> argumentFormer, () -> command);
    return argumentFormer;
  }
}
