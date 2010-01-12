/**
 * Name........: ByteCopierRunnableTest
 * Description.: Test for the class 'ByeCopierRunnable'.
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
 * Test for the class 'ByeCopierRunnable'.
 */
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
    int[]       sizes       = new int[] { 0/* , 128, 256, 1024, 2000, 7625, 43881, 78991, 130992, 390112, 782674, 1349921 */ };
    Integer[]   buffersizes = new Integer[] { Integer.valueOf(1), Integer.valueOf(1) /*, Integer.valueOf(1031) */ };
    Object[][]  result      = new Object[ sizes.length * buffersizes.length ][];
    for( int i = 0; i < sizes.length; i++ ) {
      byte[] datablock = createRandomBlock( sizes[i] );
      for( int j = 0; j < buffersizes.length; j++ ) {
        result[ i * j ] = new Object[] { datablock, buffersizes[j] };
      }
    }
    return result;
  }
  
  @Test(dataProvider="createDataBlocks")
  public void copyAsRunnable( byte[] data, Integer buffersize ) {
    ByteArrayInputStream  bytein    = new ByteArrayInputStream( data );
    ByteArrayOutputStream byteout   = new ByteArrayOutputStream();
    ByteCopierRunnable    runnable  = null;
    if( buffersize.intValue() < 0 ) {
      runnable = new ByteCopierRunnable( bytein, byteout );
    } else {
      runnable = new ByteCopierRunnable( bytein, byteout, buffersize.intValue() );
    }
    runnable.run();
    byte[] copied = byteout.toByteArray();
    Assert.assertEquals( copied, data );
  }

//  @Test(dataProvider="createDataBlocks")
//  public void copyAsThread( byte[] data, Integer buffersize ) throws InterruptedException {
//    ByteArrayInputStream  bytein    = new ByteArrayInputStream( data );
//    ByteArrayOutputStream byteout   = new ByteArrayOutputStream();
//    ByteCopierRunnable    runnable  = null;
//    if( buffersize.intValue() < 0 ) {
//      runnable = new ByteCopierRunnable( bytein, byteout );
//    } else {
//      runnable = new ByteCopierRunnable( bytein, byteout, buffersize.intValue() );
//    }
//    Thread thread = new Thread( runnable );
//    thread.start();
//    thread.join();
//    byte[] copied = byteout.toByteArray();
//    Assert.assertEquals( copied, data );
//  }

} /* ENDCLASS */
