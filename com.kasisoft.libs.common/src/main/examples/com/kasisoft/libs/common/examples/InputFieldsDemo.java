package com.kasisoft.libs.common.examples;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.ui.component.*;

import com.kasisoft.libs.common.ui.layout.*;

import com.kasisoft.libs.common.ui.*;

import lombok.experimental.*;

import lombok.*;

import javax.swing.event.*;

import javax.swing.*;

import java.util.function.*;

import java.util.*;

import java.awt.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InputFieldsDemo extends AbstractDemo {

  KDoubleField      doublevalue1;
  KIntegerField     intvalue1;
  KLabel            doublecontent1;
  KLabel            intcontent1;

  KDoubleField      doublevalue2;
  KIntegerField     intvalue2;
  KLabel            doublecontent2;
  KLabel            intcontent2;

  KMonthPanel       monthpanel;
  KLabel            monthcontent;
  
  LocalBehaviour    localbehaviour;
  
  public InputFieldsDemo() {
    super( "Input Fields Demo" );
  }
  
  @Override
  protected void initialize() {
    
    super.initialize();

    Style day = new Style( "day", StyleManager.getInstance().getDefaultStyle() );
    day.setAlignment( Alignment.Center );

    Style title = new Style( "title", day );
    title.setFont( title.getFont().deriveFont( Font.BOLD ) );

    Style weekend = new Style( "weekend", day );
    weekend.setFont( weekend.getFont().deriveFont( Font.ITALIC ) );
    weekend.setForeground( Color.red );

    Style selected = new Style( "selected", day );
    selected.setBackground( Color.magenta );
    selected.setFont( selected.getFont().deriveFont( Font.BOLD ) );
    selected.setForeground( Color.white );

    StyleManager.getInstance().addStyles( day, title, weekend, selected );
    
  }
  
  @Override
  protected void components() {
    
    super.components();
    
    // empty values are valid
    doublevalue1    = new KDoubleField();
    doublevalue1.setPlaceHolder( "Double Value" );
    doublevalue1.setEmptyValid( true );
    doublevalue1.setMinimum( -664.12 );
    
    intvalue1       = new KIntegerField();
    intvalue1.setPlaceHolder( "Integer Value" );
    intvalue1.setEmptyValid( true );
    
    intvalue1.setMinimum( -122L );
    intvalue1.setMaximum( 631L );

    doublecontent1  = newKLabel( "-" );
    intcontent1     = newKLabel( "-" );

    // empty values are invalid
    doublevalue2    = new KDoubleField();
    doublevalue2.setPlaceHolder( "Double Value" );
    doublevalue2.setEmptyValid( false );
    doublevalue2.setMinimum( -664.12 );
    
    intvalue2       = new KIntegerField();
    intvalue2.setPlaceHolder( "Integer Value" );
    intvalue2.setEmptyValid( false );
    
    intvalue2.setMinimum( -122L );
    intvalue2.setMaximum( 631L );

    doublecontent2  = newKLabel( "-" );
    intcontent2     = newKLabel( "-" );
   
    // calendar/date stuff
    monthpanel      = new KMonthPanel( Month.May, 2015 );
    monthpanel.setStyleTitle( "title" );
    monthpanel.setStyleNormal( "day" );
    monthpanel.setStyleHolidayTitle( "weekend" );
    monthpanel.setStyleHoliday( "weekend" );
    monthpanel.setStyleSelected( "selected" );
    monthpanel.setToggleSelection( true );
    monthpanel.setHolidayDates( new Predicate<Date>() {

      @SuppressWarnings("deprecation")
      @Override
      public boolean test( Date input ) {
        return input.getDate() == 14;
      }
      
    });
    
    monthcontent    = newKLabel( "-" );

  }
  
  @Override
  protected void arrange() {
    
    super.arrange();
    
    // calendar/date stuff
    JPanel panel3 = newJPanel( new SmartGridLayout( -1, 2, 4, 4 ) );
    panel3.setBorder( BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
    panel3.add( newKLabel( "Month"    ) , SmartGridLayout.FIXMINSIZE  );
    panel3.add( monthpanel              , SmartGridLayout.FIXPREFSIZE );
    panel3.add( newKLabel( "Current:" ) , SmartGridLayout.FIXMINSIZE  );
    panel3.add( monthcontent            , SmartGridLayout.FIXPREFSIZE );
    
    addTab( "Date Inputs", panel3 );
    
    // empty values are valid
    JPanel panel1 = newJPanel( new SmartGridLayout( -1, 3, 4, 4 ) );
    panel1.setBorder( BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
    
    panel1.add( newKLabel( "Double" )  , SmartGridLayout.FIXMINSIZE   );
    panel1.add( doublevalue1           , SmartGridLayout.FIXMINHEIGHT );
    panel1.add( doublecontent1         , SmartGridLayout.FIXMINHEIGHT );
    
    panel1.add( newKLabel( "Integer" ) , SmartGridLayout.FIXMINSIZE   );
    panel1.add( intvalue1              , SmartGridLayout.FIXMINHEIGHT );
    panel1.add( intcontent1            , SmartGridLayout.FIXMINHEIGHT );
    
    addTab( "Empty values are valid", panel1 );

    // empty values are invalid
    JPanel panel2 = newJPanel( new SmartGridLayout( -1, 3, 4, 4 ) );
    panel2.setBorder( BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
    
    panel2.add( newKLabel( "Double" )  , SmartGridLayout.FIXMINSIZE   );
    panel2.add( doublevalue2           , SmartGridLayout.FIXMINHEIGHT );
    panel2.add( doublecontent2         , SmartGridLayout.FIXMINHEIGHT );
    
    panel2.add( newKLabel( "Integer" ) , SmartGridLayout.FIXMINSIZE   );
    panel2.add( intvalue2              , SmartGridLayout.FIXMINHEIGHT );
    panel2.add( intcontent2            , SmartGridLayout.FIXMINHEIGHT );
    
    addTab( "Empty values are invalid", panel2 );
    
  }

  private KLabel newKLabel( String text ) {
    KLabel result = new KLabel( text != null ? text : "" );
    result.setMinCharacters( 10 );
    return result;
  }

  @Override
  protected void listeners() {
    localbehaviour = new LocalBehaviour( this );
  }
  
  @Override
  protected void finish() {
    doublevalue1 . addChangeListener( localbehaviour );
    intvalue1    . addChangeListener( localbehaviour );
    doublevalue2 . addChangeListener( localbehaviour );
    intvalue2    . addChangeListener( localbehaviour );
    monthpanel   . addChangeListener( localbehaviour );
  }
  
  private void doubleValue( KDoubleField doublevalue, KLabel doublecontent ) {
    if( doublevalue.isValid() ) {
      doublecontent.setText( String.valueOf( doublevalue.getValue() ) );
    } else {
      doublecontent.setText( "- invalid -" );
    }
  }

  private void intValue( KIntegerField intvalue, KLabel intcontent ) {
    if( intvalue.isValid() ) {
      intcontent.setText( String.valueOf( intvalue.getValue() ) );
    } else {
      intcontent.setText( "- invalid -" );
    }
  }
  
  private void monthValue( KMonthPanel monthpanel, KLabel monthcontent ) {
    Date selectiondate = monthpanel.getSelectionDate();
    if( selectiondate != null ) {
      monthcontent.setText( String.valueOf( selectiondate ) );
    } else {
      monthcontent.setText( "- unset -" );
    }
  }
  
  public static void main( String[] args ) throws Exception {
    UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
    InputFieldsDemo demo = new InputFieldsDemo();
    demo.setVisible( true );
  }
  
  private static class LocalBehaviour implements ChangeListener {

    InputFieldsDemo   pthis;
    
    public LocalBehaviour( InputFieldsDemo ref ) {
      pthis = ref;
    }
    
    @Override
    public void stateChanged( ChangeEvent evt ) {
      Object source = evt.getSource();
      if( source == pthis.doublevalue1 ) {
        pthis.doubleValue( pthis.doublevalue1, pthis.doublecontent1 );
      } else if( source == pthis.doublevalue2 ) {
        pthis.doubleValue( pthis.doublevalue2, pthis.doublecontent2 );
      } else if( source == pthis.intvalue1 ) {
        pthis.intValue( pthis.intvalue1, pthis.intcontent1 );
      } else if( source == pthis.intvalue2 ) {
        pthis.intValue( pthis.intvalue2, pthis.intcontent2 );
      } else if( source == pthis.monthpanel ) {
        pthis.monthValue( pthis.monthpanel, pthis.monthcontent );
      }
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
