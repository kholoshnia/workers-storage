package ru.storage.server.model.domain.dto.dtos;

import ru.storage.server.model.domain.dto.DTO;
import ru.storage.server.model.domain.dto.exceptions.ValidationException;
import ru.storage.server.model.domain.entity.entities.user.Role;
import ru.storage.server.model.domain.entity.entities.user.User;

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

  public UserDTO(String nameString, String loginString, String passwordString) {
    this.id = User.DEFAULT_ID;
    this.name = parseString(nameString);
    this.login = parseString(loginString);
    this.password = parseString(passwordString);
    this.role = Role.UNAUTHORIZED;
    this.loggedIn = false;
  }

  private String parseString(String value) {
    if (value == null || value.trim().isEmpty()) {
      return null;
    }

    return value;
  }

  @Override
  public User toEntity() throws ValidationException {
    return new User(this.id, this.name, this.login, this.password, this.role, this.loggedIn);
  }
}
