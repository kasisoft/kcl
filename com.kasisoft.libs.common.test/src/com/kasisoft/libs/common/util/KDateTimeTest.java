/**
 * Name........: KDateTimeTest
 * Description.: Tests for the type 'KDateTime'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.util;

import com.kasisoft.lgpl.libs.common.constants.*;

import org.testng.annotations.*;

import org.testng.*;

@Test(groups="all")
public class KDateTimeTest {

  private KDateTime newRefDateTime() {
    KDateTime result = new KDateTime();
    result.setDay( 4 );
    result.setMonth( Month.August );
    result.setYear( 2010 );
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
      
      new Object[] { "04.08.2010 12:37:34 045", newRefDateTime(), 0, 0, 0, 0, 0, 0, 0 },
      new Object[] { "05.08.2010 12:37:34 045", newRefDateTime(), 1, 0, 0, 0, 0, 0, 0 },
      new Object[] { "04.09.2010 12:37:34 045", newRefDateTime(), 0, 1, 0, 0, 0, 0, 0 },
      new Object[] { "04.08.2011 12:37:34 045", newRefDateTime(), 0, 0, 1, 0, 0, 0, 0 },
      new Object[] { "04.08.2010 13:37:34 045", newRefDateTime(), 0, 0, 0, 1, 0, 0, 0 },
      new Object[] { "04.08.2010 12:38:34 045", newRefDateTime(), 0, 0, 0, 0, 1, 0, 0 },
      new Object[] { "04.08.2010 12:37:35 045", newRefDateTime(), 0, 0, 0, 0, 0, 1, 0 },
      new Object[] { "04.08.2010 12:37:34 046", newRefDateTime(), 0, 0, 0, 0, 0, 0, 1 },
      
      new Object[] { "14.08.2010 12:37:34 045", newRefDateTime(), 10,  0,  0,  0,  0,  0,  0 },
      new Object[] { "04.06.2011 12:37:34 045", newRefDateTime(),  0, 10,  0,  0,  0,  0,  0 },
      new Object[] { "04.08.2020 12:37:34 045", newRefDateTime(),  0,  0, 10,  0,  0,  0,  0 },
      new Object[] { "04.08.2010 22:37:34 045", newRefDateTime(),  0,  0,  0, 10,  0,  0,  0 },
      new Object[] { "04.08.2010 12:47:34 045", newRefDateTime(),  0,  0,  0,  0, 10,  0,  0 },
      new Object[] { "04.08.2010 12:37:44 045", newRefDateTime(),  0,  0,  0,  0,  0, 10,  0 },
      new Object[] { "04.08.2010 12:37:34 055", newRefDateTime(),  0,  0,  0,  0,  0,  0, 10 },

      new Object[] { "03.09.2010 12:37:34 045", newRefDateTime(), 30,  0,  0,  0,  0,  0,  0 },
      new Object[] { "04.02.2013 12:37:34 045", newRefDateTime(),  0, 30,  0,  0,  0,  0,  0 },
      new Object[] { "04.08.2040 12:37:34 045", newRefDateTime(),  0,  0, 30,  0,  0,  0,  0 },
      new Object[] { "07.08.2010 20:37:34 045", newRefDateTime(),  0,  0,  0, 80,  0,  0,  0 },
      new Object[] { "04.08.2010 13:57:34 045", newRefDateTime(),  0,  0,  0,  0, 80,  0,  0 },
      new Object[] { "04.08.2010 12:38:54 045", newRefDateTime(),  0,  0,  0,  0,  0, 80,  0 },
      new Object[] { "04.08.2010 12:37:34 125", newRefDateTime(),  0,  0,  0,  0,  0,  0, 80 },

    };
  }

  @SuppressWarnings("boxing")
  @DataProvider(name="createDecrement")
  public Object[][] createDecrement() {
    return new Object[][] {
        
        new Object[] { "04.08.2010 12:37:34 045", newRefDateTime(), 0, 0, 0, 0, 0, 0, 0 },
        new Object[] { "03.08.2010 12:37:34 045", newRefDateTime(), 1, 0, 0, 0, 0, 0, 0 },
        new Object[] { "04.07.2010 12:37:34 045", newRefDateTime(), 0, 1, 0, 0, 0, 0, 0 },
        new Object[] { "04.08.2009 12:37:34 045", newRefDateTime(), 0, 0, 1, 0, 0, 0, 0 },
        new Object[] { "04.08.2010 11:37:34 045", newRefDateTime(), 0, 0, 0, 1, 0, 0, 0 },
        new Object[] { "04.08.2010 12:36:34 045", newRefDateTime(), 0, 0, 0, 0, 1, 0, 0 },
        new Object[] { "04.08.2010 12:37:33 045", newRefDateTime(), 0, 0, 0, 0, 0, 1, 0 },
        new Object[] { "04.08.2010 12:37:34 044", newRefDateTime(), 0, 0, 0, 0, 0, 0, 1 },
        
        new Object[] { "25.07.2010 12:37:34 045", newRefDateTime(), 10,  0,  0,  0,  0,  0,  0 },
        new Object[] { "04.10.2009 12:37:34 045", newRefDateTime(),  0, 10,  0,  0,  0,  0,  0 },
        new Object[] { "04.08.2000 12:37:34 045", newRefDateTime(),  0,  0, 10,  0,  0,  0,  0 },
        new Object[] { "04.08.2010 02:37:34 045", newRefDateTime(),  0,  0,  0, 10,  0,  0,  0 },
        new Object[] { "04.08.2010 12:27:34 045", newRefDateTime(),  0,  0,  0,  0, 10,  0,  0 },
        new Object[] { "04.08.2010 12:37:24 045", newRefDateTime(),  0,  0,  0,  0,  0, 10,  0 },
        new Object[] { "04.08.2010 12:37:34 035", newRefDateTime(),  0,  0,  0,  0,  0,  0, 10 },

        new Object[] { "05.07.2010 12:37:34 045", newRefDateTime(), 30,  0,  0,  0,  0,  0,  0 },
        new Object[] { "04.02.2008 12:37:34 045", newRefDateTime(),  0, 30,  0,  0,  0,  0,  0 },
        new Object[] { "04.08.1980 12:37:34 045", newRefDateTime(),  0,  0, 30,  0,  0,  0,  0 },
        new Object[] { "01.08.2010 04:37:34 045", newRefDateTime(),  0,  0,  0, 80,  0,  0,  0 },
        new Object[] { "04.08.2010 11:17:34 045", newRefDateTime(),  0,  0,  0,  0, 80,  0,  0 },
        new Object[] { "04.08.2010 12:36:14 045", newRefDateTime(),  0,  0,  0,  0,  0, 80,  0 },
        new Object[] { "04.08.2010 12:37:33 965", newRefDateTime(),  0,  0,  0,  0,  0,  0, 80 },

      };
  }
  
  @Test
  public void setDateTime() {
    KDateTime datetime = newRefDateTime();
    Assert.assertEquals( datetime.toString(), "04.08.2010 12:37:34 045" );
    Assert.assertEquals( datetime.toString( "yyyy-MM-dd" ), "2010-08-04" );
    Assert.assertEquals( datetime.toString( "HH  mm  ss  SSS" ), "12  37  34  045" );
  }

  @Test(dataProvider="createIncrement")
  public void increment( String expected, KDateTime base, int days, int months, int years, int hours, int minutes, int seconds, int milliseconds ) {
    for( int i = 0; i < days; i++ ) {
      base.incDay();
    }
    for( int i = 0; i < months; i++ ) {
      base.incMonth();
    }
    for( int i = 0; i < years; i++ ) {
      base.incYear();
    }
    for( int i = 0; i < hours; i++ ) {
      base.incHours();
    }
    for( int i = 0; i < minutes; i++ ) {
      base.incMinutes();
    }
    for( int i = 0; i < seconds; i++ ) {
      base.incSeconds();
    }
    for( int i = 0; i < milliseconds; i++ ) {
      base.incMilliseconds();
    }
    Assert.assertEquals( base.toString(), expected );
  }

  @Test(dataProvider="createDecrement")
  public void decrement( String expected, KDateTime base, int days, int months, int years, int hours, int minutes, int seconds, int milliseconds ) {
    for( int i = 0; i < days; i++ ) {
      base.decDay();
    }
    for( int i = 0; i < months; i++ ) {
      base.decMonth();
    }
    for( int i = 0; i < years; i++ ) {
      base.decYear();
    }
    for( int i = 0; i < hours; i++ ) {
      base.decHours();
    }
    for( int i = 0; i < minutes; i++ ) {
      base.decMinutes();
    }
    for( int i = 0; i < seconds; i++ ) {
      base.decSeconds();
    }
    for( int i = 0; i < milliseconds; i++ ) {
      base.decMilliseconds();
    }
    Assert.assertEquals( base.toString(), expected );
  }

} /* ENDCLASS */