package ru.storage.server.model.domain.dto.dtos;

import ru.storage.common.api.dto.DTO;
import ru.storage.common.api.dto.exceptions.ValidationException;
import ru.storage.server.model.domain.dto.IDDTO;
import ru.storage.server.model.domain.dto.Parser;
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
    this.name = Parser.parseString(nameString);
    this.login = Parser.parseString(loginString);
    this.password = Parser.parseString(passwordString);
    this.role = Role.UNAUTHORIZED;
    this.loggedIn = false;
  }

  @Override
  public User toEntity() throws ValidationException {
    return new User(this.id, this.name, this.login, this.password, this.role, this.loggedIn);
  }
}
