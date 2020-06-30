package com.kasisoft.libs.common.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertNotNull;

import com.kasisoft.libs.common.types.OutParam;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Test for various functions of the class 'MiscFunctions'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@SuppressWarnings("deprecation")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MiscFunctionsTest {

  @DataProvider(name = "data_getGravatarLink")
  public Object[][] data_getGravatarLink() {
    return new Object[][] {
      { null                                                                      , null                                  , null },
      { null                                                                      , null                                  , 12   },
      { "https://www.gravatar.com/avatar/8fea46f5bc403f6949300c3007d2f18d"        , " Daniel.KASMEROGLU@kasisoft.net \n"  , null },
      { "https://www.gravatar.com/avatar/8fea46f5bc403f6949300c3007d2f18d"        , "daniel.kasmeroglu@kasisoft.net"      , null },
      { "https://www.gravatar.com/avatar/8fea46f5bc403f6949300c3007d2f18d?s=100"  , "daniel.kasmeroglu@kasisoft.net"      , 100  },
    };
  }
  
  @Test(groups = "all", dataProvider = "data_getGravatarLink")
  public void getGravatarLink( String expected, String email, Integer size ) {
    assertThat( MiscFunctions.getGravatarLink( email, size ), is( expected ) );
  }
  
  @DataProvider(name="data_toSet")
  public Object[][] data_toSet() {
    return new Object[][] {
      {Arrays.asList("Otto", "Fred", "Ginger"), Arrays.asList("Fred", "Ginger", "Otto")},
      {Arrays.asList("Otto", "Fred", "Otto", "Ginger"), Arrays.asList("Fred", "Ginger", "Otto")},
    };
  }

  @Test(dataProvider = "data_toSet", groups = "all")
  public void toSet(List<String> list, List<String> expected) {
    var altered = MiscFunctions.toUniqueList( list );
    assertNotNull(altered);
    assertThat(altered.size(), is(expected.size()));
    for (var i = 0; i < altered.size(); i++) {
      assertThat( altered.get(i), is( expected.get(i) ) );
    }
  }
  
  @Test(groups = "all")
  public void joinThread() {
    final var outparam = new OutParam<>(Boolean.FALSE);
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        MiscFunctions.sleep(10000);
        outparam.setValue(Boolean.TRUE);
      }
    };
    var thread = new Thread(runnable);
    thread.start();
    MiscFunctions.joinThread(thread);
    assertThat(outparam.getValue(), is(Boolean.TRUE));
  }
  
  @DataProvider(name = "data_isLeapYear")
  public Object[][] data_isLeapYear() {
    return new Object[][] {
      {Integer.valueOf(1900), Boolean.FALSE},
      {Integer.valueOf(1901), Boolean.FALSE},
      {Integer.valueOf(1904), Boolean.TRUE},
      {Integer.valueOf(2000), Boolean.TRUE},
      {Integer.valueOf(2001), Boolean.FALSE}
    };
  }
  
  @Test(dataProvider = "data_isLeapYear", groups = "all")
  public void isLeapYear(int year, boolean expected) {
    assertThat(MiscFunctions.isLeapYear(year), is(expected));
  }

  private Date createDate(int year) {
    var calendar = Calendar.getInstance();
    calendar.set( Calendar.YEAR, year );
    return calendar.getTime();
  }

  @DataProvider(name="data_isLeapYear__Date")
  public Object[][] data_isLeapYear__Date() {
    return new Object[][] {
      {createDate(1900), Boolean.FALSE},
      {createDate(1901), Boolean.FALSE},
      {createDate(1904), Boolean.TRUE},
      {createDate(2000), Boolean.TRUE},
      {createDate(2001), Boolean.FALSE}
    };
  }

  @Test(dataProvider = "data_isLeapYear__Date", groups = "all")
  public void isLeapYear__Date(Date year, boolean expected) {
    assertThat(MiscFunctions.isLeapYear(year), is(expected));
  }
  
  @DataProvider(name = "data_repeat")
  public Object[][] data_repeat() {
    return new Object[][] {
      { Integer.valueOf(0), null, Arrays.asList() },  
      { Integer.valueOf(1), null, Arrays.asList( new Object[] { null } ) },
      { Integer.valueOf(5), null, Arrays.asList( null, null, null, null, null ) },
      { Integer.valueOf(0), "Dodo", Arrays.asList() },  
      { Integer.valueOf(1), "Dodo", Arrays.asList( "Dodo" ) },
      { Integer.valueOf(5), "Dodo", Arrays.asList( "Dodo", "Dodo", "Dodo", "Dodo", "Dodo" ) },
    };
  }
  
  @Test(dataProvider = "data_repeat", groups = "all")
  public <T> void repeat(int count, T element, List<T> expected) {
    var actual = MiscFunctions.repeat(count, element);
    assertThat(actual, notNullValue());
    assertThat(actual.size(), is(count));
    assertThat(actual, is(expected));
  }
  
  @Test(groups = "all")
  public void toPairs() {
    
    var noentries = MiscFunctions.toPairs();
    assertNotNull(noentries);
    assertThat(noentries.size(), is(0));

    var incompletePair = MiscFunctions.toPairs("key");
    assertNotNull(incompletePair);
    assertThat(incompletePair.size(), is(0));

    var onePair = MiscFunctions.toPairs( "key", "val" );
    assertNotNull(onePair);
    assertThat(onePair.size(), is(1));

    var stillOnePair = MiscFunctions.toPairs( "key", "val", "nextKey" );
    assertNotNull(stillOnePair);
    assertThat(stillOnePair.size(), is(1));

  }
  
  @Test(groups = "all")
  public void executeWithoutExit() {

    var exitcode1 = MiscFunctions.executeWithoutExit(() -> System.exit(13));
    assertThat(exitcode1, is(13));
    
    var exitcode2 = MiscFunctions.executeWithoutExit(() -> System.out.println("No exit here")); 
    assertThat(exitcode2, is(0));
    
  }

  @Test(groups = "all")
  public void wrapToExtendedList__newList() {
    
    var list1 = MiscFunctions.wrapToExtendedList(new ArrayList<String>());
    assertThat(list1.size(), is(0));
    
    var list2 = MiscFunctions.wrapToExtendedList(Arrays.asList("BLA", "BLUB"));
    assertThat(list2.size(), is(2));
    assertThat(list2.get(0), is("BLA"));
    assertThat(list2.get(1), is("BLUB"));
    
  }
  
  @Test(groups = "all")
  public void wrapToExtendedList__add() {
    
    var list1 = MiscFunctions.wrapToExtendedList(new ArrayList<String>());
    list1.add("BLA");
    
    list1.add(0, "BLUB");
    assertThat(list1.size(), is(2));
    assertThat(list1.get(0), is("BLUB"));
    assertThat(list1.get(1), is("BLA"));
    
    list1.add(-1, "FROG");
    assertThat(list1.size(), is(3));
    assertThat(list1.get(0), is("BLUB"));
    assertThat(list1.get(1), is("FROG"));
    assertThat(list1.get(2), is("BLA"));

    list1.addAll(-1, Arrays.asList("BLAU", "KRAUT"));
    assertThat(list1.size(), is(5));
    assertThat(list1.get(0), is("BLUB"));
    assertThat(list1.get(1), is("FROG"));
    assertThat(list1.get(2), is("BLAU"));
    assertThat(list1.get(3), is("KRAUT"));
    assertThat(list1.get(4), is("BLA"));

  }
  
  @Test(groups = "all")
  public void twrapToExtendedList__sublist() {
    
    var list1 = MiscFunctions.wrapToExtendedList(Arrays.asList("BLA", "BLUB", "BLAU", "KRAUT", "FROG"));
    var list2 = list1.subList(1, -1);
    assertThat(list2, is( notNullValue()));
    assertThat(list2.size(), is(3));
    assertThat(list2.get(0), is("BLUB"));
    assertThat(list2.get(1), is("BLAU"));
    assertThat(list2.get(2), is("KRAUT"));

  }

} /* ENDCLASS */
