package com.kasisoft.libs.common.datatypes;

import com.kasisoft.libs.common.constants.*;

import java.util.*;

/**
 * FileType for 'class' files.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class JavaClassFileType extends AbstractFileType {

    private static final byte[] MAGIC = new byte[] {(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE};

    public JavaClassFileType() {
        super(4, MimeType.JavaBytecode, 0, Arrays.asList(MAGIC));
    }

} /* ENDCLASS */
