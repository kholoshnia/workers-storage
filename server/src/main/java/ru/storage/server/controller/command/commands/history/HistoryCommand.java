package ru.storage.server.controller.command.commands.history;

import com.google.gson.Gson;
import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.command.Command;
import ru.storage.server.controller.services.history.History;

import java.util.Map;
import java.util.ResourceBundle;

public abstract class HistoryCommand extends Command {
  protected final String HISTORY_IS_EMPTY_ANSWER;

  protected final Gson gson;
  protected final History history;

  public HistoryCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Gson gson,
      History history) {
    super(configuration, argumentMediator, arguments);
    this.gson = gson;
    this.history = history;

    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.HistoryCommand");

    HISTORY_IS_EMPTY_ANSWER = resourceBundle.getString("answers.historyIsEmpty");
  }
}
