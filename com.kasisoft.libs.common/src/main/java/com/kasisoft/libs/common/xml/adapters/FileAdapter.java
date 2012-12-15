/**
 * Name........: FileAdapter
 * Description.: Simple adapter for File types.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml.adapters;

import java.io.*;

/**
 * Simple adapter for File types.
 */
public class FileAdapter extends NullSafeAdapter<String,File> {

  private boolean   canonical;
  
  public FileAdapter() {
    canonical = false;
  }
  
  public FileAdapter( boolean canonicalfiles ) {
    canonical = canonicalfiles;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String marshalImpl( File v ) throws Exception {
    if( canonical ) {
      v = v.getCanonicalFile();
    }
    return v.getPath().replace( '\\', '/' );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File unmarshalImpl( String v ) throws Exception {
    File result = new File( v.replace( '\\', '/' ).replace( '/', File.separatorChar ) );
    if( canonical ) {
      result = result.getCanonicalFile();
    }
    return result;
  }

} /* ENDCLASS */
