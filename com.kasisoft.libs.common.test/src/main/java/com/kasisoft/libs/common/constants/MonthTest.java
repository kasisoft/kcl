/**
 * Name........: MonthTest
 * Description.: Tests for the class 'Month'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.constants;

import org.testng.annotations.*;

import org.testng.*;

/**
 * Tests for the class 'Month'.
 */
@Test(groups="all")
public class MonthTest {

  @DataProvider(name="checkMonth")
  public Object[][] createData() {
    return new Object[][] {
      { Month.January   , Integer.valueOf( 31 ), Integer.valueOf( 2010 ), Weekday.Friday    },
      { Month.February  , Integer.valueOf( 28 ), Integer.valueOf( 2010 ), Weekday.Monday    },
      { Month.March     , Integer.valueOf( 31 ), Integer.valueOf( 2010 ), Weekday.Monday    },
      { Month.April     , Integer.valueOf( 30 ), Integer.valueOf( 2010 ), Weekday.Thursday  },
      { Month.May       , Integer.valueOf( 31 ), Integer.valueOf( 2010 ), Weekday.Saturday  },
      { Month.June      , Integer.valueOf( 30 ), Integer.valueOf( 2010 ), Weekday.Tuesday   },
      { Month.July      , Integer.valueOf( 31 ), Integer.valueOf( 2010 ), Weekday.Thursday  },
      { Month.August    , Integer.valueOf( 31 ), Integer.valueOf( 2010 ), Weekday.Sunday    },
      { Month.September , Integer.valueOf( 30 ), Integer.valueOf( 2010 ), Weekday.Wednesday },
      { Month.October   , Integer.valueOf( 31 ), Integer.valueOf( 2010 ), Weekday.Friday    },
      { Month.November  , Integer.valueOf( 30 ), Integer.valueOf( 2010 ), Weekday.Monday    },
      { Month.December  , Integer.valueOf( 31 ), Integer.valueOf( 2010 ), Weekday.Wednesday },
      { Month.February  , Integer.valueOf( 29 ), Integer.valueOf( 2000 ), Weekday.Tuesday   },
    };
  }

  @DataProvider(name="monthNavigation")
  public Object[][] createNavigationData() {
    return new Object[][] {
      { Month.January   , Month.December  , Month.February    },
      { Month.February  , Month.January   , Month.March       },
      { Month.March     , Month.February  , Month.April       },
      { Month.April     , Month.March     , Month.May         },
      { Month.May       , Month.April     , Month.June        },
      { Month.June      , Month.May       , Month.July        },
      { Month.July      , Month.June      , Month.August      },
      { Month.August    , Month.July      , Month.September   },
      { Month.September , Month.August    , Month.October     },
      { Month.October   , Month.September , Month.November    },
      { Month.November  , Month.October   , Month.December    },
      { Month.December  , Month.November  , Month.January     },
    };
  }

  @Test(dataProvider="checkMonth")
  public void checkMonth( Month month, int daycount, int year, Weekday weekday ) { 
    Assert.assertEquals( month.getDayCount( year ), daycount );
    Assert.assertEquals( month.getFirstWeekday( year ), weekday );
  }
  
  @Test(dataProvider="monthNavigation")
  public void monthNavigation( Month current, Month before, Month after ) {
    Assert.assertEquals( before , current.previous() );
    Assert.assertEquals( after  , current.next()     );
  }
  
} /* ENDCLASS */
