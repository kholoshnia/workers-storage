package ru.storage.client.controller.argumentFormer;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.localeManager.LocaleListener;

import java.util.Map;

public final class FormerMediator implements LocaleListener {
  private final Logger logger;
  private Map<String, ArgumentFormer> argumentFormers;

  @Inject
  public FormerMediator(Map<String, ArgumentFormer> argumentFormerMap) {
    logger = LogManager.getLogger(FormerMediator.class);
    this.argumentFormers = argumentFormerMap;
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
