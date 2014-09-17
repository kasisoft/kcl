package com.kasisoft.libs.common.constants;

import lombok.*;

/**
 * Collection of time units.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
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
   * Returns the amount of milliseconds for a specific number of timeunits.
   * 
   * @param number   The number of timeunits to be specified.
   * 
   * @return   The amount of milliseconds representing that amount.
   */
  public long amount( int number ) {
    return number * milliseconds;
  }
  
  /**
   * Converts an amount of units into it's corresponding value for another unit.
   * 
   * @param num     The amount of units to convert.
   * @param other   The other unit which amount is desired. Must be smaller than the actual one. Not <code>null</code>.
   * 
   * @return   The corresponding amount in another unit. -1 in case the supplied unit isn't smaller.
   */
  public int convert( int num, @NonNull TimeUnit other ) {
    if( other.milliseconds < milliseconds ) {
      int ratio = (int) (milliseconds / other.milliseconds);
      return num * ratio;
    }
    return -1;
  }
  
} /* ENDENUM */
