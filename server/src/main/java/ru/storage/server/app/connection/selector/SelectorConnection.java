package ru.storage.server.app.connection.selector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.exit.ExitListener;
import ru.storage.server.app.connection.selector.exceptions.ConnectionException;
import ru.storage.server.app.exceptions.ServerException;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

public abstract class SelectorConnection implements ExitListener {
  protected final Selector selector;

  private final Logger logger;
  private boolean processing;

  public SelectorConnection() throws ConnectionException {
    this.logger = LogManager.getLogger(SelectorConnection.class);
    this.processing = true;

    try {
      this.selector = Selector.open();
      logger.debug(() -> "Selector was opened.");
    } catch (IOException e) {
      logger.error(() -> "Cannot open selector.", e);
      throw new ConnectionException(e);
    }
  }

  public final void start() throws ConnectionException, ServerException {
    while (processing) {
      try {
        Thread.sleep(200);
        selector.selectNow();

        Set<SelectionKey> keys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = keys.iterator();

        process(iterator);
      } catch (IOException | InterruptedException e) {
        throw new ConnectionException(e);
      }
    }
  }

  protected abstract void accept(Selector selector) throws ConnectionException;

  protected abstract void handle(SelectionKey selectionKey)
      throws ConnectionException, ServerException;

  private void process(Iterator<SelectionKey> iterator)
      throws ConnectionException, ServerException {
    while (iterator.hasNext()) {
      SelectionKey key = iterator.next();

      if (key.isAcceptable()) {
        accept(selector);
      }

      if (key.isReadable()) {
        handle(key);
      }

      iterator.remove();
    }
  }

  @Override
  public void exit() {
    processing = false;
  }
}