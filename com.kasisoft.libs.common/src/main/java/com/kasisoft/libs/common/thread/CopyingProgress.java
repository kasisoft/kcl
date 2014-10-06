package com.kasisoft.libs.common.thread;

import com.kasisoft.libs.common.constants.*;

import lombok.*;

/**
 * Progress information for byte/character copying processes.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 * 
 * @deprecated [06-Oct-2014:KASI]   This progress instance will be dropped with 1.5+.
 */
@Deprecated
public class CopyingProgress {

  private int         total;
  private int         current;
  private Primitive   datatype;
  
  /**
   * Initialises this progress information instance.
   */
  public CopyingProgress() {
    total     = 0;
    current   = 0;
    datatype  = Primitive.PByte;
  }
  
  /**
   * Changes the current datatype of items which are being copied.
   * 
   * @param newdatatype   The new datatype of items which are being copied. Not <code>null</code>.
   */
  void setDatatype( @NonNull Primitive newdatatype ) {
    datatype = newdatatype;
  }
  
  /**
   * Returns the datatype of the items which are being copied.
   * 
   * @return   The datatype of the items which are being copied. Not <code>null</code>.
   */
  public Primitive getDatatype() {
    return datatype;
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
   * Changes the number of currently copied items.
   * 
   * @param newcurrent   The new number of currently copied items.
   */
  void setCurrent( int newcurrent ) {
    current = newcurrent;
  }
  
  /**
   * Returns the current number of currently copied items.
   * 
   * @return   The current number of currently copied items.
   */
  public int getCurrent() {
    return current;
  }
  
} /* ENDCLASS */
