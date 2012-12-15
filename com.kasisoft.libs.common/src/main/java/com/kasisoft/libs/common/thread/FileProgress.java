/**
 * Name........: FileProgress
 * Description.: Progress information for file based Runnable implementations.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.thread;

import com.kasisoft.lgpl.tools.diagnostic.*;

import java.io.*;

/**
 * Progress information for file based Runnable implementations.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class FileProgress {
  
  private int    total;
  private int    current;
  private File   file;
  
  /**
   * Initialises this progress information instance.
   */
  public FileProgress() {
    total   = 0;
    current = 0;
    file    = null;
  }
  
  /**
   * Changes the current file.
   * 
   * @param newfile   The new file. Not <code>null</code>.
   */
  void setFile( @KNotNull(name="newfile") File newfile ) {
    file = newfile;
  }
  
  /**
   * Returns the current file.
   * 
   * @return   The current file. Not <code>null</code>.
   */
  public File getFile() {
    return file;
  }
  
  /**
   * Changes the current total for this progress information.
   * 
   * @param newtotal   The new total. If negative thte total is unknown.
   */
  void setTotal( int newtotal ) {
    total = newtotal;
  }
  
  /**
   * Returns the current total for this progress information.
   * 
   * @return   The current total for this progress information. If negative the total is unknown.
   */
  public int getTotal() {
    return total;
  }
  
  /**
   * Changes the number of currently processed items.
   * 
   * @param newcurrent   The new number of currently processed items.
   */
  void setCurrent( @KIPositive(name="newcurrent", zero=true) int newcurrent ) {
    current = newcurrent;
  }
  
  /**
   * Returns the current number of currently processed items.
   * 
   * @return   The current number of currently processed items.
   */
  public int getCurrent() {
    return current;
  }

} /* ENDCLASS */
