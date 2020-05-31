package com.kasisoft.libs.common.old.ui;

import com.kasisoft.libs.common.old.model.ScreenInfo;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import java.util.HashMap;
import java.util.Map;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.awt.Frame;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * A small extension to the {@link JFrame} which provides some helpful convenience functionalities.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KFullScreen extends JFrame {

  @Getter
  ScreenInfo              screenInfo;
  
  Map<String, Runnable>   actions;
  
  /**
  /**
   * Initializes this frame.
   */
  public KFullScreen( @NonNull ScreenInfo screenInfo ) {
    this( null, false, screenInfo );
  }

  /**
   * Initializes this frame using the supplied title.
   * 
   * @param title   The window title. Neither <code>null</code> nor empty.
   */
  public KFullScreen( String title, @NonNull ScreenInfo screenInfo ) {
    this( title, false, screenInfo );
  }

  /**
   * Initializes this frame using the supplied title.
   * 
   * @param title       The window title. Neither <code>null</code> nor empty.
   * @param defer       <code>true</code> <=> The {@link #init()} method is called immediately. Otherwise the 
   *                    caller is supposed to invoke it.
   * @param screenInfo  The screen that should be used.
   */
  public KFullScreen( String title, boolean defer, ScreenInfo sInfo ) {
    super( sInfo.getGraphicsConfiguration() );
    screenInfo  = sInfo;
    if( ! screenInfo.isFullScreenSupported() ) {
      throw new IllegalArgumentException();
    }
    init( title, defer, null );
  }
  
  private void init( String title, boolean defer, String wsprop ) {
    setTitle( title );
    actions         = new HashMap<>();
    if( ! defer ) {
      init();
    }
    registerAction( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE , 0 ), this::closeFrame       );
    setExtendedState( Frame.MAXIMIZED_BOTH ); 
    setUndecorated( true );
    setResizable( true );
  }  
  
  public void closeFrame() {
    dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
  }
  
  public void registerAction( KeyStroke keyStroke, Runnable action ) {
    actions.put( action.getClass().getName(), action );
    getRootPane().registerKeyboardAction( 
      this::executeAction, 
      action.getClass().getName(),
      keyStroke, 
      JComponent.WHEN_IN_FOCUSED_WINDOW 
    );
  }
  
  public void unregisterAction( KeyStroke keyStroke, Runnable action ) {
    actions.remove( action.getClass().getName() );
    getRootPane().unregisterKeyboardAction( keyStroke );
  }
  
  private void executeAction( ActionEvent evt ) {
    Runnable runnable = actions.get( evt.getActionCommand() );
    if( runnable != null ) {
      runnable.run();
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
    listeners();
    finish();
    
    addWindowListener( new LocalBehaviour( this::onShutdown ) );
    
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
  public void setVisible( boolean enable ) {
    
    if( enable == isVisible() ) {
      return;
    }

    if( enable ) {
      screenInfo.getScreen().setFullScreenWindow( this );
    } else {
      screenInfo.getScreen().setFullScreenWindow( null );
    }
    
    super.setVisible( enable );
    
  }
  
  @AllArgsConstructor
  private static class LocalBehaviour extends WindowAdapter {

    Runnable  runnable;
    
    @Override
    public void windowClosing( WindowEvent evt ) {
      runnable.run();
    }

  } /* ENDCLASS */

} /* ENDCLASS */
