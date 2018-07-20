package com.kasisoft.libs.common.io.datatypes;

import static com.kasisoft.libs.common.constants.Primitive.*;

import com.kasisoft.libs.common.constants.*;

/**
 * FileType for 'pdf' files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class PdfFileType implements FileType {

  private static final byte[] MAGIC = "%PDF".getBytes(); 
  
  @Override
  public int getMinSize() {
    return 4;
  }

  @Override
  public boolean test( byte[] data ) {
    if( getMinSize() <= data.length ) {
      return PByte.compare( data, MAGIC );
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
