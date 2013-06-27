/**
 * Name........: CommonProperty
 * Description.: Listing of library related properties used to be accessed.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.constants;

import com.kasisoft.libs.common.util.*;
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
 */
public interface CommonProperty {

  TypedProperty<File>     Application = new TypedProperty<File>    ( "com.kasisoft.libs.common#APPLICATION", new FileAdapter    (), false );
  TypedProperty<Integer>  BufferCount = new TypedProperty<Integer> ( "com.kasisoft.libs.common#BUFFERCOUNT", new IntegerAdapter (), Integer.valueOf( 8192 ) );
  TypedProperty<Integer>  IoRetries   = new TypedProperty<Integer> ( "com.kasisoft.libs.common#IORETRIES"  , new IntegerAdapter (), Integer.valueOf( 5 ) );
  TypedProperty<File>     TempDir     = new TypedProperty<File>    ( "com.kasisoft.libs.common#TEMPDIR"    , new FileAdapter    (), new File( SystemProperty.TempDir.getValue() ) );
  
} /* ENDENUM */
