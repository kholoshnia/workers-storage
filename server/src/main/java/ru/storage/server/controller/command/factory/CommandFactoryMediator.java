package ru.storage.server.controller.command.factory;

import com.google.inject.Inject;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;
import ru.storage.server.controller.command.factory.factories.ModificationCommandFactory;
import ru.storage.server.controller.command.factory.factories.EntryCommandFactory;
import ru.storage.server.controller.command.factory.factories.HistoryCommandFactory;
import ru.storage.server.controller.command.factory.factories.ViewCommandFactory;
import ru.storage.server.controller.services.history.History;
import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.repository.Repository;

import java.util.HashMap;
import java.util.Map;

public final class CommandFactoryMediator {
  private final Logger logger;
  private final Map<String, CommandFactory> commandFactories;

  @Inject
  public CommandFactoryMediator(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      CommandMediator commandMediator,
      History history,
      Repository<User> userRepository,
      Repository<Worker> workerRepository) {
    this.logger = LogManager.getLogger(CommandFactoryMediator.class);

    CommandFactory entryCommandFactory =
        new EntryCommandFactory(configuration, argumentMediator, commandMediator, userRepository);
    CommandFactory historyCommandFactory =
        new HistoryCommandFactory(configuration, argumentMediator, commandMediator, history);
    CommandFactory modificationCommandFactory =
        new ModificationCommandFactory(
            configuration, argumentMediator, commandMediator, workerRepository);
    CommandFactory viewCommandFactory =
        new ViewCommandFactory(configuration, argumentMediator, commandMediator, workerRepository);

    this.commandFactories =
        new HashMap<String, CommandFactory>() {
          {
            put(commandMediator.LOGIN, entryCommandFactory);
            put(commandMediator.LOGOUT, entryCommandFactory);
            put(commandMediator.REGISTER, entryCommandFactory);
            put(commandMediator.SHOW_HISTORY, historyCommandFactory);
            put(commandMediator.CLEAR_HISTORY, historyCommandFactory);
            put(commandMediator.ADD, modificationCommandFactory);
            put(commandMediator.REMOVE, modificationCommandFactory);
            put(commandMediator.UPDATE, modificationCommandFactory);
            put(commandMediator.INFO, viewCommandFactory);
            put(commandMediator.SHOW, viewCommandFactory);
          }
        };

    logger.debug(() -> "Command factory map has been created.");
  }

  public CommandFactory getCommandFactory(String command) {
    CommandFactory commandFactory = commandFactories.get(command);

    logger.info("Got command factory: {}, for command: {}.", () -> commandFactory, () -> command);
    return commandFactory;
  }
}
