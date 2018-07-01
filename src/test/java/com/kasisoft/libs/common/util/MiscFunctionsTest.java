package com.kasisoft.libs.common.util;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import com.kasisoft.libs.common.model.*;
import com.kasisoft.libs.common.sys.*;

import org.testng.annotations.*;

import java.util.*;

import lombok.experimental.*;

import lombok.*;

/**
 * Test for various functions of the class 'MiscFunctions'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@SuppressWarnings("deprecation")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MiscFunctionsTest {

  static final String[] DATEPATTERNS = new String[] {
    "dd.MM.yyyy", "dd-MM-yyyy", "dd MMM - yyyy"
  };
  
  private <T> List<T> toList( T ... args ) {
    List<T> result = new ArrayList<>();
    for( T arg : args ) {
      result.add( arg );
    }
    return result;
  }

  @DataProvider(name="createDateValues")
  public Object[][] createDateValues() {
    Date date = new Date( 110, 3, 13 );
    return new Object[][] {
      { "13.04.2010"    , date },  
      { "13-04-2010"    , date },  
      { "13 Apr - 2010" , date },
      { "bla bla"       , null } 
    };
  }

  @DataProvider(name="createCalendarValues")
  public Object[][] createCalendarValues() {
    Date      date      = new Date( 110, 3, 13 );
    Calendar  calendar  = Calendar.getInstance();
    calendar.setTime( date );
    return new Object[][] {
      { "13.04.2010"    , calendar  },  
      { "13-04-2010"    , calendar  },  
      { "13 Apr - 2010" , calendar  },
      { "bla bla"       , null      } 
    };
  }

  @DataProvider(name="createToSet")
  public Object[][] createToSet() {
    return new Object[][] {
      { toList( "Otto", "Fred", "Ginger"         ), toList( "Fred", "Ginger", "Otto" ) },
      { toList( "Otto", "Fred", "Otto", "Ginger" ), toList( "Fred", "Ginger", "Otto" ) },
    };
  }
  
  @DataProvider(name="createParseBoolean")
  public Object[][] createParseBoolean() {
    return new Object[][] {
      { "true"  , Boolean.TRUE  },
      { "yes"   , Boolean.TRUE  },
      { "ja"    , Boolean.TRUE  },
      { "ein"   , Boolean.TRUE  },
      { "on"    , Boolean.TRUE  },
      { "an"    , Boolean.TRUE  },
      { "1"     , Boolean.TRUE  },
      { "-1"    , Boolean.TRUE  },
      { ""      , Boolean.FALSE },
      { "false" , Boolean.FALSE },
      { "no"    , Boolean.FALSE },
      { "nein"  , Boolean.FALSE },
      { "off"   , Boolean.FALSE },
      { "aus"   , Boolean.FALSE },
      { "0"     , Boolean.FALSE },
    };
  }
  
  @Test(dataProvider="createDateValues", groups="all")
  public void parseDate( String datevalue, Date expected ) {
    Date currentdate = MiscFunctions.parseDate( datevalue, DATEPATTERNS );
    assertThat( currentdate, is( expected ) );
  }

  @Test(dataProvider="createCalendarValues", groups="all")
  public void parseCalendar( String datevalue, Calendar expected ) {
    Calendar currentdate = MiscFunctions.parseCalendar( datevalue, DATEPATTERNS );
    assertThat( currentdate, is( expected ) );
  }

  @Test(dataProvider="createParseBoolean", groups="all")
  public void parseBoolean( String value, Boolean expected ) {
    assertThat( MiscFunctions.parseBoolean( value ), is( expected.booleanValue() ) );
  }
  
  @Test(groups="all")
  public void joinThread() {
    final Tupel<Boolean> outparam = new Tupel<>( Boolean.FALSE );
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        for( int i = 0; i < 10; i++ ) {
          try {
            Thread.sleep( 1000 );
          } catch( InterruptedException ex ) {
          }
        }
        outparam.setValues( Boolean.TRUE );
      }
    };
    Thread thread = new Thread( runnable );
    thread.start();
    MiscFunctions.joinThread( thread );
    assertThat( outparam.getValue(), is( Boolean.TRUE ) );
  }
  
  @Test(groups="all")
  public void expandVariables() {
    String template = null;
    if( SystemInfo.getRunningOS().isUnixLike() ) {
      template = "The name of the user is: $user.name !";;
    } else {
      template = "The name of the user is: %user.name% !";
    }
    String result   = MiscFunctions.expandVariables( template );
    assertThat( result, is( String.format( "The name of the user is: %s !", System.getProperty( "user.name" ) ) ) );
  }
  
  @Test(dataProvider="createToSet", groups="all")
  public void toSet( List<String> list, List<String> expected ) {
    List<String> altered = MiscFunctions.toUniqueList( list );
    assertThat( altered, is( notNullValue() ) );
    assertThat( altered.size(), is( expected.size() ) );
    for( int i = 0; i < altered.size(); i++ ) {
      assertThat( altered.get(i), is( expected.get(i) ) );
    }
  }
  
  @Test(dataProvider="createIsLeapYearInt", groups="all")
  public void isLeapYear( int year, boolean expected ) {
    assertThat( MiscFunctions.isLeapYear( year ), is( expected ) );
  }

  @Test(dataProvider="createIsLeapYearDate", groups="all")
  public void isLeapYear( Date year, boolean expected ) {
    assertThat( MiscFunctions.isLeapYear( year ), is( expected ) );
  }

  @DataProvider(name="createIsLeapYearInt")
  public Object[][] createIsLeapYearInt() {
    return new Object[][] {
      { Integer.valueOf( 1900 ), Boolean.FALSE },
      { Integer.valueOf( 1901 ), Boolean.FALSE },
      { Integer.valueOf( 1904 ), Boolean.TRUE  },
      { Integer.valueOf( 2000 ), Boolean.TRUE  },
      { Integer.valueOf( 2001 ), Boolean.FALSE }
    };
  }

  @DataProvider(name="createIsLeapYearDate")
  public Object[][] createIsLeapYearDate() {
    return new Object[][] {
      { createDate( 1900 ), Boolean.FALSE },
      { createDate( 1901 ), Boolean.FALSE },
      { createDate( 1904 ), Boolean.TRUE  },
      { createDate( 2000 ), Boolean.TRUE  },
      { createDate( 2001 ), Boolean.FALSE }
    };
  }
  
  private Date createDate( int year ) {
    Calendar calendar = Calendar.getInstance();
    calendar.set( Calendar.YEAR, year );
    return calendar.getTime();
  }
  
  @DataProvider(name="repeatData")
  public Object[][] repeatData() {
    return new Object[][] {
      { Integer.valueOf(0), null, Arrays.asList() },  
      { Integer.valueOf(1), null, Arrays.asList( new Object[] { null } ) },
      { Integer.valueOf(5), null, Arrays.asList( null, null, null, null, null ) },
      { Integer.valueOf(0), "Dodo", Arrays.asList() },  
      { Integer.valueOf(1), "Dodo", Arrays.asList( "Dodo" ) },
      { Integer.valueOf(5), "Dodo", Arrays.asList( "Dodo", "Dodo", "Dodo", "Dodo", "Dodo" ) },
    };
  }
  
  @Test(dataProvider="repeatData", groups="all")
  public <T> void repeat( int count, T element, List<T> expected ) {
    List<T> actual = MiscFunctions.repeat( count, element );
    assertThat( actual, notNullValue() );
    assertThat( actual.size(), is( count ) );
    assertThat( actual, is( expected ) );
  }
  
  @Test(groups="all")
  public void toPairs() {
    
    List<Pair<String, String>> noentries = MiscFunctions.toPairs();
    assertThat( noentries, notNullValue() );
    assertThat( noentries.size(), is(0) );

    List<Pair<String, String>> incompletePair = MiscFunctions.toPairs( "key" );
    assertThat( incompletePair, notNullValue() );
    assertThat( incompletePair.size(), is(0) );

    List<Pair<String, String>> onePair = MiscFunctions.toPairs( "key", "val" );
    assertThat( onePair, notNullValue() );
    assertThat( onePair.size(), is(1) );

    List<Pair<String, String>> stillOnePair = MiscFunctions.toPairs( "key", "val", "nextKey" );
    assertThat( stillOnePair, notNullValue() );
    assertThat( stillOnePair.size(), is(1) );

  }
  
  @Test(groups="all")
  public void newComparator() {
    
    Comparator<DummyPojo> comparator = MiscFunctions.newComparator( 
      DummyPojo::getParam1, 
      DummyPojo::getParam2, 
      DummyPojo::getParam3, 
      DummyPojo::isParam4
    );
    
    DummyPojo obj1 = new DummyPojo( "a", 12, -3L, true );
    DummyPojo obj2 = new DummyPojo( "a", 12, -3L, true );
    assertThat( comparator.compare( obj1, obj2 ), is(0) );

    DummyPojo obj3 = new DummyPojo( "a", 15, -3L, true );
    DummyPojo obj4 = new DummyPojo( "a", 12, -3L, true );
    assertThat( comparator.compare( obj3, obj4 ), is(1) );

    DummyPojo obj5 = new DummyPojo( "a", 10, -3L, true );
    DummyPojo obj6 = new DummyPojo( "a", 12, -3L, true );
    assertThat( comparator.compare( obj5, obj6 ), is(-1) );

    DummyPojo obj7 = new DummyPojo( "a", 12, null, true );
    DummyPojo obj8 = new DummyPojo( "a", 12, -3L, true );
    assertThat( comparator.compare( obj7, obj8 ), is(-1) );

    DummyPojo obj9  = new DummyPojo( "a", 12, -3L, true );
    DummyPojo obj10 = new DummyPojo( "a", 12, null, true );
    assertThat( comparator.compare( obj9, obj10 ), is(1) );

  }
  
  private static final List<String> PARENTHESIZE_DATA = Arrays.asList( new String[] {
    "/root1",
    "/root1/child1",
    "/root1/child1/child7",
    "/root1/child1/child8",
    "/root1/child1/child9",
    "/root1/child1/child10",
    "/root1/child2",
    "/root1/child2/child11",
    "/root1/child2/child12",
    "/root1/child2/child13",
    "/root1/child3",
    "/root1/child3/child14",
    "/root1/child4",
    "/root1/child5",
    "/root1/child6",
    "/root2",
    "/root2/child1",
    "/root2/child1/child7",
    "/root2/child1/child8",
    "/root2/child1/child9",
    "/root2/child1/child10",
    "/root2/child2",
    "/root2/child2/child11",
    "/root2/child2/child12",
    "/root2/child2/child13",
    "/root2/child3",
    "/root2/child3/child14",
    "/root2/child4",
    "/root2/child5",
    "/root2/child6",
    "/root3/child1/child2/child3"
  });

  @Test(groups = "all")
  public void parenthesize() {
    Set<Tupel<String>> children  = new HashSet<>();
    MiscFunctions.parenthesize(
      PARENTHESIZE_DATA,
      $ -> $,
      ($s, $p, $c) -> children.add( new Tupel<>( $s, $p, $c ) )
    );
    assertThat( children.size(), is(34) );
    for( Tupel<String> child : children ) {
      if( child.getValues()[1] != null ) {
        String parent = child.getValues()[1];
        String sub    = child.getValues()[2];
        assertTrue( sub.startsWith( parent ) );
      }
    }
  }
  
  @Data @AllArgsConstructor
  @FieldDefaults(level = AccessLevel.PRIVATE)
  private static class DummyPojo {
  
    String    param1;
    int       param2;
    Long      param3;
    boolean   param4;
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
