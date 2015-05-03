package com.kasisoft.libs.common.examples;

import com.kasisoft.libs.common.ui.component.*;

import com.kasisoft.libs.common.ui.layout.*;

import lombok.experimental.*;

import lombok.*;

import javax.swing.event.*;

import javax.swing.*;

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

  LocalBehaviour    localbehaviour;
  
  public InputFieldsDemo() {
    super( "Input Fields Demo" );
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
    
  }
  
  @Override
  protected void arrange() {
    
    super.arrange();
    
    // empty values are valid
    JPanel panel1 = newJPanel( new SmartGridLayout( -1, 3, 4, 4 ) );
    panel1.setBorder( BorderFactory.createEmptyBorder( 4, 4, 4, 5 ) );
    
    panel1.add( newKLabel( "Double" )  , SmartGridLayout.FIXMINSIZE   );
    panel1.add( doublevalue1           , SmartGridLayout.FIXMINHEIGHT );
    panel1.add( doublecontent1         , SmartGridLayout.FIXMINHEIGHT );
    
    panel1.add( newKLabel( "Integer" ) , SmartGridLayout.FIXMINSIZE   );
    panel1.add( intvalue1              , SmartGridLayout.FIXMINHEIGHT );
    panel1.add( intcontent1            , SmartGridLayout.FIXMINHEIGHT );
    
    addTab( "Empty values are valid", panel1 );

    // empty values are invalid
    JPanel panel2 = newJPanel( new SmartGridLayout( -1, 3, 4, 4 ) );
    panel2.setBorder( BorderFactory.createEmptyBorder( 4, 4, 4, 5 ) );
    
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
      }
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
