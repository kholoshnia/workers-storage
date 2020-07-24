package ru.storage.server.controller.controllers.command.factory.factories;

import com.google.inject.Inject;
import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;
import ru.storage.server.controller.controllers.command.Command;
import ru.storage.server.controller.controllers.command.commands.entry.EntryCommand;
import ru.storage.server.controller.controllers.command.commands.entry.LoginCommand;
import ru.storage.server.controller.controllers.command.commands.entry.LogoutCommand;
import ru.storage.server.controller.controllers.command.commands.entry.RegisterCommand;
import ru.storage.server.controller.controllers.command.factory.CommandFactory;
import ru.storage.server.controller.controllers.command.factory.exceptions.CommandFactoryException;
import ru.storage.server.controller.services.hash.HashGenerator;
import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.repository.Repository;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.Key;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class EntryCommandFactory extends CommandFactory {
  private final HashGenerator hashGenerator;
  private final Repository<User> userRepository;
  private final Key key;
  private final Map<String, Class<? extends EntryCommand>> entryCommandMap;

  @Inject
  public EntryCommandFactory(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      CommandMediator commandMediator,
      HashGenerator hashGenerator,
      Repository<User> userRepository,
      Key key) {
    super(configuration, argumentMediator);
    this.hashGenerator = hashGenerator;
    this.userRepository = userRepository;
    this.key = key;
    entryCommandMap = initEntryCommandMap(commandMediator);
  }

  private Map<String, Class<? extends EntryCommand>> initEntryCommandMap(
      CommandMediator commandMediator) {
    return new HashMap<String, Class<? extends EntryCommand>>() {
      {
        put(commandMediator.LOGIN, LoginCommand.class);
        put(commandMediator.REGISTER, RegisterCommand.class);
        put(commandMediator.LOGOUT, LogoutCommand.class);
      }
    };
  }

  @Override
  public Command createCommand(
      String command, Map<String, String> arguments, Locale locale, String login)
      throws CommandFactoryException {
    Class<? extends EntryCommand> clazz = entryCommandMap.get(command);
    try {
      Constructor<? extends EntryCommand> constructor =
          clazz.getConstructor(
              Configuration.class,
              ArgumentMediator.class,
              Map.class,
              Locale.class,
              HashGenerator.class,
              Repository.class,
              Key.class);
      return constructor.newInstance(
          configuration, argumentMediator, arguments, locale, hashGenerator, userRepository, key);
    } catch (NoSuchMethodException
        | InstantiationException
        | IllegalAccessException
        | InvocationTargetException e) {
      throw new CommandFactoryException(e);
    }
  }
}
