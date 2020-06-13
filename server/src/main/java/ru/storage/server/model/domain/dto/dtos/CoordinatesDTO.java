package ru.storage.server.model.domain.dto.dtos;

import ru.storage.server.model.domain.dto.DTO;
import ru.storage.server.model.domain.dto.exceptions.ValidationException;
import ru.storage.server.model.domain.entity.entities.worker.Coordinates;

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

  public CoordinatesDTO(String xString, String yString, String zString) throws ValidationException {
    this.id = Coordinates.DEFAULT_ID;
    this.ownerID = Coordinates.DEFAULT_OWNER_ID;
    this.x = parseDouble(xString);
    this.y = parseDouble(yString);
    this.z = parseDouble(zString);
  }

  private Double parseDouble(String value) throws ValidationException {
    Double result;

    try {
      result = Double.parseDouble(value);
    } catch (NumberFormatException | NullPointerException e) {
      throw new ValidationException(e);
    }

    return result;
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
