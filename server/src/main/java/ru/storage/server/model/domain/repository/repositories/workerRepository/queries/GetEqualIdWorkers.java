package ru.storage.server.model.domain.repository.repositories.workerRepository.queries;

import ru.storage.server.model.domain.entity.entities.worker.Worker;
import ru.storage.server.model.domain.repository.Query;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Returns workers collection copy. The copy contains a workers with an id equal to the specified.
 */
public final class GetEqualIdWorkers implements Query<Worker> {
  private final long id;

  /**
   * Creates a query to get workers with the specified id.
   *
   * @param id concrete id
   */
  public GetEqualIdWorkers(long id) {
    this.id = id;
  }

  @Override
  public List<Worker> execute(List<Worker> workers) {
    return workers.stream()
        .filter(worker -> worker.getId() == id)
        .collect(Collectors.toCollection(CopyOnWriteArrayList::new));
  }
}
