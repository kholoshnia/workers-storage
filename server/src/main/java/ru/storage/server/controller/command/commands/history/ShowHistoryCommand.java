package ru.storage.server.controller.command.commands.history;

import com.google.gson.Gson;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;
import ru.storage.server.controller.services.history.History;
import ru.storage.server.controller.services.history.Record;

import java.util.List;
import java.util.Map;

public final class ShowHistoryCommand extends HistoryCommand {
  private final Logger logger;

  public ShowHistoryCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Gson gson,
      History history) {
    super(configuration, argumentMediator, arguments, gson, history);
    this.logger = LogManager.getLogger(ShowHistoryCommand.class);
  }

  @Override
  public Response executeCommand() {
    if (history.getSize() == 0L) {
      logger.info("History is empty.");
      return new Response(Status.NO_CONTENT, HISTORY_IS_EMPTY_ANSWER);
    }

    List<Record> records = history.getRecords(10);
    String result = gson.toJson(records);

    logger.info("History was converted SUCCESSFULLY.");
    return new Response(Status.OK, result);
  }
}
