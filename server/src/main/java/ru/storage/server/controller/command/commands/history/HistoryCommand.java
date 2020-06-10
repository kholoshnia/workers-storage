package ru.storage.server.controller.command.commands.history;

import org.apache.commons.configuration2.Configuration;
import ru.storage.common.transfer.response.Response;
import ru.storage.server.controller.command.Command;
import ru.storage.server.controller.services.history.History;

import java.util.Locale;
import java.util.Map;

public abstract class HistoryCommand extends Command {
  protected final History history;

  public HistoryCommand(
      Configuration configuration, Map<String, String> arguments, Locale locale, History history) {
    super(configuration, arguments, locale);
    this.history = history;
  }
}
