package com.kasisoft.libs.common.workspace;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.xml.adapters.*;

import java.util.*;

import java.awt.*;

import java.io.*;

import lombok.*;

/**
 * The Workspace allows to store various configuration information during the runtime.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public final class Workspace {

  private static Workspace       instance = null;
  
  private File                        settingsfile;
  private Properties                  properties;
  private boolean                     isnew;
  private Map<Class<?>,TypeAdapter>   adapters;
  
  /**
   * Initialises this workspace used for temporary savings.
   */
  private Workspace() {
    settingsfile  = null;
    isnew         = false;
    properties    = new Properties();
    adapters      = new Hashtable<>();
    adapters.put( Rectangle.class, new RectangleAdapter( ":" ) );
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
    Runtime.getRuntime().addShutdownHook( new ShutdownWorkspace() );
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
      throw FailureException.newFailureException( FailureCode.IO, ex );
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
      throw FailureException.newFailureException( FailureCode.IO, ex );
    }
  }
  
  /**
   * Changes the value for a textual property.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param value      The value which has to be saved. Maybe <code>null</code>.
   */
  public void setString( @NonNull String property, String value ) {
    properties.setProperty( property, value );
  }
  
  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param defvalue   The default value to be used if there's no value yet. Maybe <code>null</code>.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   */
  public String getString( @NonNull String property, String defvalue ) {
    return properties.getProperty( property, defvalue );
  }

  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   */
  public String getString( @NonNull String property ) {
    return properties.getProperty( property, null );
  }

  /**
   * Changes the value for a Rectangle property.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param value      The value which has to be saved. Maybe <code>null</code>.
   */
  public void setRectangle( @NonNull String property, Rectangle value ) {
    set( Rectangle.class, property, value );
  }
  
  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param defvalue   The default value to be used if there's no value yet. Maybe <code>null</code>.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   */
  public Rectangle getRectangle( @NonNull String property, Rectangle defvalue ) {
    return get( Rectangle.class, property, defvalue );
  }

  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   */
  public Rectangle getRectangle( @NonNull String property ) {
    return get( Rectangle.class, property, null );
  }
  
  /**
   * Returns a typed value of a property. 
   *  
   * @param type       The class type used to be stored. Not <code>null</code>.
   * @param property   The key of the property. Neither <code>null</code> nor empty.
   * @param defvalue   The default value which will be returned if there's no value.
   * 
   * @return   The typed value. Maybe <code>null</code>.
   */
  private <T> T get( @NonNull Class<T> type, @NonNull String property, T defvalue ) {
    TypeAdapter<String,T> adapter = adapters.get( type );
    try {
      String result = properties.getProperty( property );
      if( result != null ) {
        return adapter.unmarshal( result );
      }
    } catch( Exception ex ) {
      // ignored
    }
    return defvalue;
  }
  
  /**
   * Changes the property value depending on the type.
   * 
   * @param type       The class type used to be stored. Not <code>null</code>.
   * @param property   The key of the property. Neither <code>null</code> nor empty.
   * @param value      The value which has to be set.
   */
  private <T> void set( @NonNull Class<T> type, @NonNull String property, T value ) {
    TypeAdapter<String,T> adapter = adapters.get( type );
    try {
      String strvalue = adapter.marshal( value );
      properties.setProperty( property, strvalue );
    } catch( Exception ex ) {
      // ignored
    }
  }

  /**
   * Returns a Workspace instance used for the current runtime. If the property 
   * {@link "CommonLibraryConstants#PROP_APPLICATIONFILE"} has not been set, it will return a dummy instance always 
   * relying on default values.
   * 
   * @return   A Workspace instance used for the current runtime. Not <code>null</code>.
   * 
   * @throws FailureException   Loading existing settings failed for some reason.
   */
  public static synchronized Workspace getInstance() {
    return getInstance( null );
  }
  
  /**
   * Returns a Workspace instance used for the current runtime. This function primarily makes use of the supplied
   * location <code>settings</code>. If this parameter is <code>null</code> the property
   * {@link "CommonLibraryConstants#PROP_APPLICATIONFILE"} will be used. If this property also has not been set a dummy
   * instance will be created.
   * 
   * @ks.note [19-Dec-2010:KASI]   Supplying a parameter won't have any effect if an instance already has been created so
   *                               it's advisable to create a new instance as early as possible when launching the 
   *                               application.
   *                            
   * @param settings   The file where all settings will be written, to. Maybe <code>null</code>.
   * 
   * @return   A Workspace instance used for the current runtime. Not <code>null</code>.
   * 
   * @throws FailureException   Loading existing settings failed for some reason.
   */
  public static synchronized Workspace getInstance( File settings ) {
    if( instance == null ) {
      File appfile = settings;
      if( appfile == null ) {
        // no settings available, so we're providing a dummy instance instead
        instance = new Workspace();
      } else {
        instance  = new Workspace( appfile );
      }
    }
    return instance;
  }
  
  /**
   * Shutdown hook for this Workspace instance.
   */
  private class ShutdownWorkspace extends Thread {
    
    @Override
    public void run() {
      Workspace.this.saveSettings();
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */