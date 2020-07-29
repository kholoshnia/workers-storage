package ru.storage.server.model.dao.adapter;

import ru.storage.server.model.dao.adapter.exceptions.AdapterException;

/**
 * Adapter class is used to convert value from the {@link From} type to the {@link To} type and
 * back.
 *
 * @param <From> from type
 * @param <To> to type
 */
public interface Adapter<From, To> {
  /**
   * Converts value from the {@link From} type to the {@link To} type.
   *
   * @param from from type
   * @return to type
   * @throws AdapterException - in case of converting exceptions.
   */
  To to(From from) throws AdapterException;

  /**
   * Converts value from the {@link To} type to the {@link From} type.
   *
   * @param to to type
   * @return from type
   * @throws AdapterException - in case of converting exceptions.
   */
  From from(To to) throws AdapterException;
}
