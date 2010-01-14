/**
 * Name........: Utilities
 * Description.: Collection of utility functions.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.test.framework;

import org.testng.*;

import java.util.*;

import java.io.*;

/**
 * Collection of utility functions.
 */
public class Utilities {

  public static final String[] toArray( String ... args ) {
    return Arrays.copyOf( args, args.length );
  }
  
  public static final <T> List<T> toList( T ... args ) {
    List<T> result = new ArrayList<T>();
    for( int i = 0; i < args.length; i++ ) {
      result.add( args[i] );
    }
    return result;
  }
  
  public static final List<Integer> toList( int ... args ) {
    List<Integer> result = new ArrayList<Integer>();
    for( int value : args ) {
      result.add( Integer.valueOf( value ) );
    }
    return result;
  }
  
  public static final byte[] join( byte[] ... segments ) {
    ByteArrayOutputStream byteout = new ByteArrayOutputStream();
    for( byte[] segment : segments ) {
      try {
        byteout.write( segment );
      } catch( IOException ex ) {
        Assert.fail( ex.getMessage() );
      }
    }
    return byteout.toByteArray();
  }
  
} /* ENDCLASS */
