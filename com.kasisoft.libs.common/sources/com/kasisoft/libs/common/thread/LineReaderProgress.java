/**
 * Name........: LineReaderProgress
 * Description.: Progress information for the line reading Runnable. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.thread;

import com.kasisoft.lgpl.tools.diagnostic.*;

/**
 * Progress information for the line reading Runnable.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class LineReaderProgress {

  private int   total;
  private int   current;
  
  /**
   * Initialises this progress information instance.
   */
  public LineReaderProgress() {
    total   = 0;
    current = 0;
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
   * Changes the number of currently copied lines.
   * 
   * @param newcurrent   The new number of currently copied lines.
   */
  void setCurrent( @KIPositive(name="newcurrent", zero=true) int newcurrent ) {
    current = newcurrent;
  }
  
  /**
   * Returns the current number of currently copied lines.
   * 
   * @return   The current number of currently copied lines.
   */
  public int getCurrent() {
    return current;
  }
  
} /* ENDCLASS */
