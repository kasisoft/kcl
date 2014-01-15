/**
 * Name........: IoFunctions
 * Description.: Collection of functions used for IO operations. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.io;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.sys.*;
import com.kasisoft.libs.common.thread.*;
import com.kasisoft.libs.common.util.*;

import java.util.regex.*;

import java.util.*;
import java.util.zip.*;

import java.net.*;

import java.io.*;

import lombok.*;

/**
 * Collection of functions used for IO operations.
 */
public class IoFunctions {

  private static final byte[] NO_DATA = new byte[0];

  private static final String WC1 = "([^/]+)";    // *
  private static final String WC2 = "(.+)";       // **

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
    return newInputStream( true, file );
  }

  /**
   * Creates an instance of {@link InputStream} and handles potential exceptions if enabled.
   * 
   * @param fail   <code>true</code> <=> Generate an exception upon failure or otherwise return <code>null</code>.
   * @param file   The {@link File} that will be opened. Not <code>null</code>.
   * 
   * @return   The opened {@link InputStream}. Not <code>null</code> if <param>fail</param> was <code>true</code>.
   */
  public static InputStream newInputStream( boolean fail, @NonNull File file ) {
    try {
      return new BufferedInputStream( new FileInputStream( file ) );
    } catch( FileNotFoundException ex ) {
      if( fail ) {
        throw new FailureException( FailureCode.FileNotFound, file.getAbsolutePath(), ex );
      } else {
        return null;
      }
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
    return newInputStream( true, url );
  }

  /**
   * Creates an instance of {@link InputStream} and handles potential exceptions if enabled.
   * 
   * @param fail   <code>true</code> <=> Generate an exception upon failure or otherwise return <code>null</code>.
   * @param url    The URL pointing to the resource that will be opened. Not <code>null</code>.
   * 
   * @return   The opened {@link InputStream}. Not <code>null</code> if <param>fail</param> was <code>true</code>.
   */
  public static InputStream newInputStream( boolean fail, @NonNull URL url ) {
    try {
      return new BufferedInputStream( url.openStream() );
    } catch( IOException ex ) {
      if( fail ) {
        throw new FailureException( FailureCode.IO, url.toExternalForm(), ex );
      } else {
        return null;
      }
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
    return newOutputStream( true, file );
  }

  /**
   * Creates an instance of {@link OutputStream} and handles potential exceptions if enabled.
   * 
   * @param fail   <code>true</code> <=> Generate an exception upon failure or otherwise return <code>null</code>.
   * @param file   The {@link File} that will be opened. Not <code>null</code>.
   * 
   * @return   The opened {@link OutputStream}. Not <code>null</code> if <param>fail</param> was <code>true</code>.
   */
  public static OutputStream newOutputStream( boolean fail, @NonNull File file ) {
    try {
      return new BufferedOutputStream( new FileOutputStream( file ) );
    } catch( FileNotFoundException ex ) {
      if( fail ) {
        throw new FailureException( FailureCode.FileNotFound, file.getAbsolutePath(), ex );
      } else {
        return null;
      }
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
   * Allocates some byte buffer.
   * 
   * @param size   The size of the buffer if set. <code>null</code> means to use a default value.
   * 
   * @return   The buffer itself. Not <code>null</code>.
   */
  public static byte[] allocateBytes( Integer size ) {
    return Primitive.PByte.<byte[]>getBuffers().allocate( size );
  }

  /**
   * Releases the supplied buffer so it can be reused later.
   * 
   * @param buffer   The buffer which has to be released. Not <code>null</code>.
   */
  public static void releaseBytes( @NonNull byte[] buffer ) {
    Primitive.PByte.<byte[]>getBuffers().release( buffer );
  }

  /**
   * Copies some content from an InputStream to an OutputStream.
   * 
   * @param input    The stream providing the content. Not <code>null</code>.
   * @param output   The stream receiving the content. Not <code>null</code>.
   * @param buffer   The buffer to use while copying. Maybe <code>null</code>.
   *
   * @throws FailureException whenever the copying failed for some reason.
   */
  public static void copy( @NonNull InputStream input, @NonNull OutputStream output, byte[] buffer ) {
    ByteCopierRunnable runnable = new ByteCopierRunnable( buffer );
    runnable.configure( input, output );
    runnable.run();
    if( ! runnable.hasCompleted() ) {
      throw new FailureException( FailureCode.IO );
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
   * @throws FailureException whenever the copying failed for some reason.
   */
  public static void copy( @NonNull InputStream input, @NonNull OutputStream output, Integer buffersize ) {
    ByteCopierRunnable runnable = new ByteCopierRunnable( buffersize );
    runnable.configure( input, output );
    runnable.run();
    if( ! runnable.hasCompleted() ) {
      throw new FailureException( FailureCode.IO );
    }
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
    InputStream   instream   = null;
    OutputStream  outstream  = null;
    try {
      instream   = newInputStream  ( input  );
      outstream  = newOutputStream ( output );
      copy( instream, outstream );
    } finally {
      MiscFunctions.close( instream   );
      MiscFunctions.close( outstream  );
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
   * @throws FailureException whenever the copying failed for some reason.
   */
  public static void copy( @NonNull Reader input, @NonNull Writer output, char[] buffer ) {
    CharCopierRunnable runnable = new CharCopierRunnable( buffer );
    runnable.configure( input, output );
    runnable.run();
    if( ! runnable.hasCompleted() ) {
      throw new FailureException( FailureCode.IO );
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
    CharCopierRunnable runnable = new CharCopierRunnable( buffersize );
    runnable.configure( input, output );
    runnable.run();
    if( ! runnable.hasCompleted() ) {
      throw new FailureException( FailureCode.IO );
    }
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
  public static byte[] loadBytes( @NonNull InputStream input, @NonNull Integer buffersize ) {
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
      MiscFunctions.close( true, input );
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
      MiscFunctions.close( true, input );
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
   * @throws FailureException in case of an io error.
   */
  public static List<String> readText( @NonNull Reader input, boolean trim, boolean emptylines ) {
    List<String>       result   = new ArrayList<String>();
    LineReaderRunnable runnable = new LineReaderRunnable( input, result );
    runnable.setTrim( trim );
    runnable.setEmptyLines( emptylines );
    runnable.run();
    if( ! runnable.hasCompleted() ) {
      throw new FailureException( FailureCode.IO );
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
    InputStream instream = null;
      try {
        instream = resource.openStream();
        return readTextAsIs( instream, encoding );
      } catch( IOException ex ) {
        throw new FailureException( FailureCode.IO, resource.toExternalForm(), ex );
      } finally {
        MiscFunctions.close( instream );
      }
  }

  /**
   * Skip some bytes within an InputStream.
   * 
   * @param input    The InputStream providing the content. Not <code>null</code>.
   * @param offset   The location where to read the data.
   * 
   * @throws FailureException if skipping didn't succeed.
   */
  public static void skip( @NonNull InputStream input, int offset ) {
    if( offset > 0 ) {
      try {
        if( input.skip( offset ) != offset ) {
          throw new FailureException( FailureCode.Skip );
        }
      } catch( IOException ex ) {
        throw new FailureException( FailureCode.IO, ex );
      }
    }
  }
  
  /**
   * Skips some byte within a Reader.
   * 
   * @param input    The Reader providing the content. Not <code>null</code>.
   * @param offset   The location where to read the data.
   * 
   * @throws FailureException if skipping didn't succeed.
   */
  public static void skip( @NonNull Reader input, int offset ) {
    if( offset > 0 ) {
      try {
        if( input.skip( offset ) != offset ) {
          throw new FailureException( FailureCode.Skip );
        }
      } catch( IOException ex ) {
        throw new FailureException( FailureCode.IO, ex );
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
    byte[] buffer = Primitive.PByte.<byte[]>getBuffers().allocate( Integer.valueOf( length ) );
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
      throw new FailureException( FailureCode.IO, ex );
    } finally {
      Primitive.PByte.<byte[]>getBuffers().release( buffer );
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
   * Returns <code>true</code> if the supplied buffer indicates to be compressed using the popular GZIP algorithm.
   *  
   * @param buffer   The buffer which will be tested. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The buffer seems to be compressed using GZIP.
   */
  public static boolean isGZIP( @NonNull byte[] buffer ) {
    return MagicNumber.GZIP.find( buffer );
  }

  /**
   * Returns <code>true</code> if the supplied File indicates to be compressed using the popular GZIP algorithm.
   *  
   * @param file   The File that has to be tested. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The buffer seems to be compressed using GZIP.
   * 
   * @throws FailureException if loading the header failed for some reason.
   */
  public static boolean isGZIP( @NonNull File file ) {
    byte[] fragment = loadFragment( file, 0, 2 );
    if( fragment.length == 2 ) {
      return isGZIP( fragment );
    } else {
      return false;
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
    byte[] buffer = allocateBytes( buffersize );
    try {
      int read = instream.read( buffer );
      while( read != -1 ) {
        if( read > 0 ) {
          crc.update( buffer, 0, read );
        }
        read = instream.read( buffer );
      }
    } catch( IOException ex ) {
      throw new FailureException( FailureCode.IO, ex );
    } finally {
      releaseBytes( buffer );
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
    FileDeleteRunnable runnable = new FileDeleteRunnable( files );
    runnable.run();
    return runnable.hasCompleted();
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
    PrintStream printer = null;
    try {
      printer = Encoding.openPrintStream( output, encoding );
      for( int i = 0; i < lines.size(); i++ ) {
        printer.println( lines.get(i) );
      }
    } finally {
      MiscFunctions.close( printer );
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
    PrintWriter printer = null;
    try {
      printer = new PrintWriter( writer );
      for( int i = 0; i < lines.size(); i++ ) {
        printer.println( lines.get(i) );
      }
    } finally {
      MiscFunctions.close( printer );
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
      throw new FailureException( FailureCode.IO, ex );
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
      throw new FailureException( FailureCode.IO, ex );
    }
  }
  
  /**
   * Scans a directory recursively and stores them into a list.
   * 
   * @param dir            The current directory to scan. Not <code>null</code>.
   * @param filter         A FileFilter usable to specify additional filter criterias. Maybe <code>null</code>.
   * @param includefiles   <code>true</code> <=> Collect files in the returned list.
   * @param includedirs    <code>true</code> <=> Collect directories in the returned list.
   * 
   * @return   The list which collects the filesystem entries. Not <code>null</code>.
   */
  public static List<File> listRecursive( @NonNull File dir, FileFilter filter, boolean includefiles, boolean includedirs ) {
    FileListRunnable runnable = new FileListRunnable( dir );
    runnable.setIncludeFiles( includefiles );
    runnable.setIncludeDirs( includedirs );
    runnable.setFilter( filter );
    runnable.run();
    return runnable.getAllFiles();
  }

  /**
   * Scans a directory recursively and stores the collected entries into a list.
   * 
   * @param dir      The current directory to scan. Not <code>null</code>. 
   * @param filter   A FileFilter usable to specify additional filter criterias. Not <code>null</code>.
   */
  public static List<File> listRecursive( @NonNull File dir, @NonNull FileFilter filter ) {
    return listRecursive( dir, filter, true, true );
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
   * Calculates the class directory/jarfile used for the supplied class instance.
   * 
   * @param classobj   The class which is used to locate the application directory. Not <code>null</code>.
   * 
   * @return   The location of the class directory/jarfile. Not <code>null</code>.
   */
  public static File locateDirectory( @NonNull Class<?> classobj ) {
    
    String         classname    = String.format( "/%s.class", classobj.getName().replace('.','/') );
        
    // find the location. since it is this class, we can assume that the URL is valid
    URL            location     = classobj.getResource( classname );
    String         externalform = location.toExternalForm();
    String         baselocation = externalform.substring( 0, externalform.length() - classname.length() );
    
    try {
      URI  uri  = new URI( baselocation );
      File file = new File( uri );
      return file.getCanonicalFile();
    } catch( URISyntaxException ex ) {
      // won't happen as the uri is based upon a correct URL
      return null;
    } catch( IOException        ex ) {
      // won't happen as the uri is based upon a correct URL
      return null;
    }
    
  }

  /**
   * Creates a directory.
   * 
   * @param dir   The directory that needs to be created. Not <code>null</code> and must be a valid file.
   */
  public static void mkdirs( @NonNull File dir ) {
    if( dir.exists() ) {
      if( ! dir.isDirectory() ) {
        throw new FailureException( FailureCode.CreateDirectory );
      }
    } else {
      if( ! dir.mkdirs() ) {
        throw new FailureException( FailureCode.CreateDirectory );
      }
    }
  }
  
} /* ENDCLASS */
