package ru.storage.server.model.domain.entity.entities.worker;

import ru.storage.server.model.domain.dto.DTO;
import ru.storage.server.model.domain.dto.dtos.CoordinatesDTO;
import ru.storage.server.model.domain.dto.exceptions.ValidationException;
import ru.storage.server.model.domain.entity.Entity;

import java.util.Objects;
import java.util.ResourceBundle;

public final class Coordinates implements Cloneable, Entity {
  public static final long DEFAULT_ID = 0L;
  public static final long DEFAULT_OWNER_ID = 0L;

  public static final String ID_COLUMN = "id";
  public static final String OWNER_ID_COLUMN = "owner_id";
  public static final String TABLE_NAME = "coordinates";
  public static final String X_COLUMN = "x";
  public static final String Y_COLUMN = "y";
  public static final String Z_COLUMN = "z";

  private static final String WRONG_ID_EXCEPTION_MESSAGE;
  private static final String WRONG_OWNER_ID_EXCEPTION_MESSAGE;
  private static final String WRONG_X_EXCEPTION_MESSAGE;
  private static final String WRONG_Y_EXCEPTION_MESSAGE;
  private static final String WRONG_Z_EXCEPTION_MESSAGE;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.CoordinatesDAO");

    WRONG_ID_EXCEPTION_MESSAGE = resourceBundle.getString("exceptionMessages.wrongId");
    WRONG_OWNER_ID_EXCEPTION_MESSAGE = resourceBundle.getString("exceptionMessages.wrongOwnerId");
    WRONG_X_EXCEPTION_MESSAGE = resourceBundle.getString("exceptionMessages.wrongX");
    WRONG_Y_EXCEPTION_MESSAGE = resourceBundle.getString("exceptionMessages.wrongY");
    WRONG_Z_EXCEPTION_MESSAGE = resourceBundle.getString("exceptionMessages.wrongZ");
  }

  private long id;
  private long ownerID;
  private Double x;
  private Double y;
  private Double z;

  public Coordinates(long id, long ownerID, Double x, Double y, Double z)
      throws ValidationException {
    checkId(id);
    this.id = id;

    checkOwnerID(ownerID);
    this.ownerID = ownerID;

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

  public final long getID() {
    return id;
  }

  public final void setID(long id) throws ValidationException {
    checkId(id);
    this.id = id;
  }

  private void checkId(long id) throws ValidationException {
    if (id > 0 || id == DEFAULT_ID) {
      return;
    }

    throw new ValidationException(WRONG_ID_EXCEPTION_MESSAGE);
  }

  public final long getOwnerID() {
    return ownerID;
  }

  public final void setOwnerID(long ownerID) throws ValidationException {
    checkOwnerID(ownerID);
    this.ownerID = ownerID;
  }

  private void checkOwnerID(long ownerId) throws ValidationException {
    if (ownerId > 0 || ownerId == DEFAULT_OWNER_ID) {
      return;
    }

    throw new ValidationException(WRONG_OWNER_ID_EXCEPTION_MESSAGE);
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
