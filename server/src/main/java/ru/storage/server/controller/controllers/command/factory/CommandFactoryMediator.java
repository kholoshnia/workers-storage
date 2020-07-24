package ru.storage.server.controller.controllers.command.factory;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public final class CommandFactoryMediator {
  private final Logger logger;
  private final Map<String, CommandFactory> commandFactoryMap;

  @Inject
  public CommandFactoryMediator(Map<String, CommandFactory> commandFactoryMap) {
    logger = LogManager.getLogger(CommandFactoryMediator.class);
    this.commandFactoryMap = commandFactoryMap;
  }

  public CommandFactory getCommandFactory(String command) {
    CommandFactory commandFactory = commandFactoryMap.get(command);

    logger.info("Got command factory: {}, for command: {}.", () -> commandFactory, () -> command);
    return commandFactory;
  }
}
