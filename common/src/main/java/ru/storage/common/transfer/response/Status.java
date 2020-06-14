package ru.storage.common.transfer.response;

import java.io.Serializable;

public enum Status implements Serializable {
  OK(200),
  CREATED(201),
  NO_CONTENT(204),
  NOT_MODIFIED(304),
  BAD_REQUEST(400),
  UNAUTHORIZED(401),
  NOT_FOUND(404),
  FORBIDDEN(403),
  CONFLICT(409),
  INTERNAL_SERVER_ERROR(500);

  private final int code;

  Status(int code) {
    this.code = code;
  }

  public static Status getStatus(int code) {
    Status[] values = values();

    for (Status value : values) {
      if (value.getCode() == code) {
        return value;
      }
    }

    return null;
  }

  public int getCode() {
    return code;
  }
}
