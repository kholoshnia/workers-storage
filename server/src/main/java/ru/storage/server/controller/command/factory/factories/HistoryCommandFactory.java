package ru.storage.server.controller.command.factory.factories;

import com.google.gson.Gson;
import org.apache.commons.configuration2.Configuration;
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
import java.util.Map;

public final class HistoryCommandFactory extends CommandFactory {
  private final Gson gson;
  private final History history;
  private final Map<String, Class<? extends HistoryCommand>> commands;

  public HistoryCommandFactory(
      Configuration configuration, CommandMediator commandMediator, Gson gson, History history) {
    super(configuration);
    this.gson = gson;
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
  public Command createCommand(String command, Map<String, String> arguments)
      throws CommandFactoryException {
    Class<? extends HistoryCommand> clazz = commands.get(command);
    try {
      Constructor<? extends HistoryCommand> constructor =
          clazz.getConstructor(Configuration.class, Map.class, Gson.class, History.class);
      return constructor.newInstance(configuration, arguments, gson, history);
    } catch (NoSuchMethodException
        | InstantiationException
        | IllegalAccessException
        | InvocationTargetException e) {
      throw new CommandFactoryException(e);
    }
  }
}
