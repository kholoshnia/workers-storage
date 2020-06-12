package ru.storage.server.controller.command.commands.modification;

import org.apache.commons.configuration2.Configuration;
import ru.storage.common.ArgumentMediator;
import ru.storage.server.controller.command.Command;
import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.repository.Repository;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public abstract class ModificationCommand extends Command {
  protected final String WRONG_ID_ANSWER;
  protected final String COLLECTION_IS_EMPTY_ANSWER;

  protected final Repository<Worker> workerRepository;

  public ModificationCommand(
      Configuration configuration,
      ArgumentMediator argumentMediator,
      Map<String, String> arguments,
      Locale locale,
      Repository<Worker> workerRepository) {
    super(configuration, argumentMediator, arguments, locale);
    this.workerRepository = workerRepository;

    ResourceBundle resourceBundle =
        ResourceBundle.getBundle("localized.ModificationCommand", locale);

    WRONG_ID_ANSWER = resourceBundle.getString("answers.wrongID");
    COLLECTION_IS_EMPTY_ANSWER = resourceBundle.getString("answers.collectionIsEmpty");
  }
}
