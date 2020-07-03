package ru.storage.common.serizliser.serializers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.storage.common.serizliser.Serializer;
import ru.storage.common.serizliser.exceptions.DeserializationException;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public final class JsonSerializer implements Serializer {
  private final Logger logger;
  private final Gson gson;

  @Inject
  public JsonSerializer(Gson gson) {
    this.logger = LogManager.getLogger(JsonSerializer.class);
    this.gson = gson;
  }

  @Override
  public byte[] serialize(Serializable serializable) {
    String json = gson.toJson(serializable);
    byte[] result = json.getBytes(StandardCharsets.UTF_8);

    logger.info("Object has been serialized.");
    return result;
  }

  @Override
  public <T extends Serializable> T deserialize(byte[] bytes, Class<T> clazz)
      throws DeserializationException {
    String json = new String(bytes, StandardCharsets.UTF_8);
    T result;

    try {
      result = gson.fromJson(json, clazz);
    } catch (JsonSyntaxException e) {
      logger.error(() -> "Cannot deserialize object.", e);
      throw new DeserializationException(e);
    }

    logger.info(() -> "Object has been deserialized.");
    return result;
  }
}
