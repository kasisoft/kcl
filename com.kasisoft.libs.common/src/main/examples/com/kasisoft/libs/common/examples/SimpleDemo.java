package com.kasisoft.libs.common.examples;

import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.ui.*;
import com.kasisoft.libs.common.ui.border.*;
import com.kasisoft.libs.common.ui.component.*;
import com.kasisoft.libs.common.ui.layout.*;

import javax.swing.*;

import java.awt.*;

import lombok.*;
import lombok.experimental.*;

/**
 * A simple demonstration of ui components.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SimpleDemo extends KFrame {

  static final Color COLOR_FROM = Color.orange;
  static final Color COLOR_TO   = COLOR_FROM.brighter().brighter();
  
  KMultiSplitPane   multisplitpane;
  JPanel            panel1;
  JPanel            panel2;
  JPanel            panel3;
  KDoubleField      doublevalue;
  
  public SimpleDemo() {
    super( "Simple Demo" );
  }

  @Override
  protected void components() {
    
    multisplitpane  = new KMultiSplitPane( Orientation.Horizontal, 3 );
    
    doublevalue     = new KDoubleField();
    doublevalue.setPlaceHolder( "Double Value" );
    
    panel1          = new JPanel( new SmartGridLayout( 1, 2, 4, 4 ) );
    panel2          = new JPanel( new BorderLayout( 4, 4 ) );
    panel3          = new JPanel( new BorderLayout( 4, 4 ) );
    
    panel1.setBorder( new FlowingTitledBorder( "Center" , Alignment.Center , null, Color.black, COLOR_FROM, COLOR_TO ) );
    panel2.setBorder( new FlowingTitledBorder( "Left"   , Alignment.Left   , null, Color.black, COLOR_FROM, COLOR_TO ) );
    panel3.setBorder( new FlowingTitledBorder( "Right"  , Alignment.Right  , null, Color.black, COLOR_FROM, COLOR_TO ) );
    
  }

  @Override
  protected void arrange() {
    
    multisplitpane.setField( 0, panel1 );
    multisplitpane.setField( 1, panel2 );
    multisplitpane.setField( 2, panel3 );
    
    getContentPane().setLayout( new BorderLayout( 4, 4 ) );
    getContentPane().add( multisplitpane, BorderLayout.CENTER );
    
    panel1.add( new JLabel( "Double" ) , SmartGridLayout.FIXMINSIZE   );
    panel1.add( doublevalue            , SmartGridLayout.FIXMINHEIGHT );
    
  }
  
  @Override
  protected void onShutdown() {
    dispose();
  }

  public static void main( String[] args ) throws Exception {
    SimpleDemo demo = new SimpleDemo();
    demo.setVisible( true );
  }
  
} /* ENDCLASS */
