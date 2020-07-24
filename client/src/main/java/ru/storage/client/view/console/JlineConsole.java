package ru.storage.client.view.console;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jline.reader.Candidate;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.client.view.console.exceptions.ConsoleException;
import ru.storage.common.CommandMediator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/** Setups Jline line reader with specified options. */
public final class JlineConsole implements LocaleListener {
  private final Logger logger;
  private final Terminal terminal;
  private final CommandMediator commandMediator;

  private String executableCommandsGroup;
  private String datesGroup;
  private String statusesGroup;

  private String executableCommandDescription;
  private String currentDateDescription;
  private String statusDescription;

  public JlineConsole(
      InputStream inputStream, OutputStream outputStream, CommandMediator commandMediator)
      throws ConsoleException {
    logger = LogManager.getLogger(JlineConsole.class);
    terminal = initTerminal(inputStream, outputStream);
    this.commandMediator = commandMediator;
  }

  @Override
  public void changeLocale(Locale locale) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.JlineConsole", locale);

    executableCommandsGroup = resourceBundle.getString("groups.executableCommand");
    datesGroup = resourceBundle.getString("groups.dates");
    statusesGroup = resourceBundle.getString("groups.statuses");

    executableCommandDescription = resourceBundle.getString("descriptions.executableCommand");
    currentDateDescription = resourceBundle.getString("descriptions.currentDate");
    statusDescription = resourceBundle.getString("descriptions.status");
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
    return LineReaderBuilder.builder()
        .terminal(terminal)
        .completer(
            (reader, line, candidates) ->
                candidates.addAll(
                    new ArrayList<Candidate>() {
                      {
                        addAll(getCommandCandidates());
                        add(getCurrentDateCandidate());
                        addAll(getStatusCandidates());
                      }
                    }))
        .build();
  }

  public PrintWriter getPrintWriter() {
    return terminal.writer();
  }

  private List<Candidate> getCommandCandidates() {
    logger.info("Forming command candidates.");
    List<Candidate> candidates = new ArrayList<>();

    commandMediator
        .getCommands()
        .forEach(
            command ->
                candidates.add(
                    new Candidate(
                        command,
                        command,
                        executableCommandsGroup,
                        executableCommandDescription,
                        null,
                        null,
                        true)));

    return candidates;
  }

  private Candidate getCurrentDateCandidate() {
    String currentDate = ZonedDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    logger.info("Forming current date candidate.");
    return new Candidate(
        currentDate, currentDate, datesGroup, currentDateDescription, null, null, true);
  }

  private List<Candidate> getStatusCandidates() {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.WorkerValidator");
    logger.info("Forming status candidates.");
    return new ArrayList<Candidate>() {
      {
        add(newStatusCandidate(resourceBundle.getString("constants.fired")));
        add(newStatusCandidate(resourceBundle.getString("constants.hired")));
        add(newStatusCandidate(resourceBundle.getString("constants.promotion")));
      }
    };
  }

  private Candidate newStatusCandidate(String status) {
    return new Candidate(status, status, statusesGroup, statusDescription, null, null, true);
  }
}
