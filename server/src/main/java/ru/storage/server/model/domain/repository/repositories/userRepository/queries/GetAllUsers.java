package ru.storage.server.model.domain.repository.repositories.userRepository.queries;

import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.repository.Query;

import java.util.ArrayList;
import java.util.List;

/** Returns users collection copy. */
public final class GetAllUsers implements Query<User> {
  @Override
  public List<User> execute(List<User> users) {
    return new ArrayList<>(users);
  }
}
