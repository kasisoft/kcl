package com.kasisoft.libs.common.ui;

import com.kasisoft.libs.common.workspace.*;

import com.kasisoft.libs.common.config.*;

import com.kasisoft.libs.common.xml.adapters.*;

import com.kasisoft.libs.common.text.*;

import lombok.experimental.*;

import lombok.*;

import javax.swing.*;

import java.awt.event.*;

import java.awt.*;

/**
 * A small extension to the {@link JFrame} which provides some helpful convenience functionalities.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KFrame extends JFrame implements WorkspacePersistent {

  String                      property;
  SimpleProperty<Rectangle>   propertyBounds;
  Rectangle                   initialBounds;
  
  /**
   * Initializes this frame.
   */
  public KFrame() {
    this( null, false, null );
  }

  /**
   * Initializes this frame using the supplied title.
   * 
   * @param title   The window title. Neither <code>null</code> nor empty.
   */
  public KFrame( String title ) {
    this( title, false, null );
  }

  /**
   * Initializes this frame using the supplied title.
   * 
   * @param title   The window title. Neither <code>null</code> nor empty.
   * @param defer   <code>true</code> <=> The {@link #init()} method is called immediately. Otherwise the 
   *                caller is supposed to invoke it.
   */
  public KFrame( String title, boolean defer, String wsprop ) {
    super();
    setTitle( title );
    property = StringFunctions.cleanup( wsprop );
    if( property != null ) {
      propertyBounds = new SimpleProperty<>( String.format( "%s.bounds", property ), new RectangleAdapter() );
    }
    if( ! defer ) {
      init();
    }
  }

  /**
   * Run the initialization of this frame.
   */
  protected void init() {
    
    setSize( 640, 480 );
    
    initialize();
    components();
    configure();
    arrange();
    wsConfiguration();
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
   * Load all configuration for {@link WorkspacePersistent} components.
   */
  private void wsConfiguration() {
    Workspace.getInstance().configure( this );
    // register a shutdown hook, so everything will be persisted while closing
    Workspace.getInstance().setShutdown( $ -> Workspace.getInstance().persist( KFrame.this ) );
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
  public String getPersistentProperty() {
    return property;
  }

  @Override
  public void loadPersistentSettings() {
    if( property != null ) {
      initialBounds = propertyBounds.getValue( Workspace.getInstance().getProperties() );
    }
  }

  @Override
  public void savePersistentSettings() {
    if( property != null ) {
      propertyBounds.setValue( Workspace.getInstance().getProperties(), getBounds() );
    }
  }
  
  @Override
  public void setVisible( boolean enable ) {
    if( initialBounds != null ) {
      setBounds( initialBounds );
    } else {
      SwingFunctions.center( this );
    }
    super.setVisible( enable );
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
