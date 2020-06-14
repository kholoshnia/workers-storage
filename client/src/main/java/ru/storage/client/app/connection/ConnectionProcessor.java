package ru.storage.client.app.connection;

import ru.storage.client.app.connection.exceptions.ClientException;

public interface ConnectionProcessor {
  void process(ClientConnection server) throws ClientException;
}
