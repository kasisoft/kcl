/**
 * Name........: EnhancedFileFilter
 * Description.: FileFilter implementation which is capable to be used with the standard File class as well as with the 
 *               JFileChooser implementation. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.io;

import java.util.*;

import java.io.*;

/**
 * FileFilter implementation which is capable to be used with the standard File class as well as with the JFileChooser 
 * implementation.
 */
public class EnhancedFileFilter extends BasicFileFilter {
  
  private static final String[] DEFAULT_EXCLUDEDDIRS = new String[] {
    ".git", ".svn", "_svn", "CVS"
  };
  
  private static final String[] DEFAULT_EXCLUDEDFILES = new String[] {
  };

  private String        suffixlistasstring;
  private List<String>  suffixlist;
  private Set<String>   excludeddirs;
  private Set<String>   excludedfiles;
  
  /**
   * Initialises this filter with the supplied suffix.
   * 
   * @param suffix   The suffix to be used with this filter. Neither <code>null</code> nor empty.
   */
  public EnhancedFileFilter( String suffix ) {
    super( suffix );
    suffixlist          = new ArrayList<String>();
    excludeddirs        = new HashSet<String>();
    excludedfiles       = new HashSet<String>();
    suffixlistasstring  = null;
  }

  /**
   * Initialises this filter with the supplied suffix and a description.
   * 
   * @param suffix              The suffix to be used with this filter. Neither <code>null</code> nor empty.
   * @param filterdescription   The description which will be displayed.
   */
  public EnhancedFileFilter( String suffix, String filterdescription ) {
    super( suffix, filterdescription );
    suffixlist          = new ArrayList<String>();
    excludeddirs        = new HashSet<String>();
    excludedfiles       = new HashSet<String>();
    suffixlistasstring  = null;
  }
  
  /**
   * Adds the supplied directory to the exclusion list.
   * 
   * @param dirname   The name of a directory which shall be added to the exclusion list. 
   *                  Neither <code>null</code> nor empty.
   */
  public void addExcludedDir( String dirname ) {
    synchronized( excludeddirs ) {
      excludeddirs.add( normaliseFilename( dirname ) );
    }
  }
  
  /**
   * Adds the supplied filename to the exclusion list.
   * 
   * @param filename   The name of the file which shall be added to the exclusion list.
   *                   Neither <code>null</code> nor empty.
   */
  public void addExcludedFile( String filename ) {
    synchronized( excludedfiles ) {
      excludedfiles.add( normaliseFilename( filename ) );
    }
  }

  /**
   * Sets up this filter to be initialised with default exclusions.
   */
  public void addDefaultExcludes() {
    synchronized( excludeddirs ) {
      for( String dirname : DEFAULT_EXCLUDEDDIRS ) {
        addExcludedDir( dirname );
      }
    }
    synchronized( excludedfiles ) {
      for( String filename : DEFAULT_EXCLUDEDFILES ) {
        addExcludedFile( filename );
      }
    }
  }
  
  /**
   * Adds an additional supported suffix to this filter.
   * 
   * @param suffix   An additionally supported suffix. Neither <code>null</code> nor empty.
   */
  public void addSuffix( String suffix ) {
    synchronized( suffixlist ) {
      suffixlistasstring  = null;
      suffixlist.add( validateSuffix( suffix ) );
    }
  }

  @Override
  protected String getSuffixList() {
    synchronized( suffixlist ) {
      if( suffixlistasstring == null ) {
        StringBuilder buffer = new StringBuilder( super.getSuffixList() );
        for( String suffix : suffixlist ) {
          buffer.append( ", " );
          buffer.append( suffix );
        }
        suffixlistasstring = buffer.toString();
      }
      return suffixlistasstring;
    }
  }
  
  /**
   * Returns <code>true</code> if the supplied file has to be excluded from the filtering process 
   * and thus will not be returned as a possible result.
   * 
   * @param file   The file which has to be tested. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The supplied file has to be excluded from the filtering process.
   */
  public boolean isExcluded( File file ) {
    if( file.isDirectory() ) {
      return excludeddirs.contains( normaliseFilename( file.getName() ) );
    } else if( file.isFile() ) {
      return excludedfiles.contains( normaliseFilename( file.getName() ) );
    } else {
      // only happens when explicitly called, so use a default value here
      return false;
    }
  }
  
  @Override
  public boolean accept( File file ) {
    if( isExcluded( file ) ) {
      return false;
    }
    boolean result = super.accept( file );
    if( result ) {
      String filename = super.normaliseFilename( file.getName() );
      synchronized( suffixlist ) {
        for( int i = 0; (i < suffixlist.size()) && (! result); i++ ) {
          result = filename.endsWith( suffixlist.get(i) );
        }
      }
    }
    return result;
  }

} /* ENDCLASS */