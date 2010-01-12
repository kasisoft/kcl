/**
 * Name........: CharCopierRunnableTest
 * Description.: Test for the class 'CharCopierRunnable'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.test.io;

import com.kasisoft.lgpl.libs.common.thread.*;

import org.testng.annotations.*;

import org.testng.*;

import java.io.*;

/**
 * Test for the class 'CharCopierRunnable'.
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
  
  @Test(dataProvider="createDataBlocks")
  public void copyRunnable( char[] data, Integer buffersize ) {
    CharArrayReader    charin   = new CharArrayReader( data );
    CharArrayWriter    charout  = new CharArrayWriter();
    CharCopierRunnable runnable = null;
    if( buffersize == null ) {
      runnable = new CharCopierRunnable( charin, charout );
    } else {
      runnable = new CharCopierRunnable( charin, charout, buffersize.intValue() );
    }
    runnable.run();
    char[] copied = charout.toCharArray();
    Assert.assertEquals( copied, data );
  }

  @Test(dataProvider="createDataBlocks")
  public void copyThread( char[] data, Integer buffersize ) throws InterruptedException {
    CharArrayReader    charin   = new CharArrayReader( data );
    CharArrayWriter    charout  = new CharArrayWriter();
    CharCopierRunnable runnable = null;
    if( buffersize == null ) {
      runnable = new CharCopierRunnable( charin, charout );
    } else {
      runnable = new CharCopierRunnable( charin, charout, buffersize.intValue() );
    }
    Thread thread = new Thread( runnable );
    thread.start();
    thread.join();
    char[] copied = charout.toCharArray();
    Assert.assertEquals( copied, data );
  }

  @Test(dataProvider="createDataBlocks", expectedExceptions={RuntimeException.class})
  public void copyFailingRunnable( char[] data, Integer buffersize ) {
    CharArrayReader    charin   = new CharArrayReader( data );
    CharArrayWriter    charout  = new CharArrayWriter();
    CharCopierRunnable runnable = null;
    if( buffersize == null ) {
      runnable = new CharCopierRunnable( charin, charout ) {
        protected void onIteration( int done, int written ) {
          if( done > 10 ) {
            throw new RuntimeException();
          }
        }
      };
    } else {
      runnable = new CharCopierRunnable( charin, charout, buffersize.intValue() ) {
        protected void onIteration( int done, int written ) {
          if( done > 10 ) {
            throw new RuntimeException();
          }
        }
      };
    }
    runnable.run();
    // should not be reached as an exception is expected to occure
    Assert.fail();
  }

  @Test(dataProvider="createDataBlocks")
  public void copyFailingThread( char[] data, Integer buffersize ) throws InterruptedException {
    CharArrayReader    charin   = new CharArrayReader( data );
    CharArrayWriter    charout  = new CharArrayWriter();
    CharCopierRunnable runnable = null;
    if( buffersize == null ) {
      runnable = new CharCopierRunnable( charin, charout ) {
        protected void onIteration( int done, int written ) {
          if( done > 10 ) {
            throw new RuntimeException();
          }
        }
      };
    } else {
      runnable = new CharCopierRunnable( charin, charout, buffersize.intValue() ) {
        protected void onIteration( int done, int written ) {
          if( done > 10 ) {
            throw new RuntimeException();
          }
        }
      };
    }
    Thread thread = new Thread( runnable );
    thread.start();
    thread.join();
    Assert.assertNotSame( charout.toCharArray(), data );
  }

} /* ENDCLASS */
