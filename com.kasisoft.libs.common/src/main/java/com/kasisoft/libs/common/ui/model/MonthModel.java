package com.kasisoft.libs.common.ui.model;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.i18n.*;

import lombok.experimental.*;

import lombok.*;

import javax.swing.table.*;

import java.util.*;

/**
 * A model implementation which provides the data for a single month.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MonthModel extends DefaultTableModel {

  private static final int TITLEROW     = 0;
  private static final int WEEKDAYCOUNT = 7;

  @Getter
  Month       month;
  
  @Getter
  int         year;
  
  Locale      locale;
  Weekday[]   weekdays;
  
  public MonthModel() {
    this( null, null, null );
  }
  
  @SuppressWarnings("deprecation")
  public MonthModel( Month month, Integer year, Locale locale ) {
    for( int i = 0; i < 7; i++ ) {
      addColumn( null );
    }
    if( month == null ) {
      month = Month.getCurrent();
    }
    this.locale = I18NFunctions.getLocale( locale ); 
    this.month  = month;
    this.year   = year != null ? year.intValue() : (new Date().getYear() + 1900);
    weekdays    = new Weekday[ WEEKDAYCOUNT ];
    setFirstWeekday( Weekday.Monday );
    setDate( month, this.year );
  }
  
  /**
   * Changes the first weekday which has to be displayed.
   * 
   * @param newfirstweekday   The new first weekday which has to be displayed. Not <code>null</code>.
   */
  public void setFirstWeekday( @NonNull Weekday newfirstweekday ) {
    if( newfirstweekday != weekdays[0] ) {
      for( int i = 0; i < weekdays.length; i++ ) {
        weekdays[i]     = newfirstweekday;
        newfirstweekday = newfirstweekday.next();
      }
      recalculate();
    }
  }
  
  /**
   * Returns the current first weekday.
   * 
   * @return   The current first weekday. Not <code>null</code>.
   */
  public Weekday getFirstWeekday() {
    return weekdays[0];
  }
  
  /**
   * Changes the month for this model.
   * 
   * @param newmonth   The new month. Not <code>null</code>.
   */
  public void setMonth( @NonNull Month newmonth ) {
    if( month != newmonth ) {
      month = newmonth;
      recalculate();
    }
  }

  /**
   * Changes the year for this model.
   * 
   * @param newyear   The new year.
   */
  public void setYear( int newyear ) {
    if( year != newyear ) {
      year = newyear;
      recalculate();
    }
  }
  
  /**
   * Changes the date for this model.
   * 
   * @param newmonth   The new month. Not <code>null</code>.
   * @param newyear    The new year.
   */
  public void setDate( @NonNull Month newmonth, int newyear ) {
    if( (year != newyear) || (month != newmonth) ) {
      month = newmonth;
      year  = newyear;
      recalculate();
    }
  }

  /**
   * Changes the date for this model.
   * 
   * @param date   The date month referring to month and year. Not <code>null</code>.
   */
  @SuppressWarnings("deprecation")
  public void setDate( @NonNull Date date ) {
    setDate( Month.valueOf( date ), date.getYear() + 1900 );
  }
  
  @Override
  public boolean isCellEditable( int row, int column ) {
    return false;
  }

  /**
   * Returns the date for a specific cell within the table model. In case of a valid cell
   * position the result is not <code>null</code>.
   * 
   * @param row      The row used to access the cell.
   * @param column   The column used to access the cell.
   * 
   * @return   The date at that location. <code>null</code> if <code>column</code> or <code>row</code>
   *           don't specify valid values.
   */
  public Date dateValueAt( int row, int column ) {
    if( (row > TITLEROW) && (row < getRowCount()) ) {
      if( (column >= 0) && (column < getColumnCount()) ) {
        return (Date) getValueAt( row, column );
      }
    }
    return null;
  }

  /**
   * Creates a row of values used for the title.
   * 
   * @return   A row of values used for the title. Not <code>null</code>.
   */
  private String[] createTitleRow() {
    String[] result = new String[ getColumnCount() ]; 
    for( int column = 0; column < getColumnCount(); column++ ) {
      result[ column ] = weekdays[ column ].getShortPresentable( locale );
    }
    return result;
  }

  /**
   * Fills the supplied rows with date instances used to display the dates of the previous month.
   * 
   * @param rowdata   The row which has to be filled. Not <code>null</code>.
   * @param col       The last column which had to be filled.
   * @param numdays   The number of days of the current month.
   */
  private void insertPreviousMonth( Object[] rowdata, int col, int numdays ) {
    int offset = -1;
    while( col >= 0 ) {
      rowdata[ col ] = newDate( offset, numdays );
      offset--;
      col--;
    }
  }

  /**
   * Creates a new date instance which is derived from the current date.
   * 
   * @param day       The current day. Maybe negative to create a date of the previous month.
   * @param numdays   The number of days of the current month.
   * 
   * @return   The newly created date. Not <code>null</code>.
   */
  @SuppressWarnings("deprecation")
  private Date newDate( int day, int numdays ) {
    Date result = new Date();
    month.set( result );
    result.setYear( year - 1900 );
    if( day > 0 ) {
      result.setDate( Math.min( day, numdays ) );
      if( day > numdays ) {
        day -= numdays;
        while( day > 0 ) {
          result.setDate( result.getDate() + 1 );
          day--;
        }
      }
    } else {
      result.setDate(1);
      day = -day;
      while( day > 0 ) {
        result.setDate( result.getDate() - 1 );
        day--;
      }
    }
    return result;
  }
  
  /**
   * Sets up the table model used to represent a single month.
   */
  private synchronized void recalculate() {
    
    setRowCount(0);
    addRow( createTitleRow() );
    
    Weekday  wd         = month.getFirstWeekday( year );
    int      numdays    = month.getDayCount( year );
    int      currentday = -1;
    
    for( int row = 0; row < WEEKDAYCOUNT; row++ ) {
      
      Object[] rowdata  = new Object[ getColumnCount() ];
      boolean  add      = false;
      
      for( int col = 0; col < getColumnCount(); col++ ) {
        if( currentday == -1 ) {
          // we haven't added a date yet
          if( weekdays[ col ] == wd ) {
            // now we found the first entry for the weekday
            currentday = 1;
            insertPreviousMonth( rowdata, col - 1, numdays );
          }
        }
        if( currentday != -1 ) {
          // we're adding records now
          rowdata[ col ] = newDate( currentday, numdays );
          if( currentday <= numdays ) {
            add = true;
          }
          currentday++;
        }
      }
      
      if( add ) {
        addRow( rowdata );
      }
      
    }
    
  }

} /* ENDCLASS */
