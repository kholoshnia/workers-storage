package ru.storage.server.app.connection;

import ru.storage.server.app.connection.exceptions.ServerException;

public interface ServerProcessor {
  void process(Connection client) throws ServerException;
}
