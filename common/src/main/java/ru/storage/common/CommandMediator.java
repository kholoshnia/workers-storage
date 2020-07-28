package ru.storage.common;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/** Command mediator class contains all command names. */
public final class CommandMediator {
  public final String LOGIN;
  public final String LOGOUT;
  public final String REGISTER;

  public final String SHOW_HISTORY;
  public final String CLEAR_HISTORY;

  public final String ADD;
  public final String REMOVE;
  public final String UPDATE;

  public final String INFO;
  public final String SHOW;

  public final String HELP;
  public final String EXECUTE_SCRIPT;
  public final String EXIT;

  private final List<String> commands;

  @Inject
  public CommandMediator() {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("CommandMediator");

    LOGIN = resourceBundle.getString("commands.login");
    LOGOUT = resourceBundle.getString("commands.logout");
    REGISTER = resourceBundle.getString("commands.register");

    SHOW_HISTORY = resourceBundle.getString("commands.showHistory");
    CLEAR_HISTORY = resourceBundle.getString("commands.clearHistory");

    ADD = resourceBundle.getString("commands.add");
    REMOVE = resourceBundle.getString("commands.remove");
    UPDATE = resourceBundle.getString("commands.update");

    INFO = resourceBundle.getString("commands.info");
    SHOW = resourceBundle.getString("commands.show");

    HELP = resourceBundle.getString("commands.help");
    EXECUTE_SCRIPT = resourceBundle.getString("commands.executeScript");
    EXIT = resourceBundle.getString("commands.exit");

    commands = initCommandList();
  }

  private List<String> initCommandList() {
    return new ArrayList<String>() {
      {
        if (LOGIN != null) {
          add(LOGIN);
        }
        if (LOGOUT != null) {
          add(LOGOUT);
        }
        if (REGISTER != null) {
          add(REGISTER);
        }

        if (SHOW_HISTORY != null) {
          add(SHOW_HISTORY);
        }
        if (CLEAR_HISTORY != null) {
          add(CLEAR_HISTORY);
        }

        if (ADD != null) {
          add(ADD);
        }
        if (REMOVE != null) {
          add(REMOVE);
        }
        if (UPDATE != null) {
          add(UPDATE);
        }

        if (INFO != null) {
          add(INFO);
        }
        if (SHOW != null) {
          add(SHOW);
        }

        if (HELP != null) {
          add(HELP);
        }
        if (EXECUTE_SCRIPT != null) {
          add(EXECUTE_SCRIPT);
        }
        if (EXIT != null) {
          add(EXIT);
        }
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
