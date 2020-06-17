package com.kasisoft.libs.common.pools;

import java.util.Arrays;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Buffers {

  public static Buffer<byte[]> BYTES = new Buffer<>(
    $ -> new byte[upsize($)], 
    $ -> Arrays.fill($, (byte) 0), 
    $ -> $.length
  );

  public static Buffer<char[]> CHARS = new Buffer<>(
    $ -> new char[upsize($)], 
    $ -> Arrays.fill($, (char) 0), 
    $ -> $.length
  );

  private static int upsize(int size) {
    int result = size;
    if (size >= 4096) {
      result = upsize(size, 4096);
    } else if (size >= 2048) {
      result = upsize(size, 2048);
    } else if (size >= 1024) {
      result = upsize(size, 1024);
    } else {
      result = upsize(size, 512);
    }
    return result;
  }

  private static int upsize(int size, int blocksize) {
    int count = size / blocksize;
    int rest  = size % blocksize;
    if (rest > 0) {
      rest = blocksize;
    } else {
      rest = 0;
    }
    return count * blocksize + rest;
  }
  
} /* ENDCLASS */
