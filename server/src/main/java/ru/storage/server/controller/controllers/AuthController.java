package ru.storage.server.controller.controllers;

import com.google.inject.Inject;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.CommandMediator;
import ru.storage.common.transfer.Request;
import ru.storage.common.transfer.response.Response;
import ru.storage.common.transfer.response.Status;
import ru.storage.server.controller.Controller;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/** Authorizes user using java web token. */
public class AuthController implements Controller {
  private static final Logger logger = LogManager.getLogger(AuthController.class);

  private final List<String> authCommands;
  private final Key key;
  private final String subject;

  private String unauthorizedAnswer;
  private String alreadyAuthorizedAnswer;

  @Inject
  public AuthController(Configuration configuration, CommandMediator commandMediator, Key key) {
    authCommands = initAuthCommands(commandMediator);
    this.key = key;
    subject = configuration.getString("jwt.subject");
  }

  private List<String> initAuthCommands(CommandMediator commandMediator) {
    return new ArrayList<String>() {
      {
        add(commandMediator.login);
        add(commandMediator.register);
      }
    };
  }

  private void changeLocale(Locale locale) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("localized.AuthController", locale);

    unauthorizedAnswer = resourceBundle.getString("answers.unauthorized");
    alreadyAuthorizedAnswer = resourceBundle.getString("answers.alreadyAuthorized");
  }

  @Override
  public Response handle(Request request) {
    changeLocale(request.getLocale());

    boolean authorized;

    try {
      authorized =
          Jwts.parserBuilder()
              .setSigningKey(key)
              .build()
              .parseClaimsJws(request.getToken())
              .getBody()
              .getSubject()
              .equals(subject);
    } catch (JwtException | IllegalArgumentException e) {
      logger.warn(() -> "Got wrong token.", e);
      authorized = false;
    }

    if (authCommands.contains(request.getCommand())) {
      if (authorized) {
        logger.warn(() -> "User is already authorized.");
        return new Response(Status.CONFLICT, alreadyAuthorizedAnswer);
      }

      logger.info(() -> "Got an authentication command, skipping the token check.");
      return null;
    }

    if (!authorized) {
      logger.warn(() -> "User was not authorized.");
      return new Response(Status.UNAUTHORIZED, unauthorizedAnswer);
    }

    logger.info(() -> "User was authorized.");
    return null;
  }
}
