package ru.storage.common.chunker;

import java.util.List;

public interface ByteChunker {
  /**
   * Returns stop word chunk.
   *
   * @return stop word chunk
   */
  byte[] getStopWordChunk();

  /**
   * Splits byte array to the chunk list.
   *
   * @param bytes byte array
   * @return chunk list
   */
  List<byte[]> split(byte[] bytes);

  /**
   * Joins chunks to the one byte array.
   *
   * @param chunks list of chunks
   * @return byte array
   */
  byte[] join(List<byte[]> chunks);
}
