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
  private static final Logger logger = LogManager.getLogger(SelectorConnection.class);

  protected final Selector selector;

  private boolean processing;

  public SelectorConnection() throws SelectorException {
    processing = true;

    try {
      selector = Selector.open();
      logger.debug(() -> "Selector was opened.");
    } catch (IOException e) {
      logger.error(() -> "Cannot open selector.", e);
      throw new SelectorException(e);
    }
  }

  /**
   * Processes keys selection.
   *
   * @throws SelectorException - in case of selector exceptions
   * @throws ServerException - in case of server exceptions
   */
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

  /**
   * Accepts selector.
   *
   * @param selector selector to accept
   * @throws SelectorException - in case of accepting errors
   */
  protected abstract void accept(Selector selector) throws SelectorException;

  /**
   * Handles selection key.
   *
   * @param selectionKey selection key
   * @throws ServerException - in case of handling errors.
   */
  protected abstract void handle(SelectionKey selectionKey) throws ServerException;

  /**
   * Processes selector keys.
   *
   * @param iterator selection key iterator
   * @throws SelectorException - in case of selector exceptions
   * @throws ServerException - in case of server exceptions
   */
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
