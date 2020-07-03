package ru.storage.server.model.domain.entity.entities.user;

import ru.storage.server.model.domain.entity.exceptions.ValidationException;
import ru.storage.server.model.domain.entity.Entity;
import ru.storage.server.model.domain.dto.DTO;
import ru.storage.server.model.domain.dto.dtos.UserDTO;

import java.util.Objects;
import java.util.ResourceBundle;

public final class User implements Entity {
  public static final long DEFAULT_ID = 0L;

  public static final String ID_COLUMN = "id";
  public static final String TABLE_NAME = "users";
  public static final String NAME_COLUMN = "name";
  public static final String LOGIN_COLUMN = "login";
  public static final String PASSWORD_COLUMN = "password";
  public static final String ROLE_COLUMN = "role";
  public static final String STATE_COLUMN = "state";

  private static final String WRONG_ID_EXCEPTION_MESSAGE;
  private static final String WRONG_NAME_EXCEPTION_MESSAGE;
  private static final String WRONG_LOGIN_EXCEPTION_MESSAGE;
  private static final String WRONG_PASSWORD_EXCEPTION_MESSAGE;
  private static final String WRONG_ROLE_EXCEPTION_MESSAGE;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.User");

    WRONG_ID_EXCEPTION_MESSAGE = resourceBundle.getString("exceptionMessages.wrongId");
    WRONG_NAME_EXCEPTION_MESSAGE = resourceBundle.getString("exceptionMessages.wrongName");
    WRONG_LOGIN_EXCEPTION_MESSAGE = resourceBundle.getString("exceptionMessages.wrongLogin");
    WRONG_PASSWORD_EXCEPTION_MESSAGE = resourceBundle.getString("exceptionMessages.wrongPassword");
    WRONG_ROLE_EXCEPTION_MESSAGE = resourceBundle.getString("exceptionMessages.wrongRole");
  }

  private long id;
  private String name;
  private String login;
  private String password;
  private Role role;
  private boolean state;

  public User(long id, String name, String login, String password, Role role, boolean state)
      throws ValidationException {
    checkId(id);
    this.id = id;

    checkName(name);
    this.name = name;

    checkLogin(name);
    this.login = login;

    checkPassword(name);
    this.password = password;

    checkRole(role);
    this.role = role;

    this.state = state;
  }

  @Override
  public DTO<User> toDTO() {
    return new UserDTO(this.id, this.name, this.login, this.password, this.role, this.state);
  }

  public final long getID() {
    return id;
  }

  public final void setID(long id) throws ValidationException {
    checkId(id);
    this.id = id;
  }

  private void checkId(long id) throws ValidationException {
    if (id > 0 || id == DEFAULT_ID) {
      return;
    }

    throw new ValidationException(WRONG_ID_EXCEPTION_MESSAGE);
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

    throw new ValidationException(WRONG_NAME_EXCEPTION_MESSAGE);
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

    throw new ValidationException(WRONG_LOGIN_EXCEPTION_MESSAGE);
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

    throw new ValidationException(WRONG_PASSWORD_EXCEPTION_MESSAGE);
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

    throw new ValidationException(WRONG_ROLE_EXCEPTION_MESSAGE);
  }

  public boolean getState() {
    return state;
  }

  public void setState(boolean state) {
    this.state = state;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return state == user.state
        && Objects.equals(name, user.name)
        && Objects.equals(login, user.login)
        && Objects.equals(password, user.password)
        && role == user.role;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, login, password, role, state);
  }

  @Override
  public String toString() {
    return "User{"
        + "name='"
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
        + state
        + '}';
  }
}
