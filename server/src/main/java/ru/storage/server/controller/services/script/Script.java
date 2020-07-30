package ru.storage.server.controller.services.script;

import ru.storage.server.model.domain.entity.entities.user.User;

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
 *   <li>worker
 *   <li>workerId
 *   <li>workerSalary
 *   <li>workerStatus
 *   <li>workerStartDate
 *   <li>workerEndDate
 *   <li>coordinates
 *   <li>coordinatesX
 *   <li>coordinatesY
 *   <li>coordinatesZ
 *   <li>person
 *   <li>personName
 *   <li>personPassportId
 *   <li>location
 *   <li>locationAddress
 *   <li>locationLatitude
 *   <li>locationLongitude
 * </ul>
 */
public final class Script {
  private final Locale locale;
  private final User user;
  private final List<String> lines;

  private int current;

  public Script(Locale locale, User user, List<String> lines) {
    this.locale = locale;
    this.user = user;
    this.lines = lines;
    current = -1;
  }

  public Locale getLocale() {
    return locale;
  }

  public User getUser() {
    return user;
  }

  public int getCurrent() {
    return current;
  }

  public boolean hasNext() {
    return current < lines.size();
  }

  public String nextLine() {
    current++;

    if (current < lines.size()) {
      return lines.get(current);
    }

    return null;
  }

  public void back() {
    if (current > -1) {
      current--;
    }
  }

  @Override
  public int hashCode() {
    return lines.stream().mapToInt(String::hashCode).sum();
  }
}
