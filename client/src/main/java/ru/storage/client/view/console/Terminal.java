package ru.storage.client.view.console;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.UserInterruptException;
import ru.storage.client.app.connection.ServerWorker;
import ru.storage.client.app.connection.exceptions.ClientConnectionException;
import ru.storage.client.controller.argumentFormer.FormerMediator;
import ru.storage.client.controller.argumentFormer.exceptions.CancelException;
import ru.storage.client.controller.argumentFormer.exceptions.FormingException;
import ru.storage.client.controller.argumentFormer.exceptions.WrongArgumentsException;
import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.client.controller.localeManager.LocaleManager;
import ru.storage.client.controller.requestBuilder.RequestBuilder;
import ru.storage.client.controller.requestBuilder.exceptions.BuildingException;
import ru.storage.client.controller.responseHandler.ResponseHandler;
import ru.storage.client.view.console.exceptions.ConsoleException;
import ru.storage.common.CommandMediator;
import ru.storage.common.exitManager.ExitListener;
import ru.storage.common.exitManager.ExitManager;
import ru.storage.common.exitManager.exceptions.ExitingException;
import ru.storage.common.serizliser.exceptions.DeserializationException;
import ru.storage.common.transfer.Request;
import ru.storage.common.transfer.response.Response;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.channels.ConnectionPendingException;
import java.nio.channels.NotYetConnectedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Terminal implements Console, ExitListener, LocaleListener {
  private final Logger logger;
  private final ExitManager exitManager;
  private final ServerWorker serverWorker;
  private final CommandMediator commandMediator;
  private final List<String> authCommands;
  private final Pattern regex;
  private final List<ResponseHandler> responseHandlers;
  private final LocaleManager localeManager;
  private final FormerMediator formerMediator;
  private final JlineConsole jlineConsole;
  private final String prompt;

  private LineReader reader;
  private PrintWriter writer;
  private String user;
  private String login;
  private String prefix;
  private boolean processing;
  private String token;

  private String connectedMessage;
  private String connectingMessage;
  private String greetingsMessage;
  private String noSuchCommandMessage;
  private String emptyResponseMessage;
  private String wrongResponseMessage;
  private String notAuthenticatedMessage;

  private String notYetConnectedException;
  private String connectionException;
  private String deserializationException;
  private String buildingException;
  private String cancelException;

  public Terminal(
      ExitManager exitManager,
      InputStream inputStream,
      OutputStream outputStream,
      ServerWorker serverWorker,
      CommandMediator commandMediator,
      LocaleManager localeManager,
      FormerMediator formerMediator,
      List<ResponseHandler> responseHandlers)
      throws ConsoleException {
    logger = LogManager.getLogger(Terminal.class);
    this.exitManager = exitManager;
    exitManager.subscribe(this);
    this.serverWorker = serverWorker;
    this.commandMediator = commandMediator;
    authCommands = initAuthCommandsList();
    regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
    this.responseHandlers = responseHandlers;
    this.localeManager = localeManager;
    localeManager.subscribe(this);
    this.formerMediator = formerMediator;
    jlineConsole = new JlineConsole(inputStream, outputStream, commandMediator);
    reader = jlineConsole.getLineReader();
    writer = jlineConsole.getPrintWriter();
    prompt = " ~ $ ";
    prefix = "";
    processing = true;
    login = "";
    token = "";
  }

  /**
   * Initializes a list of authentication commands. If the token is empty and the user enters one of
   * the specified commands, then the request will not be sent to the server as user is not
   * authenticated.
   *
   * @return list of authentication commands
   */
  private List<String> initAuthCommandsList() {
    return new ArrayList<String>() {
      {
        add(commandMediator.LOGIN);
        add(commandMediator.REGISTER);
        add(commandMediator.EXIT);
      }
    };
  }

  @Override
  public void changeLocale(Locale locale) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.Console", locale);

    connectedMessage = resourceBundle.getString("messages.connected");
    connectingMessage = resourceBundle.getString("messages.connecting");
    greetingsMessage = resourceBundle.getString("messages.greetings");
    noSuchCommandMessage = resourceBundle.getString("messages.noSuchCommand");
    emptyResponseMessage = resourceBundle.getString("messages.emptyResponse");
    wrongResponseMessage = resourceBundle.getString("messages.wrongResponse");
    notAuthenticatedMessage = resourceBundle.getString("messages.notAuthenticated");

    notYetConnectedException = resourceBundle.getString("exceptions.notYetConnected");
    connectionException = resourceBundle.getString("exceptions.Connection");
    deserializationException = resourceBundle.getString("exceptions.deserialization");
    buildingException = resourceBundle.getString("exceptions.building");
    cancelException = resourceBundle.getString("exceptions.cancel");

    jlineConsole.changeLocale(locale);
    reader = jlineConsole.getLineReader();
    writer = jlineConsole.getPrintWriter();
  }

  /** Processes client console */
  public void process() throws ExitingException {
    localeManager.changeLocale(Locale.getDefault());
    writeLine(greetingsMessage);

    try {
      serverWorker.connect();
    } catch (ClientConnectionException e) {
      logger.fatal(() -> "Cannot connect to the server...", e);
      writeLine(connectionException);
      return;
    }

    while (processing) {
      logger.info(() -> "Waiting for user input...");

      String input = readLine(prefix + prompt, null);
      List<String> words = parse(input);
      logger.info("Got user input: {}.", () -> words);

      if (words.isEmpty()) {
        logger.info(() -> "User input is empty, continuing.");
        continue;
      }

      Request request = createRequest(words);
      logger.info("Request was created: {}.", () -> request);

      if (request == null) {
        logger.info(() -> "Got null request, continuing.");
        continue;
      }

      logger.info(() -> "Sending request to the server...");
      Response response;

      try {
        serverWorker.write(request);

        try {
          response = serverWorker.read();
        } catch (DeserializationException e) {
          logger.error(() -> "Got wrong or corrupted response.", e);
          writeLine(deserializationException);
          continue;
        }
      } catch (NotYetConnectedException e) {
        logger.info(() -> "Client not yet connected to the server.", e);
        writeLine(notYetConnectedException);
        response = waitConnection(request);
      } catch (ClientConnectionException e) {
        logger.info(() -> "Error in connection with server.", e);
        writeLine(connectionException);
        response = waitConnection(request);
      }

      if (response == null) {
        logger.info(() -> "Got null response continuing...");
        continue;
      }

      handleResponse(response);
    }

    serverWorker.exit();
  }

  /**
   * Creates new request using {@link RequestBuilder}.
   *
   * @param words user input words
   * @return new request
   */
  private Request createRequest(List<String> words) throws ExitingException {
    String command = words.get(0);
    List<String> arguments;

    if (words.size() > 1) {
      arguments = words.subList(1, words.size());
    } else {
      arguments = new ArrayList<>();
    }

    if (!authCommands.contains(command) && token.isEmpty()) {
      writeLine(notAuthenticatedMessage);
      logger.info(() -> "User token is empty, continuing.");
      return null;
    }

    if (!commandMediator.contains(command)) {
      writeLine(noSuchCommandMessage);
      return null;
    }

    if (command.equals(commandMediator.EXIT)) {
      exitManager.exit();
      return null;
    }

    try {
      return new RequestBuilder()
          .setCommand(command)
          .setRawArguments(arguments, formerMediator)
          .setLocale(Locale.getDefault())
          .setLogin(login)
          .setToken(token)
          .build();
    } catch (CancelException e) {
      logger.warn(() -> "Forming was canceled.", e);
      writeLine(cancelException);

      return null;
    } catch (WrongArgumentsException | FormingException e) {
      logger.warn(() -> "Got wrong arguments.", e);
      writeLine(e.getMessage());

      return null;
    } catch (BuildingException e) {
      logger.warn(() -> "Request building exception.", e);
      writeLine(buildingException);

      return null;
    }
  }

  /**
   * Handles response from the server. Prints message to the console using {@link
   * Terminal#write(String)}.
   *
   * @param response response from server
   * @see ResponseHandler
   */
  private void handleResponse(Response response) {
    if (response == null) {
      logger.warn(() -> "Got null response.");
      writeLine(emptyResponseMessage);
      return;
    }

    String token = response.getToken();

    if (token != null) {
      this.token = token;

      if (login == null) {
        prefix = "";
      } else {
        prefix = user;
      }

      login = user;
    }

    String answer = null;

    for (ResponseHandler responseHandler : responseHandlers) {
      answer = responseHandler.handle(response);
      if (answer != null) {
        break;
      }
    }

    if (answer == null) {
      logger.warn("Could not handle response.");
      writeLine(wrongResponseMessage);
      return;
    }

    logger.info("Got answer from server: {}.", response);
    writeLine(answer);
  }

  /**
   * Sets user. Used on auth command. User is used later to update prompt prefix.
   *
   * @param user new user
   */
  public void setUser(String user) {
    this.user = user;
  }

  /**
   * Waits connection with the server. Pending request every 1 second.
   *
   * @param request pending request
   * @return server response
   */
  private Response waitConnection(Request request) {
    Response response = null;
    String anim = "|/-\\";
    int counter = 0;

    do {
      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        logger.error(() -> "Cannot interrupt thread.", e);
      }

      counter++;
      if (counter > 3) {
        counter = 0;
      }

      write(String.format("\r%s %s", anim.charAt(counter % anim.length()), connectingMessage));

      try {
        serverWorker.connect();
        serverWorker.write(request);
        response = serverWorker.read();
      } catch (ClientConnectionException
          | NotYetConnectedException
          | ConnectionPendingException e) {
        logger.error(() -> "Got connection exception, reconnecting.", e);
      } catch (DeserializationException e) {
        logger.error(() -> "Got wrong or corrupted response.", e);
        return null;
      }
    } while (response == null);

    logger.info(() -> "Connected to the server.");
    writeLine(String.format("\r%s", connectedMessage));
    writeLine();

    return response;
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
   * Writes line to the output stream. NOTE: does not write null string.
   *
   * <p>Using the specified in the constructor output stream.
   *
   * @param string concrete string to write
   */
  public void write(String string) {
    if (string == null) {
      return;
    }

    writer.write(string);
    writer.flush();
  }

  /**
   * Writes {@link System#lineSeparator()}.
   *
   * <p>Using the specified in the constructor output stream.
   *
   * @see Terminal#write(String)
   */
  public void writeLine() {
    write(System.lineSeparator());
  }

  /**
   * Writes line and {@link System#lineSeparator()}.
   *
   * <p>Using the specified in the constructor output stream.
   *
   * @param string concrete string to write
   * @see Terminal#write(String)
   */
  public void writeLine(String string) {
    write(string);
    writeLine();
  }

  /**
   * Parses string by words in a list of string. Words can be separated by spaces or can be
   * surrounded by " and ' symbols. NOTE: returns empty list if there is no words found.
   *
   * @param string concrete string to parse
   * @return list of words from string
   */
  private List<String> parse(String string) {
    if (string == null) {
      return new ArrayList<>();
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

    words.replaceAll(String::trim);
    return words;
  }

  @Override
  public void exit() {
    processing = false;
    logger.debug("Exiting...");
  }
}
