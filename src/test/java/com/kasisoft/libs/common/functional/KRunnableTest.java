package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.*;

import org.testng.annotations.*;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KRunnableTest {

  KRunnable runnable = () -> { throw new RuntimeException("error"); };
  
  @Test(groups = "all", expectedExceptions = RuntimeException.class)
  public void run() throws Exception {
    runnable.run();
  }
  
  @Test(groups = "all", expectedExceptions = KclException.class)
  public void protect() {
    runnable.protect().run();
  }
  
} /* ENDCLASS */
