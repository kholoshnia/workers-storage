package ru.storage.common.transfer.request;

import ru.storage.common.api.dto.DTO;

import java.util.Locale;
import java.util.Map;

public final class RequestDTO implements DTO<Request> {
  public final Type type;
  public final String command;
  public final Map<String, String> arguments;
  public final Locale locale;

  public RequestDTO(Type type, String command, Map<String, String> arguments, Locale locale) {
    this.type = type;
    this.command = command;
    this.arguments = arguments;
    this.locale = locale;
  }

  @Override
  public Request toEntity() {
    return new Request(this.type, this.command, this.arguments, this.locale);
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
