package com.kasisoft.libs.common.ui;

import com.kasisoft.libs.common.config.*;
import com.kasisoft.libs.common.model.*;
import com.kasisoft.libs.common.text.*;
import com.kasisoft.libs.common.workspace.*;
import com.kasisoft.libs.common.xml.adapters.*;

import javax.swing.*;

import java.util.*;

import java.awt.event.*;

import java.awt.*;

import lombok.experimental.*;

import lombok.*;

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
  ScreenInfo                  screenInfo;
  Map<String, Runnable>       actions;
  
  @Getter @Setter
  boolean                     fullScreen;
  
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
   * Initializes this frame.
   */
  public KFrame( @NonNull ScreenInfo screenInfo ) {
    this( null, false, null, screenInfo );
  }

  /**
   * Initializes this frame using the supplied title.
   * 
   * @param title   The window title. Neither <code>null</code> nor empty.
   */
  public KFrame( String title, @NonNull ScreenInfo screenInfo ) {
    this( title, false, null, screenInfo );
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
    init( title, defer, wsprop );
  }

  /**
   * Initializes this frame using the supplied title.
   * 
   * @param title       The window title. Neither <code>null</code> nor empty.
   * @param defer       <code>true</code> <=> The {@link #init()} method is called immediately. Otherwise the 
   *                    caller is supposed to invoke it.
   * @param screenInfo  The screen that should be used.
   */
  public KFrame( String title, boolean defer, String wsprop, ScreenInfo sInfo ) {
    super( sInfo.getGraphicsConfiguration() );
    screenInfo  = sInfo;
    init( title, defer, wsprop );
  }
  
  private void init( String title, boolean defer, String wsprop ) {
    setTitle( title );
    actions         = new HashMap<>();
    initialBounds   = new Rectangle( 0, 0, 640, 480 );
    property        = StringFunctions.cleanup( wsprop );
    if( property != null ) {
      propertyBounds = new SimpleProperty<>( String.format( "%s.bounds", property ), new RectangleAdapter() );
    }
    if( ! defer ) {
      init();
    }
    registerAction( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE , 0 ), this::closeFrame       );
    registerAction( KeyStroke.getKeyStroke( KeyEvent.VK_F11    , 0 ), this::switchFullscreen );
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
    wsConfiguration();
    listeners();
    finish();
    
    addWindowListener( new LocalBehaviour( this::onShutdown ) );
    
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
    
    if( enable == isVisible() ) {
      return;
    }
    
    if( isFullScreen() ) {
      if( screenInfo != null ) {
        if( screenInfo.getScreen().isFullScreenSupported() ) {
          screenInfo.getScreen().setFullScreenWindow( this );
        } else {
          setBounds( screenInfo.getScreen().getDefaultConfiguration().getBounds() );
        }
      } else {
        setWindowAsFullscreen();
      }
    } else {
      if( initialBounds != null ) {
        setBounds( initialBounds );
      } else {
        centerThisWindow();
      }
    }
    
    initialBounds = getBounds();
    
    super.setVisible( enable );
  }
  
  private void switchFullscreen() {
    if( !isCurrentlyFullscreen() ) {
      initialBounds =  getBounds();
      setWindowAsFullscreen();
    } else {
      if( screenInfo != null ) {
        screenInfo.getScreen().setFullScreenWindow( null );
      }
      if( initialBounds != null ) {
        setBounds( initialBounds );
      } else {
        centerThisWindow();
      }
    }
  }
  
  private void setWindowAsFullscreen() {
    if( (screenInfo != null) && screenInfo.isFullScreenSupported() ) {
      screenInfo.getScreen().setFullScreenWindow( this );
    } else {
      setBounds( getGraphicsConfiguration().getBounds() );
    }
  }
  
  private void centerThisWindow() {
    if( screenInfo != null ) {
      SwingFunctions.center( this, screenInfo );
    } else {
      SwingFunctions.center( this );
    }
  }

  private boolean isCurrentlyFullscreen() {
    if( screenInfo != null ) {
      return screenInfo.getScreen().getFullScreenWindow() != null;
    } else {
      Dimension currentSize  = getSize();
      Dimension fullSize     = getGraphicsConfiguration().getBounds().getSize();
      boolean   result       = fullSize.width == currentSize.width;
      if( result ) {
        /** @todo [04-Jul-2018:KASI]   Figure out a way to add the taskbar/menubar to the height */
        result = Math.abs( fullSize.height - currentSize.height ) < 100;
      }
      return result;
    }
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
