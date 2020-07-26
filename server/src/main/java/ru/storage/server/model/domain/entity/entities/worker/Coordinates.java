package ru.storage.server.model.domain.entity.entities.worker;

import ru.storage.server.model.domain.dto.dtos.CoordinatesDTO;
import ru.storage.server.model.domain.entity.Entity;
import ru.storage.server.model.domain.entity.exceptions.ValidationException;

import java.util.Objects;
import java.util.ResourceBundle;

public final class Coordinates implements Cloneable, Entity {
  public static final long DEFAULT_ID = 0L;
  public static final long DEFAULT_OWNER_ID = 0L;

  private static final String WRONG_ID_EXCEPTION;
  private static final String WRONG_OWNER_ID_EXCEPTION;
  private static final String WRONG_X_EXCEPTION;
  private static final String WRONG_Y_EXCEPTION;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.Coordinates");

    WRONG_ID_EXCEPTION = resourceBundle.getString("exceptions.wrongId");
    WRONG_OWNER_ID_EXCEPTION = resourceBundle.getString("exceptions.wrongOwnerId");
    WRONG_X_EXCEPTION = resourceBundle.getString("exceptions.wrongX");
    WRONG_Y_EXCEPTION = resourceBundle.getString("exceptions.wrongY");
  }

  private long id;
  private long ownerId;
  private Double x;
  private Double y;
  private Double z;

  public Coordinates(long id, long ownerId, Double x, Double y, Double z)
      throws ValidationException {
    checkId(id);
    this.id = id;

    checkOwnerId(ownerId);
    this.ownerId = ownerId;

    checkX(x);
    this.x = x;

    checkY(y);
    this.y = y;

    this.z = z;
  }

  @Override
  public CoordinatesDTO toDTO() {
    return new CoordinatesDTO(id, ownerId, x, y, z);
  }

  public final long getId() {
    return id;
  }

  public final void setId(long id) throws ValidationException {
    checkId(id);
    this.id = id;
  }

  private void checkId(long id) throws ValidationException {
    if (id > 0 || id == DEFAULT_ID) {
      return;
    }

    throw new ValidationException(WRONG_ID_EXCEPTION);
  }

  public final long getOwnerId() {
    return ownerId;
  }

  public final void setOwnerId(long ownerId) throws ValidationException {
    checkOwnerId(ownerId);
    this.ownerId = ownerId;
  }

  private void checkOwnerId(long ownerId) throws ValidationException {
    if (ownerId > 0 || ownerId == DEFAULT_OWNER_ID) {
      return;
    }

    throw new ValidationException(WRONG_OWNER_ID_EXCEPTION);
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

    throw new ValidationException(WRONG_X_EXCEPTION);
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

    throw new ValidationException(WRONG_Y_EXCEPTION);
  }

  public Double getZ() {
    return z;
  }

  public void setZ(Double z) {
    this.z = z;
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
      return new Coordinates(id, ownerId, x, y, z);
    } catch (ValidationException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString() {
    return "CoordinatesDAO{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
  }
}
