package ru.storage.server.app.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.transfer.request.Request;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.serizliser.Serializer;
import ru.storage.common.transfer.serizliser.exceptions.DeserializationException;
import ru.storage.server.app.connection.exceptions.ServerException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.SocketChannel;

public class ServerConnection {
  private final Logger logger;
  private final Serializer serializer;
  private final SocketChannel client;

  public ServerConnection(Serializer serializer, SocketChannel client) throws ServerException {
    this.logger = LogManager.getLogger(ServerConnection.class);
    this.serializer = serializer;
    this.client = client;

    try {
      this.client.configureBlocking(false);
    } catch (IOException e) {
      logger.info(() -> "Cannot configure client.", e);
      throw new ServerException(e);
    }
  }

  public Request read() throws ServerException {
    try {
      ObjectInputStream objectInputStream = new ObjectInputStream(client.socket().getInputStream());
      String string = objectInputStream.readUTF();
      return serializer.deserialize(string, Request.class);
    } catch (IOException | DeserializationException e) {
      logger.error(() -> "Cannot read request.", e);
      throw new ServerException(e);
    }
  }

  public void write(Response response) throws ServerException {
    try {
      ObjectOutputStream objectOutputStream =
          new ObjectOutputStream(client.socket().getOutputStream());
      objectOutputStream.writeUTF(serializer.serialize(response));
    } catch (IOException e) {
      logger.error(() -> "Cannot write response.", e);
      throw new ServerException(e);
    }
  }
}
