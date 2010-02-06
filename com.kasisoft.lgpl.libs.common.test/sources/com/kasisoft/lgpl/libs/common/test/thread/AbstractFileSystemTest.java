/**
 * Name........: AbstractFileSystemTest
 * Description.: Basic testclass used to support filesystem related tests.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.test.thread;

import com.kasisoft.lgpl.libs.common.io.*;

import java.util.*;

import java.io.*;

/**
 * Basic testclass used to support filesystem related tests.
 */
class AbstractFileSystemTest {

  private byte[] createDataBlock() {
    byte[] result = new byte[ (int) (Math.random() * 2048) ];
    for( int i = 0; i < result.length; i++ ) {
      result[i] = (byte) (Math.random() * Byte.MAX_VALUE);
    }
    return result;
  }

  List<File> createFileSystemStructure( File basedir ) {
    List<File>    result    = new ArrayList<File>();
    int           count     = Math.max( (int) (Math.random() * 1000), 50 );
    List<String>  filenames = new LinkedList<String>();
    for( int i = 0; i < count; i++ ) {
      filenames.add( String.format( "file%04d", Integer.valueOf(i) ) );
    }
    populate( result, basedir, filenames, 1 );
    return result;
  }
  
  private void populate( List<File> collector, File basedir, List<String> filenames, int depth ) {
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
        byte[] datablock = createDataBlock();
        File   destfile  = new File( basedir, filename );
        collector.add( destfile );
        IoFunctions.writeBytes( destfile, datablock );
      }
    }
  }
  

} /* ENDCLASS */
