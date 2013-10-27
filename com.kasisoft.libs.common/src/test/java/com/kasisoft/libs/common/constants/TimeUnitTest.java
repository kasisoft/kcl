/**
 * Name........: TimeUnitTest
 * Description.: Tests for the class 'TimeUnit'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.constants;

import org.testng.annotations.*;

import org.testng.*;

/**
 * Tests for the class 'TimeUnit'.
 */
@Test(groups="all")
public class TimeUnitTest {

  @Test
  public void checkValues() {
    Assert.assertEquals( TimeUnit . Millisecond . getMilliseconds(), 1        );
    Assert.assertEquals( TimeUnit . Second      . getMilliseconds(), 1000     );
    Assert.assertEquals( TimeUnit . Minute      . getMilliseconds(), 60000    );
    Assert.assertEquals( TimeUnit . Hour        . getMilliseconds(), 3600000  );
    Assert.assertEquals( TimeUnit . Day         . getMilliseconds(), 86400000 );
  }
  
} /* ENDCLASS */
