package ru.storage.client.view.console;

import ru.storage.client.view.View;

public interface Console extends View {
  /**
   * Reads line from the console.
   *
   * @param prompt command prompt
   * @param mask concrete mask
   * @return new line
   */
  String readLine(String prompt, Character mask);

  /**
   * Writes string.
   *
   * @param string concrete string to write
   */
  void write(String string);

  /** Writes new line. */
  void writeLine();

  /**
   * Wring string and then new line.
   *
   * @param string concrete string to write
   */
  void writeLine(String string);

  /**
   * Sets user to the use it as command prompt.
   *
   * @param user new user
   */
  void setUser(String user);
}
