/**
 * Name........: Utilities
 * Description.: Collection of utility functions.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.test.framework;

import com.kasisoft.lgpl.libs.common.constants.*;

import com.kasisoft.lgpl.libs.common.io.*;

import org.testng.*;

import java.util.*;

import java.io.*;

/**
 * Collection of utility functions.
 */
public class Utilities {

  public static final List<File> createFileSystemStructure( File basedir ) {
    List<File>    result    = new ArrayList<File>();
    int           count     = Math.max( (int) (Math.random() * 1000), 50 );
    List<String>  filenames = new LinkedList<String>();
    for( int i = 0; i < count; i++ ) {
      filenames.add( String.format( "file%04d", Integer.valueOf(i) ) );
    }
    populate( result, basedir, filenames, 1 );
    return result;
  }
  
  private static final void populate( List<File> collector, File basedir, List<String> filenames, int depth ) {
    while( ! filenames.isEmpty() ) {
      boolean godeeper = (((int) (Math.random() * 8192)) % 2 == 0) && (depth <= 10);
      if( godeeper ) {
        String dirname = String.format( "dir%04d", Integer.valueOf( filenames.size() ) );
        File   dir     = new File( basedir, dirname );
        dir.mkdirs();
        collector.add( dir );
        populate( collector, dir, filenames, depth + 1 );
      } else {
        String filename  = filenames.remove(0);
        byte[] datablock = createByteBlock();
        File   destfile  = new File( basedir, filename );
        collector.add( destfile );
        IoFunctions.writeBytes( destfile, datablock );
      }
    }
  }

  public static final byte[] createByteBlock() {
    byte[] result = new byte[ (int) (Math.random() * 2048) ];
    for( int i = 0; i < result.length; i++ ) {
      result[i] = (byte) (Math.random() * Byte.MAX_VALUE);
    }
    return result;
  }

  public static final char[] createCharacterBlock() {
    char[] result = new char[ (int) (Math.random() * 2048) ];
    for( int i = 0; i < result.length; i++ ) {
      char character = (char) (Math.random() * Character.MAX_VALUE);
      if( ! Character.isDefined( character ) ) {
        character = 'A';
      }
      result[i]      = character;
    }
    return result;
  }

  public static final File createRandomBytesFile() {
    File    result  = IoFunctions.newTempFile();
    byte[]  data    = createByteBlock();
    IoFunctions.writeBytes( result, data );
    return result;
  }

  public static final File createRandomCharacterFile() {
    File    result  = IoFunctions.newTempFile();
    char[]  data    = createCharacterBlock();
    IoFunctions.writeCharacters( result, data, Encoding.getDefault() );
    return result;
  }

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
