package ru.storage.client.app;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.app.exceptions.ClientException;
import ru.storage.client.view.View;

public final class Client {
  private final Logger logger;
  private final View view;

  @Inject
  public Client(View view) {
    this.logger = LogManager.getLogger(Client.class);
    this.view = view;
  }

  public void start() throws ClientException {
    try {
      view.start();
    } catch (Throwable e) {
      logger.fatal("Error while work of user interface.");
      throw new ClientException(e);
    }
  }
}
