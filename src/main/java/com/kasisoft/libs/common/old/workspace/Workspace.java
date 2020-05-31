package com.kasisoft.libs.common.old.workspace;

import com.kasisoft.libs.common.KclException;
import com.kasisoft.libs.common.old.constants.Encoding;
import com.kasisoft.libs.common.old.model.Version;
import com.kasisoft.libs.common.old.ui.SwingFunctions;
import com.kasisoft.libs.common.old.xml.adapters.BooleanAdapter;
import com.kasisoft.libs.common.old.xml.adapters.ByteAdapter;
import com.kasisoft.libs.common.old.xml.adapters.ColorAdapter;
import com.kasisoft.libs.common.old.xml.adapters.DoubleAdapter;
import com.kasisoft.libs.common.old.xml.adapters.FileAdapter;
import com.kasisoft.libs.common.old.xml.adapters.FloatAdapter;
import com.kasisoft.libs.common.old.xml.adapters.InsetsAdapter;
import com.kasisoft.libs.common.old.xml.adapters.PointAdapter;
import com.kasisoft.libs.common.old.xml.adapters.RectangleAdapter;
import com.kasisoft.libs.common.old.xml.adapters.ShortAdapter;
import com.kasisoft.libs.common.old.xml.adapters.StringAdapter;
import com.kasisoft.libs.common.old.xml.adapters.TypeAdapter;
import com.kasisoft.libs.common.old.xml.adapters.URIAdapter;
import com.kasisoft.libs.common.old.xml.adapters.URLAdapter;
import com.kasisoft.libs.common.old.xml.adapters.VersionAdapter;

import java.util.function.Consumer;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import java.net.URI;
import java.net.URL;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

import java.nio.file.Path;

import java.io.File;
import java.io.Reader;
import java.io.Writer;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

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
   * @throws KclException   Loading failed for some reason.
   */
  private Workspace( @NonNull File settings ) {
    this();
    settingsfile = settings;
    isnew        = true;
    if( settingsfile.isFile() ) {
      isnew = false;
      loadSettings();
    }
    shutdown = new ShutdownWorkspace( this, new HashSet<>() );
  }
  
  public synchronized void addShutdown( Runnable finalizer ) {
    if( shutdown != null ) {
      shutdown.finalizer.add( finalizer );
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
    SwingFunctions.forComponentTreeDo( component, WorkspacePersistent.class::isInstance, $ -> action.accept( (WorkspacePersistent) $) );
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
   * @throws KclException   Saving the settings failed.
   */
  public synchronized void saveSettings() {
    try( Writer writer = Encoding.UTF8.openWriter( settingsfile ) ) {
      properties.store( writer, null );
    } catch( Exception ex ) {
      throw KclException.wrap( ex );
    }
    isnew  = false;
  }
  
  /**
   * Loads the current settings.
   * 
   * @throws KclException   Loading the settings failed.
   */
  private void loadSettings() {
    try( Reader reader = Encoding.UTF8.openReader( settingsfile ) ) {
      properties.load( reader );
    } catch( Exception ex ) {
      throw KclException.wrap( ex );
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
    return getInstance( (File) null, false );
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
  public static synchronized Workspace getInstance( Path settings ) {
    return getInstance( settings.toFile(), false );
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
  public static synchronized Workspace getInstance( Path settings, boolean force ) {
    return getInstance( settings.toFile(), force );
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
    Set<Runnable>   finalizer;
    
    @Override
    public void run() {
      if( (finalizer != null) && (!finalizer.isEmpty()) ) {
        finalizer.stream().forEach( Runnable::run );
      }
      workspace.saveSettings();
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */