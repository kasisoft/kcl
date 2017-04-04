package com.kasisoft.libs.common.constants;

import com.kasisoft.libs.common.config.*;
import com.kasisoft.libs.common.model.*;
import com.kasisoft.libs.common.xml.adapters.*;

import java.util.*;

import java.io.*;

import lombok.*;

/**
 * Listing of system properties used to be accessed.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class SysProperty {

  public static final SimpleProperty<List<File>> ClassPath             = new SimpleProperty<> ( "java.class.path"             , new PathAdapter( File.pathSeparator, true ) );
  public static final SimpleProperty<Double>     ClassVersion          = new SimpleProperty<> ( "java.class.version"          , new DoubleAdapter() );
  public static final SimpleProperty<List<File>> EndorsedDirs          = new SimpleProperty<> ( "java.endorsed.dirs"          , new PathAdapter( File.pathSeparator, true ) );
  public static final SimpleProperty<List<File>> ExtDirs               = new SimpleProperty<> ( "java.ext.dirs"               , new PathAdapter( File.pathSeparator, true ) );
  public static final SimpleProperty<String>     FileEncoding          = new SimpleProperty<> ( "file.encoding"               , new StringAdapter() );
  public static final SimpleProperty<String>     FileSeparator         = new SimpleProperty<> ( "file.separator"              , new StringAdapter() );
  public static final SimpleProperty<File>       JavaHome              = new SimpleProperty<> ( "java.home"                   , new FileAdapter( true ) );
  public static final SimpleProperty<Version>    JavaVersion           = new SimpleProperty<> ( "java.version"                , new VersionAdapter( true ) );
  public static final SimpleProperty<String>     LineSeparator         = new SimpleProperty<> ( "line.separator"              , new StringAdapter() ).withDefault( "\n" );
  public static final SimpleProperty<String>     OsName                = new SimpleProperty<> ( "os.name"                     , new StringAdapter() );
  public static final SimpleProperty<List<File>> Path                  = new SimpleProperty<> ( "java.library.path"           , new PathAdapter( File.pathSeparator, true ) );
  public static final SimpleProperty<Version>    RuntimeVersion        = new SimpleProperty<> ( "java.runtime.version"        , new VersionAdapter( true ) );
  public static final SimpleProperty<Version>    SpecificationVersion  = new SimpleProperty<> ( "java.specification.version"  , new VersionAdapter( true ) );
  public static final SimpleProperty<File>       TempDir               = new SimpleProperty<> ( "java.io.tmpdir"              , new FileAdapter( true ) );
  public static final SimpleProperty<File>       UserDir               = new SimpleProperty<> ( "user.dir"                    , new FileAdapter( true ) );
  public static final SimpleProperty<File>       UserHome              = new SimpleProperty<> ( "user.home"                   , new FileAdapter( true ) );
  
  /**
   * Returns a list of all currently supported system properties.
   * 
   * @return   A list of all currently supported system properties. Not <code>null</code>.
   */
  public static SimpleProperty[] values() {
    return new SimpleProperty[] {
      ClassPath             ,
      ClassVersion          ,
      EndorsedDirs          ,
      ExtDirs               ,
      FileEncoding          ,
      JavaHome              ,
      JavaVersion           ,
      LineSeparator         ,
      OsName                ,
      Path                  ,
      RuntimeVersion        ,
      SpecificationVersion  ,
      TempDir               ,
      UserDir               ,
      UserHome              
    };
  }
  
  /**
   * Creates a replacement map used to substitute system properties. The key is encapsulated by '%' characters.
   * 
   * @return   A Map containing key-value pairs for a possible replacement. Not <code>null</code>.
   */
  public static Map<String,String> createReplacementMap() {
    return createReplacementMap( "%%%s%%" );
  }
  
  /**
   * Creates a replacement map used to substitute system properties.
   * 
   * @param format     A formatting String with one %s format code. This is used in order to support various key 
   *                   formats. Neither <code>null</code> nor empty.
   *
   * @return   A Map containing key-value pairs for a possible replacement. Not <code>null</code>.
   */
  public static Map<String,String> createReplacementMap( @NonNull String format ) {
    val result = new Hashtable<String, String>();
    for( val sysprop : SysProperty.values() ) {
      String textualvalue = sysprop.getTextualValue( System.getProperties() );
      if( textualvalue == null ) {
        textualvalue = "";
      }
      result.put( String.format( format, sysprop.getKey() ), textualvalue );
    }
    return result;
  }

} /* ENDCLASS */
