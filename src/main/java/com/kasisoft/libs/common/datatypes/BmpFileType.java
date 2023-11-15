package com.kasisoft.libs.common.datatypes;

import com.kasisoft.libs.common.constants.*;

import java.util.*;

/**
 * FileType for 'bmp' files.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class BmpFileType extends AbstractFileType {

    private static final byte[] MAGIC = "BM".getBytes();

    public BmpFileType() {
        super(4, MimeType.Bitmap, 0, Arrays.asList(MAGIC));
    }

} /* ENDCLASS */
