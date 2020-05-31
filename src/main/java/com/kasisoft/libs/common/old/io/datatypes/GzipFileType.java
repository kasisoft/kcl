package com.kasisoft.libs.common.old.io.datatypes;

import com.kasisoft.libs.common.old.constants.MimeType;

import java.util.zip.GZIPInputStream;

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
  public String toString() {
    return getContentType().getMimeType();
  }

  @Override
  public MimeType getContentType() {
    return MimeType.GZip;
  }

} /* ENDCLASS */
