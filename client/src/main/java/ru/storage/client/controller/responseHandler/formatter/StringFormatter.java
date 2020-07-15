package ru.storage.client.controller.responseHandler.formatter;

import javax.annotation.Nonnull;

/** Can be used everywhere to make string colorful on console output. */
public final class StringFormatter implements Formatter {
  @Override
  public String makeBlack(@Nonnull String string) {
    return AnsiColor.BLACK + string + AnsiColor.RESET;
  }

  @Override
  public String makeRed(@Nonnull String string) {
    return AnsiColor.RED + string + AnsiColor.RESET;
  }

  @Override
  public String makeGreen(@Nonnull String string) {
    return AnsiColor.GREEN + string + AnsiColor.RESET;
  }

  @Override
  public String makeYellow(@Nonnull String string) {
    return AnsiColor.YELLOW + string + AnsiColor.RESET;
  }

  @Override
  public String makeBlue(@Nonnull String string) {
    return AnsiColor.BLUE + string + AnsiColor.RESET;
  }

  @Override
  public String makeMagenta(@Nonnull String string) {
    return AnsiColor.MAGENTA + string + AnsiColor.RESET;
  }

  @Override
  public String makeCyan(@Nonnull String string) {
    return AnsiColor.CYAN + string + AnsiColor.RESET;
  }

  @Override
  public String makeWhite(@Nonnull String string) {
    return AnsiColor.WHITE + string + AnsiColor.RESET;
  }
}
