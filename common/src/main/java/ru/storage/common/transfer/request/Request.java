package ru.storage.common.transfer.request;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

public final class Request implements Serializable {
  private final String command;
  private final Map<String, String> arguments;
  private final Locale locale;

  public Request(String command, Map<String, String> arguments, Locale locale) {
    this.command = command;
    this.arguments = arguments;
    this.locale = locale;
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
        + '}';
  }
}
