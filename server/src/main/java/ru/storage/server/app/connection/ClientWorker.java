package ru.storage.server.app.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.serizliser.Serializer;
import ru.storage.common.serizliser.exceptions.DeserializationException;
import ru.storage.common.transfer.request.Request;
import ru.storage.common.transfer.response.Response;
import ru.storage.server.app.connection.exceptions.ServerException;
import ru.storage.server.app.connection.selector.exceptions.SelectorException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public final class ClientWorker {
  private final Logger logger;
  private final ByteBuffer byteBuffer;
  private final Serializer serializer;
  private final SocketChannel client;

  public ClientWorker(int bufferSize, Serializer serializer, SocketChannel client)
      throws ServerException {
    this.logger = LogManager.getLogger(ClientWorker.class);
    this.byteBuffer = ByteBuffer.allocate(bufferSize);
    this.serializer = serializer;
    this.client = client;

    try {
      this.client.configureBlocking(false);
    } catch (IOException e) {
      logger.info(() -> "Cannot configure client.", e);
      throw new ServerException(e);
    }
  }

  public Request read() throws SelectorException {
    try {
      byteBuffer.clear();
      client.read(byteBuffer);
      return serializer.deserialize(byteBuffer.array(), Request.class);
    } catch (IOException | DeserializationException e) {
      logger.error(() -> "Cannot read request.", e);
      throw new SelectorException(e);
    }
  }

  public void write(Response response) throws SelectorException {
    try {
      byteBuffer.clear();
      byteBuffer.put(serializer.serialize(response));
      client.write(byteBuffer);
    } catch (IOException e) {
      logger.error(() -> "Cannot write response.", e);
      throw new SelectorException(e);
    }
  }
}
