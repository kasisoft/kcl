package com.kasisoft.libs.common.internal.io.datatypes;

import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.io.datatypes.*;
import com.kasisoft.libs.common.util.*;

/**
 * FileType for 'png' files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class PngFileType implements FileType {

  private static final byte[] MAGIC = "PNG".getBytes(); 
  
  @Override
  public int getMinSize() {
    return 4;
  }

  @Override
  public boolean isOfType( byte[] data ) {
    return ArrayFunctions.compare( data, MAGIC, 1 );
  }

  @Override
  public String getMimeType() {
    return MimeType.Png.getMimeType();
  }

  @Override
  public String toString() {
    return getMimeType();
  }

} /* ENDCLASS */
