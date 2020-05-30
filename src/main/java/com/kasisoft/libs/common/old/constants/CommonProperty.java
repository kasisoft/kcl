package com.kasisoft.libs.common.old.constants;

import com.kasisoft.libs.common.old.config.*;
import com.kasisoft.libs.common.old.xml.adapters.*;

import java.io.*;

/**
 * Listing of library related properties used to be accessed. Each key is simply constructed using the following naming 
 * convention:
 * 
 *    <base-package-path> '#' <property-name>
 *    
 * F.e. <code>com.kasisoft.libs.common.old.old#IORETRIES</code>
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface CommonProperty {

  /**
   * @deprecated [22-JUL-2018:KASI]   Use LibConfig#cfgIoRetries instead
   */
  @Deprecated
  SimpleProperty<Integer>  IoRetries   = new SimpleProperty<> ( "com.kasisoft.libs.common.old.old#IORETRIES"  , new IntegerAdapter () ).withDefault( Integer.valueOf( 5 ) );

  /**
   * @deprecated [22-JUL-2018:KASI]   Use LibConfig#cfgTempDir instead
   */
  @Deprecated
  SimpleProperty<File>     TempDir     = new SimpleProperty<> ( "com.kasisoft.libs.common.old.old#TEMPDIR"    , new FileAdapter    () ).withDefault( SysProperty.TempDir.getValue( System.getProperties() ) );
  
} /* ENDENUM */
