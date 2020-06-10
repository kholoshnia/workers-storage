package ru.storage.common.transfer.response;

import ru.storage.common.api.dto.DTO;
import ru.storage.common.api.dto.Entity;

public final class Response implements Entity {
  private final Status status;
  private final String answer;

  public Response(Status status, String answer) {
    this.status = status;
    this.answer = answer;
  }

  public DTO<Response> toDTO() {
    return new ResponseDTO(status.getCode(), answer);
  }
}
