package ru.storage.server.model.domain.repository.repositories.userRepository.exceptions;

import ru.storage.server.model.domain.repository.exceptions.RepositoryException;

public final class UserRepositoryException extends RepositoryException {
  public UserRepositoryException() {
    super();
  }

  public UserRepositoryException(String message) {
    super(message);
  }

  public UserRepositoryException(String message, Throwable cause) {
    super(message, cause);
  }

  public UserRepositoryException(Throwable cause) {
    super(cause);
  }
}
