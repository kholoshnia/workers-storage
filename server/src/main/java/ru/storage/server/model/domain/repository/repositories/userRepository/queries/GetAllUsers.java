package ru.storage.server.model.domain.repository.repositories.userRepository.queries;

import ru.storage.server.model.domain.repository.Query;
import ru.storage.server.model.domain.entity.entities.user.User;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/** Returns users collection clone. */
public final class GetAllUsers implements Query<User> {
  @Override
  public List<User> execute(List<User> users) {
    return new CopyOnWriteArrayList<>(users);
  }
}
