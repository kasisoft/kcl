package com.kasisoft.libs.common.io.datatypes;

import static com.kasisoft.libs.common.constants.Primitive.*;

import com.kasisoft.libs.common.constants.*;

/**
 * FileType for 'bmp' files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class JavaClassFileType implements FileType {

  private static final byte[] MAGIC = new byte[] { (byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE }; 
  
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
  public String toString() {
    return getContentType().getMimeType();
  }
  
  @Override
  public MimeType getContentType() {
    return MimeType.JavaBytecode;
  }
  
} /* ENDCLASS */