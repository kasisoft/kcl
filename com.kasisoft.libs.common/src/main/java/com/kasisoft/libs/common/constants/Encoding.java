package com.kasisoft.libs.common.constants;

import com.kasisoft.libs.common.base.*;

import com.kasisoft.libs.common.io.*;

import lombok.experimental.*;

import lombok.*;

import java.util.*;

import java.net.*;

import java.io.*;

import java.nio.charset.*;
import java.nio.file.*;

import java.nio.*;

/**
 * Collection of supported encodings.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@EqualsAndHashCode(of = "encoding")
@ToString(of = "encoding")
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class Encoding {
  
  /**
   * @ks.spec [07-Dec-2014:KASI]   http://docs.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html
   */
  
  public static final Encoding ASCII;
  public static final Encoding UTF8;
  public static final Encoding UTF16;
  public static final Encoding UTF16BE;
  public static final Encoding UTF16LE;
  public static final Encoding ISO88591;
  
  static final Map<String,Encoding>   ENCODINGS;
  
  static {
    ENCODINGS   = new Hashtable<>();
    ASCII       = new Encoding( "US-ASCII"    , false , null                  );
    UTF8        = new Encoding( "UTF-8"       , false , ByteOrderMark.UTF8    );
    UTF16       = new Encoding( "UTF-16"      , true  , null                  );
    UTF16BE     = new Encoding( "UTF-16BE"    , false , ByteOrderMark.UTF16BE );
    UTF16LE     = new Encoding( "UTF-16LE"    , false , ByteOrderMark.UTF16LE );
    ISO88591    = new Encoding( "ISO-8859-1"  , false , null                  );
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
  public Encoding( @NonNull String key, boolean requiresbom, ByteOrderMark mark ) {
    encoding      = key;
    bomRequired   = requiresbom;
    byteOrderMark = mark;
    charset       = Charset.forName( key );
    ENCODINGS.put( key, this );
  }
  
  /**
   * Opens a Reader for a specific file using this encoding.
   * 
   * @param file   The file that has to be opened using this encoding. Must be a valid file.
   *  
   * @return   The reader if the file could be opened. Not <code>null</code>.
   * 
   * @throws FailureException if opening the file failed for some reason.
   */
  public Reader openReader( @NonNull File file ) {
    return openReader( IoFunctions.newInputStream( file ) );
  }

  /**
   * Opens a Reader for a specific resource using this encoding.
   * 
   * @param url   The url of the resource that has to be opened using this encoding. Must be a valid resource.
   *  
   * @return   The reader if the url could be opened. Not <code>null</code>.
   * 
   * @throws FailureException if opening the resource failed for some reason.
   */
  public Reader openReader( @NonNull URL url ) {
    return openReader( IoFunctions.newInputStream( url ) );
  }

  /**
   * Opens a Reader for a specific resource using this encoding.
   * 
   * @param path   The path of the resource that has to be opened using this encoding. Must be a valid resource.
   *  
   * @return   The reader if the url could be opened. Not <code>null</code>.
   * 
   * @throws FailureException if opening the resource failed for some reason.
   */
  public Reader openReader( @NonNull Path path ) {
    return openReader( IoFunctions.newInputStream( path ) );
  }

  /**
   * Opens a Reader for a specific InputStream using this encoding.
   * 
   * @param instream   The InputStream that has to be accessed using this encoding. Not <code>null</code>.
   *  
   * @return   The Reader if it can be accessed. Not <code>null</code>.
   */
  public Reader openReader( @NonNull InputStream instream ) {
    try {
      return new BufferedReader( new InputStreamReader( instream, encoding ) );
    } catch( UnsupportedEncodingException ex ) {
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
   * 
   * @throws FailureException if opening the file failed for some reason.
   */
  public Writer openWriter( @NonNull File file ) {
    return openWriter( IoFunctions.newOutputStream( file ) );
  }

  /**
   * Opens a Writer for a specific file using this encoding.
   * 
   * @param path   The path that has to be opened using this encoding. Must be a valid file.
   *  
   * @return   The writer if the file could be opened. Not <code>null</code>.
   * 
   * @throws FailureException if opening the file failed for some reason.
   */
  public Writer openWriter( @NonNull Path path ) {
    return openWriter( IoFunctions.newOutputStream( path ) );
  }

  /**
   * Opens a Writer for a specific OutputStream using this encoding.
   * 
   * @param outstream   The OutputStream that has to be opened using this encoding. Must be a valid file.
   *  
   * @return   The writer if the file could be opened. Not <code>null</code>.
   */
  public Writer openWriter( @NonNull OutputStream outstream ) {
    try {
      return new BufferedWriter( new OutputStreamWriter( outstream, encoding ) );
    } catch( UnsupportedEncodingException ex ) {
      // won't happen as we only support guarantueed encodings
      return null;
    }
  }

  /**
   * Opens a PrintStream for a specific file using this encoding.
   * 
   * @param file   The file that has to be opened using this encoding. Must be a valid file.
   *  
   * @return   The PrintStream if the file could be opened. Not <code>null</code>.
   * 
   * @throws FailureException if opening the file failed for some reason.
   */
  public PrintStream openPrintStream( @NonNull File file ) {
    return openPrintStream( IoFunctions.newOutputStream( file ) );
  }

  /**
   * Opens a PrintStream for a specific OutputStream using this encoding.
   * 
   * @param outstream   The OutputStream that has to be opened using this encoding. Must be a valid file.
   *  
   * @return   The PrintStream if the OutputStream could be accessed. Not <code>null</code>.
   */
  public PrintStream openPrintStream( @NonNull OutputStream outstream ) {
    try {
      return new PrintStream( new BufferedOutputStream( outstream ), true, encoding );
    } catch( UnsupportedEncodingException ex ) {
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
  public byte[] encode( @NonNull String text ) {
    ByteBuffer buffer = charset.encode( CharBuffer.wrap( text ) );
    byte[]     result = new byte[ buffer.limit() ];
    buffer.get( result );
    return result;
  }

  /**
   * Decodes the supplied data using this encoding.
   * 
   * @param data   The data providing the content. Not <code>null</code>.
   * 
   * @return   The decoded String. Not <code>null</code>.
   */
  public String decode( @NonNull byte[] data ) {
    return charset.decode( ByteBuffer.wrap( data ) ).toString();
  }
  
  /**
   * Opens a Reader for a specific file using this encoding.
   * 
   * @param file       The file that has to be opened using this encoding. Must be a valid file.
   * @param encoding   The encoding that has to be used. If <code>null</code> the default encoding {@link #UTF8} is used.
   *  
   * @return   The reader if the file could be opened. Not <code>null</code>.
   * 
   * @throws FailureException if opening the file failed for some reason.
   */
  public static Reader openReader( @NonNull File file, Encoding encoding ) {
    if( encoding == null ) {
      return UTF8.openReader( file );
    } else {
      return encoding.openReader( file );
    }
  }

  /**
   * Opens a Reader for a specific file using this encoding.
   * 
   * @param path       The path that has to be opened using this encoding. Must be a valid file.
   * @param encoding   The encoding that has to be used. If <code>null</code> the default encoding {@link #UTF8} is used.
   *  
   * @return   The reader if the file could be opened. Not <code>null</code>.
   * 
   * @throws FailureException if opening the file failed for some reason.
   */
  public static Reader openReader( @NonNull Path path, Encoding encoding ) {
    if( encoding == null ) {
      return UTF8.openReader( path );
    } else {
      return encoding.openReader( path );
    }
  }

  /**
   * Opens a Reader for a specific InputStream using this encoding.
   * 
   * @param instream   The InputStream that has to be opened using this encoding. Not <code>null</code>.
   * @param encoding   The encoding that has to be used. If <code>null</code> the default encoding {@link #UTF8} is used.
   *  
   * @return   The reader if the InputStream could be accessed. Not <code>null</code>.
   * 
   * @throws FailureException if accessing the InputStream failed for some reason.
   */
  public static Reader openReader( @NonNull InputStream instream, Encoding encoding ) {
    if( encoding == null ) {
      return UTF8.openReader( instream );
    } else {
      return encoding.openReader( instream );
    }
  }

  /**
   * Opens a Writer for a specific file using this encoding.
   * 
   * @param file       The file that has to be opened using this encoding. Must be a valid file.
   * @param encoding   The encoding that has to be used. If <code>null</code> the default encoding {@link #UTF8} is used.
   *  
   * @return   The writer if the file could be opened. Not <code>null</code>.
   * 
   * @throws FailureException if opening the file failed for some reason.
   */
  public static Writer openWriter( @NonNull File file, Encoding encoding ) {
    if( encoding == null ) {
      return UTF8.openWriter( file );
    } else {
      return encoding.openWriter( file );
    }
  }

  /**
   * Opens a Writer for a specific file using this encoding.
   * 
   * @param path       The path that has to be opened using this encoding. Must be a valid file.
   * @param encoding   The encoding that has to be used. If <code>null</code> the default encoding {@link #UTF8} is used.
   *  
   * @return   The writer if the file could be opened. Not <code>null</code>.
   * 
   * @throws FailureException if opening the file failed for some reason.
   */
  public static Writer openWriter( @NonNull Path path, Encoding encoding ) {
    if( encoding == null ) {
      return UTF8.openWriter( path );
    } else {
      return encoding.openWriter( path );
    }
  }

  /**
   * Opens a Writer for a specific OutputStream using this encoding.
   * 
   * @param outstream   The OutputStream that has to be opened using this encoding. Not <code>null</code>.
   * @param encoding    The encoding that has to be used. If <code>null</code> the default encoding {@link #UTF8} is used.
   *  
   * @return   The writer if the OutputStream could be accessed. Not <code>null</code>.
   * 
   * @throws FailureException if opening the file failed for some reason.
   */
  public static Writer openWriter( @NonNull OutputStream outstream, Encoding encoding ) {
    if( encoding == null ) {
      return UTF8.openWriter( outstream );
    } else {
      return encoding.openWriter( outstream );
    }
  }

  /**
   * Opens a PrintStream for a specific file using this encoding.
   * 
   * @param file       The file that has to be opened using this encoding. Must be a valid file.
   * @param encoding   The encoding that has to be used. If <code>null</code> the default encoding {@link #UTF8} is used.
   *  
   * @return   The PrintStream if the file could be opened. Not <code>null</code>.
   * 
   * @throws FailureException if opening the file failed for some reason.
   */
  public static PrintStream openPrintStream( @NonNull File file, Encoding encoding ) {
    if( encoding == null ) {
      return UTF8.openPrintStream( file );
    } else {
      return encoding.openPrintStream( file );
    }
  }

  /**
   * Opens a PrintStream for a specific OutputStream using this encoding.
   * 
   * @param outstream   The OutputStream that has to be accessed using this encoding. Not <code>null</code>.
   * @param encoding    The encoding that has to be used. If <code>null</code> the default encoding {@link #UTF8} is 
   *                    used.
   *  
   * @return   The PrintStream if the OutputStream could be accessed. Not <code>null</code>.
   * 
   * @throws FailureException if opening the file failed for some reason.
   */
  public static PrintStream openPrintStream( @NonNull OutputStream outstream, Encoding encoding ) {
    if( encoding == null ) {
      return UTF8.openPrintStream( outstream );
    } else {
      return encoding.openPrintStream( outstream );
    }
  }
  
  /**
   * Decodes the binary data.
   * 
   * @param data       The binary data. Not <code>null</code>.
   * @param encoding   The encoding which has to be used. If <code>null</code> the default encoding is used.
   * 
   * @return   The decoded String. Not <code>null</code>.
   */
  public static String decode( @NonNull byte[] data, Encoding encoding ) {
    if( encoding == null ) {
      return UTF8.decode( data );
    } else {
      return encoding.decode( data );
    }
  }

  /**
   * Encodes the textual data.
   * 
   * @param data       The textual data. Not <code>null</code>.
   * @param encoding   The encoding which has to be used. If <code>null</code> the default encoding is used.
   * 
   * @return   The encoded bytes. Not <code>null</code>.
   */
  public static byte[] encode( @NonNull String data, Encoding encoding ) {
    if( encoding == null ) {
      return UTF8.encode( data );
    } else {
      return encoding.encode( data );
    }
  }

  public static Encoding[] values() {
    return ENCODINGS.values().toArray( new Encoding[ ENCODINGS.size() ] );
  }
  
  /**
   * This helper function identifies the encoding value which corresponds to the supplied name. Be
   * aware that this enumeration only supports the <b>required</b> encodings.
   * 
   * @param name   The name of the encoding which has to be identified. Case sensitivity doesn't matter here.
   *               Neither <code>null</code> nor empty.
   *               
   * @return   The encoding value or <code>null</code> if it cannot be identified.
   */
  public static Encoding valueByName( @NonNull String name ) {
    for( Encoding encoding : Encoding.values() ) {
      if( encoding.encoding.equalsIgnoreCase( name ) ) {
        return encoding;
      }
    }
    return null;
  }

} /* ENDCLASS */
