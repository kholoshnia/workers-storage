package ru.storage.server.model.domain.repository.repositories.workerRepository.exceptions;

import ru.storage.server.model.domain.repository.exceptions.RepositoryException;

public final class WorkerRepositoryException extends RepositoryException {
  public WorkerRepositoryException() {
    super();
  }

  public WorkerRepositoryException(String message) {
    super(message);
  }

  public WorkerRepositoryException(String message, Throwable cause) {
    super(message, cause);
  }

  public WorkerRepositoryException(Throwable cause) {
    super(cause);
  }
}
