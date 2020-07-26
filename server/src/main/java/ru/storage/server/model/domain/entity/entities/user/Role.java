package ru.storage.server.model.domain.entity.entities.user;

import ru.storage.server.model.domain.entity.exceptions.ValidationException;

public enum Role {
  ADMIN,
  USER,
  UNAUTHORIZED;

  public static Role getRole(String roleString) throws ValidationException {
    if (roleString == null) {
      return null;
    }

    Role[] roles = values();

    for (Role role : roles) {
      if (role.name().equals(roleString)) {
        return role;
      }
    }

    throw new ValidationException();
  }
}
