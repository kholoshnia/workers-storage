package ru.storage.server.model.domain.repository;

import ru.storage.server.model.domain.repository.exceptions.RepositoryException;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Repository provides methods to encapsulate logic of storage of the concrete {@link Entity}.
 *
 * @param <Entity> entity to store.
 */
public interface Repository<Entity> {
  /**
   * Returns entities in accordance with the specified {@link Query}.
   *
   * @param query concrete query
   * @return entities in accordance with the specified {@link Query}
   * @throws RepositoryException - in case of getting errors
   */
  List<Entity> get(@Nonnull Query<Entity> query) throws RepositoryException;

  /**
   * Inserts entity to the collection.
   *
   * @param entity entity to insert to the collection
   * @throws RepositoryException - in case of inserting errors
   */
  void insert(@Nonnull Entity entity) throws RepositoryException;

  /**
   * Updates entity of the collection.
   *
   * @param entity new entity
   * @throws RepositoryException - in case of updating errors
   */
  void update(@Nonnull Entity entity) throws RepositoryException;

  /**
   * Deletes entity from the collection.
   *
   * @param entity entity to delete
   * @throws RepositoryException - in case of deleting errors
   */
  void delete(@Nonnull Entity entity) throws RepositoryException;
}
