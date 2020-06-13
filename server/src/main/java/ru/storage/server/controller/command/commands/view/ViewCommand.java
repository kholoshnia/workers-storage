package ru.storage.server.controller.command.commands.view;

import com.google.gson.Gson;
import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.command.Command;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.repository.Repository;

import java.util.Map;

public abstract class ViewCommand extends Command {
  protected final Gson gson;
  protected final Repository<Worker> workerRepository;

  public ViewCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Gson gson,
      Repository<Worker> workerRepository) {
    super(configuration, argumentMediator, arguments);
    this.gson = gson;
    this.workerRepository = workerRepository;
  }
}
