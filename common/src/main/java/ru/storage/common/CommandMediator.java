package ru.storage.common;

import com.google.inject.Inject;
import org.apache.commons.configuration2.Configuration;

import java.util.ArrayList;
import java.util.List;

public final class CommandMediator {
  public final String LOGIN;
  public final String LOGOUT;
  public final String REGISTER;
  public final String SHOW_HISTORY;
  public final String CLEAR_HISTORY;
  public final String ADD;
  public final String REMOVE;
  public final String UPDATE;
  public final String EXIT;
  public final String HELP;
  public final String INFO;
  public final String SHOW;

  private final List<String> commands;

  @Inject
  public CommandMediator(Configuration configuration) {
    LOGIN = configuration.getString("commands.login");
    LOGOUT = configuration.getString("commands.logout");
    REGISTER = configuration.getString("commands.register");
    SHOW_HISTORY = configuration.getString("commands.showHistory");
    CLEAR_HISTORY = configuration.getString("commands.clearHistory");
    ADD = configuration.getString("commands.add");
    REMOVE = configuration.getString("commands.remove");
    UPDATE = configuration.getString("commands.update");
    EXIT = configuration.getString("commands.exit");
    HELP = configuration.getString("commands.help");
    INFO = configuration.getString("commands.info");
    SHOW = configuration.getString("commands.show");

    commands = initCommandList();
  }

  private List<String> initCommandList() {
    return new ArrayList<String>() {
      {
        add(LOGIN);
        add(LOGOUT);
        add(REGISTER);
        add(SHOW_HISTORY);
        add(CLEAR_HISTORY);
        add(ADD);
        add(REMOVE);
        add(UPDATE);
        add(EXIT);
        add(HELP);
        add(INFO);
        add(SHOW);
      }
    };
  }

  public List<String> getCommands() {
    return new ArrayList<>(commands);
  }

  public boolean contains(String command) {
    return commands.contains(command);
  }
}
