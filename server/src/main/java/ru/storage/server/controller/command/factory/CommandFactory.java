package ru.storage.server.controller.command.factory;

import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.command.Command;
import ru.storage.server.controller.command.factory.exceptions.CommandFactoryException;

import java.util.Locale;
import java.util.Map;

public abstract class CommandFactory {
  protected final Configuration configuration;
  protected final ArgumentMediator argumentMediator;

  public CommandFactory(Configuration configuration, ArgumentMediator argumentMediator) {
    this.configuration = configuration;
    this.argumentMediator = argumentMediator;
  }

  public abstract Command createCommand(
      String command, Map<String, String> arguments, Locale locale) throws CommandFactoryException;
}
