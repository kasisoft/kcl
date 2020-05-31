package com.kasisoft.libs.common.old.constants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;

import java.util.Date;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for the class 'Month'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MonthTest {

  SimpleDateFormat   formatter = new SimpleDateFormat( "dd.MM.yyyy" );
  
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

  @DataProvider(name="valueOf")
  public Object[][] createValueOf() {
    return new Object[][] {
      { Month.January   ,  1 },
      { Month.February  ,  2 },
      { Month.March     ,  3 },
      { Month.April     ,  4 },
      { Month.May       ,  5 },
      { Month.June      ,  6 },
      { Month.July      ,  7 },
      { Month.August    ,  8 },
      { Month.September ,  9 },
      { Month.October   , 10 },
      { Month.November  , 11 },
      { Month.December  , 12 },
    };
  }

  @Test(dataProvider="checkMonth", groups="all")
  public void checkMonth( Month month, int daycount, int year, Weekday weekday ) { 
    assertThat( month.getDayCount     ( year ), is( daycount ) );
    assertThat( month.getFirstWeekday ( year ), is( weekday  ) );
  }
  
  @Test(dataProvider="monthNavigation", groups="all")
  public void monthNavigation( Month current, Month before, Month after ) {
    assertThat( before , is( current.previous () ) );
    assertThat( after  , is( current.next     () ) );
  }
  
  @Test(dataProvider="valueOf", groups="all")
  public void valueOf( Month expected, int month ) throws Exception {
    Date date = formatter.parse( String.format( "01.%02d.2015", month ) );
    assertThat( Month.valueOf( date ), is( expected ) );
  }
  
} /* ENDCLASS */
