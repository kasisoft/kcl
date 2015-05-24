package com.kasisoft.libs.common.workspace;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.util.*;
import com.kasisoft.libs.common.xml.adapters.*;

import java.util.*;

import java.net.*;

import java.awt.*;

import java.io.*;

import lombok.*;
import lombok.experimental.*;

/**
 * The Workspace allows to store various configuration information during the runtime.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class Workspace {

  static Workspace       instance = null;
  
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
    shutdown      = new ShutdownWorkspace( this );
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
  
//  /**
//   * Changes the value for a textual property.
//   * 
//   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
//   * @param value      The value which has to be saved. Maybe <code>null</code>.
//   * 
//   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
//   */
//  @Deprecated
//  public void setString( @NonNull String property, String value ) {
//    properties.setProperty( property, value );
//  }
//  
//  /**
//   * Returns a value associated with a specific property key.
//   * 
//   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
//   * @param defvalue   The default value to be used if there's no value yet. Maybe <code>null</code>.
//   * 
//   * @return   The value associated with the property. Maybe <code>null</code>.
//   * 
//   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
//   */
//  @Deprecated
//  public String getString( @NonNull String property, String defvalue ) {
//    return properties.getProperty( property, defvalue );
//  }

  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public String getString( @NonNull String property ) {
    return properties.getProperty( property, null );
  }

  /**
   * Changes the value for a Rectangle property.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param value      The value which has to be saved. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
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
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public Rectangle getRectangle( @NonNull String property, Rectangle defvalue ) {
    return get( Rectangle.class, property, defvalue );
  }

  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public Rectangle getRectangle( @NonNull String property ) {
    return get( Rectangle.class, property, null );
  }
  
  /**
   * Changes the value for a Boolean property.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param value      The value which has to be saved. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public void setBoolean( @NonNull String property, Boolean value ) {
    set( Boolean.class, property, value );
  }
  
  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param defvalue   The default value to be used if there's no value yet. Maybe <code>null</code>.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public Boolean getBoolean( @NonNull String property, Boolean defvalue ) {
    return get( Boolean.class, property, defvalue );
  }

  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public Boolean getBoolean( @NonNull String property ) {
    return get( Boolean.class, property, null );
  }
  
  /**
   * Changes the value for a Byte property.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param value      The value which has to be saved. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public void setByte( @NonNull String property, Byte value ) {
    set( Byte.class, property, value );
  }
  
  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param defvalue   The default value to be used if there's no value yet. Maybe <code>null</code>.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public Byte getByte( @NonNull String property, Byte defvalue ) {
    return get( Byte.class, property, defvalue );
  }

  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public Byte getByte( @NonNull String property ) {
    return get( Byte.class, property, null );
  }
  
  /**
   * Changes the value for a Color property.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param value      The value which has to be saved. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public void setColor( @NonNull String property, Color value ) {
    set( Color.class, property, value );
  }
  
  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param defvalue   The default value to be used if there's no value yet. Maybe <code>null</code>.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public Color getColor( @NonNull String property, Color defvalue ) {
    return get( Color.class, property, defvalue );
  }

  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public Color getColor( @NonNull String property ) {
    return get( Color.class, property, null );
  }

  /**
   * Changes the value for a Double property.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param value      The value which has to be saved. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public void setDouble( @NonNull String property, Double value ) {
    set( Double.class, property, value );
  }
  
  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param defvalue   The default value to be used if there's no value yet. Maybe <code>null</code>.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public Double getDouble( @NonNull String property, Double defvalue ) {
    return get( Double.class, property, defvalue );
  }

  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public Double getDouble( @NonNull String property ) {
    return get( Double.class, property, null );
  }

  /**
   * Changes the value for a Float property.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param value      The value which has to be saved. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public void setFloat( @NonNull String property, Float value ) {
    set( Float.class, property, value );
  }
  
  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param defvalue   The default value to be used if there's no value yet. Maybe <code>null</code>.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public Float getFloat( @NonNull String property, Float defvalue ) {
    return get( Float.class, property, defvalue );
  }

  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public Float getFloat( @NonNull String property ) {
    return get( Float.class, property, null );
  }

  /**
   * Changes the value for a Insets property.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param value      The value which has to be saved. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public void setInsets( @NonNull String property, Insets value ) {
    set( Insets.class, property, value );
  }
  
  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param defvalue   The default value to be used if there's no value yet. Maybe <code>null</code>.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public Insets getInsets( @NonNull String property, Insets defvalue ) {
    return get( Insets.class, property, defvalue );
  }

  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public Insets getInsets( @NonNull String property ) {
    return get( Insets.class, property, null );
  }

  /**
   * Changes the value for a Point property.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param value      The value which has to be saved. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public void setPoint( @NonNull String property, Point value ) {
    set( Point.class, property, value );
  }
  
  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param defvalue   The default value to be used if there's no value yet. Maybe <code>null</code>.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public Point getPoint( @NonNull String property, Point defvalue ) {
    return get( Point.class, property, defvalue );
  }

  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public Point getPoint( @NonNull String property ) {
    return get( Point.class, property, null );
  }
  
  /**
   * Changes the value for a Short property.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param value      The value which has to be saved. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public void setShort( @NonNull String property, Short value ) {
    set( Short.class, property, value );
  }
  
  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param defvalue   The default value to be used if there's no value yet. Maybe <code>null</code>.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public Short getShort( @NonNull String property, Short defvalue ) {
    return get( Short.class, property, defvalue );
  }

  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public Short getShort( @NonNull String property ) {
    return get( Short.class, property, null );
  }
  
  /**
   * Changes the value for a URI property.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param value      The value which has to be saved. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public void setURI( @NonNull String property, URI value ) {
    set( URI.class, property, value );
  }
  
  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param defvalue   The default value to be used if there's no value yet. Maybe <code>null</code>.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public URI getURI( @NonNull String property, URI defvalue ) {
    return get( URI.class, property, defvalue );
  }

  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public URI getURI( @NonNull String property ) {
    return get( URI.class, property, null );
  }
  
  /**
   * Changes the value for a URL property.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param value      The value which has to be saved. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public void setURL( @NonNull String property, URL value ) {
    set( URL.class, property, value );
  }
  
  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param defvalue   The default value to be used if there's no value yet. Maybe <code>null</code>.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public URL getURL( @NonNull String property, URL defvalue ) {
    return get( URL.class, property, defvalue );
  }

  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public URL getURL( @NonNull String property ) {
    return get( URL.class, property, null );
  }

  /**
   * Changes the value for a Version property. Qualifiers aren't supported for the values.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param value      The value which has to be saved. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public void setVersion( @NonNull String property, Version value ) {
    set( Version.class, property, value );
  }
  
  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param defvalue   The default value to be used if there's no value yet. Maybe <code>null</code>.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public Version getVersion( @NonNull String property, Version defvalue ) {
    return get( Version.class, property, defvalue );
  }

  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public Version getVersion( @NonNull String property ) {
    return get( Version.class, property, null );
  }

  /**
   * Changes the value for a File property.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param value      The value which has to be saved. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public void setFile( @NonNull String property, File value ) {
    set( File.class, property, value );
  }
  
  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * @param defvalue   The default value to be used if there's no value yet. Maybe <code>null</code>.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public File getFile( @NonNull String property, File defvalue ) {
    return get( File.class, property, defvalue );
  }

  /**
   * Returns a value associated with a specific property key.
   * 
   * @param property   The property key which has to be used. Neither <code>null</code> nor empty.
   * 
   * @return   The value associated with the property. Maybe <code>null</code>.
   * 
   * @deprecated [12-Apr-2015:KASI]   This function will be removed and will be replaced by the properties declarations.
   */
  @Deprecated
  public File getFile( @NonNull String property ) {
    return get( File.class, property, null );
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
    
    private Workspace   workspace;
    
    @Override
    public void run() {
      workspace.saveSettings();
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */