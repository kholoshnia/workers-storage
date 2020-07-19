package ru.storage.server.controller.command.factory.factories;

import com.google.inject.Inject;
import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;
import ru.storage.server.controller.command.Command;
import ru.storage.server.controller.command.commands.modification.AddCommand;
import ru.storage.server.controller.command.commands.modification.ModificationCommand;
import ru.storage.server.controller.command.commands.modification.RemoveCommand;
import ru.storage.server.controller.command.commands.modification.UpdateCommand;
import ru.storage.server.controller.command.factory.CommandFactory;
import ru.storage.server.controller.command.factory.exceptions.CommandFactoryException;
import ru.storage.server.controller.services.parser.Parser;
import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.repository.Query;
import ru.storage.server.model.domain.repository.Repository;
import ru.storage.server.model.domain.repository.exceptions.RepositoryException;
import ru.storage.server.model.domain.repository.repositories.userRepository.queries.GetEqualsLoginUsers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
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

  /**
   * Returns user found by login.
   *
   * @param login user login
   * @return user
   * @throws CommandFactoryException - if user was not found
   */
  private User getUserId(String login) throws CommandFactoryException {
    if (login.isEmpty()) {
      throw new CommandFactoryException();
    }

    Query<User> query = new GetEqualsLoginUsers(login);
    List<User> equalLoginUsers;

    try {
      equalLoginUsers = userRepository.get(query);
    } catch (RepositoryException e) {
      throw new CommandFactoryException();
    }

    for (User user : equalLoginUsers) {
      return user;
    }

    throw new CommandFactoryException();
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
          getUserId(login));
    } catch (NoSuchMethodException
        | InstantiationException
        | IllegalAccessException
        | InvocationTargetException e) {
      throw new CommandFactoryException(e);
    }
  }
}
