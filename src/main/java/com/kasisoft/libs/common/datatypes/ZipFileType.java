package com.kasisoft.libs.common.datatypes;

import com.kasisoft.libs.common.constants.MimeType;

import java.util.Arrays;

/**
 * FileType for 'zip' files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ZipFileType extends AbstractFileType {

  private static final byte[] MAGIC = new byte[] {'P', 'K', 3, 4}; 
  
  public ZipFileType() {
    super(4, MimeType.Zip, 0, Arrays.asList(MAGIC));
  }
  
} /* ENDCLASS */
