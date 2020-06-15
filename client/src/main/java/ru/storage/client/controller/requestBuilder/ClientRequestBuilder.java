package ru.storage.client.controller.requestBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.client.controller.requestBuilder.requestBuilders.NoArgumentsRequestBuilder;
import ru.storage.client.controller.requestBuilder.requestBuilders.WorkerIDArgumentRequestBuilder;
import ru.storage.common.ArgumentMediator;
import ru.storage.common.CommandMediator;

import java.util.HashMap;
import java.util.Map;

public final class ClientRequestBuilder {
  private final Logger logger;
  private final Map<String, RequestBuilder> requestBuilders;

  public ClientRequestBuilder(CommandMediator commandMediator, ArgumentMediator argumentMediator) {
    this.logger = LogManager.getLogger(ClientRequestBuilder.class);
    this.requestBuilders =
        new HashMap<String, RequestBuilder>() {
          {
            put(
                commandMediator.LOGIN,
                new NoArgumentsRequestBuilder(argumentMediator, commandMediator.LOGIN));
            put(
                commandMediator.LOGOUT,
                new NoArgumentsRequestBuilder(argumentMediator, commandMediator.LOGOUT));
            put(
                commandMediator.REGISTER,
                new NoArgumentsRequestBuilder(argumentMediator, commandMediator.REGISTER));
            put(
                commandMediator.SHOW_HISTORY,
                new NoArgumentsRequestBuilder(argumentMediator, commandMediator.SHOW_HISTORY));
            put(
                commandMediator.CLEAR_HISTORY,
                new NoArgumentsRequestBuilder(argumentMediator, commandMediator.CLEAR_HISTORY));
            put(
                commandMediator.ADD,
                new WorkerIDArgumentRequestBuilder(argumentMediator, commandMediator.ADD));
            put(
                commandMediator.REMOVE,
                new WorkerIDArgumentRequestBuilder(argumentMediator, commandMediator.REMOVE));
            put(
                commandMediator.UPDATE,
                new WorkerIDArgumentRequestBuilder(argumentMediator, commandMediator.UPDATE));
            put(
                commandMediator.EXIT,
                new NoArgumentsRequestBuilder(argumentMediator, commandMediator.EXIT));
            put(
                commandMediator.HELP,
                new NoArgumentsRequestBuilder(argumentMediator, commandMediator.HELP));
            put(
                commandMediator.INFO,
                new NoArgumentsRequestBuilder(argumentMediator, commandMediator.INFO));
            put(
                commandMediator.SHOW,
                new NoArgumentsRequestBuilder(argumentMediator, commandMediator.SHOW));
          }
        };
  }

  public RequestBuilder getRequestBuilder(String command) {
    RequestBuilder requestBuilder = requestBuilders.get(command);

    logger.info("Got requestBuilder {}, for command {}.", () -> requestBuilder, () -> command);
    return requestBuilder;
  }
}
