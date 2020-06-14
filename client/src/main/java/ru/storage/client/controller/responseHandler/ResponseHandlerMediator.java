package ru.storage.client.controller.responseHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.responseHandler.responseHandlers.*;
import ru.storage.common.transfer.response.Status;

import java.util.HashMap;
import java.util.Map;

public final class ResponseHandlerMediator {
  private final Logger logger;
  private final Map<Status, ResponseHandler> responseHandlers;

  public ResponseHandlerMediator() {
    this.logger = LogManager.getLogger(ResponseHandlerMediator.class);
    this.responseHandlers =
        new HashMap<Status, ResponseHandler>() {
          {
            put(Status.OK, new OKResponseHandler());
            put(Status.CREATED, new CreatedResponseHandler());
            put(Status.NO_CONTENT, new NoContentResponseHandler());
            put(Status.NOT_MODIFIED, new NotModifiedResponseHandler());
            put(Status.BAD_REQUEST, new BadRequestResponseHandler());
            put(Status.UNAUTHORIZED, new UnauthorizedResponseHandler());
            put(Status.NOT_FOUND, new NotFoundResponseHandler());
            put(Status.FORBIDDEN, new ForbiddenResponseHandler());
            put(Status.CONFLICT, new ConflictResponseHandler());
            put(Status.INTERNAL_SERVER_ERROR, new InternalServerErrorResponseHandler());
          }
        };

    logger.debug(() -> "Response handler map was created.");
  }

  public ResponseHandler getResponseHandler(Status status) {
    ResponseHandler responseHandler = responseHandlers.get(status);

    logger.info("Got response handler: {}, for status: {}.", () -> responseHandler, () -> status);
    return responseHandler;
  }
}
