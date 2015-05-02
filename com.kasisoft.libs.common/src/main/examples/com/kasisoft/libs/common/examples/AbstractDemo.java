package com.kasisoft.libs.common.examples;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.ui.*;

import lombok.experimental.*;

import lombok.*;

import javax.swing.*;

import java.awt.*;

/**
 * A simple demonstration of ui components.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AbstractDemo extends KFrame {

  JTabbedPane   tabbed;
  
  public AbstractDemo( String title ) {
    super( title );
  }
  
  @Override
  protected void components() {
    
    tabbed = new JTabbedPane( Alignment.Top.getAlignment() );
    
  }

  @Override
  protected void arrange() {
    getContentPane().setLayout( new BorderLayout( 4, 4 ) );
    getContentPane().add( tabbed, BorderLayout.CENTER );
  }
  
  protected void addTab( String label, JComponent component ) {
    tabbed.add( label, component );
  }
  
  @Override
  protected void onShutdown() {
    dispose();
  }
  
  protected JPanel newJPanel() {
    return newJPanel( null );
  }

  protected JPanel newJPanel( LayoutManager layout ) {
    if( layout == null ) {
      layout = new BorderLayout( 4, 4 ); 
    }
    return new JPanel( layout );
  }

} /* ENDCLASS */
