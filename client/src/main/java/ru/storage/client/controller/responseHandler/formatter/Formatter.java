package ru.storage.client.controller.responseHandler.formatter;

import javax.annotation.Nonnull;

public interface Formatter {
  String makeBlack(@Nonnull String string);

  String makeRed(@Nonnull String string);

  String makeGreen(@Nonnull String string);

  String makeYellow(@Nonnull String string);

  String makeBlue(@Nonnull String string);

  String makeMagenta(@Nonnull String string);

  String makeCyan(@Nonnull String string);

  String makeWhite(@Nonnull String string);
}
