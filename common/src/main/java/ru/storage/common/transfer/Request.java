package ru.storage.common.transfer;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

public final class Request implements Serializable {
  private final String command;
  private final Map<String, String> arguments;
  private final Locale locale;
  private final String login;
  private final String token;

  public Request(
      String command, Map<String, String> arguments, Locale locale, String login, String token) {
    this.command = command;
    this.arguments = arguments;
    this.locale = locale;
    this.login = login;
    this.token = token;
  }

  public String getCommand() {
    return command;
  }

  public Map<String, String> getArguments() {
    return arguments;
  }

  public Locale getLocale() {
    return locale;
  }

  public String getLogin() {
    return login;
  }

  public String getToken() {
    return token;
  }

  @Override
  public String toString() {
    return "Request{"
        + "command='"
        + command
        + '\''
        + ", arguments="
        + arguments
        + ", locale="
        + locale
        + ", login='"
        + login
        + '\''
        + '}';
  }
}
