package ru.storage.server.model.domain.entity;

import ru.storage.server.model.domain.dto.DTO;

/**
 * DTO entities with the ability to be converted to the entity type must implement Entity interface.
 */
public interface Entity {
  /**
   * Returns DTO from current object.
   *
   * @return DTO from current object
   */
  DTO<? extends Entity> toDTO();
}
