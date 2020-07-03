package ru.storage.server.model.domain.dto.dtos;

import ru.storage.server.model.domain.entity.exceptions.ValidationException;
import ru.storage.server.model.domain.entity.entities.worker.Coordinates;
import ru.storage.server.model.domain.dto.DTO;

public final class CoordinatesDTO implements DTO<Coordinates> {
  public final long id;
  public final long ownerID;
  public final Double x;
  public final Double y;
  public final Double z;

  public CoordinatesDTO(long id, long ownerID, Double x, Double y, Double z) {
    this.id = id;
    this.ownerID = ownerID;
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override
  public Coordinates toEntity() throws ValidationException {
    return new Coordinates(this.id, this.ownerID, this.x, this.y, this.z);
  }

  @Override
  public String toString() {
    return "CoordinatesDTO{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
  }
}
