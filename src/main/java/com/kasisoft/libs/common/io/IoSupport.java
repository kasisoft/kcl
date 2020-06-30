package com.kasisoft.libs.common.io;

import static com.kasisoft.libs.common.internal.Messages.error_failed_to_read_from;
import static com.kasisoft.libs.common.internal.Messages.error_failed_to_write_text_to;
import static com.kasisoft.libs.common.internal.Messages.error_failed_to_write_to;

import com.kasisoft.libs.common.constants.Encoding;

import com.kasisoft.libs.common.KclException;
import com.kasisoft.libs.common.functional.KConsumer;
import com.kasisoft.libs.common.functional.KFunction;
import com.kasisoft.libs.common.pools.Buckets;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface IoSupport<T> {
  
  @NotNull InputStream newInputStreamImpl(@NotNull T source) throws Exception;
  
  @NotNull OutputStream newOutputStreamImpl(@NotNull T destination) throws Exception;
  
  default @NotNull InputStream newInputStream(@NotNull T source) {
    try {
      return new BufferedInputStream(newInputStreamImpl(source));
    } catch (Exception ex) {
      throw new KclException(ex, error_failed_to_read_from, source);
    }
  }

  default @NotNull OutputStream newOutputStream(@NotNull T destination) {
    try {
      return new BufferedOutputStream(newOutputStreamImpl(destination));
    } catch (Exception ex) {
      throw new KclException(ex, error_failed_to_write_to, destination);
    }
  }

  default @NotNull Reader newReader(@NotNull T source) {
    return newReader(source, null);
  }
  
  default @NotNull Reader newReader(@NotNull T source, @Null Encoding encoding) {
    return new BufferedReader(new InputStreamReader(newInputStream(source), Encoding.getEncoding(encoding).getCharset()));
  }
  
  default @NotNull Writer newWriter(@NotNull T destination) {
    return newWriter(destination, null);
  }
  
  default @NotNull Writer newWriter(@NotNull T destination, @Null Encoding encoding) {
    return new BufferedWriter(new OutputStreamWriter(newOutputStream(destination), Encoding.getEncoding(encoding).getCharset()));
  }

  default void forInputStreamDo(@NotNull T source, @NotNull KConsumer<@NotNull InputStream> action) {
    try (var instream = newInputStream(source)) {
      action.accept(instream);
    } catch (Exception ex) {
      throw new KclException(ex, error_failed_to_read_from, source);
    }
  }
  
  default <R> @Null R forInputStream(@NotNull T source, @NotNull KFunction<@NotNull InputStream, @Null R> function) {
    try (var instream = newInputStream(source)) {
      return function.apply(instream);
    } catch (Exception ex) {
      throw new KclException(ex, error_failed_to_read_from, source);
    }
  }
  
  default void forOutputStreamDo(@NotNull T destination, @NotNull KConsumer<@NotNull OutputStream> action) {
    try (var outstream = newOutputStream(destination)) {
      action.accept(outstream);
    } catch (Exception ex) {
      throw new KclException(ex, error_failed_to_write_to, destination);
    }
  }
  
  default <R> @Null R forOutputStream(@NotNull T destination, @NotNull KFunction<@NotNull OutputStream, @Null R> function) {
    try (var outstream = newOutputStream(destination)) {
      return function.apply(outstream);
    } catch (Exception ex) {
      throw new KclException(ex, error_failed_to_write_to, destination);
    }
  }
  
  default void forReaderDo(@NotNull T source, @NotNull KConsumer<@NotNull Reader> action) {
    forReaderDo(source, null, action);
  }

  default void forReaderDo(@NotNull T source, @Null Encoding encoding, @NotNull KConsumer<@NotNull Reader> action) {
    try (var reader = newReader(source, encoding)) {
      action.accept(reader);
    } catch (Exception ex) {
      throw new KclException(ex, error_failed_to_read_from, source);
    }
  }

  default <R> @Null R forReader(@NotNull T source, @NotNull KFunction<@NotNull Reader, @Null R> function) {
    return forReader(source, null, function);
  }

  default <R> @Null R forReader(@NotNull T source, @Null Encoding encoding, @NotNull KFunction<@NotNull Reader, @Null R> function) {
    try (var reader = newReader(source, encoding)) {
      return function.apply(reader);
    } catch (Exception ex) {
      throw new KclException(ex, error_failed_to_read_from, source);
    }
  }

  default void forWriterDo(@NotNull T destination, @NotNull KConsumer<@NotNull Writer> action) {
    forWriterDo(destination, null, action);
  }

  default void forWriterDo(@NotNull T destination, @Null Encoding encoding, @NotNull KConsumer<@NotNull Writer> action) {
    try (var writer = newWriter(destination, encoding)) {
      action.accept(writer);
    } catch (Exception ex) {
      throw new KclException(ex, error_failed_to_write_to, destination);
    }
  }

  default <R> @Null R forWriter(@NotNull T destination, @NotNull KFunction<@NotNull Writer, @Null R> function) {
    return forWriter(destination, null, function);
  }

  default <R> @Null R forWriter(@NotNull T destination, @Null Encoding encoding, @NotNull KFunction<@NotNull Writer, @Null R> function) {
    try (var writer = newWriter(destination, encoding)) {
      return function.apply(writer);
    } catch (Exception ex) {
      throw new KclException(ex, error_failed_to_write_to, destination);
    }
  }

  default @NotNull byte[] loadBytes(@NotNull T source, @Min(1) int size) {
    return loadBytes(source, 0, size);
  }
  
  default @NotNull byte[] loadBytes(@NotNull T source, @Min(0) int offset, @Min(1) int size) {
    return forInputStream(source, $ -> {
      try {
        var result = new byte[size];
        IoFunctions.skip($, offset);
        $.readNBytes(result, 0, size);
        return result;
      } catch (Exception ex) {
        throw new KclException(ex, error_failed_to_read_from, source);
      }
    });
  }

  default @NotNull char[] loadChars(@NotNull T source, @Min(1) int size) {
    return loadChars(source, null, 0, size);
  }
  
  default @NotNull char[] loadChars(@NotNull T source, @Min(0) int offset, @Min(1) int size) {
    return loadChars(source, null, offset, size);
  }

  default @NotNull char[] loadChars(@NotNull T source, @Null Encoding encoding, @Min(1) int size) {
    return loadChars(source, encoding, 0, size);
  }
  
  default @NotNull char[] loadChars(@NotNull T source, @Null Encoding encoding, @Min(0) int offset, @Min(1) int size) {
    return forReader(source, encoding, $reader -> {
      try {
        IoFunctions.skip($reader, offset);
        var result  = new char[size];
        var toread  = size;
        var ioffset = 0;
        while (toread > 0) {
          var read = $reader.read(result, ioffset, toread);
          if (read != -1) {
            toread  -= read;
            ioffset += read;
          }
        }
        return result;
      } catch (Exception ex) {
        throw new KclException(ex, error_failed_to_read_from, source);
      }
    });
  }

  default @NotNull byte[] loadAllBytes(@NotNull T source) {
    return loadAllBytes(source, 0);
  }
  
  default @NotNull byte[] loadAllBytes(@NotNull T source, @Min(0) int offset) {
    return Buckets.bucketByteArrayOutputStream().forInstance($byteout -> {
      forInputStreamDo(source, $instream -> {
        try {
          IoFunctions.skip($instream, offset);
          IoFunctions.copy($instream, $byteout);
        } catch (Exception ex) {
          throw new KclException(ex, error_failed_to_read_from, source);
        }
      });
      return $byteout.toByteArray();
    });
  }

  default @NotNull char[] loadAllChars(@NotNull T source) {
    return loadAllChars(source, null, 0);
  }
  
  default @NotNull char[] loadAllChars(@NotNull T source, @Min(0) int offset) {
    return loadAllChars(source, null, offset);
  }

  default @NotNull char[] loadAllChars(@NotNull T source, @Null Encoding encoding) {
    return loadAllChars(source, encoding, 0);
  }
  
  default @NotNull char[] loadAllChars(@NotNull T source, @Null Encoding encoding, @Min(0) int offset) {
    return Buckets.bucketCharArrayWriter().forInstance($charout -> {
      forReaderDo(source, encoding, $reader -> {
        try {
          IoFunctions.skip($reader, offset);
          IoFunctions.copy($reader, $charout);
        } catch (Exception ex) {
          throw new KclException(ex, error_failed_to_read_from, source);
        }
      });
      return $charout.toCharArray();
    });
  }

  default void saveBytes(@NotNull T destination, @NotNull byte[] data) {
    saveBytes(destination, data, 0, data.length);
  }
  
  default void saveBytes(@NotNull T destination, @NotNull byte[] data, @Min(0) int offset, @Min(0) int length) {
    forOutputStreamDo(destination, $ -> {
      try {
        $.write(data, offset, length);
      } catch (Exception ex) {
        throw new KclException(ex, error_failed_to_write_to, destination);
      }
    });
  }

  default void saveChars(@NotNull T destination, @NotNull char[] data) {
    saveChars(destination, null, data, 0, data.length);
  }

  default void saveChars(@NotNull T destination, @Null Encoding encoding, @NotNull char[] data) {
    saveChars(destination, encoding, data, 0, data.length);
  }

  default void saveChars(@NotNull T destination, @Null Encoding encoding, char[] data, @Min(0) int offset, @Min(0) int size) {
    forWriterDo(destination, encoding, $writer -> {
      try {
        $writer.write(data, offset, size);
      } catch (Exception ex) {
        throw new KclException(ex, error_failed_to_write_to, destination);
      }
    });
  }
  
  default @NotNull String readText(@NotNull T source) {
    return readText(source, null);
  }

  default @NotNull String readText(@NotNull T source, @Null Encoding encoding) {
    return forReader(source, encoding, IoFunctions::readText);
  }
  
  default void writeText(@NotNull T destination, @NotNull String text) {
    writeText(destination, null, text);
  }

  default void writeText(@NotNull T destination, @Null Encoding encoding, @NotNull String text) {
    forWriterDo(destination, encoding, $writer -> {
      try {
        IoFunctions.writeText($writer, text);
      } catch (Exception ex) {
        throw new KclException(ex, error_failed_to_write_text_to, destination);
      }
    });
  }
  
} /* ENDINTERFACE */
