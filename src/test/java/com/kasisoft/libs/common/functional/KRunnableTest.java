package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.*;

import org.testng.annotations.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class KRunnableTest {

  private KRunnable runnable = () -> { throw new RuntimeException("error"); };
  
  @Test(groups = "all", expectedExceptions = RuntimeException.class)
  public void run() throws Exception {
    runnable.run();
  }
  
  @Test(groups = "all", expectedExceptions = KclException.class)
  public void protect() {
    runnable.protect().run();
  }
  
} /* ENDCLASS */
