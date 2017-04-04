package com.kasisoft.libs.common.workspace;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.model.*;

import com.kasisoft.libs.common.base.*;

import com.kasisoft.libs.common.xml.adapters.*;

import javax.swing.*;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

import java.util.*;

import java.net.*;

import java.awt.*;

import java.io.*;

/**
 * The Workspace allows to store various configuration information during the runtime.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class Workspace {

  private static Workspace       instance = null;
  
  @Getter
  Properties                  properties;
  
  File                        settingsfile;
  boolean                     isnew;
  Map<Class<?>,TypeAdapter>   adapters;
  ShutdownWorkspace           shutdown;
  
  /**
   * Initialises this workspace used for temporary savings.
   */
  private Workspace() {
    settingsfile  = null;
    shutdown      = null;
    isnew         = false;
    properties    = new Properties();
    adapters      = new Hashtable<>();
    adapters.put( Version   . class, new VersionAdapter   ( null, true, false ) );
    adapters.put( Rectangle . class, new RectangleAdapter ( ":" ) );
    adapters.put( Boolean   . class, new BooleanAdapter   () );
    adapters.put( Byte      . class, new ByteAdapter      () );
    adapters.put( Color     . class, new ColorAdapter     () );
    adapters.put( Double    . class, new DoubleAdapter    () );
    adapters.put( File      . class, new FileAdapter      () );
    adapters.put( Float     . class, new FloatAdapter     () );
    adapters.put( Insets    . class, new InsetsAdapter    () );
    adapters.put( Point     . class, new PointAdapter     () );
    adapters.put( Short     . class, new ShortAdapter     () );
    adapters.put( String    . class, new StringAdapter    () );
    adapters.put( URI       . class, new URIAdapter       () );
    adapters.put( URL       . class, new URLAdapter       () );
  }
  
  /**
   * Initialises this workspace using the supplied settings File for persistency.
   * 
   * @param settings   The File where the settings have to be stored. Must be a writable File. Not <code>null</code>.
   * 
   * @throws FailureException   Loading failed for some reason.
   */
  private Workspace( @NonNull File settings ) throws FailureException {
    this();
    settingsfile = settings;
    isnew        = true;
    if( settingsfile.isFile() ) {
      isnew = false;
      loadSettings();
    }
    shutdown = new ShutdownWorkspace( this, null );
  }
  
  public synchronized void setShutdown( Consumer<Void> consumer ) {
    if( shutdown != null ) {
      shutdown.finalizer = consumer;
    }
  }
  
  public synchronized void configure( @NonNull Component component ) {
    iterate( component, this::loadWorkspacePersistent );
  }

  public synchronized void persist( @NonNull Component component ) {
    iterate( component, this::saveWorkspacePersistent );
  }

  private void loadWorkspacePersistent( WorkspacePersistent object ) {
    object.loadPersistentSettings();
  }

  private void saveWorkspacePersistent( WorkspacePersistent object ) {
    object.savePersistentSettings();
  }

  private void iterate( Component component, Consumer<WorkspacePersistent> action ) {
    if( component instanceof WorkspacePersistent ) {
      action.accept( (WorkspacePersistent) component );
    }
    Component[] children = getChildren( component );
    if( children != null ) {
      for( Component child : children ) {
        iterate( child, action );
      }
    }
  }
  
  private Component[] getChildren( Component parent ) {
    Component[] result = null;
    if( parent instanceof JMenu ) {
      result = ((JMenu) parent).getMenuComponents();
    } else if( parent instanceof Container ) {
      result = ((Container) parent).getComponents();
    }
    if( (result != null) && (result.length == 0) ) {
      result = null;
    }
    return result;
  }
  
  /**
   * Returns <code>true</code> in case the configuration file had to be created while setting up this instance.
   * 
   * @return   <code>true</code> <=> The configuration file had to be created.
   */
  public synchronized boolean isNew() {
    return isnew;
  }

  /**
   * Stores the current settings.
   * 
   * @throws FailureException   Saving the settings failed.
   */
  public synchronized void saveSettings() throws FailureException {
    try( Writer writer = Encoding.UTF8.openWriter( settingsfile ) ) {
      properties.store( writer, null );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
    isnew  = false;
  }
  
  /**
   * Loads the current settings.
   * 
   * @throws FailureException   Loading the settings failed.
   */
  private void loadSettings() throws FailureException {
    try( Reader reader = Encoding.UTF8.openReader( settingsfile ) ) {
      properties.load( reader );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
  }

  /**
   * Returns a Workspace instance used for the current runtime.
   * 
   * @return   A Workspace instance used for the current runtime. Not <code>null</code>.
   * 
   * @throws FailureException   Loading existing settings failed for some reason.
   */
  public static synchronized Workspace getInstance() {
    return getInstance( null, false );
  }
  
  /**
   * Returns a Workspace instance used for the current runtime. This function primarily makes use of the supplied
   * location <code>settings</code>. If this property also has not been set a dummy instance will be created.
   * 
   * @ks.note [19-Dec-2010:KASI]   Supplying a parameter won't have any effect if an instance already has been created 
   *                               so it's advisable to create a new instance as early as possible when launching the 
   *                               application.
   *                            
   * @param settings   The file where all settings will be written, to. Maybe <code>null</code>.
   * 
   * @return   A Workspace instance used for the current runtime. Not <code>null</code>.
   * 
   * @throws FailureException   Loading existing settings failed for some reason.
   */
  public static synchronized Workspace getInstance( File settings ) {
    return getInstance( settings, false );
  }
  
  /**
   * Returns a Workspace instance used for the current runtime. This function primarily makes use of the supplied
   * location <code>settings</code>. If this property also has not been set a dummy instance will be created.
   * 
   * @param settings   The file where all settings will be written, to. Maybe <code>null</code>.
   * @param force      <code>true</code> <=> Enforces to recreate the instance. 
   * 
   * @return   A Workspace instance used for the current runtime. Not <code>null</code>.
   * 
   * @throws FailureException   Loading existing settings failed for some reason.
   */
  public static synchronized Workspace getInstance( File settings, boolean force ) {
    
    if( (instance == null) || force ) {
      
      if( (instance != null) && (instance.shutdown != null) ) {
        Runtime.getRuntime().removeShutdownHook( instance.shutdown );
        instance.saveSettings();
      }
      
      File appfile = settings;
      if( appfile == null ) {
        // no settings available, so we're providing a dummy instance instead
        instance = new Workspace();
      } else {
        instance  = new Workspace( appfile );
        Runtime.getRuntime().addShutdownHook( instance.shutdown );
      }
      
    }
    
    return instance;
    
  }
  
  /**
   * Shutdown hook for this Workspace instance.
   */
  @AllArgsConstructor
  private class ShutdownWorkspace extends Thread {
    
    Workspace       workspace;
    Consumer<Void>  finalizer;
    
    @Override
    public void run() {
      if( finalizer != null ) {
        finalizer.accept( null );
      }
      workspace.saveSettings();
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */