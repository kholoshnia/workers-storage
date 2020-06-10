package ru.storage.server.model.domain.dto;

public abstract class IDDTO {
  public final long id;

  protected IDDTO(long id) {
    this.id = id;
  }
}
