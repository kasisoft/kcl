/**
 * Name........: KDate
 * Description.: Convenience class used to represent a date information.
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
 * Convenience class used to represent a date information. Initially I intended to create a descendant of java.util.Date 
 * but that would require to change the API which could be problematic in cases an instance would have been supplied to 
 * jre methods which wouldn't be aware of such API differences. So this is a completely new Date class just meant as an 
 * aid.
 */
public class KDate {

  private GregorianCalendar   calendar;
  private SimpleDateFormat    formatter;
  
  /**
   * Initialises this instance with the setting of the current timestamp.
   */
  public KDate() {
    formatter = new SimpleDateFormat( "dd.MM.yyyy" );
    calendar  = new GregorianCalendar();
  }
  
  /**
   * Initialises this instance with the supplied timestamp in milliseconds.
   * 
   * @param time    The timestamp in milliseconds.
   */
  public KDate( long time ) {
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
    return formatter.format( toDate() );
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
    if( obj instanceof KDate ) {
      KDate other = (KDate) obj;
      return
        (getDay   () == other . getDay   ()) &&
        (getMonth () == other . getMonth ()) &&
        (getYear  () == other . getYear  ());
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
