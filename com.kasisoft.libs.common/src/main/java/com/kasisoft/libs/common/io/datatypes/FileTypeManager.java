/**
 * Name........: FileTypeManager
 * Description.: Management for file type recognizers.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.io.datatypes;

import com.kasisoft.libs.common.io.*;
import com.kasisoft.libs.common.util.*;

import java.util.*;

import java.net.*;

import java.io.*;

import lombok.*;

/**
 * Management for file type recognizers.
 */
public class FileTypeManager {

  private List<FileType>    recognizers;
  private int               maxspace;
  
  /**
   * Initializes this management type while looking for all SPI declarations.
   */
  public FileTypeManager() {
    recognizers = MiscFunctions.loadSPIServices( FileType.class );
    Collections.sort( recognizers, new Comparator<FileType>() {
      @Override
      public int compare( FileType f1, FileType f2 ) {
        Integer i1 = Integer.valueOf( f1.getMinSize());
        Integer i2 = Integer.valueOf( f2.getMinSize() );
        return i2.compareTo(i1);
      }
    });
    if( ! recognizers.isEmpty() ) {
      maxspace = recognizers.get(0).getMinSize();
    } else {
      maxspace = 0;
    }
  }
  
  /**
   * Returns a list with all known FileType instances.
   * 
   * @return   A list with all known FileType instances. Not <code>null</code>.
   */
  public FileType[] getFileTypes() {
    return recognizers.toArray( new FileType[ recognizers.size() ] );
  }
  
  /**
   * Identifies the FileType for the supplied file.
   * 
   * @param file   The File which type shall be identified. Not <code>null</code>.
   * 
   * @return   The FileType if it could be identified. Maybe <code>null</code>.
   * 
   * @throws FailureException   Accessing the file failed for some reason.
   */
  public FileType identify( @NonNull File file ) {
    if( file.isFile() && (file.length() > 0) ) {
      return identify( IoFunctions.loadFragment( file, 0, maxspace ) );
    }
    return null;
  }

  /**
   * Identifies the FileType for the supplied resource.
   * 
   * @param url   The URL which type shall be identified. Not <code>null</code>.
   * 
   * @return   The FileType if it could be identified. Maybe <code>null</code>.
   * 
   * @throws FailureException   Accessing the resource failed for some reason.
   */
  public FileType identify( @NonNull URL url ) {
    return identify( IoFunctions.loadFragment( url, 0, maxspace ) );
  }

  /**
   * Identifies the FileType for the supplied data.
   * 
   * @param data   The data of the input which type shall be identified. Not <code>null</code>.
   * 
   * @return   The FileType if it could be identified. Maybe <code>null</code>.
   */
  public FileType identify( @NonNull byte[] data ) {
    for( int i = 0; i < recognizers.size(); i++ ) {
      FileType current = recognizers.get(i);
      if( (current.getMinSize() <= data.length) && current.isOfType( data ) ) {
        return current;
      }
    }
    return null;
  }
  
} /* ENDCLASS */
