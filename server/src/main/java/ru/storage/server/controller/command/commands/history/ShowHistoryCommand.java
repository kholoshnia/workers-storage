package ru.storage.server.controller.command.commands.history;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;
import ru.storage.server.controller.services.history.History;

import java.util.Locale;
import java.util.Map;

public final class ShowHistoryCommand extends HistoryCommand {
  private final Logger logger;

  public ShowHistoryCommand(
      Configuration configuration, Map<String, String> arguments, Locale locale, History history) {
    super(configuration, arguments, locale, history);
    this.logger = LogManager.getLogger(ShowHistoryCommand.class);
  }

  @Override
  public Response executeCommand() {
    return new Response(Status.OK, "History: ...");
  }
}
