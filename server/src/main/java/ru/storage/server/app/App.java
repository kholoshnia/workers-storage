package ru.storage.server.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.server.app.guice.GeneralModule;

import java.util.Locale;
import java.util.ResourceBundle;

/** Main server application class. */
public final class App {
  private static final String FATAL_ERROR;
  private static final String WRONG_ARGUMENTS_NUMBER;
  private static final Logger LOGGER = LogManager.getLogger(App.class);

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.App", Locale.ENGLISH);

    FATAL_ERROR = resourceBundle.getString("exceptionMessages.fatalError");
    WRONG_ARGUMENTS_NUMBER = resourceBundle.getString("exceptionMessages.wrongArgumentsNumber");
  }

  public static void main(String[] args) {
    try {
      LOGGER.info("Launching app...");

      if (args.length != 2) {
        LOGGER.fatal("Wrong arguments number. App was not started.");
        System.err.println(WRONG_ARGUMENTS_NUMBER);
        System.exit(1);
      }

      LOGGER.info("Creating Guice injector...");
      Injector injector = Guice.createInjector(new GeneralModule(args));
      LOGGER.info("Guice injector was created SUCCESSFULLY.");

      Server server = injector.getInstance(Server.class);
      LOGGER.info("Server was created SUCCESSFULLY.");

      LOGGER.info("Server was started.");
      server.start();
    } catch (Throwable throwable) {
      LOGGER.fatal("Got a throwable during work of server.", throwable);
      System.err.println(FATAL_ERROR);
      System.err.println(throwable.getMessage());
      LOGGER.fatal("App was stopped with error.");
      System.exit(1);
    }
  }
}
