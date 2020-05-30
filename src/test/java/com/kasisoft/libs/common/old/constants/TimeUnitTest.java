package com.kasisoft.libs.common.old.constants;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

/**
 * Tests for the class 'TimeUnit'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class TimeUnitTest {

  @Test(groups="all")
  public void checkValues() {
    assertThat( TimeUnit . Millisecond . getMilliseconds(), is( 1L        ) );
    assertThat( TimeUnit . Second      . getMilliseconds(), is( 1000L     ) );
    assertThat( TimeUnit . Minute      . getMilliseconds(), is( 60000L    ) );
    assertThat( TimeUnit . Hour        . getMilliseconds(), is( 3600000L  ) );
    assertThat( TimeUnit . Day         . getMilliseconds(), is( 86400000L ) );
  }
  
} /* ENDCLASS */
