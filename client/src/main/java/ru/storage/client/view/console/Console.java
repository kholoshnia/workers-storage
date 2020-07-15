package ru.storage.client.view.console;

import ru.storage.client.view.View;

public interface Console extends View {
  String readLine(String prompt, Character mask);

  void write(String string);

  void writeLine(String string);

  void writeLine();

  void setLogin(String login);
}
