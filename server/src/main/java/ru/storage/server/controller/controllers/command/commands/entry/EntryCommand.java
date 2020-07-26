package ru.storage.server.controller.controllers.command.commands.entry;

import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.controllers.command.Command;
import ru.storage.server.controller.services.hash.HashGenerator;
import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.repository.Repository;

import java.security.Key;
import java.util.Locale;
import java.util.Map;

public abstract class EntryCommand extends Command {
  protected final Locale locale;
  protected final HashGenerator hashGenerator;
  protected final Repository<User> userRepository;
  protected final Key key;
  protected final String subject;

  public EntryCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      HashGenerator hashGenerator,
      Repository<User> userRepository,
      Key key) {
    super(configuration, argumentMediator, arguments);
    this.locale = locale;
    this.hashGenerator = hashGenerator;
    this.userRepository = userRepository;
    this.key = key;
    subject = configuration.getString("jwt.subject");
  }
}
