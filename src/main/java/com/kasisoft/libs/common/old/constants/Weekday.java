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
 * Constants used to describe a weekday.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum Weekday implements TemporalValue {

  Monday    ( Calendar.MONDAY    ) ,
  Tuesday   ( Calendar.TUESDAY   ) ,
  Wednesday ( Calendar.WEDNESDAY ) ,
  Thursday  ( Calendar.THURSDAY  ) ,
  Friday    ( Calendar.FRIDAY    ) , 
  Saturday  ( Calendar.SATURDAY  ) ,
  Sunday    ( Calendar.SUNDAY    ) ;
  
  int              jreday;
  
  /** Neither <code>null</code> nor empty. */
  @Getter String   presentable;
  
  /** Neither <code>null</code> nor empty. */
  @Getter String   shortPresentable;
  
  /**
   * Sets up this constant with the supplied index within a week.
   * 
   * @param jre   The constant of the JRE used to identify a weekday. 
   */
  Weekday( int jre ) {
    jreday           = jre;
    presentable      = getPresentable( Locale.getDefault() );
    shortPresentable = getShortPresentable( Locale.getDefault() );
  }
  
  /**
   * Returns a long presentable text for this weekday.
   *
   * @param locale   The Locale instance used for the internationalisation. Not <code>null</code>.
   * 
   * @return   A long presentable text for this weekday. Neither <code>null</code> nor empty.
   */
  public String getPresentable( @NonNull Locale locale ) {
    return getPresentable( locale, "EEEE", createDate() );
  }

  /**
   * Returns a short presentable text for this weekday.
   *
   * @param locale   The Locale instance used for the internationalisation. Not <code>null</code>.
   * 
   * @return   A short presentable text for this weekday. Neither <code>null</code> nor empty.
   */
  public String getShortPresentable( @NonNull Locale locale ) {
    return getPresentable( locale, "EEE", createDate() );
  }
  
  @SuppressWarnings("deprecation")
  private Date createDate() {
    val result = new Date();
    while( result.getDay() != (jreday - 1) ) {
      result.setDate( result.getDate() + 1 );
    }
    return result;
  }

  /**
   * Returns the Weekday which follows this one.
   * 
   * @return   The Weekday which follows this one. Not <code>null</code>.
   */
  public Weekday next() {
    switch( this ) {
    case Monday          : return Tuesday;
    case Tuesday         : return Wednesday;
    case Wednesday       : return Thursday;
    case Thursday        : return Friday;
    case Friday          : return Saturday;
    case Saturday        : return Sunday;
    default /* Sunday */ : return Monday;
    }
  }

  /**
   * Returns the Weekday which comes before this one.
   * 
   * @return   The Weekday which comes before this one. Not <code>null</code>.
   */
  public Weekday previous() {
    switch( this ) {
    case Monday          : return Sunday;
    case Tuesday         : return Monday;
    case Wednesday       : return Tuesday;
    case Thursday        : return Wednesday;
    case Friday          : return Thursday;
    case Saturday        : return Friday;
    default /* Sunday */ : return Saturday;
    }
  }

  /**
   * Returns the weekday constant for the supplied Date.
   * 
   * @param date    The date to be used. Not <code>null</code>.
   * 
   * @return   The weekday for the supplied date. <code>null</code> in case the supplied date wasn't valid.
   */
  @SuppressWarnings("deprecation")
  public static Weekday valueOf( @NonNull Date date ) {
    int day = date.getDay() + 1;
    for( Weekday weekday : Weekday.values() ) {
      if( weekday.jreday == day ) {
        return weekday;
      }
    } 
    return null;
  }

  /**
   * Returns the weekday constant for the supplied day as specified
   * in java.util.Calendar .
   * 
   * @param day   The day to be used. Not <code>null</code>.
   * 
   * @return   The weekday for the supplied date. <code>null</code> in case the supplied day wasn't valid.
   */
  public static Weekday valueOf( int day ) {
    for( Weekday weekday : Weekday.values() ) {
      if( weekday.jreday == day ) {
        return weekday;
      }
    } 
    return null;
  }

  /**
   * Returns <code>true</code> if the supplied date is a weekend.
   * 
   * @param date   The date that is supposed to be tested. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The supplied Date is a weekend.
   */
  public static boolean isWeekend( @NonNull Date date ) {
    Weekday value = valueOf( date );
    return (value == Saturday) || (value == Sunday);
  }
  
} /* ENDENUM */
