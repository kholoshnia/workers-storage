package ru.storage.server.controller.command.commands.view;

import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.command.Command;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.repository.Repository;

import java.util.Locale;
import java.util.Map;

public abstract class ViewCommand extends Command {
  protected final Repository<Worker> workerRepository;

  public ViewCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      Repository<Worker> workerRepository) {
    super(configuration, argumentMediator, arguments, locale);
    this.workerRepository = workerRepository;
  }

  protected final String workerToString(Worker worker) {
    return null;
  }
}
