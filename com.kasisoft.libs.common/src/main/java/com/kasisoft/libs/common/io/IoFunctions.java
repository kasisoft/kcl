/**
 * Name........: IoFunctions
 * Description.: Collection of functions used for IO operations. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.io;

import com.kasisoft.lgpl.libs.common.constants.*;
import com.kasisoft.lgpl.libs.common.thread.*;
import com.kasisoft.lgpl.libs.common.util.*;
import com.kasisoft.lgpl.libs.common.base.*;
import com.kasisoft.lgpl.libs.common.sys.*;

import com.kasisoft.lgpl.tools.diagnostic.*;

import java.util.zip.*;
import java.util.*;

import java.nio.channels.*;

import java.net.*;
import java.io.*;

/**
 * Collection of functions used for IO operations.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class IoFunctions {

  private static final byte[]          NO_DATA     = new byte[0];

  private static final Buffers<byte[]> BYTEBUFFERS = Buffers.newBuffers( Primitive.PByte );

  /**
   * Prevent instantiation.
   */
  private IoFunctions() {
  }
  
  /**
   * Creates an instance of {@link FileInputStream} and handles potential exceptions.
   * 
   * @param file   The {@link File} that will be opened. Not <code>null</code>.
   * 
   * @return   The opened {@link FileInputStream}. Not <code>null</code>.
   */
  public static final FileInputStream newFileInputStream( @KFile(name="file") File file ) {
    try {
      return new FileInputStream( file );
    } catch( FileNotFoundException ex ) {
      throw new FailureException( FailureCode.FileNotFound, file.getAbsolutePath() );
    }
  }

  /**
   * Creates an instance of {@link FileOutputStream} and handles potential exceptions.
   * 
   * @param file   The {@link File} that will be opened. Not <code>null</code>.
   * 
   * @return   The opened {@link FileOutputStream}. Not <code>null</code>.
   */
  public static final FileOutputStream newFileOutputStream( @KFile(name="file") File file ) {
    try {
      return new FileOutputStream( file );
    } catch( FileNotFoundException ex ) {
      throw new FailureException( FailureCode.FileNotFound, file.getAbsolutePath() );
    }
  }

  /**
   * Returns a file for temporary use.
   * 
   * @return   The file usable to create a directory or a file. Not <code>null</code> and does not exist.
   */
  public static final File newTempFile() {
    return newTempFile( null, null );
  }

  /**
   * Returns a file for temporary use.
   * 
   * @param basename   The basename for the file. Maybe <code>null</code>.
   * 
   * @return   The file usable to create a directory or a file. Not <code>null</code> and does not exist.
   */
  public static final File newTempFile( String basename ) {
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
  public static final File newTempFile( String basename, String suffix ) {
    if( suffix == null ) {
      suffix    = "";
    }
    if( basename == null ) {
      basename  = "";
    }
    File dir        = CommonProperty.TempDir.getValue();
    long number     = (long) (System.currentTimeMillis() * Math.random());
    File candidate  = new File( dir, String.format( "%s%08x%s", basename, Long.valueOf( number ), suffix ) );
    while( candidate.exists() ) {
      candidate = new File( dir, String.format( "%s%08x%s", basename, Long.valueOf( number ), suffix ) );
    }
    return candidate;
  }

  /**
   * Closes the supplied channel. 
   * 
   * @param fail      <code>true</code> <=> Cause an exception if it happens.
   * @param channel   The channel that has to be closed. Maybe <code>null</code>.
   * 
   * @throws FailureException   Will be launched only when <code>fail</code> is set to true and an
   *                            exception comes up.
   *                            
   * @deprecated since 0.7; Use {@link MiscFunctions#close(boolean, Closeable)} instead.
   */
  public static final void close( boolean fail, FileChannel channel ) {
    MiscFunctions.close( fail, channel );
  }

  /**
   * Closes the supplied channel. An exception will not be launched in case of an exception. 
   * 
   * @param channel   The channel that has to be closed. Maybe <code>null</code>.
   * 
   * @deprecated since 0.7; Use {@link MiscFunctions#close(Closeable)} instead.
   */
  public static final void close( FileChannel channel ) {
    MiscFunctions.close( channel );
  }
  
  /**
   * Closes the supplied stream. 
   * 
   * @param fail     <code>true</code> <=> Cause an exception if it happens.
   * @param stream   The stream that has to be closed. Maybe <code>null</code>.
   * 
   * @throws FailureException   Will be launched only when <code>fail</code> is set to true and an
   *                            exception comes up.
   *                            
   * @deprecated since 0.7; Use {@link MiscFunctions#close(boolean, Closeable)} instead.
   */
  public static final void close( boolean fail, InputStream stream ) {
    MiscFunctions.close( fail, stream );
  }
  
  /**
   * Closes the supplied stream. An exception will not be launched in case of an exception. 
   * 
   * @param stream   The stream that has to be closed. Maybe <code>null</code>.
   * 
   * @deprecated since 0.7; Use {@link MiscFunctions#close(Closeable)} instead.
   */
  public static final void close( InputStream stream ) {
    MiscFunctions.close( stream );
  }
  
  /**
   * Closes the supplied stream.
   * 
   * @param fail     <code>true</code> <=> Cause an exception if it happens.
   * @param stream   The stream that has to be closed. Maybe <code>null</code>.
   * 
   * @throws FailureException   Will be launched only when <code>fail</code> is set to true and an
   *                            exception comes up.
   *                            
   * @deprecated since 0.7; Use {@link MiscFunctions#close(boolean, Closeable)} instead.
   */
  public static final void close( boolean fail, OutputStream stream ) {
    MiscFunctions.close( fail, stream );
  }
  
  /**
   * Closes the supplied stream. An exception will not be launched in case of an exception.
   * 
   * @param stream   The stream that has to be closed. Maybe <code>null</code>.
   * 
   * @deprecated since 0.7; Use {@link MiscFunctions#close(Closeable)} instead.
   */
  public static final void close( OutputStream stream ) {
    MiscFunctions.close( stream );
  }
  
  /**
   * Closes the supplied reader. 
   * 
   * @param fail     <code>true</code> <=> Cause an exception if it happens.
   * @param reader   The reader that has to be closed. Maybe <code>null</code>.
   * 
   * @throws FailureException   Will be launched only when <code>fail</code> is set to true and an
   *                            exception comes up.
   *                            
   * @deprecated since 0.7; Use {@link MiscFunctions#close(boolean, Closeable)} instead.
   */
  public static final void close( boolean fail, Reader reader ) {
    MiscFunctions.close( fail, reader );
  }
  
  /**
   * Closes the supplied reader. An exception will not be launched in case of an exception. 
   * 
   * @param reader   The reader that has to be closed. Maybe <code>null</code>.
   * 
   * @deprecated since 0.7; Use {@link MiscFunctions#close(Closeable)} instead.
   */
  public static final void close( Reader reader ) {
    MiscFunctions.close( reader );
  }
  
  /**
   * Closes the supplied writer.
   * 
   * @param fail     <code>true</code> <=> Cause an exception if it happens.
   * @param writer   The writer that has to be closed. Maybe <code>null</code>.
   * 
   * @throws FailureException   Will be launched only when <code>fail</code> is set to true and an
   *                            exception comes up.
   *                            
   * @deprecated since 0.7; Use {@link MiscFunctions#close(boolean, Closeable)} instead.
   */
  public static final void close( boolean fail, Writer writer ) {
    MiscFunctions.close( fail, writer );
  }
  
  /**
   * Closes the supplied writer. An exception will not be launched in case of an exception.
   * 
   * @param writer   The writer that has to be closed. Maybe <code>null</code>.
   * 
   * @deprecated since 0.7; Use {@link MiscFunctions#close(Closeable)} instead.
   */
  public static final void close( Writer writer ) {
    MiscFunctions.close( writer );
  }
  
  /**
   * Allocates some byte buffer.
   * 
   * @param size   The size of the buffer if set. <code>null</code> means to use a default value.
   * 
   * @return   The buffer itself. Not <code>null</code>.
   */
  public static final byte[] allocateBytes( Integer size ) {
    return BYTEBUFFERS.allocate( size );
  }

  /**
   * Releases the supplied buffer so it can be reused later.
   * 
   * @param buffer   The buffer which has to be released. Not <code>null</code>.
   */
  public static final void releaseBytes( 
    @KNotNull(name="buffer")   byte[]   buffer 
  ) {
    BYTEBUFFERS.release( buffer );
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
  public static final void copy( 
    @KNotNull(name="input")    InputStream    input, 
    @KNotNull(name="output")   OutputStream   output, 
                               byte[]         buffer
  ) {
    byte[] allocated = null;
    if( buffer == null ) { 
      allocated = allocateBytes( null );
      buffer    = allocated;
    }
    try {
      ByteCopierRunnable runnable = new ByteCopierRunnable();
      runnable.configure( input, output, buffer );
      runnable.run();
      if( ! runnable.hasCompleted() ) {
        throw new FailureException( FailureCode.IO );
      }
    } finally {
      if( allocated != null ) {
        releaseBytes( allocated );
      }
    }
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
  public static final void copy( 
    @KNotNull(name="input")    InputStream    input, 
    @KNotNull(name="output")   OutputStream   output
  ) {
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
  public static final void copy( 
    @KNotNull(name="input")    InputStream    input, 
    @KNotNull(name="output")   OutputStream   output, 
                               Integer        buffersize
  ) {
    byte[] allocated = allocateBytes( buffersize );
    try {
      ByteCopierRunnable runnable = new ByteCopierRunnable();
      runnable.configure( input, output, allocated );
      runnable.run();
      if( ! runnable.hasCompleted() ) {
        throw new FailureException( FailureCode.IO );
      }
    } finally {
      releaseBytes( allocated );
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
  public static final void copy( 
    @KNotNull(name="input")    InputStream    input, 
    @KNotNull(name="output")   OutputStream   output, 
                               int            buffersize
  ) {
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
  public static final void copy( 
    @KFile(name="input")                             File   input, 
    @KFile(name="output", right=KFile.Right.Write)   File   output 
  ) {
    FileInputStream   instream   = null;
    FileOutputStream  outstream  = null;
    FileChannel       inchannel  = null;
    FileChannel       outchannel = null;
    try {
      long size  = input.length();
      instream   = newFileInputStream  ( input );
      outstream  = newFileOutputStream ( output );
      inchannel  = instream  . getChannel();
      outchannel = outstream . getChannel();
      inchannel.transferTo( 0, size, outchannel );
    } catch( IOException ex ) {
      throw new FailureException( FailureCode.IO, ex );
    } finally {
      MiscFunctions.close( inchannel  );
      MiscFunctions.close( outchannel );
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
  public static final void copyDir( 
    @KDirectory(name="input")                  File      input, 
    @KDirectory(name="output", exists=false)   File      output,
                                               boolean   recursive
  ) {
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
  public static final void copy( 
    @KNotNull(name="input")    Reader   input, 
    @KNotNull(name="output")   Writer   output, 
                               char[]   buffer
  ) {
    char[] allocated = null;
    if( buffer == null ) { 
      allocated = StringFunctions.allocateChars( null );
      buffer    = allocated;
    }
    try {
      CharCopierRunnable runnable = new CharCopierRunnable();
      runnable.configure( input, output, buffer );
      runnable.run();
      if( ! runnable.hasCompleted() ) {
        throw new FailureException( FailureCode.IO );
      }
    } finally {
      if( allocated != null ) {
        StringFunctions.releaseChars( allocated );
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
  public static final void copy( 
    @KNotNull(name="input")    Reader   input, 
    @KNotNull(name="output")   Writer   output
  ) {
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
  public static final void copy( 
    @KNotNull(name="input")    Reader    input, 
    @KNotNull(name="output")   Writer    output, 
                               Integer   buffersize
  ) {
    char[] allocated = StringFunctions.allocateChars( buffersize );
    try {
      CharCopierRunnable runnable = new CharCopierRunnable();
      runnable.configure( input, output, allocated );
      runnable.run();
      if( ! runnable.hasCompleted() ) {
        throw new FailureException( FailureCode.IO );
      }
    } finally {
      StringFunctions.releaseChars( allocated );
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
  public static final void copy( 
    @KNotNull(name="input")    Reader   input, 
    @KNotNull(name="output")   Writer   output, 
                               int      buffersize
  ) {
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
  public static final byte[] loadBytes( 
    @KNotNull(name="input")   InputStream   input, 
                              Integer       buffersize
  ) {
    ByteArrayOutputStream byteout = new ByteArrayOutputStream();
    copy( input, byteout, buffersize );
    return byteout.toByteArray();
  }

  /**
   * Reads the binary content of a Reader.
   * 
   * @param input        The Reader providing the content. Not <code>null</code>.
   * @param buffersize   The buffer size to use. A value of <code>null</code> indicates to use
   *                     the default size.
   *                       
   * @return   The binary content. Not <code>null</code>.
   * 
   * @throws FailureException when the copying process fails for some reason.
   */
  public static final char[] loadChars( 
    @KNotNull(name="input")   Reader    input, 
                              Integer   buffersize
  ) {
    CharArrayWriter charout = new CharArrayWriter();
    copy( input, charout, buffersize );
    return charout.toCharArray();
  }

  /**
   * Reads the binary content of a File.
   * 
   * @param file         The File providing the content. Not <code>null</code>.
   * @param buffersize   The buffer size to use. A value of <code>null</code> indicates to use
   *                     the default size.
   *                       
   * @return   The binary content. Not <code>null</code>.
   *
   * @throws FailureException whenever the reading process fails for some reason.
   */
  public static final byte[] loadBytes( 
    @KFile(name="file")   File      file, 
                          Integer   buffersize 
  ) {
    InputStream           input   = null;
    ByteArrayOutputStream byteout = new ByteArrayOutputStream();
    try {
      input = newFileInputStream( file );
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
   * @param buffersize   The buffer size to use. A value of <code>null</code> indicates to use
   *                     the default size.
   *                       
   * @return   The binary content. Not <code>null</code>.
   *
   * @throws FailureException whenever the reading process fails for some reason.
   */
  public static final byte[] loadBytes( 
    @KNotNull(name="url")   URL       url, 
                            Integer   buffersize 
  ) {
    InputStream           input   = null;
    ByteArrayOutputStream byteout = new ByteArrayOutputStream();
    try {
      input = url.openStream();
      copy( input, byteout, buffersize );
    } catch( IOException ex ) {
      throw new FailureException( FailureCode.IO, ex );
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
  public static final void loadBytes( 
    @KNotNull(name="buffer")    byte[]        buffer, 
    @KNotNull(name="instream")  InputStream   instream, 
    @KIPositive(name="count")   int           count 
  ) throws IOException {
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
  public static final void loadChars( 
    @KNotNull(name="buffer")    char[]   buffer, 
    @KNotNull(name="instream")  Reader   reader, 
    @KIPositive(name="count")   int      count 
  ) throws IOException {
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
   * @param buffersize   The buffer size to use. A value of <code>null</code> indicates to use
   *                     the default size.
   * @param encoding     The encoding which has to be used to read the characters. If <code>null</code>
   *                     the default encoding is used.
   *                       
   * @return   The character content. Not <code>null</code>.
   *
   * @throws FailureException in case of an io error.
   */
  public static final char[] loadChars( 
    @KFile(name="input")   File       file, 
                           Integer    buffersize, 
                           Encoding   encoding 
  ) {
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
  public static final List<String> readText( 
    @KNotNull(name="input")   Reader    input, 
                              boolean   trim, 
                              boolean   emptylines 
  ) {
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
  public static final List<String> readText(
    @KNotNull(name="input")   Reader   input 
  ) {
    return readText( input, false, true );
  }
  
  /**
   * Loads the textual content from a File.
   * 
   * @param input        The File providing the textual content. Not <code>null</code>.
   * @param trim         <code>true</code> <=> Trim each line.
   * @param emptylines   <code>true</code> <=> Also include empty lines in the result.
   * @param encoding     The encoding to be used while loading the content. If <code>null</code>
   *                     the default encoding will be used.
   * 
   * @return   A list with the textual content. Not <code>null</code>.
   *
   * @throws FailureException in case of an io error.
   */
  public static final List<String> readText( 
    @KFile(name="input")   File       input, 
                           boolean    trim, 
                           boolean    emptylines,
                           Encoding   encoding
  ) {
    Reader reader = null;
    try {
      reader = Encoding.openReader( input, encoding );
      return readText( reader, trim, emptylines );
    } finally {
      MiscFunctions.close( reader );
    }
  }

  /**
   * Loads the textual content from a File. The lines will be read as is, so even empty
   * lines will be returned.
   * 
   * @param input      The File providing the textual content. Not <code>null</code>.
   * @param encoding   The encoding to be used while loading the content. If <code>null</code> the
   *                   default encoding will be used.
   * 
   * @return   A list with the textual content. Not <code>null</code>.
   *
   * @throws FailureException in case of an io error.
   */
  public static final List<String> readText( 
    @KFile(name="input")   File       input,
                           Encoding   encoding
  ) {
    Reader reader = null;
    try {
      reader = Encoding.openReader( input, encoding );
      return readText( reader, false, true );
    } finally {
      MiscFunctions.close( reader );
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
  public static final void skip( 
    @KNotNull(name="input")                 InputStream   input, 
    @KIPositive(name="offset", zero=true)   int           offset 
  ) {
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
  public static final void skip( 
    @KNotNull(name="input")                 Reader   input, 
    @KIPositive(name="offset", zero=true)   int      offset 
  ) {
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
  public static final byte[] loadFragment( 
    @KNotNull(name="input")                 InputStream   input, 
    @KIPositive(name="offset", zero=true)   int           offset, 
    @KIPositive(name="length")              int           length 
  ) {
    skip( input, offset );
    byte[] buffer = BYTEBUFFERS.allocate( Integer.valueOf( length ) );
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
      BYTEBUFFERS.release( buffer );
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
  public static final byte[] loadFragment( 
    @KFile(name="file")                     File   file, 
    @KIPositive(name="offset", zero=true)   int    offset, 
    @KIPositive(name="length")              int    length 
  ) {
    InputStream input = null;
    try {
      input = newFileInputStream( file );
      return loadFragment( input, offset, length );
    } finally {
      MiscFunctions.close( input );
    }
  }
  
  /**
   * Returns <code>true</code> if the supplied buffer indicates to be compressed using the popular
   * GZIP algorithm.
   *  
   * @param buffer   The buffer which will be tested. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The buffer seems to be compressed using GZIP.
   */
  public static final boolean isGZIP( 
    @KNotNull(name="buffer")   byte[]   buffer 
  ) {
    return (((buffer[1] << 8) | buffer[0]) & 0x0000FFFF ) == GZIPInputStream.GZIP_MAGIC;
  }

  /**
   * Returns <code>true</code> if the supplied File indicates to be compressed using the popular
   * GZIP algorithm.
   *  
   * @param file   The File that has to be tested. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The buffer seems to be compressed using GZIP.
   * 
   * @throws FailureException if loading the header failed for some reason.
   */
  public static final boolean isGZIP( 
    @KFile(name="file")   File   file 
  ) {
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
  public static final long crc32( 
    @KNotNull(name="instream")   InputStream   instream, 
                                 CRC32         crc, 
                                 Integer       buffersize 
  ) {
    crc           = crc == null ? new CRC32() : crc;
    byte[] buffer = BYTEBUFFERS.allocate( buffersize );
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
      BYTEBUFFERS.release( buffer );
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
  public static final long crc32( 
    @KNotNull(name="instream")   InputStream   instream 
  ) {
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
  public static final long crc32( 
    @KFile(name="file")   File      file, 
                          CRC32     crc, 
                          Integer   buffersize 
  ) {
    InputStream input = null;
    try {
      input = newFileInputStream( file );
      return crc32( input, crc, buffersize );
    } finally {
      MiscFunctions.close( input );
    }
  }

  /**
   * Calculates the CRC32 checksum for the content delivered by a File.
   * 
   * @param instream   The stream that delivers the input. Not <code>null</code>.
   * 
   * @return   The CRC32 checksum value.
   * 
   * @throws FailureException in case io failed for some reason.
   */
  public static final long crc32( 
    @KFile(name="file")   File   file 
  ) {
    InputStream input = null;
    try {
      input = newFileInputStream( file );
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
  public static final boolean delete( @KNotNull(name="files") File ... files ) {
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
  public static final void writeText( 
    @KNotNull(name="output")   OutputStream   output, 
    @KNotNull(name="lines")    List<String>   lines, 
                               Encoding       encoding 
  ) {
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
  public static final void writeText( 
    @KNotNull(name="writer")   Writer         writer, 
    @KNotNull(name="lines")    List<String>   lines 
  ) {
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
   * 
   * @throws FailureException if writing the text failed for some reason.
   */
  public static final void writeText( 
    @KFile(name="file", right=KFile.Right.Write)   File           file, 
    @KNotNull(name="lines")                        List<String>   lines, 
                                                   Encoding       encoding 
  ) {
    OutputStream output = null;
    try {
      output = newFileOutputStream( file );
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
  public static final void writeText( 
    @KNotNull(name="output")   OutputStream   output, 
    @KNotNull(name="text")     String         text, 
                               Encoding       encoding 
  ) {
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
  public static final void writeText( 
    @KFile(name="file", right=KFile.Right.Write)   File       file, 
    @KNotNull(name="text")                         String     text, 
                                                   Encoding   encoding 
  ) {
    OutputStream output = null;
    try {
      output = newFileOutputStream( file );
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
  public static final void writeBytes( 
    @KFile(name="file", right=KFile.Right.Write)   File     file, 
    @KNotNull(name="content")                      byte[]   content 
  ) {
    OutputStream output = null;
    try {
      output = newFileOutputStream( file );
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
  public static final void writeBytes( 
    @KNotNull(name="outstream")   OutputStream   outstream, 
    @KNotNull(name="content")     byte[]         content 
  ) {
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
   * @param encoding   The encoding to be used for the file.
   * 
   * @throws FailureException if writing the data failed for some reason.
   */
  public static final void writeCharacters( 
    @KFile(name="file", right=KFile.Right.Write)   File       file, 
    @KNotNull(name="content")                      char[]     content,
    @KNotNull(name="encoding")                     Encoding   encoding
  ) {
    Writer writer = null;
    try {
      writer = encoding.openWriter( file );
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
  public static final void writeCharacters( 
    @KNotNull(name="writer")    Writer   writer, 
    @KNotNull(name="content")   char[]   content 
  ) {
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
  public static final List<File> listRecursive( 
    @KDirectory(name="dir")   File         dir, 
                              FileFilter   filter, 
                              boolean      includefiles, 
                              boolean      includedirs 
  ) {
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
  public static final List<File> listRecursive( 
    @KDirectory(name="dir")   File         dir, 
    @KNotNull(name="filter")  FileFilter   filter 
  ) {
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
  public static final boolean zip( 
    @KFile(name="zipfile", right=KFile.Right.Write)   File      zipfile, 
    @KDirectory(name="dir")                           File      dir, 
                                                      Integer   buffersize 
  ) {
    ZipRunnable runnable = new ZipRunnable( zipfile, dir, buffersize );
    runnable.run();
    return runnable.hasCompleted();
  }

  /**
   * Unpacks a ZIP file into a destination directory.
   * 
   * @param zipfile      The ZIP file.
   * @param destdir      The destination directory.
   * @param buffersize   The buffer size used within the extraction process. A value of <code>null</code>
   *                     indicates to use the default size.
   * 
   * @return   <code>true</code> if the process could successfully complete.
   */
  public static final boolean unzip( 
    @KFile(name="zipfile")        File      zipfile, 
    @KDirectory(name="destdir")   File      destdir, 
                                  Integer   buffersize 
  ) {
    UnzipRunnable runnable = new UnzipRunnable( zipfile, destdir, buffersize );
    runnable.run();
    return runnable.hasCompleted();
  }

  /**
   * Calculates the class directory used for the supplied class instance.
   * 
   * @param classobj   The class which is used to locate the application directory. Not <code>null</code>.
   * 
   * @return   The location of the application directory or <code>null</code> in case of a failure.
   */
  public static final File locateDirectory( @KNotNull(name="classobj") Class<?> classobj ) {
    
    String        classname = String.format( "/%s.class", classobj.getName().replace('.','/') );
        
    // find the location. since it is this class, we can assume that the URL is valid
    URL           location  = classobj.getResource( classname );
    
    StringBuffer  url       = new StringBuffer( location.toExternalForm() );
    int           idx       = url.indexOf( classname );
    
    // remove everything starting with the path above
    url.delete( idx, url.length() );
    
    // remove trailing archive separators
    while( (url.length() > 0) && ( (url.charAt( url.length() - 1 ) == '!') || (url.charAt( url.length() - 1 ) == '!') ) ) {
      url.deleteCharAt( url.length() -1 );
    }
    
    // remove a leading jar-prefix 
    if( (url.indexOf( "jar:" ) == 0) || (url.indexOf( "zip:" ) == 0) ) {
      url.delete( 0, "jar:".length() );
    }
    
    // remove a leading file-prefix 
    if( url.indexOf( "file:" ) == 0 ) {
      url.delete( 0, "file:".length() );
    }
    
    // remove a leading double slash
    if( url.indexOf( "//" ) == 0 ) {
      url.delete( 0, "//".length() );
    }
    
    SystemInfo sysinfo = SystemInfo.getRunningOS();
    
    // remove the root slash which only stays on windows like systems
    if( sysinfo.isWindowsLike() ) {
      if( url.charAt(0) == '/' ) {
        url.deleteCharAt(0);
      }
    }
    
    // strip a '.jar' or '.zip' suffix in case the application is located within an archive
    if( 
      sysinfo.endsWith( url, ".jar" ) ||
      sysinfo.endsWith( url, ".ear" ) ||
      sysinfo.endsWith( url, ".war" ) ||
      sysinfo.endsWith( url, ".zip" )
    ) {
      int lidx = url.lastIndexOf( "/" );
      if( lidx == -1 ) {
        // wow, top level directory on windows which means the drive
        if( sysinfo.isWindowsLike() ) {
          lidx = url.lastIndexOf( ":" );
          url.delete( lidx + 1, url.length() );
        } else {
          // totally unexpected
          return null;
        }
      } else {
        url.delete( lidx, url.length() );
      }
    }
    
    // just make sure we're having a directory
    File result = new File( url.toString() );
    if( result.isDirectory() ) {
      try {
        result = result.getCanonicalFile();
      } catch( IOException ex ) {
        result = null;
      }
    } else {
      result = null;
    }
    
    return result;
    
  }

  /**
   * Creates a directory.
   * 
   * @param dir   The directory that needs to be created. Not <code>null</code> and must be a valid file.
   */
  public static final void mkdirs( @KFile(name="dir") File dir ) {
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
