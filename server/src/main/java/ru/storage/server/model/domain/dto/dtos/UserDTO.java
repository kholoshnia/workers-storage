package ru.storage.server.model.domain.dto.dtos;

import ru.storage.common.dto.DTO;
import ru.storage.common.dto.exceptions.ValidationException;
import ru.storage.server.model.domain.dto.IDDTO;
import ru.storage.server.model.domain.entity.ID;
import ru.storage.server.model.domain.entity.entities.user.Role;
import ru.storage.server.model.domain.entity.entities.user.User;

public final class UserDTO extends IDDTO implements DTO<User> {
  public String name;

  public String login;

  public String password;

  public Role role;

  public boolean loggedIn;

  public UserDTO(long id, String name, String login, String password, Role role, boolean loggedIn) {
    super(id);
    this.name = name;
    this.login = login;
    this.password = password;
    this.role = role;
    this.loggedIn = loggedIn;
  }

  public UserDTO(String nameString, String loginString, String passwordString) {
    super(ID.DEFAULT);
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
