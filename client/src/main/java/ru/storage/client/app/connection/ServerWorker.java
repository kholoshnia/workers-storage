package ru.storage.client.app.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.app.connection.exceptions.ClientConnectionException;
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

public final class ServerWorker implements ExitListener {
  private final Logger logger;
  private final int bufferSize;
  private final Serializer serializer;
  private final InetSocketAddress socketAddress;

  private Socket server;
  private ByteBuffer buffer;

  public ServerWorker(InetAddress address, int port, int bufferSize, Serializer serializer) {
    logger = LogManager.getLogger(ServerWorker.class);
    this.bufferSize = bufferSize;
    buffer = ByteBuffer.allocate(bufferSize);
    this.serializer = serializer;
    socketAddress = new InetSocketAddress(address, port);
    server = new Socket();
  }

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

  public Response read() throws ClientConnectionException, DeserializationException {
    try {
      InputStream inputStream = server.getInputStream();
      int size = inputStream.read(buffer.array());

      if (size == -1) {
        buffer = ByteBuffer.allocate(bufferSize);
        server.close();
        return null;
      }

      byte[] bytes = new byte[size];
      buffer.rewind();
      buffer.get(bytes, 0, size);
      buffer.clear();

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

  public void write(Request request) throws ClientConnectionException {
    try {
      OutputStream outputStream = server.getOutputStream();
      byte[] bytes = serializer.serialize(request);
      outputStream.write(bytes);
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
