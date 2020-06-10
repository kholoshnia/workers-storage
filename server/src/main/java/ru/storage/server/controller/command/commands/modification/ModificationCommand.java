package ru.storage.server.controller.command.commands.modification;

import org.apache.commons.configuration2.Configuration;
import ru.storage.server.controller.command.Command;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.repository.Repository;

import java.util.Locale;
import java.util.Map;

public abstract class ModificationCommand extends Command {
  protected final Repository<Worker> workerRepository;

  public ModificationCommand(
      Configuration configuration,
      Map<String, String> arguments,
      Locale locale,
      Repository<Worker> workerRepository) {
    super(configuration, arguments, locale);
    this.workerRepository = workerRepository;
  }
}
