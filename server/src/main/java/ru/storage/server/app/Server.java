package ru.storage.server.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// TODO: Implement using TCP and concurrent.
public final class Server {
  private final Logger logger;

  public Server() {
    this.logger = LogManager.getLogger(Server.class);
  }

  public void start() {}
}
