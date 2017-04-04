package com.kasisoft.libs.common.constants;

import com.kasisoft.libs.common.config.*;

import com.kasisoft.libs.common.xml.adapters.*;

import java.io.*;

/**
 * Listing of library related properties used to be accessed. Each key is simply constructed using the following naming 
 * convention:
 * 
 *    <base-package-path> '#' <property-name>
 *    
 * F.e. <code>com.kasisoft.libs.common#IORETRIES</code>
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface CommonProperty {

  SimpleProperty<Integer>  BufferCount = new SimpleProperty<> ( "com.kasisoft.libs.common#BUFFERCOUNT", new IntegerAdapter () ).withDefault( Integer.valueOf( 8192 ) );
  SimpleProperty<Integer>  IoRetries   = new SimpleProperty<> ( "com.kasisoft.libs.common#IORETRIES"  , new IntegerAdapter () ).withDefault( Integer.valueOf( 5 ) );
  SimpleProperty<File>     TempDir     = new SimpleProperty<> ( "com.kasisoft.libs.common#TEMPDIR"    , new FileAdapter    () ).withDefault( SysProperty.TempDir.getValue( System.getProperties() ) );
  
} /* ENDENUM */
