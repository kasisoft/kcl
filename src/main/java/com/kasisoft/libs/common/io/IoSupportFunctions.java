package com.kasisoft.libs.common.io;

import com.kasisoft.libs.common.functional.*;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.io.impl.*;

import jakarta.validation.constraints.*;

import java.nio.file.*;

import java.net.*;

import java.io.*;

/**
 * Collection of all {@link IoSupport} functionalities.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class IoSupportFunctions {

    private static IoSupport<Path> IO_PATH;
    private static IoSupport<File> IO_FILE;
    private static IoSupport<URI>  IO_URI;
    private static IoSupport<URL>  IO_URL;

    private static synchronized IoSupport<Path> ioPath() {
        if (IO_PATH == null) {
            IO_PATH = new PathIoSupport();
        }
        return IO_PATH;
    }

    private static synchronized IoSupport<File> ioFile() {
        if (IO_FILE == null) {
            IO_FILE = new FileIoSupport();
        }
        return IO_FILE;
    }

    private static synchronized IoSupport<URI> ioURI() {
        if (IO_URI == null) {
          IO_URI = new URIIoSupport();
        }
        return IO_URI;
    }

    private static synchronized IoSupport<URL> ioURL() {
        if (IO_URL == null) {
            IO_URL = new URLIoSupport();
        }
        return IO_URL;
    }

    @SuppressWarnings("rawtypes")
    public static IoSupport ioSupport(@NotNull Class<?> clazz) {
        if (File.class.isAssignableFrom(clazz)) {
            return ioFile();
        } else if (Path.class.isAssignableFrom(clazz)) {
            return ioPath();
        } else if (URL.class.isAssignableFrom(clazz)) {
            return ioURL();
        } else if (URI.class.isAssignableFrom(clazz)) {
            return ioURI();
        }
        return null;
    }

    /**
     * @see IoSupport#forInputStream(Object, Function<InputStream, R>)
     */
    public static <R> R forInputStream(@NotNull Path source, @NotNull KFunction<InputStream, R> function) {
        return ioPath().forInputStream(source, function);
    }

    /**
     * @see IoSupport#forInputStream(Object, Function<InputStream, R>)
     */
    public static <R> R forInputStream(@NotNull File source, @NotNull KFunction<InputStream, R> function) {
        return ioFile().forInputStream(source, function);
    }

    /**
     * @see IoSupport#forInputStream(Object, Function<InputStream, R>)
     */
    public static <R> R forInputStream(@NotNull URI source, @NotNull KFunction<InputStream, R> function) {
        return ioURI().forInputStream(source, function);
    }

    /**
     * @see IoSupport#forInputStream(Object, Function<InputStream, R>)
     */
    public static <R> R forInputStream(@NotNull URL source, @NotNull KFunction<InputStream, R> function) {
        return ioURL().forInputStream(source, function);
    }

    /**
     * @see IoSupport#forInputStreamDo(Object, Consumer<InputStream>)
     */
    public static void forInputStreamDo(@NotNull Path source, @NotNull KConsumer<InputStream> action) {
        ioPath().forInputStreamDo(source, action);
    }

    /**
     * @see IoSupport#forInputStreamDo(Object, Consumer<InputStream>)
     */
    public static void forInputStreamDo(@NotNull File source, @NotNull KConsumer<InputStream> action) {
        ioFile().forInputStreamDo(source, action);
    }

    /**
     * @see IoSupport#forInputStreamDo(Object, Consumer<InputStream>)
     */
    public static void forInputStreamDo(@NotNull URI source, @NotNull KConsumer<InputStream> action) {
        ioURI().forInputStreamDo(source, action);
    }

    /**
     * @see IoSupport#forInputStreamDo(Object, Consumer<InputStream>)
     */
    public static void forInputStreamDo(@NotNull URL source, @NotNull KConsumer<InputStream> action) {
        ioURL().forInputStreamDo(source, action);
    }

    /**
     * @see IoSupport#forOutputStream(Object, Function<OutputStream, R>)
     */
    public static <R> R forOutputStream(@NotNull Path destination, @NotNull KFunction<OutputStream, R> function) {
        return ioPath().forOutputStream(destination, function);
    }

    /**
     * @see IoSupport#forOutputStream(Object, Function<OutputStream, R>)
     */
    public static <R> R forOutputStream(@NotNull File destination, @NotNull KFunction<OutputStream, R> function) {
        return ioFile().forOutputStream(destination, function);
    }

    /**
     * @see IoSupport#forOutputStream(Object, Function<OutputStream, R>)
     */
    public static <R> R forOutputStream(@NotNull URI destination, @NotNull KFunction<OutputStream, R> function) {
        return ioURI().forOutputStream(destination, function);
    }

    /**
     * @see IoSupport#forOutputStreamDo(Object, Consumer<OutputStream>)
     */
    public static void forOutputStreamDo(@NotNull Path destination, @NotNull KConsumer<OutputStream> action) {
        ioPath().forOutputStreamDo(destination, action);
    }

    /**
     * @see IoSupport#forOutputStreamDo(Object, Consumer<OutputStream>)
     */
    public static void forOutputStreamDo(@NotNull File destination, @NotNull KConsumer<OutputStream> action) {
        ioFile().forOutputStreamDo(destination, action);
    }

    /**
     * @see IoSupport#forOutputStreamDo(Object, Consumer<OutputStream>)
     */
    public static void forOutputStreamDo(@NotNull URI destination, @NotNull KConsumer<OutputStream> action) {
        ioURI().forOutputStreamDo(destination, action);
    }

    /**
     * @see IoSupport#forReader(Object, Function<Reader, R>)
     */
    public static <R> R forReader(@NotNull Path source, @NotNull KFunction<Reader, R> function) {
        return ioPath().forReader(source, function);
    }

    /**
     * @see IoSupport#forReader(Object, Function<Reader, R>)
     */
    public static <R> R forReader(@NotNull File source, @NotNull KFunction<Reader, R> function) {
        return ioFile().forReader(source, function);
    }

    /**
     * @see IoSupport#forReader(Object, Function<Reader, R>)
     */
    public static <R> R forReader(@NotNull URI source, @NotNull KFunction<Reader, R> function) {
        return ioURI().forReader(source, function);
    }

    /**
     * @see IoSupport#forReader(Object, Function<Reader, R>)
     */
    public static <R> R forReader(@NotNull URL source, @NotNull KFunction<Reader, R> function) {
        return ioURL().forReader(source, function);
    }

    /**
     * @see IoSupport#forReader(Object, Encoding, Function<Reader, R>)
     */
    public static <R> R forReader(@NotNull Path source, Encoding encoding, @NotNull KFunction<Reader, R> function) {
        return ioPath().forReader(source, encoding, function);
    }

    /**
     * @see IoSupport#forReader(Object, Encoding, Function<Reader, R>)
     */
    public static <R> R forReader(@NotNull File source, Encoding encoding, @NotNull KFunction<Reader, R> function) {
        return ioFile().forReader(source, encoding, function);
    }

    /**
     * @see IoSupport#forReader(Object, Encoding, Function<Reader, R>)
     */
    public static <R> R forReader(@NotNull URI source, Encoding encoding, @NotNull KFunction<Reader, R> function) {
        return ioURI().forReader(source, encoding, function);
    }

    /**
     * @see IoSupport#forReader(Object, Encoding, Function<Reader, R>)
     */
    public static <R> R forReader(@NotNull URL source, Encoding encoding, @NotNull KFunction<Reader, R> function) {
        return ioURL().forReader(source, encoding, function);
    }

    /**
     * @see IoSupport#forReaderDo(Object, Consumer<Reader>)
     */
    public static void forReaderDo(@NotNull Path source, @NotNull KConsumer<Reader> action) {
        ioPath().forReaderDo(source, action);
    }

    /**
     * @see IoSupport#forReaderDo(Object, Consumer<Reader>)
     */
    public static void forReaderDo(@NotNull File source, @NotNull KConsumer<Reader> action) {
        ioFile().forReaderDo(source, action);
    }

    /**
     * @see IoSupport#forReaderDo(Object, Consumer<Reader>)
     */
    public static void forReaderDo(@NotNull URI source, @NotNull KConsumer<Reader> action) {
        ioURI().forReaderDo(source, action);
    }

    /**
     * @see IoSupport#forReaderDo(Object, Consumer<Reader>)
     */
    public static void forReaderDo(@NotNull URL source, @NotNull KConsumer<Reader> action) {
        ioURL().forReaderDo(source, action);
    }

    /**
     * @see IoSupport#forReaderDo(Object, Encoding, Consumer<Reader>)
     */
    public static void forReaderDo(@NotNull Path source, Encoding encoding, @NotNull KConsumer<Reader> action) {
        ioPath().forReaderDo(source, encoding, action);
    }

    /**
     * @see IoSupport#forReaderDo(Object, Encoding, Consumer<Reader>)
     */
    public static void forReaderDo(@NotNull File source, Encoding encoding, @NotNull KConsumer<Reader> action) {
        ioFile().forReaderDo(source, encoding, action);
    }

    /**
     * @see IoSupport#forReaderDo(Object, Encoding, Consumer<Reader>)
     */
    public static void forReaderDo(@NotNull URI source, Encoding encoding, @NotNull KConsumer<Reader> action) {
        ioURI().forReaderDo(source, encoding, action);
    }

    /**
     * @see IoSupport#forReaderDo(Object, Encoding, Consumer<Reader>)
     */
    public static void forReaderDo(@NotNull URL source, Encoding encoding, @NotNull KConsumer<Reader> action) {
        ioURL().forReaderDo(source, encoding, action);
    }

    /**
     * @see IoSupport#forWriter(Object, Function<Writer, R>)
     */
    public static <R> R forWriter(@NotNull Path destination, @NotNull KFunction<Writer, R> function) {
        return ioPath().forWriter(destination, function);
    }

    /**
     * @see IoSupport#forWriter(Object, Function<Writer, R>)
     */
    public static <R> R forWriter(@NotNull File destination, @NotNull KFunction<Writer, R> function) {
        return ioFile().forWriter(destination, function);
    }

    /**
     * @see IoSupport#forWriter(Object, Function<Writer, R>)
     */
    public static <R> R forWriter(@NotNull URI destination, @NotNull KFunction<Writer, R> function) {
        return ioURI().forWriter(destination, function);
    }

    /**
     * @see IoSupport#forWriter(Object, Encoding, Function<Writer, R>)
     */
    public static <R> R forWriter(@NotNull Path destination, Encoding encoding, @NotNull KFunction<Writer, R> function) {
        return ioPath().forWriter(destination, encoding, function);
    }

    /**
     * @see IoSupport#forWriter(Object, Encoding, Function<Writer, R>)
     */
    public static <R> R forWriter(@NotNull File destination, Encoding encoding, @NotNull KFunction<Writer, R> function) {
        return ioFile().forWriter(destination, encoding, function);
    }

    /**
     * @see IoSupport#forWriter(Object, Encoding, Function<Writer, R>)
     */
    public static <R> R forWriter(@NotNull URI destination, Encoding encoding, @NotNull KFunction<Writer, R> function) {
        return ioURI().forWriter(destination, encoding, function);
    }

    /**
     * @see IoSupport#forWriterDo(Object, Consumer<Writer>)
     */
    public static void forWriterDo(@NotNull Path destination, @NotNull KConsumer<Writer> action) {
        ioPath().forWriterDo(destination, action);
    }

    /**
     * @see IoSupport#forWriterDo(Object, Consumer<Writer>)
     */
    public static void forWriterDo(@NotNull File destination, @NotNull KConsumer<Writer> action) {
        ioFile().forWriterDo(destination, action);
    }

    /**
     * @see IoSupport#forWriterDo(Object, Consumer<Writer>)
     */
    public static void forWriterDo(@NotNull URI destination, @NotNull KConsumer<Writer> action) {
        ioURI().forWriterDo(destination, action);
    }

    /**
     * @see IoSupport#forWriterDo(Object, Encoding, Consumer<Writer>)
     */
    public static void forWriterDo(@NotNull Path destination, Encoding encoding, @NotNull KConsumer<Writer> action) {
        ioPath().forWriterDo(destination, encoding, action);
    }

    /**
     * @see IoSupport#forWriterDo(Object, Encoding, Consumer<Writer>)
     */
    public static void forWriterDo(@NotNull File destination, Encoding encoding, @NotNull KConsumer<Writer> action) {
        ioFile().forWriterDo(destination, encoding, action);
    }

    /**
     * @see IoSupport#forWriterDo(Object, Encoding, Consumer<Writer>)
     */
    public static void forWriterDo(@NotNull URI destination, Encoding encoding, @NotNull KConsumer<Writer> action) {
        ioURI().forWriterDo(destination, encoding, action);
    }

    /**
     * @see IoSupport#loadAllBytes(Object)
     */
    public static byte[] loadAllBytes(@NotNull Path source) {
        return ioPath().loadAllBytes(source);
    }

    /**
     * @see IoSupport#loadAllBytes(Object)
     */
    public static byte[] loadAllBytes(@NotNull File source) {
        return ioFile().loadAllBytes(source);
    }

    /**
     * @see IoSupport#loadAllBytes(Object)
     */
    public static byte[] loadAllBytes(@NotNull URI source) {
        return ioURI().loadAllBytes(source);
    }

    /**
     * @see IoSupport#loadAllBytes(Object)
     */
    public static byte[] loadAllBytes(@NotNull URL source) {
        return ioURL().loadAllBytes(source);
    }

    /**
     * @see IoSupport#loadAllBytes(Object, int)
     */
    public static byte[] loadAllBytes(@NotNull Path source, @Min(0L) int offset) {
        return ioPath().loadAllBytes(source, offset);
    }

    /**
     * @see IoSupport#loadAllBytes(Object, int)
     */
    public static byte[] loadAllBytes(@NotNull File source, @Min(0L) int offset) {
        return ioFile().loadAllBytes(source, offset);
    }

    /**
     * @see IoSupport#loadAllBytes(Object, int)
     */
    public static byte[] loadAllBytes(@NotNull URI source, @Min(0L) int offset) {
        return ioURI().loadAllBytes(source, offset);
    }

    /**
     * @see IoSupport#loadAllBytes(Object, int)
     */
    public static byte[] loadAllBytes(@NotNull URL source, @Min(0L) int offset) {
        return ioURL().loadAllBytes(source, offset);
    }

    /**
     * @see IoSupport#loadAllChars(Object)
     */
    public static char[] loadAllChars(@NotNull Path source) {
        return ioPath().loadAllChars(source);
    }

    /**
     * @see IoSupport#loadAllChars(Object)
     */
    public static char[] loadAllChars(@NotNull File source) {
        return ioFile().loadAllChars(source);
    }

    /**
     * @see IoSupport#loadAllChars(Object)
     */
    public static char[] loadAllChars(@NotNull URI source) {
        return ioURI().loadAllChars(source);
    }

    /**
     * @see IoSupport#loadAllChars(Object)
     */
    public static char[] loadAllChars(@NotNull URL source) {
        return ioURL().loadAllChars(source);
    }

    /**
     * @see IoSupport#loadAllChars(Object, int)
     */
    public static char[] loadAllChars(@NotNull Path source, @Min(0L) int offset) {
        return ioPath().loadAllChars(source, offset);
    }

    /**
     * @see IoSupport#loadAllChars(Object, int)
     */
    public static char[] loadAllChars(@NotNull File source, @Min(0L) int offset) {
        return ioFile().loadAllChars(source, offset);
    }

    /**
     * @see IoSupport#loadAllChars(Object, int)
     */
    public static char[] loadAllChars(@NotNull URI source, @Min(0L) int offset) {
        return ioURI().loadAllChars(source, offset);
    }

    /**
     * @see IoSupport#loadAllChars(Object, int)
     */
    public static char[] loadAllChars(@NotNull URL source, @Min(0L) int offset) {
        return ioURL().loadAllChars(source, offset);
    }

    /**
     * @see IoSupport#loadAllChars(Object, Encoding)
     */
    public static char[] loadAllChars(@NotNull Path source, Encoding encoding) {
        return ioPath().loadAllChars(source, encoding);
    }

    /**
     * @see IoSupport#loadAllChars(Object, Encoding)
     */
    public static char[] loadAllChars(@NotNull File source, Encoding encoding) {
        return ioFile().loadAllChars(source, encoding);
    }

    /**
     * @see IoSupport#loadAllChars(Object, Encoding)
     */
    public static char[] loadAllChars(@NotNull URI source, Encoding encoding) {
        return ioURI().loadAllChars(source, encoding);
    }

    /**
     * @see IoSupport#loadAllChars(Object, Encoding)
     */
    public static char[] loadAllChars(@NotNull URL source, Encoding encoding) {
        return ioURL().loadAllChars(source, encoding);
    }

    /**
     * @see IoSupport#loadAllChars(Object, Encoding, int)
     */
    public static char[] loadAllChars(@NotNull Path source, Encoding encoding, @Min(0L) int offset) {
        return ioPath().loadAllChars(source, encoding, offset);
    }

    /**
     * @see IoSupport#loadAllChars(Object, Encoding, int)
     */
    public static char[] loadAllChars(@NotNull File source, Encoding encoding, @Min(0L) int offset) {
        return ioFile().loadAllChars(source, encoding, offset);
    }

    /**
     * @see IoSupport#loadAllChars(Object, Encoding, int)
     */
    public static char[] loadAllChars(@NotNull URI source, Encoding encoding, @Min(0L) int offset) {
        return ioURI().loadAllChars(source, encoding, offset);
    }

    /**
     * @see IoSupport#loadAllChars(Object, Encoding, int)
     */
    public static char[] loadAllChars(@NotNull URL source, Encoding encoding, @Min(0L) int offset) {
        return ioURL().loadAllChars(source, encoding, offset);
    }

    /**
     * @see IoSupport#loadBytes(Object, int)
     */
    public static byte[] loadBytes(@NotNull Path source, @Min(1L) int size) {
        return ioPath().loadBytes(source, size);
    }

    /**
     * @see IoSupport#loadBytes(Object, int)
     */
    public static byte[] loadBytes(@NotNull File source, @Min(1L) int size) {
        return ioFile().loadBytes(source, size);
    }

    /**
     * @see IoSupport#loadBytes(Object, int)
     */
    public static byte[] loadBytes(@NotNull URI source, @Min(1L) int size) {
        return ioURI().loadBytes(source, size);
    }

    /**
     * @see IoSupport#loadBytes(Object, int)
     */
    public static byte[] loadBytes(@NotNull URL source, @Min(1L) int size) {
        return ioURL().loadBytes(source, size);
    }

    /**
     * @see IoSupport#loadBytes(Object, int, int)
     */
    public static byte[] loadBytes(@NotNull Path source, @Min(0L) int offset, @Min(1L) int size) {
        return ioPath().loadBytes(source, offset, size);
    }

    /**
     * @see IoSupport#loadBytes(Object, int, int)
     */
    public static byte[] loadBytes(@NotNull File source, @Min(0L) int offset, @Min(1L) int size) {
        return ioFile().loadBytes(source, offset, size);
    }

    /**
     * @see IoSupport#loadBytes(Object, int, int)
     */
    public static byte[] loadBytes(@NotNull URI source, @Min(0L) int offset, @Min(1L) int size) {
        return ioURI().loadBytes(source, offset, size);
    }

    /**
     * @see IoSupport#loadBytes(Object, int, int)
     */
    public static byte[] loadBytes(@NotNull URL source, @Min(0L) int offset, @Min(1L) int size) {
        return ioURL().loadBytes(source, offset, size);
    }

    /**
     * @see IoSupport#loadChars(Object, int)
     */
    public static char[] loadChars(@NotNull Path source, @Min(1L) int size) {
        return ioPath().loadChars(source, size);
    }

    /**
     * @see IoSupport#loadChars(Object, int)
     */
    public static char[] loadChars(@NotNull File source, @Min(1L) int size) {
        return ioFile().loadChars(source, size);
    }

    /**
     * @see IoSupport#loadChars(Object, int)
     */
    public static char[] loadChars(@NotNull URI source, @Min(1L) int size) {
        return ioURI().loadChars(source, size);
    }

    /**
     * @see IoSupport#loadChars(Object, int)
     */
    public static char[] loadChars(@NotNull URL source, @Min(1L) int size) {
        return ioURL().loadChars(source, size);
    }

    /**
     * @see IoSupport#loadChars(Object, int, int)
     */
    public static char[] loadChars(@NotNull Path source, @Min(0L) int offset, @Min(1L) int size) {
        return ioPath().loadChars(source, offset, size);
    }

    /**
     * @see IoSupport#loadChars(Object, int, int)
     */
    public static char[] loadChars(@NotNull File source, @Min(0L) int offset, @Min(1L) int size) {
        return ioFile().loadChars(source, offset, size);
    }

    /**
     * @see IoSupport#loadChars(Object, int, int)
     */
    public static char[] loadChars(@NotNull URI source, @Min(0L) int offset, @Min(1L) int size) {
        return ioURI().loadChars(source, offset, size);
    }

    /**
     * @see IoSupport#loadChars(Object, int, int)
     */
    public static char[] loadChars(@NotNull URL source, @Min(0L) int offset, @Min(1L) int size) {
        return ioURL().loadChars(source, offset, size);
    }

    /**
     * @see IoSupport#loadChars(Object, Encoding, int)
     */
    public static char[] loadChars(@NotNull Path source, Encoding encoding, @Min(1L) int size) {
        return ioPath().loadChars(source, encoding, size);
    }

    /**
     * @see IoSupport#loadChars(Object, Encoding, int)
     */
    public static char[] loadChars(@NotNull File source, Encoding encoding, @Min(1L) int size) {
        return ioFile().loadChars(source, encoding, size);
    }

    /**
     * @see IoSupport#loadChars(Object, Encoding, int)
     */
    public static char[] loadChars(@NotNull URI source, Encoding encoding, @Min(1L) int size) {
        return ioURI().loadChars(source, encoding, size);
    }

    /**
     * @see IoSupport#loadChars(Object, Encoding, int)
     */
    public static char[] loadChars(@NotNull URL source, Encoding encoding, @Min(1L) int size) {
        return ioURL().loadChars(source, encoding, size);
    }

    /**
     * @see IoSupport#loadChars(Object, Encoding, int, int)
     */
    public static char[] loadChars(@NotNull Path source, Encoding encoding, @Min(0L) int offset, @Min(1L) int size) {
        return ioPath().loadChars(source, encoding, offset, size);
    }

    /**
     * @see IoSupport#loadChars(Object, Encoding, int, int)
     */
    public static char[] loadChars(@NotNull File source, Encoding encoding, @Min(0L) int offset, @Min(1L) int size) {
        return ioFile().loadChars(source, encoding, offset, size);
    }

    /**
     * @see IoSupport#loadChars(Object, Encoding, int, int)
     */
    public static char[] loadChars(@NotNull URI source, Encoding encoding, @Min(0L) int offset, @Min(1L) int size) {
        return ioURI().loadChars(source, encoding, offset, size);
    }

    /**
     * @see IoSupport#loadChars(Object, Encoding, int, int)
     */
    public static char[] loadChars(@NotNull URL source, Encoding encoding, @Min(0L) int offset, @Min(1L) int size) {
        return ioURL().loadChars(source, encoding, offset, size);
    }

    /**
     * @see IoSupport#newInputStream(Object)
     */
    public static @NotNull InputStream newInputStream(@NotNull Path source) {
        return ioPath().newInputStream(source);
    }

    /**
     * @see IoSupport#newInputStream(Object)
     */
    public static @NotNull InputStream newInputStream(@NotNull File source) {
        return ioFile().newInputStream(source);
    }

    /**
     * @see IoSupport#newInputStream(Object)
     */
    public static @NotNull InputStream newInputStream(@NotNull URI source) {
        return ioURI().newInputStream(source);
    }

    /**
     * @see IoSupport#newInputStream(Object)
     */
    public static @NotNull InputStream newInputStream(@NotNull URL source) {
        return ioURL().newInputStream(source);
    }

    /**
     * @see IoSupport#newOutputStream(Object)
     */
    public static @NotNull OutputStream newOutputStream(@NotNull Path destination) {
        return ioPath().newOutputStream(destination);
    }

    /**
     * @see IoSupport#newOutputStream(Object)
     */
    public static @NotNull OutputStream newOutputStream(@NotNull File destination) {
        return ioFile().newOutputStream(destination);
    }

    /**
     * @see IoSupport#newOutputStream(Object)
     */
    public static @NotNull OutputStream newOutputStream(@NotNull URI destination) {
        return ioURI().newOutputStream(destination);
    }

    /**
     * @see IoSupport#newReader(Object)
     */
    public static @NotNull Reader newReader(@NotNull Path source) {
        return ioPath().newReader(source);
    }

    /**
     * @see IoSupport#newReader(Object)
     */
    public static @NotNull Reader newReader(@NotNull File source) {
        return ioFile().newReader(source);
    }

    /**
     * @see IoSupport#newReader(Object)
     */
    public static @NotNull Reader newReader(@NotNull URI source) {
        return ioURI().newReader(source);
    }

    /**
     * @see IoSupport#newReader(Object)
     */
    public static @NotNull Reader newReader(@NotNull URL source) {
        return ioURL().newReader(source);
    }

    /**
     * @see IoSupport#newReader(Object, Encoding)
     */
    public static @NotNull Reader newReader(@NotNull Path source, Encoding encoding) {
        return ioPath().newReader(source, encoding);
    }

    /**
     * @see IoSupport#newReader(Object, Encoding)
     */
    public static @NotNull Reader newReader(@NotNull File source, Encoding encoding) {
        return ioFile().newReader(source, encoding);
    }

    /**
     * @see IoSupport#newReader(Object, Encoding)
     */
    public static @NotNull Reader newReader(@NotNull URI source, Encoding encoding) {
        return ioURI().newReader(source, encoding);
    }

    /**
     * @see IoSupport#newReader(Object, Encoding)
     */
    public static @NotNull Reader newReader(@NotNull URL source, Encoding encoding) {
        return ioURL().newReader(source, encoding);
    }

    /**
     * @see IoSupport#newWriter(Object)
     */
    public static @NotNull Writer newWriter(@NotNull Path destination) {
        return ioPath().newWriter(destination);
    }

    /**
     * @see IoSupport#newWriter(Object)
     */
    public static @NotNull Writer newWriter(@NotNull File destination) {
        return ioFile().newWriter(destination);
    }

    /**
     * @see IoSupport#newWriter(Object)
     */
    public static @NotNull Writer newWriter(@NotNull URI destination) {
        return ioURI().newWriter(destination);
    }

    /**
     * @see IoSupport#newWriter(Object, Encoding)
     */
    public static @NotNull Writer newWriter(@NotNull Path destination, Encoding encoding) {
        return ioPath().newWriter(destination, encoding);
    }

    /**
     * @see IoSupport#newWriter(Object, Encoding)
     */
    public static @NotNull Writer newWriter(@NotNull File destination, Encoding encoding) {
        return ioFile().newWriter(destination, encoding);
    }

    /**
     * @see IoSupport#newWriter(Object, Encoding)
     */
    public static @NotNull Writer newWriter(@NotNull URI destination, Encoding encoding) {
        return ioURI().newWriter(destination, encoding);
    }

    /**
     * @see IoSupport#readText(Object)
     */
    public static @NotNull String readText(@NotNull Path source) {
        return ioPath().readText(source);
    }

    /**
     * @see IoSupport#readText(Object)
     */
    public static @NotNull String readText(@NotNull File source) {
        return ioFile().readText(source);
    }

    /**
     * @see IoSupport#readText(Object)
     */
    public static @NotNull String readText(@NotNull URI source) {
        return ioURI().readText(source);
    }

    /**
     * @see IoSupport#readText(Object)
     */
    public static @NotNull String readText(@NotNull URL source) {
        return ioURL().readText(source);
    }

    /**
     * @see IoSupport#readText(Object, Encoding)
     */
    public static @NotNull String readText(@NotNull Path source, Encoding encoding) {
        return ioPath().readText(source, encoding);
    }

    /**
     * @see IoSupport#readText(Object, Encoding)
     */
    public static @NotNull String readText(@NotNull File source, Encoding encoding) {
        return ioFile().readText(source, encoding);
    }

    /**
     * @see IoSupport#readText(Object, Encoding)
     */
    public static @NotNull String readText(@NotNull URI source, Encoding encoding) {
        return ioURI().readText(source, encoding);
    }

    /**
     * @see IoSupport#readText(Object, Encoding)
     */
    public static @NotNull String readText(@NotNull URL source, Encoding encoding) {
        return ioURL().readText(source, encoding);
    }

    /**
     * @see IoSupport#saveBytes(Object, byte[])
     */
    public static void saveBytes(@NotNull Path destination, @NotNull byte[] data) {
        ioPath().saveBytes(destination, data);
    }

    /**
     * @see IoSupport#saveBytes(Object, byte[])
     */
    public static void saveBytes(@NotNull File destination, @NotNull byte[] data) {
        ioFile().saveBytes(destination, data);
    }

    /**
     * @see IoSupport#saveBytes(Object, byte[])
     */
    public static void saveBytes(@NotNull URI destination, @NotNull byte[] data) {
        ioURI().saveBytes(destination, data);
    }

    /**
     * @see IoSupport#saveBytes(Object, byte[], int, int)
     */
    public static void saveBytes(@NotNull Path destination, @NotNull byte[] data, @Min(0L) int offset, @Min(0L) int length) {
        ioPath().saveBytes(destination, data, offset, length);
    }

    /**
     * @see IoSupport#saveBytes(Object, byte[], int, int)
     */
    public static void saveBytes(@NotNull File destination, @NotNull byte[] data, @Min(0L) int offset, @Min(0L) int length) {
        ioFile().saveBytes(destination, data, offset, length);
    }

    /**
     * @see IoSupport#saveBytes(Object, byte[], int, int)
     */
    public static void saveBytes(@NotNull URI destination, @NotNull byte[] data, @Min(0L) int offset, @Min(0L) int length) {
        ioURI().saveBytes(destination, data, offset, length);
    }

    /**
     * @see IoSupport#saveChars(Object, char[])
     */
    public static void saveChars(@NotNull Path destination, @NotNull char[] data) {
        ioPath().saveChars(destination, data);
    }

    /**
     * @see IoSupport#saveChars(Object, char[])
     */
    public static void saveChars(@NotNull File destination, @NotNull char[] data) {
        ioFile().saveChars(destination, data);
    }

    /**
     * @see IoSupport#saveChars(Object, char[])
     */
    public static void saveChars(@NotNull URI destination, @NotNull char[] data) {
        ioURI().saveChars(destination, data);
    }

    /**
     * @see IoSupport#saveChars(Object, Encoding, char[])
     */
    public static void saveChars(@NotNull Path destination, Encoding encoding, @NotNull char[] data) {
        ioPath().saveChars(destination, encoding, data);
    }

    /**
     * @see IoSupport#saveChars(Object, Encoding, char[])
     */
    public static void saveChars(@NotNull File destination, Encoding encoding, @NotNull char[] data) {
        ioFile().saveChars(destination, encoding, data);
    }

    /**
     * @see IoSupport#saveChars(Object, Encoding, char[])
     */
    public static void saveChars(@NotNull URI destination, Encoding encoding, @NotNull char[] data) {
        ioURI().saveChars(destination, encoding, data);
    }

    /**
     * @see IoSupport#saveChars(Object, Encoding, char[], int, int)
     */
    public static void saveChars(@NotNull Path destination, Encoding encoding, char[] data, @Min(0L) int offset, @Min(0L) int size) {
        ioPath().saveChars(destination, encoding, data, offset, size);
    }

    /**
     * @see IoSupport#saveChars(Object, Encoding, char[], int, int)
     */
    public static void saveChars(@NotNull File destination, Encoding encoding, char[] data, @Min(0L) int offset, @Min(0L) int size) {
        ioFile().saveChars(destination, encoding, data, offset, size);
    }

    /**
     * @see IoSupport#saveChars(Object, Encoding, char[], int, int)
     */
    public static void saveChars(@NotNull URI destination, Encoding encoding, char[] data, @Min(0L) int offset, @Min(0L) int size) {
        ioURI().saveChars(destination, encoding, data, offset, size);
    }

    /**
     * @see IoSupport#writeText(Object, String)
     */
    public static void writeText(@NotNull Path destination, @NotNull String text) {
        ioPath().writeText(destination, text);
    }

    /**
     * @see IoSupport#writeText(Object, String)
     */
    public static void writeText(@NotNull File destination, @NotNull String text) {
        ioFile().writeText(destination, text);
    }

    /**
     * @see IoSupport#writeText(Object, String)
     */
    public static void writeText(@NotNull URI destination, @NotNull String text) {
        ioURI().writeText(destination, text);
    }

    /**
     * @see IoSupport#writeText(Object, Encoding, String)
     */
    public static void writeText(@NotNull Path destination, Encoding encoding, @NotNull String text) {
        ioPath().writeText(destination, encoding, text);
    }

    /**
     * @see IoSupport#writeText(Object, Encoding, String)
     */
    public static void writeText(@NotNull File destination, Encoding encoding, @NotNull String text) {
        ioFile().writeText(destination, encoding, text);
    }

    /**
     * @see IoSupport#writeText(Object, Encoding, String)
     */
    public static void writeText(@NotNull URI destination, Encoding encoding, @NotNull String text) {
        ioURI().writeText(destination, encoding, text);
    }

} /* ENDCLASS */
