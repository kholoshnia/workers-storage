package ru.storage.server.model.dao.adapter.adapters;

import ru.storage.server.model.dao.adapter.Adapter;
import ru.storage.server.model.dao.adapter.exceptions.AdapterException;
import ru.storage.server.model.domain.entity.entities.user.Role;
import ru.storage.server.model.domain.entity.exceptions.ValidationException;

public final class RoleAdapter implements Adapter<Role, String> {
  @Override
  public String to(Role role) {
    if (role == null) {
      return null;
    }

    return role.name();
  }

  @Override
  public Role from(String string) throws AdapterException {
    if (string == null) {
      return null;
    }

    Role role;

    try {
      role = Role.getRole(string);
    } catch (ValidationException e) {
      throw new AdapterException(e);
    }

    return role;
  }
}
