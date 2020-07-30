package ru.storage.client.app.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.app.connection.exceptions.ClientConnectionException;
import ru.storage.common.chunker.ByteChunker;
import ru.storage.common.exitManager.ExitListener;
import ru.storage.common.exitManager.exceptions.ExitingException;
import ru.storage.common.serizliser.Serializer;
import ru.storage.common.serizliser.exceptions.DeserializationException;
import ru.storage.common.transfer.Request;
import ru.storage.common.transfer.response.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ServerWorker implements ExitListener {
  private static final Logger logger = LogManager.getLogger(ServerWorker.class);

  private final int bufferSize;
  private final ByteChunker chunker;
  private final Serializer serializer;
  private final InetSocketAddress socketAddress;

  private Socket server;
  private ByteBuffer buffer;

  public ServerWorker(
      InetAddress address, int port, int bufferSize, ByteChunker chunker, Serializer serializer) {
    this.bufferSize = bufferSize;
    buffer = ByteBuffer.allocate(bufferSize);
    this.chunker = chunker;
    this.serializer = serializer;
    socketAddress = new InetSocketAddress(address, port);
    server = new Socket();
  }

  /**
   * Connects to the server.
   *
   * @throws ClientConnectionException - if cannot connect to the server
   */
  public void connect() throws ClientConnectionException {
    try {
      server.connect(socketAddress);
      server.setSoTimeout(5000);
    } catch (IOException e) {
      server = new Socket();
      try {
        server.connect(socketAddress);
      } catch (IOException ex) {
        logger.warn(() -> "Cannot connect to the server.", e);
        throw new ClientConnectionException(e);
      }
    }
  }

  /**
   * Reads one chunk.
   *
   * @param inputStream server input stream
   * @return chunk
   * @throws IOException - in case of connection exceptions
   */
  private byte[] readChunk(InputStream inputStream) throws IOException {
    int size = inputStream.read(buffer.array());

    if (size == -1) {
      buffer = ByteBuffer.allocate(bufferSize);
      server.close();
      return null;
    }

    byte[] chunk = new byte[size];

    buffer.rewind();
    buffer.get(chunk, 0, size);
    buffer.clear();

    return chunk;
  }

  /**
   * Reads {@link Response} from the server.
   *
   * @return response from the server
   * @throws ClientConnectionException - in case of connection exceptions
   * @throws DeserializationException - in case of deserialization exceptions
   */
  public Response read() throws ClientConnectionException, DeserializationException {
    try {
      InputStream inputStream = server.getInputStream();
      List<byte[]> chunks = new ArrayList<>();
      byte[] chunk = readChunk(inputStream);

      while (!Arrays.equals(chunk, chunker.getStopWordChunk())) {
        chunks.add(chunk);
        chunk = readChunk(inputStream);
      }

      byte[] bytes = chunker.join(chunks);
      return serializer.deserialize(bytes, Response.class);
    } catch (IOException e) {
      try {
        buffer = ByteBuffer.allocate(bufferSize);
        server.close();
      } catch (IOException ex) {
        logger.error(() -> "Cannot close connection.", e);
        throw new ClientConnectionException(e);
      }

      logger.error(() -> "Cannot read response.", e);
      throw new ClientConnectionException(e);
    }
  }

  /**
   * Writes {@link Request} to the server.
   *
   * @param request request to the server
   * @throws ClientConnectionException - in case of connection exceptions
   */
  public void write(Request request) throws ClientConnectionException {
    try {
      OutputStream outputStream = server.getOutputStream();
      byte[] bytes = serializer.serialize(request);
      List<byte[]> chunks = chunker.split(bytes);

      for (byte[] chunk : chunks) {
        outputStream.write(chunk);
      }
    } catch (IOException e) {
      try {
        server.close();
      } catch (IOException ex) {
        logger.error(() -> "Cannot close connection.", e);
        throw new ClientConnectionException(e);
      }

      logger.error(() -> "Cannot write request.", e);
      throw new ClientConnectionException(e);
    }
  }

  @Override
  public void exit() throws ExitingException {
    try {
      server.close();
    } catch (IOException e) {
      logger.error(() -> "Cannot close connection.", e);
      throw new ExitingException(e);
    }
  }
}
