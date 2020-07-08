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
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public final class ClientWorker {
  private final Logger logger;
  private final int bufferSize;
  private final Serializer serializer;
  private final SocketChannel client;
  private ByteBuffer buffer;

  public ClientWorker(int bufferSize, Serializer serializer, SocketChannel client)
      throws ServerException {
    this.logger = LogManager.getLogger(ClientWorker.class);
    this.buffer = ByteBuffer.allocate(bufferSize);
    this.bufferSize = bufferSize;
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
    int size;
    try {
      InputStream inputStream = client.socket().getInputStream();
      size = inputStream.read(buffer.array());
      // socket.setSoTimeout(5000);
    } catch (IOException e) {
      try {
        client.close();
        buffer = ByteBuffer.allocate(bufferSize);
      } catch (IOException ex) {
        ex.printStackTrace();
        System.exit(1);
      }
      throw new SelectorException(e);
    }

    if (size == -1) {
      try {
        client.close();
        buffer = ByteBuffer.allocate(bufferSize);
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
      }
      throw new SelectorException("Reached an end of inputStream");
    }

    byte[] bytes = new byte[bufferSize];
    buffer.rewind();
    buffer.get(bytes, 0, size);
    buffer.clear();

    try {
      return serializer.deserialize(bytes, Request.class);
    } catch (DeserializationException e) {
      return null;
    }
  }

  //  public Request read() throws SelectorException {
  //    try {
  //      buffer.clear();
  //
  //      InputStream inputStream = client.socket().getInputStream();
  //      int size = inputStream.read(buffer.array());
  //
  //      // client.read(buffer);
  //      return serializer.deserialize(buffer.array(), Request.class);
  //    } catch (IOException | DeserializationException e) {
  //      logger.error(() -> "Cannot read request.", e);
  //      throw new SelectorException(e);
  //    }
  //  }

  public void write(Response response) throws SelectorException {
    try {
      buffer.clear();
      buffer.put(serializer.serialize(response));
      client.write(buffer);
    } catch (IOException e) {
      logger.error(() -> "Cannot write response.", e);
      throw new SelectorException(e);
    }
  }
}
