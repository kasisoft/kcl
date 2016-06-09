package com.kasisoft.libs.common.io.datatypes;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.util.*;

/**
 * FileType for 'pdf' files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class PdfFileType implements FileType {

  static final byte[] MAGIC = "%PDF".getBytes(); 
  
  @Override
  public int getMinSize() {
    return 4;
  }

  @Override
  public boolean test( byte[] data ) {
    if( getMinSize() <= data.length ) {
      return ArrayFunctions.compare( data, MAGIC, 0 );
    } else {
      return false;
    }
  }

  @Override
  public String getMimeType() {
    return MimeType.AdobePdf.getMimeType();
  }

  @Override
  public String toString() {
    return getMimeType();
  }

  @Override
  public String getSuffix() {
    return ".pdf";
  }

} /* ENDCLASS */