package ru.storage.server.controller.controllers.command.factory.factories;

import com.google.inject.Inject;
import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;
import ru.storage.server.controller.controllers.command.Command;
import ru.storage.server.controller.controllers.command.commands.modification.AddCommand;
import ru.storage.server.controller.controllers.command.commands.modification.ModificationCommand;
import ru.storage.server.controller.controllers.command.commands.modification.RemoveCommand;
import ru.storage.server.controller.controllers.command.commands.modification.UpdateCommand;
import ru.storage.server.controller.controllers.command.factory.CommandFactory;
import ru.storage.server.controller.controllers.command.factory.exceptions.CommandFactoryException;
import ru.storage.server.controller.services.parser.Parser;
import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.repository.Repository;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class ModificationCommandFactory extends CommandFactory {
  private final Repository<User> userRepository;
  private final Repository<Worker> workerRepository;
  private final Parser parser;
  private final Map<String, Class<? extends ModificationCommand>> modificationCommandMap;

  @Inject
  public ModificationCommandFactory(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      CommandMediator commandMediator,
      Repository<User> userRepository,
      Repository<Worker> workerRepository,
      Parser parser) {
    super(configuration, argumentMediator);
    this.userRepository = userRepository;
    this.workerRepository = workerRepository;
    this.parser = parser;
    modificationCommandMap = initModificationCommandMap(commandMediator);
  }

  private Map<String, Class<? extends ModificationCommand>> initModificationCommandMap(
      CommandMediator commandMediator) {
    return new HashMap<String, Class<? extends ModificationCommand>>() {
      {
        put(commandMediator.ADD, AddCommand.class);
        put(commandMediator.UPDATE, UpdateCommand.class);
        put(commandMediator.REMOVE, RemoveCommand.class);
      }
    };
  }

  @Override
  public Command createCommand(
      String command, Map<String, String> arguments, Locale locale, String login)
      throws CommandFactoryException {
    Class<? extends ModificationCommand> clazz = modificationCommandMap.get(command);
    try {
      Constructor<? extends ModificationCommand> constructor =
          clazz.getConstructor(
              Configuration.class,
              ArgumentMediator.class,
              Map.class,
              Locale.class,
              Repository.class,
              Parser.class,
              User.class);
      return constructor.newInstance(
          configuration,
          argumentMediator,
          arguments,
          locale,
          workerRepository,
          parser,
          getUser(userRepository, login));
    } catch (NoSuchMethodException
        | InstantiationException
        | IllegalAccessException
        | InvocationTargetException e) {
      throw new CommandFactoryException(e);
    }
  }
}
