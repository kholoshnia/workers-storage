package ru.storage.server.controller.services.history;

import ru.storage.common.transfer.response.Response;

import java.util.Map;

public final class Record implements Cloneable {
  private final String command;
  private final Map<String, String> arguments;
  private final Response response;

  public Record(String command, Map<String, String> arguments, Response response) {
    this.command = command;
    this.arguments = arguments;
    this.response = response;
  }

  public String getCommand() {
    return command;
  }

  public Map<String, String> getArguments() {
    return arguments;
  }

  public Response getResponse() {
    return response;
  }

  @Override
  public Record clone() {
    return new Record(this.command, this.arguments, this.response);
  }
}
