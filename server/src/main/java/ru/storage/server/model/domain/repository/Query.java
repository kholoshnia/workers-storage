package ru.storage.server.model.domain.repository;

import ru.storage.server.model.domain.repository.exceptions.RepositoryException;

import java.util.List;

public interface Query<Entity> {
  /**
   * Executes specified query.
   *
   * @param entities all entities of the repository
   * @return entities in accordance with query
   * @throws RepositoryException - in case of repository exception
   */
  List<Entity> execute(List<Entity> entities) throws RepositoryException;
}
