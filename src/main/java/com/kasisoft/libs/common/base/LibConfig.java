package com.kasisoft.libs.common.base;

import com.kasisoft.libs.common.constants.*;

import java.util.*;

import java.nio.file.*;

import lombok.experimental.*;

import lombok.*;

/**
 * A helper which allows to change/update the behaviour of the library functions. 
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LibConfig {

  private static final String[] DEFAULT_TRUE_VALUES;
  private static final String[] DEFAULT_FALSE_VALUES;
  private static final String   DEFAULT_VAR_FORMAT;
  private static final String[] DEFAULT_ARCHIVE_PREFIXES;
  
  public static final LibConfig INSTANCE;
  
  static {
    
    DEFAULT_TRUE_VALUES         = new String[] { "true", "ja", "yes", "on","ein", "an", "1", "-1" };
    DEFAULT_FALSE_VALUES        = new String[] { "false", "nein", "no", "off","aus", "0" };
    DEFAULT_VAR_FORMAT          = "${%s}";
    DEFAULT_ARCHIVE_PREFIXES    = new String[] { "jar:", "ear:", "zip:", "war:" };
    
    INSTANCE                    = new LibConfig();
    
  }

  Set<String>                     trueValues;
  Set<String>                     falseValues;
  Set<String>                     archivePrefixes;
  String                          defaultVarFormat;
  int                             bufferSize;
  int                             ioRetries;
  Path                            tempDir;
  
  private LibConfig() {
    trueValues        ( true, DEFAULT_TRUE_VALUES      );
    falseValues       ( true, DEFAULT_FALSE_VALUES     );
    archivePrefixes   ( true, DEFAULT_ARCHIVE_PREFIXES );
    defaultVarFormat  ( null );
    bufferSize        ( 0 );
    ioRetries         ( 5 );
    tempDir           ( null );
  }

  public synchronized LibConfig tempDir( Path dir ) {
    if( dir != null ) {
      tempDir = dir;
      if( ! Files.isDirectory( tempDir ) ) {
        try {
          Files.createDirectories( tempDir );
        } catch( Exception ex ) {
          throw KclException.wrap( ex );
        }
      }
    } else {
      tempDir = SysProperty.TempDir.getValue().toPath();
    }
    return this;
  }

  public synchronized LibConfig ioRetries( int retries ) {
    ioRetries = retries > 0 ? retries : 0;
    return this;
  }

  public synchronized LibConfig bufferSize( int size ) {
    bufferSize = size > 0 ? size : 16384;
    return this;
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

  public static int cfgBufferSize() {
    synchronized( INSTANCE ) {
      return INSTANCE.bufferSize;
    }
  }

  public static int cfgIoRetries() {
    synchronized( INSTANCE ) {
      return INSTANCE.ioRetries;
    }
  }

  public static Path cfgTempDir() {
    synchronized( INSTANCE ) {
      return INSTANCE.tempDir;
    }
  }

} /* ENDCLASS */
