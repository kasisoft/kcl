/**
 * Name........: JpegFileType
 * Description.: FileType for 'jpeg' files.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.internal.io.datatypes;

import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.io.datatypes.*;
import com.kasisoft.libs.common.util.*;

/**
 * FileType for 'jpeg' files.
 */
public class JpegFileType implements FileType {

  private static final byte[] MAGIC = "JFIF".getBytes(); 
  
  @Override
  public int getMinSize() {
    return 10;
  }

  @Override
  public boolean isOfType( byte[] data ) {
    return ArrayFunctions.compare( data, MAGIC, 6 );
  }

  @Override
  public String getMimeType() {
    return MimeType.Jpeg.getMimeType();
  }

  @Override
  public String toString() {
    return getMimeType();
  }

} /* ENDCLASS */