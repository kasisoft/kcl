package com.kasisoft.libs.common.old.io.datatypes;

import static com.kasisoft.libs.common.old.constants.Primitive.*;

import com.kasisoft.libs.common.old.constants.*;

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
  public String toString() {
    return getContentType().getMimeType();
  }

  @Override
  public MimeType getContentType() {
    return MimeType.Bzip2;
  }

} /* ENDCLASS */
