package com.kasisoft.libs.common.test.utils;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.types.*;
import com.kasisoft.libs.common.utils.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import org.junit.jupiter.api.*;

import java.util.function.*;

import java.util.stream.*;

import java.util.*;

import java.time.*;

/**
 * Test for various functions of the class 'MiscFunctions'.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class MiscFunctionsTest {

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_getGravatarLink() {
    return Stream.of(
      Arguments.of(null                                                                    , null                                  , null),
      Arguments.of(null                                                                    , null                                  , 12),
      Arguments.of("https://www.gravatar.com/avatar/8fea46f5bc403f6949300c3007d2f18d"      , " Daniel.KASMEROGLU@kasisoft.net \n"  , null),
      Arguments.of("https://www.gravatar.com/avatar/8fea46f5bc403f6949300c3007d2f18d"      , "daniel.kasmeroglu@kasisoft.net"      , null),
      Arguments.of("https://www.gravatar.com/avatar/8fea46f5bc403f6949300c3007d2f18d?s=100", "daniel.kasmeroglu@kasisoft.net"      , 100)
    );
  }

  @ParameterizedTest
  @MethodSource("data_getGravatarLink")
  public void getGravatarLink(String expected, String email, Integer size) {
    assertThat(MiscFunctions.getGravatarLink(email, size), is(expected));
  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_toUniqueList() {
    return Stream.of(
      Arguments.of(Arrays.asList("Otto", "Fred", "Ginger"), Arrays.asList("Fred", "Ginger", "Otto")),
      Arguments.of(Arrays.asList("Otto", "Fred", "Otto", "Ginger"), Arrays.asList("Fred", "Ginger", "Otto"))
    );
  }

  @ParameterizedTest
  @MethodSource("data_toUniqueList")
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

  @ParameterizedTest
  @MethodSource("data_toUniqueList")
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

  @Test
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

  private static <T> Stream<Arguments> createLeapYearTests(Function<Integer, T> year2Info) {
    return Arrays.asList(
      Arguments.of(year2Info.apply(1900), false),
      Arguments.of(year2Info.apply(1901), false),
      Arguments.of(year2Info.apply(1904), true),
      Arguments.of(year2Info.apply(2000), true),
      Arguments.of(year2Info.apply(2001), false)
    ).stream();
  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_isLeapYear() {
    return createLeapYearTests(Function.identity());
  }

  @ParameterizedTest
  @MethodSource("data_isLeapYear")
  public void isLeapYear(int year, boolean expected) {
    assertThat(MiscFunctions.isLeapYear(year), is(expected));
  }

  private static Date createDate(int year) {
    var calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, year);
    return calendar.getTime();
  }

  private static OffsetDateTime createOffsetDateTime(int year) {
    return OffsetDateTime.ofInstant(createDate(year).toInstant(), ZoneId.of("UTC"));
  }

  private static LocalDateTime createLocalDateTime(int year) {
    return LocalDateTime.ofInstant(createDate(year).toInstant(), ZoneId.of("UTC"));
  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_isLeapYear__Date() {
    return createLeapYearTests(MiscFunctionsTest::createDate);
  }

  @ParameterizedTest
  @MethodSource("data_isLeapYear__Date")
  public void isLeapYear__Date(Date year, boolean expected) {
    assertThat(MiscFunctions.isLeapYear(year), is(expected));
  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_isLeapYear__OffsetDateTime() {
    return createLeapYearTests(MiscFunctionsTest::createOffsetDateTime);
  }

  @ParameterizedTest
  @MethodSource("data_isLeapYear__OffsetDateTime")
  public void isLeapYear__OffsetDateTime(OffsetDateTime year, boolean expected) {
    assertThat(MiscFunctions.isLeapYear(year), is(expected));
  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_isLeapYear__LocalDateTime() {
    return createLeapYearTests(MiscFunctionsTest::createLocalDateTime);
  }

  @ParameterizedTest
  @MethodSource("data_isLeapYear__LocalDateTime")
  public void isLeapYear__LocalDateTime(LocalDateTime year, boolean expected) {
    assertThat(MiscFunctions.isLeapYear(year), is(expected));
  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_repeat() {
    return Stream.of(
      Arguments.of(0, null, Arrays.asList()),
      Arguments.of(1, null, Arrays.asList( new Object[] {null})),
      Arguments.of(5, null, Arrays.asList( null, null, null, null, null)),
      Arguments.of(0, "Dodo", Arrays.asList()),
      Arguments.of(1, "Dodo", Arrays.asList("Dodo")),
      Arguments.of(5, "Dodo", Arrays.asList("Dodo", "Dodo", "Dodo", "Dodo", "Dodo"))
    );
  }

  @ParameterizedTest
  @MethodSource("data_repeat")
  public <T> void repeat(int count, T element, List<T> expected) {
    var actual = MiscFunctions.repeat(count, element);
    assertThat(actual, notNullValue());
    assertThat(actual.size(), is(count));
    assertThat(actual, is(expected));
  }

  @Test
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

  @Test
  public void wrapToExtendedList__newList() {

    var list1 = MiscFunctions.wrapToExtendedList(new ArrayList<String>());
    assertThat(list1.size(), is(0));

    var list2 = MiscFunctions.wrapToExtendedList(Arrays.asList("BLA", "BLUB"));
    assertThat(list2.size(), is(2));
    assertThat(list2.get(0), is("BLA"));
    assertThat(list2.get(1), is("BLUB"));

  }

  @Test
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

  @Test
  public void twrapToExtendedList__sublist() {

    var list1 = MiscFunctions.wrapToExtendedList(Arrays.asList("BLA", "BLUB", "BLAU", "KRAUT", "FROG"));
    var list2 = list1.subList(1, -1);
    assertThat(list2, is( notNullValue()));
    assertThat(list2.size(), is(3));
    assertThat(list2.get(0), is("BLUB"));
    assertThat(list2.get(1), is("BLAU"));
    assertThat(list2.get(2), is("KRAUT"));

  }

  @Test
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

  @Test
  public void gcd() {
    assertThat(MiscFunctions.gcd(20, 15), is(5));
    assertThat(MiscFunctions.gcd(99, 44), is(11));
  }

  @Test
  public void trimLeading() {

    var list1        = new ArrayList<String>(Arrays.asList(null, null, null, "str1", "str2", null, null));
    var trimmed1     = MiscFunctions.trimLeading(list1);
    assertThat(trimmed1, is(Arrays.asList("str1", "str2", null, null)));

    var list2        = new ArrayList<String>();
    var trimmed2     = MiscFunctions.trimLeading(list2);
    assertNotNull(trimmed2);
    assertTrue(trimmed2.isEmpty());

  }

  @Test
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

  @Test
  public void parseBoolean() {
    assertThat(MiscFunctions.parseBoolean("false"), is(false));
    assertNull(MiscFunctions.parseBoolean("Afalse"));
    assertNull(MiscFunctions.parseBoolean(null));
  }


  @Test
  public void parseByte() {
    assertThat(MiscFunctions.parseByte("54"), is((byte) 54));
    assertNull(MiscFunctions.parseByte("A54"));
    assertNull(MiscFunctions.parseByte(null));
  }

  @Test
  public void parseShort() {
    assertThat(MiscFunctions.parseShort("54"), is((short) 54));
    assertNull(MiscFunctions.parseShort("A54"));
    assertNull(MiscFunctions.parseShort(null));
  }

  @Test
  public void parseInt() {
    assertThat(MiscFunctions.parseInt("54"), is(54));
    assertNull(MiscFunctions.parseInt("A54"));
    assertNull(MiscFunctions.parseInt(null));
  }

  @Test
  public void parseLong() {
    assertThat(MiscFunctions.parseLong("54"), is(54L));
    assertNull(MiscFunctions.parseLong("A54"));
    assertNull(MiscFunctions.parseLong(null));
  }

  @Test
  public void parseFloat() {
    assertThat(MiscFunctions.parseFloat("54.3"), is(54.3f));
    assertNull(MiscFunctions.parseFloat("A54.3"));
    assertNull(MiscFunctions.parseFloat(null));
  }

  @Test
  public void parseDouble() {
    assertThat(MiscFunctions.parseDouble("55.3"), is(55.3));
    assertNull(MiscFunctions.parseDouble("A55.3"));
    assertNull(MiscFunctions.parseDouble(null));
  }

  @Test
  public void wrapToExtendedList() {

    var list1 = MiscFunctions.wrapToExtendedList(new ArrayList<String>(Arrays.asList("pos1", "pos2", "pos3", "pos4")));
    assertNotNull(list1);
    assertThat(list1.get(-1), is("pos4"));

    var list2 = MiscFunctions.wrapToExtendedList(null);
    assertNotNull(list2);
    list2.add("bibo");
    assertThat(list2.get(-1), is("bibo"));

  }

  @Test
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
