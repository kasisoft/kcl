package com.kasisoft.libs.common.thread;

import java.io.*;

import lombok.*;

/**
 * Progress information for file based Runnable implementations.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
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
  void setFile( @NonNull File newfile ) {
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
  void setCurrent( int newcurrent ) {
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
