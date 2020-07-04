package ru.storage.client.controller.requestBuilder;

import ru.storage.client.controller.argumentFormer.ArgumentFormer;
import ru.storage.client.controller.argumentFormer.FormerMediator;
import ru.storage.client.controller.requestBuilder.exceptions.BuildingException;
import ru.storage.common.transfer.request.Request;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Class is responsible for requests building. */
public final class RequestBuilder {
  private FormerMediator formerMediator;
  private String command;
  private List<String> arguments;
  private Locale locale;
  private String token;

  /**
   * Sets argument former mediator. NOTE: if not set on {@link RequestBuilder#build()} it throws
   * {@link BuildingException}.
   *
   * @param formerMediator concrete argument former mediator
   * @return this request builder
   */
  public RequestBuilder setFormerMediator(FormerMediator formerMediator) {
    this.formerMediator = formerMediator;
    return this;
  }

  /**
   * Sets command. NOTE: if not set on {@link RequestBuilder#build()} it throws {@link
   * BuildingException}.
   *
   * @param command concrete command
   * @return this request builder
   */
  public RequestBuilder setCommand(String command) {
    this.command = command;
    return this;
  }

  /**
   * Sets command arguments. NOTE: if not set on {@link RequestBuilder#build()} it throws {@link
   * BuildingException}.
   *
   * @param arguments concrete command arguments
   * @return this request builder
   */
  public RequestBuilder setArguments(List<String> arguments) {
    this.arguments = arguments;
    return this;
  }

  /**
   * Sets locale. NOTE: if not set on {@link RequestBuilder#build()} it throws {@link
   * BuildingException}.
   *
   * @param locale concrete locale
   * @return this request builder
   */
  public RequestBuilder setLocale(Locale locale) {
    this.locale = locale;
    return this;
  }

  /**
   * Sets java web token.
   *
   * @param token concrete token
   * @return this request builder
   */
  public RequestBuilder setToken(String token) {
    this.token = token;
    return this;
  }

  /**
   * Builds request from set parameters.
   *
   * @return new request
   * @throws BuildingException - if parameters were not set or required were empty
   */
  public Request build() throws BuildingException {
    if (formerMediator == null || command == null || arguments == null || locale == null) {
      return null;
    }

    ArgumentFormer argumentFormer = formerMediator.getArgumentFormer(command);

    if (argumentFormer == null) {
      return null;
    }

    argumentFormer.check(arguments);
    Map<String, String> allArguments = argumentFormer.form(arguments);

    return new Request(command, allArguments, locale, token);
  }
}
