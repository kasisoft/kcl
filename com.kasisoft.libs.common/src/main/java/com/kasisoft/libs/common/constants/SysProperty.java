package com.kasisoft.libs.common.constants;

import com.kasisoft.libs.common.config.*;
import com.kasisoft.libs.common.util.*;
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

  @SuppressWarnings("deprecation")
  public static final SimpleProperty<List<File>> ClassPath             = new SimpleProperty<> ( "java.class.path"             , new PathAdapter( File.pathSeparator, true ) ).withShortkey( "classpath" );

  @SuppressWarnings("deprecation")
  public static final SimpleProperty<Double>     ClassVersion          = new SimpleProperty<> ( "java.class.version"          , new DoubleAdapter() ).withShortkey( "classver" );

  @SuppressWarnings("deprecation")
  public static final SimpleProperty<List<File>> EndorsedDirs          = new SimpleProperty<> ( "java.endorsed.dirs"          , new PathAdapter( File.pathSeparator, true ) ).withShortkey( "endorsed" );

  @SuppressWarnings("deprecation")
  public static final SimpleProperty<List<File>> ExtDirs               = new SimpleProperty<> ( "java.ext.dirs"               , new PathAdapter( File.pathSeparator, true ) ).withShortkey( "extdirs" );

  @SuppressWarnings("deprecation")
  public static final SimpleProperty<String>     FileEncoding          = new SimpleProperty<> ( "file.encoding"               , new StringAdapter() ).withShortkey( "encoding" );

  @SuppressWarnings("deprecation")
  public static final SimpleProperty<String>     FileSeparator         = new SimpleProperty<> ( "file.separator"              , new StringAdapter() ).withShortkey( "filesep" );

  @SuppressWarnings("deprecation")
  public static final SimpleProperty<File>       JavaHome              = new SimpleProperty<> ( "java.home"                   , new FileAdapter( true ) ).withShortkey( "jre" );

  @SuppressWarnings("deprecation")
  public static final SimpleProperty<Version>    JavaVersion           = new SimpleProperty<> ( "java.version"                , new VersionAdapter( true ) ).withShortkey( "javaver" );

  @SuppressWarnings("deprecation")
  public static final SimpleProperty<String>     LineSeparator         = new SimpleProperty<> ( "line.separator"              , new StringAdapter() ).withShortkey( "linesep" );

  @SuppressWarnings("deprecation")
  public static final SimpleProperty<String>     OsName                = new SimpleProperty<> ( "os.name"                     , new StringAdapter() ).withShortkey( "os"  );

  @SuppressWarnings("deprecation")
  public static final SimpleProperty<List<File>> Path                  = new SimpleProperty<> ( "java.library.path"           , new PathAdapter( File.pathSeparator, true ) ).withShortkey( "path" );

  @SuppressWarnings("deprecation")
  public static final SimpleProperty<Version>    RuntimeVersion        = new SimpleProperty<> ( "java.runtime.version"        , new VersionAdapter( true ) ).withShortkey( "runtimever" );

  @SuppressWarnings("deprecation")
  public static final SimpleProperty<Version>    SpecificationVersion  = new SimpleProperty<> ( "java.specification.version"  , new VersionAdapter( true ) ).withShortkey( "specver" );

  @SuppressWarnings("deprecation")
  public static final SimpleProperty<File>       TempDir               = new SimpleProperty<> ( "java.io.tmpdir"              , new FileAdapter( true ) ).withShortkey( "temp" );

  @SuppressWarnings("deprecation")
  public static final SimpleProperty<File>       UserDir               = new SimpleProperty<> ( "user.dir"                    , new FileAdapter( true ) ).withShortkey( "pwd" );

  @SuppressWarnings("deprecation")
  public static final SimpleProperty<File>       UserHome              = new SimpleProperty<> ( "user.home"                   , new FileAdapter( true ) ).withShortkey( "home" );
  
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
   * @param shortkey     <code>true</code> <=> Use the short key representation to create the key.
   *
   * @return   A Map containing key-value pairs for a possible replacement. Not <code>null</code>.
   */
  public static Map<String,String> createReplacementMap( boolean shortkey ) {
    return createReplacementMap( "%%%s%%", shortkey );
  }
  
  /**
   * Creates a replacement map used to substitute system properties.
   * 
   * @param format     A formatting String with one %s format code. This is used in order to support various key 
   *                   formats. Neither <code>null</code> nor empty.
   * @param shortkey   <code>true</code> <=> Use the short key representation to create the key.
   *                   @deprecated [06-Oct-2014:KASI]   The <param>shortkey</param> will be dropped beginning with 
   *                                                    version 1.5.
   *
   * @return   A Map containing key-value pairs for a possible replacement. Not <code>null</code>.
   */
  @SuppressWarnings("deprecation")
  @Deprecated
  public static Map<String,String> createReplacementMap( @NonNull String format, boolean shortkey ) {
    Map<String,String> result = new Hashtable<String,String>();
    for( SimpleProperty sysprop : SysProperty.values() ) {
      String textualvalue = sysprop.getTextualValue( System.getProperties() );
      if( shortkey ) {
        result.put( String.format( format, sysprop.getShortkey() ), textualvalue );
      } else {
        result.put( String.format( format, sysprop.getKey() ), textualvalue );
      }
    }
    return result;
  }

} /* ENDCLASS */
