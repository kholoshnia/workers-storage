package ru.storage.client.app.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.app.connection.exceptions.ClientConnectionException;
import ru.storage.common.serizliser.Serializer;
import ru.storage.common.serizliser.exceptions.DeserializationException;
import ru.storage.common.transfer.request.Request;
import ru.storage.common.transfer.response.Response;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public final class ServerWorker {
  private final Logger logger;
  private final ByteBuffer byteBuffer;
  private final Serializer serializer;
  private final SocketChannel server;
  private final InetSocketAddress socketAddress;

  public ServerWorker(int bufferSize, Serializer serializer, InetAddress address, int port)
      throws ClientConnectionException {
    this.logger = LogManager.getLogger(ServerWorker.class);
    this.byteBuffer = ByteBuffer.allocate(bufferSize);
    this.serializer = serializer;
    this.socketAddress = new InetSocketAddress(address, port);

    try {
      this.server = SocketChannel.open();
      this.server.configureBlocking(false);
    } catch (IOException e) {
      logger.info(() -> "Cannot configure client connection.", e);
      throw new ClientConnectionException(e);
    }
  }

  public void connect() throws ClientConnectionException {
    try {
      server.connect(socketAddress);
    } catch (IOException e) {
      logger.warn(() -> "Cannot connect to the server.");
      throw new ClientConnectionException(e);
    }
  }

  public boolean isConnected() {
    return server.isConnected();
  }

  public Response read() throws ClientConnectionException, DeserializationException {
    try {
      byteBuffer.clear();
      server.read(byteBuffer);
      return serializer.deserialize(byteBuffer.array(), Response.class);
    } catch (IOException e) {
      logger.error(() -> "Cannot read response.", e);
      throw new ClientConnectionException(e);
    }
  }

  public void write(Request request) throws ClientConnectionException {
    try {
      byteBuffer.clear();
      byteBuffer.put(serializer.serialize(request));
      server.write(byteBuffer);
    } catch (IOException e) {
      logger.error(() -> "Cannot write request.", e);
      throw new ClientConnectionException(e);
    }
  }
}
