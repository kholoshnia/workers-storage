package ru.storage.server.model.domain.entity.entities.user;

public enum Role {
  ADMIN,
  USER,
  UNAUTHORIZED;

  public static Role getRole(String roleString) {
    if (roleString == null) {
      return null;
    }

    Role[] roles = values();

    for (Role role : roles) {
      if (role.toString().equals(roleString)) {
        return role;
      }
    }

    return null;
  }
}
