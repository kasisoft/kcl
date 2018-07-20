package com.kasisoft.libs.common.io.datatypes;

import static com.kasisoft.libs.common.constants.Primitive.*;

import com.kasisoft.libs.common.constants.*;

/**
 * FileType for 'bzip2' files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Bzip2FileType implements FileType {

  private static final byte[] MAGIC = "BZh".getBytes(); 
  
  @Override
  public int getMinSize() {
    return 3;
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
    return MimeType.Bzip2.getMimeType();
  }

  @Override
  public String toString() {
    return getMimeType();
  }

  @Override
  public String getSuffix() {
    return ".bz2";
  }

} /* ENDCLASS */
