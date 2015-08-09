package com.kasisoft.libs.common.io.datatypes;

import com.kasisoft.libs.common.base.*;

import com.kasisoft.libs.common.spi.*;

import com.kasisoft.libs.common.io.*;

import lombok.experimental.*;

import lombok.*;

import java.util.*;

import java.net.*;

import java.io.*;

/**
 * Management for file type recognizers.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileTypeManager {

  List<FileType>    filetypes;
  int               maxspace;
  
  /**
   * Initializes this management type while looking for all SPI declarations.
   */
  public FileTypeManager() {
    filetypes = SPIFunctions.loadSPIServices( FileType.class );
    Collections.sort( filetypes, new FileTypeBySizeComparator() );
    if( ! filetypes.isEmpty() ) {
      maxspace = filetypes.get(0).getMinSize();
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
    return filetypes.toArray( new FileType[ filetypes.size() ] );
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
    for( int i = 0; i < filetypes.size(); i++ ) {
      FileType current = filetypes.get(i);
      if( (current.getMinSize() <= data.length) && current.isOfType( data ) ) {
        return current;
      }
    }
    return null;
  }
  
  private static class FileTypeBySizeComparator implements Comparator<FileType> {

    @Override
    public int compare( FileType f1, FileType f2 ) {
      Integer i1 = Integer.valueOf( f1.getMinSize());
      Integer i2 = Integer.valueOf( f2.getMinSize() );
      return i2.compareTo(i1);
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
