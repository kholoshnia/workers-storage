package ru.storage.client.view.console;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.client.view.console.exceptions.ConsoleException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;

/** Setups Jline line reader with specified options. */
public final class JlineConsole implements LocaleListener {
  private final Logger logger;
  private final Configuration configuration;
  private final Terminal terminal;

  public JlineConsole(
      Configuration configuration, InputStream inputStream, OutputStream outputStream)
      throws ConsoleException {
    this.logger = LogManager.getLogger(JlineConsole.class);
    this.configuration = configuration;
    this.terminal = initTerminal(inputStream, outputStream);
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

  public LineReader getLineReader() {
    return LineReaderBuilder.builder().terminal(terminal).build();
  }

  public PrintWriter getPrintWriter() {
    return terminal.writer();
  }
}
