package ru.storage.server.controller;

import ru.storage.common.transfer.request.Request;
import ru.storage.common.transfer.response.Response;

public interface Controller {
  Response handle(Request request);
}
