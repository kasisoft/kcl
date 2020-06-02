package com.kasisoft.libs.common.old.constants;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;

/**
 * Values to identify a month.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum Month implements TemporalValue {

  January     ( Calendar.JANUARY   , 31 ),
  February    ( Calendar.FEBRUARY  , 28 ),
  March       ( Calendar.MARCH     , 31 ),
  April       ( Calendar.APRIL     , 30 ),
  May         ( Calendar.MAY       , 31 ),
  June        ( Calendar.JUNE      , 30 ),
  July        ( Calendar.JULY      , 31 ),
  August      ( Calendar.AUGUST    , 31 ),
  September   ( Calendar.SEPTEMBER , 30 ),
  October     ( Calendar.OCTOBER   , 31 ),
  November    ( Calendar.NOVEMBER  , 30 ),
  December    ( Calendar.DECEMBER  , 31 );
  
  @SuppressWarnings("deprecation")
  static final int CURRENTYEAR = (new Date()).getYear() + 1900;
  
          int      daycount;
  
  /** @see java.util.Calendar */
  @Getter int      jreMonth;
  
  /** Neither <code>null</code> nor empty. */
  @Getter String   presentable;
  
  /** Neither <code>null</code> nor empty. */
  @Getter String   shortPresentable;
  
  /**
   * Initialises this constant.
   * 
   * @param jre     The month constant within the JRE.
   * @param days    The number of days.
   */
  Month( int jre, int days ) {
    jreMonth         = jre;
    daycount         = days;
    presentable      = getPresentable( Locale.getDefault() );
    shortPresentable = getShortPresentable( Locale.getDefault() );
  }
  
  /**
   * Sets this month to the supplied date.
   * 
   * @param date   The date which is supposed to be updated. Not <code>null</code>.
   */
  @SuppressWarnings("deprecation")
  public void set( @NonNull Date date ) {
    date.setMonth( jreMonth );
  }
  
  /**
   * Returns a long presentable text for this month.
   *
   * @param locale   The Locale instance used for the internationalisation. Not <code>null</code>.
   * 
   * @return   A long presentable text for this month. Neither <code>null</code> nor empty.
   */
  public String getPresentable( @NonNull Locale locale ) {
    return getPresentable( locale, "MMMM", createDate() );
  }

  /**
   * Returns a short presentable text for this month.
   *
   * @param locale   The Locale instance used for the internationalisation. Not <code>null</code>.
   * 
   * @return   A short presentable text for this month. Neither <code>null</code> nor empty.
   */
  public String getShortPresentable( @NonNull Locale locale ) {
    return getPresentable( locale, "MMM", createDate() );
  }

  @SuppressWarnings("deprecation")
  private Date createDate() {
    val result = new Date();
    result.setMonth( jreMonth );
    return result;
  }
  
  /**
   * Returns the numbering within a year. January = 1, February = 2 and so on.
   * 
   * @return   The numbering within a year. [ 1 .. result .. 12 ]
   */
  public int getNumber() {
    return jreMonth + 1;
  }
  
  /**
   * Returns <code>true</code> if the supplied date corresponds to this month.
   * 
   * @param date   The date that is supposed to be tested. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The supplied date corresponds to this month.
   */
  public boolean isInMonth( @NonNull Date date ) {
    return valueOf( date ) == this;
  }
  
  /**
   * Returns the Month which follows this one.
   * 
   * @return   The Month which follows this one. Not <code>null</code>.
   */
  public Month next() {
    switch( this ) {
    case January           : return February;
    case February          : return March;
    case March             : return April;
    case April             : return May;
    case May               : return June;
    case June              : return July;
    case July              : return August;
    case August            : return September;
    case September         : return October;
    case October           : return November;
    case November          : return December;
    default /* December */ : return January;
    }
  }

  /**
   * Returns the Month which comes before this one.
   * 
   * @return   The Month which comes before this one. Not <code>null</code>.
   */
  public Month previous() {
    switch( this ) {
    case January           : return December;
    case February          : return January;
    case March             : return February;
    case April             : return March;
    case May               : return April;
    case June              : return May;
    case July              : return June;
    case August            : return July;
    case September         : return August;
    case October           : return September;
    case November          : return October;
    default /* December */ : return November;
    }
  }

  /**
   * Returns the number of days for this month. To calculate the number of days for {@link #February} the 
   * current year is used.
   * 
   * @return   The number of days for this month. [ 1 .. result .. 31 ]
   */
  public int getDayCount() {
    return getDayCount( CURRENTYEAR );
  }
  
  /**
   * Returns the number of days for this month using the supplied year.
   * 
   * @param year   The year which is used for the calculation. [ 1800 .. year .. 2200 ]
   * 
   * @return   The number of days for this month. [ 1 .. result .. 31 ]
   */
  public int getDayCount( int year ) {
    if( this != February ) {
      return daycount;
    }
    // we need to calculate the count for this year
    if( year % 400 == 0 ) {
      return daycount + 1;
    } else if( year % 100 == 0 ) {
      return daycount;
    } else if( year % 4 == 0 ) {
      return daycount + 1;
    } else {
      return daycount;
    }
  }
  
  /**
   * Returns the first weekday for the month of the current year.
   * 
   * @return   The first weekday of the month. Not <code>null</code>.
   */
  public Weekday getFirstWeekday() {
    return getFirstWeekday( CURRENTYEAR );
  }
  
  /**
   * Returns the first weekday for this month within a specified year.
   * 
   * @param year   The year which first weekday is desired. [ 1800 .. year .. 2200 ]
   * 
   * @return   The first weekday of the month. Not <code>null</code>.
   */
  @SuppressWarnings("deprecation")
  public Weekday getFirstWeekday( int year ) {
    return Weekday.valueOf( new Date( year - 1900, jreMonth, 1 ) );
  }
  
  /**
   * Returns the weekday of a specific day.
   * 
   * @param day   The day within the month. [ 1 .. getDayCount ]
   * 
   * @return   The weekday of the specified day. Not <code>null</code>.
   */
  public Weekday getWeekay( int day ) {
    return getWeekday( CURRENTYEAR, day );
  }

  /**
   * Returns the weekday of a specific day.
   * 
   * @param year   The year we're currently using. [ 1800 .. year .. 2200 ]
   * @param day    The day within the month. [ 1 .. getDayCount ]
   * 
   * @return   The weekday of the specified day. Not <code>null</code>.
   */
  @SuppressWarnings("deprecation")
  public Weekday getWeekday( int year, int day ) {
    return Weekday.valueOf( new Date( year - 1900, jreMonth, day ) );
  }
  
  /**
   * Returns the month constant for the supplied Date.
   * 
   * @param date   The date to be used. Not <code>null</code>.
   * 
   * @return   The month for the supplied date. 
   */
  @SuppressWarnings("deprecation")
  public static Month valueOf( @NonNull Date date ) {
    return valueOf( date.getMonth() );
  }

  /**
   * Returns the month constant for the supplied month declared as a Calendar constant.
   * 
   * @param jremonth   The constant used within Calendar.
   * 
   * @return   The month for the supplied jre month constant. 
   */
  public static Month valueOf( int jremonth ) {
    for( val month : Month.values() ) {
      if( month.jreMonth == jremonth ) {
        return month;
      }
    } 
    return null;
  }
  
  /**
   * Returns the current month which is the month present while calling this function.
   * 
   * @return   The current month which is the month present while calling this function. Not <code>null</code>.
   */
  public static Month getCurrent() {
    return valueOf( new Date() );
  }

} /* ENDENUM */