package ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.argumentFormers;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.services.script.Script;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.exceptions.FormingException;
import ru.storage.server.controller.services.script.scriptExecutor.argumentFormer.exceptions.WrongArgumentsException;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public final class NewWorkerFormer extends WorkerFormer {
  private static final Logger logger = LogManager.getLogger(NewWorkerFormer.class);

  private String wrongArgumentsNumberException;

  @Inject
  public NewWorkerFormer(ArgumentMediator argumentMediator) {
    super(argumentMediator);
  }

  @Override
  protected void changeLocale(Locale locale) {
    super.changeLocale(locale);
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.NewWorkerFormer", locale);

    wrongArgumentsNumberException = resourceBundle.getString("exceptions.wrongArgumentsNumber");
  }

  @Override
  public void check(List<String> arguments) throws WrongArgumentsException {
    if (arguments.size() != 0) {
      logger.warn(() -> "Got wrong arguments number.");
      throw new WrongArgumentsException(wrongArgumentsNumberException);
    }
  }

  @Override
  public Map<String, String> form(List<String> arguments, Script script) throws FormingException {
    Map<String, String> allArguments;

    try {
      allArguments = formWorker(script);
    } catch (WrongArgumentsException e) {
      logger.info(() -> "Cannot form worker from script.", e);
      throw new FormingException(e.getMessage());
    }

    logger.info(() -> "All arguments were formed.");
    return allArguments;
  }
}
