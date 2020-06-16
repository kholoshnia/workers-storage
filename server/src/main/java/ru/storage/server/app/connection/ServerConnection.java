package ru.storage.server.app.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.transfer.serizliser.Serializer;
import ru.storage.server.app.connection.selector.SelectorConnection;
import ru.storage.server.app.connection.selector.exceptions.ConnectionException;
import ru.storage.server.app.exceptions.ServerException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ServerConnection extends SelectorConnection {
  private final Logger logger;
  private final Serializer serializer;
  private final ServerProcessor serverProcessor;
  private final ServerSocketChannel serverSocketChannel;

  public ServerConnection(
      InetAddress address, int port, ServerProcessor serverProcessor, Serializer serializer)
      throws ConnectionException, ServerException {
    this.logger = LogManager.getLogger(ServerConnection.class);
    this.serializer = serializer;
    this.serverProcessor = serverProcessor;

    try {
      this.serverSocketChannel = ServerSocketChannel.open();
      this.serverSocketChannel.bind(new InetSocketAddress(address, port));
      this.serverSocketChannel.configureBlocking(false);
      this.serverSocketChannel.register(selector, serverSocketChannel.validOps());
      logger.debug(() -> "Server was opened.");
    } catch (IOException e) {
      logger.error(() -> "Cannot open server.", e);
      throw new ServerException(e);
    }
  }

  @Override
  protected void accept(Selector selector) throws ConnectionException {
    try {
      SocketChannel client = serverSocketChannel.accept();
      client.configureBlocking(false);
      client.register(selector, SelectionKey.OP_READ);
    } catch (IOException e) {
      logger.error(() -> "Cannot accept client.", e);
      throw new ConnectionException(e);
    }
  }

  @Override
  protected void handle(SelectionKey selectionKey) throws ConnectionException, ServerException {
    SocketChannel client = (SocketChannel) selectionKey.channel();
    Connection connection = new Connection(serializer, client);
    logger.info(() -> "Connection with client was created.");
    serverProcessor.process(connection);
  }
}
