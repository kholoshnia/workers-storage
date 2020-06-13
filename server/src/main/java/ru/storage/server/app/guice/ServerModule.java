package ru.storage.server.app.guice;

import com.google.inject.AbstractModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ServerModule extends AbstractModule {
  private final Logger logger;

  private final String USER;
  private final String PASSWORD;

  public ServerModule(String[] args) {
    this.logger = LogManager.getLogger(ServerModule.class);

    USER = args[0];
    PASSWORD = args[1];
  }
}
