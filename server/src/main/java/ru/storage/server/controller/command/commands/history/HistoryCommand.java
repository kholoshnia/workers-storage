package ru.storage.server.controller.command.commands.history;

import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.command.Command;
import ru.storage.server.controller.services.history.History;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public abstract class HistoryCommand extends Command {
  protected final String SEPARATOR = "----------------------";
  protected final String HISTORY_IS_EMPTY_ANSWER;

  protected final History history;

  public HistoryCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      History history) {
    super(configuration, argumentMediator, arguments);
    this.history = history;

    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.HistoryCommand", locale);

    HISTORY_IS_EMPTY_ANSWER = resourceBundle.getString("answers.historyIsEmpty");
  }
}
