package com.kasisoft.libs.common.thread;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

import java.io.*;

/**
 * Test for the class 'CharCopierRunnable'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class CharCopierRunnableTest {

  private char[] createRandomBlock( int size ) {
    char[] result = new char[ size ];
    for( int i = 0; i < size; i++ ) {
      result[i] = (char) ((Math.random() * 512) % 256);
    }
    return result;
  }
  
  @DataProvider(name="createDataBlocks")
  public Object[][] createDataBlocks() {
    int[]      sizes       = new int[] { 128, 256, 1024, 2000, 7625, 43881, 78991, 130992, 390112, 782674, 1349921 };
    Integer[]  buffersizes = new Integer[] { null, Integer.valueOf(1), Integer.valueOf(1031) };
    Object[][] result      = new Object[ sizes.length * buffersizes.length ][2];
    for( int i = 0; i < result.length; i++ ) {
      int sizeidx  = i / buffersizes.length;
      int buffidx  = i % buffersizes.length;
      if( sizeidx > 0 ) {
        result[i][0] = result[i - buffersizes.length][0];
      } else {
        result[i][0] = createRandomBlock( sizes[ sizeidx ] );
      }
      result[i][1] = buffersizes[ buffidx ];
    }
    return result;
  }
  
  @Test(dataProvider="createDataBlocks", groups="all")
  public void copyRunnable( char[] data, Integer buffersize ) {
    CharArrayReader    charin   = new CharArrayReader( data );
    CharArrayWriter    charout  = new CharArrayWriter();
    CharCopierRunnable runnable = new CharCopierRunnable( buffersize );
    runnable.configure( charin, charout );
    runnable.run();
    char[] copied = charout.toCharArray();
    assertThat( copied, is( data ) );
  }

  @Test(dataProvider="createDataBlocks", groups="all")
  public void copyThread( char[] data, Integer buffersize ) throws InterruptedException {
    CharArrayReader    charin   = new CharArrayReader( data );
    CharArrayWriter    charout  = new CharArrayWriter();
    CharCopierRunnable runnable = new CharCopierRunnable( buffersize );
    runnable.configure( charin, charout );
    Thread thread = new Thread( runnable );
    thread.start();
    thread.join();
    char[] copied = charout.toCharArray();
    assertThat( copied, is( data ) );
  }

} /* ENDCLASS */
