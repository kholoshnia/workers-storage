package ru.storage.server.app.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.serizliser.Serializer;
import ru.storage.common.serizliser.exceptions.DeserializationException;
import ru.storage.common.transfer.Request;
import ru.storage.common.transfer.response.Response;
import ru.storage.server.app.connection.exceptions.ServerException;
import ru.storage.server.app.connection.selector.exceptions.SelectorException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;

public final class ClientWorker {
  private final Logger logger;
  private final int bufferSize;
  private final Serializer serializer;
  private final SocketChannel client;

  private ByteBuffer buffer;

  public ClientWorker(int bufferSize, Serializer serializer, SocketChannel client)
      throws ServerException {
    logger = LogManager.getLogger(ClientWorker.class);
    this.bufferSize = bufferSize;
    buffer = ByteBuffer.allocate(bufferSize);
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
      int size = client.read(buffer);

      if (size == -1) {
        buffer = ByteBuffer.allocate(bufferSize);
        client.close();
        return null;
      }

      byte[] bytes = new byte[size];
      buffer.rewind();
      buffer.get(bytes, 0, size);
      buffer.clear();

      return serializer.deserialize(bytes, Request.class);
    } catch (ClosedChannelException e) {
      logger.info(() -> "Client closed connection.");
      return null;
    } catch (IOException | DeserializationException e) {
      try {
        buffer = ByteBuffer.allocate(bufferSize);
        client.close();
      } catch (IOException ex) {
        logger.error(() -> "Cannot close connection.", e);
        throw new SelectorException(e);
      }

      logger.error(() -> "Cannot read request.", e);
      throw new SelectorException(e);
    }
  }

  public void write(Response response) throws SelectorException {
    try {
      byte[] bytes = serializer.serialize(response);
      client.write(ByteBuffer.wrap(bytes));
    } catch (ClosedChannelException e) {
      logger.info(() -> "Client closed connection.");
    } catch (IOException e) {
      try {
        client.close();
      } catch (IOException ex) {
        logger.error(() -> "Cannot close connection.", e);
        throw new SelectorException(e);
      }

      logger.error(() -> "Cannot write response.", e);
      throw new SelectorException(e);
    }
  }
}
