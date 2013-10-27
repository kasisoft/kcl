/**
 * Name........: WeekdayTest
 * Description.: Tests for the class 'Weekday'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.constants;

import org.testng.annotations.*;

import org.testng.*;

/**
 * Tests for the class 'Weekday'.
 */
@Test(groups="all")
public class WeekdayTest {

  @DataProvider(name="checkWeekday")
  public Object[][] createData() {
    return new Object[][] {
      { Weekday.Monday      , Weekday.Sunday        , Weekday.Tuesday     },
      { Weekday.Tuesday     , Weekday.Monday        , Weekday.Wednesday   },
      { Weekday.Wednesday   , Weekday.Tuesday       , Weekday.Thursday    },
      { Weekday.Thursday    , Weekday.Wednesday     , Weekday.Friday      },
      { Weekday.Friday      , Weekday.Thursday      , Weekday.Saturday    },
      { Weekday.Saturday    , Weekday.Friday        , Weekday.Sunday      },
      { Weekday.Sunday      , Weekday.Saturday      , Weekday.Monday      },
    };
  }
  
  @Test(dataProvider="checkWeekday")
  public void checkWeekday( Weekday month, Weekday before, Weekday after ) { 
    Assert.assertEquals( month.previous() , before );
    Assert.assertEquals( month.next()     , after  );
  }
  
} /* ENDCLASS */
