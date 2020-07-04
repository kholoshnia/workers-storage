package ru.storage.server.controller.command.factory.factories;

import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;
import ru.storage.server.controller.command.Command;
import ru.storage.server.controller.command.commands.view.InfoCommand;
import ru.storage.server.controller.command.commands.view.ShowCommand;
import ru.storage.server.controller.command.commands.view.ViewCommand;
import ru.storage.server.controller.command.factory.CommandFactory;
import ru.storage.server.controller.command.factory.exceptions.CommandFactoryException;
import ru.storage.server.model.domain.repository.repositories.workerRepository.WorkerRepository;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class ViewCommandFactory extends CommandFactory {
  private final WorkerRepository workerRepository;
  private final Map<String, Class<? extends ViewCommand>> viewCommandsMap;

  public ViewCommandFactory(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      CommandMediator commandMediator,
      WorkerRepository workerRepository) {
    super(configuration, argumentMediator);
    this.workerRepository = workerRepository;
    this.viewCommandsMap =
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
    Class<? extends ViewCommand> clazz = viewCommandsMap.get(command);
    try {
      Constructor<? extends ViewCommand> constructor =
          clazz.getConstructor(
              Configuration.class,
              ArgumentMediator.class,
              Map.class,
              Locale.class,
              WorkerRepository.class);
      return constructor.newInstance(
          configuration, argumentMediator, arguments, locale, workerRepository);
    } catch (NoSuchMethodException
        | InstantiationException
        | IllegalAccessException
        | InvocationTargetException e) {
      throw new CommandFactoryException(e);
    }
  }
}
