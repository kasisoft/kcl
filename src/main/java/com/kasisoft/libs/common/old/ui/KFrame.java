package com.kasisoft.libs.common.old.ui;

import com.kasisoft.libs.common.converters.RectangleAdapter;
import com.kasisoft.libs.common.old.config.SimpleProperty;
import com.kasisoft.libs.common.old.workspace.Workspace;
import com.kasisoft.libs.common.old.workspace.WorkspacePersistent;
import com.kasisoft.libs.common.text.StringFunctions;
import com.kasisoft.libs.common.types.ScreenInfo;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import java.util.HashMap;
import java.util.Map;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.awt.Rectangle;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;

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
  Rectangle                   lastBounds; 
  ScreenInfo                  screenInfo;
  Map<String, Runnable>       actions;
  
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
    initialBounds   = null;
    property        = StringFunctions.cleanup( wsprop );
    if( property != null ) {
      propertyBounds = new SimpleProperty<>( String.format( "%s.bounds", property ), new RectangleAdapter() );
    }
    if( ! defer ) {
      init();
    }
    registerAction( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE , 0 ), this::closeFrame );
  }  
  
  protected void setInitialBounds( Rectangle bounds ) {
    initialBounds = bounds;
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
    Workspace.getInstance().addShutdown( () -> Workspace.getInstance().persist( KFrame.this ) );
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
  public void loadPersistentSettings() {
    if( property != null ) {
      initialBounds = propertyBounds.getValue( Workspace.getInstance().getProperties() );
    }
  }

  @Override
  public void savePersistentSettings() {
    if( property != null ) {
      Rectangle bounds = lastBounds != null ? lastBounds : getBounds();
      propertyBounds.setValue( Workspace.getInstance().getProperties(), bounds );
    }
  }
  
  @Override
  public void setVisible( boolean enable ) {
    
    if( enable == isVisible() ) {
      return;
    }

    if( ! enable ) {
      lastBounds = getBounds();
    }
    
    if( initialBounds != null ) {
      setBounds( transformBounds( initialBounds ) );
    } else {
      centerThisWindow();
    }
    
    initialBounds = getBounds();
    
    super.setVisible( enable );
  }

  public Rectangle transformBounds( Rectangle rectangle ) {
    if( screenInfo != null ) {
      Rectangle sinfo = screenInfo.getGraphicsConfiguration().getBounds();
      return new Rectangle( rectangle.x + sinfo.x, rectangle.y + sinfo.y, rectangle.width, rectangle.height ); 
    } else {
      return rectangle;
    }
  }
  
  private void centerThisWindow() {
    if( screenInfo != null ) {
      SwingFunctions.center( this, screenInfo );
    } else {
      SwingFunctions.center( this );
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
