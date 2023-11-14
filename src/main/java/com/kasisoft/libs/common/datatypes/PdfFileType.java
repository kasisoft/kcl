package com.kasisoft.libs.common.datatypes;

import com.kasisoft.libs.common.constants.*;

import java.util.*;

/**
 * FileType for 'pdf' files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class PdfFileType extends AbstractFileType {

    private static final byte[] MAGIC = "%PDF".getBytes();

    public PdfFileType() {
        super(4, MimeType.AdobePdf, 0, Arrays.asList(MAGIC));
    }

} /* ENDCLASS */
