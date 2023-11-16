package com.kasisoft.libs.common.io;

import static com.kasisoft.libs.common.internal.Messages.*;

import com.kasisoft.libs.common.functional.*;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.pools.*;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import java.util.stream.*;

import java.util.regex.Pattern;

import java.util.zip.*;

import java.util.*;

import java.nio.file.*;

import java.net.*;

import java.io.*;

/**
 * Collection of functions used for IO operations.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class IoFunctions {

    private static final String   WC1     = "([^/]+)";          // *

    private static final String   WC2     = "(.+)";             // **

    @NotNull
    public static Reader newReader(@NotNull InputStream source) {
        return newReader(source, null);
    }

    @NotNull
    public static Reader newReader(@NotNull InputStream source, Encoding encoding) {
        return new BufferedReader(new InputStreamReader(source, Encoding.getEncoding(encoding).getCharset()));
    }

    public static void forReaderDo(@NotNull InputStream source, @NotNull KConsumer<@NotNull Reader> function) {
        forReaderDo(source, null, function);
    }

    public static void forReaderDo(@NotNull InputStream source, Encoding encoding, @NotNull KConsumer<@NotNull Reader> function) {
        try (var reader = newReader(source, encoding)) {
            function.accept(reader);
        } catch (Exception ex) {
            throw KclException.wrap(ex);
        }
    }

    @NotNull
    public static <R> R forReader(@NotNull InputStream source, @NotNull KFunction<@NotNull Reader, R> function) {
        return forReader(source, null, function);
    }

    @NotNull
    public static <R> R forReader(@NotNull InputStream source, Encoding encoding, @NotNull KFunction<@NotNull Reader, R> function) {
        try (var reader = newReader(source, encoding)) {
            return function.apply(reader);
        } catch (Exception ex) {
            throw KclException.wrap(ex);
        }
    }

    @NotNull
    public static String readText(@NotNull InputStream instream) {
        return forReader(instream, null, IoFunctions::readText);
    }

    @NotNull
    public static String readText(@NotNull InputStream instream, Encoding encoding) {
        return forReader(instream, encoding, IoFunctions::readText);
    }

    @NotNull
    public static String readText(@NotNull Reader reader) {
        return Buckets.bucketStringWriter().forInstance($sw -> {
            IoFunctions.copy(reader, $sw);
            return $sw.toString();
        });
    }

    @NotNull
    public static Writer newWriter(@NotNull OutputStream destination) {
        return newWriter(destination, null);
    }

    @NotNull
    public static Writer newWriter(@NotNull OutputStream destination, Encoding encoding) {
        return new BufferedWriter(new OutputStreamWriter(destination, Encoding.getEncoding(encoding).getCharset()));
    }

    public static void forWriterDo(@NotNull OutputStream destination, @NotNull KConsumer<@NotNull Writer> action) {
        forWriterDo(destination, null, action);
    }

    public static void forWriterDo(@NotNull OutputStream destination, Encoding encoding, @NotNull KConsumer<@NotNull Writer> action) {
        try (var writer = newWriter(destination, encoding)) {
            action.accept(writer);
        } catch (Exception ex) {
            throw KclException.wrap(ex);
        }
    }

    public static void writeText(@NotNull OutputStream destination, @NotNull String text) {
        writeText(destination, null, text);
    }

    public static void writeText(@NotNull OutputStream destination, Encoding encoding, @NotNull String text) {
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
        Buffers.byteArray().forInstanceDo(blockSize, $ -> {
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
        Buffers.charArray().forInstanceDo(blockSize, $ -> {
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
     * @param pattern
     *            The filesystem pattern which is supposed to be compiled.
     * @return A {@link Pattern} instance used to test filesystem pathes.
     */
    @NotNull
    public static Pattern compileFilesystemPattern(@NotNull String pattern) {
        return compileFilesystemPattern(pattern, 0);
    }

    /**
     * Creates a regex pattern used to match a filesystem path.
     *
     * @param pattern
     *            The filesystem pattern which is supposed to be compiled.
     * @param flags
     *            The Pattern flags.
     * @return A {@link Pattern} instance used to test filesystem pathes.
     */
    @NotNull
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
                buffer.append(Pattern.quote(token));
            }
        }
        if (last) {
            buffer.append(WC1);
        }
        return Pattern.compile(buffer.toString(), flags);
    }

    @NotNull
    public static Path newTempFile(String prefix, String suffix) {
        try {
            return Files.createTempFile(prefix, suffix);
        } catch (Exception ex) {
            throw new KclException(ex, error_failed_to_create_temporary_file.formatted(prefix, suffix));
        }
    }

    @NotNull
    public static Optional<Path> findNewTempFile(String prefix, String suffix) {
        try {
            return Optional.of(Files.createTempFile(prefix, suffix));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    /**
     * Skip some bytes within an InputStream.
     *
     * @param input
     *            The InputStream providing the content.
     * @param size
     *            The amount of bytes to be skipped.
     */
    @NotNull
    public static void skip(InputStream input, @Min(0) long size) {
        if (size > 0) {
            try {
                input.skip(size);
            } catch (Exception ex) {
                throw KclException.wrap(ex);
            }
        }
    }

    /**
     * Skips some byte within a Reader.
     *
     * @param input
     *            The Reader providing the content.
     * @param size
     *            The amount of bytes to be skipped.
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
     * @param file
     *            The file which has to be gzipped.
     * @return The gzipped file.
     */
    @NotNull
    public static Path gzip(@NotNull Path source) {
        return gzip(source, null);
    }

    /**
     * GZIPs the supplied file. Preexisting files will be overwritten.
     *
     * @param source
     *            The file which has to be gzipped.
     * @param destination
     *            The gziped file. If null it's the input file plus a suffix '.gz'
     * @return The gzipped file.
     */
    @NotNull
    public static Path gzip(@NotNull Path source, Path destination) {
        source = source.normalize();
        var result = destination;
        if (result == null) {
            result = buildDefaultGzippedPath(source);
        }
        result = result.normalize();
        if (source.compareTo(result) == 0) {
            // we need to do an inline compression
            IoSupportFunctions.saveBytes(result, loadGzipped(source));
        } else {
            try (var instream = IoSupportFunctions.newInputStream(source);
                var outstream = new GZIPOutputStream(IoSupportFunctions.newOutputStream(result));) {
                copy(instream, outstream);
            } catch (Exception ex) {
                throw KclException.wrap(ex, error_failed_to_gzip.formatted(source, result));
            }

        }
        return result;
    }

    @NotNull
    private static Path buildDefaultGzippedPath(@NotNull Path source) {
        var parentDir = source.toAbsolutePath().getParent();
        var fileName  = source.getFileName().toString() + ".gz";
        return parentDir.resolve(fileName).normalize();
    }

    @NotNull
    public static byte[] loadGzipped(@NotNull Path source) {
        return Buckets.bucketByteArrayOutputStream().forInstance($byteout -> {
            try (var instream = IoSupportFunctions.newInputStream(source); var gzipOut = new GZIPOutputStream($byteout);) {
                copy(instream, gzipOut);
            } catch (Exception ex) {
                throw KclException.wrap(ex, error_failed_to_load_gzipped.formatted(source));
            }
            return $byteout.toByteArray();
        });
    }

    /**
     * UNGZIPs the supplied file.
     *
     * @param file
     *            The file which has to be ungzipped.
     * @return The ungzipped file.
     */
    @NotNull
    public static Path ungzip(@NotNull Path source) {
        return ungzip(source, null);
    }

    /**
     * UNGZIPs the supplied file. Preexisting files will be overwritten.
     *
     * @param source
     *            The file which has to be ungzipped.
     * @param destination
     *            The ungziped file. If null it's the input file plus a suffix '.gz'
     * @return The ungzipped file.
     */
    @NotNull
    public static Path ungzip(@NotNull Path source, Path destination) {

        source = source.normalize();
        var result = destination;

        if (result == null) {
            result = buildDefaultUngzippedPath(source);
        }

        result = result.normalize();

        if (source.compareTo(result) == 0) {
            // we need to do an inline compression
            IoSupportFunctions.saveBytes(result, loadUngzipped(source));
        } else {
            try (var instream = new GZIPInputStream(IoSupportFunctions.newInputStream(source));
                var outstream = IoSupportFunctions.newOutputStream(result);) {
                copy(instream, outstream);
            } catch (Exception ex) {
                throw KclException.wrap(ex, error_failed_to_gzip.formatted(source, result));
            }

        }
        return result;
    }

    @NotNull
    private static Path buildDefaultUngzippedPath(@NotNull Path source) {
        var parentDir = source.toAbsolutePath().getParent();
        var fileName  = source.getFileName().toString();
        if (fileName.endsWith(".gz")) {
            fileName = fileName.substring(0, fileName.length() - ".gz".length());
        } else {
            fileName = fileName + ".ungz";
        }
        return parentDir.resolve(fileName).normalize();
    }

    @NotNull
    public static byte[] loadUngzipped(@NotNull Path source) {
        return Buckets.bucketByteArrayOutputStream().forInstance($byteout -> {
            try (var instream = new GZIPInputStream(IoSupportFunctions.newInputStream(source));) {
                copy(instream, $byteout);
            } catch (Exception ex) {
                throw KclException.wrap(ex, error_failed_to_ungzip.formatted(source));
            }
            return $byteout.toByteArray();
        });
    }

    /**
     * Returns the nearest existing path.
     *
     * @param path
     *            The current path.
     * @return An existing parent path or null if there's none.
     */
    @NotNull
    public static Optional<Path> findExistingPath(Path path) {
        var result = path != null ? path.normalize() : null;
        while ((result != null) && (!Files.exists(result))) {
            result = result.getParent();
        }
        return Optional.ofNullable(result);
    }

    public static void copyFile(@NotNull Path source, @NotNull Path destination) {
        try {
            if (!Files.isRegularFile(source)) {
                throw new KclException(error_file_does_not_exist.formatted(source));
            }
            Files.copy(source, destination, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            throw KclException.wrap(ex, error_failed_to_copy.formatted(source, destination));
        }
    }

    public static void moveFile(@NotNull Path source, @NotNull Path destination) {
        try {
            if (!Files.isRegularFile(source)) {
                throw new KclException(error_file_does_not_exist.formatted(source));
            }
            Files.move(source, destination, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            throw KclException.wrap(ex, error_failed_to_copy.formatted(source, destination));
        }
    }

    /**
     * Creates the supplied directory including it's parent.
     *
     * @param dir
     *            The directory that needs to be created.
     */
    public static void mkDirs(@NotNull Path dir) {
        try {
            Files.createDirectories(dir);
        } catch (Exception ex) {
            throw KclException.wrap(ex, error_failed_to_create_directory.formatted(dir));
        }
    }

    public static void copyDir(@NotNull Path source, @NotNull Path destination) {

        if (!Files.isDirectory(source)) {
            throw new KclException(error_directory_does_not_exist.formatted(source));
        }

        new CopyingFileWalker(source, destination).run();

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
                throw new KclException(error_directory_does_not_exist.formatted(source));
            }

            var sourceRoot = findRoot(source);
            var destRoot   = findRoot(destination);
            if (sourceRoot.compareTo(destRoot) == 0) {

                // same root, so we can simply move
                Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);

            } else {
                new MovingFileWalker(source, destination).run();
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
                throw KclException.wrap(ex, error_failed_to_delete_file.formatted(file));
            }
        }
    }

    public static void deleteDir(@NotNull Path dir) {
        if (Files.isDirectory(dir)) {
            new DeletingFileWalker(dir).run();
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
     * @param start
     *            The base path.
     * @return A list of relative pathes (directories will end with a slash).
     */
    @NotNull
    public static List<@NotNull String> listPathes(@NotNull Path start) {
        return listPathes(start, null, true);
    }

    /**
     * Collects relative pathes.
     *
     * @param start
     *            The base path.
     * @param filter
     *            A filter used to accept the relative path.
     * @return A list of relative pathes (directories will end with a slash).
     */
    @NotNull
    public static List<@NotNull String> listPathes(@NotNull Path start, KPredicate<String> filter) {
        return listPathes(start, filter, true);
    }

    /**
     * Collects relative pathes.
     *
     * @param start
     *            The base path.
     * @param filter
     *            A filter used to accept the relative path.
     * @param includeDirs
     *            <code>true</code> <=> Include directories in the result list.
     * @return A list of relative pathes (directories will end with a slash).
     */
    @NotNull
    public static List<@NotNull String> listPathes(@NotNull Path start, KPredicate<String> filter, boolean includeDirs) {

        try {

            KPredicate<String> predicate = filter != null ? filter : $ -> true;
            return new ListingFileWalker(start, includeDirs).get().stream().filter(predicate.protect()).sorted().collect(Collectors.toList());

        } catch (Exception ex) {
            throw KclException.wrap(ex, error_failed_to_scan_dir.formatted(start));
        }

    }

    /**
     * Executes some function per entry (unless rejected).
     *
     * @param zipFile
     *            The zip file.
     * @param encoding
     *            The encoding to use.
     * @param filter
     *            A filter used to accept the relative path.
     * @param consumer
     *            A function that will be executed.
     */
    public static void forZipFileDo(@NotNull Path zipFile, Encoding encoding, KPredicate<@NotNull ZipEntry> filter, @NotNull KBiConsumer<@NotNull ZipFile, @NotNull ZipEntry> consumer) {
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
            throw KclException.wrap(ex, error_failed_to_process_zip.formatted(zipFile));
        }
    }

    /**
     * Collects relative pathes within the supplied archive.
     *
     * @param zipFile
     *            The zip file.
     * @return A list of relative pathes.
     */
    @NotNull
    public static List<@NotNull String> listZipFile(@NotNull Path zipFile) {
        return listZipFile(zipFile, null, null);
    }

    /**
     * Collects relative pathes within the supplied archive.
     *
     * @param zipFile
     *            The zip file.
     * @param encoding
     *            The encoding to use.
     * @return A list of relative pathes.
     */
    @NotNull
    public static List<@NotNull String> listZipFile(@NotNull Path zipFile, Encoding encoding) {
        return listZipFile(zipFile, encoding, null);
    }

    /**
     * Collects relative pathes within the supplied archive.
     *
     * @param zipFile
     *            The zip file.
     * @param encoding
     *            The encoding to use.
     * @param filter
     *            A filter used to accept the relative path.
     * @return A list of relative pathes.
     */
    @NotNull
    public static List<@NotNull String> listZipFile(@NotNull Path zipFile, Encoding encoding, KPredicate<@NotNull ZipEntry> filter) {
        var result = new ArrayList<String>();
        forZipFileDo(zipFile, encoding, filter, ($z, $e) -> result.add($e.getName().replace('\\', '/')));
        return result;
    }

    public static void unzip(@NotNull Path zipFile, @NotNull Path destination) {
        unzip(zipFile, destination, null, null);
    }

    public static void unzip(@NotNull Path zipFile, @NotNull Path destination, Encoding encoding) {
        unzip(zipFile, destination, encoding, null);
    }

    public static void unzip(@NotNull Path zipFile, @NotNull Path destination, Encoding encoding, KPredicate<@NotNull ZipEntry> filter) {
        forZipFileDo(zipFile, encoding, filter, ($z, $e) -> {

            var dest = destination.resolve($e.getName());

            if ($e.isDirectory()) {

                mkDirs(dest);

            } else {

                mkDirs(dest.getParent());

                try (var instream = $z.getInputStream($e); var outstream = IoSupportFunctions.newOutputStream(dest);) {
                    copy(instream, outstream);
                } catch (Exception ex) {
                    throw KclException.wrap(ex, error_failed_to_unzip.formatted(dest, zipFile, $e.getName()));
                }

            }

        });
    }

    public static void zip(@NotNull Path zipFile, @NotNull Path source, Encoding encoding) {
        var charset = encoding != null ? encoding.getCharset() : Encoding.IBM437.getCharset();
        var pathes  = listPathes(source);
        IoSupportFunctions.forOutputStreamDo(zipFile, $output -> {
            try (var zipout = new ZipOutputStream($output, charset)) {
                for (var path : pathes) {
                    var zentry = new ZipEntry(path);
                    zipout.putNextEntry(zentry);
                    if (!path.endsWith("/")) {
                        IoSupportFunctions.forInputStreamDo(source.resolve(path), $input -> copy($input, zipout));
                    }
                    zipout.closeEntry();
                }
            } catch (Exception ex) {
                throw KclException.wrap(ex, error_failed_to_zip.formatted(zipFile));
            }
        });
    }

    private static final String[] ARCHIVE_PREFIXES = new String[] {"jar:", "ear:", "zip:", "war:"};

    /**
     * Calculates the class directory/jarfile used for the supplied class instance.
     *
     * @param classobj
     *            The class which is used to locate the application directory.
     * @param skippable
     *            If supplied immediate parental directories named as provided will be skipped. This is
     *            an easy way to skip directories in a build environment (f.e. target/classes).
     * @return The location of the class directory/jarfile.
     */
    @NotNull
    public static Path locateDirectory(@NotNull Class<?> classobj, String ... skippable) {

        var classname    = "%s.class".formatted(classobj.getName().replace('.', '/'));
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

    @NotNull
    public static Properties loadProperties(@NotNull Reader reader) {
        Properties result = new Properties();
        try {
            result.load(reader);
        } catch (Exception ex) {
            throw KclException.wrap(ex, error_failed_to_load_properties);
        }
        return result;
    }

    @NotNull
    public static Optional<byte[]> genericLoadBytes(@NotNull Object source, int size) {
        IoSupport ioSupport = IoSupportFunctions.ioSupport(source.getClass());
        if (ioSupport != null) {
            return Optional.of(ioSupport.loadBytes(source, size));
        } else if (source instanceof CharSequence cs) {
            return genericLoadBytes(Paths.get(cs.toString()), size);
        } else if (source instanceof InputStream instream) {
            try {
                var result = new byte[size];
                instream.readNBytes(result, 0, size);
                return Optional.of(result);
            } catch (Exception ex) {
                throw KclException.wrap(ex);
            }
        }
        return Optional.empty();
    }

    @NotNull
    public static BufferedReader newBufferedReader(@NotNull Reader reader) {
        if (reader instanceof BufferedReader bufferedReader) {
            return bufferedReader;
        }
        return new BufferedReader(reader);
    }

    @NotNull
    public static BufferedWriter newBufferedReader(@NotNull Writer writer) {
        if (writer instanceof BufferedWriter bufferedWriter) {
            return bufferedWriter;
        }
        return new BufferedWriter(writer);
    }

} /* ENDCLASS */
