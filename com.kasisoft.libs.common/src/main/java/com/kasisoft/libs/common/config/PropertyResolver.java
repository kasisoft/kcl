package com.kasisoft.libs.common.config;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.base.*;

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.text.*;

import lombok.experimental.*;

import lombok.*;

import java.util.regex.*;

import java.util.*;

import java.net.*;

import java.nio.file.*;

import java.io.*;

/**
 * Helper class which allows to resolve properties from the classpath.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PropertyResolver {

  Map<Pattern, String>   substitutions;
  ClassLoader            classloader;
  String                 format;
  Map<String, String>    data;
  Map<String, String>    resolveddata;

  /**
   * Creates a new resolver using the system ClassLoader.
   */
  public PropertyResolver() {
    this( null );
  }
  
  /**
   * Creates a new resolver using the supplied ClassLoader.
   * 
   * @param cl   The ClassLoader which shall be used. <code>null</code> means to use the system ClassLoader.
   */
  public PropertyResolver( ClassLoader cl ) {
    classloader = cl;
    if( classloader == null ) {
      classloader = ClassLoader.getSystemClassLoader();
    }
    substitutions = new Hashtable<>();
    format        = "${%s}";
    data          = new HashMap<>();
    resolveddata  = new HashMap<>();
  }
  
  /**
   * Clears the content of this instance.
   */
  public synchronized void clear() {
    data.clear();
    resolveddata.clear();
  }
  
  /**
   * Returns the number of properties available.
   * 
   * @return   The number of properties available.
   */
  public int getSize() {
    return data.size();
  }

  /**
   * Returns a list of all registered property names.
   * 
   * @return   A list of all registered property names. Not <code>null</code>.
   */
  public synchronized String[] getPropertyNames() {
    return data.keySet().toArray( new String[ data.size() ] );
  }

  /**
   * Returns the property values associated with the supplied key.
   * 
   * @param key   The key used to identify the property. Neither <code>null</code> nor empty.
   * 
   * @return   The property value. Either <code>null</code> or not empty.
   */
  public synchronized String getProperty( @NonNull String key ) {
    if( resolveddata.containsKey( key ) ) {
      return resolveddata.get( key );
    }
    String result = resolveProperty( data.get( key ) );
    resolveddata.put( key, result );
    return result;
  }
  
  /**
   * Returns the property values associated with the supplied key.
   * 
   * @param key        The key used to identify the property. Neither <code>null</code> nor empty.
   * @param defvalue   The default value to be used if the property value is <code>null</code>.
   * 
   * @return   The property value. Either <code>null</code> or not empty.
   */
  public synchronized String getProperty( @NonNull String key, String defvalue ) {
    String result = getProperty( key );
    if( result == null ) {
      result = defvalue;
    }
    return result;
  }

  /**
   * Changes the value of a property.
   * 
   * @param key     The key used to identify the property. Neither <code>null</code> nor empty.
   * @param value   The new value which will be set. Maybe <code>null</code>.
   */
  public synchronized void setProperty( @NonNull String key, String value ) {
    if( resolveddata.containsKey( key ) ) {
      resolveddata.remove( key );
    }
    data.put( key, value );
  }

  /**
   * Changes the format which is used to substitute variables.
   *  
   * @param newformat   The formatting string (f.e. "${%s}"). Neither <code>null</code> nor empty.
   * 
   * @return   this
   */
  public synchronized PropertyResolver withFormat( @NonNull String newformat ) {
    format = newformat;
    resolveddata.clear();
    return this;
  }

  /**
   * Enables system substitutions.
   * 
   * @return   this
   */
  public synchronized PropertyResolver withSystemSubstitutions() {
    return withSubstitutions( System.getProperties() );
  }

  /**
   * Enables substitutions with the supplied properties.
   * 
   * @param properties   The properties that will be used for substitutions. Not <code>null</code>.
   * 
   * @return   this
   */
  public synchronized PropertyResolver withSubstitutions( @NonNull Properties properties ) {
    substitutions = ConfigurationHelper.quoteKeys( ConfigurationHelper.createReplacementMap( properties, format, null ) );
    resolveddata.clear();
    return this;
  }

  /**
   * Enables substitutions with the supplied properties.
   * 
   * @param properties   The properties that will be used for substitutions. Not <code>null</code>.
   * 
   * @return   this
   */
  public synchronized PropertyResolver withSubstitutions( @NonNull Map<String, String> properties ) {
    substitutions = ConfigurationHelper.quoteKeys( ConfigurationHelper.createReplacementMap( properties, format, null ) );
    resolveddata.clear();
    return this;
  }

  /**
   * Adds a single substitution to this instance.
   * 
   * @param key           The key to be used for the substitutions. Neither <code>null</code> nor empty.
   * @param replacement   The replacement string. Not <code>null</code>. 
   * 
   * @return   this
   */
  public synchronized PropertyResolver withSubstitution( @NonNull String key, @NonNull String replacement ) {
    substitutions.put( Pattern.compile( Pattern.quote( String.format( format, key ) ) ), replacement );
    return this;
  }
  
  /**
   * Loads all properties located in the resources of the supplied path.
   * 
   * @param resourcepath   A classpath location. Neither <code>null</code> nor empty.
   * 
   * @return   this
   * 
   * @throws IOException   Loading failed for some reason.
   */
  public synchronized PropertyResolver load( @NonNull String resourcepath ) throws IOException {
    List<URL>        postponed = new ArrayList<>();
    Enumeration<URL> resources = classloader.getResources( resourcepath );
    while( resources.hasMoreElements() ) {
      URL    resource = resources.nextElement();
      String external = resource.toExternalForm();
      if( external.indexOf( "/test/" ) != -1 ) {
        postponed.add( resource );
      } else {
        load( resource );
      }
    }
    for( URL resource : postponed ) {
      load( resource );
    }
    return this;
  }

  
  /**
   * Loads all properties located in the supplied respource.
   * 
   * @param resource   The location of the properties file. Not <code>null</code>.
   * 
   * @return   this
   * 
   * @throws FailureException   Loading failed for some reason.
   */
  public synchronized PropertyResolver load( @NonNull URL resource ) {
    IoFunctions.forInputStreamDo( resource, this::loadSetting );
    return this;
  }

  /**
   * Loads all properties located in the supplied file.
   * 
   * @param file   The location of the properties file. Not <code>null</code>.
   * 
   * @return   this
   * 
   * @throws FailureException   Loading failed for some reason.
   */
  public synchronized PropertyResolver load( @NonNull Path file ) {
    IoFunctions.forInputStreamDo( file, this::loadSetting );
    return this;
  }

  /**
   * Loads all properties located in the supplied file.
   * 
   * @param file   The location of the properties file. Not <code>null</code>.
   * 
   * @return   this
   * 
   * @throws FailureException   Loading failed for some reason.
   */
  public synchronized PropertyResolver load( @NonNull File file ) {
    IoFunctions.forInputStreamDo( file, this::loadSetting );
    return this;
  }

  /**
   * Loads all properties located in the supplied file.
   * 
   * @param instream   The InputStream that provides the properties to be loaded. Not <code>null</code>.
   * 
   * @return   this
   * 
   * @throws FailureException   Loading failed for some reason.
   */
  public synchronized PropertyResolver load( @NonNull InputStream instream ) {
    loadSetting( instream );
    return this;
  }

  /**
   * Imports the properties from the supplied resource.
   * 
   * @param instream   The InputStream that provides the properties to be loaded. Not <code>null</code>.
   * 
   * @throws FailureException   Loading failed for some reason.
   */
  private void loadSetting( InputStream instream ) {
    try {
      Properties  newprops = new Properties();
      try( Reader reader = Encoding.UTF8.openReader( instream ) ) {
        newprops.load( reader );
      }
      putProperties( newprops );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
  }

  /**
   * Loads all properties located in the supplied properties object.
   * 
   * @param properties   The properties that will be imported. Not <code>null</code>.
   * 
   * @return   this
   */
  public synchronized PropertyResolver load( @NonNull Properties properties ) {
    putProperties( properties );
    return this;
  }

  /**
   * Loads all properties located in the supplied properties object.
   * 
   * @param properties   The properties that will be imported. Not <code>null</code>.
   * 
   * @return   this
   */
  public synchronized PropertyResolver load( @NonNull Map<String,String> properties ) {
    putProperties( properties );
    return this;
  }

  /**
   * Loads all properties from the supplied instance.
   * 
   * @param properties   The properties that shall be loaded. Not <code>null</code>.
   */
  private void putProperties( Properties properties ) {
    Enumeration<String> names = (Enumeration<String>) properties.propertyNames();
    while( names.hasMoreElements() ) {
      String name   = names.nextElement();
      String value  = properties.getProperty( name );
      data.put( name, value );
      resolveddata.remove( name );
    }
  }

  /**
   * Loads all properties from the supplied instance.
   * 
   * @param properties   The properties that shall be loaded. Not <code>null</code>.
   */
  private void putProperties( Map<String,String> properties ) {
    for( Map.Entry<String,String> entry : properties.entrySet() ) {
      data.put( entry.getKey(), entry.getValue() );
      resolveddata.remove( entry.getKey() );
    }
  }

  /**
   * Resolves the supplied property value while substituting all variables.
   * 
   * @param value   The value that needs to be resolved. Maybe <code>null</code>.
   * 
   * @return   The resolved value. Maybe <code>null</code>.
   */
  private String resolveProperty( String value ) {
    if( (value != null) && (value.length() > 0) && (! substitutions.isEmpty()) ) {
      for( Map.Entry<Pattern,String> replacement : substitutions.entrySet() ) {
        Matcher matcher = replacement.getKey().matcher( value );
        value = replaceAll( matcher, value, replacement.getValue() );
      }
    }
    return StringFunctions.cleanup( value );
  }
  
  /**
   * Performs a plain replacement of all occurrences.
   * 
   * @param matcher       The regex matcher to be used for the replacement. Not <code>null</code>.
   * @param value         The current value of the property. Not <code>null</code>.
   * @param replacement   The replacing String. Not <code>null</code>.
   * 
   * @return   The substituted value. Not <code>null</code>.
   */
  private String replaceAll( Matcher matcher, String value, String replacement ) {
    StringBuilder builder   = new StringBuilder();
    int           laststart = 0;
    while( matcher.find() ) {
      int start = matcher.start();
      int end   = matcher.end();
      if( start > laststart ) {
        builder.append( value.substring( laststart, start ) );
      }
      builder.append( replacement );
      laststart = end;
    }
    
    if( laststart < value.length() ) {
      builder.append( value.substring( laststart ) );
    }
    return builder.toString();
  }
  
} /* ENDCLASS */
