package ru.storage.client.controller.responseHandler.formatter;

public enum AnsiColor {
  BLACK("\u001b[30m"),
  RED("\u001b[31"),
  GREEN("\u001b[32m"),
  YELLOW("\u001b[33m"),
  BLUE("\u001b[34m"),
  MAGENTA("\u001b[35m"),
  CYAN("\u001b[36m"),
  WHITE("\u001b[37m"),
  RESET("\u001b[0m");

  private final String colorCode;

  AnsiColor(String colorCode) {
    this.colorCode = colorCode;
  }

  @Override
  public String toString() {
    return colorCode;
  }
}