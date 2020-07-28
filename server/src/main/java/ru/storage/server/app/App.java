package ru.storage.server.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.server.app.guice.ServerModule;

import java.util.ResourceBundle;

/** Main server application class. */
public final class App {
  private static final Logger logger = LogManager.getLogger(App.class);

  private static final String FATAL_ERROR;
  private static final String WRONG_ARGUMENTS_NUMBER_ERROR;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.App");

    FATAL_ERROR = resourceBundle.getString("errors.fatal");
    WRONG_ARGUMENTS_NUMBER_ERROR = resourceBundle.getString("errors.wrongArgumentsNumber");
  }

  public static void main(String[] args) {
    try {
      logger.debug(() -> "Launching application...");

      if (args.length != 3) {
        logger.fatal(() -> "Wrong arguments number. App was not started.");
        System.err.println(WRONG_ARGUMENTS_NUMBER_ERROR);
        System.exit(1);
      }

      logger.debug(() -> "Creating Guice injector...");
      Injector injector = Guice.createInjector(new ServerModule(args));
      logger.debug(() -> "Guice injector was created.");

      Server server = injector.getInstance(Server.class);
      logger.debug(() -> "Server was created.");

      logger.debug(() -> "Server was started.");
      server.start();
    } catch (Exception exception) {
      logger.fatal(() -> "Got an exception during work of server.", exception);
      System.err.println();
      System.err.println(FATAL_ERROR);
      System.err.println();
      System.err.println(exception.getMessage());
      logger.fatal(() -> "Application was stopped with an error.");
      System.exit(1);
    }
  }
}
