package com.kasisoft.libs.common.old.constants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests for the class 'Weekday'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
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
  
  @Test(dataProvider="checkWeekday", groups="all")
  public void checkWeekday( Weekday month, Weekday before, Weekday after ) { 
    assertThat( month.previous() , is( before ) );
    assertThat( month.next()     , is( after  ) );
  }
  
} /* ENDCLASS */