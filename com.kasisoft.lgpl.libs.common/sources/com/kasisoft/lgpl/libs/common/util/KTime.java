/**
 * Name........: KTime
 * Description.: Convenience class used to represent a time information.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.util;

import com.kasisoft.lgpl.tools.diagnostic.*;

import java.text.*;

import java.util.*;

/**
 * Convenience class used to represent a time information. Initially I intended to create a 
 * descendant of java.util.Date but that would require to change the API which could be problematic 
 * in cases an instance would have been supplied to jre methods which wouldn't be aware of such API
 * differences. So this is a completely new Date class just meant as an aid.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class KTime {

  private GregorianCalendar   calendar;
  
  /**
   * Initialises this instance with the setting of the current timestamp.
   */
  public KTime() {
    calendar = new GregorianCalendar();
  }
  
  /**
   * Initialises this instance with the supplied timestamp in milliseconds.
   * 
   * @param time    The timestamp in milliseconds.
   */
  public KTime( long time ) {
    this();
    calendar.setTimeInMillis( time );
  }
  
  /**
   * Returns the hour of the day.
   * 
   * @return   The hour of the day. [ 0 .. result .. 23 ]
   */
  public int getHours() {
    return calendar.get( Calendar.HOUR_OF_DAY );
  }

  /**
   * Changes the hour of the day.
   * 
   * @param hours   The hour of the day. [ 0 .. 23 ]
   */
  public void setHours( 
    @KIRange(name="hours", min=0, max=23)   int   hours 
  ) {
    calendar.set( Calendar.HOUR_OF_DAY, hours );
  }

  /**
   * Returns the minute of the hour.
   * 
   * @return   The minute of the hour. [ 0 .. result .. 59 ]
   */
  public int getMinutes() {
    return calendar.get( Calendar.MINUTE );
  }

  /**
   * Changes the minute of the hour.
   * 
   * @param minutes   The minute of the hour. [ 0 .. 59 ]
   */
  public void setMinutes( int minutes ) {
    calendar.set( Calendar.MINUTE, minutes );
  }

  /**
   * Returns the seconds of this date.
   * 
   * @return   The seconds of this date. [ 0 .. result .. 59 ]
   */
  public int getSeconds() {
    return calendar.get( Calendar.SECOND );
  }

  /**
   * Changes the seconds of this date.
   * 
   * @param seconds   The new seconds for this date. [ 0 .. 59 ]
   */
  public void setSeconds( int seconds ) {
    calendar.set( Calendar.SECOND, seconds );
  }

  /**
   * Returns the current date in milliseconds.
   * 
   * @return   The current date in milliseconds.
   */
  public long getTime() {
    return calendar.getTimeInMillis();
  }
  
  /**
   * Changes the current date in milliseconds.
   * 
   * @param time   The new date in milliseconds.
   */
  public void setTime( long time ) {
    calendar.setTimeInMillis( time );
  }

  /**
   * Returns the current millisecond part of this date.
   * 
   * @return   The current millisecond part of this date. [ 0 .. result .. 999 ]
   */
  public int getMilliseconds() {
    return calendar.get( Calendar.MILLISECOND );
  }
  
  /**
   * Changes the current millisecond part of this date.
   * 
   * @param newmilliseconds   The new millisecond part of this date. [ 0 .. 999 ]
   */
  public void setMilliseconds( int newmilliseconds ) {
    calendar.set( Calendar.MILLISECOND, newmilliseconds );
  }
  
  /**
   * Increases the hour.
   */
  public void incHours() {
    if( getHours() == 23 ) {
      setHours(0);
    } else {
      setHours( getHours() + 1 );
    }
  }

  /**
   * Decreases the hour.
   */
  public void decHours() {
    if( getHours() == 0 ) {
      setHours(23);
    } else {
      setHours( getHours() - 1 );
    }
  }

  /**
   * Increases the minute.
   */
  public void incMinutes() {
    if( getMinutes() == 59 ) {
      incHours();
      setMinutes(0);
    } else {
      setMinutes( getMinutes() + 1 );
    }
  }

  /**
   * Decreases the minute.
   */
  public void decMinutes() {
    if( getMinutes() == 0 ) {
      decHours();
      setMinutes(59);
    } else {
      setMinutes( getMinutes() - 1 );
    }
  }

  /**
   * Increases the second.
   */
  public void incSeconds() {
    if( getSeconds() == 59 ) {
      incMinutes();
      setSeconds(0);
    } else {
      setSeconds( getSeconds() + 1 );
    }
  }

  /**
   * Decreases the second.
   */
  public void decSeconds() {
    if( getSeconds() == 0 ) {
      decMinutes();
      setSeconds(59);
    } else {
      setSeconds( getSeconds() - 1 );
    }
  }

  /**
   * Increases the millisecond.
   */
  public void incMilliseconds() {
    if( getMilliseconds() == 999 ) {
      incSeconds();
      setMilliseconds(0);
    } else {
      setMilliseconds( getMilliseconds() + 1 );
    }
  }

  /**
   * Decreases the millisecond.
   */
  public void decMilliseconds() {
    if( getMilliseconds() == 0 ) {
      decSeconds();
      setMilliseconds(999);
    } else {
      setMilliseconds( getMilliseconds() - 1 );
    }
  }

  /**
   * {@inheritDoc}
   */
  public String toString() {
    return
      StringFunctions.decFormat2( getHours() ) + ":" +
      StringFunctions.decFormat2( getMinutes() ) + ":" +
      StringFunctions.decFormat2( getSeconds() ) + " " +
      StringFunctions.decFormat3( getMilliseconds() );
  }

  /**
   * Like {@link #toString()} but this function also accepts a formatting String.
   * 
   * @param format   The formatting String to be used. This must be specified as documented in
   *                 {@link SimpleDateFormat}. Neither <code>null</code> nor empty.
   *                 
   * @return   A textual representation of this instance. Not <code>null</code>.
   */
  public String toString( @KNotEmpty(name="format") String format ) {
    SimpleDateFormat formatter = new SimpleDateFormat( format );
    return formatter.format( toDate() );
  }
  
  /**
   * Converts this time into a Calendar.
   * 
   * @return   A Calendar representation of this time.
   */
  public Calendar toCalendar() {
    Calendar result = Calendar.getInstance();
    result.setTime( calendar.getTime() );
    return( result );
  }

  /**
   * Converts this time into a jre Date.
   * 
   * @return   The jre Date representation of this time.
   */
  public Date toDate() {
    return calendar.getTime();
  }

  /**
   * {@inheritDoc}
   */
  public boolean equals( Object obj ) {
    if( obj == null ) {
      return false;
    }
    if( obj == this ) {
      return true;
    }
    if( obj instanceof KTime ) {
      KTime other = (KTime) obj;
      return
        (getHours        () == other . getHours        ()) &&
        (getMinutes      () == other . getMinutes      ()) &&
        (getSeconds      () == other . getSeconds      ()) &&
        (getMilliseconds () == other . getMilliseconds ());
    }
    return false;
  }

} /* ENDCLASS */
