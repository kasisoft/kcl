package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.config.*;
import com.kasisoft.libs.common.constants.*;

import java.util.regex.*;

import java.util.*;

import java.net.*;

import java.io.*;

import lombok.*;

public class PropertyResolver {

  private Map<Pattern,String>   substitutions;
  private ClassLoader           classloader;
  private String                format;
  private Map<String,String>    data;
  private Map<String,String>    resolveddata;
  
  public PropertyResolver() {
    this( null );
  }

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

  public synchronized String[] getPropertyNames() {
    return data.keySet().toArray( new String[ data.size() ] );
  }
  
  public synchronized String getProperty( @NonNull String key ) {
    if( resolveddata.containsKey( key ) ) {
      return resolveddata.get( key );
    }
    String result = resolveProperty( data.get( key ) );
    resolveddata.put( key, result );
    return result;
  }
  
  public synchronized String getProperty( @NonNull String key, String defvalue ) {
    String result = getProperty( key );
    if( result == null ) {
      result = defvalue;
    }
    return result;
  }
  
  public synchronized void setProperty( @NonNull String key, String value ) {
    if( resolveddata.containsKey( key ) ) {
      resolveddata.remove( key );
    }
    data.put( key, value );
  }
  
  public synchronized PropertyResolver withFormat( @NonNull String newformat ) {
    format = newformat;
    resolveddata.clear();
    return this;
  }
  
  public synchronized PropertyResolver withSystemSubstitutions() {
    return withSubstitutions( System.getProperties() );
  }

  public synchronized PropertyResolver withSubstitutions( @NonNull Properties properties ) {
    substitutions = ConfigurationHelper.quoteKeys( ConfigurationHelper.createReplacementMap( properties, format, null ) );
    resolveddata.clear();
    return this;
  }

  public synchronized PropertyResolver withSubstitutions( @NonNull Map<String,String> properties ) {
    substitutions = ConfigurationHelper.quoteKeys( ConfigurationHelper.createReplacementMap( properties, format, null ) );
    resolveddata.clear();
    return this;
  }

  public synchronized PropertyResolver load( @NonNull String resourcepath ) throws IOException {
    Enumeration<URL> resources = classloader.getResources( resourcepath );
    while( resources.hasMoreElements() ) {
      URL resource = resources.nextElement();
      loadSetting( resource );
    }
    return this;
  }
  
  public synchronized PropertyResolver load( @NonNull File file ) throws IOException {
    loadSetting( file.toURI().toURL() );
    return this;
  }

  public synchronized PropertyResolver load( @NonNull Properties properties ) {
    putResolvedProperties( properties );
    return this;
  }

  private void loadSetting( URL resource ) throws IOException {
    Properties  newprops = new Properties();
    try( Reader reader   = Encoding.UTF8.openReader( resource ) ) {
      newprops.load( reader );
    }
    putResolvedProperties( newprops );
  }
  
  private void putResolvedProperties( Properties properties ) {
    Enumeration<String> names = (Enumeration<String>) properties.propertyNames();
    while( names.hasMoreElements() ) {
      String name = names.nextElement();
      data.put( name, properties.getProperty( name ) );
    }
  }

  private String resolveProperty( String value ) {
    if( (value != null) && (value.length() > 0) ) {
      for( Map.Entry<Pattern,String> replacement : substitutions.entrySet() ) {
        Matcher matcher = replacement.getKey().matcher( value );
        value = matcher.replaceAll( replacement.getValue() );
      }
    }
    return value;
  }
  
} /* ENDCLASS */
