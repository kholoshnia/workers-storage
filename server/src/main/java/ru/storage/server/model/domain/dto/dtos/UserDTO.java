package ru.storage.server.model.domain.dto.dtos;

import ru.storage.server.model.domain.dto.DTO;
import ru.storage.server.model.domain.entity.entities.user.Role;
import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.entity.exceptions.ValidationException;

public final class UserDTO implements DTO<User> {
  public static final String TABLE_NAME = "users";

  public static final String ID_COLUMN = "id";
  public static final String NAME_COLUMN = "name";
  public static final String LOGIN_COLUMN = "login";
  public static final String PASSWORD_COLUMN = "password";
  public static final String ROLE_COLUMN = "role";

  public final long id;
  public final String name;
  public final String login;
  public final String password;
  public final Role role;

  public UserDTO(long id, String name, String login, String password, Role role) {
    this.id = id;
    this.name = name;
    this.login = login;
    this.password = password;
    this.role = role;
  }

  @Override
  public User toEntity() throws ValidationException {
    return new User(id, name, login, password, role);
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
        + '}';
  }
}
