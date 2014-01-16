/**
 * Name........: GifFileType
 * Description.: FileType for 'gif' files.
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
 * FileType for 'gif' files.
 */
public class GifFileType implements FileType {

  private static final byte[] MAGIC1 = "GIF87a".getBytes(); 
  private static final byte[] MAGIC2 = "GIF89a".getBytes();
  
  @Override
  public int getMinSize() {
    return 6;
  }

  @Override
  public boolean isOfType( byte[] data ) {
    return ArrayFunctions.compare( data, MAGIC1, 0 ) || ArrayFunctions.compare( data, MAGIC2, 0 );
  }

  @Override
  public String getMimeType() {
    return MimeType.Gif.getMimeType();
  }

  @Override
  public String toString() {
    return getMimeType();
  }

} /* ENDCLASS */
