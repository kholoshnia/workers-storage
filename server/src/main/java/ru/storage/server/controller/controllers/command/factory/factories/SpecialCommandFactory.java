package ru.storage.server.controller.controllers.command.factory.factories;

import com.google.inject.Inject;
import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;
import ru.storage.common.exitManager.ExitManager;
import ru.storage.server.controller.controllers.command.Command;
import ru.storage.server.controller.controllers.command.commands.special.ExecuteScriptCommand;
import ru.storage.server.controller.controllers.command.commands.special.ExitCommand;
import ru.storage.server.controller.controllers.command.commands.special.HelpCommand;
import ru.storage.server.controller.controllers.command.commands.special.SpecialCommand;
import ru.storage.server.controller.controllers.command.factory.CommandFactory;
import ru.storage.server.controller.controllers.command.factory.exceptions.CommandFactoryException;
import ru.storage.server.controller.services.script.scriptExecutor.ScriptExecutor;
import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.repository.Repository;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class SpecialCommandFactory extends CommandFactory {
  private final CommandMediator commandMediator;
  private final Repository<User> userRepository;
  private final ScriptExecutor scriptExecutor;
  private final ExitManager exitManager;
  private final Map<String, Class<? extends SpecialCommand>> specialCommandMap;

  @Inject
  public SpecialCommandFactory(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      CommandMediator commandMediator,
      Repository<User> userRepository,
      ScriptExecutor scriptExecutor,
      ExitManager exitManager) {
    super(configuration, argumentMediator);
    this.commandMediator = commandMediator;
    this.userRepository = userRepository;
    this.scriptExecutor = scriptExecutor;
    this.exitManager = exitManager;
    specialCommandMap = initSpecialCommandMap(commandMediator);
  }

  private Map<String, Class<? extends SpecialCommand>> initSpecialCommandMap(
      CommandMediator commandMediator) {
    return new HashMap<String, Class<? extends SpecialCommand>>() {
      {
        put(commandMediator.EXIT, ExitCommand.class);
        put(commandMediator.HELP, HelpCommand.class);
        put(commandMediator.EXECUTE_SCRIPT, ExecuteScriptCommand.class);
      }
    };
  }

  @Override
  public Command createCommand(
      String command, Map<String, String> arguments, Locale locale, String login)
      throws CommandFactoryException {
    Class<? extends SpecialCommand> clazz = specialCommandMap.get(command);
    try {
      Constructor<? extends SpecialCommand> constructor =
          clazz.getConstructor(
              Configuration.class,
              CommandMediator.class,
              ArgumentMediator.class,
              Map.class,
              User.class,
              Locale.class,
              ExitManager.class,
              ScriptExecutor.class);
      return constructor.newInstance(
          configuration,
          commandMediator,
          argumentMediator,
          arguments,
          getUser(userRepository, login),
          locale,
          exitManager,
          scriptExecutor);
    } catch (NoSuchMethodException
        | InstantiationException
        | IllegalAccessException
        | InvocationTargetException e) {
      throw new CommandFactoryException(e);
    }
  }
}
