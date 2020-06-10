package ru.storage.server.app.guice;

import com.google.inject.AbstractModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class GeneralModule extends AbstractModule {
  private final Logger logger;

  private final String user;
  private final String password;

  public GeneralModule(String[] args) {
    this.logger = LogManager.getLogger(GeneralModule.class);

    this.user = args[0];
    this.password = args[1];
  }
}
