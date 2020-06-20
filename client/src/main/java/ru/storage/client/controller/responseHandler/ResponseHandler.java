package ru.storage.client.controller.responseHandler;

import ru.storage.client.controller.localeManager.LocaleListener;
import ru.storage.common.transfer.response.Response;

public interface ResponseHandler extends LocaleListener {
  String handle(Response response);
}
