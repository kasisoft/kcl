package com.kasisoft.libs.common.test.framework;

import com.kasisoft.libs.common.io.*;

import org.testng.*;

import java.util.*;

import java.net.*;

import java.io.*;

/**
 * Collection of utility functions.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Utilities {
  
  @SuppressWarnings("null")
  public static File getTestdataDir() {
    URL location = null;
    try {
      Enumeration<URL> locations = Utilities.class.getClassLoader().getResources( "testdata" );
      while( locations.hasMoreElements() ) {
        location = locations.nextElement();
        break;
      }
      Assert.assertNotNull( location );
      return new File( location.toURI() );
    } catch( Exception ex ) {
      Assert.fail( ex.getLocalizedMessage(), ex );
      return null;
    }
  }

  public static File getTestdataDir( String path ) {
    File dir = getTestdataDir();
    try {
      return new File( dir, path );
    } catch( Exception ex ) {
      Assert.fail( ex.getLocalizedMessage(), ex );
      return null;
    }
  }

  public static List<File> createFileSystemStructure( File basedir ) {
    List<File>    result    = new ArrayList<File>();
    int           count     = Math.max( (int) (Math.random() * 1000), 50 );
    List<String>  filenames = new LinkedList<String>();
    for( int i = 0; i < count; i++ ) {
      filenames.add( String.format( "file%04d", Integer.valueOf(i) ) );
    }
    populate( result, basedir, filenames, 1 );
    return result;
  }
  
  private static void populate( List<File> collector, File basedir, List<String> filenames, int depth ) {
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

  public static byte[] createByteBlock() {
    byte[] result = new byte[ (int) (Math.random() * 2048) ];
    for( int i = 0; i < result.length; i++ ) {
      result[i] = (byte) (Math.random() * Byte.MAX_VALUE);
    }
    return result;
  }

  public static char[] createCharacterBlock() {
    char[] result = new char[ (int) (Math.random() * 2048) ];
    for( int i = 0; i < result.length; i++ ) {
      /** @ks.todo [07-Feb-2010:KASI]   When using Character.MAX_VALUE instead of 256 characters which
       *                             are being read might have a directional property set which results
       *                             in a different code point (even though the data is binary
       *                             equivalent).
       */
      char character = (char) (Math.random() * 256);
      if( ! Character.isDefined( character ) ) {
        character = 'A';
      }
      result[i]      = character;
    }
    return result;
  }

  public static File createRandomBytesFile() {
    File    result  = IoFunctions.newTempFile();
    byte[]  data    = createByteBlock();
    IoFunctions.writeBytes( result, data );
    return result;
  }
  
  public static File createRandomDirectory() {
    File result = IoFunctions.newTempFile();
    IoFunctions.mkdirs( result );
    int  count  = 20 + (int) (Math.random() * 100);
    fill( result, count );
    return result;
  }
  
  private static void fill( File dir, int count ) {
    count--;
    boolean createdir = ((int) (Math.random() * 100)) % 2 == 0;
    File    newchild  = new File( dir, String.format( "child%d", Integer.valueOf( count ) ) ); 
    if( createdir ) {
      IoFunctions.mkdirs( newchild );
      fill( newchild, count );
    } else {
      byte[]  data    = createByteBlock();
      IoFunctions.writeBytes( newchild, data );
      if( count > 0 ) {
        fill( dir, count );
      }
    }
  }

  public static String[] toArray( String ... args ) {
    return Arrays.copyOf( args, args.length );
  }
  
  public static <T> List<T> toList( T ... args ) {
    List<T> result = new ArrayList<T>();
    for( int i = 0; i < args.length; i++ ) {
      result.add( args[i] );
    }
    return result;
  }
  
  public static List<Integer> intsToList( int ... args ) {
    List<Integer> result = new ArrayList<Integer>();
    for( int value : args ) {
      result.add( Integer.valueOf( value ) );
    }
    return result;
  }
  
} /* ENDCLASS */
