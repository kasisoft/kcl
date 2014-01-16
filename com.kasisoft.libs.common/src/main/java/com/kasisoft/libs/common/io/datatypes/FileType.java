/**
 * Name........: FileType
 * Description.: A simple definition of characteristics used to identify file types.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.io.datatypes;

/**
 * A simple definition of characteristics used to identify file types.
 */
public interface FileType {
  
  int getMinSize();
  
  boolean isOfType( byte[] data );
  
  String getMimeType();
  
} /* ENDINTERFACE */
