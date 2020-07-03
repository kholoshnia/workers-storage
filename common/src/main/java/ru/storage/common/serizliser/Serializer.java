package ru.storage.common.serizliser;

import ru.storage.common.serizliser.exceptions.DeserializationException;

import java.io.Serializable;

/** Serializer class is responsible for serialization and deserialization objects. */
public interface Serializer {
  /**
   * Returns serialized object in a form of a bytes.
   *
   * @param serializable concrete serializable object
   * @return object in a form of a bytes
   */
  byte[] serialize(Serializable serializable);

  /**
   * Returns deserialized object from bytes.
   *
   * @param bytes object in a form of a bytes
   * @param clazz class of the deserializable object
   * @param <T> class type of the deserializable object
   * @return deserializes object
   * @throws DeserializationException - if deserialization is incorrect
   */
  <T extends Serializable> T deserialize(byte[] bytes, Class<T> clazz)
      throws DeserializationException;
}
