package com.kasisoft.libs.common.ui;

import com.kasisoft.libs.common.workspace.*;

import javax.swing.*;

import java.awt.event.*;

import lombok.*;
import lombok.experimental.*;

/**
 * A small extension to the {@link JFrame} which provides some helpful convenience functionalities.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KFrame extends JFrame {

  /**
   * Initializes this frame.
   */
  public KFrame() {
    super();
    init();
  }
  
  /**
   * Initializes this frame using the supplied title.
   * 
   * @param title   The window title. Neither <code>null</code> nor empty.
   */
  public KFrame( String title ) {
    super( title );
    init();
  }

  /**
   * Run the initialization of this frame.
   */
  private void init() {
    
    setSize( 640, 480 );
    
    initialize();
    components();
    configure();
    arrange();
    listeners();
    finish();
   
    LocalBehaviour localbehaviour = new LocalBehaviour( this );
    getRootPane().registerKeyboardAction( 
      localbehaviour, 
      KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ), 
      JComponent.WHEN_IN_FOCUSED_WINDOW 
    );
    addWindowListener( localbehaviour );
    
  }
  
  /**
   * 01. Prepare fields/calculations etc.
   */
  protected void initialize() {
  }

  /**
   * 02. Sets up all components.
   */
  protected void components() {
  }

  /**
   * 03. Loads configuration information and sets up components accordingly.
   */
  protected void configure() {
  }
  
  /**
   * 04. Layout all components.
   */
  protected void arrange() {
  }
  
  /**
   * 05. Create all listeners.
   */
  protected void listeners() {
  }
  
  /**
   * 06. Finishes the configuration while registering some basic listeners.
   */
  protected void finish() {
  }
  
  /**
   * This method will be called whenever the window will be shut down.
   */
  protected void onShutdown() {
  }

  @Override
  public synchronized void addComponentListener( ComponentListener l ) {
    if( l instanceof WSComponentListener ) {
      ((WSComponentListener) l).configure( this );
    }
    super.addComponentListener( l );
  }

  private static class LocalBehaviour extends WindowAdapter implements ActionListener {

    KFrame   pthis;
    
    public LocalBehaviour( KFrame ref ) {
      pthis = ref;
    }
    
    @Override
    public void actionPerformed( ActionEvent evt ) {
      pthis.onShutdown();
    }
    
    @Override
    public void windowClosing( WindowEvent e ) {
      pthis.onShutdown();
    }

  } /* ENDCLASS */
  
} /* ENDCLASS */
