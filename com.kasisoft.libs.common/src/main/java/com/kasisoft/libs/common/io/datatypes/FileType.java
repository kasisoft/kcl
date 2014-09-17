package com.kasisoft.libs.common.io.datatypes;

/**
 * A simple definition of characteristics used to identify file types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface FileType {
  
  int getMinSize();
  
  boolean isOfType( byte[] data );
  
  String getMimeType();
  
} /* ENDINTERFACE */
