package ru.storage.server.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;
import ru.storage.server.app.concurrent.ExecutorService;
import ru.storage.server.app.guice.ServerModule;
import ru.storage.server.controller.auth.AuthController;
import ru.storage.server.controller.check.CheckController;
import ru.storage.server.controller.command.CommandController;
import ru.storage.server.controller.command.factory.CommandFactoryMediator;
import ru.storage.server.controller.services.hash.HashGenerator;
import ru.storage.server.controller.services.history.History;
import ru.storage.server.controller.services.parser.Parser;
import ru.storage.server.model.domain.repository.repositories.userRepository.UserRepository;
import ru.storage.server.model.domain.repository.repositories.workerRepository.WorkerRepository;

import java.security.Key;
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

      if (args.length != 3) {
        LOGGER.fatal(() -> "Wrong arguments number. App has not been started.");
        System.err.println(WRONG_ARGUMENTS_NUMBER_ERROR);
        System.exit(1);
      }

      LOGGER.debug(() -> "Creating Guice injector...");
      Injector injector = Guice.createInjector(new ServerModule(args));
      LOGGER.debug(() -> "Guice injector has been created.");

      Configuration configuration = injector.getInstance(Configuration.class);
      ArgumentMediator argumentMediator = injector.getInstance(ArgumentMediator.class);
      CommandMediator commandMediator = injector.getInstance(CommandMediator.class);
      History history = injector.getInstance(History.class);
      HashGenerator hashGenerator = injector.getInstance(HashGenerator.class);
      UserRepository userRepository = injector.getInstance(UserRepository.class);
      Parser parser = injector.getInstance(Parser.class);
      WorkerRepository workerRepository = injector.getInstance(WorkerRepository.class);
      Key key = injector.getInstance(Key.class);

      CommandFactoryMediator commandFactoryMediator = injector.getInstance(CommandFactoryMediator.class);
      ExecutorService executorService = injector.getInstance(ExecutorService.class);
      CheckController checkController = injector.getInstance(CheckController.class);
      CommandController commandController = injector.getInstance(CommandController.class);
      AuthController authController = injector.getInstance(AuthController.class);

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
