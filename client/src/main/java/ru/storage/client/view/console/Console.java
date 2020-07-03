package ru.storage.client.view.console;

import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.UserInterruptException;
import ru.storage.client.app.connection.Connection;
import ru.storage.client.app.connection.exceptions.ClientConnectionException;
import ru.storage.client.controller.argumentFormer.FormerMediator;
import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.client.controller.localeManager.LocaleManager;
import ru.storage.client.controller.requestBuilder.RequestBuilder;
import ru.storage.client.controller.requestBuilder.exceptions.BuildingException;
import ru.storage.client.controller.responseHandler.ResponseHandler;
import ru.storage.client.view.View;
import ru.storage.client.view.console.exceptions.ConsoleException;
import ru.storage.common.CommandMediator;
import ru.storage.common.managers.exit.ExitListener;
import ru.storage.common.serizliser.exceptions.DeserializationException;
import ru.storage.common.transfer.request.Request;
import ru.storage.common.transfer.response.Response;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Console implements View, ExitListener, LocaleListener {
  private final Logger logger;
  private final Connection connection;
  private final CommandMediator commandMediator;
  private final Pattern regex;
  private final ResponseHandler responseHandler;
  private final LocaleManager localeManager;
  private final FormerMediator formerMediator;
  private final JlineConsole jlineConsole;

  private LineReader reader;
  private PrintWriter writer;
  private String prompt;
  private boolean processing;

  private String connectedMessage;
  private String connectingMessage;
  private String greetingsMessage;
  private String connectionException;
  private String deserializationException;
  private String buildingException;
  private String noSuchCommandMessage;

  public Console(
      Configuration configuration,
      InputStream inputStream,
      OutputStream outputStream,
      Connection connection,
      CommandMediator commandMediator,
      LocaleManager localeManager,
      FormerMediator formerMediator,
      ResponseHandler responseHandler)
      throws ConsoleException {
    this.logger = LogManager.getLogger(Console.class);
    this.jlineConsole = new JlineConsole(configuration, inputStream, outputStream);
    this.reader = jlineConsole.getLineReader();
    this.writer = jlineConsole.getPrintWriter();
    this.connection = connection;
    this.commandMediator = commandMediator;
    this.localeManager = localeManager;
    this.localeManager.subscribe(this);
    this.formerMediator = formerMediator;
    this.responseHandler = responseHandler;
    this.regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
    this.prompt = " $ ";
    this.processing = true;
  }

  @Override
  public void changeLocale() {
    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("localized.Console", Locale.getDefault());

    connectedMessage = resourceBundle.getString("messages.connected");
    connectingMessage = resourceBundle.getString("messages.connecting");
    greetingsMessage = resourceBundle.getString("messages.greetings");
    connectionException = resourceBundle.getString("exceptions.connection");
    deserializationException = resourceBundle.getString("exceptions.deserialization");
    buildingException = resourceBundle.getString("exceptions.building");
    noSuchCommandMessage = resourceBundle.getString("exceptions.noSuchCommand");

    jlineConsole.changeLocale();
    reader = jlineConsole.getLineReader();
    writer = jlineConsole.getPrintWriter();
  }

  /** Processes client console */
  public void process() {
    localeManager.changeLocale();
    writeLine(greetingsMessage);

    try {
      connection.connect();
    } catch (ClientConnectionException e) {
      logger.fatal(() -> "Cannot connect to the server...");
      writeLine(connectionException);
      return;
    }

    waitConnection();
    writeLine();

    while (processing) {
      logger.info(() -> "Waiting for user input...");

      String input = readLine(prompt, null);
      List<String> words = parse(input);
      logger.info("Got user input: {}.", () -> words);

      if (words == null) {
        logger.info(() -> "User input is null, continuing.");
        continue;
      }

      Request request = createRequest(words);
      logger.info("Request was created: {}.", () -> request);

      if (request == null) {
        writeLine(noSuchCommandMessage);
        logger.info(() -> "Got null request, continuing.");
        continue;
      }

      logger.info(() -> "Sending request to the server...");
      Response response;

      try {
        connection.write(request);
      } catch (ClientConnectionException e) {
        logger.info(() -> "Error in connection with server.", e);
        writeLine(connectionException);
        waitConnection();
        continue;
      }

      try {
        response = connection.read();
      } catch (ClientConnectionException e) {
        logger.info(() -> "Error in connection with server.", e);
        writeLine(connectionException);
        waitConnection();
        continue;
      } catch (DeserializationException e) {
        logger.info(() -> "Got deserialization exception.", e);
        writeLine(deserializationException);
        continue;
      }

      String answer = responseHandler.handle(response);
      logger.info("Got answer from server: {}.", () -> answer);
      writeLine(answer);
    }
  }

  /**
   * Creates new request using {@link RequestBuilder}.
   *
   * @param words user input words
   * @return new request
   */
  private Request createRequest(List<String> words) {
    String command = words.get(0);
    List<String> arguments;

    if (words.size() > 1) {
      arguments = words.subList(1, words.size() - 1);
    } else {
      arguments = new ArrayList<>();
    }

    try {
      return new RequestBuilder()
          .setFormerMediator(formerMediator)
          .setCommand(command)
          .setArguments(arguments)
          .setLocale(Locale.getDefault())
          .build();
    } catch (BuildingException e) {
      logger.warn(() -> "Request building exception.", e);
      writeLine(buildingException);
      return null;
    }
  }

  /** Waits connection to the server. Checks connection every 1 second. */
  private void waitConnection() {
    while (!connection.isConnected()) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        logger.error(() -> "Cannot interrupt thread.", e);
      }

      writeLine(String.format("\r%s...", connectingMessage));
    }

    logger.info(() -> "Connected to the server.");
    writeLine(connectedMessage);
  }

  /**
   * Reads next line. NOTE:
   *
   * <ul>
   *   <li>If mask is null then no mask is being applied to the input;
   *   <li>if prompt is null then there is no output prompt;
   *   <li>if user input is empty line returns null;
   *   <li>if user presses ^D returns null;
   *   <li>if user presses ^C returns exit command.
   * </ul>
   *
   * @param prompt input prompt
   * @param mask character mask
   * @return line from input stream
   */
  public String readLine(String prompt, Character mask) {
    String input;

    try {
      input = reader.readLine(prompt, mask);
    } catch (UserInterruptException e) {
      input = commandMediator.EXIT;
    } catch (EndOfFileException e) {
      input = null;
    }

    return input;
  }

  /**
   * Writes line to the output stream.
   *
   * <p>Using the specified in the constructor output stream.
   *
   * @param string concrete string to write
   */
  public void write(String string) {
    writer.write(string);
  }

  /**
   * Writes line and {@link System#lineSeparator()}.
   *
   * <p>Using the specified in the constructor output stream.
   *
   * @param string concrete string to write
   * @see Console#write(String)
   */
  public void writeLine(String string) {
    write(string);
    write(System.lineSeparator());
  }

  /**
   * Writes {@link System#lineSeparator()}.
   *
   * <p>Using the specified in the constructor output stream.
   *
   * @see Console#write(String)
   */
  public void writeLine() {
    write(System.lineSeparator());
  }

  /**
   * Parses string by words in a list of string. Words can be separated by spaces or can be
   * surrounded by " and ' symbols. NOTE: if there is no words found returns null.
   *
   * @param string concrete string to parse
   * @return list of words from string
   */
  private List<String> parse(String string) {
    if (string == null) {
      return null;
    }

    List<String> words = new ArrayList<>();
    Matcher matcher = regex.matcher(string);

    while (matcher.find()) {
      if (matcher.group(1) != null) {
        words.add(matcher.group(1));
      } else if (matcher.group(2) != null) {
        words.add(matcher.group(2));
      } else {
        words.add(matcher.group());
      }
    }

    if (words.isEmpty()) {
      return null;
    }

    words.replaceAll(String::trim);
    return words;
  }

  @Override
  public void exit() {
    processing = false;
    logger.debug("Exiting...");
  }
}
