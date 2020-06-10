package ru.storage.server.model.domain.dto.dtos;

import ru.storage.common.api.dto.DTO;
import ru.storage.common.api.dto.exceptions.ValidationException;
import ru.storage.server.model.domain.dto.OwnableDTO;
import ru.storage.server.model.domain.dto.Parser;
import ru.storage.server.model.domain.entity.ID;
import ru.storage.server.model.domain.entity.entities.worker.Coordinates;

public final class CoordinatesDTO extends OwnableDTO implements DTO<Coordinates> {
  public final Double x;

  public final Double y;

  public final Double z;

  public CoordinatesDTO(long id, long ownerID, Double x, Double y, Double z) {
    super(id, ownerID);
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public CoordinatesDTO(String xString, String yString, String zString) throws ValidationException {
    super(ID.DEFAULT, ID.DEFAULT);
    this.x = Parser.parseDouble(xString);
    this.y = Parser.parseDouble(yString);
    this.z = Parser.parseDouble(zString);
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
