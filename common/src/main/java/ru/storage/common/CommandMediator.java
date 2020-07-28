package ru.storage.common;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/** Command mediator class contains all command names. */
public final class CommandMediator {
  public final String login;
  public final String logout;
  public final String register;

  public final String showHistory;
  public final String clearHistory;

  public final String add;
  public final String remove;
  public final String update;

  public final String info;
  public final String show;

  public final String help;
  public final String executeScript;
  public final String exit;

  private final List<String> commands;

  @Inject
  public CommandMediator() {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("CommandMediator");

    login = resourceBundle.getString("commands.login");
    logout = resourceBundle.getString("commands.logout");
    register = resourceBundle.getString("commands.register");

    showHistory = resourceBundle.getString("commands.showHistory");
    clearHistory = resourceBundle.getString("commands.clearHistory");

    add = resourceBundle.getString("commands.add");
    remove = resourceBundle.getString("commands.remove");
    update = resourceBundle.getString("commands.update");

    info = resourceBundle.getString("commands.info");
    show = resourceBundle.getString("commands.show");

    help = resourceBundle.getString("commands.help");
    executeScript = resourceBundle.getString("commands.executeScript");
    exit = resourceBundle.getString("commands.exit");

    commands = initCommandList();
  }

  private List<String> initCommandList() {
    return new ArrayList<String>() {
      {
        if (login != null) {
          add(login);
        }
        if (logout != null) {
          add(logout);
        }
        if (register != null) {
          add(register);
        }

        if (showHistory != null) {
          add(showHistory);
        }
        if (clearHistory != null) {
          add(clearHistory);
        }

        if (add != null) {
          add(add);
        }
        if (remove != null) {
          add(remove);
        }
        if (update != null) {
          add(update);
        }

        if (info != null) {
          add(info);
        }
        if (show != null) {
          add(show);
        }

        if (help != null) {
          add(help);
        }
        if (executeScript != null) {
          add(executeScript);
        }
        if (exit != null) {
          add(exit);
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
