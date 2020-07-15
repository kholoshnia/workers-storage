package ru.storage.common.transfer.response;

import java.io.Serializable;

/** Response from server to the client. */
public final class Response implements Serializable {
  private final Status status;
  private final String answer;
  private final String token;

  public Response(Status status, String answer, String token) {
    this.status = status;
    this.answer = answer;
    this.token = token;
  }

  public Response(Status status, String answer) {
    this.status = status;
    this.answer = answer;
    token = null;
  }

  public Status getStatus() {
    return status;
  }

  public String getAnswer() {
    return answer;
  }

  public String getToken() {
    return token;
  }

  @Override
  public String toString() {
    return "Response{" + "status=" + status + ", answer='" + answer + '\'' + '}';
  }
}
