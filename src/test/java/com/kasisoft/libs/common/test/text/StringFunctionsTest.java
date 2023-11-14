package com.kasisoft.libs.common.test.text;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import org.junit.jupiter.api.*;

import com.kasisoft.libs.common.text.*;

import java.util.function.*;

import java.util.stream.*;

import java.util.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class StringFunctionsTest {

  public static Stream<Arguments> data_removeSuffix() {
    return Stream.of(
      Arguments.of("a/b/c/test"    , "a/b/c/test"),
      Arguments.of("a/b/c/test."   , "a/b/c/test"),
      Arguments.of("a/b/c/test.txt", "a/b/c/test")
    );
  }

  @ParameterizedTest
  @MethodSource("data_removeSuffix")
  public void removeSuffix(String name, String expected) {
    assertThat(StringFunctions.removeSuffix(name), is(expected));
  }

  public static Stream<Arguments> data_changeSuffix() {
    return Stream.of(
      Arguments.of("a/b/c/test"    , "jpg" , "a/b/c/test.jpg"),
      Arguments.of("a/b/c/test."   , "jpg" , "a/b/c/test.jpg"),
      Arguments.of("a/b/c/test.txt", "jpg" , "a/b/c/test.jpg"),
      Arguments.of("a/b/c/test.txt", ".jpg", "a/b/c/test..jpg")
    );
  }

  @ParameterizedTest
  @MethodSource("data_changeSuffix")
  public void changeSuffix(String name, String suffix, String expected) {
    assertThat(StringFunctions.changeSuffix(name, suffix), is(expected));
  }


  public static Stream<Arguments> data_cleanup() {
    return Stream.of(
      Arguments.of(null    , null),
      Arguments.of(""      , null),
      Arguments.of("\t"    , null),
      Arguments.of(" "     , null),
      Arguments.of("\r\t\n", null),
      Arguments.of("a"     , "a"),
      Arguments.of("\ra\n" , "a"),
      Arguments.of(" a "   , "a"),
      Arguments.of( " ab " , "ab"),
      Arguments.of(" a c " , "a c")
    );
  }

  @ParameterizedTest
  @MethodSource("data_cleanup")
  public void cleanup(String current, String expected) {
    String result = StringFunctions.cleanup(current);
    assertThat(result, is(expected));
  }

  @Test
  public void firstUp() {
    assertThat(StringFunctions.firstUp("D"), is("D"));
    assertThat(StringFunctions.firstUp("d"), is("D"));
    assertThat(StringFunctions.firstUp("Dummy"), is("Dummy"));
    assertThat(StringFunctions.firstUp("dummy"), is("Dummy"));
  }

  @Test
  public void firstDown() {
    assertThat(StringFunctions.firstDown("D"), is("d"));
    assertThat(StringFunctions.firstDown("d"), is("d"));
    assertThat(StringFunctions.firstDown("Dummy"), is("dummy"));
    assertThat(StringFunctions.firstDown("dummy"), is("dummy"));
  }

  @Test
  public void replaceAll() {

    var replacements = new HashMap<String, String>();
    replacements.put("name", "Daniel Kasmeroglu");
    replacements.put("company", "Kasisoft");

    var value1 = StringFunctions.replaceAll("The pseudo company company is driven by name [company]", replacements);
    assertThat(value1, is("The pseudo Kasisoft Kasisoft is driven by Daniel Kasmeroglu [Kasisoft]"));

    var value2 = StringFunctions.replaceAll("The pseudo company ${company} is driven by ${name} [${company}]", replacements, "${%s}");
    assertThat(value2, is("The pseudo company Kasisoft is driven by Daniel Kasmeroglu [Kasisoft]"));

  }

  @Test
  public void camelCase() {
    assertThat(StringFunctions.camelCase(""), is(""));
    assertThat(StringFunctions.camelCase("simple"), is( "simple"));
    assertThat(StringFunctions.camelCase("simpleTon"), is("simpleTon"));
    assertThat(StringFunctions.camelCase("simple_ton"), is("simpleTon"));
    assertThat(StringFunctions.camelCase("simple__ton"), is("simpleTon"));
    assertThat(StringFunctions.camelCase("simple ton"), is("simpleTon"));
    assertThat(StringFunctions.camelCase("Simple-ton"), is("simpleTon"));
  }

  public static Stream<Arguments> data_regionReplaceSimple() {
    return Stream.of(
      Arguments.of("this is // my text // without a section // dodo //", "", "this is  without a section "),
      Arguments.of("this is // // my text // without a section // dodo //", "", "this is  my text  dodo //"),
      Arguments.of("this is // // my text // without a section // dodo // //", "", "this is  my text  dodo "),
      Arguments.of("this is // my text // without a section // dodo //", "bibo", "this is bibo without a section bibo"),
      Arguments.of("this is // // my text // without a section // dodo //", "bibo", "this is bibo my text bibo dodo //"),
      Arguments.of("this is // // my text // without a section // dodo // //", "bibo", "this is bibo my text bibo dodo bibo")
    );
  }

  @ParameterizedTest
  @MethodSource("data_regionReplaceSimple")
  public void regionReplaceSimple(String text, String replacement, String expected) {
    assertThat(StringFunctions.replaceRegions(text, "//", replacement), is( expected ) );
    assertThat(StringFunctions.replaceRegions(text, "//", null, replacement), is( expected ) );
  }

  public static Stream<Arguments> data_regionReplaceFunction() {
    var mapping = new HashMap<String, String>();
    mapping.put("my text", "changed text");
    mapping.put("dodo", "dodo text");
    Function<String, CharSequence> replacement = $ -> mapping.get($.trim());
    return Stream.of(
      Arguments.of("this is // my text // without a section // dodo //", replacement, "this is changed text without a section dodo text"),
      Arguments.of("this is // // my text // without a section // dodo //", replacement, "this is  my text  dodo //"),
      Arguments.of("this is // // my text // without a section // dodo // //", replacement, "this is  my text  dodo ")
    );
  }

  @ParameterizedTest
  @MethodSource("data_regionReplaceFunction")
  public void regionReplaceFunction(String text, Function<String, CharSequence> replacement, String expected) {
    assertThat(StringFunctions.replaceRegions(text, "//", replacement), is(expected));
  }

  public static Stream<Arguments> data_endsWithMany() {
    return Stream.of(
      Arguments.of("20 Frösche fliegen über den Ozean", new String[] {"20", "Ozean"}, true),
      Arguments.of("20 Frösche fliegen über den Ozean", new String[] {"blau", "den"}, false),
      Arguments.of("20 Frösche fliegen über den Ozean", new String[] {}, false),
      Arguments.of("20 Frösche fliegen über den Ozean", null, false)
    );
  }


  @ParameterizedTest
  @MethodSource("data_endsWithMany")
  public void endsWithMany(String text, String[] candidates, boolean contained) {
    if (contained) {
      assertNotNull(StringFunctions.endsWithMany(text, candidates ));
    } else {
      assertNull(StringFunctions.endsWithMany(text, candidates ));
    }

  }

  public static Stream<Arguments> data_startsWithMany() {
    return Stream.of(
      Arguments.of("20 Frösche fliegen über den Ozean", new String[] {"20", "fliegen"}, true),
      Arguments.of("20 Frösche fliegen über den Ozean", new String[] {"blau", "den"}, false),
      Arguments.of("20 Frösche fliegen über den Ozean", new String[] {}, false),
      Arguments.of("20 Frösche fliegen über den Ozean", null, false)
    );
  }

  @ParameterizedTest
  @MethodSource("data_startsWithMany")
  public void startsWithMany(String text, String[] candidates, boolean contained) {
    if (contained) {
      assertNotNull(StringFunctions.startsWithMany(text, candidates ));
    } else {
      assertNull(StringFunctions.startsWithMany(text, candidates ));
    }
  }

  @Test
  public void concatenate() {

    // String array

    // without delimiter
    assertThat(StringFunctions.concatenate(null), is(""));
    assertThat(StringFunctions.concatenate(null, new String[0]), is(""));
    assertThat(StringFunctions.concatenate(null, "A"), is("A"));
    assertThat(StringFunctions.concatenate(null, "A", "B"), is("AB"));
    assertThat(StringFunctions.concatenate(null, "A", "B", "C"), is("ABC"));
    assertThat(StringFunctions.concatenate(null, "A", null, "C"), is( "AC"));
    assertThat(StringFunctions.concatenate(null, "A", "", "C"), is( "AC"));

    // with delimiter
    assertThat(StringFunctions.concatenate("#"), is(""));
    assertThat(StringFunctions.concatenate("#", new String[0]), is(""));
    assertThat(StringFunctions.concatenate("#", "A"), is("A"));
    assertThat(StringFunctions.concatenate("#", "A", "B"), is("A#B"));
    assertThat(StringFunctions.concatenate("#", "A", "B", "C"), is("A#B#C"));
    assertThat(StringFunctions.concatenate("#", "A", null, "C"), is( "A#C"));
    assertThat(StringFunctions.concatenate("#", "A", "", "C"), is( "A#C"));

    // String collection

    // without delimiter
    assertThat(StringFunctions.concatenate(null), is(""));
    assertThat(StringFunctions.concatenate(null, Arrays.asList(new String[0])), is(""));
    assertThat(StringFunctions.concatenate(null, Arrays.asList("A")), is("A"));
    assertThat(StringFunctions.concatenate(null, Arrays.asList("A", "B")), is("AB"));
    assertThat(StringFunctions.concatenate(null, Arrays.asList("A", "B", "C")), is("ABC"));
    assertThat(StringFunctions.concatenate(null, Arrays.asList("A", null, "C") ), is("AC"));
    assertThat(StringFunctions.concatenate(null, Arrays.asList("A", "", "C")), is("AC"));

    // with delimiter
    assertThat(StringFunctions.concatenate("#"), is(""));
    assertThat(StringFunctions.concatenate("#", Arrays.asList(new String[0])), is(""));
    assertThat(StringFunctions.concatenate("#", Arrays.asList("A")), is("A"));
    assertThat(StringFunctions.concatenate("#", Arrays.asList("A", "B")), is( "A#B" ) );
    assertThat(StringFunctions.concatenate("#", Arrays.asList("A", "B", "C")), is("A#B#C"));
    assertThat(StringFunctions.concatenate("#", Arrays.asList("A", null, "C")), is("A#C"));
    assertThat(StringFunctions.concatenate("#", Arrays.asList("A", "", "C")), is("A#C"));

  }

  public static Stream<Arguments> data_repeat() {
    return Stream.of(
      Arguments.of(0, null, ""),
      Arguments.of(1, null, ""),
      Arguments.of(2, null, ""),
      Arguments.of(0, "", ""),
      Arguments.of(1, "", ""),
      Arguments.of(2, "", ""),
      Arguments.of(0, "A", ""),
      Arguments.of(1, "A", "A"),
      Arguments.of(2, "A", "AA")
    );
  }

  @ParameterizedTest
  @MethodSource("data_repeat")
  public void repeat(int n, String text, String expected) {
    assertThat(StringFunctions.repeat(n, text), is(expected));
  }

  public static Stream<Arguments> data_objectToString() {
    return Stream.of(

      Arguments.of(null, "null"),

      Arguments.of("", ""),
      Arguments.of("Hello", "Hello"),

      Arguments.of(false, "false"),
      Arguments.of(true, "true"),
      Arguments.of('a', "a"),
      Arguments.of((byte) 12, "12"),
      Arguments.of((short) 13, "13"),
      Arguments.of((int) 14, "14"),
      Arguments.of((long) 15, "15"),
      Arguments.of((float) 16.1, "16.1"),
      Arguments.of((double) 17.2, "17.2"),

      Arguments.of(new String[] {"Hello", "Buffalo"}, "[Hello,Buffalo]"),
      Arguments.of(new boolean[] {true, false}, "[true,false]"),
      Arguments.of(new char[] {'c', 'k', 'l'}, "['c','k','l']"),
      Arguments.of(new byte[] {13, 18, 19}, "[(byte)13,(byte)18,(byte)19]"),
      Arguments.of(new short[] {23, 28, 29}, "[(short)23,(short)28,(short)29]"),
      Arguments.of(new int[] {33, 38, 39}, "[33,38,39]"),
      Arguments.of(new long[] {43, 48, 49}, "[43l,48l,49l]"),
      Arguments.of(new float[] {53.1f, 58.2f, 59.3f}, "[53.1f,58.2f,59.3f]"),
      Arguments.of(new double[] {63.1, 68.2, 69.3}, "[63.1,68.2,69.3]")
    );
  }

  @ParameterizedTest
  @MethodSource("data_objectToString")
  public static void objectToString(Object obj, String expected) {
    assertThat(StringFunctions.objectToString(obj), is(expected));
  }

  @Test
  public void trimLeading() {
    var str = StringFunctions.trimLeading("\r\n   My test is this: Hello World ! Not 0x11 !");
    assertThat(str, is("My test is this: Hello World ! Not 0x11 !"));
  }

  @Test
  public void trimLeading__WithSpecifiedChars() {
    var str = StringFunctions.trimLeading("\r\n   My test is this: Hello World ! Not 0x11 !", "\r\n ");
    assertThat(str, is("My test is this: Hello World ! Not 0x11 !"));
  }

  @Test
  public void trimTrailing() {
    var str = StringFunctions.trimTrailing("My test is this: Hello World ! Not 0x11 !\r\n   ");
    assertThat(str, is("My test is this: Hello World ! Not 0x11 !"));
  }

  @Test
  public void trimTrailing__WithSpecifiedChars() {
    var str = StringFunctions.trimTrailing("My test is this: Hello World ! Not 0x11 !\r\n   ", "\r\n ");
    assertThat(str, is("My test is this: Hello World ! Not 0x11 !"));
  }

  @Test
  public void trim() {
    var str = StringFunctions.trim("\r\n   My test is this: Hello World ! Not 0x11 !\r\n   ");
    assertThat(str, is("My test is this: Hello World ! Not 0x11 !"));
  }

  @Test
  public void trim__WithSpecifiedChars() {
    var str = StringFunctions.trim("\r\n   My test is this: Hello World ! Not 0x11 !\r\n   ", "\r\n ", null);
    assertThat(str, is("My test is this: Hello World ! Not 0x11 !"));
  }

  @Test
  public void trim__WithSpecifiedChars_Left() {
    var str = StringFunctions.trim("\r\n   My test is this: Hello World ! Not 0x11 !\r\n   ", "\r\n ", true);
    assertThat(str, is("My test is this: Hello World ! Not 0x11 !\r\n   "));
  }

  @Test
  public void trim__WithSpecifiedChars_Right() {
    var str = StringFunctions.trim("\r\n   My test is this: Hello World ! Not 0x11 !\r\n   ", "\r\n ", false);
    assertThat(str, is("\r\n   My test is this: Hello World ! Not 0x11 !"));
  }

} /* ENDCLASS */
