/**
 * Name........: KDateTime
 * Description.: Convenience class used to represent a datetime information.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.constants.*;

import java.text.*;

import java.util.*;

/**
 * Convenience class used to represent a datetime information. Initially I intended to create a  descendant of 
 * java.util.Date but that would require to change the API which could be problematic in cases an instance would have 
 * been supplied to jre methods which wouldn't be aware of such API differences. So this is a completely new Date class 
 * just meant as an aid.
 */
public class KDateTime {

  private static final SimpleDateFormat FORMATTER = new SimpleDateFormat( "dd.MM.yyyy HH:mm:ss SSS" );

  private GregorianCalendar   calendar;
  
  /**
   * Initialises this instance with the setting of the current timestamp.
   */
  public KDateTime() {
    calendar = new GregorianCalendar();
  }
  
  /**
   * Initialises this instance with the supplied timestamp in milliseconds.
   * 
   * @param time    The timestamp in milliseconds.
   */
  public KDateTime( long time ) {
    this();
    calendar.setTimeInMillis( time );
  }
  
  /**
   * Returns the day within the month. 
   * 
   * @return   The day within the month. [ 1 .. result .. 31 ]
   */
  public int getDay() {
    return calendar.get( Calendar.DAY_OF_MONTH );
  }

  /**
   * Changes the day within the month.
   * 
   * @param day   The day within the month. [ 1 .. 31 ]
   */
  public void setDay( int day ) {
    calendar.set( Calendar.DAY_OF_MONTH, day );
  }

  /**
   * Returns the weekday for this date.
   * 
   * @return   The weekday for this date. Not <code>null</code>.
   */
  public Weekday getWeekday() {
    return Weekday.valueOf( calendar.get( Calendar.DAY_OF_WEEK ) );
  }


  /**
   * Returns the month of this date instance.
   * 
   * @return   The month of this date instance. Not <code>null</code>.
   */
  public Month getMonth() {
    return Month.valueOf( calendar.get( Calendar.MONTH ) );
  }
  
  /**
   * Changes the month for this date instance.
   * 
   * @param month   The new month which has to be set. Not <code>null</code>.
   */
  public void setMonth( Month month ) {
    calendar.set( Calendar.MONTH, month.getJreMonth() );
  }

  /**
   * Returns the year of this date.
   * 
   * @return   The year of this date. [ 1800 .. result .. * ]
   */
  public int getYear() {
    return calendar.get( Calendar.YEAR );
  }

  /**
   * Changes the year of this date.
   * 
   * @paran year   The new year of this date. [ 1800 .. * ] 
   */
  public void setYear( int year ) {
    calendar.set( Calendar.YEAR, year );
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
  public void setHours( int hours ) {
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
   * Converts this date into a Calendar.
   * 
   * @return   A Calendar representation of this date.
   */
  public Calendar toCalendar() {
    Calendar result = Calendar.getInstance();
    result.setTime( calendar.getTime() );
    return result;
  }

  /**
   * Converts this date into a jre Date.
   * 
   * @return   The jre Date representation of this date.
   */
  public Date toDate() {
    return calendar.getTime();
  }
  
  /**
   * Returns <code>true</code> if this date is a leap year.
   * 
   * @return   <code>true</code> <=> This date is a leap year.
   */
  public boolean isLeapYear() {
    return calendar.isLeapYear( getYear() );
  }
  
  /**
   * Increases the year.
   */
  public void incYear() {
    setYear( getYear() + 1 );
  }

  /**
   * Decreases the year.
   */
  public void decYear() {
    setYear( getYear() - 1 );
  }

  /**
   * Increases the month.
   */
  public void incMonth() {
    if( getMonth() == Month.December ) {
      incYear();
    }
    setMonth( getMonth().next() );
  }

  /**
   * Decreases the month.
   */
  public void decMonth() {
    if( getMonth() == Month.January ) {
      decYear();
    }
    setMonth( getMonth().previous() );
  }

  /**
   * Increases the day.
   */
  public void incDay() {
    if( getDay() == getMonth().getDayCount( getYear() ) ) {
      incMonth();
      setDay(1);
    } else {
      setDay( getDay() + 1 );
    }
  }

  /**
   * Decreases the day.
   */
  public void decDay() {
    if( getDay() == 1 ) {
      decMonth();
      setDay( getMonth().getDayCount( getYear() ) );
    } else {
      setDay( getDay() - 1 );
    }
  }

  /**
   * Increases the hour.
   */
  public void incHours() {
    if( getHours() == 23 ) {
      incDay();
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
      decDay();
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
   * Returns the number of days for the currently set Month.
   * 
   * @return   The number of days for the currently set Month.
   */
  public int getDayCount() {
    return getMonth().getDayCount( getYear() );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return FORMATTER.format( toDate() );
  }

  /**
   * Like {@link #toString()} but this function also accepts a formatting String.
   * 
   * @param format   The formatting String to be used. This must be specified as documented in
   *                 {@link SimpleDateFormat}. Neither <code>null</code> nor empty.
   *                 
   * @return   A textual representation of this instance. Not <code>null</code>.
   */
  public String toString( String format ) {
    SimpleDateFormat formatter = new SimpleDateFormat( format );
    return formatter.format( toDate() );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object obj ) {
    if( obj == null ) {
      return false;
    }
    if( obj == this ) {
      return true;
    }
    if( obj instanceof KDateTime ) {
      KDateTime other = (KDateTime) obj;
      return
        (getDay          () == other . getDay          ()) &&
        (getMonth        () == other . getMonth        ()) &&
        (getYear         () == other . getYear         ()) &&
        (getHours        () == other . getHours        ()) &&
        (getMinutes      () == other . getMinutes      ()) &&
        (getSeconds      () == other . getSeconds      ()) &&
        (getMilliseconds () == other . getMilliseconds ());
    }
    return false;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return calendar.hashCode();
  }

} /* ENDCLASS */
