package ru.storage.client.app.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.app.connection.exceptions.ClientException;
import ru.storage.client.app.connection.exceptions.ConnectionException;
import ru.storage.common.exit.ExitListener;
import ru.storage.common.transfer.serizliser.Serializer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public final class ClientProcessor implements ExitListener {
  private final Logger logger;
  private final ConnectionProcessor client;
  private final SocketChannel socketChannel;
  private final ClientConnection server;
  private boolean processing;

  public ClientProcessor(
      InetAddress address, int port, ConnectionProcessor client, Serializer serializer)
      throws ConnectionException {
    this.logger = LogManager.getLogger(ClientProcessor.class);
    this.processing = true;
    this.client = client;

    try {
      this.socketChannel = SocketChannel.open(new InetSocketAddress(address, port));
      socketChannel.configureBlocking(false);
      logger.info(() -> "Client was opened.");
    } catch (IOException e) {
      logger.error(() -> "Cannot open client.", e);
      throw new ConnectionException(e);
    }

    try {
      this.server = new ClientConnection(serializer, socketChannel);
    } catch (ClientException e) {
      logger.error(() -> "Cannot create client connection.", e);
      throw new ConnectionException(e);
    }
  }

  public void start() throws ClientException {
    while (processing) {
      client.process(server);
      logger.info(() -> "Processed server.");
    }
  }

  @Override
  public void exit() {
    processing = false;
  }
}
