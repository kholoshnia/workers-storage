package ru.storage.server.app.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.exitManager.ExitListener;
import ru.storage.common.transfer.serizliser.Serializer;
import ru.storage.server.app.connection.exceptions.ConnectionException;
import ru.storage.server.app.connection.exceptions.ServerException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public final class ServerProcessor implements ExitListener {
  private final Logger logger;
  private final ConnectionProcessor server;
  private final Serializer serializer;
  private final Selector selector;
  private final ServerSocketChannel serverSocketChannel;
  private boolean processing;

  public ServerProcessor(
      InetAddress address, int port, ConnectionProcessor server, Serializer serializer)
      throws ConnectionException {
    this.logger = LogManager.getLogger(ServerProcessor.class);
    this.processing = true;
    this.server = server;
    this.serializer = serializer;

    try {
      this.serverSocketChannel = ServerSocketChannel.open();
      this.serverSocketChannel.bind(new InetSocketAddress(address, port));
      this.serverSocketChannel.configureBlocking(false);

      this.selector = Selector.open();
      this.serverSocketChannel.register(selector, serverSocketChannel.validOps());

      logger.info(() -> "Server was opened.");
    } catch (IOException e) {
      logger.error(() -> "Cannot open server.", e);
      throw new ConnectionException(e);
    }
  }

  private void accept(Selector selector) throws ServerException {
    try {
      SocketChannel client = serverSocketChannel.accept();
      client.configureBlocking(false);
      client.register(selector, SelectionKey.OP_READ);
    } catch (IOException e) {
      logger.error(() -> "Cannot accept client.", e);
      throw new ServerException(e);
    }
  }

  private void handle(SelectionKey selectionKey) throws ServerException {
    SocketChannel client = (SocketChannel) selectionKey.channel();
    ServerConnection serverConnection = new ServerConnection(serializer, client);
    logger.info("Server connection was created.");
    server.process(serverConnection);
  }

  private void process(Iterator<SelectionKey> iterator) throws ServerException {
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

  public void start() throws ServerException {
    while (processing) {
      try {
        Thread.sleep(200);
        selector.selectNow();

        Set<SelectionKey> keys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = keys.iterator();

        process(iterator);
        logger.info(() -> "Processed client.");
      } catch (IOException | InterruptedException e) {
        throw new ServerException(e);
      }
    }
  }

  @Override
  public void exit() {
    processing = false;
  }
}
