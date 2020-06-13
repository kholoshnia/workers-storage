package ru.storage.common.transfer.request;

import java.io.Serializable;
import java.util.Map;

public final class Request implements Serializable {
  private final String command;
  private final Map<String, String> arguments;

  public Request(String command, Map<String, String> arguments) {
    this.command = command;
    this.arguments = arguments;
  }

  public String getCommand() {
    return command;
  }

  public Map<String, String> getArguments() {
    return arguments;
  }

  @Override
  public String toString() {
    return "Request{" + "command='" + command + '\'' + ", arguments=" + arguments + '}';
  }
}
