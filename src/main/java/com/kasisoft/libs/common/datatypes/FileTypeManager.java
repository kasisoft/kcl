package com.kasisoft.libs.common.datatypes;

import com.kasisoft.libs.common.io.*;

import javax.validation.constraints.*;

import java.util.*;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

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
    filetypes = new ArrayList<FileType>();
    ServiceLoader.load(FileType.class).forEach(filetypes::add);
    Collections.sort(filetypes, new FileTypeBySizeComparator());
    if (!filetypes.isEmpty()) {
      maxspace = filetypes.get(0).getMinSize();
    } else {
      maxspace = 0;
    }
  }
  
  /**
   * Returns a list with all known FileType instances.
   * 
   * @return   A list with all known FileType instances.
   */
  public @NotNull FileType[] getFileTypes() {
    return filetypes.toArray(new FileType[filetypes.size()]);
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
   * @return   The FileType if it could be identified.
   */
  public <T> FileType identify(@NotNull T input) {
    Optional<byte[]> data = IoFunctions.genericLoadBytes(input, maxspace);
    return data.map(this::identify).orElse(null);
  }

  /**
   * Identifies the FileType for the supplied data.
   * 
   * @param data   The data of the input which type shall be identified.
   * 
   * @return   The FileType if it could be identified.
   */
  public FileType identify(@NotNull byte[] data) {
    for (var i = 0; i < filetypes.size(); i++ ) {
      if (filetypes.get(i).test(data)) {
        return filetypes.get(i);
      }
    }
    return null;
  }
  
  private static class FileTypeBySizeComparator implements Comparator<FileType> {

    @Override
    public int compare(FileType f1, FileType f2) {
      var i1 = Integer.valueOf(f1.getMinSize());
      var i2 = Integer.valueOf(f2.getMinSize());
      return i2.compareTo(i1);
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
