package ru.storage.client.app.guice;

import com.google.inject.AbstractModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ClientModule extends AbstractModule {
  private final Logger logger;

  public ClientModule(String[] args) {
    this.logger = LogManager.getLogger(ClientModule.class);
  }
}
