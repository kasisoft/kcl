/**
 * Name........: Workspace
 * Description.: The Workspace allows to store various configuration information during the runtime.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.workspace;

import com.kasisoft.lgpl.libs.common.constants.*;

import com.kasisoft.lgpl.libs.common.util.*;

import com.kasisoft.lgpl.libs.common.xml.adapters.*;

import com.kasisoft.lgpl.libs.common.base.*;
import com.kasisoft.lgpl.tools.diagnostic.*;

import javax.xml.bind.annotation.adapters.*;

import java.util.*;

import java.io.*;

import java.awt.*;

/**
 * The Workspace allows to store various configuration information during the runtime.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public final class Workspace {

  private static Workspace       instance = null;
  
  private File                       settingsfile;
  private ExtProperties              properties;
  private boolean                    isnew;
  private Map<Class<?>,XmlAdapter>   adapters;
  
  /**
   * Initialises this workspace used for temporary savings.
   */
  private Workspace() {
    settingsfile  = null;
    isnew         = false;
    properties    = new ExtProperties();
    adapters      = new Hashtable<Class<?>,XmlAdapter>();
    adapters.put( Rectangle.class, new RectangleAdapter( ":" ) );
  }
  
  /**
   * Initialises this workspace using the supplied settings File for persistency.
   * 
   * @param settings   The File where the settings have to be stored. Must be a writable File.
   *                   Not <code>null</code>.
   * 
   * @throws FailureException   Loading failed for some reason.
   */
  private Workspace( @KFile(name="settings", right=KFile.Right.Write) File settings ) throws FailureException {
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
   * Returns <code>true</code> in case the configuration file had to be created while setting
   * up this instance.
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
    properties.store( settingsfile, Encoding.UTF8 );
    isnew  = false;
  }
  
  /**
   * Loads the current settings.
   * 
   * @throws FailureException   Loading the settings failed.
   */
  private void loadSettings() throws FailureException {
    properties.load( settingsfile, Encoding.UTF8 );
  }
  
  /**
   * Changes the value for a textual property.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param value      The value which has to be saved. Maybe <code>null</code>.
   */
  public void setString( @KNotEmpty(name="property") String property, String value ) {
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
  public String getString( @KNotEmpty(name="property") String property, String defvalue ) {
    return properties.getProperty( property, defvalue );
  }

  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   */
  public String getString( @KNotEmpty(name="property") String property ) {
    return properties.getProperty( property, null );
  }

  /**
   * Changes the value for a Rectangle property.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param value      The value which has to be saved. Maybe <code>null</code>.
   */
  public void setRectangle( @KNotEmpty(name="property") String property, Rectangle value ) {
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
  public Rectangle getRectangle( @KNotEmpty(name="property") String property, Rectangle defvalue ) {
    return get( Rectangle.class, property, defvalue );
  }

  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   */
  public Rectangle getRectangle( @KNotEmpty(name="property") String property ) {
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
  private <T> T get( Class<T> type, String property, T defvalue ) {
    XmlAdapter<String,T> adapter = adapters.get( type );
    try {
      String result = properties.getProperty( property );
      if( result != null ) {
        return adapter.unmarshal( result );
      }
    } catch( Exception ex ) {
      if( properties.getErrorHandler() != null ) {
        properties.getErrorHandler().failure( this, ex.getMessage(), ex );
      }
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
  private <T> void set( Class<T> type, String property, T value ) {
    XmlAdapter<String,T> adapter = adapters.get( type );
    try {
      String strvalue = adapter.marshal( value );
      properties.setProperty( property, strvalue );
    } catch( Exception ex ) {
      if( properties.getErrorHandler() != null ) {
        properties.getErrorHandler().failure( this, ex.getMessage(), ex );
      }
    }
  }

  /**
   * Returns a Workspace instance used for the current runtime. If the property
   * {@link CommonLibraryConstants#PROP_APPLICATIONFILE} has not been set, it will return
   * a dummy instance always relying on default values.
   * 
   * @return   A Workspace instance used for the current runtime. Not <code>null</code>.
   * 
   * @throws FailureException   Loading existing settings failed for some reason.
   */
  public static final synchronized Workspace getInstance() {
    if( instance == null ) {
      File appfile = CommonProperty.Application.getValue();
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
    
    /**
     * {@inheritDoc}
     */
    public void run() {
      Workspace.this.saveSettings();
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */