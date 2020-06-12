package ru.storage.server.model.domain.entity;

import ru.storage.common.dto.exceptions.ValidationException;

import java.util.Locale;
import java.util.ResourceBundle;

public abstract class Ownable extends ID {
  public static final String OWNER_ID_COLUMN = "owner_id";

  private static final String WRONG_OWNER_ID_EXCEPTION_MESSAGE;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.Ownable", Locale.ENGLISH);

    WRONG_OWNER_ID_EXCEPTION_MESSAGE = resourceBundle.getString("exceptionMessages.wrongOwnerId");
  }

  protected long ownerID;

  protected Ownable(long id, long ownerID) throws ValidationException {
    super(id);

    checkOwnerID(id);
    this.ownerID = ownerID;
  }

  public final long getOwnerID() {
    return ownerID;
  }

  public final void setOwnerID(long ownerID) throws ValidationException {
    checkOwnerID(ownerID);
    this.ownerID = ownerID;
  }

  private void checkOwnerID(long ownerId) throws ValidationException {
    if (ownerId > 0 || ownerId == DEFAULT) {
      return;
    }

    throw new ValidationException(WRONG_OWNER_ID_EXCEPTION_MESSAGE);
  }
}
