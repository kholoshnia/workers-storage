package ru.storage.server.controller.command.commands.entry;

import org.apache.commons.configuration2.Configuration;
import ru.storage.common.transfer.response.Response;
import ru.storage.server.controller.command.Command;
import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.repository.Repository;

import java.util.Locale;
import java.util.Map;

public abstract class EntryCommand extends Command {
  protected final Repository<User> userRepository;

  public EntryCommand(
      Configuration configuration,
      Map<String, String> arguments,
      Locale locale,
      Repository<User> userRepository) {
    super(configuration, arguments, locale);
    this.userRepository = userRepository;
  }
}
