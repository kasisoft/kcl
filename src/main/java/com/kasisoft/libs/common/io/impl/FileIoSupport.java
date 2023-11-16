package com.kasisoft.libs.common.io.impl;

import static com.kasisoft.libs.common.internal.Messages.*;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import java.nio.charset.*;

import java.nio.file.*;

import java.io.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class FileIoSupport implements IoSupport<File> {

    private static final long MB_16 = 16 * 1024 * 1024;

    @Override
    public InputStream newInputStreamImpl(File source) throws Exception {
        return Files.newInputStream(source.toPath());
    }

    @Override
    public OutputStream newOutputStreamImpl(File destination) throws Exception {
        return Files.newOutputStream(destination.toPath());
    }

    @Override
    @NotNull
    public byte[] loadAllBytes(@NotNull File source) {
        try {
            var fileSize = source.length();
            if (fileSize <= MB_16) {
                return Files.readAllBytes(source.toPath());
            } else {
                return IoSupport.super.loadAllBytes(source);
            }
        } catch (Exception ex) {
            throw new KclException(ex, error_failed_to_read_from.formatted(source));
        }
    }

    @Override
    @NotNull
    public String readText(@NotNull File source, Encoding encoding) {
        try {
            var fileSize = source.length();
            if (fileSize <= MB_16) {
                return Files.readString(source.toPath(), encoding != null ? encoding.getCharset()
                    : StandardCharsets.UTF_8);
            } else {
                return IoSupport.super.readText(source, encoding);
            }
        } catch (Exception ex) {
            throw new KclException(ex, error_failed_to_read_from.formatted(source));
        }
    }

} /* ENDCLASS */
