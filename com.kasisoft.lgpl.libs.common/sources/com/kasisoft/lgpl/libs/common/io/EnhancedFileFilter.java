/**
 * Name........: EnhancedFileFilter
 * Description.: FileFilter implementation which is capable to be used with the standard
 *               File class as well as with the JFileChooser implementation. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.io;

import com.kasisoft.lgpl.tools.diagnostic.*;

import java.util.*;

import java.io.*;

/**
 * FileFilter implementation which is capable to be used with the standard
 * File class as well as with the JFileChooser implementation.
 */
public class EnhancedFileFilter extends BasicFileFilter {

  
  private static final String[] DEFAULT_EXCLUDEDDIRS = new String[] {
    ".svn", "_svn", "CVS"
  };
  
  private static final String[] DEFAULT_EXCLUDEDFILES = new String[] {
    
  };

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
    suffixlist    = new ArrayList<String>();
    excludeddirs  = new HashSet<String>();
    excludedfiles = new HashSet<String>();
  }

  /**
   * Initialises this filter with the supplied suffix and a description.
   * 
   * @param suffix              The suffix to be used with this filter. Neither <code>null</code> nor empty.
   * @param filterdescription   The description which will be displayed.
   */
  public EnhancedFileFilter( String suffix, String filterdescription ) {
    super( suffix, filterdescription );
    suffixlist    = new ArrayList<String>();
    excludeddirs  = new HashSet<String>();
    excludedfiles = new HashSet<String>();
  }
  
  /**
   * Adds the supplied directory to the exclusion list.
   * 
   * @param dirname   The name of a directory which shall be added to the exclusion list.
   *                  Neither <code>null</code> nor empty.
   */
  public void addExcludedDir( @KNotEmpty(name="dirname") String dirname ) {
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
  public void addExcludedFile( @KNotEmpty(name="filename") String filename ) {
    synchronized( excludedfiles ) {
      excludedfiles.add( normaliseFilename( filename ) );
    }
  }

  /**
   * Sets up this filter to be initialised with default exclusions.
   */
  public void addDefaultExcludes() {
    for( String dirname : DEFAULT_EXCLUDEDDIRS ) {
      addExcludedDir( dirname );
    }
    for( String filename : DEFAULT_EXCLUDEDFILES ) {
      addExcludedFile( filename );
    }
  }
  
  /**
   * Adds an additional supported suffix to this filter.
   * 
   * @param suffix   An additionally supported suffix. Neither <code>null</code> nor empty.
   */
  public void addSuffix( 
    @KNotEmpty(name="suffix")   String   suffix 
  ) {
    synchronized( suffixlist ) {
      suffixlist.add( validateSuffix( suffix ) );
    }
  }

  /**
   * {@inheritDoc}
   */
  protected String getSuffixList() {
    synchronized( suffixlist ) {
      String result = super.getSuffixList();
      for( int i = 0; i < suffixlist.size(); i++ ) {
        result += ", " + suffixlist.get(i);
      }
      return result;
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
  public boolean isExcluded( @KNotNull(name="file") File file ) {
    if( file.isDirectory() ) {
      return excludeddirs.contains( normaliseFilename( file.getName() ) );
    } else if( file.isFile() ) {
      return excludedfiles.contains( normaliseFilename( file.getName() ) );
    } else {
      // only happens when explicitly called, so use a default value here
      return false;
    }
  }
  
  /**
   * {@inheritDoc}
   */
  public boolean accept( File file ) {
    if( isExcluded( file ) ) {
      return false;
    }
    boolean result = super.accept( file );
    if( result ) {
      String  filename  = super.normaliseFilename( file.getName() );
      synchronized( suffixlist ) {
        for( int i = 0; (i < suffixlist.size()) && (! result); i++ ) {
          result = filename.endsWith( suffixlist.get(i) );
        }
      }
    }
    return result;
  }

} /* ENDCLASS */