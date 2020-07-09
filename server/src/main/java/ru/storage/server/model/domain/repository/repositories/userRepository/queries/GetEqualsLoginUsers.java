package ru.storage.server.model.domain.repository.repositories.userRepository.queries;

import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.repository.Query;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/** Returns users collection copy. The copy contains a users with a login equal to the specified. */
public final class GetEqualsLoginUsers implements Query<User> {
  private final String login;

  /**
   * Creates a query to get users with the specified login.
   *
   * @param login concrete login
   */
  public GetEqualsLoginUsers(String login) {
    this.login = login;
  }

  @Override
  public List<User> execute(List<User> users) {
    return users.stream()
        .filter(user -> user.getLogin().equals(login))
        .collect(Collectors.toCollection(CopyOnWriteArrayList::new));
  }
}
