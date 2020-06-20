package ru.storage.client.app.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.app.connection.exceptions.ClientConnectionException;
import ru.storage.common.transfer.request.Request;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.serizliser.Serializer;
import ru.storage.common.serizliser.exceptions.DeserializationException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public final class Connection {
  private final Logger logger;
  private final Serializer serializer;
  private final SocketChannel socketChannel;
  private final InetSocketAddress socketAddress;

  public Connection(Serializer serializer, InetAddress address, int port)
      throws ClientConnectionException {
    this.logger = LogManager.getLogger(Connection.class);
    this.serializer = serializer;
    this.socketAddress = new InetSocketAddress(address, port);

    try {
      this.socketChannel = SocketChannel.open();
      this.socketChannel.configureBlocking(false);
    } catch (IOException e) {
      logger.info(() -> "Cannot configure client connection.", e);
      throw new ClientConnectionException(e);
    }
  }

  public boolean connect() {
    try {
      socketChannel.connect(socketAddress);
      return true;
    } catch (IOException e) {
      logger.warn(() -> "Cannot connect to the server.");
      return false;
    }
  }

  public boolean isConnected() {
    return socketChannel.isConnected();
  }

  public Response read() throws ClientConnectionException, DeserializationException {
    try {
      ObjectInputStream objectInputStream =
          new ObjectInputStream(socketChannel.socket().getInputStream());
      String string = objectInputStream.readUTF();
      return serializer.deserialize(string, Response.class);
    } catch (IOException e) {
      logger.error(() -> "Cannot read response.", e);
      throw new ClientConnectionException(e);
    }
  }

  public void write(Request request) throws ClientConnectionException {
    try {
      ObjectOutputStream objectOutputStream =
          new ObjectOutputStream(socketChannel.socket().getOutputStream());
      objectOutputStream.writeUTF(serializer.serialize(request));
    } catch (IOException e) {
      logger.error(() -> "Cannot write request.", e);
      throw new ClientConnectionException(e);
    }
  }
}
