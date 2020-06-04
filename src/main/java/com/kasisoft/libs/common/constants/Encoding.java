package com.kasisoft.libs.common.constants;

import com.kasisoft.libs.common.KclException;
import com.kasisoft.libs.common.annotation.Specification;
import com.kasisoft.libs.common.old.constants.ByteOrderMark;

import javax.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import java.net.URL;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Collection of supported encodings.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Specification(value = "https://docs.oracle.com/javase/10/docs/api/java/nio/charset/Charset.html", date = "04-JUN-2020")
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
  
  private static final Map<String, Encoding>   ENCODINGS;
  
  static {
    ENCODINGS   = new HashMap<>();
    ASCII       = new Encoding("US-ASCII"    , false , null                 );
    UTF8        = new Encoding("UTF-8"       , false , ByteOrderMark.UTF8   );
    UTF16       = new Encoding("UTF-16"      , true  , null                 );
    UTF16BE     = new Encoding("UTF-16BE"    , false , ByteOrderMark.UTF16BE);
    UTF16LE     = new Encoding("UTF-16LE"    , false , ByteOrderMark.UTF16LE);
    ISO88591    = new Encoding("ISO-8859-1"  , false , null                 );
  }
  
  /** Neither <code>null</code> nor empty. */
  @Getter String          encoding;
  
  @Getter boolean         bomRequired;
  
  /** Maybe <code>null</code> */
  @Getter ByteOrderMark   byteOrderMark;
  
  /** Not <code>null</code> */
  @Getter Charset         charset;
  
  /**
   * Initializes this Encoding instance for a specific character set.
   *  
   * @param key           The name of the character set. Neither <code>null</code> nor empty.
   * @param requiresbom   <code>true</code> <=> Identifying this encoding requires a {@link ByteOrderMark}. 
   * @param mark          A {@link ByteOrderMark} which allows to identify the encoding. Maybe <code>null</code>.
   */
  public Encoding(@NotNull String key, boolean requiresbom, ByteOrderMark mark) {
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
   * @return   The reader if the file could be opened. Not <code>null</code>.
   */
  public @NotNull Reader openReader(@NotNull File file) {
    return openReader(file.toPath());
  }

  /**
   * Opens a Reader for a specific resource using this encoding.
   * 
   * @param url   The url of the resource that has to be opened using this encoding. Must be a valid resource.
   *  
   * @return   The reader if the url could be opened. Not <code>null</code>.
   */
  @SuppressWarnings("resource")
  public @NotNull Reader openReader(@NotNull URL url) {
    try {
      return openReader(url.openStream());
    } catch (IOException ex) {
      throw new KclException(ex, "Could not open '%s'!", url);
    }
  }

  /**
   * Opens a Reader for a specific resource using this encoding.
   * 
   * @param path   The path of the resource that has to be opened using this encoding. Must be a valid resource.
   *  
   * @return   The reader if the url could be opened. Not <code>null</code>.
   */
  @SuppressWarnings("resource")
  public @NotNull Reader openReader(@NotNull Path path) {
    try {
      return openReader(Files.newInputStream(path));
    } catch (IOException ex) {
      throw new KclException(ex, "Could not open '%s'!", path);
    }
  }

  /**
   * Opens a Reader for a specific InputStream using this encoding.
   * 
   * @param instream   The InputStream that has to be accessed using this encoding. Not <code>null</code>.
   *  
   * @return   The Reader if it can be accessed. Not <code>null</code>.
   */
  public @NotNull Reader openReader(@NotNull InputStream instream) {
    try {
      return new BufferedReader(new InputStreamReader(instream, encoding));
    } catch (UnsupportedEncodingException ex) {
      // won't happen as we only support guarantueed encodings
      return null;
    }
  }

  /**
   * Opens a Writer for a specific file using this encoding.
   * 
   * @param file   The file that has to be opened using this encoding. Must be a valid file.
   *  
   * @return   The writer if the file could be opened. Not <code>null</code>.
   */
  public @NotNull Writer openWriter(@NotNull File file) {
    return openWriter(file.toPath());
  }

  /**
   * Opens a Writer for a specific file using this encoding.
   * 
   * @param path   The path that has to be opened using this encoding. Must be a valid file.
   *  
   * @return   The writer if the file could be opened. Not <code>null</code>.
   */
  @SuppressWarnings("resource")
  public @NotNull Writer openWriter(@NotNull Path path) {
    try {
      return openWriter(Files.newOutputStream(path));
    } catch (IOException ex) {
      throw new KclException(ex, "Failed to open '%s'!", path);
    }
  }

  /**
   * Opens a Writer for a specific OutputStream using this encoding.
   * 
   * @param outstream   The OutputStream that has to be opened using this encoding. Must be a valid file.
   *  
   * @return   The writer if the file could be opened. Not <code>null</code>.
   */
  public @NotNull Writer openWriter(@NotNull OutputStream outstream) {
    try {
      return new BufferedWriter(new OutputStreamWriter(outstream, encoding));
    } catch (UnsupportedEncodingException ex) {
      // won't happen as we only support guarantueed encodings
      return null;
    }
  }

  /**
   * Encodes the supplied text.
   * 
   * @param text   The text that has to be encoded. Not <code>null</code>.
   * 
   * @return   The data which has to be encoded. Not <code>null</code>.
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
   * @param data   The data providing the content. Not <code>null</code>.
   * 
   * @return   The decoded String. Not <code>null</code>.
   */
  public @NotNull String decode(@NotNull byte[] data) {
    return charset.decode(ByteBuffer.wrap(data)).toString();
  }
  
  public static Encoding[] values() {
    return ENCODINGS.values().toArray(new Encoding[ENCODINGS.size()]);
  }
  
  /**
   * This helper function identifies the encoding value which corresponds to the supplied name. Be
   * aware that this enumeration only supports the <b>required</b> encodings.
   * 
   * @param name   The name of the encoding which has to be identified. Case sensitivity doesn't matter here.
   *               Neither <code>null</code> nor empty.
   *               
   * @return   The encoding value if available.
   */
  public static @NotNull Optional<Encoding> findByName(@NotNull String name) {
    return Optional.ofNullable(ENCODINGS.get(name));
  }

} /* ENDCLASS */
