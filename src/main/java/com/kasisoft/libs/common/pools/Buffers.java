package com.kasisoft.libs.common.pools;

import java.util.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
@SuppressWarnings("unchecked")
public class Buffers {

    private static Map<Class<?>, Buffer<?>> BUFFERS = new HashMap<>();

    public static Buffer<boolean[]> booleanArray() {
        return (Buffer<boolean[]>) BUFFERS.computeIfAbsent(boolean[].class, $type -> {
            return new Buffer<>($ -> new boolean[upsize($)], $ -> Arrays.fill($, false), $ -> $.length);
        });
    }

    public static Buffer<char[]> charArray() {
        return (Buffer<char[]>) BUFFERS.computeIfAbsent(char[].class, $type -> {
            return new Buffer<>($ -> new char[upsize($)], $ -> Arrays.fill($, (char) 0), $ -> $.length);
        });
    }

    public static Buffer<byte[]> byteArray() {
        return (Buffer<byte[]>) BUFFERS.computeIfAbsent(byte[].class, $type -> {
            return new Buffer<>($ -> new byte[upsize($)], $ -> Arrays.fill($, (byte) 0), $ -> $.length);
        });
    }

    public static Buffer<short[]> shortArray() {
        return (Buffer<short[]>) BUFFERS.computeIfAbsent(short[].class, $type -> {
            return new Buffer<>($ -> new short[upsize($)], $ -> Arrays.fill($, (short) 0), $ -> $.length);
        });
    }

    public static Buffer<int[]> intArray() {
        return (Buffer<int[]>) BUFFERS.computeIfAbsent(int[].class, $type -> {
            return new Buffer<>($ -> new int[upsize($)], $ -> Arrays.fill($, 0), $ -> $.length);
        });
    }

    public static Buffer<long[]> longArray() {
        return (Buffer<long[]>) BUFFERS.computeIfAbsent(long[].class, $type -> {
            return new Buffer<>($ -> new long[upsize($)], $ -> Arrays.fill($, 0l), $ -> $.length);
        });
    }

    public static Buffer<float[]> floatArray() {
        return (Buffer<float[]>) BUFFERS.computeIfAbsent(float[].class, $type -> {
            return new Buffer<>($ -> new float[upsize($)], $ -> Arrays.fill($, 0l), $ -> $.length);
        });
    }

    public static Buffer<double[]> doubleArray() {
        return (Buffer<double[]>) BUFFERS.computeIfAbsent(double[].class, $type -> {
            return new Buffer<>($ -> new double[upsize($)], $ -> Arrays.fill($, 0l), $ -> $.length);
        });
    }

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
