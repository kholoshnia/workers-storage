package ru.storage.server.controller.controllers.command.commands.history;

import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.controllers.command.Command;
import ru.storage.server.model.domain.history.History;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public abstract class HistoryCommand extends Command {
  protected final String HISTORY_IS_EMPTY_ANSWER;

  protected final Locale locale;
  protected final History history;

  public HistoryCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      History history) {
    super(configuration, argumentMediator, arguments);
    this.locale = locale;
    this.history = history;

    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.HistoryCommand", locale);

    HISTORY_IS_EMPTY_ANSWER = resourceBundle.getString("answers.historyIsEmpty");
  }
}
