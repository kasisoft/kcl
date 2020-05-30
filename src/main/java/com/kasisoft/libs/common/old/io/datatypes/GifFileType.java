package com.kasisoft.libs.common.old.io.datatypes;

import static com.kasisoft.libs.common.old.constants.Primitive.*;

import com.kasisoft.libs.common.old.constants.*;

/**
 * FileType for 'gif' files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class GifFileType implements FileType {

  private static final byte[] MAGIC1 = "GIF87a".getBytes(); 
  private static final byte[] MAGIC2 = "GIF89a".getBytes();
  
  @Override
  public int getMinSize() {
    return 6;
  }

  @Override
  public boolean test( byte[] data ) {
    if( getMinSize() <= data.length ) {
      return PByte.compare( data, MAGIC1 ) || PByte.compare( data, MAGIC2 );
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
    return MimeType.Gif;
  }

} /* ENDCLASS */
