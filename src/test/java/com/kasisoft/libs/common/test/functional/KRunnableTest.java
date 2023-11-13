package com.kasisoft.libs.common.test.functional;

import static org.junit.jupiter.api.Assertions.*;

import com.kasisoft.libs.common.functional.*;
import com.kasisoft.libs.common.*;

import org.junit.jupiter.api.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class KRunnableTest {

  private KRunnable runnable = () -> { throw new RuntimeException("error"); };

  @Test
  public void run() throws Exception {
    assertThrows(RuntimeException.class, () -> {
      runnable.run();
    });
  }

  @Test
  public void protect() {
    assertThrows(KclException.class, () -> {
      runnable.protect().run();
    });
  }

} /* ENDCLASS */
