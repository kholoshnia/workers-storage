package ru.storage.server.controller.command.factory.factories;

import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;
import ru.storage.common.exitManager.ExitManager;
import ru.storage.server.controller.command.Command;
import ru.storage.server.controller.command.commands.special.ExitCommand;
import ru.storage.server.controller.command.commands.special.HelpCommand;
import ru.storage.server.controller.command.commands.special.SpecialCommand;
import ru.storage.server.controller.command.factory.CommandFactory;
import ru.storage.server.controller.command.factory.exceptions.CommandFactoryException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class SpecialCommandFactory extends CommandFactory {
  private final CommandMediator commandMediator;
  private final ExitManager exitManager;
  private final Map<String, Class<? extends SpecialCommand>> specialCommandMap;

  public SpecialCommandFactory(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      CommandMediator commandMediator,
      ExitManager exitManager) {
    super(configuration, argumentMediator);
    this.commandMediator = commandMediator;
    this.exitManager = exitManager;
    specialCommandMap =
        new HashMap<String, Class<? extends SpecialCommand>>() {
          {
            put(commandMediator.EXIT, ExitCommand.class);
            put(commandMediator.HELP, HelpCommand.class);
          }
        };
  }

  @Override
  public Command createCommand(String command, Map<String, String> arguments, Locale locale)
      throws CommandFactoryException {
    Class<? extends SpecialCommand> clazz = specialCommandMap.get(command);
    try {
      Constructor<? extends SpecialCommand> constructor =
          clazz.getConstructor(
              Configuration.class,
              CommandMediator.class,
              ArgumentMediator.class,
              Map.class,
              Locale.class,
              ExitManager.class);
      return constructor.newInstance(
          configuration, commandMediator, argumentMediator, arguments, locale, exitManager);
    } catch (NoSuchMethodException
        | InstantiationException
        | IllegalAccessException
        | InvocationTargetException e) {
      throw new CommandFactoryException(e);
    }
  }
}
