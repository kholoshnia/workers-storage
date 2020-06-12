package ru.storage.server.controller.command.factory;

import org.apache.commons.configuration2.Configuration;
import ru.storage.common.CommandMediator;
import ru.storage.server.controller.command.factory.factories.EntryCommandFactory;
import ru.storage.server.controller.command.factory.factories.HistoryCommandFactory;
import ru.storage.server.controller.command.factory.factories.ModificationCommandFactory;
import ru.storage.server.controller.command.factory.factories.ViewCommandFactory;
import ru.storage.server.controller.services.history.History;
import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.repository.Repository;

import java.util.HashMap;
import java.util.Map;

public final class CommandFactoryMediator {
  private final Map<String, CommandFactory> commandFactories;

  public CommandFactoryMediator(
      Configuration configuration,
      CommandMediator commandMediator,
      History history,
      Repository<User> userRepository,
      Repository<Worker> workerRepository) {
    EntryCommandFactory entryCommandFactory =
        new EntryCommandFactory(configuration, commandMediator, userRepository);
    HistoryCommandFactory historyCommandFactory =
        new HistoryCommandFactory(configuration, commandMediator, history);
    ModificationCommandFactory modificationCommandFactory =
        new ModificationCommandFactory(configuration, commandMediator, workerRepository);
    ViewCommandFactory viewCommandFactory =
        new ViewCommandFactory(configuration, commandMediator, workerRepository);

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
  }

  public CommandFactory getCommandFactory(String command) {
    return commandFactories.get(command);
  }
}
