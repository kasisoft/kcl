package com.kasisoft.libs.common.io;

import static com.kasisoft.libs.common.internal.Messages.error_directory_does_not_exist;
import static com.kasisoft.libs.common.internal.Messages.error_failed_to_copy;
import static com.kasisoft.libs.common.internal.Messages.error_failed_to_create_directory;
import static com.kasisoft.libs.common.internal.Messages.error_failed_to_create_temporary_file;
import static com.kasisoft.libs.common.internal.Messages.error_failed_to_delete_directory;
import static com.kasisoft.libs.common.internal.Messages.error_failed_to_delete_file;
import static com.kasisoft.libs.common.internal.Messages.error_failed_to_gzip;
import static com.kasisoft.libs.common.internal.Messages.error_failed_to_load_gzipped;
import static com.kasisoft.libs.common.internal.Messages.error_failed_to_load_properties;
import static com.kasisoft.libs.common.internal.Messages.error_failed_to_process_zip;
import static com.kasisoft.libs.common.internal.Messages.error_failed_to_scan_dir;
import static com.kasisoft.libs.common.internal.Messages.error_failed_to_ungzip;
import static com.kasisoft.libs.common.internal.Messages.error_failed_to_unzip;
import static com.kasisoft.libs.common.internal.Messages.error_failed_to_zip;
import static com.kasisoft.libs.common.internal.Messages.error_file_does_not_exist;

import com.kasisoft.libs.common.constants.Encoding;

import com.kasisoft.libs.common.io.impl.FileIoSupport;
import com.kasisoft.libs.common.io.impl.PathIoSupport;
import com.kasisoft.libs.common.io.impl.URIIoSupport;
import com.kasisoft.libs.common.io.impl.URLIoSupport;

