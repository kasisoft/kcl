package com.kasisoft.libs.common.io;

import static com.kasisoft.libs.common.constants.Primitive.*;
import static com.kasisoft.libs.common.base.LibConfig.*;
import static com.kasisoft.libs.common.io.DefaultIO.*;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.function.*;
import com.kasisoft.libs.common.sys.*;
import com.kasisoft.libs.common.thread.*;
import com.kasisoft.libs.common.util.*;

import java.util.function.*;

import java.util.regex.*;

import java.util.*;
import java.util.zip.*;

import java.net.*;

import java.io.*;

import java.nio.file.*;
import java.nio.file.attribute.*;

import lombok.experimental.*;

import lombok.*;

/**
 * Collection of functions used for IO operations.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IoFunctions {

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
    try {
      return new BufferedInputStream( new FileInputStream( file ) );
    } catch( Exception ex ) {
      throw KclException.wrap( ex );
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
      return new BufferedInputStream( Files.newInputStream( path ) );
    } catch( Exception ex ) {
      throw KclException.wrap( ex );
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
    } catch( Exception ex ) {
      throw KclException.wrap( ex );
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
    } catch( Exception ex ) {
      throw KclException.wrap( ex );
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
      return Files.newOutputStream( path, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING );
    } catch( Exception ex ) {
      throw KclException.wrap( ex );
    }
  }
  
  /**
   * Returns a {@link BufferedReader} instance.
   * 
   * @param reader   The {@link Reader} that might need wrapping. Not <code>null</code>.
   * 
   * @return   The {@link BufferedReader} instance. Not <code>null</code>.
   */
  public static BufferedReader newBufferedReader( @NonNull Reader reader ) {
    if( reader instanceof BufferedReader ) {
      return (BufferedReader) reader;
    } else {
      return new BufferedReader( reader );
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
    File dir        = cfgTempDir().toFile();
    File candidate  = null;
    do {
      long number = (long) (System.currentTimeMillis() * Math.random());
      candidate   = new File( dir, String.format( "%s%08x%s", basename, Long.valueOf( number ), suffix ) );
    } while( candidate.exists() );
    return candidate;
  }

  /**
   * Copies the content from one file to another file.
   * 
   * @param input    The file which contains the content. Must be a valid file.
   * @param output   The destination where to write the content to. Not <code>null</code>.
   * 
   * @throws KclException when the copying process fails for some reason.
   */
  public static void move( @NonNull Path input, @NonNull Path output ) {
    try {
      if( Files.isRegularFile( input ) ) {
        Files.move( input, output, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING );
      } else {
        moveDir( input, output );
      }
    } catch( Exception ex ) {
      throw KclException.wrap(ex);
    }
  }

  private static void moveDir( Path input, Path output ) throws Exception {
    Files.walkFileTree(input, new SimpleFileVisitor<Path>() {
  
      @Override
      public FileVisitResult preVisitDirectory( Path dir, BasicFileAttributes attrs ) throws IOException {
          Path destination = output.resolve( input.relativize( dir ) );
          if( ! Files.exists( destination ) ) {
              Files.createDirectory( destination );
          }
          return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult visitFile( Path file, BasicFileAttributes attrs ) throws IOException {
          Path destination = output.resolve( input.relativize( file ) );
          Files.move( file, destination, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING );
          return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult postVisitDirectory( Path dir, IOException ex ) throws IOException {
          Files.deleteIfExists( dir );
          return FileVisitResult.CONTINUE;
      }
        
    });
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
    copy( input.toPath(), output.toPath() );
  }
  
  /**
   * Copies the content from one file to another file.
   * 
   * @param input    The file which contains the content. Must be a valid file.
   * @param output   The destination where to write the content to. Not <code>null</code>.
   * 
   * @throws KclException when the copying process fails for some reason.
   */
  public static void copy( @NonNull Path input, @NonNull Path output ) {
    try {
      if( Files.isRegularFile( input ) ) {
        Files.copy( input, output, StandardCopyOption.REPLACE_EXISTING );
      } else {
        copyDir( input, output, FileVisitResult.CONTINUE );
      }
    } catch( Exception ex ) {
      throw KclException.wrap(ex);
    }
  }

  public static boolean copy( @NonNull Reader reader, @NonNull Writer writer, Consumer<Exception> errhandler ) {
    return PChar.withBuffer( $ -> copy( reader, writer, $, errhandler ) );
  }

  public static boolean copy( @NonNull Reader reader, @NonNull Writer writer, @NonNull char[] buffer, Consumer<Exception> errhandler ) {
    boolean result = false;
    try {
      int read = reader.read( buffer );
      while( read != -1 ) {
        if( read > 0 ) {
          writer.write( buffer, 0, read );
        }
        read = reader.read( buffer );
      }
      result = true;
    } catch( Exception ex ) {
      if( errhandler != null ) {
        errhandler.accept(ex);
      } else {
        throw KclException.wrap( ex );
      }
    }
    return result;
  }

  public static boolean copy( @NonNull InputStream input, @NonNull OutputStream output, Consumer<Exception> errhandler ) {
    return PByte.withBuffer( $ -> copy( input, output, $, errhandler ) );
  }
  
  public static boolean copy( @NonNull InputStream input, @NonNull OutputStream output, @NonNull byte[] buffer, Consumer<Exception> errhandler ) {
    boolean result = false;
    try {
      int read = input.read( buffer );
      while( read != -1 ) {
        if( read > 0 ) {
          output.write( buffer, 0, read );
        }
        read = input.read( buffer );
      }
      result = true;
    } catch( Exception ex ) {
      if( errhandler != null ) {
        errhandler.accept(ex);
      } else {
        throw KclException.wrap( ex );
      }
    }
    return result;
  }
  
  public static boolean copyFile( @NonNull Path input, @NonNull Path output, Consumer<Exception> errhandler ) {
    boolean result = false;
    if( Files.isRegularFile( input ) ) {
      try {
        Files.copy( input, output, StandardCopyOption.REPLACE_EXISTING );
        result = true;
      } catch( Exception ex ) {
        if( errhandler != null ) {
          errhandler.accept(ex);
        } else {
          throw KclException.wrap(ex);
        }
      }
    }
    return result;
  }
  
  private static void copyDir( final Path input, final Path output, final FileVisitResult fileVisitResult ) throws Exception {
    try {
      
      Files.walkFileTree( input, new SimpleFileVisitor<Path>() {
  
        @Override
        public FileVisitResult preVisitDirectory( Path dir, BasicFileAttributes attrs ) throws IOException {
            Path destination = output.resolve( input.relativize( dir ) );
            if( !Files.exists( destination ) ) {
                Files.createDirectories( destination );
            }
            return fileVisitResult;
        }
  
        @Override
        public FileVisitResult visitFile( Path file, BasicFileAttributes attrs ) throws IOException {
            Path destination = output.resolve( input.relativize( file ) );
            Files.copy( file, destination, StandardCopyOption.REPLACE_EXISTING );
            return FileVisitResult.CONTINUE;
        }
  
      });
      
    } catch( Exception ex ) {
      throw KclException.wrap(ex);
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
    try {
      Path inputPath  = Paths.get( input.toURI() );
      Path outputPath = Paths.get( output.toURI() );
      copyDir( inputPath, outputPath, recursive ? FileVisitResult.CONTINUE : FileVisitResult.SKIP_SUBTREE );
    } catch( Exception ex ) {
      throw KclException.wrap(ex);
    }
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
    copy( input, byteout, null );
    return byteout.toByteArray();
  }

  /**
   * Reads the binary content of an InputStream.
   * 
   * @param input   The InputStream providing the content. Not <code>null</code>.
   *                       
   * @return   The binary content. Not <code>null</code>.
   * 
   * @throws FailureException when the copying process fails for some reason.
   */
  public static byte[] loadBytes( @NonNull InputStream input ) {
    return loadBytes( input, null );
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
    copy( input, charout, null );
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
    return FILE_INPUTSTREAM_EX.readAll( file ).orElse( null );
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
    return URL_INPUTSTREAM_EX.readAll( url ).orElse( null );
  }

  /**
   * Loads a specific amount of data into a buffer.
   * 
   * @param buffer     The buffer where the data shall be written to. Not <code>null</code>.
   * @param source   The InputStream which provides the content. Not <code>null</code>.
   * @param count      The amount of data which has to be loaded.
   * 
   * @throws IOException   Loading the content failed for some reason.
   */
  public static void loadBytes( @NonNull byte[] buffer, @NonNull InputStream source, int count ) throws IOException {
    int offset  = 0; 
    int read    = source.read( buffer, offset, count );
    while( (read != -1) && (count > 0) ) {
      if( read > 0 ) {
        offset += read;
        count  -= read;
      }
      read = source.read( buffer, offset, count );
    }
  }
  
  /**
   * Loads a specific amount of data into a buffer.
   * 
   * @param buffer   The buffer where the data shall be written to. Not <code>null</code>.
   * @param source   The Reader which provides the content. Not <code>null</code>.
   * @param count    The amount of data which has to be loaded.
   * 
   * @throws IOException   Loading the content failed for some reason.
   */
  public static void loadChars( @NonNull char[] buffer, @NonNull Reader source, int count ) throws IOException {
    int offset  = 0; 
    int read    = source.read( buffer, offset, count );
    while( (read != -1) && (count > 0) ) {
      if( read > 0 ) {
        offset += read;
        count  -= read;
      }
      read = source.read( buffer, offset, count );
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
    return FILE_READER_EX.readAll( file ).orElse( null );
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
   * @throws KclException   In case of an io error.
   */
  public static List<String> readText( @NonNull Reader input, boolean trim, boolean emptylines ) {
    List<String>       result   = new ArrayList<>();
    LineReaderRunnable runnable = new LineReaderRunnable( input, result );
    runnable.setTrim( trim );
    runnable.setEmptyLines( emptylines );
    runnable.run();
    if( ! runnable.hasCompleted() ) {
      throw new KclException();
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
   * Loads the textual content from a Reader. The content will be loaded as is.
   * 
   * @param reader   The Reader providing the textual content. Not <code>null</code>.
   * 
   * @return   The complete textual content. Not <code>null</code>.
   *
   * @throws FailureException in case of an io error.
   */
  public static String readTextFully( @NonNull Reader reader ) {
    StringWriter writer = new StringWriter();
    copy( reader, writer, null );
    return writer.toString();
  }

  /**
   * Loads the textual content from a URL. The content will be loaded as is.
   * 
   * @param url   The URL providing the textual content. Not <code>null</code>.
   * 
   * @return   The complete textual content. Not <code>null</code>.
   *
   * @throws FailureException in case of an io error.
   */
  public static String readTextFully( @NonNull URL url ) {
    return URL_READER_EX.readText( url ).orElse( null );
  }
  
  /**
   * Loads the textual content from a Path. The content will be loaded as is.
   * 
   * @param path   The path providing the textual content. Not <code>null</code>.
   * 
   * @return   The complete textual content. Not <code>null</code>.
   *
   * @throws FailureException in case of an io error.
   */
  public static String readTextFully( @NonNull Path path ) {
    return PATH_READER_EX.readText( path ).orElse( null );
  }
  
  /**
   * Skip some bytes within an InputStream.
   * 
   * @param input    The InputStream providing the content. Not <code>null</code>.
   * @param offset   The location where to read the data.
   * 
   * @throws KclException   If skipping didn't succeed.
   */
  public static void skip( @NonNull InputStream input, int offset ) {
    if( offset > 0 ) {
      try {
        if( input.skip( offset ) != offset ) {
          throw new KclException();
        }
      } catch( Exception ex ) {
        throw KclException.wrap( ex );
      }
    }
  }
  
  /**
   * Skips some byte within a Reader.
   * 
   * @param input    The Reader providing the content. Not <code>null</code>.
   * @param offset   The location where to read the data.
   * 
   * @throws KclException   If skipping didn't succeed.
   */
  public static void skip( @NonNull Reader input, int offset ) {
    if( offset > 0 ) {
      try {
        if( input.skip( offset ) != offset ) {
          throw new KclException();
        }
      } catch( Exception ex ) {
        throw KclException.wrap( ex );
      }
    }
  }

  /**
   * Reads some fragment of the supplied input.
   * 
   * @param input    The InputStream providing the content. Not <code>null</code>.
   * @param length   The length of the data that has to be read starting with the first byte. 
   * 
   * @return   The fragment or at least the beginning part of the desired fragment.
   * 
   * @throws FailureException in case the fragment could not be read.
   */
  public static byte[] loadFragment( @NonNull InputStream input, int length ) {
    return loadFragment( input, 0, length );
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
   * @throws KclException in case the fragment could not be read.
   */
  public static byte[] loadFragment( @NonNull InputStream input, int offset, int length ) {
    skip( input, offset );
    byte[] buffer = PByte.allocate( Integer.valueOf( length ) );
    try {
      int read = input.read( buffer, 0, length );
      if( (read != -1) && (read > 0) ) {
        byte[] result = new byte[ read ];
        System.arraycopy( buffer, 0, result, 0, read );
        return result;
      } else {
        return Empty.NO_BYTES;
      }
    } catch( Exception ex ) {
      throw KclException.wrap( ex );
    } finally {
      PByte.release( buffer );
    }
  }
  
  /**
   * Calculates the CRC32 checksum for the content delivered by an InputStream.
   * 
   * @param buffer     The buffer to use. <code>null</code> indicates to use a default value.
   * @param instream   The stream that delivers the input. Not <code>null</code>.
   * @param crc        A CRC32 object for the calculation. Maybe <code>null</code>.
   * 
   * @return   The CRC32 checksum value.
   * 
   * @throws KclException if the accessing the stream failed for some reason.
   */
  private static long crc32( byte[] buffer, InputStream instream, CRC32 crc ) {
    crc = crc == null ? new CRC32() : crc;
    try {
      int read = instream.read( buffer );
      while( read != -1 ) {
        if( read > 0 ) {
          crc.update( buffer, 0, read );
        }
        read = instream.read( buffer );
      }
    } catch( Exception ex ) {
      throw KclException.wrap( ex );
    }
    return crc.getValue();
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
    return PByte.<Long>withBuffer( buffersize, $ -> crc32( $, instream, crc ) );
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
   * Returns <code>true</code> if the supplied list of files could be deletes. This method tries
   * to attempt the deletion several times if necessary.
   * 
   * @param files    A list of files. Must be not <code>null</code> (even it's elements).
   * 
   * @return   <code>true</code> <=> Deletion of all files succeeded.
   */
  public static boolean delete( @NonNull Path ... pathes ) {
    File[] files = new File[ pathes.length ];
    for( int i = 0; i < files.length; i++ ) {
      files[i] = pathes[i].toFile();
    }
    return delete( files );
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
      lines.forEach( printer::println );
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
      lines.forEach( printer::println );
    }
  }
  
  public static void writeText( @NonNull Writer writer, @NonNull String text ) {
    try( PrintWriter printer = new PrintWriter( writer ) ) {
      printer.print( text ); 
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
    } catch( Exception ex ) {
      throw KclException.wrap( ex );
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
    } catch( Exception ex ) {
      throw KclException.wrap( ex );
    }
  }
  
  public static Properties readProperties( @NonNull Path path ) {
    return PATH_READER_EX.forReader( path, IoFunctions::newProperties ).orElse( null );
  }

  public static Properties readProperties( @NonNull InputStream instream ) {
    return INPUTSTREAM_READER_EX.forReader( instream, IoFunctions::newProperties ).orElse( null );
  }

  private static Properties newProperties( Reader reader ) {
    Properties result = new Properties();
    try {
      result.load( reader );
    } catch( Exception ex ) {
      throw KclException.wrap( ex );
    }
    return result;
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
  private static void iterateFiles( Predicate<File> filter, List<File> receiver, boolean files, boolean dirs, StringBuilder path, File current ) {
    int oldlength = path.length();
    path.append( current.getName() );
    if( filter.test( current ) ) {
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
    List<File>      result    = new ArrayList<>();
    Predicate<File> predicate = filter != null ? ($ -> filter.accept($)) : Predicates.<File>acceptAll(); 
    StringBuilder   builder   = new StringBuilder();
    for( File dir : dirs ) {
      builder.setLength(0);
      iterateFiles( predicate, result, includefiles, includedirs, builder, dir );
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
   * Collects relative pathes.
   * 
   * @param start   The base path. Not <code>null</code>.
   * 
   * @return   A list of relative pathes. Not <code>null</code>.
   */
  public static List<String> listPathes( @NonNull Path start ) {
    return listPathes( start, null );
  }
  
  /**
   * Collects relative pathes.
   * 
   * @param start    The base path. Not <code>null</code>.
   * @param filter   A filter used to accept the relative path. Maybe <code>null</code>. 
   * 
   * @return   A list of relative pathes. Not <code>null</code>.
   */
  public static List<String> listPathes( @NonNull Path start, BiPredicate<Path, String> filter ) {
    try {
      BiPredicate<Path,String> predicate = filter != null ? filter : (x,y) -> true;
      List<String>             result    = new ArrayList<>();
      Files.walkFileTree( start, new SimpleFileVisitor<Path>() {
        @Override 
        public FileVisitResult visitFile( Path path, BasicFileAttributes attrs ) {
          String relative = start.relativize( path ).toString();
          if( predicate.test( path, relative ) ) {
            result.add( relative.replace( '\\', '/' ) );
          }
          return FileVisitResult.CONTINUE;
        }
      });
      return result;
    } catch( Exception ex ) {
      throw KclException.wrap( ex );
    }
  }

  /**
   * Collects relative pathes within the supplied archive.
   * 
   * @param start    The base path. Not <code>null</code>.
   * 
   * @return   A list of relative pathes. Not <code>null</code>.
   */
  public static List<String> listPathesInZip( @NonNull Path start ) {
    return listPathesInZip( start, null );
  }
  
  /**
   * Collects relative pathes within the supplied archive.
   * 
   * @param start    The base path. Not <code>null</code>.
   * @param filter   A filter used to accept the relative path. Maybe <code>null</code>. 
   * 
   * @return   A list of relative pathes. Not <code>null</code>.
   */
  public static List<String> listPathesInZip( @NonNull Path start, BiPredicate<Path,ZipEntry> filter ) {
    return forPathesInZip( start, filter, (p,e) -> e.getName().replace( '\\', '/' ) );
  }

  /**
   * Executes some function per entry (unless rejected). 
   * 
   * @param start      The base path. Not <code>null</code>.
   * @param filter     A filter used to accept the relative path. Maybe <code>null</code>. 
   * @param consumer   A function that will be executed. Not <code>null</code>.
   * 
   * @return   A list of records generated by the supplied transform. Not <code>null</code>.
   */
  public static void forPathesInZipDo( @NonNull Path start, BiPredicate<Path, ZipEntry> filter, @NonNull BiConsumer<ZipFile, ZipEntry> consumer ) {
    try( ZipFile zipfile = new ZipFile( new File( start.toUri() ) ) ) {
      BiPredicate<Path, ZipEntry>     predicate = filter != null ? filter : (x,y) -> true;
      Enumeration<? extends ZipEntry> entries   = zipfile.entries();
      while( entries.hasMoreElements() ) {
        ZipEntry entry = entries.nextElement();
        if( predicate.test( start, entry ) ) {
          consumer.accept( zipfile, entry );
        }
      }
    } catch( Exception ex ) {
      throw KclException.wrap( ex );
    }
  }

  /**
   * Collects a list of records per entry (unless rejected). 
   * 
   * @param start       The base path. Not <code>null</code>.
   * @param filter      A filter used to accept the relative path. Maybe <code>null</code>. 
   * @param transform   A transformer which generates the records as desired. Not <code>null</code>.
   * 
   * @return   A list of records generated by the supplied transform. Not <code>null</code>.
   */
  public static <R> List<R> forPathesInZip( @NonNull Path start, BiPredicate<Path, ZipEntry> filter, @NonNull BiFunction<ZipFile, ZipEntry, R> transform ) {
    try( ZipFile zipfile = new ZipFile( new File( start.toUri() ) ) ) {
      BiPredicate<Path,ZipEntry>      predicate = filter != null ? filter : (x,y) -> true;
      List<R>                         result    = new ArrayList<>();
      Enumeration<? extends ZipEntry> entries   = zipfile.entries();
      while( entries.hasMoreElements() ) {
        ZipEntry entry = entries.nextElement();
        if( predicate.test( start, entry ) ) {
          result.add( transform.apply( zipfile, entry ) );
        }
      }
      return result;
    } catch( Exception ex ) {
      throw KclException.wrap( ex );
    }
  }
  
  /**
   * Collects a list of records per entry. 
   * 
   * @param start       The base path. Not <code>null</code>.
   * @param transform   A transformer which generates the records as desired. Not <code>null</code>.
   * 
   * @return   A list of records generated by the supplied transform. Not <code>null</code>.
   */
  public static <R> List<R> forPathesInZip( @NonNull Path start, @NonNull BiFunction<ZipFile, ZipEntry, R> transform ) {
    return forPathesInZip( start, null, transform );
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
   * Compresses the supplied directory into a zip file.
   *
   * @param zipfile      The destination zipfile. Not <code>null</code>.
   * @param dir          The directory with the whole content. Not <code>null<code>.
   * @param buffersize   The size to use for a buffer while compressing. Maybe <code>null</code>.
   * 
   * @return   <code>true</code> if the process could successfully complete.
   */
  public static boolean zip( @NonNull Path zipfile, @NonNull Path dir, Integer buffersize ) {
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
   * Unpacks a ZIP file into a destination directory.
   * 
   * @param zipfile      The ZIP file. Not <code>null</code>.
   * @param destdir      The destination directory. Not <code>null</code>.
   * @param buffersize   The buffer size used within the extraction process. A value of <code>null</code>
   *                     indicates to use the default size.
   * 
   * @return   <code>true</code> if the process could successfully complete.
   */
  public static boolean unzip( @NonNull Path zipfile, @NonNull Path destdir, Integer buffersize ) {
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
    Path result = gzip( file.toPath() );
    if( result != null ) {
      return result.toFile();
    } else {
      return null;
    }
  }

  /**
   * GZIPs the supplied file.
   * 
   * @param file   The file which has to be gzipped. Not <code>null</code>.
   * 
   * @return   The gzipped file. <code>null</code> in case of an error.
   */
  public static Path gzip( @NonNull Path file ) {
    Path gzfile = Paths.get( file.toString() + ".gz" );
    try( 
      GZIPOutputStream outstream = new GZIPOutputStream( newOutputStream( gzfile ) );
      InputStream      filein    = newInputStream( file );
    ) {
      copy( filein, outstream, null );
      return gzfile;
    } catch( Exception ex ) {
      return null;
    }
  }

  /**
   * Ungzips the supplied file.
   * 
   * @param gzfile   The gzipped file which must end with <i>.gz</i>. Not <code>null</code>.
   * 
   * @return   The uncompressed file. <code>null</code> in case of an error.
   */
  public static File ungzip( @NonNull File gzfile ) {
    Path result = ungzip( gzfile.toPath() );
    if( result != null ) {
      return result.toFile();
    } else {
      return null;
    }
  }

  /**
   * Ungzips the supplied file.
   * 
   * @param gzfile   The gzipped file which must end with <i>.gz</i>. Not <code>null</code>.
   * 
   * @return   The uncompressed file. <code>null</code> in case of an error.
   */
  public static Path ungzip( @NonNull Path gzfile ) {
    if( ! gzfile.getFileName().toString().endsWith( ".gz" ) ) {
      return null;
    }
    String fullpath = gzfile.toString();
    Path   file     = Paths.get( fullpath.substring( 0, fullpath.length() - ".gz".length() ) );
    try( 
      OutputStream outstream = newOutputStream( file );
      InputStream  filein    = new GZIPInputStream( newInputStream( gzfile ) );
    ) {
      copy( filein, outstream, null );
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
      throw new KclException();
    }
  }

  /**
   * Creates a directory.
   * 
   * @param dir   The directory that needs to be created. Not <code>null</code> and must be a valid file.
   * 
   * @throws FailureException   The supplied directory cannot be assured to be an existing directory.
   */
  public static void mkdirs( @NonNull Path dir ) {
    if( ! Files.isDirectory( dir ) ) {
      try {
        Files.createDirectories( dir );
      } catch( Exception ex ) {
        throw KclException.wrap(ex);
      }
    }
  }

  public static Path toPath( URL url ) {
    try {
      return Paths.get( url.toURI() );
    } catch( Exception ex ) {
      throw KclException.wrap(ex);
    }
  }
  
  /**
   * Returns the nearest existing path.
   * 
   * @param path   The current path. Not <code>null</code>.
   * 
   * @return   An existing parent path or <code>null</code> if there's none.
   */
  public static Path getExisting( @NonNull Path path ) {
    Path result = path;
    while( (result != null) && (! Files.exists( result )) ) {
      result = result.getParent();
    }
    return result;
  }
  
  /**
   * Returns <code>true</code> if the supplied path allows to be written.
   * 
   * @param path   The path under test. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The supplied path allows to be written.
   */
  public static boolean isWritable( @NonNull Path path ) {
    boolean result   = false;
    Path    existing = getExisting( path );
    if( existing != null ) {
      result = Files.isWritable( existing );
    }
    return result;
  }
  
  public static Properties loadProperties( @NonNull URL url ) {
    return URL_READER_EX.forReader( url, $ -> load( new Properties(), $ ) ).orElse( null );
  }

  public static Properties loadProperties( @NonNull Path path ) {
    return PATH_READER_EX.forReader( path, $ -> load( new Properties(), $ ) ).orElse( null );
  }

  public static Properties loadProperties( @NonNull Reader reader ) {
    return load( new Properties(), reader );
  }

  private static Properties load( Properties properties, Reader reader ) {
    try {
      properties.load( reader );
      return properties;
    } catch( Exception ex ) {
      throw KclException.wrap( ex );
    }
  }
  
  public static boolean isValidZip( @NonNull Path path ) {
    boolean result = false;
    if( Files.isRegularFile( path ) ) {
      ZipFile zipfile = null;
      try {
        zipfile                                 = new ZipFile(path.toFile());
        Enumeration<? extends ZipEntry> entries = zipfile.entries();
        while( entries.hasMoreElements() ) {
          entries.nextElement();
        }
        result = true;
      } catch( Exception ex ) {
        // return false if an error occurs 
      } finally {
        MiscFunctions.close( zipfile );
      }
    }
    return result;
  }
  
} /* ENDCLASS */
