package ru.storage.server.controller.command.commands.entry;

import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.command.Command;
import ru.storage.server.model.domain.entity.entities.user.User;
import ru.storage.server.model.domain.repository.Repository;

import java.util.Map;

public abstract class EntryCommand extends Command {
  protected final Repository<User> userRepository;

  public EntryCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Repository<User> userRepository) {
    super(configuration, argumentMediator, arguments);
    this.userRepository = userRepository;
  }
}
