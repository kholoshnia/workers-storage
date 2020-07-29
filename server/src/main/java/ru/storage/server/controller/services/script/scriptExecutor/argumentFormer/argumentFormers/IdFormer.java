package ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.argumentFormers;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.services.script.Script;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.ArgumentFormer;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.exceptions.WrongArgumentsException;

import java.util.*;

public final class IdFormer extends ArgumentFormer {
  private static final Logger logger = LogManager.getLogger(IdFormer.class);

  private final ArgumentMediator argumentMediator;

  private String wrongArgumentsNumber;

  @Inject
  public IdFormer(ArgumentMediator argumentMediator) {
    this.argumentMediator = argumentMediator;
  }

  @Override
  protected void changeLocale(Locale locale) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.IdFormer", locale);

    wrongArgumentsNumber = resourceBundle.getString("exceptions.wrongArgumentsNumber");
  }

  @Override
  public void check(List<String> arguments) throws WrongArgumentsException {
    if (arguments.size() != 1) {
      logger.warn(() -> "Got wrong arguments number.");
      throw new WrongArgumentsException(wrongArgumentsNumber);
    }
  }

  @Override
  public Map<String, String> form(List<String> arguments, Script script) {
    Map<String, String> allArguments = new HashMap<>();
    allArguments.put(argumentMediator.workerId, arguments.get(0));

    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
