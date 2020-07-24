package ru.storage.server.controller.controllers.command.factory.factories;

import com.google.inject.Inject;
import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;
import ru.storage.server.controller.controllers.command.Command;
import ru.storage.server.controller.controllers.command.commands.view.InfoCommand;
import ru.storage.server.controller.controllers.command.commands.view.ShowCommand;
import ru.storage.server.controller.controllers.command.commands.view.ViewCommand;
import ru.storage.server.controller.controllers.command.factory.CommandFactory;
import ru.storage.server.controller.controllers.command.factory.exceptions.CommandFactoryException;
import ru.storage.server.model.domain.repository.repositories.workerRepository.WorkerRepository;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class ViewCommandFactory extends CommandFactory {
  private final WorkerRepository workerRepository;
  private final Map<String, Class<? extends ViewCommand>> viewCommandMap;

  @Inject
  public ViewCommandFactory(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      CommandMediator commandMediator,
      WorkerRepository workerRepository) {
    super(configuration, argumentMediator);
    this.workerRepository = workerRepository;
    viewCommandMap = initViewCommandMap(commandMediator);
  }

  private Map<String, Class<? extends ViewCommand>> initViewCommandMap(
      CommandMediator commandMediator) {
    return new HashMap<String, Class<? extends ViewCommand>>() {
      {
        put(commandMediator.INFO, InfoCommand.class);
        put(commandMediator.SHOW, ShowCommand.class);
      }
    };
  }

  @Override
  public Command createCommand(
      String command, Map<String, String> arguments, Locale locale, String login)
      throws CommandFactoryException {
    Class<? extends ViewCommand> clazz = viewCommandMap.get(command);
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
