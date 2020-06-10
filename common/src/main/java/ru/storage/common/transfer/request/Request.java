package ru.storage.common.transfer.request;

import ru.storage.common.api.dto.DTO;
import ru.storage.common.api.dto.Entity;

import java.util.Locale;
import java.util.Map;

public final class Request implements Entity {
  private final Type type;
  private final String command;
  private final Map<String, String> arguments;
  private final Locale locale;

  public Request(Type type, String command, Map<String, String> arguments, Locale locale) {
    this.type = type;
    this.command = command;
    this.arguments = arguments;
    this.locale = locale;
  }

  @Override
  public DTO<? extends Entity> toDTO() {
    return null;
  }

  public Type getType() {
    return type;
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
        + "type="
        + type
        + ", command='"
        + command
        + '\''
        + ", arguments="
        + arguments
        + ", locale="
        + locale
        + '}';
  }
}
