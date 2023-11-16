package com.kasisoft.libs.common.io.impl;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.internal.*;

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import java.nio.file.*;

import java.nio.charset.*;

import java.io.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class PathIoSupport implements IoSupport<Path> {

    private static final long MB_16 = 16 * 1024 * 1024;

    @Override
    public InputStream newInputStreamImpl(Path source) throws Exception {
        return Files.newInputStream(source);
    }

    @Override
    public OutputStream newOutputStreamImpl(Path destination) throws Exception {
        return Files.newOutputStream(destination);
    }

    @Override
    @NotNull
    public byte[] loadAllBytes(@NotNull Path source) {
        try {
            var fileSize = Files.size(source);
            if (fileSize <= MB_16) {
                return Files.readAllBytes(source);
            } else {
                return IoSupport.super.loadAllBytes(source);
            }
        } catch (Exception ex) {
            throw new KclException(ex, Messages.error_failed_to_read_from.formatted(source));
        }
    }

    @Override
    @NotNull
    public String readText(@NotNull Path source, Encoding encoding) {
        try {
            var fileSize = Files.size(source);
            if (fileSize <= MB_16) {
                return Files.readString(source, encoding != null ? encoding.getCharset() : StandardCharsets.UTF_8);
            } else {
                return IoSupport.super.readText(source, encoding);
            }
        } catch (Exception ex) {
            throw new KclException(ex, Messages.error_failed_to_read_from.formatted(source));
        }
    }

} /* ENDCLASS */
