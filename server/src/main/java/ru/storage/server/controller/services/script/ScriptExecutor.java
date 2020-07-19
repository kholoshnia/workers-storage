package ru.storage.server.controller.services.script;

import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.transfer.response.Response;
import ru.storage.server.controller.command.factory.CommandFactoryMediator;

import java.util.Map;

public final class ScriptExecutor {
  private final Logger logger;
  private final CommandFactoryMediator commandFactoryMediator;

  @Inject
  public ScriptExecutor(CommandFactoryMediator commandFactoryMediator) {
    logger = LogManager.getLogger(ScriptExecutor.class);
    this.commandFactoryMediator = commandFactoryMediator;
  }

  public Response execute(Script script) {
    return null;
  }

  private Map<String, String> getArguments() {
    return null;
  }
}
