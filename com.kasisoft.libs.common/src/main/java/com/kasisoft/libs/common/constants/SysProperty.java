/**
 * Name........: SysProperty
 * Description.: Listing of system properties used to be accessed.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.constants;

import com.kasisoft.libs.common.config.*;
import com.kasisoft.libs.common.util.*;
import com.kasisoft.libs.common.xml.adapters.*;

import java.util.*;

import java.io.*;

/**
 * Listing of system properties used to be accessed.
 */
public class SysProperty {

  public static final SimpleProperty<List<File>> ClassPath             = new SimpleProperty<List<File>> ( "java.class.path"             , new PathAdapter( File.pathSeparator, true ) );
  public static final SimpleProperty<Double>     ClassVersion          = new SimpleProperty<Double>     ( "java.class.version"          , new DoubleAdapter() );
  public static final SimpleProperty<List<File>> EndorsedDirs          = new SimpleProperty<List<File>> ( "java.endorsed.dirs"          , new PathAdapter( File.pathSeparator, true ) );
  public static final SimpleProperty<List<File>> ExtDirs               = new SimpleProperty<List<File>> ( "java.ext.dirs"               , new PathAdapter( File.pathSeparator, true ) );
  public static final SimpleProperty<String>     FileEncoding          = new SimpleProperty<String>     ( "file.encoding"               , new StringAdapter() );
  public static final SimpleProperty<File>       JavaHome              = new SimpleProperty<File>       ( "java.home"                   , new FileAdapter( true ) );
  public static final SimpleProperty<Version>    JavaVersion           = new SimpleProperty<Version>    ( "java.version"                , new VersionAdapter( true ) );
  public static final SimpleProperty<String>     LineSeparator         = new SimpleProperty<String>     ( "line.separator"              , new StringAdapter() );
  public static final SimpleProperty<String>     OsName                = new SimpleProperty<String>     ( "os.name"                     , new StringAdapter() );
  public static final SimpleProperty<List<File>> Path                  = new SimpleProperty<List<File>> ( "java.library.path"           , new PathAdapter( File.pathSeparator, true ) );
  public static final SimpleProperty<Version>    RuntimeVersion        = new SimpleProperty<Version>    ( "java.runtime.version"        , new VersionAdapter( true ) );
  public static final SimpleProperty<Version>    SpecificationVersion  = new SimpleProperty<Version>    ( "java.specification.version"  , new VersionAdapter( true ) );
  public static final SimpleProperty<File>       TempDir               = new SimpleProperty<File>       ( "java.io.tmpdir"              , new FileAdapter( true ) );
  public static final SimpleProperty<File>       UserDir               = new SimpleProperty<File>       ( "user.dir"                    , new FileAdapter( true ) );
  public static final SimpleProperty<File>       UserHome              = new SimpleProperty<File>       ( "user.home"                   , new FileAdapter( true ) );
  
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
  
} /* ENDCLASS */
