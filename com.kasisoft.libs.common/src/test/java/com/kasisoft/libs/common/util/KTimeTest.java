/**
 * Name........: KTimeTest
 * Description.: Tests for the type 'KTime'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.util;

import org.testng.annotations.*;

import org.testng.*;

@Test(groups="all")
public class KTimeTest {

  private KTime newRefTime() {
    KTime result = new KTime();
    result.setHours( 12 );
    result.setMinutes( 37 );
    result.setSeconds( 34 );
    result.setMilliseconds( 45 );
    return result;
  }
  
  @SuppressWarnings("boxing")
  @DataProvider(name="createIncrement")
  public Object[][] createIncrement() {
    return new Object[][] {
      
      new Object[] { "12:37:34 045", newRefTime(), 0, 0, 0, 0 },
      new Object[] { "13:37:34 045", newRefTime(), 1, 0, 0, 0 },
      new Object[] { "12:38:34 045", newRefTime(), 0, 1, 0, 0 },
      new Object[] { "12:37:35 045", newRefTime(), 0, 0, 1, 0 },
      new Object[] { "12:37:34 046", newRefTime(), 0, 0, 0, 1 },
      
      new Object[] { "22:37:34 045", newRefTime(), 10,  0,  0,  0 },
      new Object[] { "12:47:34 045", newRefTime(),  0, 10,  0,  0 },
      new Object[] { "12:37:44 045", newRefTime(),  0,  0, 10,  0 },
      new Object[] { "12:37:34 055", newRefTime(),  0,  0,  0, 10 },

      new Object[] { "20:37:34 045", newRefTime(), 80,  0,  0,  0 },
      new Object[] { "13:57:34 045", newRefTime(),  0, 80,  0,  0 },
      new Object[] { "12:38:54 045", newRefTime(),  0,  0, 80,  0 },
      new Object[] { "12:37:34 125", newRefTime(),  0,  0,  0, 80 },

    };
  }

  @SuppressWarnings("boxing")
  @DataProvider(name="createDecrement")
  public Object[][] createDecrement() {
    return new Object[][] {
        
        new Object[] { "12:37:34 045", newRefTime(), 0, 0, 0, 0 },
        new Object[] { "11:37:34 045", newRefTime(), 1, 0, 0, 0 },
        new Object[] { "12:36:34 045", newRefTime(), 0, 1, 0, 0 },
        new Object[] { "12:37:33 045", newRefTime(), 0, 0, 1, 0 },
        new Object[] { "12:37:34 044", newRefTime(), 0, 0, 0, 1 },
        
        new Object[] { "02:37:34 045", newRefTime(), 10,  0,  0,  0 },
        new Object[] { "12:27:34 045", newRefTime(),  0, 10,  0,  0 },
        new Object[] { "12:37:24 045", newRefTime(),  0,  0, 10,  0 },
        new Object[] { "12:37:34 035", newRefTime(),  0,  0,  0, 10 },

        new Object[] { "04:37:34 045", newRefTime(), 80,  0,  0,  0 },
        new Object[] { "11:17:34 045", newRefTime(),  0, 80,  0,  0 },
        new Object[] { "12:36:14 045", newRefTime(),  0,  0, 80,  0 },
        new Object[] { "12:37:33 965", newRefTime(),  0,  0,  0, 80 },

      };
  }
  
  @Test
  public void setTime() {
    KTime time = newRefTime();
    Assert.assertEquals( time.toString(), "12:37:34 045" );
    Assert.assertEquals( time.toString( "HH  mm  ss  SSS" ), "12  37  34  045" );
  }

  @Test(dataProvider="createIncrement")
  public void increment( String expected, KTime base, int hours, int minutes, int seconds, int millis ) {
    for( int i = 0; i < hours; i++ ) {
      base.incHours();
    }
    for( int i = 0; i < minutes; i++ ) {
      base.incMinutes();
    }
    for( int i = 0; i < seconds; i++ ) {
      base.incSeconds();
    }
    for( int i = 0; i < millis; i++ ) {
      base.incMilliseconds();
    }
    Assert.assertEquals( base.toString(), expected );
  }

  @Test(dataProvider="createDecrement")
  public void decrement( String expected, KTime base, int hours, int minutes, int seconds, int millis ) {
    for( int i = 0; i < hours; i++ ) {
      base.decHours();
    }
    for( int i = 0; i < minutes; i++ ) {
      base.decMinutes();
    }
    for( int i = 0; i < seconds; i++ ) {
      base.decSeconds();
    }
    for( int i = 0; i < millis; i++ ) {
      base.decMilliseconds();
    }
    Assert.assertEquals( base.toString(), expected );
  }

} /* ENDCLASS */