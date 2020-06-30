package com.kasisoft.libs.common.utils;

import com.kasisoft.libs.common.constants.Empty;

import com.kasisoft.libs.common.KclException;
import com.kasisoft.libs.common.functional.PrimitiveInterfaces.KConsumerBoolean;
import com.kasisoft.libs.common.functional.PrimitiveInterfaces.KConsumerByte;
import com.kasisoft.libs.common.functional.PrimitiveInterfaces.KConsumerChar;
import com.kasisoft.libs.common.functional.PrimitiveInterfaces.KConsumerDouble;
import com.kasisoft.libs.common.functional.PrimitiveInterfaces.KConsumerFloat;
import com.kasisoft.libs.common.functional.PrimitiveInterfaces.KConsumerInt;
import com.kasisoft.libs.common.functional.PrimitiveInterfaces.KConsumerLong;
import com.kasisoft.libs.common.functional.PrimitiveInterfaces.KConsumerShort;
import com.kasisoft.libs.common.functional.PrimitiveInterfaces.KFunctionBoolean;
import com.kasisoft.libs.common.functional.PrimitiveInterfaces.KFunctionByte;
import com.kasisoft.libs.common.functional.PrimitiveInterfaces.KFunctionChar;
import com.kasisoft.libs.common.functional.PrimitiveInterfaces.KFunctionDouble;
import com.kasisoft.libs.common.functional.PrimitiveInterfaces.KFunctionFloat;
import com.kasisoft.libs.common.functional.PrimitiveInterfaces.KFunctionInt;
import com.kasisoft.libs.common.functional.PrimitiveInterfaces.KFunctionLong;
import com.kasisoft.libs.common.functional.PrimitiveInterfaces.KFunctionShort;
import com.kasisoft.libs.common.functional.PrimitiveInterfaces.KSupplierBoolean;
import com.kasisoft.libs.common.functional.PrimitiveInterfaces.KSupplierByte;
import com.kasisoft.libs.common.functional.PrimitiveInterfaces.KSupplierChar;
import com.kasisoft.libs.common.functional.PrimitiveInterfaces.KSupplierDouble;
import com.kasisoft.libs.common.functional.PrimitiveInterfaces.KSupplierFloat;
import com.kasisoft.libs.common.functional.PrimitiveInterfaces.KSupplierInt;
import com.kasisoft.libs.common.functional.PrimitiveInterfaces.KSupplierLong;
import com.kasisoft.libs.common.functional.PrimitiveInterfaces.KSupplierShort;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Declarations used to identify primitive types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class PrimitiveFunctions {

  private static final Set<String> BOOLEAN_TRUES  = new HashSet<>(Arrays.asList("true", "yes", "ja", "Y", "j", "ein", "on", "enabled"));

  private static final Set<String> BOOLEAN_FALSES = new HashSet<>(Arrays.asList("false", "no", "nein", "n", "aus", "off", "disabled"));

  /* BYTE */
  
  public static byte min(@NotNull byte[] buffer) {
    if (buffer.length > 0) {
      var result = buffer[0];
      for (var i = 1; i < buffer.length; i++) {
        result = (byte) Math.min(result, buffer[i]);
      }
      return result;
    } else {
      throw new KclException("API misuse. Buffer must have at least one element.");
    }
  }

  public static byte max(@NotNull byte[] buffer) {
    if (buffer.length > 0) {
      var result = buffer[0];
      for (var i = 1; i < buffer.length; i++) {
        result = (byte) Math.max(result, buffer[i]);
      }
      return result;
    } else {
      throw new KclException("API misuse. Buffer must have at least one element.");
    }
  }
  
  public static boolean compare(@NotNull byte[] buffer, @NotNull byte[] sequence) {
    return compare(buffer, sequence, 0);
  }

  public static boolean compare(@NotNull byte[] buffer, @NotNull byte[] sequence, int pos) {
    if (sequence.length == 0) {
      return true;
    }
    var maxPos = buffer.length - sequence.length;
    if ((maxPos >= 0) && (pos <= maxPos)) {
      for (int i = 0, j = pos; i < sequence.length; i++, j++) {
        if (buffer[j] != sequence[i]) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  public static int find(@NotNull byte[] buffer, byte value) {
    return find(buffer, value, 0);
  }

  public static int find(@NotNull byte[] buffer, byte value, int pos) {
    for (var i = pos; i < buffer.length; i++) {
      if (buffer[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static @Min(-1) int indexOf(@NotNull byte[] buffer, @NotNull byte[] sequence) {
    return indexOf(buffer, sequence, 0);
  }
  
  public static @Min(-1) int indexOf(@NotNull byte[] buffer, @NotNull byte[] sequence, @Min(0) int pos) {
    if (buffer.length >= sequence.length) {
      var maxPos = buffer.length - sequence.length;
      if (maxPos >= pos) {
        var idx = find(buffer, sequence[0], pos);
        while (idx != -1) {
          if (compare(buffer, sequence, idx)) {
            return idx;
          }
          idx = find(buffer, sequence[0], idx + 1);
        }
      }
    }
    return -1;
  }

  public static @Min(-1) int lastIndexOf(@NotNull byte[] buffer, @NotNull byte[] sequence) {
    if (buffer.length >= sequence.length) {
      var maxPos = buffer.length - sequence.length;
      if (maxPos >= 0) {
        // @todo [06-JUN-2020:KASI]   scan from the end
        var idx  = find(buffer, sequence[0]);
        var lidx = -1;
        while (idx != -1) {
          if (compare(buffer, sequence, idx)) {
            lidx = idx;
          }
          idx = find(buffer, sequence[0], idx + 1);
        }
        return lidx;
      }
    }
    return -1;
  }

  public static @NotNull List<Byte> toList(@NotNull byte[] buffer) {
    var result = new ArrayList<Byte>(buffer.length);
    for (var element : buffer) {
      result.add(element);
    }
    return result;
  }

  public static @NotNull Byte[] toObjectArray(@NotNull byte[] buffer) {
    var result = new Byte[buffer.length];
    for (var i = 0; i < buffer.length; i++) {
      result[i] = buffer[i];
    }
    return result;  
  }

  public static @NotNull byte[] toPrimitiveArray(@NotNull Byte[] buffer) {
    var result = new byte[buffer.length];
    for (var i = 0; i < buffer.length; i++) {
      result[i] = buffer[i];
    }
    return result;
  }

  public static @NotNull byte[] toPrimitiveArrayByte(@NotNull List<Byte> buffer) {
    var result = new byte[buffer.size()];
    for (var i = 0; i < buffer.size(); i++) {
      result[i] = buffer.get(i);
    }
    return result;
  }

  public static @NotNull byte[] concat(@NotNull byte[] ... buffers) {
    if (buffers.length == 0) {
      return Empty.NO_BYTES;
    }
    if (buffers.length == 1) {
      return Arrays.copyOf(buffers[0], buffers[0].length);
    }
    var total = 0;
    for (var buffer : buffers) {
      total += buffer.length;
    }
    var result = new byte[total];
    var pos    = 0;
    for (var buffer : buffers) {
      System.arraycopy(buffer, 0, result, pos, buffer.length);
      pos += buffer.length;
    }
    return result;
  }
  
  public static @NotNull byte[] insert(@NotNull byte[] buffer, @NotNull byte[] sequence) {
    return concat(sequence, buffer);
  }

  public static @NotNull byte[] insert(@NotNull byte[] buffer, @NotNull byte[] sequence, int offset) {
    if (offset == 0) {
      return concat(sequence, buffer);
    }
    if (offset >= buffer.length) {
      throw new KclException("Cannot insert at position %d into buffer of length %d", offset, buffer.length);
    }
    var total  = buffer.length + sequence.length;
    var result = new byte[total];
    System.arraycopy(buffer, 0, result, 0, offset);
    System.arraycopy(sequence, 0, result, offset, sequence.length);
    System.arraycopy(buffer, offset, result, offset + sequence.length, buffer.length - offset);
    return result;
  }

  /* SHORT */
  
  public static short min(@NotNull short[] buffer) {
    if (buffer.length > 0) {
      var result = buffer[0];
      for (var i = 1; i < buffer.length; i++) {
        result = (short) Math.min(result, buffer[i]);
      }
      return result;
    } else {
      throw new KclException("API misuse. Buffer must have at least one element.");
    }
  }

  public static short max(@NotNull short[] buffer) {
    if (buffer.length > 0) {
      var result = buffer[0];
      for (var i = 1; i < buffer.length; i++) {
        result = (short) Math.max(result, buffer[i]);
      }
      return result;
    } else {
      throw new KclException("API misuse. Buffer must have at least one element.");
    }
  }
  
  public static boolean compare(@NotNull short[] buffer, @NotNull short[] sequence) {
    return compare(buffer, sequence, 0);
  }

  public static boolean compare(@NotNull short[] buffer, @NotNull short[] sequence, int pos) {
    if (sequence.length == 0) {
      return true;
    }
    var maxPos = buffer.length - sequence.length;
    if ((maxPos >= 0) && (pos <= maxPos)) {
      for (int i = 0, j = pos; i < sequence.length; i++, j++) {
        if (buffer[j] != sequence[i]) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  public static int find(@NotNull short[] buffer, short value) {
    return find(buffer, value, 0);
  }

  public static int find(@NotNull short[] buffer, short value, int pos) {
    for (var i = pos; i < buffer.length; i++) {
      if (buffer[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static @Min(-1) int indexOf(@NotNull short[] buffer, @NotNull short[] sequence) {
    return indexOf(buffer, sequence, 0);
  }
  
  public static @Min(-1) int indexOf(@NotNull short[] buffer, @NotNull short[] sequence, @Min(0) int pos) {
    if (buffer.length >= sequence.length) {
      var maxPos = buffer.length - sequence.length;
      if (maxPos >= pos) {
        var idx = find(buffer, sequence[0], pos);
        while (idx != -1) {
          if (compare(buffer, sequence, idx)) {
            return idx;
          }
          idx = find(buffer, sequence[0], idx + 1);
        }
      }
    }
    return -1;
  }

  public static @Min(-1) int lastIndexOf(@NotNull short[] buffer, @NotNull short[] sequence) {
    if (buffer.length >= sequence.length) {
      var maxPos = buffer.length - sequence.length;
      if (maxPos >= 0) {
        // @todo [06-JUN-2020:KASI]   scan from the end
        var idx  = find(buffer, sequence[0]);
        var lidx = -1;
        while (idx != -1) {
          if (compare(buffer, sequence, idx)) {
            lidx = idx;
          }
          idx = find(buffer, sequence[0], idx + 1);
        }
        return lidx;
      }
    }
    return -1;
  }

  public static @NotNull List<Short> toList(@NotNull short[] buffer) {
    var result = new ArrayList<Short>(buffer.length);
    for (var element : buffer) {
      result.add(element);
    }
    return result;
  }

  public static @NotNull Short[] toObjectArray(@NotNull short[] buffer) {
    var result = new Short[buffer.length];
    for (var i = 0; i < buffer.length; i++) {
      result[i] = buffer[i];
    }
    return result;  
  }

  public static @NotNull short[] toPrimitiveArray(@NotNull Short[] buffer) {
    var result = new short[buffer.length];
    for (var i = 0; i < buffer.length; i++) {
      result[i] = buffer[i];
    }
    return result;
  }

  public static @NotNull short[] toPrimitiveArrayShort(@NotNull List<Short> buffer) {
    var result = new short[buffer.size()];
    for (var i = 0; i < buffer.size(); i++) {
      result[i] = buffer.get(i);
    }
    return result;
  }

  public static @NotNull short[] concat(@NotNull short[] ... buffers) {
    if (buffers.length == 0) {
      return Empty.NO_SHORTS;
    }
    if (buffers.length == 1) {
      return Arrays.copyOf(buffers[0], buffers[0].length);
    }
    var total = 0;
    for (var buffer : buffers) {
      total += buffer.length;
    }
    var result = new short[total];
    var pos    = 0;
    for (var buffer : buffers) {
      System.arraycopy(buffer, 0, result, pos, buffer.length);
      pos += buffer.length;
    }
    return result;
  }
  
  public static @NotNull short[] insert(@NotNull short[] buffer, @NotNull short[] sequence) {
    return concat(sequence, buffer);
  }

  public static @NotNull short[] insert(@NotNull short[] buffer, @NotNull short[] sequence, int offset) {
    if (offset == 0) {
      return concat(sequence, buffer);
    }
    if (offset >= buffer.length) {
      throw new KclException("Cannot insert at position %d into buffer of length %d", offset, buffer.length);
    }
    var total  = buffer.length + sequence.length;
    var result = new short[total];
    System.arraycopy(buffer, 0, result, 0, offset);
    System.arraycopy(sequence, 0, result, offset, sequence.length);
    System.arraycopy(buffer, offset, result, offset + sequence.length, buffer.length - offset);
    return result;
  }

  /* INTEGER */
  
  public static int min(@NotNull int[] buffer) {
    if (buffer.length > 0) {
      var result = buffer[0];
      for (var i = 1; i < buffer.length; i++) {
        result = Math.min(result, buffer[i]);
      }
      return result;
    } else {
      throw new KclException("API misuse. Buffer must have at least one element.");
    }
  }

  public static int max(@NotNull int[] buffer) {
    if (buffer.length > 0) {
      var result = buffer[0];
      for (var i = 1; i < buffer.length; i++) {
        result = Math.max(result, buffer[i]);
      }
      return result;
    } else {
      throw new KclException("API misuse. Buffer must have at least one element.");
    }
  }
  
  public static boolean compare(@NotNull int[] buffer, @NotNull int[] sequence) {
    return compare(buffer, sequence, 0);
  }

  public static boolean compare(@NotNull int[] buffer, @NotNull int[] sequence, int pos) {
    if (sequence.length == 0) {
      return true;
    }
    var maxPos = buffer.length - sequence.length;
    if ((maxPos >= 0) && (pos <= maxPos)) {
      for (int i = 0, j = pos; i < sequence.length; i++, j++) {
        if (buffer[j] != sequence[i]) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  public static int find(@NotNull int[] buffer, int value) {
    return find(buffer, value, 0);
  }

  public static int find(@NotNull int[] buffer, int value, int pos) {
    for (var i = pos; i < buffer.length; i++) {
      if (buffer[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static @Min(-1) int indexOf(@NotNull int[] buffer, @NotNull int[] sequence) {
    return indexOf(buffer, sequence, 0);
  }
  
  public static @Min(-1) int indexOf(@NotNull int[] buffer, @NotNull int[] sequence, @Min(0) int pos) {
    if (buffer.length >= sequence.length) {
      var maxPos = buffer.length - sequence.length;
      if (maxPos >= pos) {
        var idx = find(buffer, sequence[0], pos);
        while (idx != -1) {
          if (compare(buffer, sequence, idx)) {
            return idx;
          }
          idx = find(buffer, sequence[0], idx + 1);
        }
      }
    }
    return -1;
  }

  public static @Min(-1) int lastIndexOf(@NotNull int[] buffer, @NotNull int[] sequence) {
    if (buffer.length >= sequence.length) {
      var maxPos = buffer.length - sequence.length;
      if (maxPos >= 0) {
        // @todo [06-JUN-2020:KASI]   scan from the end
        var idx  = find(buffer, sequence[0]);
        var lidx = -1;
        while (idx != -1) {
          if (compare(buffer, sequence, idx)) {
            lidx = idx;
          }
          idx = find(buffer, sequence[0], idx + 1);
        }
        return lidx;
      }
    }
    return -1;
  }

  public static @NotNull List<Integer> toList(@NotNull int[] buffer) {
    var result = new ArrayList<Integer>(buffer.length);
    for (var element : buffer) {
      result.add(element);
    }
    return result;
  }

  public static @NotNull Integer[] toObjectArray(@NotNull int[] buffer) {
    var result = new Integer[buffer.length];
    for (var i = 0; i < buffer.length; i++) {
      result[i] = buffer[i];
    }
    return result;  
  }

  public static @NotNull int[] toPrimitiveArray(@NotNull Integer[] buffer) {
    var result = new int[buffer.length];
    for (var i = 0; i < buffer.length; i++) {
      result[i] = buffer[i];
    }
    return result;
  }

  public static @NotNull int[] toPrimitiveArrayInteger(@NotNull List<Integer> buffer) {
    var result = new int[buffer.size()];
    for (var i = 0; i < buffer.size(); i++) {
      result[i] = buffer.get(i);
    }
    return result;
  }

  public static @NotNull int[] concat(@NotNull int[] ... buffers) {
    if (buffers.length == 0) {
      return Empty.NO_INTS;
    }
    if (buffers.length == 1) {
      return Arrays.copyOf(buffers[0], buffers[0].length);
    }
    var total = 0;
    for (var buffer : buffers) {
      total += buffer.length;
    }
    var result = new int[total];
    var pos    = 0;
    for (var buffer : buffers) {
      System.arraycopy(buffer, 0, result, pos, buffer.length);
      pos += buffer.length;
    }
    return result;
  }
  
  public static @NotNull int[] insert(@NotNull int[] buffer, @NotNull int[] sequence) {
    return concat(sequence, buffer);
  }

  public static @NotNull int[] insert(@NotNull int[] buffer, @NotNull int[] sequence, int offset) {
    if (offset == 0) {
      return concat(sequence, buffer);
    }
    if (offset >= buffer.length) {
      throw new KclException("Cannot insert at position %d into buffer of length %d", offset, buffer.length);
    }
    var total  = buffer.length + sequence.length;
    var result = new int[total];
    System.arraycopy(buffer, 0, result, 0, offset);
    System.arraycopy(sequence, 0, result, offset, sequence.length);
    System.arraycopy(buffer, offset, result, offset + sequence.length, buffer.length - offset);
    return result;
  }

  /* LONG */
  
  public static long min(@NotNull long[] buffer) {
    if (buffer.length > 0) {
      var result = buffer[0];
      for (var i = 1; i < buffer.length; i++) {
        result = Math.min(result, buffer[i]);
      }
      return result;
    } else {
      throw new KclException("API misuse. Buffer must have at least one element.");
    }
  }

  public static long max(@NotNull long[] buffer) {
    if (buffer.length > 0) {
      var result = buffer[0];
      for (var i = 1; i < buffer.length; i++) {
        result = Math.max(result, buffer[i]);
      }
      return result;
    } else {
      throw new KclException("API misuse. Buffer must have at least one element.");
    }
  }
  
  public static boolean compare(@NotNull long[] buffer, @NotNull long[] sequence) {
    return compare(buffer, sequence, 0);
  }

  public static boolean compare(@NotNull long[] buffer, @NotNull long[] sequence, int pos) {
    if (sequence.length == 0) {
      return true;
    }
    var maxPos = buffer.length - sequence.length;
    if ((maxPos >= 0) && (pos <= maxPos)) {
      for (int i = 0, j = pos; i < sequence.length; i++, j++) {
        if (buffer[j] != sequence[i]) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  public static int find(@NotNull long[] buffer, long value) {
    return find(buffer, value, 0);
  }

  public static int find(@NotNull long[] buffer, long value, int pos) {
    for (var i = pos; i < buffer.length; i++) {
      if (buffer[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static @Min(-1) int indexOf(@NotNull long[] buffer, @NotNull long[] sequence) {
    return indexOf(buffer, sequence, 0);
  }
  
  public static @Min(-1) int indexOf(@NotNull long[] buffer, @NotNull long[] sequence, @Min(0) int pos) {
    if (buffer.length >= sequence.length) {
      var maxPos = buffer.length - sequence.length;
      if (maxPos >= pos) {
        var idx = find(buffer, sequence[0], pos);
        while (idx != -1) {
          if (compare(buffer, sequence, idx)) {
            return idx;
          }
          idx = find(buffer, sequence[0], idx + 1);
        }
      }
    }
    return -1;
  }

  public static @Min(-1) int lastIndexOf(@NotNull long[] buffer, @NotNull long[] sequence) {
    if (buffer.length >= sequence.length) {
      var maxPos = buffer.length - sequence.length;
      if (maxPos >= 0) {
        // @todo [06-JUN-2020:KASI]   scan from the end
        var idx  = find(buffer, sequence[0]);
        var lidx = -1;
        while (idx != -1) {
          if (compare(buffer, sequence, idx)) {
            lidx = idx;
          }
          idx = find(buffer, sequence[0], idx + 1);
        }
        return lidx;
      }
    }
    return -1;
  }

  public static @NotNull List<Long> toList(@NotNull long[] buffer) {
    var result = new ArrayList<Long>(buffer.length);
    for (var element : buffer) {
      result.add(element);
    }
    return result;
  }

  public static @NotNull Long[] toObjectArray(@NotNull long[] buffer) {
    var result = new Long[buffer.length];
    for (var i = 0; i < buffer.length; i++) {
      result[i] = buffer[i];
    }
    return result;  
  }

  public static @NotNull long[] toPrimitiveArray(@NotNull Long[] buffer) {
    var result = new long[buffer.length];
    for (var i = 0; i < buffer.length; i++) {
      result[i] = buffer[i];
    }
    return result;
  }

  public static @NotNull long[] toPrimitiveArrayLong(@NotNull List<Long> buffer) {
    var result = new long[buffer.size()];
    for (var i = 0; i < buffer.size(); i++) {
      result[i] = buffer.get(i);
    }
    return result;
  }

  public static @NotNull long[] concat(@NotNull long[] ... buffers) {
    if (buffers.length == 0) {
      return Empty.NO_LONGS;
    }
    if (buffers.length == 1) {
      return Arrays.copyOf(buffers[0], buffers[0].length);
    }
    var total = 0;
    for (var buffer : buffers) {
      total += buffer.length;
    }
    var result = new long[total];
    var pos    = 0;
    for (var buffer : buffers) {
      System.arraycopy(buffer, 0, result, pos, buffer.length);
      pos += buffer.length;
    }
    return result;
  }
  
  public static @NotNull long[] insert(@NotNull long[] buffer, @NotNull long[] sequence) {
    return concat(sequence, buffer);
  }

  public static @NotNull long[] insert(@NotNull long[] buffer, @NotNull long[] sequence, int offset) {
    if (offset == 0) {
      return concat(sequence, buffer);
    }
    if (offset >= buffer.length) {
      throw new KclException("Cannot insert at position %d into buffer of length %d", offset, buffer.length);
    }
    var total  = buffer.length + sequence.length;
    var result = new long[total];
    System.arraycopy(buffer, 0, result, 0, offset);
    System.arraycopy(sequence, 0, result, offset, sequence.length);
    System.arraycopy(buffer, offset, result, offset + sequence.length, buffer.length - offset);
    return result;
  }

  /* FLOAT */
  
  public static float min(@NotNull float[] buffer) {
    if (buffer.length > 0) {
      var result = buffer[0];
      for (var i = 1; i < buffer.length; i++) {
        result = Math.min(result, buffer[i]);
      }
      return result;
    } else {
      throw new KclException("API misuse. Buffer must have at least one element.");
    }
  }

  public static float max(@NotNull float[] buffer) {
    if (buffer.length > 0) {
      var result = buffer[0];
      for (var i = 1; i < buffer.length; i++) {
        result = Math.max(result, buffer[i]);
      }
      return result;
    } else {
      throw new KclException("API misuse. Buffer must have at least one element.");
    }
  }
  
  public static boolean compare(@NotNull float[] buffer, @NotNull float[] sequence) {
    return compare(buffer, sequence, 0);
  }

  public static boolean compare(@NotNull float[] buffer, @NotNull float[] sequence, int pos) {
    if (sequence.length == 0) {
      return true;
    }
    var maxPos = buffer.length - sequence.length;
    if ((maxPos >= 0) && (pos <= maxPos)) {
      for (int i = 0, j = pos; i < sequence.length; i++, j++) {
        if (buffer[j] != sequence[i]) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  public static int find(@NotNull float[] buffer, float value) {
    return find(buffer, value, 0);
  }

  public static int find(@NotNull float[] buffer, float value, int pos) {
    for (var i = pos; i < buffer.length; i++) {
      if (buffer[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static @Min(-1) int indexOf(@NotNull float[] buffer, @NotNull float[] sequence) {
    return indexOf(buffer, sequence, 0);
  }
  
  public static @Min(-1) int indexOf(@NotNull float[] buffer, @NotNull float[] sequence, @Min(0) int pos) {
    if (buffer.length >= sequence.length) {
      var maxPos = buffer.length - sequence.length;
      if (maxPos >= pos) {
        var idx = find(buffer, sequence[0], pos);
        while (idx != -1) {
          if (compare(buffer, sequence, idx)) {
            return idx;
          }
          idx = find(buffer, sequence[0], idx + 1);
        }
      }
    }
    return -1;
  }

  public static @Min(-1) int lastIndexOf(@NotNull float[] buffer, @NotNull float[] sequence) {
    if (buffer.length >= sequence.length) {
      var maxPos = buffer.length - sequence.length;
      if (maxPos >= 0) {
        // @todo [06-JUN-2020:KASI]   scan from the end
        var idx  = find(buffer, sequence[0]);
        var lidx = -1;
        while (idx != -1) {
          if (compare(buffer, sequence, idx)) {
            lidx = idx;
          }
          idx = find(buffer, sequence[0], idx + 1);
        }
        return lidx;
      }
    }
    return -1;
  }

  public static @NotNull List<Float> toList(@NotNull float[] buffer) {
    var result = new ArrayList<Float>(buffer.length);
    for (var element : buffer) {
      result.add(element);
    }
    return result;
  }

  public static @NotNull Float[] toObjectArray(@NotNull float[] buffer) {
    var result = new Float[buffer.length];
    for (var i = 0; i < buffer.length; i++) {
      result[i] = buffer[i];
    }
    return result;  
  }

  public static @NotNull float[] toPrimitiveArray(@NotNull Float[] buffer) {
    var result = new float[buffer.length];
    for (var i = 0; i < buffer.length; i++) {
      result[i] = buffer[i];
    }
    return result;
  }

  public static @NotNull float[] toPrimitiveArrayFloat(@NotNull List<Float> buffer) {
    var result = new float[buffer.size()];
    for (var i = 0; i < buffer.size(); i++) {
      result[i] = buffer.get(i);
    }
    return result;
  }

  public static @NotNull float[] concat(@NotNull float[] ... buffers) {
    if (buffers.length == 0) {
      return Empty.NO_FLOATS;
    }
    if (buffers.length == 1) {
      return Arrays.copyOf(buffers[0], buffers[0].length);
    }
    var total = 0;
    for (var buffer : buffers) {
      total += buffer.length;
    }
    var result = new float[total];
    var pos    = 0;
    for (var buffer : buffers) {
      System.arraycopy(buffer, 0, result, pos, buffer.length);
      pos += buffer.length;
    }
    return result;
  }
  
  public static @NotNull float[] insert(@NotNull float[] buffer, @NotNull float[] sequence) {
    return concat(sequence, buffer);
  }

  public static @NotNull float[] insert(@NotNull float[] buffer, @NotNull float[] sequence, int offset) {
    if (offset == 0) {
      return concat(sequence, buffer);
    }
    if (offset >= buffer.length) {
      throw new KclException("Cannot insert at position %d into buffer of length %d", offset, buffer.length);
    }
    var total  = buffer.length + sequence.length;
    var result = new float[total];
    System.arraycopy(buffer, 0, result, 0, offset);
    System.arraycopy(sequence, 0, result, offset, sequence.length);
    System.arraycopy(buffer, offset, result, offset + sequence.length, buffer.length - offset);
    return result;
  }

  /* DOUBLE */
  
  public static double min(@NotNull double[] buffer) {
    if (buffer.length > 0) {
      var result = buffer[0];
      for (var i = 1; i < buffer.length; i++) {
        result = Math.min(result, buffer[i]);
      }
      return result;
    } else {
      throw new KclException("API misuse. Buffer must have at least one element.");
    }
  }

  public static double max(@NotNull double[] buffer) {
    if (buffer.length > 0) {
      var result = buffer[0];
      for (var i = 1; i < buffer.length; i++) {
        result = Math.max(result, buffer[i]);
      }
      return result;
    } else {
      throw new KclException("API misuse. Buffer must have at least one element.");
    }
  }
  
  public static boolean compare(@NotNull double[] buffer, @NotNull double[] sequence) {
    return compare(buffer, sequence, 0);
  }

  public static boolean compare(@NotNull double[] buffer, @NotNull double[] sequence, int pos) {
    if (sequence.length == 0) {
      return true;
    }
    var maxPos = buffer.length - sequence.length;
    if ((maxPos >= 0) && (pos <= maxPos)) {
      for (int i = 0, j = pos; i < sequence.length; i++, j++) {
        if (buffer[j] != sequence[i]) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  public static int find(@NotNull double[] buffer, double value) {
    return find(buffer, value, 0);
  }

  public static int find(@NotNull double[] buffer, double value, int pos) {
    for (var i = pos; i < buffer.length; i++) {
      if (buffer[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static @Min(-1) int indexOf(@NotNull double[] buffer, @NotNull double[] sequence) {
    return indexOf(buffer, sequence, 0);
  }
  
  public static @Min(-1) int indexOf(@NotNull double[] buffer, @NotNull double[] sequence, @Min(0) int pos) {
    if (buffer.length >= sequence.length) {
      var maxPos = buffer.length - sequence.length;
      if (maxPos >= pos) {
        var idx = find(buffer, sequence[0], pos);
        while (idx != -1) {
          if (compare(buffer, sequence, idx)) {
            return idx;
          }
          idx = find(buffer, sequence[0], idx + 1);
        }
      }
    }
    return -1;
  }

  public static @Min(-1) int lastIndexOf(@NotNull double[] buffer, @NotNull double[] sequence) {
    if (buffer.length >= sequence.length) {
      var maxPos = buffer.length - sequence.length;
      if (maxPos >= 0) {
        // @todo [06-JUN-2020:KASI]   scan from the end
        var idx  = find(buffer, sequence[0]);
        var lidx = -1;
        while (idx != -1) {
          if (compare(buffer, sequence, idx)) {
            lidx = idx;
          }
          idx = find(buffer, sequence[0], idx + 1);
        }
        return lidx;
      }
    }
    return -1;
  }

  public static @NotNull List<Double> toList(@NotNull double[] buffer) {
    var result = new ArrayList<Double>(buffer.length);
    for (var element : buffer) {
      result.add(element);
    }
    return result;
  }

  public static @NotNull Double[] toObjectArray(@NotNull double[] buffer) {
    var result = new Double[buffer.length];
    for (var i = 0; i < buffer.length; i++) {
      result[i] = buffer[i];
    }
    return result;  
  }

  public static @NotNull double[] toPrimitiveArray(@NotNull Double[] buffer) {
    var result = new double[buffer.length];
    for (var i = 0; i < buffer.length; i++) {
      result[i] = buffer[i];
    }
    return result;
  }

  public static @NotNull double[] toPrimitiveArrayDouble(@NotNull List<Double> buffer) {
    var result = new double[buffer.size()];
    for (var i = 0; i < buffer.size(); i++) {
      result[i] = buffer.get(i);
    }
    return result;
  }

  public static @NotNull double[] concat(@NotNull double[] ... buffers) {
    if (buffers.length == 0) {
      return Empty.NO_DOUBLES;
    }
    if (buffers.length == 1) {
      return Arrays.copyOf(buffers[0], buffers[0].length);
    }
    var total = 0;
    for (var buffer : buffers) {
      total += buffer.length;
    }
    var result = new double[total];
    var pos    = 0;
    for (var buffer : buffers) {
      System.arraycopy(buffer, 0, result, pos, buffer.length);
      pos += buffer.length;
    }
    return result;
  }
  
  public static @NotNull double[] insert(@NotNull double[] buffer, @NotNull double[] sequence) {
    return concat(sequence, buffer);
  }

  public static @NotNull double[] insert(@NotNull double[] buffer, @NotNull double[] sequence, int offset) {
    if (offset == 0) {
      return concat(sequence, buffer);
    }
    if (offset >= buffer.length) {
      throw new KclException("Cannot insert at position %d into buffer of length %d", offset, buffer.length);
    }
    var total  = buffer.length + sequence.length;
    var result = new double[total];
    System.arraycopy(buffer, 0, result, 0, offset);
    System.arraycopy(sequence, 0, result, offset, sequence.length);
    System.arraycopy(buffer, offset, result, offset + sequence.length, buffer.length - offset);
    return result;
  }
  
  /* CHAR */
  
  public static boolean compare(@NotNull char[] buffer, @NotNull char[] sequence) {
    return compare(buffer, sequence, 0);
  }

  public static boolean compare(@NotNull char[] buffer, @NotNull char[] sequence, int pos) {
    if (sequence.length == 0) {
      return true;
    }
    var maxPos = buffer.length - sequence.length;
    if ((maxPos >= 0) && (pos <= maxPos)) {
      for (int i = 0, j = pos; i < sequence.length; i++, j++) {
        if (buffer[j] != sequence[i]) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  public static int find(@NotNull char[] buffer, char value) {
    return find(buffer, value, 0);
  }

  public static int find(@NotNull char[] buffer, char value, int pos) {
    for (var i = pos; i < buffer.length; i++) {
      if (buffer[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static @Min(-1) int indexOf(@NotNull char[] buffer, @NotNull char[] sequence) {
    return indexOf(buffer, sequence, 0);
  }
  
  public static @Min(-1) int indexOf(@NotNull char[] buffer, @NotNull char[] sequence, @Min(0) int pos) {
    if (buffer.length >= sequence.length) {
      var maxPos = buffer.length - sequence.length;
      if (maxPos >= pos) {
        var idx = find(buffer, sequence[0], pos);
        while (idx != -1) {
          if (compare(buffer, sequence, idx)) {
            return idx;
          }
          idx = find(buffer, sequence[0], idx + 1);
        }
      }
    }
    return -1;
  }

  public static @Min(-1) int lastIndexOf(@NotNull char[] buffer, @NotNull char[] sequence) {
    if (buffer.length >= sequence.length) {
      var maxPos = buffer.length - sequence.length;
      if (maxPos >= 0) {
        // @todo [06-JUN-2020:KASI]   scan from the end
        var idx  = find(buffer, sequence[0]);
        var lidx = -1;
        while (idx != -1) {
          if (compare(buffer, sequence, idx)) {
            lidx = idx;
          }
          idx = find(buffer, sequence[0], idx + 1);
        }
        return lidx;
      }
    }
    return -1;
  }

  public static @NotNull List<Character> toList(@NotNull char[] buffer) {
    var result = new ArrayList<Character>(buffer.length);
    for (var element : buffer) {
      result.add(element);
    }
    return result;
  }

  public static @NotNull Character[] toObjectArray(@NotNull char[] buffer) {
    var result = new Character[buffer.length];
    for (var i = 0; i < buffer.length; i++) {
      result[i] = buffer[i];
    }
    return result;  
  }

  public static @NotNull char[] toPrimitiveArray(@NotNull Character[] buffer) {
    var result = new char[buffer.length];
    for (var i = 0; i < buffer.length; i++) {
      result[i] = buffer[i];
    }
    return result;
  }

  public static @NotNull char[] toPrimitiveArrayCharacter(@NotNull List<Character> buffer) {
    var result = new char[buffer.size()];
    for (var i = 0; i < buffer.size(); i++) {
      result[i] = buffer.get(i);
    }
    return result;
  }

  public static @NotNull char[] concat(@NotNull char[] ... buffers) {
    if (buffers.length == 0) {
      return Empty.NO_CHARS;
    }
    if (buffers.length == 1) {
      return Arrays.copyOf(buffers[0], buffers[0].length);
    }
    var total = 0;
    for (var buffer : buffers) {
      total += buffer.length;
    }
    var result = new char[total];
    var pos    = 0;
    for (var buffer : buffers) {
      System.arraycopy(buffer, 0, result, pos, buffer.length);
      pos += buffer.length;
    }
    return result;
  }
  
  public static @NotNull char[] insert(@NotNull char[] buffer, @NotNull char[] sequence) {
    return concat(sequence, buffer);
  }

  public static @NotNull char[] insert(@NotNull char[] buffer, @NotNull char[] sequence, int offset) {
    if (offset == 0) {
      return concat(sequence, buffer);
    }
    if (offset >= buffer.length) {
      throw new KclException("Cannot insert at position %d into buffer of length %d", offset, buffer.length);
    }
    var total  = buffer.length + sequence.length;
    var result = new char[total];
    System.arraycopy(buffer, 0, result, 0, offset);
    System.arraycopy(sequence, 0, result, offset, sequence.length);
    System.arraycopy(buffer, offset, result, offset + sequence.length, buffer.length - offset);
    return result;
  }

  /* BOOLEAN */
  
  public static boolean or(@NotNull boolean[] buffer) {
    if (buffer.length > 0) {
      for (boolean b : buffer) {
        if (b) {
          return true;
        }
      }
      return false;
    } else {
      throw new KclException("API misuse. Buffer must have at least one element.");
    }
  }

  public static boolean and(@NotNull boolean[] buffer) {
    if (buffer.length > 0) {
      for (boolean b : buffer) {
        if (!b) {
          return false;
        }
      }
      return true;
    } else {
      throw new KclException("API misuse. Buffer must have at least one element.");
    }
  }
  
  public static boolean compare(@NotNull boolean[] buffer, @NotNull boolean[] sequence) {
    return compare(buffer, sequence, 0);
  }

  public static boolean compare(@NotNull boolean[] buffer, @NotNull boolean[] sequence, int pos) {
    if (sequence.length == 0) {
      return true;
    }
    var maxPos = buffer.length - sequence.length;
    if ((maxPos >= 0) && (pos <= maxPos)) {
      for (int i = 0, j = pos; i < sequence.length; i++, j++) {
        if (buffer[j] != sequence[i]) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  public static int find(@NotNull boolean[] buffer, boolean value) {
    return find(buffer, value, 0);
  }

  public static int find(@NotNull boolean[] buffer, boolean value, int pos) {
    for (var i = pos; i < buffer.length; i++) {
      if (buffer[i] == value) {
        return i;
      }
    }
    return -1;
  }

  public static @Min(-1) int indexOf(@NotNull boolean[] buffer, @NotNull boolean[] sequence) {
    return indexOf(buffer, sequence, 0);
  }
  
  public static @Min(-1) int indexOf(@NotNull boolean[] buffer, @NotNull boolean[] sequence, @Min(0) int pos) {
    if (buffer.length >= sequence.length) {
      var maxPos = buffer.length - sequence.length;
      if (maxPos >= pos) {
        var idx = find(buffer, sequence[0], pos);
        while (idx != -1) {
          if (compare(buffer, sequence, idx)) {
            return idx;
          }
          idx = find(buffer, sequence[0], idx + 1);
        }
      }
    }
    return -1;
  }

  public static @Min(-1) int lastIndexOf(@NotNull boolean[] buffer, @NotNull boolean[] sequence) {
    if (buffer.length >= sequence.length) {
      var maxPos = buffer.length - sequence.length;
      if (maxPos >= 0) {
        // @todo [06-JUN-2020:KASI]   scan from the end
        var idx  = find(buffer, sequence[0]);
        var lidx = -1;
        while (idx != -1) {
          if (compare(buffer, sequence, idx)) {
            lidx = idx;
          }
          idx = find(buffer, sequence[0], idx + 1);
        }
        return lidx;
      }
    }
    return -1;
  }

  public static @NotNull List<Boolean> toList(@NotNull boolean[] buffer) {
    var result = new ArrayList<Boolean>(buffer.length);
    for (var element : buffer) {
      result.add(element);
    }
    return result;
  }

  public static @NotNull Boolean[] toObjectArray(@NotNull boolean[] buffer) {
    var result = new Boolean[buffer.length];
    for (var i = 0; i < buffer.length; i++) {
      result[i] = buffer[i];
    }
    return result;  
  }

  public static @NotNull boolean[] toPrimitiveArray(@NotNull Boolean[] buffer) {
    var result = new boolean[buffer.length];
    for (var i = 0; i < buffer.length; i++) {
      result[i] = buffer[i];
    }
    return result;
  }

  public static @NotNull boolean[] toPrimitiveArrayBoolean(@NotNull List<Boolean> buffer) {
    var result = new boolean[buffer.size()];
    for (var i = 0; i < buffer.size(); i++) {
      result[i] = buffer.get(i);
    }
    return result;
  }

  public static @NotNull boolean[] concat(@NotNull boolean[] ... buffers) {
    if (buffers.length == 0) {
      return Empty.NO_BOOLEANS;
    }
    if (buffers.length == 1) {
      return Arrays.copyOf(buffers[0], buffers[0].length);
    }
    var total = 0;
    for (var buffer : buffers) {
      total += buffer.length;
    }
    var result = new boolean[total];
    var pos    = 0;
    for (var buffer : buffers) {
      System.arraycopy(buffer, 0, result, pos, buffer.length);
      pos += buffer.length;
    }
    return result;
  }
  
  public static @NotNull boolean[] insert(@NotNull boolean[] buffer, @NotNull boolean[] sequence) {
    return concat(sequence, buffer);
  }

  public static @NotNull boolean[] insert(@NotNull boolean[] buffer, @NotNull boolean[] sequence, int offset) {
    if (offset == 0) {
      return concat(sequence, buffer);
    }
    if (offset >= buffer.length) {
      throw new KclException("Cannot insert at position %d into buffer of length %d", offset, buffer.length);
    }
    var total  = buffer.length + sequence.length;
    var result = new boolean[total];
    System.arraycopy(buffer, 0, result, 0, offset);
    System.arraycopy(sequence, 0, result, offset, sequence.length);
    System.arraycopy(buffer, offset, result, offset + sequence.length, buffer.length - offset);
    return result;
  }
  
  /* PARSING PRIMITIVE TYPES */
  
  /**
   * Interpretes a value as a boolean.
   * 
   * @param value   The value which has to be parsed. Maybe <code>null</code>.
   * 
   * @return   <code>true</code>  <=> If the supplied literal has one of the values {@link #TRUEVALUES} (case insensitive).
   *           <code>false</code> <=> All other cases.
   */
  public static boolean parseBoolean( String value ) {
    if (value != null) {
      var lower = value.toLowerCase();
      if (BOOLEAN_TRUES.contains(lower)) {
        return true;
      }
      if (BOOLEAN_FALSES.contains(lower)) {
        return false;
      }
    }
    throw new KclException("Invalid boolean value: '%s'", value);
  }

  /* SUPPLIERS */
  
  public static boolean getBoolean(@NotNull KSupplierBoolean inv) {
    try {
      return inv.get();
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static char getChar(@NotNull KSupplierChar inv) {
    try {
      return inv.get();
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static byte getByte(@NotNull KSupplierByte inv) {
    try {
      return inv.get();
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static short getShort(@NotNull KSupplierShort inv) {
    try {
      return inv.get();
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static int getInt(@NotNull KSupplierInt inv) {
    try {
      return inv.get();
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static long getLong(@NotNull KSupplierLong inv) {
    try {
      return inv.get();
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static float getFloat(@NotNull KSupplierFloat inv) {
    try {
      return inv.get();
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static double getDouble(@NotNull KSupplierDouble inv) {
    try {
      return inv.get();
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  /* CONSUMERS */
  
  public static void acceptBoolean(@NotNull KConsumerBoolean inv, boolean value) {
    try {
      inv.accept(value);
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static void acceptChar(@NotNull KConsumerChar inv, char value) {
    try {
      inv.accept(value);
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static void acceptByte(@NotNull KConsumerByte inv, byte value) {
    try {
      inv.accept(value);
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static void acceptShort(@NotNull KConsumerShort inv, short value) {
    try {
      inv.accept(value);
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static void acceptInt(@NotNull KConsumerInt inv, int value) {
    try {
      inv.accept(value);
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static void acceptLong(@NotNull KConsumerLong inv, long value) {
    try {
      inv.accept(value);
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static void acceptFloat(@NotNull KConsumerFloat inv, float value) {
    try {
      inv.accept(value);
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static void acceptDouble(@NotNull KConsumerDouble inv, double value) {
    try {
      inv.accept(value);
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  /* FUNCTIONS */
  
  public static <I> boolean applyBoolean(@NotNull KFunctionBoolean<I> inv, I input) {
    try {
      return inv.apply(input);
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static <I> char applyChar(@NotNull KFunctionChar<I> inv, I input) {
    try {
      return inv.apply(input);
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static <I> byte applyByte(@NotNull KFunctionByte<I> inv, I input) {
    try {
      return inv.apply(input);
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static <I> short applyShort(@NotNull KFunctionShort<I> inv, I input) {
    try {
      return inv.apply(input);
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static <I> int applyInt(@NotNull KFunctionInt<I> inv, I input) {
    try {
      return inv.apply(input);
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static <I> long applyLong(@NotNull KFunctionLong<I> inv, I input) {
    try {
      return inv.apply(input);
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static <I> float applyFloat(@NotNull KFunctionFloat<I> inv, I input) {
    try {
      return inv.apply(input);
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static <I> double applyDouble(@NotNull KFunctionDouble<I> inv, I input) {
    try {
      return inv.apply(input);
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

} /* ENDCLASS */
