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
import java.util.ResourceBundle;

/** Authorizes user using java web token. */
public class AuthController implements Controller {
  private final Logger logger;
  private final List<String> authCommands;
  private final Key key;
  private final String subject;

  @Inject
  public AuthController(Configuration configuration, CommandMediator commandMediator, Key key) {
    logger = LogManager.getLogger(AuthController.class);
    authCommands = initAuthCommands(commandMediator);
    this.key = key;
    subject = configuration.getString("jwt.subject");
  }

  private List<String> initAuthCommands(CommandMediator commandMediator) {
    return new ArrayList<String>() {
      {
        add(commandMediator.LOGIN);
        add(commandMediator.REGISTER);
      }
    };
  }

  @Override
  public Response handle(Request request) {
    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("localized.AuthController", request.getLocale());
    String unauthorizedAnswer = resourceBundle.getString("answers.unauthorized");
    String alreadyAuthorizedAnswer = resourceBundle.getString("answers.alreadyAuthorized");

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
