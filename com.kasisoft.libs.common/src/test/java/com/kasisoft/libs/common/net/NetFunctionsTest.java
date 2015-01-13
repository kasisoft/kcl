package com.kasisoft.libs.common.net;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import com.kasisoft.libs.common.constants.*;

import org.testng.annotations.*;

/**
 * Testcases for the class 'NetFunctions'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Test(groups="all")
public class NetFunctionsTest {

  @Test
  public void waitAndSendMessage() throws InterruptedException {
    
    Thread t1 = new Thread() {
      @Override
      public void run() {
        byte[] message = NetFunctions.waitForMessage( 17175 );
        assertThat( message, is( "MY MESSAGE".getBytes() ) );
      }
    };
    t1.start();
    Thread.sleep( 3000 );
    assertTrue( NetFunctions.sendMessage( "127.0.0.1", 17175, "MY MESSAGE".getBytes() ) );
    t1.join();

    // test with a timeout
    long   before = System.currentTimeMillis();
    Thread t2     = new Thread() {
      @Override
      public void run() {
        byte[] message = NetFunctions.waitForMessage( 17175, Integer.valueOf( (int) TimeUnit.Second.amount( 30 ) ) );
        assertThat( message, is( nullValue() ) );
      }
    };
    t2.start();
    Thread.sleep( 3000 );
    t2.join();
    long   after  = System.currentTimeMillis();
    long   diff   = after - before; 
    assertTrue( diff >= TimeUnit.Second.amount( 30 ) );
    assertTrue( diff <= TimeUnit.Second.amount( 35 ) );
    
  }
  
} /* ENDCLASS */
