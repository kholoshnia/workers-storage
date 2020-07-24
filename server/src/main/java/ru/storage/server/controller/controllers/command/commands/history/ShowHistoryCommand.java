package ru.storage.server.controller.controllers.command.commands.history;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;
import ru.storage.server.model.domain.history.History;
import ru.storage.server.model.domain.history.Record;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public final class ShowHistoryCommand extends HistoryCommand {
  public static final String BEGINNING = "---------------< HISTORY >---------------";
  public static final String SEPARATOR = "-----------------------------------------";

  private final String SHOW_HISTORY_PREFIX;

  private final Logger logger;

  public ShowHistoryCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      History history) {
    super(configuration, argumentMediator, arguments, locale, history);
    logger = LogManager.getLogger(ShowHistoryCommand.class);

    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.ShowHistoryCommand");

    SHOW_HISTORY_PREFIX = resourceBundle.getString("prefixes.showHistory");
  }

  @Override
  public Response executeCommand() {
    if (history.getSize() == 0L) {
      logger.info(() -> "History is empty.");
      return new Response(Status.NO_CONTENT, HISTORY_IS_EMPTY_ANSWER);
    }

    List<Record> records = history.getRecords(10);
    StringBuilder result =
        new StringBuilder(BEGINNING)
            .append(System.lineSeparator())
            .append(SHOW_HISTORY_PREFIX)
            .append(System.lineSeparator())
            .append(SEPARATOR);

    for (Record record : records) {
      result
          .append(System.lineSeparator())
          .append(record.getCommand())
          .append(System.lineSeparator());

      record
          .getArguments()
          .forEach(
              (key, value) ->
                  result
                      .append(String.format("%s: %s", key, value))
                      .append(System.lineSeparator()));

      result.append(SEPARATOR);
    }

    logger.info(() -> "History was formed.");
    return new Response(Status.OK, result.toString());
  }
}
