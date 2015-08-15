package com.kasisoft.libs.common.io;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.thread.*;

import com.kasisoft.libs.common.util.*;

import com.kasisoft.libs.common.base.*;

import com.kasisoft.libs.common.sys.*;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

import java.util.regex.*;

import java.util.*;
import java.util.zip.*;

import java.net.*;

import java.io.*;

import java.nio.file.*;

/**
 * Collection of functions used for IO operations.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IoFunctions {

  static final byte[] NO_DATA = new byte[0];

  static final String WC1 = "([^/]+)";    // *
  static final String WC2 = "(.+)";       // **

  public static final FileFilter ACCEPT_ALL = new FileFilter() {

    @Override
    public boolean accept( File pathname ) {
      return true;
    }
    
  };
  
  /**
   * Prevent instantiation.
   */
  private IoFunctions() {
  }
  
  /**
   * Creates a regex pattern used to match a filesystem path.
   * 
   * @param pattern   The filesystem pattern which is supposed to be compiled. Neither <code>null</code> nor empty.
   * 
   * @return   A {@link Pattern} instance used to test filesystem pathes. Not <code>null</code>.
   */
  public static Pattern compileFilesystemPattern( @NonNull String pattern ) {
    StringBuilder   buffer    = new StringBuilder();
    StringTokenizer tokenizer = new StringTokenizer( pattern, "*", true );
    boolean         last      = false;
    while( tokenizer.hasMoreTokens() ) {
      String token = tokenizer.nextToken();
      if( "*".equals( token ) ) {
        if( last ) {
          buffer.append( WC2 );
          last = false;
        } else {
          last = true;
        }
      } else {
        if( last ) {
          buffer.append( WC1 );
          last = false;
        }
        buffer.append( Pattern.quote( token ) );
      }
    }
    if( last ) {
      buffer.append( WC1 );
    }
    int flags = 0;
    if( ! SystemInfo.getRunningOS().isCaseSensitiveFS() ) {
      flags |= Pattern.CASE_INSENSITIVE;
    }
    return Pattern.compile( buffer.toString(), flags );
  }

  /**
   * Creates an instance of {@link InputStream} and handles potential exceptions.
   * 
   * @param file   The {@link File} that will be opened. Not <code>null</code>.
   * 
   * @return   The opened {@link InputStream}. Not <code>null</code>.
   */
  public static InputStream newInputStream( @NonNull File file ) {
    try {
      return new BufferedInputStream( new FileInputStream( file ) );
    } catch( FileNotFoundException ex ) {
      throw FailureCode.IO.newException( ex );
    }
  }

  /**
   * Creates an instance of {@link InputStream} and handles potential exceptions.
   * 
   * @param path   The {@link Path} that will be opened. Not <code>null</code>.
   * 
   * @return   The opened {@link InputStream}. Not <code>null</code>.
   */
  public static InputStream newInputStream( @NonNull Path path ) {
    try {
      return Files.newInputStream( path );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
  }

  /**
   * Creates an instance of {@link InputStream} and handles potential exceptions.
   * 
   * @param url   The URL pointing to the resource that will be opened. Not <code>null</code>.
   * 
   * @return   The opened {@link InputStream}. Not <code>null</code>.
   */
  public static InputStream newInputStream( @NonNull URL url ) {
    try {
      return new BufferedInputStream( url.openStream() );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    } 
  }

  /**
   * Creates an instance of {@link OutputStream} and handles potential exceptions.
   * 
   * @param file   The {@link File} that will be opened. Not <code>null</code>.
   * 
   * @return   The opened {@link OutputStream}. Not <code>null</code>.
   */
  public static OutputStream newOutputStream( @NonNull File file ) {
    try {
      return new BufferedOutputStream( new FileOutputStream( file ) );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    } 
  }

  /**
   * Creates an instance of {@link OutputStream} and handles potential exceptions.
   * 
   * @param file   The {@link File} that will be opened. Not <code>null</code>.
   * 
   * @return   The opened {@link OutputStream}. Not <code>null</code>.
   */
  public static OutputStream newOutputStream( @NonNull Path path ) {
    try {
      return Files.newOutputStream( path, StandardOpenOption.CREATE );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
  }

  /**
   * Returns a file for temporary use.
   * 
   * @return   The file usable to create a directory or a file. Not <code>null</code> and does not exist.
   */
  public static File newTempFile() {
    return newTempFile( null, null );
  }

  /**
   * Returns a file for temporary use.
   * 
   * @param basename   The basename for the file. Maybe <code>null</code>.
   * 
   * @return   The file usable to create a directory or a file. Not <code>null</code> and does not exist.
   */
  public static File newTempFile( String basename ) {
    return newTempFile( basename, null );
  }
  
  /**
   * Returns a file for temporary use.
   * 
   * @param basename   The basename for the file. Maybe <code>null</code>.
   * @param suffix     The suffix for the file. Maybe <code>null</code>.
   * 
   * @return   The file usable to create a directory or a file. Not <code>null</code> and does not exist.
   */
  public static File newTempFile( String basename, String suffix ) {
    if( suffix == null ) {
      suffix    = "";
    }
    if( basename == null ) {
      basename  = "";
    }
    File dir        = CommonProperty.TempDir.getValue( System.getProperties() );
    File candidate  = null;
    do {
      long number = (long) (System.currentTimeMillis() * Math.random());
      candidate   = new File( dir, String.format( "%s%08x%s", basename, Long.valueOf( number ), suffix ) );
    } while( candidate.exists() );
    return candidate;
  }

  /**
   * Copies some content from an InputStream to an OutputStream.
   * 
   * @param input    The stream providing the content. Not <code>null</code>.
   * @param output   The stream receiving the content. Not <code>null</code>.
   * @param buffer   The buffer to use while copying. Maybe <code>null</code>.
   *
   * @throws FailureException   Whenever the copying failed for some reason.
   */
  public static void copy( @NonNull InputStream input, @NonNull OutputStream output, byte[] buffer ) {
    byte[] data = buffer;
    if( buffer == null ) {
      data = Primitive.PByte.allocate( null );
    }
    try {
      int read = input.read( data );
      while( read != -1 ) {
        if( read > 0 ) {
          output.write( data, 0, read );
        }
        read = input.read( data );
      }
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
    if( buffer == null ) {
      Primitive.PByte.release( data );
    }
  }

  /**
   * Copies some content from an InputStream to an OutputStream.
   * 
   * @param input    The stream providing the content. Not <code>null</code>.
   * @param output   The stream receiving the content. Not <code>null</code>.
   *
   * @throws FailureException whenever the copying failed for some reason.
   */
  public static void copy( @NonNull InputStream input, @NonNull OutputStream output ) {
    copy( input, output, (byte[]) null );
  }
  
  /**
   * Copies some content from an InputStream to an OutputStream.
   * 
   * @param input        The stream providing the content. Not <code>null</code>.
   * @param output       The stream receiving the content. Not <code>null</code>.
   * @param buffersize   The buffer size to use while copying. Maybe <code>null</code>.
   *
   * @throws FailureException   Whenever the copying failed for some reason.
   */
  public static void copy( @NonNull InputStream input, @NonNull OutputStream output, Integer buffersize ) {
    byte[] buffer = Primitive.PByte.allocate( buffersize );
    copy( input, output, buffer );
    Primitive.PByte.release( buffer );
  }

  /**
   * Copies some content from an InputStream to an OutputStream.
   * 
   * @param input        The stream providing the content. Not <code>null</code>.
   * @param output       The stream receiving the content. Not <code>null</code>.
   * @param buffersize   The buffer size to use while copying.
   *
   * @throws FailureException whenever the copying failed for some reason.
   */
  public static void copy( @NonNull InputStream input, @NonNull OutputStream output, int buffersize ) {
    copy( input, output, Integer.valueOf( buffersize ) );
  }
  
  /**
   * Copies the content from one file to another file.
   * 
   * @param input    The file which contains the content. Must be a valid file.
   * @param output   The destination where to write the content to. Not <code>null</code>.
   * 
   * @throws FailureException when the copying process fails for some reason.
   */
  public static void copy( @NonNull File input, @NonNull File output ) {
    copy( Paths.get( input.toURI() ), Paths.get( output.toURI() ) );
  }
  
  /**
   * Copies the content from one file to another file.
   * 
   * @param input    The file which contains the content. Must be a valid file.
   * @param output   The destination where to write the content to. Not <code>null</code>.
   * 
   * @throws FailureException when the copying process fails for some reason.
   */
  public static void copy( @NonNull Path input, @NonNull Path output ) {
    try {
      Files.copy( input, output, StandardCopyOption.REPLACE_EXISTING );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException(ex);
    }
  }
  
  /**
   * Copies the content from one directory to another directory. Destination directories will be created as needed.
   * 
   * @param input       The directory which contains the content. Must be a valid directory.
   * @param output      The destination where to write the content to. Not <code>null</code>.
   * @param recursive   <code>true</code> <=> Copy subdirectories, too.
   * 
   * @throws FailureException when the copying process fails for some reason.
   */
  public static void copyDir( @NonNull File input, @NonNull File output, boolean recursive ) {
    if( ! output.exists() ) {
      mkdirs( output );
    }
    File[] children = input.listFiles();
    for( int i = 0; i < children.length; i++ ) {
      File newchild = new File( output, children[i].getName() );
      if( children[i].isFile() ) {
        copy( children[i], newchild );
      } else if( children[i].isDirectory() ) {
        mkdirs( newchild );
        if( recursive ) {
          copyDir( children[i], newchild, true );
        }
      }
    }
  }
  
  /**
   * Copies some content from a Reader to a Writer.
   * 
   * @param input    The reader providing the content. Not <code>null</code>.
   * @param output   The writer receiving the content. Not <code>null</code>.
   * @param buffer   The buffer to use while copying. Maybe <code>null</code>.
   *
   * @throws FailureException   Whenever the copying failed for some reason.
   */
  public static void copy( @NonNull Reader input, @NonNull Writer output, char[] buffer ) {
    char[] data = buffer;
    if( buffer == null ) {
      data = Primitive.PChar.allocate();
    }
    try {
      int read = input.read( data );
      while( read != -1 ) {
        if( read > 0 ) {
          output.write( data, 0, read );
        }
        read = input.read( data );
      }
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
    if( buffer == null ) {
      Primitive.PChar.release( data );
    }
  }

  /**
   * Copies some content from a Reader to a Writer.
   * 
   * @param input    The reader providing the content. Not <code>null</code>.
   * @param output   The writer receiving the content. Not <code>null</code>.
   *
   * @throws FailureException whenever the copying failed for some reason.
   */
  public static void copy( @NonNull Reader input, @NonNull Writer output ) {
    copy( input, output, (char[]) null );
  }
  
  /**
   * Copies some content from a Reader to a Writer.
   * 
   * @param input        The reader providing the content. Not <code>null</code>.
   * @param output       The writer receiving the content. Not <code>null</code>.
   * @param buffersize   The buffer size to use while copying. Maybe <code>null</code>.
   *
   * @throws FailureException whenever the copying failed for some reason.
   */
  public static void copy( @NonNull Reader input, @NonNull Writer output, Integer buffersize ) {
    char[] buffer = Primitive.PChar.allocate( buffersize );
    copy( input, output, buffer );
    Primitive.PChar.release( buffer );
  }

  /**
   * Copies some content from a Reader to a Writer.
   * 
   * @param input        The reader providing the content. Not <code>null</code>.
   * @param output       The writer receiving the content. Not <code>null</code>.
   * @param buffersize   The buffer size to use while copying.
   *
   * @throws FailureException whenever the copying failed for some reason.
   */
  public static void copy( @NonNull Reader input, @NonNull Writer output, int buffersize ) {
    copy( input, output, Integer.valueOf( buffersize ) );
  }
  
  /**
   * Reads the binary content of an InputStream.
   * 
   * @param input        The InputStream providing the content. Not <code>null</code>.
   * @param buffersize   The buffer size to use. A value of <code>null</code> indicates to use
   *                     the default size.
   *                       
   * @return   The binary content. Not <code>null</code>.
   * 
   * @throws FailureException when the copying process fails for some reason.
   */
  public static byte[] loadBytes( @NonNull InputStream input, Integer buffersize ) {
    ByteArrayOutputStream byteout = new ByteArrayOutputStream();
    copy( input, byteout, buffersize );
    return byteout.toByteArray();
  }

  /**
   * Reads the binary content of a Reader.
   * 
   * @param input        The Reader providing the content. Not <code>null</code>.
   * @param buffersize   The buffer size to use. A value of <code>null</code> indicates to use the default size.
   *                       
   * @return   The binary content. Not <code>null</code>.
   * 
   * @throws FailureException when the copying process fails for some reason.
   */
  public static char[] loadChars( @NonNull Reader input, Integer buffersize ) {
    CharArrayWriter charout = new CharArrayWriter();
    copy( input, charout, buffersize );
    return charout.toCharArray();
  }

  /**
   * Reads the binary content of a File.
   * 
   * @param file         The File providing the content. Not <code>null</code>.
   * @param buffersize   The buffer size to use. A value of <code>null</code> indicates to use the default size.
   *                       
   * @return   The binary content. Not <code>null</code>.
   *
   * @throws FailureException whenever the reading process fails for some reason.
   */
  public static byte[] loadBytes( @NonNull File file, Integer buffersize ) {
    InputStream           input   = null;
    ByteArrayOutputStream byteout = new ByteArrayOutputStream();
    try {
      input = newInputStream( file );
      copy( input, byteout, buffersize );
    } finally {
      MiscFunctions.close( input );
    }
    return byteout.toByteArray();
  }

  /**
   * Reads the binary content of an URL.
   * 
   * @param url          The resource providing the content. Not <code>null</code>.
   * @param buffersize   The buffer size to use. A value of <code>null</code> indicates to use the default size.
   *                       
   * @return   The binary content. Not <code>null</code>.
   *
   * @throws FailureException whenever the reading process fails for some reason.
   */
  public static byte[] loadBytes( @NonNull URL url, Integer buffersize ) {
    InputStream           input   = null;
    ByteArrayOutputStream byteout = new ByteArrayOutputStream();
    try {
      input = newInputStream( url );
      copy( input, byteout, buffersize );
    } finally {
      MiscFunctions.close( input );
    }
    return byteout.toByteArray();
  }

  /**
   * Loads a specific amount of data into a buffer.
   * 
   * @param buffer     The buffer where the data shall be written to. Not <code>null</code>.
   * @param instream   The InputStream which provides the content. Not <code>null</code>.
   * @param count      The amount of data which has to be loaded.
   * 
   * @throws IOException   Loading the content failed for some reason.
   */
  public static void loadBytes( @NonNull byte[] buffer, @NonNull InputStream instream, int count ) throws IOException {
    int offset  = 0; 
    int read    = instream.read( buffer, offset, count );
    while( (read != -1) && (count > 0) ) {
      if( read > 0 ) {
        offset += read;
        count  -= read;
      }
      read = instream.read( buffer, offset, count );
    }
  }
  
  /**
   * Loads a specific amount of data into a buffer.
   * 
   * @param buffer   The buffer where the data shall be written to. Not <code>null</code>.
   * @param reader   The Reader which provides the content. Not <code>null</code>.
   * @param count    The amount of data which has to be loaded.
   * 
   * @throws IOException   Loading the content failed for some reason.
   */
  public static void loadChars( @NonNull char[] buffer, @NonNull Reader reader, int count ) throws IOException {
    int offset  = 0; 
    int read    = reader.read( buffer, offset, count );
    while( (read != -1) && (count > 0) ) {
      if( read > 0 ) {
        offset += read;
        count  -= read;
      }
      read = reader.read( buffer, offset, count );
    }
  }

  /**
   * Reads the character content of a File.
   * 
   * @param file         The File providing the content. Not <code>null</code>.
   * @param buffersize   The buffer size to use. A value of <code>null</code> indicates to use the default size.
   * @param encoding     The encoding which has to be used to read the characters. If <code>null</code> the default 
   *                     encoding is used.
   *                       
   * @return   The character content. Not <code>null</code>.
   *
   * @throws FailureException in case of an io error.
   */
  public static char[] loadChars( @NonNull File file, Integer buffersize, Encoding encoding ) {
    Reader          reader  = null;
    CharArrayWriter charout = new CharArrayWriter();
    try {
      reader = Encoding.openReader( file, encoding );
      copy( reader, charout, buffersize );
    } finally {
      MiscFunctions.close( reader );
    }
    return charout.toCharArray();
  }
  
  /**
   * Loads the textual content from a reader.
   * 
   * @param input        The Reader providing the textual content. Not <code>null</code>.
   * @param trim         <code>true</code> <=> Trim each line.
   * @param emptylines   <code>true</code> <=> Also include empty lines in the result.
   * 
   * @return   A list with the textual content. Not <code>null</code>.
   *
   * @throws FailureException   In case of an io error.
   */
  public static List<String> readText( @NonNull Reader input, boolean trim, boolean emptylines ) {
    List<String>       result   = new ArrayList<>();
    LineReaderRunnable runnable = new LineReaderRunnable( input, result );
    runnable.setTrim( trim );
    runnable.setEmptyLines( emptylines );
    runnable.run();
    if( ! runnable.hasCompleted() ) {
      throw FailureCode.IO.newException();
    }
    return result;
  }

  /**
   * Loads the textual content from a reader. The lines will be read as is, so even empty
   * lines will be returned.
   * 
   * @param input   The Reader providing the textual content. Not <code>null</code>.
   * 
   * @return   A list with the textual content. Not <code>null</code>.
   *
   * @throws FailureException in case of an io error.
   */
  public static List<String> readText( @NonNull Reader input ) {
    return readText( input, false, true );
  }
  
  /**
   * Loads the textual content from a File.
   * 
   * @param input        The File providing the textual content. Not <code>null</code>.
   * @param trim         <code>true</code> <=> Trim each line.
   * @param emptylines   <code>true</code> <=> Also include empty lines in the result.
   * @param encoding     The encoding to be used while loading the content. If <code>null</code> the default encoding 
   *                     will be used.
   * 
   * @return   A list with the textual content. Not <code>null</code>.
   *
   * @throws FailureException in case of an io error.
   */
  public static List<String> readText( @NonNull File input, boolean trim, boolean emptylines, Encoding encoding ) {
    Reader reader = null;
    try {
      reader = Encoding.openReader( input, encoding );
      return readText( reader, trim, emptylines );
    } finally {
      MiscFunctions.close( reader );
    }
  }

  /**
   * Loads the textual content from a File. The lines will be read as is, so even empty lines will be returned.
   * 
   * @param input      The File providing the textual content. Not <code>null</code>.
   * @param encoding   The encoding to be used while loading the content. If <code>null</code> the default encoding will 
   *                   be used.
   * 
   * @return   A list with the textual content. Not <code>null</code>.
   *
   * @throws FailureException in case of an io error.
   */
  public static List<String> readText( @NonNull File input, Encoding encoding ) {
    return readText( input, false, true, encoding );
  }

  /**
   * Loads the textual content from a File. The content will be loaded as is so it's equal to the content on the disk.
   * 
   * @param input      The File providing the textual content. Not <code>null</code>.
   * @param encoding   The encoding to be used while loading the content. If <code>null</code> the default encoding will 
   *                   be used.
   * 
   * @return   The complete textual content. Not <code>null</code>.
   *
   * @throws FailureException in case of an io error.
   */
  public static String readTextAsIs( @NonNull File input, Encoding encoding ) {
    return Encoding.decode( loadBytes( input, null ), encoding );
  }

  /**
   * Loads the textual content from an InputStream. The content will be loaded as is so it's equal to the content supplied
   * by the InputStream.
   * 
   * @param instream   The InputStream providing the textual content. Not <code>null</code>.
   * @param encoding   The encoding to be used while loading the content. If <code>null</code> the default encoding will 
   *                   be used.
   * 
   * @return   The complete textual content. Not <code>null</code>.
   *
   * @throws FailureException in case of an io error.
   */
  public static String readTextAsIs( @NonNull InputStream instream, Encoding encoding ) {
    return Encoding.decode( loadBytes( instream, null ), encoding );
  }

  /**
   * Loads the textual content from a resource. The content will be loaded as is so it's equal to the content supplied
   * by the resource.
   * 
   * @param resource   The URL of the resource providing the textual content. Not <code>null</code>.
   * @param encoding   The encoding to be used while loading the content. If <code>null</code> the default encoding will 
   *                   be used.
   * 
   * @return   The complete textual content. Not <code>null</code>.
   *
   * @throws FailureException in case of an io error.
   */
  public static String readTextAsIs( @NonNull URL resource, Encoding encoding ) {
    try( InputStream instream = resource.openStream() ) {
      return readTextAsIs( instream, encoding );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( null, ex, resource );
    }
  }

  /**
   * Skip some bytes within an InputStream.
   * 
   * @param input    The InputStream providing the content. Not <code>null</code>.
   * @param offset   The location where to read the data.
   * 
   * @throws FailureException   If skipping didn't succeed.
   */
  public static void skip( @NonNull InputStream input, int offset ) {
    if( offset > 0 ) {
      try {
        if( input.skip( offset ) != offset ) {
          throw FailureCode.IO.newException();
        }
      } catch( IOException ex ) {
        throw FailureCode.IO.newException( ex );
      }
    }
  }
  
  /**
   * Skips some byte within a Reader.
   * 
   * @param input    The Reader providing the content. Not <code>null</code>.
   * @param offset   The location where to read the data.
   * 
   * @throws FailureException   If skipping didn't succeed.
   */
  public static void skip( @NonNull Reader input, int offset ) {
    if( offset > 0 ) {
      try {
        if( input.skip( offset ) != offset ) {
          throw FailureCode.IO.newException();
        }
      } catch( IOException ex ) {
        throw FailureCode.IO.newException( ex );
      }
    }
  }

  /**
   * Reads some fragment of the supplied input.
   * 
   * @param input    The InputStream providing the content. Not <code>null</code>.
   * @param offset   The location where to read the data.
   * @param length   The length of the data that has to be read. 
   * 
   * @return   The fragment or at least the beginning part of the desired fragment.
   * 
   * @throws FailureException in case the fragment could not be read.
   */
  public static byte[] loadFragment( @NonNull InputStream input, int offset, int length ) {
    skip( input, offset );
    byte[] buffer = Primitive.PByte.allocate( Integer.valueOf( length ) );
    try {
      int read = input.read( buffer, 0, length );
      if( (read != -1) && (read > 0) ) {
        byte[] result = new byte[ read ];
        System.arraycopy( buffer, 0, result, 0, read );
        return result;
      } else {
        return NO_DATA;
      }
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    } finally {
      Primitive.PByte.release( buffer );
    }
  }
  
  /**
   * Reads some fragment of the supplied input.
   * 
   * @param file     The File providing the content. Not <code>null</code>.
   * @param offset   The location where to read the data.
   * @param length   The length of the data that has to be read. 
   * 
   * @return   The fragment or at least the beginning part of the desired fragment.
   * 
   * @throws FailureException in case the fragment could not be read.
   */
  public static byte[] loadFragment( @NonNull File file, int offset, int length ) {
    InputStream input = null;
    try {
      input = newInputStream( file );
      return loadFragment( input, offset, length );
    } finally {
      MiscFunctions.close( input );
    }
  }

  /**
   * Reads some fragment of the supplied input.
   * 
   * @param url      The respource providing the content. Not <code>null</code>.
   * @param offset   The location where to read the data.
   * @param length   The length of the data that has to be read. 
   * 
   * @return   The fragment or at least the beginning part of the desired fragment.
   * 
   * @throws FailureException in case the fragment could not be read.
   */
  public static byte[] loadFragment( @NonNull URL url, int offset, int length ) {
    InputStream input = null;
    try {
      input = newInputStream( url );
      return loadFragment( input, offset, length );
    } finally {
      MiscFunctions.close( input );
    }
  }

  /**
   * Calculates the CRC32 checksum for the content delivered by an InputStream.
   * 
   * @param instream     The stream that delivers the input. Not <code>null</code>.
   * @param crc          A CRC32 object for the calculation. Maybe <code>null</code>.
   * @param buffersize   The size of the buffer to use. <code>null</code> indicates to use a default value.
   * 
   * @return   The CRC32 checksum value.
   * 
   * @throws FailureException if the accessing the stream failed for some reason.
   */
  public static long crc32( @NonNull InputStream instream, CRC32 crc, Integer buffersize ) {
    crc           = crc == null ? new CRC32() : crc;
    byte[] buffer = Primitive.PByte.allocate( buffersize );
    try {
      int read = instream.read( buffer );
      while( read != -1 ) {
        if( read > 0 ) {
          crc.update( buffer, 0, read );
        }
        read = instream.read( buffer );
      }
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    } finally {
      Primitive.PByte.release( buffer );
    }
    return crc.getValue();
  }

  /**
   * Calculates the CRC32 checksum for the content delivered by an InputStream.
   * 
   * @param instream   The stream that delivers the input. Not <code>null</code>.
   * 
   * @return   The CRC32 checksum value.
   * 
   * @throws FailureException in case io failed for some reason.
   */
  public static long crc32( @NonNull InputStream instream ) {
    return crc32( instream, null, null );
  }

  /**
   * Calculates the CRC32 checksum for the content delivered by a File.
   * 
   * @param file         The File that delivers the input. Not <code>null</code>.
   * @param crc          A CRC32 object for the calculation. Maybe <code>null</code>.
   * @param buffersize   The size of the buffer to use. <code>null</code> indicates to use a default value.
   * 
   * @return   The CRC32 checksum value.
   * 
   * @throws FailureException in case io failed for some reason.
   */
  public static long crc32( @NonNull File file, CRC32 crc, Integer buffersize ) {
    InputStream input = null;
    try {
      input = newInputStream( file );
      return crc32( input, crc, buffersize );
    } finally {
      MiscFunctions.close( input );
    }
  }

  /**
   * Calculates the CRC32 checksum for the content delivered by a File.
   * 
   * @param file   The File that delivers the input. Not <code>null</code>.
   * 
   * @return   The CRC32 checksum value.
   * 
   * @throws FailureException in case io failed for some reason.
   */
  public static long crc32( @NonNull File file ) {
    InputStream input = null;
    try {
      input = newInputStream( file );
      return crc32( input, null, null );
    } finally {
      MiscFunctions.close( input );
    }
  }

  /**
   * Returns <code>true</code> if the supplied list of files could be deletes. This method tries
   * to attempt the deletion several times if necessary.
   * 
   * @param files    A list of files. Must be not <code>null</code> (even it's elements).
   * 
   * @return   <code>true</code> <=> Deletion of all files succeeded.
   */
  public static boolean delete( @NonNull File ... files ) {
    
    List<File> entries = listRecursive( null, files );
    boolean    result  = true;
    
    // delete files first
    for( int i = entries.size() - 1; i >= 0; i-- ) {
      File entry = entries.get(i);
      if( entry.isFile() ) {
        result = deleteFile( entry.isFile(), entry, result );
      }
    }

    // delete directories
    for( int i = entries.size() - 1; i >= 0; i-- ) {
      File entry = entries.get(i);
      if( entry.isDirectory() ) {
        result = deleteFile( entry.isDirectory(), entry, result );
      }
    }

    return result;
    
  }
  
  private static boolean deleteFile( boolean execute, File file, boolean previous ) {
    if( execute ) {
      return file.delete() && previous;
    } else {
      return previous;
    }
  }
  
  /**
   * Writes the list of text lines to the supplied OutputStream.
   *  
   * @param output     The OutputStream receiving the text content. Not <code>null</code>.
   * @param lines      The text lines that have to be dumped. Not <code>null</code>.
   * @param encoding   The encoding to use. <code>null</code> means default encoding.
   * 
   * @throws FailureException if writing the text failed for some reason.
   */
  public static void writeText( @NonNull OutputStream output, @NonNull List<String> lines, Encoding encoding ) {
    try( PrintStream printer = Encoding.openPrintStream( output, encoding ) ) {
      lines.forEach( line -> printer.println( line ) );
    }
  }

  /**
   * Writes the list of text lines to the supplied Writer.
   *  
   * @param writer   The Writer receiving the text content. Not <code>null</code>.
   * @param lines    The text lines that have to be dumped. Not <code>null</code>.
   * 
   * @throws FailureException if writing the text failed for some reason.
   */
  public static void writeText( @NonNull Writer writer, @NonNull List<String> lines ) {
    try( PrintWriter printer = new PrintWriter( writer ) ) {
      lines.forEach( line -> printer.println( line ) );
    }
  }

  /**
   * Writes the list of text lines to the supplied File.
   *  
   * @param file       The File receiving the text content.
   * @param lines      The text lines that have to be dumped.
   * @param encoding   The encoding to use. <code>null</code> means default encoding.
   * 
   * @throws FailureException if writing the text failed for some reason.
   */
  public static void writeText( @NonNull File file, @NonNull List<String> lines, Encoding encoding ) {
    OutputStream output = null;
    try {
      output = newOutputStream( file );
      writeText( output, lines, encoding );
    } finally {
      MiscFunctions.close( output );
    }
  }
  
  /**
   * Writes the supplied text into an OutputStream.
   * 
   * @param output     The OutputStream used to receive the text. Not <code>null</code>.
   * @param text       The text which has to be written. Not <code>null</code>.
   * @param encoding   The encoding which has to be used. Maybe <code>null</code>.
   */
  public static void writeText( @NonNull OutputStream output, @NonNull String text, Encoding encoding ) {
    PrintStream printer = null;
    try {
      printer = Encoding.openPrintStream( output, encoding );
      printer.print( text );
    } finally {
      MiscFunctions.close( printer );
    }
  }
  
  /**
   * Writes the supplied text into a File.
   * 
   * @param file       The File used to receive the text. Not <code>null</code> and must be writable.
   * @param text       The text which has to be written. Not <code>null</code>.
   * @param encoding   The encoding which has to be used. Maybe <code>null</code>.
   */
  public static void writeText( @NonNull File file, @NonNull String text, Encoding encoding ) {
    OutputStream output = null;
    try {
      output = newOutputStream( file );
      writeText( output, text, encoding );
    } finally {
      MiscFunctions.close( output );
    }
  }
  
  /**
   * Writes some binary content to a file.
   * 
   * @param file      The destination where to write the content to. Must be writable destination.
   * @param content   The content which has to be stored. Not <code>null</code>.
   * 
   * @throws FailureException if writing the data failed for some reason.
   */
  public static void writeBytes( @NonNull File file, @NonNull byte[] content ) {
    OutputStream output = null;
    try {
      output = newOutputStream( file );
      writeBytes( output, content );
    } finally {
      MiscFunctions.close( output );
    }
  }

  /**
   * Writes some binary content to an OutputStream.
   * 
   * @param outstream   The destination where the content has to be stored to. Not <code>null</code>.
   * @param content     The content which has to be stored. Not <code>null</code>.
   * 
   * @throws FailureException if writing the data failed for some reason.
   */
  public static void writeBytes( @NonNull OutputStream outstream, @NonNull byte[] content ) {
    try {
      outstream.write( content );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
  }
  
  /**
   * Writes some character content to a file.
   * 
   * @param file       The destination where to write the content to. Must be writable destination.
   * @param content    The content which has to be stored. Not <code>null</code>.
   * @param encoding   The encoding to be used for the file. Maybe <code>null</code>.
   * 
   * @throws FailureException if writing the data failed for some reason.
   */
  public static void writeCharacters( @NonNull File file, @NonNull char[] content, Encoding encoding ) {
    Writer writer = null;
    try {
      writer = Encoding.openWriter( file, encoding );
      writeCharacters( writer, content );
    } finally {
      MiscFunctions.close( writer );
    }
  }

  /**
   * Writes some character content to a writer.
   * 
   * @param writer    The destination where the content has to be stored to. Not <code>null</code>.
   * @param content   The content which has to be stored. Not <code>null</code>.
   * 
   * @throws FailureException if writing the data failed for some reason.
   */
  public static void writeCharacters( @NonNull Writer writer, @NonNull char[] content ) {
    try {
      writer.write( content );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
  }
  
  /**
   * Iterates through the supplied resource.
   * 
   * @param filter      A FileFilter to use. Not <code>null</code>.
   * @param receiver    The list that will be filled with the desired File instances. Not <code>null</code>.
   * @param files       <code>true</code> <=> Only include file instances.
   * @param dirs        <code>true</code> <=> Only include directory instances.
   * @param path        A buffer providing the relative path. Not <code>null</code>.
   * @param current     The resource that is supposed to be processed. Not <code>null</code>.
   */
  private static void iterateFiles( FileFilter filter, List<File> receiver, boolean files, boolean dirs, StringBuilder path, File current ) {
    int oldlength = path.length();
    path.append( current.getName() );
    if( filter.accept( current ) ) {
      if( files && current.isFile() ) {
        receiver.add( current );
      }
      if( dirs && current.isDirectory() ) {
        receiver.add( current );
      }
      if( current.isDirectory() ) {
        path.append( "/" );
        File[] children = current.listFiles();
        if( children != null ) {
          for( File child : children ) {
            iterateFiles( filter, receiver, files, dirs, path, child );
          }
        }
      }
    }
    path.setLength( oldlength );
  }
  
  /**
   * Scans a directory recursively and stores them into a list.
   * 
   * @param filter         A FileFilter usable to specify additional filter criterias. If <code>null</code> accept all.
   * @param includefiles   <code>true</code> <=> Collect files in the returned list.
   * @param includedirs    <code>true</code> <=> Collect directories in the returned list.
   * @param dirs           The directories to scan. Not <code>null</code>.
   * 
   * @return   The list which collects the filesystem entries. Not <code>null</code>.
   */
  public static List<File> listRecursive( FileFilter filter, boolean includefiles, boolean includedirs, @NonNull File ... dirs ) {
    List<File> result = new ArrayList<>();
    if( filter == null ) {
      filter = ACCEPT_ALL;
    }
    StringBuilder builder = new StringBuilder();
    for( File dir : dirs ) {
      builder.setLength(0);
      iterateFiles( filter, result, includefiles, includedirs, builder, dir );
    }
    return result;
  }
  
  /**
   * Scans a directory recursively and stores them into a list.
   * 
   * @param dir            The current directory to scan. Not <code>null</code>.
   * @param filter         A FileFilter usable to specify additional filter criterias. If <code>null</code> accept all.
   * @param includefiles   <code>true</code> <=> Collect files in the returned list.
   * @param includedirs    <code>true</code> <=> Collect directories in the returned list.
   * 
   * @return   The list which collects the filesystem entries. Not <code>null</code>.
   */
  public static List<File> listRecursive( @NonNull File dir, FileFilter filter, boolean includefiles, boolean includedirs ) {
    return listRecursive( filter, includefiles, includedirs, dir );
  }

  /**
   * Scans a directory recursively and stores the collected entries into a list.
   * 
   * @param dir      The current directory to scan. Not <code>null</code>. 
   * @param filter   A FileFilter usable to specify additional filter criterias. If <code>null</code> accept all.
   */
  public static List<File> listRecursive( @NonNull File dir, FileFilter filter ) {
    return listRecursive( dir, filter, true, true );
  }
  
  /**
   * Scans a directory recursively and stores the collected entries into a list.
   * 
   * @param filter   A FileFilter usable to specify additional filter criterias. If <code>null</code> accept all.
   * @param dirs     The directories to scan. Not <code>null</code>.
   */
  public static List<File> listRecursive( FileFilter filter, @NonNull File ... dirs ) {
    return listRecursive( filter, true, true, dirs );
  }

  /**
   * Compresses the supplied directory into a zip file.
   *
   * @param zipfile      The destination zipfile. Not <code>null</code>.
   * @param dir          The directory with the whole content. Not <code>null<code>.
   * @param buffersize   The size to use for a buffer while compressing. Maybe <code>null</code>.
   * 
   * @return   <code>true</code> if the process could successfully complete.
   */
  public static boolean zip( @NonNull File zipfile, @NonNull File dir, Integer buffersize ) {
    ZipRunnable runnable = new ZipRunnable( zipfile, dir, buffersize );
    runnable.run();
    return runnable.hasCompleted();
  }

  /**
   * Unpacks a ZIP file into a destination directory.
   * 
   * @param zipfile      The ZIP file. Not <code>null</code>.
   * @param destdir      The destination directory. Not <code>null</code>.
   * @param buffersize   The buffer size used within the extraction process. A value of <code>null</code>
   *                     indicates to use the default size.
   * 
   * @return   <code>true</code> if the process could successfully complete.
   */
  public static boolean unzip( @NonNull File zipfile, @NonNull File destdir, Integer buffersize ) {
    UnzipRunnable runnable = new UnzipRunnable( zipfile, destdir, buffersize );
    runnable.run();
    return runnable.hasCompleted();
  }

  /**
   * GZIPs the supplied file.
   * 
   * @param file   The file which has to be gzipped. Not <code>null</code>.
   * 
   * @return   The gzipped file. <code>null</code> in case of an error.
   */
  public static File gzip( @NonNull File file ) {
    File gzfile = new File( file.getParentFile(), String.format( "%s.gz", file.getName() ) );
    try( 
      GZIPOutputStream outstream = new GZIPOutputStream( new FileOutputStream( gzfile ) );
      InputStream      filein    = new FileInputStream( file );
    ) {
      copy( filein, outstream );
      return gzfile;
    } catch( IOException ex ) {
      return null;
    }
  }

  /**
   * Ungzips the supplied file.
   * 
   * @param gzfile   The gzipped file which must end with <i>.gz</i>. Not <code>null</code>.
   * 
   * @return   The original file. <code>null</code> in case of an error.
   */
  public static File ungzip( @NonNull File gzfile ) {
    if( ! gzfile.getName().endsWith( ".gz" ) ) {
      return null;
    }
    File file = new File( gzfile.getParentFile(), gzfile.getName().substring( 0, gzfile.getName().length() - ".gz".length() ) );
    try( 
      OutputStream outstream = new FileOutputStream( file );
      InputStream  filein    = new GZIPInputStream( new FileInputStream( gzfile ) );
    ) {
      copy( filein, outstream );
      return file;
    } catch( IOException ex ) {
      return null;
    }
  }

  private static final String[] ARCHIVE_PREFIXES = new String[] {
    "jar:", "ear:", "zip:", "war:"
  };
  
  /**
   * Calculates the class directory/jarfile used for the supplied class instance.
   * 
   * @param classobj   The class which is used to locate the application directory. Not <code>null</code>.
   * @param skippable  If supplied immediate parental directories named as provided will be skipped. This is an easy
   *                   way to skip directories in a build environment (f.e. target/classes). Maybe <code>null</code>.
   * 
   * @return   The location of the class directory/jarfile. Not <code>null</code>.
   */
  public static File locateDirectory( @NonNull Class<?> classobj, String ... skippable ) {
    
    String         classname    = String.format( "/%s.class", classobj.getName().replace('.','/') );
        
    // find the location. since it is this class, we can assume that the URL is valid
    URL            location     = classobj.getResource( classname );
    String         externalform = location.toExternalForm();
    String         baselocation = externalform.substring( 0, externalform.length() - classname.length() );

    if( baselocation.endsWith("!") ) {
      baselocation = baselocation.substring( 0, baselocation.length() - 1 );
    }
    
    for( String prefix : ARCHIVE_PREFIXES ) {
      if( baselocation.startsWith( prefix ) ) {
        baselocation = baselocation.substring( prefix.length() );
        break;
      }
    }
    
    try {
      URI  uri  = new URI( baselocation );
      File file = new File( uri ).getCanonicalFile();
      if( file.isFile() ) {
        // might be the case if the class was located in an archive
        file = file.getParentFile();
      }
      if( (skippable != null) && (skippable.length > 0) ) {
        file = skip( file, skippable, skippable.length - 1 );
      }
      return file;
    } catch( URISyntaxException | IOException ex ) {
      // won't happen as the uri is based upon a correct URL
      return null;
    }
    
  }
  
  /**
   * Skips the parental directories as long as they match a certain list of skippable parents.
   * 
   * @param dir         The directory which might be altered. Not <code>null</code>.
   * @param skippable   The list of names for skippable parental directories. Not <code>null</code>.
   * @param index       The current index within the list of skippable parental directories.
   *  
   * @return   The desired directory. Not <code>null</code>.
   */
  private static File skip( File dir, String[] skippable, int index ) {
    if( index >= 0 ) {
      if( dir.getName().equalsIgnoreCase( skippable[ index ] ) ) {
        return skip( dir.getParentFile(), skippable, index - 1 );
      }
    }
    return dir;
  }
  
  /**
   * Creates a directory.
   * 
   * @param dir   The directory that needs to be created. Not <code>null</code> and must be a valid file.
   * 
   * @throws FailureException   The supplied directory cannot be assured to be an existing directory.
   */
  public static void mkdirs( @NonNull File dir ) {
    if( ! dir.exists() ) {
      dir.mkdirs();
    }
    if( ! dir.isDirectory() ) {
      throw FailureCode.IO.newException( null, null, dir );
    }
  }

  public static <R> R forInputStreamDo( @NonNull File file, @NonNull Function<InputStream,R> function ) {
    try( InputStream instream = newInputStream( file ) ) {
      return function.apply( instream );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
  }

  public static <R,C> R forInputStreamDo( @NonNull File file, C context, @NonNull BiFunction<InputStream,C,R> function ) {
    try( InputStream instream = newInputStream( file ) ) {
      return function.apply( instream, context );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
  }

  public static <R> R forInputStreamDo( @NonNull Path path, @NonNull Function<InputStream,R> function ) {
    try( InputStream instream = Files.newInputStream( path ) ) {
      return function.apply( instream );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
  }

  public static <R,C> R forInputStreamDo( @NonNull Path path, C context, @NonNull BiFunction<InputStream,C,R> function ) {
    try( InputStream instream = Files.newInputStream( path ) ) {
      return function.apply( instream, context );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
  }

  public static <R> R forInputStreamDo( @NonNull String path, @NonNull Function<InputStream,R> function ) {
    return forInputStreamDo( Paths.get( path ), function );
  }

  public static <R,C> R forInputStreamDo( @NonNull String path, C context, @NonNull BiFunction<InputStream,C,R> function ) {
    return forInputStreamDo( Paths.get( path ), context, function );
  }

  public static <R> R forInputStreamDo( @NonNull URI uri, @NonNull Function<InputStream,R> function ) {
    return forInputStreamDo( Paths.get( uri ), function );
  }

  public static <R,C> R forInputStreamDo( @NonNull URI uri, C context, @NonNull BiFunction<InputStream,C,R> function ) {
    return forInputStreamDo( Paths.get( uri ), context, function );
  }

  public static <R> R forInputStreamDo( @NonNull URL url, @NonNull Function<InputStream,R> function ) {
    return forInputStreamDo( toURI( url ), function );
  }

  public static <R,C> R forInputStreamDo( @NonNull URL url, C context, @NonNull BiFunction<InputStream,C,R> function ) {
    return forInputStreamDo( toURI( url ), context, function );
  }

  public static <R> R forOutputStreamDo( @NonNull File file, @NonNull Function<OutputStream,R> function ) {
    try( OutputStream outstream = newOutputStream( file ) ) {
      return function.apply( outstream );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
  }

  public static <R,C> R forOutputStreamDo( @NonNull File file, C context, @NonNull BiFunction<OutputStream,C,R> function ) {
    try( OutputStream outstream = newOutputStream( file ) ) {
      return function.apply( outstream, context );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
  }

  public static <R> R forOutputStreamDo( @NonNull Path path, @NonNull Function<OutputStream,R> function ) {
    try( OutputStream outstream = Files.newOutputStream( path ) ) {
      return function.apply( outstream );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
  }

  public static <R,C> R forOutputStreamDo( @NonNull Path path, C context, @NonNull BiFunction<OutputStream,C,R> function ) {
    try( OutputStream outstream = Files.newOutputStream( path ) ) {
      return function.apply( outstream, context );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
  }

  public static <R> R forOutputStreamDo( @NonNull String path, @NonNull Function<OutputStream,R> function ) {
    return forOutputStreamDo( Paths.get( path ), function );
  }

  public static <R,C> R forOutputStreamDo( @NonNull String path, C context, @NonNull BiFunction<OutputStream,C,R> function ) {
    return forOutputStreamDo( Paths.get( path ), context, function );
  }

  public static <R> R forOutputStreamDo( @NonNull URI uri, @NonNull Function<OutputStream,R> function ) {
    return forOutputStreamDo( Paths.get( uri ), function );
  }

  public static <R,C> R forOutputStreamDo( @NonNull URI uri, C context, @NonNull BiFunction<OutputStream,C,R> function ) {
    return forOutputStreamDo( Paths.get( uri ), context, function );
  }

  public static <R> R forOutputStreamDo( @NonNull URL url, @NonNull Function<OutputStream,R> function ) {
    return forOutputStreamDo( toURI( url ), function );
  }

  public static <R,C> R forOutputStreamDo( @NonNull URL url, C context, @NonNull BiFunction<OutputStream,C,R> function ) {
    return forOutputStreamDo( toURI( url ), context, function );
  }

  public static <R> R forReaderDo( @NonNull File file, Encoding encoding, @NonNull Function<Reader,R> function ) {
    try( Reader reader = Encoding.openReader( file, encoding ) ) {
      return function.apply( reader );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
  }

  public static <R,C> R forReaderDo( @NonNull File file, Encoding encoding, C context, @NonNull BiFunction<Reader,C,R> function ) {
    try( Reader reader = Encoding.openReader( file, encoding ) ) {
      return function.apply( reader, context );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
  }

  public static <R> R forReaderDo( @NonNull Path path, Encoding encoding, @NonNull Function<Reader,R> function ) {
    try( Reader reader = Encoding.openReader( path, encoding ) ) {
      return function.apply( reader );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
  }

  public static <R,C> R forReaderDo( @NonNull Path path, Encoding encoding, C context, @NonNull BiFunction<Reader,C,R> function ) {
    try( Reader reader = Encoding.openReader( path, encoding ) ) {
      return function.apply( reader, context );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
  }

  public static <R> R forReaderDo( @NonNull String path, Encoding encoding, @NonNull Function<Reader,R> function ) {
    return forReaderDo( Paths.get( path ), encoding, function );
  }

  public static <R,C> R forReaderDo( @NonNull String path, Encoding encoding, C context, @NonNull BiFunction<Reader,C,R> function ) {
    return forReaderDo( Paths.get( path ), encoding, context, function );
  }

  public static <R> R forReaderDo( @NonNull URI path, Encoding encoding, @NonNull Function<Reader,R> function ) {
    return forReaderDo( Paths.get( path ), encoding, function );
  }

  public static <R,C> R forReaderDo( @NonNull URI path, Encoding encoding, C context, @NonNull BiFunction<Reader,C,R> function ) {
    return forReaderDo( Paths.get( path ), encoding, context, function );
  }

  public static <R> R forWriterDo( @NonNull File file, Encoding encoding, @NonNull Function<Writer,R> function ) {
    try( Writer writer = Encoding.openWriter( file, encoding ) ) {
      return function.apply( writer );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
  }

  public static <R,C> R forWriterDo( @NonNull File file, Encoding encoding, C context, @NonNull BiFunction<Writer,C,R> function ) {
    try( Writer writer = Encoding.openWriter( file, encoding ) ) {
      return function.apply( writer, context );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
  }

  public static <R> R forWriterDo( @NonNull Path path, Encoding encoding, @NonNull Function<Writer,R> function ) {
    try( Writer writer = Encoding.openWriter( path, encoding ) ) {
      return function.apply( writer );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
  }

  public static <R,C> R forWriterDo( @NonNull Path path, Encoding encoding, C context, @NonNull BiFunction<Writer,C,R> function ) {
    try( Writer writer = Encoding.openWriter( path, encoding ) ) {
      return function.apply( writer, context );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
  }

  public static <R> R forWriterDo( @NonNull String path, Encoding encoding, @NonNull Function<Writer,R> function ) {
    return forWriterDo( Paths.get( path ), encoding, function );
  }

  public static <R,C> R forWriterDo( @NonNull String path, Encoding encoding, C context, @NonNull BiFunction<Writer,C,R> function ) {
    return forWriterDo( Paths.get( path ), encoding, context, function );
  }

  public static <R> R forWriterDo( @NonNull URI path, Encoding encoding, @NonNull Function<Writer,R> function ) {
    return forWriterDo( Paths.get( path ), encoding, function );
  }

  public static <R,C> R forWriterDo( @NonNull URI path, Encoding encoding, C context, @NonNull BiFunction<Writer,C,R> function ) {
    return forWriterDo( Paths.get( path ), encoding, context, function );
  }

  public static <R> R forReaderDo( @NonNull File file, @NonNull Function<Reader,R> function ) {
    return forReaderDo( file, null, function );
  }
    
  public static <R,C> R forReaderDo( @NonNull File file, C context, @NonNull BiFunction<Reader,C,R> function ) {
    return forReaderDo( file, null, context, function );
  }
    
  public static <R> R forReaderDo( @NonNull Path path, @NonNull Function<Reader,R> function ) {
    return forReaderDo( path, null, function );
  }
    
  public static <R,C> R forReaderDo( @NonNull Path path, C context, @NonNull BiFunction<Reader,C,R> function ) {
    return forReaderDo( path, null, context, function );
  }
    
  public static <R> R forReaderDo( @NonNull String path, @NonNull Function<Reader,R> function ) {
    return forReaderDo( path, null, function );
  }
    
  public static <R,C> R forReaderDo( @NonNull String path, C context, @NonNull BiFunction<Reader,C,R> function ) {
    return forReaderDo( path, null, context, function );
  }
    
  public static <R> R forReaderDo( @NonNull URI uri, @NonNull Function<Reader,R> function ) {
    return forReaderDo( uri, null, function );
  }
    
  public static <R,C> R forReaderDo( @NonNull URI uri, C context, @NonNull BiFunction<Reader,C,R> function ) {
    return forReaderDo( uri, null, context, function );
  }

  public static <R> R forReaderDo( @NonNull URL url, @NonNull Function<Reader,R> function ) {
    return forReaderDo( toURI( url ), null, function );
  }
    
  public static <R,C> R forReaderDo( @NonNull URL url, C context, @NonNull BiFunction<Reader,C,R> function ) {
    return forReaderDo( toURI( url ), null, context, function );
  }

  public static <R> R forReaderDo( @NonNull URL url, Encoding encoding, @NonNull Function<Reader,R> function ) {
    return forReaderDo( toURI( url ), encoding, function );
  }
      
  public static <R,C> R forReaderDo( @NonNull URL url, Encoding encoding, C context, @NonNull BiFunction<Reader,C,R> function ) {
    return forReaderDo( toURI( url ), encoding, context, function );
  }

  public static <R> R forWriterDo( @NonNull File file, @NonNull Function<Writer,R> function ) {
    return forWriterDo( file, null, function );
  }
    
  public static <R,C> R forWriterDo( @NonNull File file, C context, @NonNull BiFunction<Writer,C,R> function ) {
    return forWriterDo( file, null, context, function );
  }
    
  public static <R> R forWriterDo( @NonNull Path path, @NonNull Function<Writer,R> function ) {
    return forWriterDo( path, null, function );
  }
    
  public static <R,C> R forWriterDo( @NonNull Path path, C context, @NonNull BiFunction<Writer,C,R> function ) {
    return forWriterDo( path, null, context, function );
  }
    
  public static <R> R forWriterDo( @NonNull String path, @NonNull Function<Writer,R> function ) {
    return forWriterDo( path, null, function );
  }
    
  public static <R,C> R forWriterDo( @NonNull String path, C context, @NonNull BiFunction<Writer,C,R> function ) {
    return forWriterDo( path, null, context, function );
  }
    
  public static <R> R forWriterDo( @NonNull URI uri, @NonNull Function<Writer,R> function ) {
    return forWriterDo( uri, null, function );
  }
    
  public static <R,C> R forWriterDo( @NonNull URI uri, C context, @NonNull BiFunction<Writer,C,R> function ) {
    return forWriterDo( uri, null, context, function );
  }

  public static <R> R forWriterDo( @NonNull URL url, @NonNull Function<Writer,R> function ) {
    return forWriterDo( toURI( url ), null, function );
  }

  public static <R,C> R forWriterDo( @NonNull URL url, C context, @NonNull BiFunction<Writer,C,R> function ) {
    return forWriterDo( toURI( url ), null, context, function );
  }

  public static <R> R forWriterDo( @NonNull URL url, Encoding encoding, @NonNull Function<Writer,R> function ) {
    return forWriterDo( toURI( url ), encoding, function );
  }

  public static <R,C> R forWriterDo( @NonNull URL url, Encoding encoding, C context, @NonNull BiFunction<Writer,C,R> function ) {
    return forWriterDo( toURI( url ), encoding, context, function );
  }

  private static URI toURI( URL url ) {
    try {
      return url.toURI();
    } catch( URISyntaxException ex ) {
      throw FailureCode.IO.newException( ex );
    }
  }
  
} /* ENDCLASS */
