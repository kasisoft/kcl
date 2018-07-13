package com.kasisoft.libs.common.base;

import java.util.*;

import lombok.experimental.*;

import lombok.*;

/**
 * A helper which allows to change/update the behaviour of the library functions. 
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LibConfig {

  public static final LibConfig INSTANCE = new LibConfig();
  
  private static final String[] DEFAULT_TRUE_VALUES       = { "true", "ja", "yes", "on","ein", "an", "1", "-1" };
  private static final String[] DEFAULT_FALSE_VALUES      = { "false", "nein", "no", "off","aus", "0" };
  private static final String   DEFAULT_VAR_FORMAT        = "${%s}";
  private static final String[] DEFAULT_ARCHIVE_PREFIXES  = { "jar:", "ear:", "zip:", "war:" };

  Set<String>             trueValues;
  Set<String>             falseValues;
  Set<String>             archivePrefixes;
  String                  defaultVarFormat;
  
  private LibConfig() {
    trueValues        ( true, DEFAULT_TRUE_VALUES      );
    falseValues       ( true, DEFAULT_FALSE_VALUES     );
    archivePrefixes   ( true, DEFAULT_ARCHIVE_PREFIXES );
    defaultVarFormat  ( null );
  }

  public synchronized LibConfig trueValues( boolean add, String ... values ) {
    trueValues = Collections.unmodifiableSet( booleanValues( add, DEFAULT_TRUE_VALUES, values ) );
    return this;
  }

  public synchronized LibConfig falseValues( boolean add, String ... values ) {
    falseValues = Collections.unmodifiableSet( booleanValues( add, DEFAULT_FALSE_VALUES, values ) );
    return this;
  }

  public synchronized LibConfig archivePrefixes( boolean add, String ... values ) {
    archivePrefixes = Collections.unmodifiableSet( booleanValues( add, DEFAULT_ARCHIVE_PREFIXES, values ) );
    return this;
  }

  public synchronized LibConfig defaultVarFormat( String format ) {
    defaultVarFormat = format != null ? format : DEFAULT_VAR_FORMAT;
    return this;
  }

  private Set<String> booleanValues( boolean add, String[] defaultValues, String[] additionalValues ) {
    Set<String> result = new HashSet<>();
    if( add ) {
      for( String str : defaultValues ) {
        result.add( str );
      }
    }
    if( additionalValues != null ) {
      for( String str : additionalValues ) {
        result.add( str );
      }
    }
    return result;
  }
  
  public static Set<String> cfgTrueValues() {
    synchronized( INSTANCE ) {
      return INSTANCE.trueValues;
    }
  }

  public static Set<String> cfgFalseValues() {
    synchronized( INSTANCE ) {
      return INSTANCE.falseValues;
    }
  }

  public static String cfgDefaultVarFormat() {
    synchronized( INSTANCE ) {
      return INSTANCE.defaultVarFormat;
    }
  }

  public static Set<String> cfgArchivePrefixes() {
    synchronized( INSTANCE ) {
      return INSTANCE.archivePrefixes;
    }
  }

} /* ENDCLASS */
