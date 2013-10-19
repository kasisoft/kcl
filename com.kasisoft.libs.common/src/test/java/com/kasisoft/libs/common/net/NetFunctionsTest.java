/**
 * Name........: NetFunctionsTest
 * Description.: Testcases for the class 'NetFunctions'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.net;

import com.kasisoft.libs.common.constants.*;

import org.testng.annotations.*;

import org.testng.*;

/**
 * Testcases for the class 'NetFunctions'.
 */
@Test(groups="all")
public class NetFunctionsTest {

  @Test
  public void waitAndSendMessage() throws InterruptedException {
    
    Thread t1 = new Thread() {
      @Override
      public void run() {
        byte[] message = NetFunctions.waitForMessage( 17175 );
        Assert.assertEquals( message, "MY MESSAGE".getBytes() );
      }
    };
    t1.start();
    Thread.sleep( 3000 );
    Assert.assertTrue( NetFunctions.sendMessage( "127.0.0.1", 17175, "MY MESSAGE".getBytes() ) );
    t1.join();

    // test with a timeout
    long   before = System.currentTimeMillis();
    Thread t2     = new Thread() {
      @Override
      public void run() {
        byte[] message = NetFunctions.waitForMessage( 17175, Integer.valueOf( (int) TimeUnit.Second.amount( 30 ) ) );
        Assert.assertNull( message );
      }
    };
    t2.start();
    Thread.sleep( 3000 );
    t2.join();
    long   after  = System.currentTimeMillis();
    long   diff   = after - before;
    Assert.assertTrue( diff >= TimeUnit.Second.amount( 30 ) );
    Assert.assertTrue( diff <= TimeUnit.Second.amount( 32 ) );
    
  }
  
} /* ENDCLASS */
