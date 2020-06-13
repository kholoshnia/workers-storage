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

  private static final Logger logger = LogManager.getLogger(App.class);

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.App");

    FATAL_ERROR = resourceBundle.getString("exceptionMessages.fatalError");
    WRONG_ARGUMENTS_NUMBER = resourceBundle.getString("exceptionMessages.wrongArgumentsNumber");
  }

  public static void main(String[] args) {
    try {
      logger.info("Launching app...");

      if (args.length > 1) {
        logger.fatal("Wrong arguments number. App was not started.");
        System.err.println(WRONG_ARGUMENTS_NUMBER);
        System.exit(1);
      }

      logger.info("Creating Guice injector...");
      Injector injector = Guice.createInjector(new ClientModule(args));
      logger.info("Guice injector was created SUCCESSFULLY.");

      Client client = injector.getInstance(Client.class);
      logger.info("Client was created SUCCESSFULLY.");

      logger.info("Client was started.");
      client.start();
    } catch (Throwable throwable) {
      logger.fatal("Got a throwable during work of server.", throwable);
      System.err.println(FATAL_ERROR);
      System.err.println(throwable.getMessage());
      logger.fatal("App was stopped with error.");
      System.exit(1);
    }
  }
}
