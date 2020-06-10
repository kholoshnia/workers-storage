package ru.storage.server.controller.command.factory;

import org.apache.commons.configuration2.Configuration;
import ru.storage.common.api.CommandMediator;
import ru.storage.server.controller.command.factory.factories.EntryCommandFactory;
import ru.storage.server.controller.command.factory.factories.HistoryCommandFactory;
import ru.storage.server.controller.command.factory.factories.ModificationCommandFactory;
import ru.storage.server.controller.command.factory.factories.ViewCommandFactory;
import ru.storage.server.controller.services.history.History;
import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CommandFactoryMediator {
  private final List<CommandFactory> commandFactories;
  private final Map<String, Class<? extends CommandFactory>> commandFactoriesMap;

  public CommandFactoryMediator(
      Configuration configuration,
      CommandMediator commandMediator,
      History history,
      Repository<User> userRepository,
      Repository<Worker> workerRepository) {
    this.commandFactories = new ArrayList<>();
    commandFactories.add(new EntryCommandFactory(configuration, commandMediator, userRepository));
    commandFactories.add(new HistoryCommandFactory(configuration, commandMediator, history));
    commandFactories.add(
        new ModificationCommandFactory(configuration, commandMediator, workerRepository));
    commandFactories.add(new ViewCommandFactory(configuration, commandMediator, workerRepository));
    this.commandFactoriesMap = initCommandFactoriesMap(commandMediator);
  }

  private Map<String, Class<? extends CommandFactory>> initCommandFactoriesMap(
      CommandMediator commandMediator) {
    return new HashMap<String, Class<? extends CommandFactory>>() {
      {
        put(commandMediator.LOGIN, EntryCommandFactory.class);
        put(commandMediator.LOGOUT, EntryCommandFactory.class);
        put(commandMediator.REGISTER, EntryCommandFactory.class);
        put(commandMediator.SHOW_HISTORY, HistoryCommandFactory.class);
        put(commandMediator.CLEAR_HISTORY, HistoryCommandFactory.class);
        put(commandMediator.ADD, ModificationCommandFactory.class);
        put(commandMediator.REMOVE, ModificationCommandFactory.class);
        put(commandMediator.UPDATE, ModificationCommandFactory.class);
        put(commandMediator.INFO, ViewCommandFactory.class);
        put(commandMediator.SHOW, ViewCommandFactory.class);
      }
    };
  }

  public CommandFactory getCommandFactory(String command) {
    Class<? extends CommandFactory> targetFactory = commandFactoriesMap.get(command);

    for (CommandFactory commandFactory : commandFactories) {
      if (commandFactory.getClass().equals(targetFactory)) {
        return commandFactory;
      }
    }

    return null;
  }
}
