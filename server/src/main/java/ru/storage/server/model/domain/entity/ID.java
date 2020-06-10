package ru.storage.server.model.domain.entity;

import ru.storage.common.api.dto.exceptions.ValidationException;

import java.util.Locale;
import java.util.ResourceBundle;

public abstract class ID {
  public static final String ID_COLUMN = "id";

  public static final long DEFAULT = 0L;

  private static final String WRONG_ID_EXCEPTION_MESSAGE;

  static {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("internal.ID", Locale.ENGLISH);

    WRONG_ID_EXCEPTION_MESSAGE = resourceBundle.getString("exceptionMessages.wrongId");
  }

  protected long id;

  protected ID(long id) throws ValidationException {
    checkId(id);
    this.id = id;
  }

  public final long getID() {
    return id;
  }

  public final void setID(long id) throws ValidationException {
    checkId(id);
    this.id = id;
  }

  private void checkId(long id) throws ValidationException {
    if (id > 0 || id == DEFAULT) {
      return;
    }

    throw new ValidationException(WRONG_ID_EXCEPTION_MESSAGE);
  }
}
