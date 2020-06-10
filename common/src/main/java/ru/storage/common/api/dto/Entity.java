package ru.storage.common.api.dto;

/** DTO entities with ability to be converted to the entity type must implement Entity interface. */
public interface Entity {
  /**
   * Returns DTO from current object.
   *
   * @return DTO from current object
   */
  DTO<? extends Entity> toDTO();
}
