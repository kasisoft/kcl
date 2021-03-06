package com.kasisoft.libs.common.constants;

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.*;
import com.kasisoft.libs.common.annotation.*;

import javax.validation.constraints.*;

import java.util.*;

import java.net.*;

import java.nio.*;
import java.nio.charset.*;
import java.nio.file.*;

import java.io.*;

import lombok.experimental.FieldDefaults;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Collection of supported encodings.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Specification(value = "https://docs.oracle.com/javase/10/docs/api/java/nio/charset/Charset.html", date = "04-JUN-2020")
@Getter
@EqualsAndHashCode(of = "encoding")
@ToString(of = "encoding")
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class Encoding {
  
  public static final Encoding ASCII;
  public static final Encoding UTF8;
  public static final Encoding UTF16;
  public static final Encoding UTF16BE;
  public static final Encoding UTF16LE;
  public static final Encoding ISO88591;
  public static final Encoding IBM437;
  
  private static final Map<String, Encoding>   ENCODINGS;
  
  static {
    ENCODINGS   = new HashMap<>();
    ASCII       = new Encoding("US-ASCII"  , false, null);
    UTF8        = new Encoding("UTF-8"     , false, ByteOrderMark.UTF8);
    UTF16       = new Encoding("UTF-16"    , true , null);
    UTF16BE     = new Encoding("UTF-16BE"  , false, ByteOrderMark.UTF16BE);
    UTF16LE     = new Encoding("UTF-16LE"  , false, ByteOrderMark.UTF16LE);
    ISO88591    = new Encoding("ISO-8859-1", false, null);
    IBM437      = new Encoding("IBM437"    , false, null);
  }
  
  String          encoding;
  boolean         bomRequired;
  ByteOrderMark   byteOrderMark;
  Charset         charset;
  
  /**
   * Initializes this Encoding instance for a specific character set.
   *  
   * @param key           The name of the character set.
   * @param requiresbom   <code>true</code> <=> Identifying this encoding requires a {@link ByteOrderMark}. 
   * @param mark          A {@link ByteOrderMark} which allows to identify the encoding.
   */
  public Encoding(@NotBlank String key, boolean requiresbom, ByteOrderMark mark) {
    encoding      = key;
    bomRequired   = requiresbom;
    byteOrderMark = mark;
    charset       = Charset.forName(key);
    synchronized (ENCODINGS) {
      ENCODINGS.put(key, this);
    }
  }
  
  /**
   * Opens a Reader for a specific file using this encoding.
   * 
   * @param file   The file that has to be opened using this encoding. Must be a valid file.
   *  
   * @return   The reader if the file could be opened.
   */
  public @NotNull Reader openReader(@NotNull File file) {
    return IoFunctions.newReader(file, this);
  }

  /**
   * Opens a Reader for a specific resource using this encoding.
   * 
   * @param url   The url of the resource that has to be opened using this encoding. Must be a valid resource.
   *  
   * @return   The reader if the url could be opened.
   */
  @SuppressWarnings("resource")
  public @NotNull Reader openReader(@NotNull URL url) {
    return IoFunctions.newReader(url, this);
  }

  /**
   * Opens a Reader for a specific resource using this encoding.
   * 
   * @param uri   The URI of the resource that has to be opened using this encoding. Must be a valid resource.
   *  
   * @return   The reader if the url could be opened.
   */
  @SuppressWarnings("resource")
  public @NotNull Reader openReader(@NotNull URI uri) {
    return IoFunctions.newReader(uri, this);
  }

  /**
   * Opens a Reader for a specific resource using this encoding.
   * 
   * @param path   The path of the resource that has to be opened using this encoding. Must be a valid resource.
   *  
   * @return   The reader if the url could be opened.
   */
  @SuppressWarnings("resource")
  public @NotNull Reader openReader(@NotNull Path path) {
    return IoFunctions.newReader(path, this);
  }

  /**
   * Opens a Reader for a specific InputStream using this encoding.
   * 
   * @param instream   The InputStream that has to be accessed using this encoding.
   *  
   * @return   The Reader if it can be accessed.
   */
  public @NotNull Reader openReader(@NotNull InputStream instream) {
    return IoFunctions.newReader(instream, this);
  }

  /**
   * Opens a Writer for a specific file using this encoding.
   * 
   * @param file   The file that has to be opened using this encoding. Must be a valid file.
   *  
   * @return   The writer if the file could be opened.
   */
  public @NotNull Writer openWriter(@NotNull File file) {
    return IoFunctions.newWriter(file, this);
  }

  /**
   * Opens a Writer for a specific file using this encoding.
   * 
   * @param path   The path that has to be opened using this encoding. Must be a valid file.
   *  
   * @return   The writer if the file could be opened.
   */
  @SuppressWarnings("resource")
  public @NotNull Writer openWriter(@NotNull Path path) {
    return IoFunctions.newWriter(path, this);
  }

  /**
   * Opens a Writer for a specific OutputStream using this encoding.
   * 
   * @param outstream   The OutputStream that has to be opened using this encoding. Must be a valid file.
   *  
   * @return   The writer if the file could be opened.
   */
  public @NotNull Writer openWriter(@NotNull OutputStream outstream) {
    return IoFunctions.newWriter(outstream, this);
  }

  /**
   * Encodes the supplied text.
   * 
   * @param text   The text that has to be encoded.
   * 
   * @return   The data which has to be encoded.
   */
  public @NotNull byte[] encode(@NotNull String text) {
    var buffer = charset.encode(CharBuffer.wrap(text));
    var result = new byte[buffer.limit()];
    buffer.get(result);
    return result;
  }

  /**
   * Decodes the supplied data using this encoding.
   * 
   * @param data   The data providing the content.
   * 
   * @return   The decoded String.
   */
  public @NotNull String decode(@NotNull byte[] data) {
    return charset.decode(ByteBuffer.wrap(data)).toString();
  }
  
  public static Encoding[] values() {
    synchronized (ENCODINGS) {
      return ENCODINGS.values().toArray(new Encoding[ENCODINGS.size()]);
    }
  }
  
  /**
   * This helper function identifies the encoding value which corresponds to the supplied name. Be
   * aware that this enumeration only supports the <b>required</b> encodings.
   * 
   * @param name   The name of the encoding which has to be identified. Case sensitivity doesn't matter here.
   *               
   * @return   The encoding value if available.
   */
  public static @NotNull Optional<Encoding> findByName(@NotNull String name) {
    synchronized (ENCODINGS) {
      return Optional.ofNullable(ENCODINGS.get(name));
    }
  }
  
  public static @NotNull Encoding getEncoding(Encoding encoding) {
    if (encoding == null) {
      return KclConfig.DEFAULT_ENCODING;
    }
    return encoding;
  }

} /* ENDCLASS */
