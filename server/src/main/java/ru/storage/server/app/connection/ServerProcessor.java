package ru.storage.server.app.connection;

public interface ServerProcessor {
  void process(ClientWorker clientWorker);
}
