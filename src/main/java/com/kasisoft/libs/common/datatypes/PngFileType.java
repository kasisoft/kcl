package com.kasisoft.libs.common.datatypes;

import com.kasisoft.libs.common.constants.*;

import java.util.*;

/**
 * FileType for 'png' files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class PngFileType extends AbstractFileType {

    private static final byte[] MAGIC = "PNG".getBytes();

    public PngFileType() {
        super(4, MimeType.Png, 1, Arrays.asList(MAGIC));
    }

} /* ENDCLASS */
