package com.kasisoft.libs.common.old.io.datatypes;

import com.kasisoft.libs.common.old.io.DefaultIO;
import com.kasisoft.libs.common.old.io.IoFunctions;
import com.kasisoft.libs.common.spi.MultiSPILoader;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.NonNull;

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
    filetypes = MultiSPILoader.builder().build().loadServices( FileType.class );
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
   * Identifies the FileType for the supplied resource.
   * 
   * @param input   The input type which may be one of the following types:
   *                    <ul>
   *                        <li>String</li>
   *                        <li>Path</li>
   *                        <li>URL</li>
   *                        <li>URI</li>
   *                        <li>InputStream</li>
   *                    </ul>
   * 
   * @return   The FileType if it could be identified. Maybe <code>null</code>.
   * 
   * @throws FailureException   Accessing the resource failed for some reason.
   */
  public <T> FileType identify( @NonNull T input ) {
    Optional<byte[]> data = DefaultIO.inputstream( input ).forInputStream( input, maxspace, IoFunctions::loadFragment );
    return data.map( this::identify ).orElse( null );
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
      if( filetypes.get(i).test( data ) ) {
        return filetypes.get(i);
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
