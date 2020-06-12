package ru.storage.common.dto;

import ru.storage.common.dto.exceptions.ValidationException;

/**
 * Entities with ability to be converted to the DTO type must implement DTO interface.
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
