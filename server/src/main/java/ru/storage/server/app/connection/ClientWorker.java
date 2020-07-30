package ru.storage.server.app.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.chunker.ByteChunker;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ClientWorker {
  private static final Logger logger = LogManager.getLogger(ClientWorker.class);

  private final int bufferSize;
  private final ByteChunker chunker;
  private final Serializer serializer;
  private final SocketChannel client;

  private ByteBuffer buffer;

  public ClientWorker(
      int bufferSize, ByteChunker chunker, Serializer serializer, SocketChannel client)
      throws ServerException {
    this.bufferSize = bufferSize;
    buffer = ByteBuffer.allocate(bufferSize);
    this.chunker = chunker;
    this.serializer = serializer;
    this.client = client;

    try {
      this.client.configureBlocking(false);
    } catch (IOException e) {
      logger.info(() -> "Cannot configure client.", e);
      throw new ServerException(e);
    }
  }

  /**
   * Reads one chunk.
   *
   * @return chunk
   * @throws IOException - in case of connection exceptions
   */
  private byte[] readChunk() throws IOException {
    int size = client.read(buffer);

    if (size == -1) {
      buffer = ByteBuffer.allocate(bufferSize);
      client.close();
      return null;
    }

    byte[] chunk = new byte[size];

    buffer.rewind();
    buffer.get(chunk, 0, size);
    buffer.clear();

    return chunk;
  }

  /**
   * Reads {@link Request} from the client.
   *
   * @return request from the client
   * @throws SelectorException - in case of selector exceptions
   */
  public Request read() throws SelectorException {
    try {
      List<byte[]> chunks = new ArrayList<>();
      byte[] chunk = readChunk();

      while (!Arrays.equals(chunk, chunker.getStopWordChunk())) {
        chunks.add(chunk);
        chunk = readChunk();
      }

      byte[] bytes = chunker.join(chunks);
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

  /**
   * Writes {@link Response} to the client.
   *
   * @param response response to the client
   * @throws SelectorException - in case of selector exceptions
   */
  public void write(Response response) throws SelectorException {
    try {
      byte[] bytes = serializer.serialize(response);
      List<byte[]> chunks = chunker.split(bytes);

      for (byte[] chunk : chunks) {
        client.write(ByteBuffer.wrap(chunk));
      }
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
