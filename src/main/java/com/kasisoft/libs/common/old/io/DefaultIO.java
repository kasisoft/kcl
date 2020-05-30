package com.kasisoft.libs.common.old.io;

import com.kasisoft.libs.common.old.base.*;

import java.util.*;

import java.net.*;

import java.io.*;

import java.nio.file.*;

import lombok.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class DefaultIO {

  public static final KReader<String>       STRING_PATH_READER          = KReader.builder( String      . class ).build();
  public static final KReader<Path>         PATH_READER                 = KReader.builder( Path        . class ).build();
  public static final KReader<File>         FILE_READER                 = KReader.builder( File        . class ).build();
  public static final KReader<URI>          URI_READER                  = KReader.builder( URI         . class ).build();
  public static final KReader<URL>          URL_READER                  = KReader.builder( URL         . class ).build();
  public static final KReader<InputStream>  INPUTSTREAM_READER          = KReader.builder( InputStream . class ).build();

  public static final KReader<String>       STRING_PATH_READER_EX       = KReader.builder( String      . class ).errorHandler( DefaultIO::errorHandler ).build();
  public static final KReader<Path>         PATH_READER_EX              = KReader.builder( Path        . class ).errorHandler( DefaultIO::errorHandler ).build();
  public static final KReader<File>         FILE_READER_EX              = KReader.builder( File        . class ).errorHandler( DefaultIO::errorHandler ).build();
  public static final KReader<URI>          URI_READER_EX               = KReader.builder( URI         . class ).errorHandler( DefaultIO::errorHandler ).build();
  public static final KReader<URL>          URL_READER_EX               = KReader.builder( URL         . class ).errorHandler( DefaultIO::errorHandler ).build();
  public static final KReader<InputStream>  INPUTSTREAM_READER_EX       = KReader.builder( InputStream . class ).errorHandler( DefaultIO::errorHandler ).build();

  public static final KWriter<String>       STRING_PATH_WRITER          = KWriter.builder( String       . class ).build();
  public static final KWriter<Path>         PATH_WRITER                 = KWriter.builder( Path         . class ).build();
  public static final KWriter<File>         FILE_WRITER                 = KWriter.builder( File         . class ).build();
  public static final KWriter<URI>          URI_WRITER                  = KWriter.builder( URI          . class ).build();
  public static final KWriter<URL>          URL_WRITER                  = KWriter.builder( URL          . class ).build();
  public static final KWriter<OutputStream> OUTPUTSTREAM_WRITER         = KWriter.builder( OutputStream . class ).build();

  public static final KWriter<String>       STRING_PATH_WRITER_EX       = KWriter.builder( String       . class ).errorHandler( DefaultIO::errorHandler ).build();
  public static final KWriter<Path>         PATH_WRITER_EX              = KWriter.builder( Path         . class ).errorHandler( DefaultIO::errorHandler ).build();
  public static final KWriter<File>         FILE_WRITER_EX              = KWriter.builder( File         . class ).errorHandler( DefaultIO::errorHandler ).build();
  public static final KWriter<URI>          URI_WRITER_EX               = KWriter.builder( URI          . class ).errorHandler( DefaultIO::errorHandler ).build();
  public static final KWriter<URL>          URL_WRITER_EX               = KWriter.builder( URL          . class ).errorHandler( DefaultIO::errorHandler ).build();
  public static final KWriter<OutputStream> OUTPUTSTREAM_WRITER_EX      = KWriter.builder( OutputStream . class ).errorHandler( DefaultIO::errorHandler ).build();
  
  public static final KInputStream<String>  STRING_PATH_INPUTSTREAM     = KInputStream.builder( String . class ).build();
  public static final KInputStream<Path>    PATH_INPUTSTREAM            = KInputStream.builder( Path   . class ).build();
  public static final KInputStream<File>    FILE_INPUTSTREAM            = KInputStream.builder( File   . class ).build();
  public static final KInputStream<URI>     URI_INPUTSTREAM             = KInputStream.builder( URI    . class ).build();
  public static final KInputStream<URL>     URL_INPUTSTREAM             = KInputStream.builder( URL    . class ).build();

  public static final KInputStream<String>  STRING_PATH_INPUTSTREAM_EX  = KInputStream.builder( String . class ).errorHandler( DefaultIO::errorHandler ).build();
  public static final KInputStream<Path>    PATH_INPUTSTREAM_EX         = KInputStream.builder( Path   . class ).errorHandler( DefaultIO::errorHandler ).build();
  public static final KInputStream<File>    FILE_INPUTSTREAM_EX         = KInputStream.builder( File   . class ).errorHandler( DefaultIO::errorHandler ).build();
  public static final KInputStream<URI>     URI_INPUTSTREAM_EX          = KInputStream.builder( URI    . class ).errorHandler( DefaultIO::errorHandler ).build();
  public static final KInputStream<URL>     URL_INPUTSTREAM_EX          = KInputStream.builder( URL    . class ).errorHandler( DefaultIO::errorHandler ).build();

  public static final KOutputStream<String> STRING_PATH_OUTPUTSTREAM    = KOutputStream.builder( String .class ).build();
  public static final KOutputStream<Path>   PATH_OUTPUTSTREAM           = KOutputStream.builder( Path   .class ).build();
  public static final KOutputStream<File>   FILE_OUTPUTSTREAM           = KOutputStream.builder( File   .class ).build();
  public static final KOutputStream<URI>    URI_OUTPUTSTREAM            = KOutputStream.builder( URI    .class ).build();
  public static final KOutputStream<URL>    URL_OUTPUTSTREAM            = KOutputStream.builder( URL    .class ).build();

  public static final KOutputStream<String> STRING_PATH_OUTPUTSTREAM_EX = KOutputStream.builder( String .class ).errorHandler( DefaultIO::errorHandler ).build();
  public static final KOutputStream<Path>   PATH_OUTPUTSTREAM_EX        = KOutputStream.builder( Path   .class ).errorHandler( DefaultIO::errorHandler ).build();
  public static final KOutputStream<File>   FILE_OUTPUTSTREAM_EX        = KOutputStream.builder( File   .class ).errorHandler( DefaultIO::errorHandler ).build();
  public static final KOutputStream<URI>    URI_OUTPUTSTREAM_EX         = KOutputStream.builder( URI    .class ).errorHandler( DefaultIO::errorHandler ).build();
  public static final KOutputStream<URL>    URL_OUTPUTSTREAM_EX         = KOutputStream.builder( URL    .class ).errorHandler( DefaultIO::errorHandler ).build();

  private static Map<Class, KInputStream>   inputStreams;
  private static Map<Class, KOutputStream>  outputStreams;
  private static Map<Class, KInputStream>   inputStreamsEx;
  private static Map<Class, KOutputStream>  outputStreamsEx;
  private static Map<Class, KReader>        readers;
  private static Map<Class, KWriter>        writers;
  private static Map<Class, KReader>        readersEx;
  private static Map<Class, KWriter>        writersEx;
  
  static {
    
    inputStreams    = new HashMap<>();
    outputStreams   = new HashMap<>();
    inputStreamsEx  = new HashMap<>();
    outputStreamsEx = new HashMap<>();
    readers         = new HashMap<>();
    writers         = new HashMap<>();
    readersEx       = new HashMap<>();
    writersEx       = new HashMap<>();

    readers.put( String      . class, STRING_PATH_READER );
    readers.put( Path        . class, PATH_READER        );
    readers.put( File        . class, FILE_READER        );
    readers.put( URI         . class, URI_READER         );
    readers.put( URL         . class, URL_READER         );
    readers.put( InputStream . class, INPUTSTREAM_READER );

    readersEx.put( String      . class, STRING_PATH_READER_EX );
    readersEx.put( Path        . class, PATH_READER_EX        );
    readersEx.put( File        . class, FILE_READER_EX        );
    readersEx.put( URI         . class, URI_READER_EX         );
    readersEx.put( URL         . class, URL_READER_EX         );
    readersEx.put( InputStream . class, INPUTSTREAM_READER_EX );

    writers.put( String       . class, STRING_PATH_WRITER  );
    writers.put( Path         . class, PATH_WRITER         );
    writers.put( File         . class, FILE_WRITER         );
    writers.put( URI          . class, URI_WRITER          );
    writers.put( URL          . class, URL_WRITER          );
    writers.put( OutputStream . class, OUTPUTSTREAM_WRITER );

    writersEx.put( String       . class, STRING_PATH_WRITER_EX  );
    writersEx.put( Path         . class, PATH_WRITER_EX         );
    writersEx.put( File         . class, FILE_WRITER_EX         );
    writersEx.put( URI          . class, URI_WRITER_EX          );
    writersEx.put( URL          . class, URL_WRITER_EX          );
    writersEx.put( OutputStream . class, OUTPUTSTREAM_WRITER_EX );

    inputStreams.put( String . class, STRING_PATH_INPUTSTREAM );
    inputStreams.put( Path   . class, PATH_INPUTSTREAM        );
    inputStreams.put( File   . class, FILE_INPUTSTREAM        );
    inputStreams.put( URI    . class, URI_INPUTSTREAM         );
    inputStreams.put( URL    . class, URL_INPUTSTREAM         );

    inputStreamsEx.put( String . class, STRING_PATH_INPUTSTREAM_EX );
    inputStreamsEx.put( Path   . class, PATH_INPUTSTREAM_EX        );
    inputStreamsEx.put( File   . class, FILE_INPUTSTREAM_EX        );
    inputStreamsEx.put( URI    . class, URI_INPUTSTREAM_EX         );
    inputStreamsEx.put( URL    . class, URL_INPUTSTREAM_EX         );

    outputStreams.put( String . class, STRING_PATH_OUTPUTSTREAM  );
    outputStreams.put( Path   . class, PATH_OUTPUTSTREAM         );
    outputStreams.put( File   . class, FILE_OUTPUTSTREAM         );
    outputStreams.put( URI    . class, URI_OUTPUTSTREAM          );
    outputStreams.put( URL    . class, URL_OUTPUTSTREAM          );

    outputStreamsEx.put( String . class, STRING_PATH_OUTPUTSTREAM_EX  );
    outputStreamsEx.put( Path   . class, PATH_OUTPUTSTREAM_EX         );
    outputStreamsEx.put( File   . class, FILE_OUTPUTSTREAM_EX         );
    outputStreamsEx.put( URI    . class, URI_OUTPUTSTREAM_EX          );
    outputStreamsEx.put( URL    . class, URL_OUTPUTSTREAM_EX          );
    
  }

  public static <T> KReader<T> reader( @NonNull T input ) {
    return readers.get( input.getClass() );
  }

  public static <T> KReader<T> readerEx( @NonNull T input ) {
    return readersEx.get( input.getClass() );
  }

  public static <T> KWriter<T> writer( @NonNull T output ) {
    return writers.get( output.getClass() );
  }

  public static <T> KWriter<T> writersEx( @NonNull T output ) {
    return writersEx.get( output.getClass() );
  }

  public static <T> KInputStream<T> inputstream( @NonNull T input ) {
    return inputStreams.get( input.getClass() );
  }

  public static <T> KInputStream<T> inputstreamEx( @NonNull T input ) {
    return inputStreamsEx.get( input.getClass() );
  }

  public static <T> KOutputStream<T> outputstream( @NonNull T output ) {
    return outputStreams.get( output.getClass() );
  }

  public static <T> KOutputStream<T> outputstreamEx( @NonNull T output ) {
    return outputStreamsEx.get( output.getClass() );
  }

  private static <T> void errorHandler( Exception ex, T input ) {
    throw KclException.wrap( ex );
  }

} /* ENDCLAS */
