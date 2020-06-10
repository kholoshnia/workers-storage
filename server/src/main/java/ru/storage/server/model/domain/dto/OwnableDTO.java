package ru.storage.server.model.domain.dto;

public abstract class OwnableDTO extends IDDTO {
  public final long ownerID;

  protected OwnableDTO(long id, long ownerID) {
    super(id);
    this.ownerID = ownerID;
  }
}
