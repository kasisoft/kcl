/**
 * Name........: ByteCopierRunnableTest
 * Description.: Test for the class 'ByteCopierRunnable'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.thread;

import org.testng.annotations.*;

import org.testng.*;

import java.io.*;

/**
 * Test for the class 'ByteCopierRunnable'.
 */
@Test(groups="all")
public class ByteCopierRunnableTest {

  private byte[] createRandomBlock( int size ) {
    byte[] result = new byte[ size ];
    for( int i = 0; i < size; i++ ) {
      result[i] = (byte) ((Math.random() * 512) % 256);
    }
    return result;
  }
  
  @DataProvider(name="createDataBlocks")
  public Object[][] createDataBlocks() {
    int[]       sizes       = new int[] { 128, 256, 1024, 2000, 7625, 43881, 78991, 130992, 390112, 782674, 1349921 };
    Integer[]   buffersizes = new Integer[] { null, Integer.valueOf(1), Integer.valueOf(1031) };
    Object[][]  result      = new Object[ sizes.length * buffersizes.length ][2];
    for( int i = 0; i < result.length; i++ ) {
      int sizeidx   = i / buffersizes.length;
      int buffidx   = i % buffersizes.length;
      result[i][0] = createRandomBlock( sizes[ sizeidx ] );
      result[i][1] = buffersizes[ buffidx ];
    }
    return result;
  }
  
  @Test(dataProvider="createDataBlocks")
  public void copyRunnable( byte[] data, Integer buffersize ) {
    ByteArrayInputStream  bytein    = new ByteArrayInputStream( data );
    ByteArrayOutputStream byteout   = new ByteArrayOutputStream();
    ByteCopierRunnable    runnable  = new ByteCopierRunnable( buffersize );
    runnable.configure( bytein, byteout );
    runnable.run();
    byte[] copied = byteout.toByteArray();
    Assert.assertEquals( copied, data );
  }

  @Test(dataProvider="createDataBlocks")
  public void copyThread( byte[] data, Integer buffersize ) throws InterruptedException {
    ByteArrayInputStream  bytein    = new ByteArrayInputStream( data );
    ByteArrayOutputStream byteout   = new ByteArrayOutputStream();
    ByteCopierRunnable    runnable  = new ByteCopierRunnable( buffersize );
    runnable.configure( bytein, byteout );
    Thread thread = new Thread( runnable );
    thread.start();
    thread.join();
    byte[] copied = byteout.toByteArray();
    Assert.assertEquals( copied, data );
  }

  @Test(dataProvider="createDataBlocks", expectedExceptions={RuntimeException.class})
  public void copyFailingRunnable( byte[] data, Integer buffersize ) {
    ByteArrayInputStream  bytein    = new ByteArrayInputStream( data );
    ByteArrayOutputStream byteout   = new ByteArrayOutputStream();
    ByteCopierRunnable    runnable  = new ByteCopierRunnable( buffersize ) {
      protected void progress( CopyingProgress progress ) {
        if( progress.getCurrent() > 10 ) {
          throw new RuntimeException();
        }
      }
    };
    runnable.configure( bytein, byteout );
    runnable.run();
    // should not be reached as an exception is expected to occure
    Assert.fail();
  }

  @Test(dataProvider="createDataBlocks")
  public void copyFailingThread( byte[] data, Integer buffersize ) throws InterruptedException {
    ByteArrayInputStream  bytein    = new ByteArrayInputStream( data );
    ByteArrayOutputStream byteout   = new ByteArrayOutputStream();
    ByteCopierRunnable    runnable  = new ByteCopierRunnable( buffersize ) {
      protected void progress( CopyingProgress progress ) {
        if( progress.getCurrent() > 10 ) {
          throw new RuntimeException();
        }
      }
    };
    runnable.configure( bytein, byteout );
    Thread thread = new Thread( runnable );
    thread.start();
    thread.join();
    Assert.assertNotSame( byteout.toByteArray(), data );
  }

} /* ENDCLASS */