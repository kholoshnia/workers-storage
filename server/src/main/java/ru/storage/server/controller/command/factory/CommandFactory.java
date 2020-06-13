package ru.storage.server.controller.command.factory;

import org.apache.commons.configuration2.Configuration;
import ru.storage.server.controller.command.Command;
import ru.storage.server.controller.command.factory.exceptions.CommandFactoryException;

import java.util.Map;

public abstract class CommandFactory {
  protected final Configuration configuration;

  public CommandFactory(Configuration configuration) {
    this.configuration = configuration;
  }

  public abstract Command createCommand(String command, Map<String, String> arguments)
      throws CommandFactoryException;
}
