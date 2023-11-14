package com.kasisoft.libs.common.converters;

import jakarta.validation.constraints.*;

import java.nio.file.*;

/**
 * Adapter for Path values.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class NioPathAdapter extends AbstractConverter<String, Path> {

    @Override
    public String encodeImpl(@NotNull Path v) {
        return v.normalize().toString().replace('\\', '/');
    }

    @Override
    public Path decodeImpl(@NotNull String v) {
        return Paths.get(v.replace('\\', '/')).normalize();
    }

} /* ENDCLASS */
