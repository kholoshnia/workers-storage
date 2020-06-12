package ru.storage.common.transfer.request;

import ru.storage.common.dto.DTO;

import java.util.Locale;
import java.util.Map;

public final class RequestDTO implements DTO<Request> {
  public final String command;
  public final Map<String, String> arguments;
  public final Locale locale;

  public RequestDTO(String command, Map<String, String> arguments, Locale locale) {
    this.command = command;
    this.arguments = arguments;
    this.locale = locale;
  }

  @Override
  public Request toEntity() {
    return new Request(this.command, this.arguments, this.locale);
  }

  @Override
  public String toString() {
    return "RequestDTO{"
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
