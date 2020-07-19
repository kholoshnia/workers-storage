package ru.storage.client.controller.argumentFormer.argumentFormers;

import ru.storage.client.controller.argumentFormer.ArgumentValidator;
import ru.storage.client.controller.argumentFormer.exceptions.WrongArgumentsException;
import ru.storage.client.view.console.Console;

import java.util.List;
import java.util.Map;

public final class ScriptFormer extends Former {
  public ScriptFormer(Console console, Map<String, ArgumentValidator> validatorMap) {
    super(console, validatorMap);
  }

  @Override
  public void check(List<String> arguments) throws WrongArgumentsException {}

  @Override
  public Map<String, String> form(List<String> arguments) {
    return null;
  }
}
