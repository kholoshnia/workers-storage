package ru.storage.server.app.connection;

public interface ConnectionProcessor {
  void process(ServerConnection client);
}
