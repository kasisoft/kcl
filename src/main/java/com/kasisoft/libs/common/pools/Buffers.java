package com.kasisoft.libs.common.pools;

import java.util.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Buffers {

  public static Buffer<boolean[]> BOOLEANS = new Buffer<>(
    $ -> new boolean[upsize($)], 
    $ -> Arrays.fill($, false), 
    $ -> $.length
  );
  
  public static Buffer<char[]> CHARS = new Buffer<>(
    $ -> new char[upsize($)], 
    $ -> Arrays.fill($, (char) 0), 
    $ -> $.length
  );
  
  public static Buffer<byte[]> BYTES = new Buffer<>(
    $ -> new byte[upsize($)], 
    $ -> Arrays.fill($, (byte) 0), 
    $ -> $.length
  );

  public static Buffer<short[]> SHORTS = new Buffer<>(
    $ -> new short[upsize($)], 
    $ -> Arrays.fill($, (short) 0), 
    $ -> $.length
  );

  public static Buffer<int[]> INTS = new Buffer<>(
    $ -> new int[upsize($)], 
    $ -> Arrays.fill($, 0), 
    $ -> $.length
  );

  public static Buffer<long[]> LONGS = new Buffer<>(
    $ -> new long[upsize($)], 
    $ -> Arrays.fill($, 0l), 
    $ -> $.length
  );

  public static Buffer<float[]> FLOATS = new Buffer<>(
    $ -> new float[upsize($)], 
    $ -> Arrays.fill($, 0l), 
    $ -> $.length
  );

  public static Buffer<double[]> DOUBLES = new Buffer<>(
    $ -> new double[upsize($)], 
    $ -> Arrays.fill($, 0l), 
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
