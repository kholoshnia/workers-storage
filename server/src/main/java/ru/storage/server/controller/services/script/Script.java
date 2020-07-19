package ru.storage.server.controller.services.script;

import ru.storage.server.model.domain.entity.entities.user.User;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Script class implements {@link Iterable<String>}, contains necessary data for commands execution.
 */
public final class Script implements Iterable<String> {
  private final Locale locale;
  private final User user;
  private final List<String> lines;

  public Script(Locale locale, User user, List<String> lines) {
    this.locale = locale;
    this.user = user;
    this.lines = lines;
  }

  public Locale getLocale() {
    return locale;
  }

  public User getUser() {
    return user;
  }

  @Override
  public Iterator<String> iterator() {
    return lines.iterator();
  }
}
