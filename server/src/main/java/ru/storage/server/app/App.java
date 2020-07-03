package ru.storage.server.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.server.app.guice.ServerModule;

import java.util.ResourceBundle;

/** Main server application class. */
public final class App {
  private static final String FATAL_ERROR;
  private static final String WRONG_ARGUMENTS_NUMBER_ERROR;

  private static final Logger LOGGER = LogManager.getLogger(App.class);

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.App");

    FATAL_ERROR = resourceBundle.getString("errors.fatal");
    WRONG_ARGUMENTS_NUMBER_ERROR = resourceBundle.getString("errors.wrongArgumentsNumber");
  }

  public static void main(String[] args) {
    try {
      LOGGER.debug(() -> "Launching application...");

      if (args.length != 2) {
        LOGGER.fatal(() -> "Wrong arguments number. App has not been started.");
        System.err.println(WRONG_ARGUMENTS_NUMBER_ERROR);
        System.exit(1);
      }

      LOGGER.debug(() -> "Creating Guice injector...");
      Injector injector = Guice.createInjector(new ServerModule(args));
      LOGGER.debug(() -> "Guice injector has been created.");

      Server server = injector.getInstance(Server.class);
      LOGGER.debug(() -> "Server has been created.");

      LOGGER.debug(() -> "Server has been started.");
      server.start();
    } catch (Throwable throwable) {
      LOGGER.fatal(() -> "Got a throwable during work of server.", throwable);
      System.err.println(FATAL_ERROR);
      System.err.println(throwable.getMessage());
      LOGGER.fatal(() -> "Application has been stopped with an error.");
      System.exit(1);
    }
  }
}
