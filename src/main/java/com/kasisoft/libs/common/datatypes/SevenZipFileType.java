package com.kasisoft.libs.common.datatypes;

import com.kasisoft.libs.common.constants.*;

import java.util.*;

/**
 * FileType for '7z' files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class SevenZipFileType extends AbstractFileType {

  private static final byte[] MAGIC = new byte[] {(byte) 0x37, (byte) 0x7A, (byte) 0xBC, (byte) 0xAF, (byte) 0x27, (byte) 0x1C};
  
  public SevenZipFileType() {
    super(6, MimeType.SevenZip, 0, Arrays.asList(MAGIC));
  }

} /* ENDCLASS */
