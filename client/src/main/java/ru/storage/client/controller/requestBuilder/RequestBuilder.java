package ru.storage.client.controller.requestBuilder;

import ru.storage.client.controller.argumentFormer.ArgumentFormer;
import ru.storage.client.controller.argumentFormer.FormerMediator;
import ru.storage.client.controller.argumentFormer.exceptions.CancelException;
import ru.storage.client.controller.argumentFormer.exceptions.FormingException;
import ru.storage.client.controller.argumentFormer.exceptions.WrongArgumentsException;
import ru.storage.client.controller.requestBuilder.exceptions.BuildingException;
import ru.storage.common.transfer.Request;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Class is responsible for building requests. */
public final class RequestBuilder {
  private String command = "";
  private Map<String, String> arguments = new HashMap<>();
  private Locale locale = Locale.ENGLISH;
  private String login = "";
  private String token = "";

  /**
   * Sets command. NOTE: if not set uses default empty arguments.
   *
   * @param command command name
   * @return this request builder
   */
  public RequestBuilder setCommand(@Nonnull String command) {
    this.command = command;
    return this;
  }

  /**
   * Sets command arguments. NOTE: if not set uses default empty command.
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
   *   <li>Checks and forms arguments using {@link ArgumentFormer#formArguments(List)};
   * </ol>
   *
   * @param arguments command arguments
   * @param formerMediator argument former mediator
   * @return this request builder
   * @throws BuildingException - if got null {@link ArgumentFormer}
   * @throws WrongArgumentsException - if specified arguments are wrong
   * @throws FormingException - in case of any exceptions during forming
   * @throws CancelException - if forming was canceled
   */
  public RequestBuilder setRawArguments(
      @Nonnull List<String> arguments, @Nonnull FormerMediator formerMediator)
      throws BuildingException, WrongArgumentsException, FormingException, CancelException {
    ArgumentFormer argumentFormer = formerMediator.getArgumentFormer(command);

    if (argumentFormer == null) {
      throw new BuildingException();
    }

    this.arguments = argumentFormer.formArguments(arguments);
    return this;
  }

  /**
   * Sets locale. NOTE: if not set uses default {@link Locale#ENGLISH}.
   *
   * @param locale user locale
   * @return this request builder
   */
  public RequestBuilder setLocale(@Nonnull Locale locale) {
    this.locale = locale;
    return this;
  }

  /**
   * Sets login. NOTE: if not set uses default empty login.
   *
   * @param login user login
   * @return this request builder.
   */
  public RequestBuilder setLogin(@Nonnull String login) {
    this.login = login;
    return this;
  }

  /**
   * Sets java web token. NOTE: if not set uses default empty token.
   *
   * @param token user token
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
   */
  public Request build() {
    return new Request(command, arguments, locale, login, token);
  }
}
