package ru.storage.server.app.connection.selector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.exitManager.ExitListener;
import ru.storage.server.app.connection.exceptions.ServerException;
import ru.storage.server.app.connection.selector.exceptions.SelectorException;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

public abstract class SelectorConnection implements ExitListener {
  protected final Selector selector;

  private final Logger logger;
  private boolean processing;

  public SelectorConnection() throws SelectorException {
    logger = LogManager.getLogger(SelectorConnection.class);
    processing = true;

    try {
      selector = Selector.open();
      logger.debug(() -> "Selector was opened.");
    } catch (IOException e) {
      logger.error(() -> "Cannot open selector.", e);
      throw new SelectorException(e);
    }
  }

  public final void process() throws SelectorException, ServerException {
    while (processing) {
      try {
        Thread.sleep(200);
        selector.selectNow();

        Set<SelectionKey> keys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = keys.iterator();

        processKeys(iterator);
      } catch (IOException | InterruptedException e) {
        throw new SelectorException(e);
      }
    }
  }

  protected abstract void accept(Selector selector) throws SelectorException;

  protected abstract void handle(SelectionKey selectionKey) throws ServerException;

  private void processKeys(Iterator<SelectionKey> iterator)
      throws SelectorException, ServerException {
    while (iterator.hasNext()) {
      SelectionKey key = iterator.next();

      if (key.isValid()) {
        if (key.isAcceptable()) {
          accept(selector);
        }

        if (key.isReadable()) {
          handle(key);
        }
      }

      iterator.remove();
    }
  }

  @Override
  public void exit() {
    processing = false;
    logger.info("Stopping processing the selection...");
  }
}
