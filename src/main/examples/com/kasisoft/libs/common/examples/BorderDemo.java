package com.kasisoft.libs.common.old.old.examples;

import static javax.swing.BorderFactory.*;

import com.kasisoft.libs.common.old.old.constants.*;

import com.kasisoft.libs.common.old.old.ui.layout.*;

import com.kasisoft.libs.common.old.old.ui.border.*;

import lombok.experimental.*;

import lombok.*;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BorderDemo extends AbstractDemo {

  static final Color COLOR_FROM = Color.orange;
  static final Color COLOR_TO   = COLOR_FROM.brighter().brighter();

  JPanel   center;
  JPanel   left;
  JPanel   right;
  JLabel   left_border;
  JLabel   right_border;
  JLabel   top_border;
  JLabel   bottom_border;
  JLabel   left_right_border;
  JLabel   top_bottom_border;
  JLabel   right_top_border;
  JLabel   all_border;

  public BorderDemo() {
    super( "Border Demo" );
  }
  
  @Override
  protected void components() {
    
    super.components();
    
    left    = newJPanel( new SmartGridLayout( 8, 1, 4, 4 ) );
    center  = newJPanel();
    right   = newJPanel();
    
    left   . setBorder( createCompoundBorder( newFlowingTitledBorder( Alignment.Left   ), newEmptyBorder() ) );
    center . setBorder( createCompoundBorder( newFlowingTitledBorder( Alignment.Center ), newEmptyBorder() ) );
    right  . setBorder( createCompoundBorder( newFlowingTitledBorder( Alignment.Right  ), newEmptyBorder() ) );

    left_border       = newJLabel( "<- LEFT"             , false, true , false, false );
    right_border      = newJLabel( "RIGHT ->"            , false, false, false, true  );
    top_border        = newJLabel( "TOP /\\"             , true , false, false, false );
    bottom_border     = newJLabel( "BOTTOM \\/"          , false, false, true , false );
    left_right_border = newJLabel( "<- LEFT, RIGHT ->"   , false, true , false, true  );
    top_bottom_border = newJLabel( "<- LEFT, BOTTOM \\/" , false, true , true , false );
    right_top_border  = newJLabel( "/\\ TOP, RIGHT ->"   , true , false, false, true  );
    all_border        = newJLabel( "ALL"                 , true , true , true , true  );
    
  }
  
  @Override
  protected void arrange() {
  
    super.arrange();
    
    left.add( left_border       , SmartGridLayout.FIXMINHEIGHT );
    left.add( right_border      , SmartGridLayout.FIXMINHEIGHT );
    left.add( top_border        , SmartGridLayout.FIXMINHEIGHT );
    left.add( bottom_border     , SmartGridLayout.FIXMINHEIGHT );
    left.add( left_right_border , SmartGridLayout.FIXMINHEIGHT );
    left.add( top_bottom_border , SmartGridLayout.FIXMINHEIGHT );
    left.add( right_top_border  , SmartGridLayout.FIXMINHEIGHT );
    left.add( all_border        , SmartGridLayout.FIXMINHEIGHT );
    
    addTab( "Left"   , left   );
    addTab( "Center" , center );
    addTab( "Right"  , right  );
    
  }  
  
  private FlowingTitledBorder newFlowingTitledBorder( Alignment alignment ) {
    return new FlowingTitledBorder( "FlowingTitledBorder: " + alignment.name(), alignment, null, Color.black, COLOR_FROM, COLOR_TO );
  }
  
  private Border newEmptyBorder() {
    return createEmptyBorder( 4, 4, 4, 4 );
  }
  
  private JLabel newJLabel( String label, boolean top, boolean left, boolean bottom, boolean right ) {
    JLabel result = new JLabel( label );
    result.setBorder( new CellBorder( Color.blue, 4, top, left, bottom, right ) );
    return result;
  }
  
  public static void main( String[] args ) throws Exception {
    UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
    BorderDemo demo = new BorderDemo();
    demo.setVisible( true );
  }
  
} /* ENDCLASS */
