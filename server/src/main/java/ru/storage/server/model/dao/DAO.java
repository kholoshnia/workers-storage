package ru.storage.server.model.dao;

import ru.storage.server.model.dao.exceptions.DAOException;
import ru.storage.server.model.source.exceptions.DataSourceException;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * DAO interface can be used to create data access objects.
 *
 * @param <Entity> Entity of DAO object.
 */
public interface DAO<Key, Entity> {
  /**
   * Returns all entities list.
   *
   * @return All entities from database
   * @throws DAOException - in case of incorrect statement execution
   * @throws DataSourceException - in case of getting all entities errors
   */
  List<Entity> getAll() throws DAOException, DataSourceException;

  /**
   * Returns entity by key.
   *
   * @param key concrete entity key
   * @return Entity with specified key
   * @throws DAOException - in case of incorrect statement execution
   * @throws DataSourceException - in case of getting by key errors
   */
  Entity getByKey(@Nonnull Key key) throws DAOException, DataSourceException;

  /**
   * Inserts entity. Returns entity with generated id.
   *
   * @param entity concrete entity
   * @return Entity with generated id
   * @throws DAOException - in case of incorrect statement execution
   * @throws DataSourceException - in case of inserting errors
   */
  Entity insert(@Nonnull Entity entity) throws DAOException, DataSourceException;

  /**
   * Updates entity in database.
   *
   * @param entity new entity
   * @return updated entity
   * @throws DAOException - in case of incorrect statement execution
   * @throws DataSourceException - in case of updating errors
   */
  Entity update(@Nonnull Entity entity) throws DAOException, DataSourceException;

  /**
   * Deletes entity from the database.
   *
   * @param entity concrete entity
   * @throws DAOException - in case of incorrect statement execution
   * @throws DataSourceException - in case of deleting errors
   */
  void delete(@Nonnull Entity entity) throws DAOException, DataSourceException;
}
