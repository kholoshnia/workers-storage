package ru.storage.server.controller.controllers.command.factory;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public final class CommandFactoryMediator {
  private static final Logger logger = LogManager.getLogger(CommandFactoryMediator.class);

  private final Map<String, CommandFactory> commandFactoryMap;

  @Inject
  public CommandFactoryMediator(Map<String, CommandFactory> commandFactoryMap) {
    this.commandFactoryMap = commandFactoryMap;
  }

  public CommandFactory getCommandFactory(String command) {
    CommandFactory commandFactory = commandFactoryMap.get(command);

    logger.info("Got command factory: {}, for command: {}.", () -> commandFactory, () -> command);
    return commandFactory;
  }
}
