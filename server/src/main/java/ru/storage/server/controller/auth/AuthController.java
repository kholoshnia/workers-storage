package ru.storage.server.controller.auth;

import com.google.inject.Inject;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.apache.commons.configuration2.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.CommandMediator;
import ru.storage.common.transfer.request.Request;
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

    authCommands =
        new ArrayList<String>() {
          {
            add(commandMediator.LOGIN);
            add(commandMediator.REGISTER);
            add(commandMediator.LOGOUT);
          }
        };

    this.key = key;
    subject = configuration.getString("jwt.subject");
  }

  @Override
  public Response handle(Request request) {
    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("localized.AuthController", request.getLocale());
    String unauthorizedAnswer = resourceBundle.getString("answers.unauthorized");

    if (authCommands.contains(request.getCommand())) {
      logger.warn(() -> "Got an authentication command, skipping the token check.");
      return null;
    }

    if (request.getToken().isEmpty()) {
      logger.warn("Token was not found, user was not authenticated.");
      return new Response(Status.UNAUTHORIZED, unauthorizedAnswer);
    }

    try {
      if (!Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(request.getToken())
          .getBody()
          .getSubject()
          .equals(subject)) {
        logger.warn(() -> "User was not authorized");
        return new Response(Status.UNAUTHORIZED, unauthorizedAnswer);
      }
    } catch (JwtException e) {
      logger.warn(() -> "User was not authorized", e);
      return new Response(Status.UNAUTHORIZED, unauthorizedAnswer);
    }

    logger.info(() -> "User has been authorized.");
    return null;
  }
}
