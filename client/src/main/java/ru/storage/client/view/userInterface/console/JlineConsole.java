package ru.storage.client.view.userInterface.console;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import ru.storage.client.controller.localeListener.LocaleListener;
import ru.storage.client.view.userInterface.console.exceptions.ConsoleException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.ResourceBundle;

/** Setups jline line reader with specified options. */
public final class JlineConsole implements LocaleListener {
  private final Logger logger;
  private final Terminal terminal;
  private final Configuration configuration;

  public JlineConsole(
      Configuration configuration, InputStream inputStream, OutputStream outputStream)
      throws ConsoleException {
    this.logger = LogManager.getLogger(JlineConsole.class);
    changeLocale();
    this.configuration = configuration;
    this.terminal = initTerminal(inputStream, outputStream);
  }

  public LineReader getLineReader() throws ConsoleException {
    return LineReaderBuilder.builder().terminal(terminal).build();
  }

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("localized.JlineConsole", Locale.getDefault());
  }

  private Terminal initTerminal(InputStream inputStream, OutputStream outputStream)
      throws ConsoleException {
    Terminal terminal;

    try {
      terminal = TerminalBuilder.builder().streams(inputStream, outputStream).system(true).build();
    } catch (IOException e) {
      throw new ConsoleException(e);
    }

    return terminal;
  }
}
