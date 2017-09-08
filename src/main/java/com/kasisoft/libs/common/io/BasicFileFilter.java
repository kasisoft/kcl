package com.kasisoft.libs.common.io;

import com.kasisoft.libs.common.sys.*;

import lombok.experimental.*;

import lombok.*;

import java.io.*;

/**
 * FileFilter implementation which is capable to be used with the standard File class as well as with the JFileChooser 
 * implementation.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BasicFileFilter extends javax.swing.filechooser.FileFilter implements FileFilter {

  String   mainsuffix;
  String   description;

  /**
   * Initialises this filter with the supplied suffix.
   * 
   * @param suffix   The suffix to be used with this filter. Neither <code>null</code> nor empty.
   */
  public BasicFileFilter( String suffix ) {
    this( suffix, null );
  }

  /**
   * Initialises this filter with the supplied suffix and a description.
   * 
   * @param suffix              The suffix to be used with this filter. Neither <code>null</code> nor empty.
   * @param filterdescription   The description which will be displayed. Maybe <code>null</code>.
   */
  public BasicFileFilter( @NonNull String suffix, String filterdescription ) {
    mainsuffix  = validateSuffix( suffix );
    description = filterdescription;
  }

  /**
   * Makes sure that the returned suffix starts with a dot. On operating systems without case 
   * sensitive filesystems the suffix is also lower cased.
   * 
   * @param suffix   The suffix which possibly requires some changes. 
   *                 Neither <code>null</code> nor empty.
   * 
   * @return   The suffix if it was correct already or an adjusted pendant. 
   *           Neither <code>null</code> nor empty.
   */
  protected String validateSuffix( @NonNull String suffix ) {
    if( suffix.charAt(0) != '.' ) {
      suffix = String.format( ".%s", suffix );
    }
    if( ! SystemInfo.getRunningOS().isCaseSensitiveFS() ) {
      suffix = suffix.toLowerCase();
    }
    return suffix;
  }

  /**
   * Help function which assures that the supplied File will have the suffix used with this filter.
   * 
   * @param file   The File which must be fixed if necessary. Not <code>null</code>.
   * 
   * @return   The supplied File or a changed one if it was necessary.
   */
  public File fixFile( @NonNull File file ) {
    String name = file.getName();
    if( name.endsWith( mainsuffix ) ) {
      // the name is already valid
      return file;
    }
    String fixedname = String.format( "%s%s", name, mainsuffix );
    if( file.getParentFile() != null ) {
      return new File( file.getParentFile(), fixedname );
    } else {
      return new File( fixedname );
    }
  }

  @Override
  public boolean accept( @NonNull File file ) {
    if( ! file.isFile() ) {
      return true;
    }
    String filename = normaliseFilename( file.getName() );
    return filename.endsWith( mainsuffix );
  }

  /**
   * Returns a normalised filename allowing to be used depending on the currently running operating system mainly useful 
   * to handle case sensitivity.
   * 
   * @param filename   The filename to be altered if necessary. Neither <code>null</code> nor empty.
   * 
   * @return   The normalised filename. Neither <code>null</code> nor empty.
   */
  protected String normaliseFilename( @NonNull String filename ) {
    if( SystemInfo.getRunningOS().isCaseSensitiveFS() ) {
      return filename;
    } else {
      return filename.toLowerCase();
    }
  }
  
  /**
   * Returns a comma separated list of all suffices.
   * 
   * @return   A comma separated list of all suffixes. Neither <code>null</code> nor empty.
   */
  protected String getSuffixList() {
    return mainsuffix;
  }

  @Override
  public String getDescription() {
    if( description == null ) {
      return getSuffixList();
    }
    return String.format( "%s (%s)", description, getSuffixList() );
  }

} /* ENDCLASS */