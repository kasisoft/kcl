package com.kasisoft.libs.common.datatypes;

import com.kasisoft.libs.common.constants.*;

import java.util.*;

/**
 * FileType for 'gif' files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class GifFileType extends AbstractFileType {

    private static final byte[] MAGIC1 = "GIF87a".getBytes();

    private static final byte[] MAGIC2 = "GIF89a".getBytes();

    public GifFileType() {
        super(6, MimeType.Gif, 0, Arrays.asList(MAGIC1, MAGIC2));
    }

} /* ENDCLASS */
