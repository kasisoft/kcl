package com.kasisoft.libs.common.old.thread;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Test for the class 'ByteCopierRunnable'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
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
  
  @Test(dataProvider="createDataBlocks", groups="all")
  public void copyRunnable( byte[] data, Integer buffersize ) {
    ByteArrayInputStream  bytein    = new ByteArrayInputStream( data );
    ByteArrayOutputStream byteout   = new ByteArrayOutputStream();
    ByteCopierRunnable    runnable  = new ByteCopierRunnable( buffersize );
    runnable.configure( bytein, byteout );
    runnable.run();
    byte[] copied = byteout.toByteArray();
    assertThat( copied, is( data ) );
  }

  @Test(dataProvider="createDataBlocks", groups="all")
  public void copyThread( byte[] data, Integer buffersize ) throws InterruptedException {
    ByteArrayInputStream  bytein    = new ByteArrayInputStream( data );
    ByteArrayOutputStream byteout   = new ByteArrayOutputStream();
    ByteCopierRunnable    runnable  = new ByteCopierRunnable( buffersize );
    runnable.configure( bytein, byteout );
    Thread thread = new Thread( runnable );
    thread.start();
    thread.join();
    byte[] copied = byteout.toByteArray();
    assertThat( copied, is( data ) );
  }

} /* ENDCLASS */