package ru.storage.client.controller.argumentFormer;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.argumentFormer.argumentFormers.NewWorkerFormer;
import ru.storage.client.controller.argumentFormer.argumentFormers.NewWorkerIDFormer;
import ru.storage.client.controller.argumentFormer.argumentFormers.NoArgumentsFormer;
import ru.storage.client.controller.argumentFormer.argumentFormers.WorkerIDFormer;
import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;

import java.util.HashMap;
import java.util.Map;

public final class FormerMediator implements LocaleListener {
  private final Logger logger;
  private Map<String, ArgumentFormer> argumentFormers;

  @Inject
  public FormerMediator(CommandMediator commandMediator, ArgumentMediator argumentMediator) {
    this.logger = LogManager.getLogger(FormerMediator.class);

    ArgumentFormer noArgumentsFormer = new NoArgumentsFormer();
    ArgumentFormer workerIDFormer = new WorkerIDFormer(argumentMediator);
    ArgumentFormer newWorkerFormer = new NewWorkerFormer(argumentMediator);
    ArgumentFormer newWorkerID = new NewWorkerIDFormer(argumentMediator);

    this.argumentFormers =
        new HashMap<String, ArgumentFormer>() {
          {
            put(commandMediator.LOGIN, noArgumentsFormer);
            put(commandMediator.LOGOUT, noArgumentsFormer);
            put(commandMediator.REGISTER, noArgumentsFormer);
            put(commandMediator.SHOW_HISTORY, noArgumentsFormer);
            put(commandMediator.CLEAR_HISTORY, noArgumentsFormer);
            put(commandMediator.ADD, newWorkerFormer);
            put(commandMediator.REMOVE, workerIDFormer);
            put(commandMediator.UPDATE, newWorkerID);
            put(commandMediator.EXIT, workerIDFormer);
            put(commandMediator.HELP, noArgumentsFormer);
            put(commandMediator.INFO, noArgumentsFormer);
            put(commandMediator.SHOW, noArgumentsFormer);
          }
        };

    logger.debug(() -> "Argument former map was created.");
  }

  @Override
  public void changeLocale() {
    argumentFormers.values().forEach(LocaleListener::changeLocale);
  }

  public ArgumentFormer getArgumentFormer(String command) {
    ArgumentFormer argumentFormer = argumentFormers.get(command);

    logger.info("Got argument former {}, for command {}.", () -> argumentFormer, () -> command);
    return argumentFormer;
  }
}
