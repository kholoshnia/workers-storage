package ru.storage.server.controller.command.factory.factories;

import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;
import ru.storage.server.controller.command.Command;
import ru.storage.server.controller.command.commands.history.ClearHistoryCommand;
import ru.storage.server.controller.command.commands.history.HistoryCommand;
import ru.storage.server.controller.command.commands.history.ShowHistoryCommand;
import ru.storage.server.controller.command.factory.CommandFactory;
import ru.storage.server.controller.command.factory.exceptions.CommandFactoryException;
import ru.storage.server.controller.services.history.History;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class HistoryCommandFactory extends CommandFactory {
  private final History history;
  private final Map<String, Class<? extends HistoryCommand>> commands;

  public HistoryCommandFactory(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      CommandMediator commandMediator,
      History history) {
    super(configuration, argumentMediator);
    this.history = history;
    this.commands =
        new HashMap<String, Class<? extends HistoryCommand>>() {
          {
            put(commandMediator.SHOW_HISTORY, ShowHistoryCommand.class);
            put(commandMediator.CLEAR_HISTORY, ClearHistoryCommand.class);
          }
        };
  }

  @Override
  public Command createCommand(String command, Map<String, String> arguments, Locale locale)
      throws CommandFactoryException {
    Class<? extends HistoryCommand> clazz = commands.get(command);
    try {
      Constructor<? extends HistoryCommand> constructor =
          clazz.getConstructor(
              Configuration.class, ArgumentMediator.class, Map.class, Locale.class, History.class);
      return constructor.newInstance(configuration, argumentMediator, arguments, locale, history);
    } catch (NoSuchMethodException
        | InstantiationException
        | IllegalAccessException
        | InvocationTargetException e) {
      throw new CommandFactoryException(e);
    }
  }
}
