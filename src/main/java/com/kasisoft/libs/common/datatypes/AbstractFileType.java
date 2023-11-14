package com.kasisoft.libs.common.datatypes;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.utils.*;

import java.util.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public abstract class AbstractFileType implements FileType {

    private int          minSize;

    private MimeType     contentType;

    private int          offset;

    private List<byte[]> magics;

    public AbstractFileType(int minSize, MimeType contentType, int offset, List<byte[]> magics) {
        this.minSize     = minSize;
        this.contentType = contentType;
        this.offset      = offset;
        this.magics      = magics;
    }

    @Override
    public int getMinSize() {
        return minSize;
    }

    @Override
    public MimeType getContentType() {
        return contentType;
    }

    @Override
    public boolean test(byte[] data) {
        if ((data != null) && (getMinSize() <= data.length)) {
            for (byte[] magic : magics) {
                if (PrimitiveFunctions.compare(data, magic, offset)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return getContentType().getMimeType();
    }

} /* ENDCLASS */
