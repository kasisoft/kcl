package com.kasisoft.libs.common.internal.io.datatypes;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.io.datatypes.*;

import java.util.zip.*;

/**
 * FileType for 'gzip' files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class GzipFileType implements FileType {

  @Override
  public int getMinSize() {
    return 2;
  }

  @Override
  public boolean test( byte[] data ) {
    if( getMinSize() <= data.length ) {
      return ( ((data[1] << 8) | data[0]) & 0x0000FFFF ) == GZIPInputStream.GZIP_MAGIC;
    } else {
      return false;
    }
  }

  @Override
  public String getMimeType() {
    return MimeType.GZip.getMimeType();
  }

  @Override
  public String toString() {
    return getMimeType();
  }

  @Override
  public String getSuffix() {
    return ".gz";
  }

} /* ENDCLASS */
