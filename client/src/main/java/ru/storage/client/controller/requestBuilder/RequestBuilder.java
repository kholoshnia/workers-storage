package ru.storage.client.controller.requestBuilder;

import ru.storage.client.controller.argumentFormer.ArgumentFormer;
import ru.storage.client.controller.argumentFormer.FormerMediator;
import ru.storage.client.controller.requestBuilder.exceptions.BuildingException;
import ru.storage.common.transfer.request.Request;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Class is responsible for requests building. */
public final class RequestBuilder {
  private String command = "";
  private Map<String, String> arguments = new HashMap<>();
  private Locale locale = Locale.ENGLISH;
  private String token = "";

  /**
   * Sets command. NOTE: if not set uses default empty arguments. If is null on {@link #build} call,
   * throws {@link BuildingException}.
   *
   * @param command concrete command
   * @return this request builder
   */
  public RequestBuilder setCommand(@Nonnull String command) {
    this.command = command;
    return this;
  }

  /**
   * Sets command arguments. NOTE: if not set uses default empty command. If is null on {@link
   * #build} call, throws {@link BuildingException}.
   *
   * @param arguments command arguments
   * @return this request builder
   */
  public RequestBuilder setArguments(@Nonnull Map<String, String> arguments) {
    this.arguments = arguments;
    return this;
  }

  /**
   * Sets raw arguments using {@link ArgumentFormer}.
   *
   * <ol>
   *   <li>Gets argument former using {@link FormerMediator};
   *   <li>Checks arguments using {@link ArgumentFormer#check(List)};
   *   <li>Forms additional arguments using {@link ArgumentFormer#form(List)}.
   * </ol>
   *
   * @param arguments command arguments
   * @param formerMediator concrete former mediator
   * @return this request builder
   * @throws BuildingException - if specified arguments are wrong or got null {@link ArgumentFormer}
   */
  public RequestBuilder setRawArguments(
      @Nonnull List<String> arguments, @Nonnull FormerMediator formerMediator)
      throws BuildingException {
    ArgumentFormer argumentFormer = formerMediator.getArgumentFormer(command);

    if (argumentFormer == null) {
      throw new BuildingException();
    }

    argumentFormer.check(arguments);
    this.arguments = argumentFormer.form(arguments);

    return this;
  }

  /**
   * Sets locale. NOTE: if not set uses default {@link Locale#ENGLISH}. If is null on {@link #build}
   * call, throws {@link BuildingException}.
   *
   * @param locale concrete locale
   * @return this request builder
   */
  public RequestBuilder setLocale(@Nonnull Locale locale) {
    this.locale = locale;
    return this;
  }

  /**
   * Sets java web token. NOTE: if not set uses default empty token. If is null on {@link #build}
   * call, throws {@link BuildingException}.
   *
   * @param token concrete token
   * @return this request builder
   */
  public RequestBuilder setToken(@Nonnull String token) {
    this.token = token;
    return this;
  }

  /**
   * Builds request from set parameters. NOTE: returns null if cannot build request.
   *
   * @return new request
   * @throws BuildingException - if parameters were not set or required were empty
   */
  public Request build() throws BuildingException {
    if (command == null || locale == null || arguments == null || token == null) {
      throw new BuildingException();
    }

    return new Request(command, arguments, locale, token);
  }
}
