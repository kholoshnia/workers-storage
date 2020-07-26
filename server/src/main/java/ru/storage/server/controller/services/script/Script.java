package ru.storage.server.controller.services.script;

import ru.storage.server.model.domain.entity.entities.user.User;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Script class implements {@link Iterable}, contains necessary data for commands execution.
 *
 * <p>Script semantics:
 *
 * <ul>
 *   <li>each command must start on a new line;
 *   <li>if the command requires additional arguments, the following lines must contain them in the
 *       form name:value, name:"value" or name:'value' in case of ":' symbols.
 * </ul>
 *
 * <p>List of available arguments:
 *
 * <ul>
 *   <li>id
 *   <li>workerSalary
 *   <li>workerStatus
 *   <li>workerStartDate
 *   <li>workerEndDate
 *   <li>coordinatesX
 *   <li>coordinatesY
 *   <li>coordinatesZ
 *   <li>personName
 *   <li>personPassportId
 *   <li>locationAddress
 *   <li>locationLatitude
 *   <li>locationLongitude
 * </ul>
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
