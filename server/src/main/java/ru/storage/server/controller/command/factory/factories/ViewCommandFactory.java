package ru.storage.server.controller.command.factory.factories;

import org.apache.commons.configuration2.Configuration;
import ru.storage.common.CommandMediator;
import ru.storage.server.controller.command.Command;
import ru.storage.server.controller.command.commands.view.InfoCommand;
import ru.storage.server.controller.command.commands.view.ShowCommand;
import ru.storage.server.controller.command.commands.view.ViewCommand;
import ru.storage.server.controller.command.factory.CommandFactory;
import ru.storage.server.controller.command.factory.exceptions.CommandFactoryException;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.repository.Repository;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class ViewCommandFactory extends CommandFactory {
  private final Repository<Worker> workerRepository;
  private final Map<String, Class<? extends ViewCommand>> commands;

  public ViewCommandFactory(
      Configuration configuration,
      CommandMediator commandMediator,
      Repository<Worker> workerRepository) {
    super(configuration);
    this.workerRepository = workerRepository;
    this.commands =
        new HashMap<String, Class<? extends ViewCommand>>() {
          {
            put(commandMediator.INFO, InfoCommand.class);
            put(commandMediator.SHOW, ShowCommand.class);
          }
        };
  }

  @Override
  public Command createCommand(String command, Map<String, String> arguments, Locale locale)
      throws CommandFactoryException {
    Class<? extends ViewCommand> clazz = commands.get(command);
    try {
      Constructor<? extends ViewCommand> constructor =
          clazz.getConstructor(Configuration.class, Map.class, Locale.class, Repository.class);
      return constructor.newInstance(configuration, arguments, locale, workerRepository);
    } catch (NoSuchMethodException
        | InstantiationException
        | IllegalAccessException
        | InvocationTargetException e) {
      throw new CommandFactoryException(e);
    }
  }
}
