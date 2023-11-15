package com.kasisoft.libs.common.datatypes;

import com.kasisoft.libs.common.constants.*;

import java.util.zip.*;

/**
 * FileType for 'gzip' files.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class GzipFileType implements FileType {

    @Override
    public int getMinSize() {
        return 2;
    }

    @Override
    public boolean test(byte[] data) {
        if (getMinSize() <= data.length) {
            return (((data[1] << 8) | data[0]) & 0x0000FFFF) == GZIPInputStream.GZIP_MAGIC;
        }
        return false;
    }

    @Override
    public MimeType getContentType() {
        return MimeType.GZip;
    }

    @Override
    public String toString() {
        return getContentType().getMimeType();
    }

} /* ENDCLASS */
