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
  private static final String WRONG_ARGUMENTS_NUMBER;

  private static final Logger LOGGER = LogManager.getLogger(App.class);

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.App");

    FATAL_ERROR = resourceBundle.getString("exceptionMessages.fatalError");
    WRONG_ARGUMENTS_NUMBER = resourceBundle.getString("exceptionMessages.wrongArgumentsNumber");
  }

  public static void main(String[] args) {
    try {
      LOGGER.info(() -> "Launching app...");

      if (args.length > 1) {
        LOGGER.fatal(() -> "Wrong arguments number. App was not started.");
        System.err.println(WRONG_ARGUMENTS_NUMBER);
        System.exit(1);
      }

      LOGGER.info(() -> "Creating Guice injector...");
      Injector injector = Guice.createInjector(new ClientModule(args));
      LOGGER.info(() -> "Guice injector was created.");

      Client client = injector.getInstance(Client.class);
      LOGGER.info(() -> "Client was created.");

      LOGGER.info(() -> "Client was started.");
      client.start();
    } catch (Throwable throwable) {
      LOGGER.fatal(() -> "Got a throwable during work of server.", throwable);
      System.err.println(FATAL_ERROR);
      System.err.println(throwable.getMessage());
      LOGGER.fatal(() -> "App was stopped with error.");
      System.exit(1);
    }
  }
}
