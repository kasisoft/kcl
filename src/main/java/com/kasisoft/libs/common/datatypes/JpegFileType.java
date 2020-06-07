package com.kasisoft.libs.common.datatypes;

import com.kasisoft.libs.common.constants.MimeType;

import java.util.Arrays;

/**
 * FileType for 'jpeg' files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class JpegFileType extends AbstractFileType {

  private static final byte[] MAGIC = "JFIF".getBytes(); 
  
  public JpegFileType() {
    super(10, MimeType.Jpeg, 6, Arrays.asList(MAGIC));
  }
  
} /* ENDCLASS */
