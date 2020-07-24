package ru.storage.server.controller.controllers.command.commands.history;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;
import ru.storage.server.model.domain.history.History;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public final class ClearHistoryCommand extends HistoryCommand {
  protected final String CLEARED_SUCCESSFULLY_ANSWER;

  private final Logger logger;

  public ClearHistoryCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      History history) {
    super(configuration, argumentMediator, arguments, locale, history);
    logger = LogManager.getLogger(ClearHistoryCommand.class);

    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("localized.ClearHistoryCommand", locale);

    CLEARED_SUCCESSFULLY_ANSWER = resourceBundle.getString("answers.clearedSuccessfully");
  }

  @Override
  public Response executeCommand() {
    if (history.getSize() == 0L) {
      logger.info(() -> "History is empty.");
      return new Response(Status.NO_CONTENT, HISTORY_IS_EMPTY_ANSWER);
    }

    history.clear();

    logger.info(() -> "History was cleared.");
    return new Response(Status.OK, CLEARED_SUCCESSFULLY_ANSWER);
  }
}
