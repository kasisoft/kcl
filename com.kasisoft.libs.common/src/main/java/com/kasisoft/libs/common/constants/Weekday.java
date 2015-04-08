package com.kasisoft.libs.common.constants;

import java.text.*;

import java.util.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Constants used to describe a weekday.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum Weekday {

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
  @SuppressWarnings("deprecation")
  public String getPresentable( @NonNull Locale locale ) {
    SimpleDateFormat formatter = new SimpleDateFormat( "EEEE", locale );
    Date             date      = new Date();
    while( date.getDay() != (jreday - 1) ) {
      date.setDate( date.getDate() + 1 );
    }
    return formatter.format( date );
  }

  /**
   * Returns a short presentable text for this weekday.
   *
   * @param locale   The Locale instance used for the internationalisation. Not <code>null</code>.
   * 
   * @return   A short presentable text for this weekday. Neither <code>null</code> nor empty.
   */
  @SuppressWarnings("deprecation")
  public String getShortPresentable( @NonNull Locale locale ) {
    SimpleDateFormat formatter = new SimpleDateFormat( "EEE", locale );
    Date             date      = new Date();
    while( date.getDay() != (jreday - 1) ) {
      date.setDate( date.getDate() + 1 );
    }
    return formatter.format( date );
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
   * @ks.note [26-Oct-2008:KASI]   If the day is taken from a Date instance you need to increase the value once since 
   *                               Sunday on Date is 0 and the corresponding constant in the JRE is 1.
   * 
   * @param day    The day to be used. Not <code>null</code>.
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

} /* ENDENUM */
