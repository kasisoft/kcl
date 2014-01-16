/**
 * Name........: GzipFileType
 * Description.: FileType for 'gzip' files.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.internal.io.datatypes;

import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.io.datatypes.*;

import java.util.zip.*;

/**
 * FileType for 'gzip' files.
 */
public class GzipFileType implements FileType {

  @Override
  public int getMinSize() {
    return 2;
  }

  @Override
  public boolean isOfType( byte[] data ) {
    return ( ((data[1] << 8) | data[0]) & 0x0000FFFF ) == GZIPInputStream.GZIP_MAGIC;
  }

  @Override
  public String getMimeType() {
    return MimeType.GZip.getMimeType();
  }

  @Override
  public String toString() {
    return getMimeType();
  }

} /* ENDCLASS */
