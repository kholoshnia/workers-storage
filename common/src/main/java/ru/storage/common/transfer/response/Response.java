package ru.storage.common.transfer.response;

import java.io.Serializable;

public final class Response implements Serializable {
  private final Status status;
  private final String answer;

  public Response(Status status, String answer) {
    this.status = status;
    this.answer = answer;
  }

  public Status getStatus() {
    return status;
  }

  public String getAnswer() {
    return answer;
  }

  @Override
  public String toString() {
    return "Response{" + "status=" + status + ", answer='" + answer + '\'' + '}';
  }
}
