package ru.storage.client.view.console;

import org.apache.commons.configuration2.Configuration;
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
  private final Configuration configuration;
  private final Terminal terminal;
  private final CommandMediator commandMediator;

  private String executableCommandsGroup;
  private String datesGroup;
  private String statusesGroup;

  private String executableCommandDescription;
  private String currentDateDescription;
  private String statusDescription;

  public JlineConsole(
      Configuration configuration,
      InputStream inputStream,
      OutputStream outputStream,
      CommandMediator commandMediator)
      throws ConsoleException {
    logger = LogManager.getLogger(JlineConsole.class);
    this.configuration = configuration;
    terminal = initTerminal(inputStream, outputStream);
    this.commandMediator = commandMediator;
  }

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("localized.JlineConsole", Locale.getDefault());

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
    return new ArrayList<Candidate>() {
      {
        add(newCommandCandidate(commandMediator.LOGIN));
        add(newCommandCandidate(commandMediator.LOGOUT));
        add(newCommandCandidate(commandMediator.REGISTER));
        add(newCommandCandidate(commandMediator.SHOW_HISTORY));
        add(newCommandCandidate(commandMediator.CLEAR_HISTORY));
        add(newCommandCandidate(commandMediator.ADD));
        add(newCommandCandidate(commandMediator.REMOVE));
        add(newCommandCandidate(commandMediator.UPDATE));
        add(newCommandCandidate(commandMediator.EXIT));
        add(newCommandCandidate(commandMediator.HELP));
        add(newCommandCandidate(commandMediator.INFO));
        add(newCommandCandidate(commandMediator.SHOW));
      }
    };
  }

  private Candidate newCommandCandidate(String command) {
    return new Candidate(
        command, command, executableCommandsGroup, executableCommandDescription, null, null, true);
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
