/**
 * Name........: MiscFunctionsTest
 * Description.: Test for various functions of the class 'MiscFunctions'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.test.util;

import com.kasisoft.lgpl.libs.common.constants.*;

import com.kasisoft.lgpl.libs.common.util.*;

import com.kasisoft.lgpl.libs.common.base.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

/**
 * Test for various functions of the class 'MiscFunctions'.
 */
public class MiscFunctionsTest {

  private static final String[] DATEPATTERNS = new String[] {
    "dd.MM.yyyy", "dd-MM-yyyy", "dd MMM - yyyy"
  };
  
  @SuppressWarnings("deprecation")
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

  @SuppressWarnings("deprecation")
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

  @Test(dataProvider="createDateValues")
  public void parseDate( String datevalue, Date expected ) {
    Date currentdate = MiscFunctions.parseDate( datevalue, DATEPATTERNS );
    Assert.assertEquals( currentdate, expected );
  }

  @Test(dataProvider="createCalendarValues")
  public void parseCalendar( String datevalue, Calendar expected ) {
    Calendar currentdate = MiscFunctions.parseCalendar( datevalue, DATEPATTERNS );
    Assert.assertEquals( currentdate, expected );
  }

  private char[][] toCharArray( String ... str ) {
    if( str == null ) {
      return null;
    }
    char[][] result = new char[ str.length ][];
    for( int i = 0; i < result.length; i++ ) {
      if( str[i] != null ) {
        result[i] = str[i].toCharArray();
      }
    }
    return result;
  }

  private byte[][] toByteArray( String ... str ) {
    if( str == null ) {
      return null;
    }
    byte[][] result = new byte[ str.length ][];
    for( int i = 0; i < result.length; i++ ) {
      if( str[i] != null ) {
        result[i] = str[i].getBytes();
      }
    }
    return result;
  }

  @DataProvider(name="createCharBuffers")
  public Object[][] createCharBuffers() {
    return new Object[][] {
      { toCharArray( "Hello" ), "Hello" },
      { toCharArray( "Hello", null, " ", "World" ), "Hello World" },
      { toCharArray( "Hello", " ", "World" ), "Hello World" },
    };
  }

  @DataProvider(name="createByteBuffers")
  public Object[][] createByteBuffers() {
    return new Object[][] {
      { toByteArray( "Hello" ), "Hello" },
      { toByteArray( "Hello", null, " ", "World" ), "Hello World" },
      { toByteArray( "Hello", " ", "World" ), "Hello World" },
    };
  }

  @Test
  public void sleepMilliseconds() {
    long time   = (long) (30000 * Math.random());
    long before = System.currentTimeMillis();
    MiscFunctions.sleep( time );
    long after  = System.currentTimeMillis();
    Assert.assertTrue( (after - before) >= time );
  }

  @Test
  public void sleepDefault() {
    Integer time   = CommonProperty.Sleep.getValue();
    long    before = System.currentTimeMillis();
    MiscFunctions.sleep();
    long after  = System.currentTimeMillis();
    Assert.assertTrue( (after - before) >= time.intValue() );
  }
  
  @Test
  public void sleepTimeUnit() {
    long time   = TimeUnit.Second.amount(5);
    long before = System.currentTimeMillis();
    MiscFunctions.sleep( 5, TimeUnit.Second );
    long after  = System.currentTimeMillis();
    Assert.assertTrue( (after - before) >= time );
  }

  @Test
  public void newInstance() {
    Object object = MiscFunctions.newInstance( false, String.class.getName(), "Frosch".getBytes() );
    Assert.assertEquals( object, "Frosch" );
  }

  @Test(expectedExceptions={FailureException.class})
  public void newInstanceFailure() {
    MiscFunctions.newInstance( true, String.class.getName(), new float[12] );
    Assert.fail();
  }

  @Test
  public void joinThread() {
    final Tupel<Boolean> outparam = new Tupel<Boolean>( Boolean.FALSE );
    Runnable runnable = new Runnable() {
      public void run() {
        for( int i = 0; i < 10; i++ ) {
          MiscFunctions.sleep();
        }
        outparam.setValues( Boolean.TRUE );
      }
    };
    Thread thread = new Thread( runnable );
    thread.start();
    MiscFunctions.joinThread( thread );
    Assert.assertEquals( outparam.getValue(), Boolean.TRUE );
  }
  
  @Test(dataProvider="createCharBuffers")
  public void joinCharBuffers( char[][] buffers, String expected ) {
    char[] joined = MiscFunctions.joinBuffers( buffers );
    Assert.assertEquals( new String( joined ), expected );
  }

  @Test(dataProvider="createByteBuffers")
  public void joinByteBuffers( byte[][] buffers, String expected ) {
    byte[] joined = MiscFunctions.joinBuffers( buffers );
    Assert.assertEquals( new String( joined ), expected );
  }
  
  @DataProvider(name="createCharInsertion")
  public Object[][] createCharInsertion() {
    return new Object[][] {
      { "".toCharArray(), "Hello World".toCharArray(), Integer.valueOf(0), "" },  
      { "".toCharArray(), "Hello World".toCharArray(), Integer.valueOf(5), "" },  
      { "Hello World".toCharArray(), " small ".toCharArray(), Integer.valueOf(0), " small Hello World" },  
      { "Hello World".toCharArray(), " small ".toCharArray(), Integer.valueOf(5), "Hello small  World" },  
      { "Hello World".toCharArray(), " small ".toCharArray(), Integer.valueOf(50), "Hello World" },  
    };
  }
  
  @Test(dataProvider="createCharInsertion")
  public void insertChars( char[] dest, char[] insert, int index, String expected ) {
    char[] combined = MiscFunctions.insert( dest, insert, index );
    Assert.assertEquals( new String( combined ), expected );
  }

} /* ENDCLASS */