import com.kasisoft.libs.common.KclException;
import com.kasisoft.libs.common.functional.KBiConsumer;
import com.kasisoft.libs.common.functional.KConsumer;
import com.kasisoft.libs.common.functional.KFunction;
import com.kasisoft.libs.common.functional.KPredicate;
import com.kasisoft.libs.common.functional.Predicates;
import com.kasisoft.libs.common.pools.Buckets;
import com.kasisoft.libs.common.pools.Buffers;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import java.net.URI;
import java.net.URL;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Collection of functions used for IO operations.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IoFunctions {

  private static final String WC1 = "([^/]+)";    // *
  private static final String WC2 = "(.+)";       // **

  public static IoSupport<Path> IO_PATH = new PathIoSupport();
  
  public static IoSupport<File> IO_FILE = new FileIoSupport();
  
  public static IoSupport<URI> IO_URI = new URIIoSupport();
  
  public static IoSupport<URL> IO_URL = new URLIoSupport();

  public static @NotNull Reader newReader(@NotNull InputStream source) {
    return newReader(source, null);
  }
  
  public static @NotNull Reader newReader(@NotNull InputStream source, @Null Encoding encoding) {
    return new BufferedReader(new InputStreamReader(source, Encoding.getEncoding(encoding).getCharset()));
  }

  public static void forReaderDo(@NotNull InputStream source, @NotNull KConsumer<@NotNull Reader> function) {
    forReaderDo(source, null, function);
  }

  public static void forReaderDo(@NotNull InputStream source, @Null Encoding encoding, @NotNull KConsumer<@NotNull Reader> function) {
    try (var reader = newReader(source, encoding)) {
      function.accept(reader);
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static <R> @Null R forReader(@NotNull InputStream source, @NotNull KFunction<@NotNull Reader, @Null R> function) {
    return forReader(source, null, function);
  }

  public static <R> @Null R forReader(@NotNull InputStream source, @Null Encoding encoding, @NotNull KFunction<@NotNull Reader, @Null R> function) {
    try (var reader = newReader(source, encoding)) {
      return function.apply(reader);
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static @NotNull String readText(@NotNull InputStream instream) {
    return forReader(instream, null, IoFunctions::readText);
  }

  public static @NotNull String readText(@NotNull InputStream instream, @Null Encoding encoding) {
    return forReader(instream, encoding, IoFunctions::readText);
  }

  public static @NotNull String readText(@NotNull Reader reader) {
    return Buckets.bucketStringWriter().forInstance($sw -> {
      IoFunctions.copy(reader, $sw);
      return $sw.toString();
    });
  }

  public static @NotNull Writer newWriter(@NotNull OutputStream destination) {
    return newWriter(destination, null);
  }
  
  public static @NotNull Writer newWriter(@NotNull OutputStream destination, @Null Encoding encoding) {
    return new BufferedWriter(new OutputStreamWriter(destination, Encoding.getEncoding(encoding).getCharset()));
  }

  public static void forWriterDo(@NotNull OutputStream destination, @NotNull KConsumer<@NotNull Writer> action) {
    forWriterDo(destination, null, action);
  }

  public static void forWriterDo(@NotNull OutputStream destination, @Null Encoding encoding, @NotNull KConsumer<@NotNull Writer> action) {
    try (var writer = newWriter(destination, encoding)) {
      action.accept(writer);
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }
  
  public static void writeText(@NotNull OutputStream destination, @NotNull String text) {
    writeText(destination, null, text);
  }

  public static void writeText(@NotNull OutputStream destination, @Null Encoding encoding, @NotNull String text) {
    forWriterDo(destination, encoding, $ -> writeText($, text));
  }

  public static void writeText(@NotNull Writer writer, @NotNull String text) {
    try {
      writer.write(text);
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  public static void copy(@NotNull InputStream instream, @NotNull OutputStream outstream) {
    copy(instream, outstream, 8192);
  }
  
  public static void copy(@NotNull InputStream instream, @NotNull OutputStream outstream, @Min(1) int blockSize) {
    Buffers.BYTES.forInstanceDo(blockSize, $ -> {
      try {
        int read = instream.read($);
        while (read != -1) {
          if (read > 0) {
            outstream.write($, 0, read);
          }
          read = instream.read($);
        }
      } catch (Exception ex) {
        throw KclException.wrap(ex);
      }
    });
  }

  public static void copy(@NotNull Reader reader, @NotNull Writer writer) {
    copy(reader, writer, 8192);
  }
  
  public static void copy(@NotNull Reader reader, @NotNull Writer writer, @Min(1) int blockSize) {
    Buffers.CHARS.forInstanceDo(blockSize, $ -> {
      try {
        int read = reader.read($);
        while (read != -1) {
          if (read > 0) {
            writer.write($, 0, read);
          }
          read = reader.read($);
        }
      } catch (Exception ex) {
        throw KclException.wrap(ex);
      }
    });
  }

  /**
   * Creates a regex pattern used to match a filesystem path.
   * 
   * @param pattern   The filesystem pattern which is supposed to be compiled. Neither <code>null</code> nor empty.
   * 
   * @return   A {@link Pattern} instance used to test filesystem pathes. Not <code>null</code>.
   */
  public static Pattern compileFilesystemPattern(@NotNull String pattern) {
    return compileFilesystemPattern(pattern, 0);
  }
  
  /**
   * Creates a regex pattern used to match a filesystem path.
   * 
   * @param pattern   The filesystem pattern which is supposed to be compiled. Neither <code>null</code> nor empty.
   * @param flags     The Pattern flags.
   * 
   * @return   A {@link Pattern} instance used to test filesystem pathes. Not <code>null</code>.
   */
  public static Pattern compileFilesystemPattern(@NotNull String pattern, int flags) {
    var buffer    = new StringBuilder();
    var tokenizer = new StringTokenizer(pattern, "*", true);
    var last      = false;
    while (tokenizer.hasMoreTokens()) {
      var token = tokenizer.nextToken();
      if ("*".equals(token)) {
        if (last) {
          buffer.append(WC2);
          last = false;
        } else {
          last = true;
        }
      } else {
        if (last) {
          buffer.append(WC1);
          last = false;
        }
        buffer.append (Pattern.quote( token));
      }
    }
    if (last) {
      buffer.append(WC1);
    }
    return Pattern.compile(buffer.toString(), flags);
  }
  
  public static @NotNull Path newTempFile(@Null String prefix, @Null String suffix) {
    try {
      return Files.createTempFile(prefix, suffix);
    } catch (Exception ex) {
      throw new KclException(ex, error_failed_to_create_temporary_file, prefix, suffix);
    }
  }

  public static @NotNull Optional<Path> findNewTempFile(@Null String prefix, @Null String suffix) {
    try {
      return Optional.of(Files.createTempFile(prefix, suffix));
    } catch (Exception ex) {
      return Optional.empty();
    }
  }
  
  /**
   * Skip some bytes within an InputStream.
   * 
   * @param input    The InputStream providing the content. Not <code>null</code>.
   * @param size     The amount of bytes to be skipped.
   */
  public static void skip(@NotNull InputStream input, @Min(0) long size) {
    if (size > 0) {
      try {
        input.skipNBytes(size);
      } catch (Exception ex) {
        throw KclException.wrap(ex);
      }
    }
  }
  
  /**
   * Skips some byte within a Reader.
   * 
   * @param input    The Reader providing the content. Not <code>null</code>.
   * @param size     The amount of bytes to be skipped.
   */
  public static void skip(@NotNull Reader input, @Min(0) long size) {
    if (size > 0) {
      try {
        while (size > 0) {
          size -= input.skip(size);
        }
      } catch (Exception ex) {
        throw KclException.wrap(ex);
      }
    }
  }

  /**
  * GZIPs the supplied file.
  * 
  * @param file   The file which has to be gzipped. Not <code>null</code>.
  * 
  * @return   The gzipped file.
  */
  public static @NotNull Path gzip(@NotNull Path source) {
    return gzip(source, null);
  }
  
  /**
  * GZIPs the supplied file. Preexisting files will be overwritten.
  * 
  * @param source        The file which has to be gzipped. Not <code>null</code>.
  * @param destination   The gziped file. If <code>null</code> it's the input file plus a suffix '.gz'
  * 
  * @return   The gzipped file. <code>null</code> in case of an error.
  */
  public static @NotNull Path gzip(@NotNull Path source, @Null Path destination) {
    source     = source.normalize();
    var result = destination;
    if (result == null) {
      result = buildDefaultGzippedPath(source);
    }
    result = result.normalize();
    if (source.compareTo(result) == 0) {
      // we need to do an inline compression
      IO_PATH.saveBytes(result, loadGzipped(source));
    } else {
      try (
        var instream  = IO_PATH.newInputStream(source);
        var outstream = new GZIPOutputStream(IO_PATH.newOutputStream(result));
      ) {
        copy(instream, outstream);
      } catch (Exception ex) {
        throw KclException.wrap(ex, error_failed_to_gzip, source, result);
      }
      
    }
    return result;
  }
  
  private static @NotNull Path buildDefaultGzippedPath(@NotNull Path source) {
    var parentDir = source.toAbsolutePath().getParent();
    var fileName  = source.getFileName().toString() + ".gz";
    return parentDir.resolve(fileName).normalize();
  }
  
  public static @NotNull byte[] loadGzipped(@NotNull Path source) {
    return Buckets.bucketByteArrayOutputStream().forInstance($byteout -> {
      try (
        var instream = IO_PATH.newInputStream(source);
        var gzipOut  = new GZIPOutputStream($byteout);
      ) {
        copy(instream, gzipOut);
      } catch (Exception ex) {
        throw KclException.wrap(ex, error_failed_to_load_gzipped, source);
      }
      return $byteout.toByteArray();
    });
  }

  /**
  * UNGZIPs the supplied file.
  * 
  * @param file   The file which has to be ungzipped. Not <code>null</code>.
  * 
  * @return   The ungzipped file.
  */
  public static @NotNull Path ungzip(@NotNull Path source) {
    return ungzip(source, null);
  }
  
  /**
  * UNGZIPs the supplied file. Preexisting files will be overwritten.
  * 
  * @param source        The file which has to be ungzipped. Not <code>null</code>.
  * @param destination   The ungziped file. If <code>null</code> it's the input file plus a suffix '.gz'
  * 
  * @return   The ungzipped file. <code>null</code> in case of an error.
  */
  public static @NotNull Path ungzip(@NotNull Path source, @Null Path destination) {
    
    source     = source.normalize();
    var result = destination;
    
    if (result == null) {
      result = buildDefaultUngzippedPath(source);
    }
    
    result = result.normalize();
    
    if (source.compareTo(result) == 0) {
      // we need to do an inline compression
      IO_PATH.saveBytes(result, loadUngzipped(source));
    } else {
      try (
        var instream  = new GZIPInputStream(IO_PATH.newInputStream(source));
        var outstream = IO_PATH.newOutputStream(result);
      ) {
        copy(instream, outstream);
      } catch (Exception ex) {
        throw KclException.wrap(ex, error_failed_to_gzip, source, result);
      }
      
    }
    return result;
  }
  
  private static @NotNull Path buildDefaultUngzippedPath(@NotNull Path source) {
    var parentDir = source.toAbsolutePath().getParent();
    var fileName  = source.getFileName().toString();
    if (fileName.endsWith(".gz")) {
      fileName = fileName.substring(0, fileName.length() - ".gz".length());
    } else {
      fileName = fileName + ".ungz";
    }
    return parentDir.resolve(fileName).normalize();
  }
  
  public static @NotNull byte[] loadUngzipped(@NotNull Path source) {
    return Buckets.bucketByteArrayOutputStream().forInstance($byteout -> {
      try (
        var instream = new GZIPInputStream(IO_PATH.newInputStream(source));
      ) {
        copy(instream, $byteout);
      } catch (Exception ex) {
        throw KclException.wrap(ex, error_failed_to_ungzip, source);
      }
      return $byteout.toByteArray();
    });
  }
  
  /**
   * Returns the nearest existing path.
   * 
   * @param path   The current path.
   * 
   * @return   An existing parent path or <code>null</code> if there's none.
   */
  public static @NotNull Optional<Path> findExistingPath(@Null Path path) {
    var result = path != null ? path.normalize() : null;
    while ((result != null) && (!Files.exists(result))) {
      result = result.getParent();
    }
    return Optional.ofNullable(result);
  }
  
  public static void copyFile(@NotNull Path source, @NotNull Path destination) {
    try {
      if (!Files.isRegularFile(source)) {
        throw new KclException(error_file_does_not_exist, source);
      }
      Files.copy(source, destination, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
    } catch (Exception ex) {
      throw KclException.wrap(ex, error_failed_to_copy, source, destination);
    }
  }

  public static void moveFile(@NotNull Path source, @NotNull Path destination) {
    try {
      if (!Files.isRegularFile(source)) {
        throw new KclException("The file '%s' does not exist!", source);
      }
      Files.move(source, destination, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
    } catch (Exception ex) {
      throw KclException.wrap(ex, error_failed_to_copy, source, destination);
    }
  }

  /**
   * Creates the supplied directory including it's parent.
   * 
   * @param dir   The directory that needs to be created. Not <code>null</code> and must be a valid file.
   */
  public static void mkDirs(@NotNull Path dir) {
    try {
      Files.createDirectories(dir);
    } catch (Exception ex) {
      throw KclException.wrap(ex, error_failed_to_create_directory, dir);
    }
  }
  
  public static void copyDir(@NotNull Path source, @NotNull Path destination) {
    
    try {
      
      if (!Files.isDirectory(source)) {
        throw new KclException(error_directory_does_not_exist, source);
      }
      
      mkDirs(destination);

      Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
    
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
          var destDir = destination.resolve(source.relativize(dir));
          mkDirs(destDir);
          return FileVisitResult.CONTINUE;
        }
  
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
          var destFile = destination.resolve(source.relativize(file));
          copyFile(file, destFile);
          return FileVisitResult.CONTINUE;
        }
  
      });
      
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
    
  }
  
  public static void copy(@NotNull Path source, @NotNull Path destination) {
    if (Files.isRegularFile(source)) {
      copyFile(source, destination);
    } else {
      copyDir(source, destination);
    }
  }

  private static Path findRoot(Path path) {
    if (path.getParent() == null) {
      return path;
    }
    return findRoot(path.getParent()).normalize();
  }
  
  public static void moveDir(@NotNull Path source, @NotNull Path destination) {
    
    try {
      
      if (!Files.isDirectory(source)) {
        throw new KclException(error_directory_does_not_exist, source);
      }

      var sourceRoot = findRoot(source);
      var destRoot   = findRoot(destination);
      if (sourceRoot.compareTo(destRoot) == 0) {
        
        // same root, so we can simply move
        Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
        
      } else {
        
        mkDirs(destination);
        
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
          
          @Override
          public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            var destDir = destination.resolve(source.relativize(dir));
            mkDirs(destDir);
            return FileVisitResult.CONTINUE;
          }
          
          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            var destFile = destination.resolve(source.relativize(file));
            moveFile(file, destFile);
            return FileVisitResult.CONTINUE;
          }
          
          @Override
          public FileVisitResult postVisitDirectory(Path dir, IOException ex) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
          }

        });
        
      }
      
      
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
    
  }
  
  public static void move(@NotNull Path source, @NotNull Path destination) {
    if (Files.isRegularFile(source)) {
      moveFile(source, destination);
    } else {
      moveDir(source, destination);
    }
  }
  
  public static void deleteFile(@NotNull File file) {
    deleteFile(file.toPath());
  }

  public static void deleteFile(@NotNull URI uri) {
    deleteFile(Paths.get(uri));
  }

  public static void deleteFile(@NotNull Path file) {
    if (Files.isRegularFile(file)) {
      try {
        Files.delete(file);
      } catch (Exception ex) {
        throw KclException.wrap(ex, error_failed_to_delete_file, file);
      }
    }
  }

  public static void deleteDir(@NotNull Path dir) {
    if (Files.isDirectory(dir)) {
      try {
        
        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
          
          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
          }
          
          @Override
          public FileVisitResult postVisitDirectory(Path dir, IOException ex) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
          }

        });
        
      } catch (Exception ex) {
        throw KclException.wrap(ex, error_failed_to_delete_directory, dir);
      }
    }
  }
  
  public static void delete(@NotNull Path resource) {
    if (Files.isRegularFile(resource)) {
      deleteFile(resource);
    } else {
      deleteDir(resource);
    }
  }

  /**
   * Collects relative pathes.
   * 
   * @param start   The base path. Not <code>null</code>.
   * 
   * @return   A list of relative pathes (directories will end with a slash).
   */
  public static @NotNull List<@NotNull String> listPathes(@NotNull Path start) {
    return listPathes(start, null, true);
  }

  /**
   * Collects relative pathes.
   * 
   * @param start           The base path. Not <code>null</code>.
   * @param filter          A filter used to accept the relative path.
   * 
   * @return   A list of relative pathes (directories will end with a slash).
   */
  public static @NotNull List<@NotNull String> listPathes(@NotNull Path start, @Null KPredicate<String> filter) {
    return listPathes(start, filter, true);
  }
  
  /**
   * Collects relative pathes.
   * 
   * @param start           The base path. Not <code>null</code>.
   * @param filter          A filter used to accept the relative path.
   * @param includeDirs     <code>true</code> <=> Include directories in the result list. 
   * 
   * @return   A list of relative pathes (directories will end with a slash).
   */
  public static @NotNull List<@NotNull String> listPathes(@NotNull Path start, @Null KPredicate<String> filter, boolean includeDirs) {
    
    try {
      
      KPredicate<String> predicate = filter != null ? filter : $ -> true;
      var                result    = new ArrayList<String>(100);
      
      Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
        
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
          if (includeDirs) {
            addRelativePath(dir, true);
          }
          return FileVisitResult.CONTINUE;
        }

        @Override 
        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
          addRelativePath(path, false);
          return FileVisitResult.CONTINUE;
        }
        
        private void addRelativePath(Path current, boolean dir) throws IOException {
          String str = start.relativize(current).toString().replace('\\', '/');
          if (str.isBlank()) {
            return;
          }
          if (dir && (!str.endsWith("/"))) {
            str = str + '/';
          }
          try {
            if (predicate.test(str)) {
              result.add(str);
            }
          } catch (Exception ex) {
            throw new IOException(ex);
          }
        }
        
      });
      
      Collections.sort(result);
      
      return result;
      
    } catch (Exception ex) {
      throw KclException.wrap(ex, error_failed_to_scan_dir, start);
    }
    
  }
    
  /**
   * Executes some function per entry (unless rejected). 
   * 
   * @param zipFile    The zip file. Not <code>null</code>.
   * @param encoding   The encoding to use.
   * @param filter     A filter used to accept the relative path. 
   * @param consumer   A function that will be executed. Not <code>null</code>.
   * 
   * @return   A list of records generated by the supplied transform. Not <code>null</code>.
   */
  public static void forZipFileDo(@NotNull Path zipFile, @Null Encoding encoding, @Null KPredicate<@NotNull ZipEntry> filter, @NotNull KBiConsumer<@NotNull ZipFile, @NotNull ZipEntry> consumer) {
    if (encoding == null) {
      // the default encoding for zip files
      encoding = Encoding.IBM437;
    }
    try (var zipfile = new ZipFile(zipFile.toFile(), encoding.getCharset())) {
      var predicate = Predicates.acceptAllIfUnset(filter);
      var entries   = zipfile.entries();
      while (entries.hasMoreElements()) {
        var entry = entries.nextElement();
        if (predicate.test(entry)) {
          consumer.accept(zipfile, entry);
        }
      }
    } catch (Exception ex) {
      throw KclException.wrap(ex, error_failed_to_process_zip, zipFile);
    }
  }
  
  /**
   * Collects relative pathes within the supplied archive.
   * 
   * @param zipFile    The zip file. Not <code>null</code>.
   * 
   * @return   A list of relative pathes. Not <code>null</code>.
   */
  public static @NotNull List<@NotNull String> listZipFile(@NotNull Path zipFile) {
   return listZipFile(zipFile, null, null);
  }

  /**
   * Collects relative pathes within the supplied archive.
   * 
   * @param zipFile    The zip file. Not <code>null</code>.
   * @param encoding   The encoding to use.
   * 
   * @return   A list of relative pathes. Not <code>null</code>.
   */
  public static @NotNull List<@NotNull String> listZipFile(@NotNull Path zipFile, @Null Encoding encoding) {
   return listZipFile(zipFile, encoding, null);
  }
  
  /**
   * Collects relative pathes within the supplied archive.
   * 
   * @param zipFile    The zip file. Not <code>null</code>.
   * @param encoding   The encoding to use.
   * @param filter     A filter used to accept the relative path. 
   * 
   * @return   A list of relative pathes. Not <code>null</code>.
   */
  public static @NotNull List<@NotNull String> listZipFile(@NotNull Path zipFile, @Null Encoding encoding, @Null KPredicate<@NotNull ZipEntry> filter) {
    var result = new ArrayList<String>();
    forZipFileDo(zipFile, encoding, filter, ($z, $e) -> result.add($e.getName().replace('\\', '/')));
    return result;
  }

  public static void unzip(@NotNull Path zipFile, @NotNull Path destination) {
    unzip(zipFile, destination, null, null);
  }

  public static void unzip(@NotNull Path zipFile, @NotNull Path destination, @Null Encoding encoding) {
    unzip(zipFile, destination, encoding, null);
  }
  
  public static void unzip(@NotNull Path zipFile, @NotNull Path destination, @Null Encoding encoding, @Null KPredicate<@NotNull ZipEntry> filter) {
    forZipFileDo(zipFile, encoding, filter, ($z, $e) -> {
      
      var  dest = destination.resolve($e.getName());
      
      if ($e.isDirectory()) {
        
        mkDirs(dest);
        
      } else {
        
        mkDirs(dest.getParent());
        
        try (
          var instream  = $z.getInputStream($e);
          var outstream = IO_PATH.newOutputStream(dest);
        ) {
          copy(instream, outstream);
        } catch (Exception ex) {
          throw KclException.wrap(ex, error_failed_to_unzip, dest, zipFile, $e.getName());
        }
        
      }
      
    });
  }

  public static void zip(@NotNull Path zipFile, @NotNull Path source, @Null Encoding encoding) {
    var charset = encoding != null ? encoding.getCharset() : Encoding.IBM437.getCharset();
    var pathes = listPathes(source);
    IO_PATH.forOutputStreamDo(zipFile, $output -> {
      try (var zipout = new ZipOutputStream($output, charset)) {
        for (var path : pathes) {
          var zentry = new ZipEntry(path);
          zipout.putNextEntry(zentry);
          if (!path.endsWith("/")) {
            IO_PATH.forInputStreamDo(source.resolve(path), $input -> copy($input, zipout));
          }
          zipout.closeEntry();
        }
      } catch (Exception ex) {
        throw KclException.wrap(ex, error_failed_to_zip, zipFile);
      }
    });
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
  public static @NotNull Path locateDirectory(@NotNull Class<?> classobj, String ... skippable) {
    
    var classname    = String.format("%s.class", classobj.getName().replace('.','/'));
    var location     = classobj.getClassLoader().getResource(classname);
    var externalform = location.toExternalForm();
    var baselocation = externalform.substring(0, externalform.length() - classname.length());

    if (baselocation.endsWith("!")) {
      baselocation = baselocation.substring(0, baselocation.length() - 1);
    }
    
    for (var prefix : ARCHIVE_PREFIXES) {
      if (baselocation.startsWith(prefix)) {
        baselocation = baselocation.substring(prefix.length());
        break;
      }
    }
    
    if (baselocation.startsWith("file:")) {
      baselocation = baselocation.substring("file:".length());
    }
    
    var result = Paths.get(baselocation).normalize();
    if (Files.isRegularFile(result)) {
      // might be the case if the class was located in an archive
      result = result.getParent();
    }
    
    if ((skippable != null) && (skippable.length > 0)) {
      // get rid of directories such as 'classes', 'target' etc.
      var toSkip = Arrays.asList(skippable);
      while (toSkip.contains(result.getFileName().toString())) {
        result = result.getParent();
      }
    }
    return result;
      
  }
  
  public static @NotNull Properties loadProperties(@NotNull Reader reader) {
    Properties result = new Properties();
    try {
      result.load(reader);
    } catch (Exception ex) {
      throw KclException.wrap(ex, error_failed_to_load_properties);
    }
    return result;
  }

  public static @NotNull Optional<byte[]> genericLoadBytes(@NotNull Object source, int size) {
    IoSupport ioSupport = ioSupport(source.getClass());
    if (ioSupport != null) {
      return Optional.of(ioSupport.loadBytes(source, size));
    } else if (source instanceof CharSequence) {
      return genericLoadBytes(Paths.get(((CharSequence) source).toString()), size);
    } else if (source instanceof InputStream) {
      try {
        InputStream instream = (InputStream) source;
        var         result   = new byte[size];
        instream.readNBytes(result, 0, size);
        return Optional.of(result);
      } catch (Exception ex) {
        throw KclException.wrap(ex);
      }
    }
    return Optional.empty();
  }

  private static @Null IoSupport ioSupport(@NotNull Class clazz) {
    if (File.class.isAssignableFrom(clazz)) {
      return IO_FILE;
    } else if (Path.class.isAssignableFrom(clazz)) {
      return IO_PATH;
    } else if (URL.class.isAssignableFrom(clazz)) {
      return IO_URL;
    } else if (URI.class.isAssignableFrom(clazz)) {
      return IO_URI;
    }
    return null;
  }
  
  public static @NotNull BufferedReader newBufferedReader(@NotNull Reader reader) {
    if (reader instanceof BufferedReader) {
      return (BufferedReader) reader;
    } else {
      return new BufferedReader(reader);
    }
  }

  /* BEGIN */
  
  /**
   * @see IoSupport#forInputStream(Object, Function<InputStream,  R>)
   */
  public static <R> @Null R forInputStream(@NotNull Path source, @NotNull KFunction<InputStream,  R> function) {
    return IO_PATH.forInputStream(source, function);
  }

  /**
   * @see IoSupport#forInputStream(Object, Function<InputStream,  R>)
   */
  public static <R> @Null R forInputStream(@NotNull File source, @NotNull KFunction<InputStream,  R> function) {
    return IO_FILE.forInputStream(source, function);
  }

  /**
   * @see IoSupport#forInputStream(Object, Function<InputStream,  R>)
   */
  public static <R> @Null R forInputStream(@NotNull URI source, @NotNull KFunction<InputStream,  R> function) {
    return IO_URI.forInputStream(source, function);
  }

  /**
   * @see IoSupport#forInputStream(Object, Function<InputStream,  R>)
   */
  public static <R> @Null R forInputStream(@NotNull URL source, @NotNull KFunction<InputStream,  R> function) {
    return IO_URL.forInputStream(source, function);
  }

  /**
   * @see IoSupport#forInputStreamDo(Object, Consumer<InputStream>)
   */
  public static void forInputStreamDo(@NotNull Path source, @NotNull KConsumer<InputStream> action) {
    IO_PATH.forInputStreamDo(source, action);
  }

  /**
   * @see IoSupport#forInputStreamDo(Object, Consumer<InputStream>)
   */
  public static void forInputStreamDo(@NotNull File source, @NotNull KConsumer<InputStream> action) {
    IO_FILE.forInputStreamDo(source, action);
  }

  /**
   * @see IoSupport#forInputStreamDo(Object, Consumer<InputStream>)
   */
  public static void forInputStreamDo(@NotNull URI source, @NotNull KConsumer<InputStream> action) {
    IO_URI.forInputStreamDo(source, action);
  }

  /**
   * @see IoSupport#forInputStreamDo(Object, Consumer<InputStream>)
   */
  public static void forInputStreamDo(@NotNull URL source, @NotNull KConsumer<InputStream> action) {
    IO_URL.forInputStreamDo(source, action);
  }

  /**
   * @see IoSupport#forOutputStream(Object, Function<OutputStream,  R>)
   */
  public static <R> @Null R forOutputStream(@NotNull Path destination, @NotNull KFunction<OutputStream,  R> function) {
    return IO_PATH.forOutputStream(destination, function);
  }

  /**
   * @see IoSupport#forOutputStream(Object, Function<OutputStream,  R>)
   */
  public static <R> @Null R forOutputStream(@NotNull File destination, @NotNull KFunction<OutputStream,  R> function) {
    return IO_FILE.forOutputStream(destination, function);
  }

  /**
   * @see IoSupport#forOutputStream(Object, Function<OutputStream,  R>)
   */
  public static <R> @Null R forOutputStream(@NotNull URI destination, @NotNull KFunction<OutputStream,  R> function) {
    return IO_URI.forOutputStream(destination, function);
  }

  /**
   * @see IoSupport#forOutputStreamDo(Object, Consumer<OutputStream>)
   */
  public static void forOutputStreamDo(@NotNull Path destination, @NotNull KConsumer<OutputStream> action) {
    IO_PATH.forOutputStreamDo(destination, action);
  }

  /**
   * @see IoSupport#forOutputStreamDo(Object, Consumer<OutputStream>)
   */
  public static void forOutputStreamDo(@NotNull File destination, @NotNull KConsumer<OutputStream> action) {
    IO_FILE.forOutputStreamDo(destination, action);
  }

  /**
   * @see IoSupport#forOutputStreamDo(Object, Consumer<OutputStream>)
   */
  public static void forOutputStreamDo(@NotNull URI destination, @NotNull KConsumer<OutputStream> action) {
    IO_URI.forOutputStreamDo(destination, action);
  }

  /**
   * @see IoSupport#forReader(Object, Function<Reader,  R>)
   */
  public static <R> @Null R forReader(@NotNull Path source, @NotNull KFunction<Reader,  R> function) {
    return IO_PATH.forReader(source, function);
  }

  /**
   * @see IoSupport#forReader(Object, Function<Reader,  R>)
   */
  public static <R> @Null R forReader(@NotNull File source, @NotNull KFunction<Reader,  R> function) {
    return IO_FILE.forReader(source, function);
  }

  /**
   * @see IoSupport#forReader(Object, Function<Reader,  R>)
   */
  public static <R> @Null R forReader(@NotNull URI source, @NotNull KFunction<Reader,  R> function) {
    return IO_URI.forReader(source, function);
  }

  /**
   * @see IoSupport#forReader(Object, Function<Reader,  R>)
   */
  public static <R> @Null R forReader(@NotNull URL source, @NotNull KFunction<Reader,  R> function) {
    return IO_URL.forReader(source, function);
  }

  /**
   * @see IoSupport#forReader(Object, Encoding, Function<Reader,  R>)
   */
  public static <R> @Null R forReader(@NotNull Path source, @Null Encoding encoding, @NotNull KFunction<Reader,  R> function) {
    return IO_PATH.forReader(source, encoding, function);
  }

  /**
   * @see IoSupport#forReader(Object, Encoding, Function<Reader,  R>)
   */
  public static <R> @Null R forReader(@NotNull File source, @Null Encoding encoding, @NotNull KFunction<Reader,  R> function) {
    return IO_FILE.forReader(source, encoding, function);
  }

  /**
   * @see IoSupport#forReader(Object, Encoding, Function<Reader,  R>)
   */
  public static <R> @Null R forReader(@NotNull URI source, @Null Encoding encoding, @NotNull KFunction<Reader,  R> function) {
    return IO_URI.forReader(source, encoding, function);
  }

  /**
   * @see IoSupport#forReader(Object, Encoding, Function<Reader,  R>)
   */
  public static <R> @Null R forReader(@NotNull URL source, @Null Encoding encoding, @NotNull KFunction<Reader,  R> function) {
    return IO_URL.forReader(source, encoding, function);
  }

  /**
   * @see IoSupport#forReaderDo(Object, Consumer<Reader>)
   */
  public static void forReaderDo(@NotNull Path source, @NotNull KConsumer<Reader> action) {
    IO_PATH.forReaderDo(source, action);
  }

  /**
   * @see IoSupport#forReaderDo(Object, Consumer<Reader>)
   */
  public static void forReaderDo(@NotNull File source, @NotNull KConsumer<Reader> action) {
    IO_FILE.forReaderDo(source, action);
  }

  /**
   * @see IoSupport#forReaderDo(Object, Consumer<Reader>)
   */
  public static void forReaderDo(@NotNull URI source, @NotNull KConsumer<Reader> action) {
    IO_URI.forReaderDo(source, action);
  }

  /**
   * @see IoSupport#forReaderDo(Object, Consumer<Reader>)
   */
  public static void forReaderDo(@NotNull URL source, @NotNull KConsumer<Reader> action) {
    IO_URL.forReaderDo(source, action);
  }

  /**
   * @see IoSupport#forReaderDo(Object, Encoding, Consumer<Reader>)
   */
  public static void forReaderDo(@NotNull Path source, @Null Encoding encoding, @NotNull KConsumer<Reader> action) {
    IO_PATH.forReaderDo(source, encoding, action);
  }

  /**
   * @see IoSupport#forReaderDo(Object, Encoding, Consumer<Reader>)
   */
  public static void forReaderDo(@NotNull File source, @Null Encoding encoding, @NotNull KConsumer<Reader> action) {
    IO_FILE.forReaderDo(source, encoding, action);
  }

  /**
   * @see IoSupport#forReaderDo(Object, Encoding, Consumer<Reader>)
   */
  public static void forReaderDo(@NotNull URI source, @Null Encoding encoding, @NotNull KConsumer<Reader> action) {
    IO_URI.forReaderDo(source, encoding, action);
  }

  /**
   * @see IoSupport#forReaderDo(Object, Encoding, Consumer<Reader>)
   */
  public static void forReaderDo(@NotNull URL source, @Null Encoding encoding, @NotNull KConsumer<Reader> action) {
    IO_URL.forReaderDo(source, encoding, action);
  }

  /**
   * @see IoSupport#forWriter(Object, Function<Writer,  R>)
   */
  public static <R> @Null R forWriter(@NotNull Path destination, @NotNull KFunction<Writer,  R> function) {
    return IO_PATH.forWriter(destination, function);
  }

  /**
   * @see IoSupport#forWriter(Object, Function<Writer,  R>)
   */
  public static <R> @Null R forWriter(@NotNull File destination, @NotNull KFunction<Writer,  R> function) {
    return IO_FILE.forWriter(destination, function);
  }

  /**
   * @see IoSupport#forWriter(Object, Function<Writer,  R>)
   */
  public static <R> @Null R forWriter(@NotNull URI destination, @NotNull KFunction<Writer,  R> function) {
    return IO_URI.forWriter(destination, function);
  }

  /**
   * @see IoSupport#forWriter(Object, Encoding, Function<Writer,  R>)
   */
  public static <R> @Null R forWriter(@NotNull Path destination, @Null Encoding encoding, @NotNull KFunction<Writer,  R> function) {
    return IO_PATH.forWriter(destination, encoding, function);
  }

  /**
   * @see IoSupport#forWriter(Object, Encoding, Function<Writer,  R>)
   */
  public static <R> @Null R forWriter(@NotNull File destination, @Null Encoding encoding, @NotNull KFunction<Writer,  R> function) {
    return IO_FILE.forWriter(destination, encoding, function);
  }

  /**
   * @see IoSupport#forWriter(Object, Encoding, Function<Writer,  R>)
   */
  public static <R> @Null R forWriter(@NotNull URI destination, @Null Encoding encoding, @NotNull KFunction<Writer,  R> function) {
    return IO_URI.forWriter(destination, encoding, function);
  }

  /**
   * @see IoSupport#forWriterDo(Object, Consumer<Writer>)
   */
  public static void forWriterDo(@NotNull Path destination, @NotNull KConsumer<Writer> action) {
    IO_PATH.forWriterDo(destination, action);
  }

  /**
   * @see IoSupport#forWriterDo(Object, Consumer<Writer>)
   */
  public static void forWriterDo(@NotNull File destination, @NotNull KConsumer<Writer> action) {
    IO_FILE.forWriterDo(destination, action);
  }

  /**
   * @see IoSupport#forWriterDo(Object, Consumer<Writer>)
   */
  public static void forWriterDo(@NotNull URI destination, @NotNull KConsumer<Writer> action) {
    IO_URI.forWriterDo(destination, action);
  }

  /**
   * @see IoSupport#forWriterDo(Object, Encoding, Consumer<Writer>)
   */
  public static void forWriterDo(@NotNull Path destination, @Null Encoding encoding, @NotNull KConsumer<Writer> action) {
    IO_PATH.forWriterDo(destination, encoding, action);
  }

  /**
   * @see IoSupport#forWriterDo(Object, Encoding, Consumer<Writer>)
   */
  public static void forWriterDo(@NotNull File destination, @Null Encoding encoding, @NotNull KConsumer<Writer> action) {
    IO_FILE.forWriterDo(destination, encoding, action);
  }

  /**
   * @see IoSupport#forWriterDo(Object, Encoding, Consumer<Writer>)
   */
  public static void forWriterDo(@NotNull URI destination, @Null Encoding encoding, @NotNull KConsumer<Writer> action) {
    IO_URI.forWriterDo(destination, encoding, action);
  }

  /**
   * @see IoSupport#loadAllBytes(Object)
   */
  public static byte[] loadAllBytes(@NotNull Path source) {
    return IO_PATH.loadAllBytes(source);
  }

  /**
   * @see IoSupport#loadAllBytes(Object)
   */
  public static byte[] loadAllBytes(@NotNull File source) {
    return IO_FILE.loadAllBytes(source);
  }

  /**
   * @see IoSupport#loadAllBytes(Object)
   */
  public static byte[] loadAllBytes(@NotNull URI source) {
    return IO_URI.loadAllBytes(source);
  }

  /**
   * @see IoSupport#loadAllBytes(Object)
   */
  public static byte[] loadAllBytes(@NotNull URL source) {
    return IO_URL.loadAllBytes(source);
  }

  /**
   * @see IoSupport#loadAllBytes(Object, int)
   */
  public static byte[] loadAllBytes(@NotNull Path source, @Min(0L) int offset) {
    return IO_PATH.loadAllBytes(source, offset);
  }

  /**
   * @see IoSupport#loadAllBytes(Object, int)
   */
  public static byte[] loadAllBytes(@NotNull File source, @Min(0L) int offset) {
    return IO_FILE.loadAllBytes(source, offset);
  }

  /**
   * @see IoSupport#loadAllBytes(Object, int)
   */
  public static byte[] loadAllBytes(@NotNull URI source, @Min(0L) int offset) {
    return IO_URI.loadAllBytes(source, offset);
  }

  /**
   * @see IoSupport#loadAllBytes(Object, int)
   */
  public static byte[] loadAllBytes(@NotNull URL source, @Min(0L) int offset) {
    return IO_URL.loadAllBytes(source, offset);
  }

  /**
   * @see IoSupport#loadAllChars(Object)
   */
  public static char[] loadAllChars(@NotNull Path source) {
    return IO_PATH.loadAllChars(source);
  }

  /**
   * @see IoSupport#loadAllChars(Object)
   */
  public static char[] loadAllChars(@NotNull File source) {
    return IO_FILE.loadAllChars(source);
  }

  /**
   * @see IoSupport#loadAllChars(Object)
   */
  public static char[] loadAllChars(@NotNull URI source) {
    return IO_URI.loadAllChars(source);
  }

  /**
   * @see IoSupport#loadAllChars(Object)
   */
  public static char[] loadAllChars(@NotNull URL source) {
    return IO_URL.loadAllChars(source);
  }

  /**
   * @see IoSupport#loadAllChars(Object, int)
   */
  public static char[] loadAllChars(@NotNull Path source, @Min(0L) int offset) {
    return IO_PATH.loadAllChars(source, offset);
  }

  /**
   * @see IoSupport#loadAllChars(Object, int)
   */
  public static char[] loadAllChars(@NotNull File source, @Min(0L) int offset) {
    return IO_FILE.loadAllChars(source, offset);
  }

  /**
   * @see IoSupport#loadAllChars(Object, int)
   */
  public static char[] loadAllChars(@NotNull URI source, @Min(0L) int offset) {
    return IO_URI.loadAllChars(source, offset);
  }

  /**
   * @see IoSupport#loadAllChars(Object, int)
   */
  public static char[] loadAllChars(@NotNull URL source, @Min(0L) int offset) {
    return IO_URL.loadAllChars(source, offset);
  }

  /**
   * @see IoSupport#loadAllChars(Object, Encoding)
   */
  public static char[] loadAllChars(@NotNull Path source, @Null Encoding encoding) {
    return IO_PATH.loadAllChars(source, encoding);
  }

  /**
   * @see IoSupport#loadAllChars(Object, Encoding)
   */
  public static char[] loadAllChars(@NotNull File source, @Null Encoding encoding) {
    return IO_FILE.loadAllChars(source, encoding);
  }

  /**
   * @see IoSupport#loadAllChars(Object, Encoding)
   */
  public static char[] loadAllChars(@NotNull URI source, @Null Encoding encoding) {
    return IO_URI.loadAllChars(source, encoding);
  }

  /**
   * @see IoSupport#loadAllChars(Object, Encoding)
   */
  public static char[] loadAllChars(@NotNull URL source, @Null Encoding encoding) {
    return IO_URL.loadAllChars(source, encoding);
  }

  /**
   * @see IoSupport#loadAllChars(Object, Encoding, int)
   */
  public static char[] loadAllChars(@NotNull Path source, @Null Encoding encoding, @Min(0L) int offset) {
    return IO_PATH.loadAllChars(source, encoding, offset);
  }

  /**
   * @see IoSupport#loadAllChars(Object, Encoding, int)
   */
  public static char[] loadAllChars(@NotNull File source, @Null Encoding encoding, @Min(0L) int offset) {
    return IO_FILE.loadAllChars(source, encoding, offset);
  }

  /**
   * @see IoSupport#loadAllChars(Object, Encoding, int)
   */
  public static char[] loadAllChars(@NotNull URI source, @Null Encoding encoding, @Min(0L) int offset) {
    return IO_URI.loadAllChars(source, encoding, offset);
  }

  /**
   * @see IoSupport#loadAllChars(Object, Encoding, int)
   */
  public static char[] loadAllChars(@NotNull URL source, @Null Encoding encoding, @Min(0L) int offset) {
    return IO_URL.loadAllChars(source, encoding, offset);
  }

  /**
   * @see IoSupport#loadBytes(Object, int)
   */
  public static byte[] loadBytes(@NotNull Path source, @Min(1L) int size) {
    return IO_PATH.loadBytes(source, size);
  }

  /**
   * @see IoSupport#loadBytes(Object, int)
   */
  public static byte[] loadBytes(@NotNull File source, @Min(1L) int size) {
    return IO_FILE.loadBytes(source, size);
  }

  /**
   * @see IoSupport#loadBytes(Object, int)
   */
  public static byte[] loadBytes(@NotNull URI source, @Min(1L) int size) {
    return IO_URI.loadBytes(source, size);
  }

  /**
   * @see IoSupport#loadBytes(Object, int)
   */
  public static byte[] loadBytes(@NotNull URL source, @Min(1L) int size) {
    return IO_URL.loadBytes(source, size);
  }

  /**
   * @see IoSupport#loadBytes(Object, int, int)
   */
  public static byte[] loadBytes(@NotNull Path source, @Min(0L) int offset, @Min(1L) int size) {
    return IO_PATH.loadBytes(source, offset, size);
  }

  /**
   * @see IoSupport#loadBytes(Object, int, int)
   */
  public static byte[] loadBytes(@NotNull File source, @Min(0L) int offset, @Min(1L) int size) {
    return IO_FILE.loadBytes(source, offset, size);
  }

  /**
   * @see IoSupport#loadBytes(Object, int, int)
   */
  public static byte[] loadBytes(@NotNull URI source, @Min(0L) int offset, @Min(1L) int size) {
    return IO_URI.loadBytes(source, offset, size);
  }

  /**
   * @see IoSupport#loadBytes(Object, int, int)
   */
  public static byte[] loadBytes(@NotNull URL source, @Min(0L) int offset, @Min(1L) int size) {
    return IO_URL.loadBytes(source, offset, size);
  }

  /**
   * @see IoSupport#loadChars(Object, int)
   */
  public static char[] loadChars(@NotNull Path source, @Min(1L) int size) {
    return IO_PATH.loadChars(source, size);
  }

  /**
   * @see IoSupport#loadChars(Object, int)
   */
  public static char[] loadChars(@NotNull File source, @Min(1L) int size) {
    return IO_FILE.loadChars(source, size);
  }

  /**
   * @see IoSupport#loadChars(Object, int)
   */
  public static char[] loadChars(@NotNull URI source, @Min(1L) int size) {
    return IO_URI.loadChars(source, size);
  }

  /**
   * @see IoSupport#loadChars(Object, int)
   */
  public static char[] loadChars(@NotNull URL source, @Min(1L) int size) {
    return IO_URL.loadChars(source, size);
  }

  /**
   * @see IoSupport#loadChars(Object, int, int)
   */
  public static char[] loadChars(@NotNull Path source, @Min(0L) int offset, @Min(1L) int size) {
    return IO_PATH.loadChars(source, offset, size);
  }

  /**
   * @see IoSupport#loadChars(Object, int, int)
   */
  public static char[] loadChars(@NotNull File source, @Min(0L) int offset, @Min(1L) int size) {
    return IO_FILE.loadChars(source, offset, size);
  }

  /**
   * @see IoSupport#loadChars(Object, int, int)
   */
  public static char[] loadChars(@NotNull URI source, @Min(0L) int offset, @Min(1L) int size) {
    return IO_URI.loadChars(source, offset, size);
  }

  /**
   * @see IoSupport#loadChars(Object, int, int)
   */
  public static char[] loadChars(@NotNull URL source, @Min(0L) int offset, @Min(1L) int size) {
    return IO_URL.loadChars(source, offset, size);
  }

  /**
   * @see IoSupport#loadChars(Object, Encoding, int)
   */
  public static char[] loadChars(@NotNull Path source, @Null Encoding encoding, @Min(1L) int size) {
    return IO_PATH.loadChars(source, encoding, size);
  }

  /**
   * @see IoSupport#loadChars(Object, Encoding, int)
   */
  public static char[] loadChars(@NotNull File source, @Null Encoding encoding, @Min(1L) int size) {
    return IO_FILE.loadChars(source, encoding, size);
  }

  /**
   * @see IoSupport#loadChars(Object, Encoding, int)
   */
  public static char[] loadChars(@NotNull URI source, @Null Encoding encoding, @Min(1L) int size) {
    return IO_URI.loadChars(source, encoding, size);
  }

  /**
   * @see IoSupport#loadChars(Object, Encoding, int)
   */
  public static char[] loadChars(@NotNull URL source, @Null Encoding encoding, @Min(1L) int size) {
    return IO_URL.loadChars(source, encoding, size);
  }

  /**
   * @see IoSupport#loadChars(Object, Encoding, int, int)
   */
  public static char[] loadChars(@NotNull Path source, @Null Encoding encoding, @Min(0L) int offset, @Min(1L) int size) {
    return IO_PATH.loadChars(source, encoding, offset, size);
  }

  /**
   * @see IoSupport#loadChars(Object, Encoding, int, int)
   */
  public static char[] loadChars(@NotNull File source, @Null Encoding encoding, @Min(0L) int offset, @Min(1L) int size) {
    return IO_FILE.loadChars(source, encoding, offset, size);
  }

  /**
   * @see IoSupport#loadChars(Object, Encoding, int, int)
   */
  public static char[] loadChars(@NotNull URI source, @Null Encoding encoding, @Min(0L) int offset, @Min(1L) int size) {
    return IO_URI.loadChars(source, encoding, offset, size);
  }

  /**
   * @see IoSupport#loadChars(Object, Encoding, int, int)
   */
  public static char[] loadChars(@NotNull URL source, @Null Encoding encoding, @Min(0L) int offset, @Min(1L) int size) {
    return IO_URL.loadChars(source, encoding, offset, size);
  }

  /**
   * @see IoSupport#newInputStream(Object)
   */
  public static @NotNull InputStream newInputStream(@NotNull Path source) {
    return IO_PATH.newInputStream(source);
  }

  /**
   * @see IoSupport#newInputStream(Object)
   */
  public static @NotNull InputStream newInputStream(@NotNull File source) {
    return IO_FILE.newInputStream(source);
  }

  /**
   * @see IoSupport#newInputStream(Object)
   */
  public static @NotNull InputStream newInputStream(@NotNull URI source) {
    return IO_URI.newInputStream(source);
  }

  /**
   * @see IoSupport#newInputStream(Object)
   */
  public static @NotNull InputStream newInputStream(@NotNull URL source) {
    return IO_URL.newInputStream(source);
  }

  /**
   * @see IoSupport#newOutputStream(Object)
   */
  public static @NotNull OutputStream newOutputStream(@NotNull Path destination) {
    return IO_PATH.newOutputStream(destination);
  }

  /**
   * @see IoSupport#newOutputStream(Object)
   */
  public static @NotNull OutputStream newOutputStream(@NotNull File destination) {
    return IO_FILE.newOutputStream(destination);
  }

  /**
   * @see IoSupport#newOutputStream(Object)
   */
  public static @NotNull OutputStream newOutputStream(@NotNull URI destination) {
    return IO_URI.newOutputStream(destination);
  }

  /**
   * @see IoSupport#newReader(Object)
   */
  public static @NotNull Reader newReader(@NotNull Path source) {
    return IO_PATH.newReader(source);
  }

  /**
   * @see IoSupport#newReader(Object)
   */
  public static @NotNull Reader newReader(@NotNull File source) {
    return IO_FILE.newReader(source);
  }

  /**
   * @see IoSupport#newReader(Object)
   */
  public static @NotNull Reader newReader(@NotNull URI source) {
    return IO_URI.newReader(source);
  }

  /**
   * @see IoSupport#newReader(Object)
   */
  public static @NotNull Reader newReader(@NotNull URL source) {
    return IO_URL.newReader(source);
  }

  /**
   * @see IoSupport#newReader(Object, Encoding)
   */
  public static @NotNull Reader newReader(@NotNull Path source, @Null Encoding encoding) {
    return IO_PATH.newReader(source, encoding);
  }

  /**
   * @see IoSupport#newReader(Object, Encoding)
   */
  public static @NotNull Reader newReader(@NotNull File source, @Null Encoding encoding) {
    return IO_FILE.newReader(source, encoding);
  }

  /**
   * @see IoSupport#newReader(Object, Encoding)
   */
  public static @NotNull Reader newReader(@NotNull URI source, @Null Encoding encoding) {
    return IO_URI.newReader(source, encoding);
  }

  /**
   * @see IoSupport#newReader(Object, Encoding)
   */
  public static @NotNull Reader newReader(@NotNull URL source, @Null Encoding encoding) {
    return IO_URL.newReader(source, encoding);
  }

  /**
   * @see IoSupport#newWriter(Object)
   */
  public static @NotNull Writer newWriter(@NotNull Path destination) {
    return IO_PATH.newWriter(destination);
  }

  /**
   * @see IoSupport#newWriter(Object)
   */
  public static @NotNull Writer newWriter(@NotNull File destination) {
    return IO_FILE.newWriter(destination);
  }

  /**
   * @see IoSupport#newWriter(Object)
   */
  public static @NotNull Writer newWriter(@NotNull URI destination) {
    return IO_URI.newWriter(destination);
  }

  /**
   * @see IoSupport#newWriter(Object, Encoding)
   */
  public static @NotNull Writer newWriter(@NotNull Path destination, @Null Encoding encoding) {
    return IO_PATH.newWriter(destination, encoding);
  }

  /**
   * @see IoSupport#newWriter(Object, Encoding)
   */
  public static @NotNull Writer newWriter(@NotNull File destination, @Null Encoding encoding) {
    return IO_FILE.newWriter(destination, encoding);
  }

  /**
   * @see IoSupport#newWriter(Object, Encoding)
   */
  public static @NotNull Writer newWriter(@NotNull URI destination, @Null Encoding encoding) {
    return IO_URI.newWriter(destination, encoding);
  }

  /**
   * @see IoSupport#readText(Object)
   */
  public static @NotNull String readText(@NotNull Path source) {
    return IO_PATH.readText(source);
  }

  /**
   * @see IoSupport#readText(Object)
   */
  public static @NotNull String readText(@NotNull File source) {
    return IO_FILE.readText(source);
  }

  /**
   * @see IoSupport#readText(Object)
   */
  public static @NotNull String readText(@NotNull URI source) {
    return IO_URI.readText(source);
  }

  /**
   * @see IoSupport#readText(Object)
   */
  public static @NotNull String readText(@NotNull URL source) {
    return IO_URL.readText(source);
  }

  /**
   * @see IoSupport#readText(Object, Encoding)
   */
  public static @NotNull String readText(@NotNull Path source, @Null Encoding encoding) {
    return IO_PATH.readText(source, encoding);
  }

  /**
   * @see IoSupport#readText(Object, Encoding)
   */
  public static @NotNull String readText(@NotNull File source, @Null Encoding encoding) {
    return IO_FILE.readText(source, encoding);
  }

  /**
   * @see IoSupport#readText(Object, Encoding)
   */
  public static @NotNull String readText(@NotNull URI source, @Null Encoding encoding) {
    return IO_URI.readText(source, encoding);
  }

  /**
   * @see IoSupport#readText(Object, Encoding)
   */
  public static @NotNull String readText(@NotNull URL source, @Null Encoding encoding) {
    return IO_URL.readText(source, encoding);
  }

  /**
   * @see IoSupport#saveBytes(Object, byte[])
   */
  public static void saveBytes(@NotNull Path destination, @NotNull byte[] data) {
    IO_PATH.saveBytes(destination, data);
  }

  /**
   * @see IoSupport#saveBytes(Object, byte[])
   */
  public static void saveBytes(@NotNull File destination, @NotNull byte[] data) {
    IO_FILE.saveBytes(destination, data);
  }

  /**
   * @see IoSupport#saveBytes(Object, byte[])
   */
  public static void saveBytes(@NotNull URI destination, @NotNull byte[] data) {
    IO_URI.saveBytes(destination, data);
  }

  /**
   * @see IoSupport#saveBytes(Object, byte[], int, int)
   */
  public static void saveBytes(@NotNull Path destination, @NotNull byte[] data, @Min(0L) int offset, @Min(0L) int length) {
    IO_PATH.saveBytes(destination, data, offset, length);
  }

  /**
   * @see IoSupport#saveBytes(Object, byte[], int, int)
   */
  public static void saveBytes(@NotNull File destination, @NotNull byte[] data, @Min(0L) int offset, @Min(0L) int length) {
    IO_FILE.saveBytes(destination, data, offset, length);
  }

  /**
   * @see IoSupport#saveBytes(Object, byte[], int, int)
   */
  public static void saveBytes(@NotNull URI destination, @NotNull byte[] data, @Min(0L) int offset, @Min(0L) int length) {
    IO_URI.saveBytes(destination, data, offset, length);
  }

  /**
   * @see IoSupport#saveChars(Object, char[])
   */
  public static void saveChars(@NotNull Path destination, @NotNull char[] data) {
    IO_PATH.saveChars(destination, data);
  }

  /**
   * @see IoSupport#saveChars(Object, char[])
   */
  public static void saveChars(@NotNull File destination, @NotNull char[] data) {
    IO_FILE.saveChars(destination, data);
  }

  /**
   * @see IoSupport#saveChars(Object, char[])
   */
  public static void saveChars(@NotNull URI destination, @NotNull char[] data) {
    IO_URI.saveChars(destination, data);
  }

  /**
   * @see IoSupport#saveChars(Object, Encoding, char[])
   */
  public static void saveChars(@NotNull Path destination, @Null Encoding encoding, @NotNull char[] data) {
    IO_PATH.saveChars(destination, encoding, data);
  }

  /**
   * @see IoSupport#saveChars(Object, Encoding, char[])
   */
  public static void saveChars(@NotNull File destination, @Null Encoding encoding, @NotNull char[] data) {
    IO_FILE.saveChars(destination, encoding, data);
  }

  /**
   * @see IoSupport#saveChars(Object, Encoding, char[])
   */
  public static void saveChars(@NotNull URI destination, @Null Encoding encoding, @NotNull char[] data) {
    IO_URI.saveChars(destination, encoding, data);
  }

  /**
   * @see IoSupport#saveChars(Object, Encoding, char[], int, int)
   */
  public static void saveChars(@NotNull Path destination, @Null Encoding encoding, char[] data, @Min(0L) int offset, @Min(0L) int size) {
    IO_PATH.saveChars(destination, encoding, data, offset, size);
  }

  /**
   * @see IoSupport#saveChars(Object, Encoding, char[], int, int)
   */
  public static void saveChars(@NotNull File destination, @Null Encoding encoding, char[] data, @Min(0L) int offset, @Min(0L) int size) {
    IO_FILE.saveChars(destination, encoding, data, offset, size);
  }

  /**
   * @see IoSupport#saveChars(Object, Encoding, char[], int, int)
   */
  public static void saveChars(@NotNull URI destination, @Null Encoding encoding, char[] data, @Min(0L) int offset, @Min(0L) int size) {
    IO_URI.saveChars(destination, encoding, data, offset, size);
  }

  /**
   * @see IoSupport#writeText(Object, String)
   */
  public static void writeText(@NotNull Path destination, @NotNull String text) {
    IO_PATH.writeText(destination, text);
  }

  /**
   * @see IoSupport#writeText(Object, String)
   */
  public static void writeText(@NotNull File destination, @NotNull String text) {
    IO_FILE.writeText(destination, text);
  }

  /**
   * @see IoSupport#writeText(Object, String)
   */
  public static void writeText(@NotNull URI destination, @NotNull String text) {
    IO_URI.writeText(destination, text);
  }

  /**
   * @see IoSupport#writeText(Object, Encoding, String)
   */
  public static void writeText(@NotNull Path destination, @Null Encoding encoding, @NotNull String text) {
    IO_PATH.writeText(destination, encoding, text);
  }

  /**
   * @see IoSupport#writeText(Object, Encoding, String)
   */
  public static void writeText(@NotNull File destination, @Null Encoding encoding, @NotNull String text) {
    IO_FILE.writeText(destination, encoding, text);
  }

  /**
   * @see IoSupport#writeText(Object, Encoding, String)
   */
  public static void writeText(@NotNull URI destination, @Null Encoding encoding, @NotNull String text) {
    IO_URI.writeText(destination, encoding, text);
  }

  /* END */

} /* ENDCLASS */
