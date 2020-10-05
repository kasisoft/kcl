package com.kasisoft.libs.common.datatypes;

import com.kasisoft.libs.common.constants.*;

import java.util.*;

/**
 * FileType for 'bzip2' files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Bzip2FileType extends AbstractFileType {

  private static final byte[] MAGIC = "BZh".getBytes(); 
  
  public Bzip2FileType() {
    super(3, MimeType.Bzip2, 0, Arrays.asList(MAGIC));
  }
  
} /* ENDCLASS */
