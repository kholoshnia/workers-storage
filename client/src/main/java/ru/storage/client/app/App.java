package ru.storage.client.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.app.guice.ClientModule;

import java.util.ResourceBundle;

/** Main server application class. */
public final class App {
  private static final String FATAL_ERROR;
  private static final String WRONG_ARGUMENTS_NUMBER_ERROR;

  private static final Logger LOGGER = LogManager.getLogger(App.class);

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.App");

    FATAL_ERROR = resourceBundle.getString("errors.fatal");
    WRONG_ARGUMENTS_NUMBER_ERROR = resourceBundle.getString("errors.wrongArgumentsNumber");
  }

  public static void main(String[] args) {
    try {
      LOGGER.debug(() -> "Launching app...");

      if (args.length > 1) {
        LOGGER.fatal(() -> "Wrong arguments number. App was not started.");
        System.err.println(WRONG_ARGUMENTS_NUMBER_ERROR);
        System.exit(1);
      }

      LOGGER.debug(() -> "Creating Guice injector...");
      Injector injector = Guice.createInjector(new ClientModule(args));
      LOGGER.debug(() -> "Guice injector was created.");

      Client client = injector.getInstance(Client.class);
      LOGGER.debug(() -> "Client was created.");

      LOGGER.debug(() -> "Client was started.");
      client.start();
    } catch (Exception exception) {
      LOGGER.fatal(() -> "Got an exception during work of server.", exception);
      System.err.println();
      System.err.println(FATAL_ERROR);
      System.err.println();
      System.err.println(exception.getMessage());
      LOGGER.fatal(() -> "App was stopped with error.");
      System.exit(1);
    }
  }
}
