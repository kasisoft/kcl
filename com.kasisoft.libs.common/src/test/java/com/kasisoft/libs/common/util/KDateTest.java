/**
 * Name........: KDateTest
 * Description.: Tests for the type 'KDate'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.constants.*;

import org.testng.annotations.*;

import org.testng.*;

@Test(groups="all")
public class KDateTest {

  private KDate newRefDate() {
    KDate date = new KDate();
    date.setDay( 4 );
    date.setMonth( Month.August );
    date.setYear( 2010 );
    return date;
  }
  
  @SuppressWarnings("boxing")
  @DataProvider(name="createIncrement")
  public Object[][] createIncrement() {
    return new Object[][] {
      
      new Object[] { "04.08.2010", newRefDate(), 0, 0, 0 },
      new Object[] { "05.08.2010", newRefDate(), 1, 0, 0 },
      new Object[] { "04.09.2010", newRefDate(), 0, 1, 0 },
      new Object[] { "04.08.2011", newRefDate(), 0, 0, 1 },
      
      new Object[] { "14.08.2010", newRefDate(), 10,  0,  0 },
      new Object[] { "04.06.2011", newRefDate(),  0, 10,  0 },
      new Object[] { "04.08.2020", newRefDate(),  0,  0, 10 },

      new Object[] { "03.09.2010", newRefDate(), 30,  0,  0 },
      new Object[] { "04.02.2013", newRefDate(),  0, 30,  0 },
      new Object[] { "04.08.2040", newRefDate(),  0,  0, 30 },

    };
  }

  @SuppressWarnings("boxing")
  @DataProvider(name="createDecrement")
  public Object[][] createDecrement() {
    return new Object[][] {
        
        new Object[] { "04.08.2010", newRefDate(), 0, 0, 0 },
        new Object[] { "03.08.2010", newRefDate(), 1, 0, 0 },
        new Object[] { "04.07.2010", newRefDate(), 0, 1, 0 },
        new Object[] { "04.08.2009", newRefDate(), 0, 0, 1 },
        
        new Object[] { "25.07.2010", newRefDate(), 10,  0,  0 },
        new Object[] { "04.10.2009", newRefDate(),  0, 10,  0 },
        new Object[] { "04.08.2000", newRefDate(),  0,  0, 10 },
        
        new Object[] { "05.07.2010", newRefDate(), 30,  0,  0 },
        new Object[] { "04.02.2008", newRefDate(),  0, 30,  0 },
        new Object[] { "04.08.1980", newRefDate(),  0,  0, 30 },

      };
  }
  
  @Test
  public void setDate() {
    KDate date = newRefDate();
    Assert.assertEquals( date.toString(), "04.08.2010" );
    Assert.assertEquals( date.toString( "yyyy-MM-dd" ), "2010-08-04" );
  }

  @Test(dataProvider="createIncrement")
  public void increment( String expected, KDate base, int days, int months, int years ) {
    for( int i = 0; i < days; i++ ) {
      base.incDay();
    }
    for( int i = 0; i < months; i++ ) {
      base.incMonth();
    }
    for( int i = 0; i < years; i++ ) {
      base.incYear();
    }
    Assert.assertEquals( base.toString(), expected );
  }

  @Test(dataProvider="createDecrement")
  public void decrement( String expected, KDate base, int days, int months, int years ) {
    for( int i = 0; i < days; i++ ) {
      base.decDay();
    }
    for( int i = 0; i < months; i++ ) {
      base.decMonth();
    }
    for( int i = 0; i < years; i++ ) {
      base.decYear();
    }
    Assert.assertEquals( base.toString(), expected );
  }

} /* ENDCLASS */