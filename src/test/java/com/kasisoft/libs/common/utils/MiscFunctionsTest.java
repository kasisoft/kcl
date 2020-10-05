package com.kasisoft.libs.common.utils;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import com.kasisoft.libs.common.types.*;

import org.testng.annotations.*;

import java.util.*;

import java.time.*;

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
  
  @DataProvider(name="data_toUniqueList")
  public Object[][] data_toUniqueList() {
    return new Object[][] {
      {Arrays.asList("Otto", "Fred", "Ginger"), Arrays.asList("Fred", "Ginger", "Otto")},
      {Arrays.asList("Otto", "Fred", "Otto", "Ginger"), Arrays.asList("Fred", "Ginger", "Otto")},
    };
  }

  @Test(dataProvider = "data_toUniqueList", groups = "all")
  public void toUniqueList(List<String> list, List<String> expected) {
    
    var altered1 = MiscFunctions.toUniqueList(null);
    assertNotNull(altered1);
    assertTrue(altered1.isEmpty());
    
    var altered2 = MiscFunctions.toUniqueList(list);
    assertNotNull(altered2);
    assertThat(altered2.size(), is(expected.size()));
    for (var i = 0; i < altered2.size(); i++) {
      assertThat(altered2.get(i), is(expected.get(i)));
    }
    
  }

  @Test(dataProvider = "data_toUniqueList", groups = "all")
  public void toSet(List<String> list, List<String> expected) {
    
    var altered1 = MiscFunctions.toSet((String[]) null);
    assertNotNull(altered1);
    assertTrue(altered1.isEmpty());
    
    var altered2 = MiscFunctions.toSet(list.toArray(new String[list.size()]));
    assertNotNull(altered2);
    assertThat(altered2.size(), is(expected.size()));
    for (var i = 0; i < expected.size(); i++) {
      assertTrue(altered2.contains(expected.get(i)));
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

  @DataProvider(name="data_isLeapYear__OffsetDateTime")
  public Object[][] data_isLeapYear__OffsetDateTime() {
    return new Object[][] {
      {OffsetDateTime.ofInstant(createDate(1900).toInstant(), ZoneId.of("UTC")), Boolean.FALSE},
      {OffsetDateTime.ofInstant(createDate(1901).toInstant(), ZoneId.of("UTC")), Boolean.FALSE},
      {OffsetDateTime.ofInstant(createDate(1904).toInstant(), ZoneId.of("UTC")), Boolean.TRUE},
      {OffsetDateTime.ofInstant(createDate(2000).toInstant(), ZoneId.of("UTC")), Boolean.TRUE},
      {OffsetDateTime.ofInstant(createDate(2001).toInstant(), ZoneId.of("UTC")), Boolean.FALSE}
    };
  }

  @Test(dataProvider = "data_isLeapYear__OffsetDateTime", groups = "all")
  public void isLeapYear__OffsetDateTime(OffsetDateTime year, boolean expected) {
    assertThat(MiscFunctions.isLeapYear(year), is(expected));
  }

  @DataProvider(name="data_isLeapYear__LocalDateTime")
  public Object[][] data_isLeapYear__LocalDateTime() {
    return new Object[][] {
      {LocalDateTime.ofInstant(createDate(1900).toInstant(), ZoneId.of("UTC")), Boolean.FALSE},
      {LocalDateTime.ofInstant(createDate(1901).toInstant(), ZoneId.of("UTC")), Boolean.FALSE},
      {LocalDateTime.ofInstant(createDate(1904).toInstant(), ZoneId.of("UTC")), Boolean.TRUE},
      {LocalDateTime.ofInstant(createDate(2000).toInstant(), ZoneId.of("UTC")), Boolean.TRUE},
      {LocalDateTime.ofInstant(createDate(2001).toInstant(), ZoneId.of("UTC")), Boolean.FALSE}
    };
  }

  @Test(dataProvider = "data_isLeapYear__LocalDateTime", groups = "all")
  public void isLeapYear__LocalDateTime(LocalDateTime year, boolean expected) {
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
    
    var noentries1 = MiscFunctions.toPairs();
    assertNotNull(noentries1);
    assertThat(noentries1.size(), is(0));

    var noentries2 = MiscFunctions.toPairs((String[]) null);
    assertNotNull(noentries2);
    assertThat(noentries2.size(), is(0));

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
  
  @Test(groups = "all")
  public void toMap() {
    
    var map1 = MiscFunctions.toMap("key1", "value1", "key2", "value2");
    assertNotNull(map1);
    assertThat(map1.get("key1"), is("value1"));
    assertThat(map1.get("key2"), is("value2"));
    assertNull(map1.get("key3"));

    var map2 = MiscFunctions.toMap();
    assertNotNull(map2);
    assertTrue(map2.isEmpty());

    var map3 = MiscFunctions.toMap((Object[]) null);
    assertNotNull(map3);
    assertTrue(map3.isEmpty());

  }

  @Test(groups = "all")
  public void gcd() {
    assertThat(MiscFunctions.gcd(20, 15), is(5));
    assertThat(MiscFunctions.gcd(99, 44), is(11));
  }
  
  @Test(groups = "all")
  public void trimLeading() {
    
    var list1        = new ArrayList<String>(Arrays.asList(null, null, null, "str1", "str2", null, null));
    var trimmed1     = MiscFunctions.trimLeading(list1);
    assertThat(trimmed1, is(Arrays.asList("str1", "str2", null, null)));

    var list2        = new ArrayList<String>();
    var trimmed2     = MiscFunctions.trimLeading(list2);
    assertNotNull(trimmed2);
    assertTrue(trimmed2.isEmpty());

  }

  @Test(groups = "all")
  public void trimTrailing() {
    
    var list1        = new ArrayList<String>(Arrays.asList(null, null, null, "str1", "str2", null, null));
    var trimmed1     = MiscFunctions.trimTrailing(list1);
    assertThat(trimmed1, is(Arrays.asList(null, null, null, "str1", "str2")));

    var list2        = new ArrayList<String>();
    var trimmed2     = MiscFunctions.trimTrailing(list2);
    assertNotNull(trimmed2);
    assertTrue(trimmed2.isEmpty());

  }

  @Test
  public void trim() {
    
    var list1        = new ArrayList<String>(Arrays.asList(null, null, null, "str1", "str2", null, null));
    var trimmed1     = MiscFunctions.trim(list1);
    assertThat(trimmed1, is(Arrays.asList("str1", "str2")));
    
    var list2        = new ArrayList<String>();
    var trimmed2     = MiscFunctions.trim(list2);
    assertNotNull(trimmed2);
    assertTrue(trimmed2.isEmpty());

  }

  @Test(groups = "all")
  public void parseBoolean() {
    assertThat(MiscFunctions.parseBoolean("false"), is(false));
    assertNull(MiscFunctions.parseBoolean("Afalse"));
    assertNull(MiscFunctions.parseBoolean(null));
  }

  
  @Test(groups = "all")
  public void parseByte() {
    assertThat(MiscFunctions.parseByte("54"), is((byte) 54));
    assertNull(MiscFunctions.parseByte("A54"));
    assertNull(MiscFunctions.parseByte(null));
  }

  @Test(groups = "all")
  public void parseShort() {
    assertThat(MiscFunctions.parseShort("54"), is((short) 54));
    assertNull(MiscFunctions.parseShort("A54"));
    assertNull(MiscFunctions.parseShort(null));
  }

  @Test(groups = "all")
  public void parseInt() {
    assertThat(MiscFunctions.parseInt("54"), is(54));
    assertNull(MiscFunctions.parseInt("A54"));
    assertNull(MiscFunctions.parseInt(null));
  }

  @Test(groups = "all")
  public void parseLong() {
    assertThat(MiscFunctions.parseLong("54"), is(54L));
    assertNull(MiscFunctions.parseLong("A54"));
    assertNull(MiscFunctions.parseLong(null));
  }

  @Test(groups = "all")
  public void parseFloat() {
    assertThat(MiscFunctions.parseFloat("54.3"), is(54.3f));
    assertNull(MiscFunctions.parseFloat("A54.3"));
    assertNull(MiscFunctions.parseFloat(null));
  }

  @Test(groups = "all")
  public void parseDouble() {
    assertThat(MiscFunctions.parseDouble("55.3"), is(55.3));
    assertNull(MiscFunctions.parseDouble("A55.3"));
    assertNull(MiscFunctions.parseDouble(null));
  }
  
  @Test(groups = "all")
  public void wrapToExtendedList() {
    
    var list1 = MiscFunctions.wrapToExtendedList(new ArrayList<String>(Arrays.asList("pos1", "pos2", "pos3", "pos4")));
    assertNotNull(list1);
    assertThat(list1.get(-1), is("pos4"));

    var list2 = MiscFunctions.wrapToExtendedList(null);
    assertNotNull(list2);
    list2.add("bibo");
    assertThat(list2.get(-1), is("bibo"));

  }

  @Test(groups = "all")
  public void propertiesToMap() {
    
    var props1 = MiscFunctions.propertiesToMap(null);
    assertNotNull(props1);
    assertTrue(props1.isEmpty());
    
    var input1 = new Properties();
    input1.setProperty("key1", "value1");
    input1.setProperty("key2", "value2");

    var props2 = MiscFunctions.propertiesToMap(input1);
    assertNotNull(props2);
    assertThat(props2.size(), is(2));
    assertThat(props2.get("key1"), is("value1"));
    assertThat(props2.get("key2"), is("value2"));

  }
  
} /* ENDCLASS */
