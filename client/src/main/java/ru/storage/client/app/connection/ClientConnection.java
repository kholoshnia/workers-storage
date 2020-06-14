package ru.storage.client.app.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.app.connection.exceptions.ClientException;
import ru.storage.common.transfer.request.Request;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.serizliser.Serializer;
import ru.storage.common.transfer.serizliser.exceptions.DeserializationException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.SocketChannel;

public class ClientConnection {
  private final Logger logger;
  private final Serializer serializer;
  private final SocketChannel server;

  public ClientConnection(Serializer serializer, SocketChannel server) throws ClientException {
    this.logger = LogManager.getLogger(ClientConnection.class);
    this.serializer = serializer;
    this.server = server;

    try {
      this.server.configureBlocking(false);
    } catch (IOException e) {
      logger.info(() -> "Cannot configure server.", e);
      throw new ClientException(e);
    }
  }

  public Response read() throws ClientException {
    try {
      ObjectInputStream objectInputStream = new ObjectInputStream(server.socket().getInputStream());
      String string = objectInputStream.readUTF();
      return serializer.deserialize(string, Response.class);
    } catch (IOException | DeserializationException e) {
      logger.error(() -> "Cannot read response.", e);
      throw new ClientException(e);
    }
  }

  public void write(Request request) throws ClientException {
    try {
      ObjectOutputStream objectOutputStream =
          new ObjectOutputStream(server.socket().getOutputStream());
      objectOutputStream.writeUTF(serializer.serialize(request));
    } catch (IOException e) {
      logger.error(() -> "Cannot write request.", e);
      throw new ClientException(e);
    }
  }
}
