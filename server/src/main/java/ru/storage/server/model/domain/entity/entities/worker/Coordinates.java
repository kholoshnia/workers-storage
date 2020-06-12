package ru.storage.server.model.domain.entity.entities.worker;

import ru.storage.common.dto.DTO;
import ru.storage.common.dto.Entity;
import ru.storage.common.dto.exceptions.ValidationException;
import ru.storage.server.model.domain.dto.dtos.CoordinatesDTO;
import ru.storage.server.model.domain.entity.Ownable;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public final class Coordinates extends Ownable implements Cloneable, Entity {
  public static final String TABLE_NAME = "coordinates";
  public static final String X_COLUMN = "x";
  public static final String Y_COLUMN = "y";
  public static final String Z_COLUMN = "z";

  private static final String WRONG_X_EXCEPTION_MESSAGE;
  private static final String WRONG_Y_EXCEPTION_MESSAGE;
  private static final String WRONG_Z_EXCEPTION_MESSAGE;

  static {
    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("internal.CoordinatesDAO", Locale.ENGLISH);

    WRONG_X_EXCEPTION_MESSAGE = resourceBundle.getString("exceptionMessages.wrongX");
    WRONG_Y_EXCEPTION_MESSAGE = resourceBundle.getString("exceptionMessages.wrongY");
    WRONG_Z_EXCEPTION_MESSAGE = resourceBundle.getString("exceptionMessages.wrongZ");
  }

  private Double x;
  private Double y;
  private Double z;

  public Coordinates(long id, long ownerID, Double x, Double y, Double z)
      throws ValidationException {
    super(id, ownerID);

    checkX(x);
    this.x = x;

    checkY(y);
    this.y = y;

    checkZ(z);
    this.z = z;
  }

  @Override
  public DTO<Coordinates> toDTO() {
    return new CoordinatesDTO(this.id, this.ownerID, this.x, this.y, this.z);
  }

  public Double getX() {
    return x;
  }

  public void setX(Double x) throws ValidationException {
    checkX(x);
    this.x = x;
  }

  private void checkX(Double x) throws ValidationException {
    if (x != null && x >= -500.0 && x <= 500.0) {
      return;
    }

    throw new ValidationException(WRONG_X_EXCEPTION_MESSAGE);
  }

  public Double getY() {
    return y;
  }

  public void setY(Double y) throws ValidationException {
    checkY(y);
    this.y = y;
  }

  private void checkY(Double y) throws ValidationException {
    if (y != null && y >= -500.0 && y <= 500.0) {
      return;
    }

    throw new ValidationException(WRONG_Y_EXCEPTION_MESSAGE);
  }

  public Double getZ() {
    return z;
  }

  public void setZ(Double z) throws ValidationException {
    checkZ(z);
    this.z = z;
  }

  private void checkZ(Double z) throws ValidationException {
    if (z != null && z >= -500.0 && z <= 500.0) {
      return;
    }

    throw new ValidationException(WRONG_Z_EXCEPTION_MESSAGE);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Coordinates that = (Coordinates) o;
    return Objects.equals(x, that.x) && Objects.equals(y, that.y) && Objects.equals(z, that.z);
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z);
  }

  @Override
  public Coordinates clone() {
    try {
      return new Coordinates(this.id, this.ownerID, this.x, this.y, this.z);
    } catch (ValidationException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString() {
    return "CoordinatesDAO{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
  }
}
