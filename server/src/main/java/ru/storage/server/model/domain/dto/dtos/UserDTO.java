package ru.storage.server.model.domain.dto.dtos;

import ru.storage.server.model.domain.entity.exceptions.ValidationException;
import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.dto.DTO;
import ru.storage.server.model.domain.entity.entities.user.Role;

public final class UserDTO implements DTO<User> {
  public final long id;
  public final String name;
  public final String login;
  public final String password;
  public final Role role;
  public final boolean loggedIn;

  public UserDTO(long id, String name, String login, String password, Role role, boolean loggedIn) {
    this.id = id;
    this.name = name;
    this.login = login;
    this.password = password;
    this.role = role;
    this.loggedIn = loggedIn;
  }

  @Override
  public User toEntity() throws ValidationException {
    return new User(this.id, this.name, this.login, this.password, this.role, this.loggedIn);
  }

  @Override
  public String toString() {
    return "UserDTO{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", login='"
        + login
        + '\''
        + ", password='"
        + password
        + '\''
        + ", role="
        + role
        + ", loggedIn="
        + loggedIn
        + '}';
  }
}
