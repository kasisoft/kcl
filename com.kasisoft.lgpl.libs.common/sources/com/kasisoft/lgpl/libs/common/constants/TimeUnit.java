/**
 * Name........: TimeUnit
 * Description.: Collection of time units. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.constants;

/**
 * Collection of time units.
 */
public enum TimeUnit {

  Millisecond  (                   1 ),
  Second       (                1000 ),
  Minute       (           60 * 1000 ),
  Hour         (      60 * 60 * 1000 ),
  Day          ( 24 * 60 * 60 * 1000 );
  
  private long milliseconds;
  
  TimeUnit( long millis ) {
    milliseconds = millis;
  }
  
  /**
   * Returns the number of milliseconds used to represent this time unit.
   * 
   * @return   The number of milliseconds used to represent this time unit.
   */
  public long getMilliseconds() {
    return milliseconds;
  }
  
  /**
   * Converts an amount of units into it's corresponding value for another unit.
   * 
   * @param num     The amount of units to convert.
   * @param other   The other unit which amount is desired. Must be smaller than the actual one.
   * 
   * @return   The corresponding amount in another unit. -1 in case the supplied unit isn't smaller.
   */
  public int convert( int num, TimeUnit other ) {
    if( other.milliseconds < milliseconds ) {
      int ratio = (int) (milliseconds / other.milliseconds);
      return num * ratio;
    }
    return -1;
  }
  
} /* ENDENUM */
