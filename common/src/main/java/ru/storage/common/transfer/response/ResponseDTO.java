package ru.storage.common.transfer.response;

import ru.storage.common.dto.DTO;

public final class ResponseDTO implements DTO<Response> {
  public final int code;
  public final String answer;

  public ResponseDTO(int code, String answer) {
    this.code = code;
    this.answer = answer;
  }

  public Response toEntity() {
    return new Response(Status.getStatus(this.code), this.answer);
  }
}
