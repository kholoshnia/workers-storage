package ru.storage.server.app.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.transfer.request.Request;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.serizliser.Serializer;
import ru.storage.common.transfer.serizliser.exceptions.DeserializationException;
import ru.storage.server.app.connection.selector.exceptions.ConnectionException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.SocketChannel;

public final class Connection {
  private final Logger logger;
  private final Serializer serializer;
  private final SocketChannel client;

  public Connection(Serializer serializer, SocketChannel client) throws ConnectionException {
    this.logger = LogManager.getLogger(Connection.class);
    this.serializer = serializer;
    this.client = client;

    try {
      this.client.configureBlocking(false);
    } catch (IOException e) {
      logger.info(() -> "Cannot configure client.", e);
      throw new ConnectionException(e);
    }
  }

  public Request read() throws ConnectionException {
    try {
      ObjectInputStream objectInputStream = new ObjectInputStream(client.socket().getInputStream());
      String string = objectInputStream.readUTF();
      return serializer.deserialize(string, Request.class);
    } catch (IOException | DeserializationException e) {
      logger.error(() -> "Cannot read request.", e);
      throw new ConnectionException(e);
    }
  }

  public void write(Response response) throws ConnectionException {
    try {
      ObjectOutputStream objectOutputStream =
          new ObjectOutputStream(client.socket().getOutputStream());
      objectOutputStream.writeUTF(serializer.serialize(response));
    } catch (IOException e) {
      logger.error(() -> "Cannot write response.", e);
      throw new ConnectionException(e);
    }
  }
}
