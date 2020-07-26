package ru.storage.server.controller;

import ru.storage.common.transfer.Request;
import ru.storage.common.transfer.response.Response;

public interface Controller {
  /**
   * Handles request. Returns null if handling was done successfully and no response required.
   *
   * @param request user request
   * @return handling response
   */
  Response handle(Request request);
}
