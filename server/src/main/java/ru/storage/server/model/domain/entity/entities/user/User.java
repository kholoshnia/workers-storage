package ru.storage.server.model.domain.entity.entities.user;

import ru.storage.server.model.domain.dto.dtos.UserDTO;
import ru.storage.server.model.domain.entity.Entity;
import ru.storage.server.model.domain.entity.exceptions.ValidationException;

import java.util.Objects;
import java.util.ResourceBundle;

public final class User implements Entity {
  public static final long DEFAULT_ID = 0L;

  private static final String WRONG_ID_EXCEPTION;
  private static final String WRONG_NAME_EXCEPTION;
  private static final String WRONG_LOGIN_EXCEPTION;
  private static final String WRONG_PASSWORD_EXCEPTION;
  private static final String WRONG_ROLE_EXCEPTION;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.User");

    WRONG_ID_EXCEPTION = resourceBundle.getString("exceptions.wrongId");
    WRONG_NAME_EXCEPTION = resourceBundle.getString("exceptions.wrongName");
    WRONG_LOGIN_EXCEPTION = resourceBundle.getString("exceptions.wrongLogin");
    WRONG_PASSWORD_EXCEPTION = resourceBundle.getString("exceptions.wrongPassword");
    WRONG_ROLE_EXCEPTION = resourceBundle.getString("exceptions.wrongRole");
  }

  private long id;
  private String name;
  private String login;
  private String password;
  private Role role;

  public User(long id, String name, String login, String password, Role role)
      throws ValidationException {
    checkId(id);
    this.id = id;

    checkName(name);
    this.name = name;

    checkLogin(login);
    this.login = login;

    checkPassword(password);
    this.password = password;

    checkRole(role);
    this.role = role;
  }

  @Override
  public UserDTO toDTO() {
    return new UserDTO(id, name, login, password, role);
  }

  public final long getId() {
    return id;
  }

  public final void setId(long id) throws ValidationException {
    checkId(id);
    this.id = id;
  }

  private void checkId(long id) throws ValidationException {
    if (id > 0 || id == DEFAULT_ID) {
      return;
    }

    throw new ValidationException(WRONG_ID_EXCEPTION);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) throws ValidationException {
    checkName(name);
    this.name = name;
  }

  private void checkName(String name) throws ValidationException {
    if (name != null && name.length() >= 2 && name.length() <= 100) {
      return;
    }

    throw new ValidationException(WRONG_NAME_EXCEPTION);
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) throws ValidationException {
    checkLogin(login);
    this.login = login;
  }

  private void checkLogin(String login) throws ValidationException {
    if (login != null && login.length() >= 2 && login.length() <= 100) {
      return;
    }

    throw new ValidationException(WRONG_LOGIN_EXCEPTION);
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) throws ValidationException {
    checkPassword(password);
    this.password = password;
  }

  private void checkPassword(String password) throws ValidationException {
    if (password != null && password.length() >= 8 && password.length() <= 100) {
      return;
    }

    throw new ValidationException(WRONG_PASSWORD_EXCEPTION);
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) throws ValidationException {
    checkRole(role);
    this.role = role;
  }

  private void checkRole(Role role) throws ValidationException {
    if (role != null) {
      return;
    }

    throw new ValidationException(WRONG_ROLE_EXCEPTION);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return id == user.id
        && Objects.equals(name, user.name)
        && Objects.equals(login, user.login)
        && Objects.equals(password, user.password)
        && role == user.role;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, login, password, role);
  }

  @Override
  public String toString() {
    return "User{"
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
