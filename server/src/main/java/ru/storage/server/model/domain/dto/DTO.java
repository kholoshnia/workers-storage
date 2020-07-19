package ru.storage.server.model.domain.dto;

import ru.storage.server.model.domain.entity.Entity;
import ru.storage.server.model.domain.entity.exceptions.ValidationException;

/**
 * Entities with the ability to be converted to the DTO type must implement DTO interface. DTO is
 * used to transfer data between software application subsystems or layers.
 *
 * @param <T> Entity
 */
public interface DTO<T extends Entity> {
  /**
   * Returns entity from current DTO object.
   *
   * @return entity from current DTO object
   * @throws ValidationException - if DTO data is incorrect
   */
  T toEntity() throws ValidationException;
}
