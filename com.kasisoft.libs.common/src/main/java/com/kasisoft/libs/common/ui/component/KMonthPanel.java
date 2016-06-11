package com.kasisoft.libs.common.ui.component;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.i18n.*;

import com.kasisoft.libs.common.ui.model.*;

import com.kasisoft.libs.common.ui.event.*;

import com.kasisoft.libs.common.ui.*;

import lombok.experimental.*;

import lombok.*;

import javax.swing.table.*;

import javax.swing.event.*;

import javax.swing.*;
import javax.swing.border.*;

import java.util.function.*;

import java.util.*;

import java.text.*;

import java.awt.event.*;

import java.awt.*;
import java.awt.geom.*;

/**
 * This panel shows a single month within a specific year. It allows to select a specific day.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KMonthPanel extends JPanel {

  SimpleDateFormat             formatter;
  LocalBehaviour               localbehaviour;
  ChangeEventDispatcher        dispatcher;
  Border                       innerborder;
  JTable                       table;
  int                          cellwidth;
  int                          cellheight;
  
  @Getter @Setter
  int                          cellPadding;
  
  @Getter
  Date                         selectionDate;
  
  @Getter @Setter
  boolean                      selectOnDoubleClick;
  
  @Getter @Setter
  boolean                      toggleSelection;

  @Getter @Setter
  Predicate<Date>              selectableDates;

  @Getter @Setter
  Predicate<Date>              holidayDates;

  @Getter @Setter
  String                       styleTitle;

  @Getter @Setter
  String                       styleNormal;

  /** this style requires to configure the constraint {@link #holidayDates} as well. */
  @Getter @Setter
  String                       styleHoliday;

  @Getter @Setter
  String                       styleHolidayTitle;

  @Getter @Setter
  String                       styleSelected;

  @Getter
  MonthModel                   model;  

  /**
   * Sets up this table used to display a month.
   * 
   * @param month   The month used to be displayed. If <code>null</code> the current month is used.
   */
  public KMonthPanel( Month month ) {
    this( null, month, null );
  }

  /**
   * Sets up this table used to display a month.
   * 
   * @param month   The month used to be displayed. If <code>null</code> the current month is used.
   * @param year    The year used to be displayed. 
   */
  public KMonthPanel( Month month, int year ) {
    this( null, month, Integer.valueOf( year ) );
  }

  /**
   * Sets up this table used to display a month.
   * 
   * @param date   The date used to be displayed. Not <code>null</code>.
   */
  @SuppressWarnings("deprecation")
  public KMonthPanel( Date date ) {
    this( null, Month.valueOf( date ), Integer.valueOf( date.getDate() ) );
  }

  /**
   * Sets up this table used to display a month.
   * 
   * @param locale   The Locale used to provide textual content. If <code>null</code> the default Locale is used.
   * @param date     The date used to be displayed. Not <code>null</code>.
   */
  @SuppressWarnings("deprecation")
  public KMonthPanel( Locale locale, Date date ) {
    this( locale, Month.valueOf( date ), Integer.valueOf( date.getDate() ) );
  }

  /**
   * Sets up this table used to display a month.
   * 
   * @param locale   The Locale used to provide textual content. If <code>null</code> the default Locale is used.
   * @param month    The month used to be displayed. If <code>null</code> the current month is used.
   */
  public KMonthPanel( Locale locale, Month month ) {
    this( locale, month, null );
  }

  /**
   * Sets up this table used to display a month.
   * 
   * @param locale   The Locale used to provide textual content. If <code>null</code> the default Locale is used.
   * @param month    The month used to be displayed. If <code>null</code> the current month is used.
   * @param year     The year used to be displayed. 
   */
  public KMonthPanel( Locale locale, Month month, int year ) {
    this( locale, month, Integer.valueOf( year ) );
  }

  /**
   * Sets up this table used to display a month.
   * 
   * @param locale   The Locale used to provide textual content. If <code>null</code> the default Locale is used.
   * @param month    The month used to be displayed. If <code>null</code> the current month is used.
   * @param year     The year used to be displayed. If <code>null</code> the current year is used. 
   */
  private KMonthPanel( Locale locale, Month month, Integer year ) {
    super( new BorderLayout() );
    initAttributes( locale, month, year );
    setupGUI();
    redimension();
    table.addMouseListener( localbehaviour );
  }
  
  /**
   * Initialises local class attributes.
   * 
   * @param locale   The Locale used to provide textual content. If <code>null</code> the default Locale is used.
   * @param month    The month used to be displayed. If <code>null</code> the current month is used.
   * @param year     The year used to be displayed. If <code>null</code> the current year is used. 
   */
  private void initAttributes( Locale locale, Month month, Integer year ) {
    selectionDate         = null;
    selectableDates       = null;
    holidayDates          = null;
    styleTitle            = null;
    styleNormal           = null;
    styleHoliday          = null;
    styleSelected         = null;
    selectOnDoubleClick   = false;
    toggleSelection       = false;
    cellPadding           = 2;
    model                 = new MonthModel( month, year, locale );
    dispatcher            = new ChangeEventDispatcher();
    formatter             = new SimpleDateFormat( "dd.MM.yyyy", I18NFunctions.getLocale( locale ) );
    localbehaviour        = new LocalBehaviour( this );
  }
  
  /**
   * Creates and layout GUI components.
   */
  private void setupGUI() {
    innerborder = BorderFactory.createEmptyBorder( 2, 2, 2, 2 );
    table       = newTable( model );
    setBorder( null );
    add( table, BorderLayout.CENTER );
    setBackground( table.getBackground() );
  }

  @Override
  public Dimension getMinimumSize() {
    return getPreferredSize();
  }

  @Override
  public Dimension getMaximumSize() {
    return getPreferredSize();
  }

  /**
   * @see MonthModel#setFirstWeekday(Weekday)
   */
  public void setFirstWeekday( @NonNull Weekday newfirstweekday ) {
    model.setFirstWeekday( newfirstweekday );
  }
  
  /**
   * @see MonthModel#getFirstWeekday()
   */
  public Weekday getFirstWeekday() {
    return model.getFirstWeekday();
  }

  /**
   * Creates a new JTable instance for the supplied model.
   * 
   * @param model   The TableModel used for the JTable data. Not <code>null</code>.
   * 
   * @return   The new JTable instance. Not <code>null</code>.
   */
  private JTable newTable( final MonthModel model ) {
    JTable result = new JTable( model ) {
      
      @Override
      public String getToolTipText( MouseEvent evt ) {
        Date date = model.dateValueAt( table.rowAtPoint( evt.getPoint() ), table.columnAtPoint( evt.getPoint() ) );
        if( date != null ) {
          return formatter.format( date );
        } else {
          return super.getToolTipText();
        }
      }

    };
    result . setDefaultRenderer( Object.class, localbehaviour );
    result . setTableHeader( null );
    result . setShowGrid( false );
    result . setRowMargin(0);
    result . getColumnModel().setColumnMargin(0);
    return result;
  }
  
  @Override
  public void setBorder( Border newborder ) {
    if( newborder == null ) {
      super.setBorder( innerborder );
    } else {
      super.setBorder( BorderFactory.createCompoundBorder( newborder, innerborder ) );
    }
  }

  @Override
  public Border getBorder() {
    Border result = super.getBorder();
    if( result instanceof CompoundBorder ) {
      result = ((CompoundBorder) result).getOutsideBorder();
    }
    return result;
  }
  
  /**
   * Registers the supplied listener to become informed upon changes.
   * 
   * @param listener   The listener which becomes informed upon changes. Not <code>null</code>.
   */
  public void addChangeListener( @NonNull ChangeListener listener ) {
    dispatcher.addListener( listener );
  }
  
  /**
   * Prevents further notification for the supplied listener.
   * 
   * @param listener   The listener that won't be notified anymore. Not <code>null</code>.
   */
  public void removeChangeListener( @NonNull ChangeListener listener ) {
    dispatcher.removeListener( listener );
  }
  
  /**
   * Changes the current date.
   * 
   * @param newdate   The new selection date to be used. <code>null</code> has the same effect as a deselect.
   */
  public void setSelectionDate( Date newdate ) {
    if( newdate != null ) {
      selectionDate = newdate;
      model.setDate( selectionDate );
      dispatcher.fireEvent( new ChangeEvent( this ) );
    } else if( selectionDate != null ) {
      selectionDate = null;
      dispatcher.fireEvent( new ChangeEvent( this ) );
    }
    repaint();
  }

  /**
   * Recalculates dimensions for the table.
   */
  private void redimension() {
    
    cellwidth  = 1;
    cellheight = 1;
    
    for( int row = 0; row < model.getRowCount(); row++ ) {
      for( int col = 0; col < model.getColumnCount(); col++ ) {
        Object      value   = model.getValueAt( row, col );
        JLabel      label   = (JLabel) localbehaviour.getTableCellRendererComponent( table, value, true, true, row, col );
        FontMetrics fm      = label.getFontMetrics( label.getFont() );
        Rectangle2D rect    = fm.getStringBounds( label.getText(), label.getGraphics() );
        Insets      insets  = label.getInsets();
        cellheight          = Math.max( cellheight , (int) Math.ceil( rect.getHeight () ) + insets.top  + insets.bottom );
        cellwidth           = Math.max( cellwidth  , (int) Math.ceil( rect.getWidth  () ) + insets.left + insets.right  );
      }
    }
    
    cellwidth  += cellPadding * 2;
    cellheight += cellPadding * 2;
    
    for( int row = 0; row < table.getRowCount(); row++ ) {
      table.setRowHeight( row, cellheight );
    }
    
    TableColumnModel tcm = table.getColumnModel();
    for( int i = 0; i < table.getColumnCount(); i++ ) {
      TableColumn tc = tcm.getColumn(i); 
      tc.setMaxWidth       ( cellwidth );
      tc.setMinWidth       ( cellwidth );
      tc.setPreferredWidth ( cellwidth );
    }
    
  }
  
  /**
   * Will be invoked whenever a new date has been selected.
   * 
   * @param newdate   The new selected date. Not <code>null</code>.
   */
  private void selectDate( Date newdate ) {
    if( model.getMonth().isInMonth( newdate ) ) {
      if( selectableDates != null ) {
        if( ! selectableDates.test( newdate ) ) {
          // rejected by our constraint
          return;
        }
      }
      if( equal( selectionDate, newdate ) ) {
        if( toggleSelection ) {
          updateSelectionDate( null );
        }
      } else {
        updateSelectionDate( newdate );
      }
    }
  }
  
  private void updateSelectionDate( Date newdate ) {
    selectionDate = newdate;
    repaint();
    dispatcher.fireEvent( new ChangeEvent( this ) );
  }
  
  @SuppressWarnings("deprecation")
  private boolean equal( Date date1, Date date2 ) {
    if( (date1 != null) && (date2 != null) ) {
      return 
          (date1.getMonth() == date2.getMonth()) &&
          (date1.getDate() == date2.getDate()) &&
          (date1.getYear() == date2.getYear());
    } else {
      return (date1 == null) && (date2 == null);
    }
  }
  
  /**
   * Will be invoked whenever a date has been selected.
   * 
   * @param point   The mouse location within the table. Not <code>null</code>.
   */
  private void selectDate( Point point ) {
    int row       = table.rowAtPoint    ( point );
    int col       = table.columnAtPoint ( point );
    if( (row > 0) && (row < model.getRowCount()) ) {
      if( (col >= 0) && (col < model.getColumnCount()) ) {
        selectDate( model.dateValueAt( row, col ) );
      } else {
        updateSelectionDate( null );
      }
    } else {
      updateSelectionDate( null );
    }
  }
  
  /**
   * Implementation of custom behaviour.
   */
  private static class LocalBehaviour extends DefaultTableCellRenderer implements MouseListener {
    
    KMonthPanel   pthis;
    
    public LocalBehaviour( KMonthPanel ref ) {
      pthis = ref;
      setSize( pthis.cellwidth, pthis.cellheight );
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public Component getTableCellRendererComponent( 
      JTable table, Object value, boolean selected, boolean focused, int row, int column 
    ) {
      
      selected         = false;
      String stylename = null;
      Date   date      = null;
      int    day       = 0;
      
      if( value instanceof Date ) {
        date = (Date) value;
        if( pthis.model.getMonth().isInMonth( date ) ) {
          day         = date.getDate();
          value       = Integer.valueOf( day );
          if( pthis.selectionDate != null ) {
            selected  = pthis.selectionDate.getDate() == day;
          }
        } else {
          date      = null;
          value     = "";
        }
      }
      
      stylename = getStylename( row, date, selected );
      
      JLabel label = (JLabel) super.getTableCellRendererComponent( table, value, selected, false, row, column );
      if( stylename != null ) {
        // the styling overrides everything
        StyleManager.getInstance().applyStyle( label, stylename );
      } else {
        // some default stylings in case nothing has been configured
        if( selected ) {
          label.setForeground( pthis.getBackground() );
          label.setBackground( pthis.getForeground() );
        } else if( row == 0 ) {
          label.setFont( label.getFont().deriveFont( Font.BOLD ) );
        }
      }
      
      return label;
    }
    
    private String getStylename( int row, Date date, boolean selected ) {
      if( row == 0 ) {
        return pthis.styleTitle;
      }
      if( date == null ) {
        return pthis.styleNormal;
      }
      boolean holiday = isHoliday( date );
      if( row == 0 ) {
        return getTitleStyle( holiday );
      }
      if( selected ) {
        return pthis.styleSelected;
      }
      if( holiday && (pthis.styleHoliday != null) ) {
        return pthis.styleHoliday;
      }
      return pthis.styleNormal;
    }
    
    private String getTitleStyle( boolean holiday ) {
      String result = pthis.styleTitle;
      if( holiday && (pthis.styleHolidayTitle != null) ) {
        result = pthis.styleHolidayTitle;
      }
      return result;
    }
    
    private boolean isHoliday( Date date ) {
      boolean result = Weekday.isWeekend( date );
      if( (! result) && (pthis.holidayDates != null) ) {
        result = pthis.holidayDates.test( date );
      }
      return result;
    }
    
    @Override
    public void mouseClicked( MouseEvent evt ) {
      if( evt.getButton() == MouseEvent.BUTTON1 ) {
        if( pthis.selectOnDoubleClick ) {
          if( evt.getClickCount() > 1 ) {
            pthis.selectDate( evt.getPoint() );
          }
        } else {
          if( evt.getClickCount() == 1 ) {
            pthis.selectDate( evt.getPoint() );
          }
        }
      }
    }

    @Override
    public void mouseEntered( MouseEvent evt ) {
    }

    @Override
    public void mouseExited( MouseEvent evt ) {
    }

    @Override
    public void mousePressed( MouseEvent evt ) {
    }

    @Override
    public void mouseReleased( MouseEvent evt ) {
    }

  } /* ENDCLASS */
  
} /* ENDCLASS */
