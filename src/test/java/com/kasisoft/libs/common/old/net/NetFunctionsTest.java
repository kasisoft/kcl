package com.kasisoft.libs.common.old.net;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.testng.Assert.assertTrue;

import com.kasisoft.libs.common.old.constants.TimeUnit;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
  
  @DataProvider(name = "createGravatarLinkData")
  public Object[][] createGravatarLinkData() {
    return new Object[][] {
      { null                                                                      , null                                  , null },
      { null                                                                      , null                                  , 12   },
      { "https://www.gravatar.com/avatar/8fea46f5bc403f6949300c3007d2f18d"        , " Daniel.KASMEROGLU@kasisoft.net \n"  , null },
      { "https://www.gravatar.com/avatar/8fea46f5bc403f6949300c3007d2f18d"        , "daniel.kasmeroglu@kasisoft.net"      , null },
      { "https://www.gravatar.com/avatar/8fea46f5bc403f6949300c3007d2f18d?s=100"  , "daniel.kasmeroglu@kasisoft.net"      , 100  },
    };
  }
  
  @Test(groups = "all", dataProvider = "createGravatarLinkData")
  public void getGravatarLink( String expected, String email, Integer size ) {
    assertThat( NetFunctions.getGravatarLink( email, size ), is( expected ) );
  }
  
} /* ENDCLASS */
