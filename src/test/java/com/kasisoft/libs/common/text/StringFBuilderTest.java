package com.kasisoft.libs.common.text;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import org.junit.jupiter.api.*;

import java.util.function.*;

import java.util.stream.*;

import java.util.*;
import java.util.regex.*;

import java.io.*;

/**
 * Testcase for the class 'StringFBuilder'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class StringFBuilderTest {

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_buffers() {
    return Stream.of(
      Arguments.of(create()),
      Arguments.of(create(256)),
      Arguments.of(create(""))
    );
  }
  
  @Test
  public void append() {
    
    var buffer1 = create();
    buffer1.append('h');
    assertThat(buffer1.toString(), is("h"));

    var buffer2 = create();
    buffer2.append(false);
    assertThat(buffer2.toString(), is("false"));

    var buffer3 = create();
    buffer3.append(true);
    assertThat(buffer3.toString(), is("true"));

    var buffer4 = create();
    buffer4.append("text");
    assertThat(buffer4.toString(), is("text"));

    var buffer5 = create();
    buffer5.append((byte) 65);
    assertThat(buffer5.toString(), is("65"));

    var buffer6 = create();
    buffer6.append((short) 1882);
    assertThat(buffer6.toString(), is("1882"));

    var buffer7 = create();
    buffer7.append(9_2981);
    assertThat(buffer7.toString(), is("92981"));

    var buffer8 = create();
    buffer8.append(9_298_191_391_921L);
    assertThat(buffer8.toString(), is("9298191391921"));

    var buffer9 = create();
    buffer9.append(3.193819388131);
    assertThat(buffer9.toString(), is("3.193819388131"));

    var buffer10 = create();
    buffer10.append((float) -4.138813);
    assertThat(buffer10.toString(), is("-4.138813"));

    var buffer11 = create();
    buffer11.append(Boolean.TRUE);
    assertThat(buffer11.toString(), is("true"));

    var buffer12 = create();
    buffer12.append("my text", 3, -2);
    assertThat(buffer12.toString(), is("te"));
    
    var buffer13 = create();
    buffer13.append(new char[] {'H', 'e', 'l', 'l', 'o'});
    assertThat(buffer13.toString(), is("Hello"));

    var buffer14 = create();
    buffer14.append(new char[] {'m', 'y', ' ', 't', 'e', 'x', 't'}, 3, 2);
    assertThat(buffer14.toString(), is("te"));

  }
  
  @Test
  public void insert() {
    
    var buffer1 = create("Hello World");
    buffer1.insert(-5, 'h');
    assertThat(buffer1.toString(), is("Hello hWorld"));

    var buffer2 = create("Hello World");
    buffer2.insert(-5, false);
    assertThat(buffer2.toString(), is("Hello falseWorld"));

    var buffer3 = create("Hello World");
    buffer3.insert(-5, true);
    assertThat(buffer3.toString(), is("Hello trueWorld"));

    var buffer4 = create("Hello World");
    buffer4.insert(-5, "text");
    assertThat(buffer4.toString(), is("Hello textWorld"));

    var buffer5 = create("Hello World");
    buffer5.insert(-5, (byte) 65);
    assertThat(buffer5.toString(), is("Hello 65World"));

    var buffer6 = create("Hello World");
    buffer6.insert(-5, (short) 1882);
    assertThat(buffer6.toString(), is("Hello 1882World"));

    var buffer7 = create("Hello World");
    buffer7.insert(-5, 9_2981);
    assertThat(buffer7.toString(), is("Hello 92981World"));

    var buffer8 = create("Hello World");
    buffer8.insert(-5, 9_298_191_391_921L);
    assertThat(buffer8.toString(), is("Hello 9298191391921World"));

    var buffer9 = create("Hello World");
    buffer9.insert(-5, 3.193819388131);
    assertThat(buffer9.toString(), is("Hello 3.193819388131World"));

    var buffer10 = create("Hello World");
    buffer10.insert(-5, (float) -4.138813);
    assertThat(buffer10.toString(), is("Hello -4.138813World"));

    var buffer11 = create("Hello World");
    buffer11.insert(-5, Boolean.TRUE);
    assertThat(buffer11.toString(), is("Hello trueWorld"));

    var buffer12 = create("Hello World");
    buffer12.insert(-5, "my text", 3, -2);
    assertThat(buffer12.toString(), is("Hello teWorld"));
    
    var buffer13 = create("Hello World");
    buffer13.insert(-5, new char[] {'H', 'e', 'l', 'l', 'o'});
    assertThat(buffer13.toString(), is("Hello HelloWorld"));

    var buffer14 = create("Hello World");
    buffer14.insert(-5, new char[] {'m', 'y', ' ', 't', 'e', 'x', 't'}, 3, 2);
    assertThat(buffer14.toString(), is("Hello teWorld"));

  }
  
  @Test
  public void getChars() {
    var charray = new char[20];
    Arrays.fill(charray, 'A');
    var buffer  = create("Hello World");
    buffer.getChars(2, -5, charray, -10);
    assertThat(String.valueOf(charray), is("AAAAAAAAAAllo AAAAAA"));
  }

  @Test
  public void subSequence() {
    var buffer = create("Hello World");
    var seq    = buffer.subSequence(0, -2);
    assertThat(String.valueOf(seq), is("Hello Wor"));
  }

  @Test
  public void replace() {
    var buffer = create("Hello World");
    buffer.replace(-5, 0, "Fred");
    assertThat(buffer.toString(), is("Hello Fred"));
  }
  
  @Test
  public void serialization() throws Exception {
    
    var buffer  = create("Dummy");
    
    var byteout = new ByteArrayOutputStream();
    try (var objectout = new ObjectOutputStream(byteout)) {
      objectout.writeObject(buffer);
    }
    
    Object read   = null;
    var    bytein = new ByteArrayInputStream(byteout.toByteArray());
    try (var objectin = new ObjectInputStream(bytein)) {
      read = objectin.readObject();
    }
    
    assertNotNull(read);
    assertTrue(StringFBuilder.class.isAssignableFrom(read.getClass()));
    
    var readBuffer = (StringFBuilder) read;
    assertThat(buffer.compareTo(readBuffer), is(0));
    
  }
  
  @Test
  public void compareTo() throws Exception {
    var buffer = create("Dummy");
    assertTrue(buffer.compareTo(buffer) == 0);
  }

  @Test
  public void firstUp() {
    
    var buffer1 = create("Dummy");
    buffer1.firstUp();
    assertThat(buffer1.toString(), is("Dummy"));

    var buffer2 = create("dummy");
    buffer2.firstUp();
    assertThat(buffer2.toString(), is("Dummy"));

  }

  @Test
  public void firstDown() {
    
    var buffer1 = create("Dummy");
    buffer1.firstDown();
    assertThat(buffer1.toString(), is("dummy"));

    var buffer2 = create("dummy");
    buffer2.firstDown();
    assertThat(buffer2.toString(), is("dummy"));

  }

  @Test
  public void camelCase() {
    assertThat(create("").camelCase().toString(), is(""));
    assertThat(create("simple").camelCase().toString(), is("simple"));
    assertThat(create("simpleTon").camelCase().toString(), is("simpleTon"));
    assertThat(create("simple_ton").camelCase().toString(), is("simpleTon"));
    assertThat(create("simple__ton").camelCase().toString(), is("simpleTon"));
    assertThat(create("simple ton").camelCase().toString(), is("simpleTon"));
    assertThat(create("Simple-ton").camelCase().toString(), is("simpleTon"));
  }

  @ParameterizedTest
  @MethodSource("data_buffers")
  public void appendF(StringFBuilder buffer) {
    buffer.appendF("My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf(17));
    assertThat(buffer.toString(), is("My test is this: Hello World ! Not 0x11 !"));
  }
  
  @ParameterizedTest
  @MethodSource("data_buffers")
  public void charAt(StringFBuilder buffer) {
    buffer.appendF("My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf(17));
    assertThat(buffer.charAt(0), is('M'));
    assertThat(buffer.charAt(-1), is('!'));
  }

  @ParameterizedTest
  @MethodSource("data_buffers")
  public void substring(StringFBuilder buffer) {
    buffer.appendF("My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf(17));
    assertThat(buffer.substring(0, 2), is("My"));
    assertThat(buffer.substring(-10), is("Not 0x11 !"));
  }
  
  @ParameterizedTest
  @MethodSource("data_buffers")
  public void trimLeading(StringFBuilder buffer) {
    buffer.appendF("\r\n   My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf(17));
    buffer.trimLeading();
    assertThat(buffer.toString(), is("My test is this: Hello World ! Not 0x11 !"));
  }

  @ParameterizedTest
  @MethodSource("data_buffers")
  public void trimLeading__WithSpecifiedChars(StringFBuilder buffer) {
    buffer.appendF("\r\n   My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf(17));
    buffer.trimLeading("\r\n ");
    assertThat(buffer.toString(), is("My test is this: Hello World ! Not 0x11 !"));
  }

  @ParameterizedTest
  @MethodSource("data_buffers")
  public void trimTrailing(StringFBuilder buffer) {
    buffer.appendF("My test is this: %s ! Not 0x%02x !\r\n   ", "Hello World", Integer.valueOf(17));
    buffer.trimTrailing();
    assertThat(buffer.toString(), is("My test is this: Hello World ! Not 0x11 !"));
  }

  @ParameterizedTest
  @MethodSource("data_buffers")
  public void trimTrailing__WithSpecifiedChars(StringFBuilder buffer) {
    buffer.appendF("My test is this: %s ! Not 0x%02x !\r\n   ", "Hello World", Integer.valueOf(17));
    buffer.trimTrailing("\r\n ");
    assertThat(buffer.toString(), is("My test is this: Hello World ! Not 0x11 !"));
  }

  @ParameterizedTest
  @MethodSource("data_buffers")
  public void trim(StringFBuilder buffer) {
    buffer.appendF("\r\n   My test is this: %s ! Not 0x%02x !\r\n   ", "Hello World", Integer.valueOf(17));
    buffer.trim();
    assertThat(buffer.toString(), is("My test is this: Hello World ! Not 0x11 !"));
  }

  @ParameterizedTest
  @MethodSource("data_buffers")
  public void trim__WithSpecifiedChars(StringFBuilder buffer) {
    buffer.appendF("\r\n   My test is this: %s ! Not 0x%02x !\r\n   ", "Hello World", Integer.valueOf(17));
    buffer.trim("\r\n ", null);
    assertThat(buffer.toString(), is("My test is this: Hello World ! Not 0x11 !"));
  }

  @ParameterizedTest
  @MethodSource("data_buffers")
  public void trim__WithSpecifiedChars_Left(StringFBuilder buffer) {
    buffer.appendF("\r\n   My test is this: %s ! Not 0x%02x !\r\n   ", "Hello World", Integer.valueOf(17));
    buffer.trim("\r\n ", true);
    assertThat(buffer.toString(), is("My test is this: Hello World ! Not 0x11 !\r\n   "));
  }

  @ParameterizedTest
  @MethodSource("data_buffers")
  public void trim__WithSpecifiedChars_Right(StringFBuilder buffer) {
    buffer.appendF("\r\n   My test is this: %s ! Not 0x%02x !\r\n   ", "Hello World", Integer.valueOf(17));
    buffer.trim("\r\n ", false);
    assertThat(buffer.toString(), is("\r\n   My test is this: Hello World ! Not 0x11 !"));
  }

  @ParameterizedTest
  @MethodSource("data_buffers")
  public void endsWith(StringFBuilder buffer) {
    
    buffer.appendF("The frog is here !");
    
    assertTrue  (buffer.endsWith(true, "here !"));
    assertFalse (buffer.endsWith(true, "HERE !"));
    assertTrue  (buffer.endsWith(false, "HERE !"));
    assertTrue  (buffer.endsWith("here !"));
    assertFalse (buffer.endsWith("HERE !"));
    assertFalse (buffer.endsWith(true, "The frog is here ! Oops !"));
    assertFalse (buffer.endsWith(false, "The frog is here ! Oops !"));
    
  }

  @Test
  public void endsWithMany() {
    
    var buffer = create("The frog is here !");
    
    assertNull(buffer.endsWithMany("Here !"));
    assertThat(buffer.endsWithMany(false, "Here !"), is("Here !"));
    assertThat(buffer.endsWithMany("here !"), is("here !"));

  }

  @ParameterizedTest
  @MethodSource("data_buffers")
  public void startsWith(StringFBuilder buffer) {
    
    buffer.appendF("The frog is here !");
    
    assertTrue  (buffer.startsWith(true, "The"));
    assertFalse (buffer.startsWith(true, "THE"));
    assertTrue  (buffer.startsWith(false, "THE"));
    assertTrue  (buffer.startsWith("The"));
    assertFalse (buffer.startsWith("THE"));
    assertFalse (buffer.startsWith(true, "The frog is here ! Oops !"));
    assertFalse (buffer.startsWith(false, "The frog is here ! Oops !"));
    
  }

  @Test
  public void startsWithMany() {
    var buffer = create("The frog is here !");
    assertNull(buffer.startsWithMany("the"));
    assertThat(buffer.startsWithMany(false, "the"), is("the"));
    assertThat(buffer.startsWithMany("The"), is("The"));
  }

  @ParameterizedTest
  @MethodSource("data_buffers")
  public void equals(StringFBuilder buffer) {
    buffer.appendF("The frog is here !");
    assertTrue  (buffer.equals(true, "The frog is here !"));
    assertFalse (buffer.equals(true, "THE FROG IS HERE !"));
    assertTrue  (buffer.equals(false, "THE FROG IS HERE !"));
    assertTrue  (buffer.equals("The frog is here !"));
    assertFalse (buffer.equals("THE FROG IS HERE !"));
  }
  
  @ParameterizedTest
  @MethodSource("data_buffers")
  public void remove(StringFBuilder buffer) {
    buffer.appendF("Moloko was a great band !");
    assertThat(buffer.remove("oa").toString(), is("Mlk ws  gret bnd !"));
  }

  @ParameterizedTest
  @MethodSource("data_buffers")
  public void reverse(StringFBuilder buffer) {
    buffer.append("The frog is here !");
    buffer.reverse();
    assertThat(buffer.toString(), is("! ereh si gorf ehT"));
  }

  @ParameterizedTest
  @MethodSource("data_buffers")
  public void indexOf(StringFBuilder buffer) {
    buffer.append("The frog is here !");
    assertThat(buffer.indexOf("frog"), is(4));
    assertThat(buffer.indexOf("Flansch"), is(-1));
    assertThat(buffer.indexOf("frog", 5), is(-1));
    assertThat(buffer.indexOf("Flansch", 5), is(-1));
    assertThat(buffer.indexOf("is", "frog"), is(4));
    assertThat(buffer.indexOf("is", "Flansch"), is(9));
    assertThat(buffer.indexOf("co", "Flansch"), is(-1));
  }

  @ParameterizedTest
  @MethodSource("data_buffers")
  public void lastIndexOf(StringFBuilder buffer) {
    buffer.append("The frog is here !");
    assertThat(buffer.lastIndexOf("frog"), is( 4));
    assertThat(buffer.lastIndexOf("Flansch"), is(-1));
    assertThat(buffer.lastIndexOf("frog", 5), is(4));
    assertThat(buffer.lastIndexOf("Flansch", 5), is(-1));
    assertThat(buffer.lastIndexOf("frog", "is"), is(9));
    assertThat(buffer.lastIndexOf("Flansch"), is(-1));
  }
  
  @ParameterizedTest
  @MethodSource("data_buffers")
  public void replace(StringFBuilder buffer) {
    buffer.append("The frog is here !");
    assertThat(buffer.replace('e', 'a').toString(), is("Tha frog is hara !"));
  }

  @ParameterizedTest
  @MethodSource("data_buffers")
  public void split(StringFBuilder buffer) {
    
    buffer.append("This was my 3 birthday on the 2 street.");
    var expected = new String[] {"This", "was", "my", "3", "bir", "hday", "on", "he", "2", "s", "ree", "."};
    var parts = buffer.split(" t");
    assertThat(parts, is(notNullValue()));
    assertThat(parts.length, is(expected.length));
    assertThat(parts, is(expected));
    
    buffer.setLength(0);
    buffer.append("58817162");
    parts = buffer.split("0123456789");
    assertNotNull(parts);
    assertThat(parts.length, is(0));
    
  }

  @ParameterizedTest
  @MethodSource("data_buffers")
  public void splitRegex(StringFBuilder buffer) {
    
    buffer.append("This was my 3 birthday on the 2 street.");
    var expected = new String[] {"This was my ", " birthday on the ", " street."};
    var parts = buffer.splitRegex("[0-9]+");
    assertThat(parts, is(notNullValue()));
    assertThat(parts.length, is(expected.length));
    assertThat(parts, is(expected));
    
    buffer.setLength(0);
    buffer.append("58817162");
    parts = buffer.splitRegex("[0-9]+");
    assertNotNull(parts);
    assertThat(parts.length, is(0));
    
    buffer.setLength(0);
    buffer.append("part,0");
    parts = buffer.splitRegex(Pattern.quote(","));
    assertNotNull(parts);
    assertThat(parts.length, is(2));
    assertThat(parts[0], is("part"));
    assertThat(parts[1], is("0"));
    
  }

  @ParameterizedTest
  @MethodSource("data_buffers")
  public void replaceAll(StringFBuilder buffer) {
    
    buffer.append("This was my 3 birthday on the 2 street.");
    buffer.replaceAll("[0-9]+", "abce");
    assertThat(buffer.toString(), is("This was my abce birthday on the abce street."));
    
    buffer.setLength(0);
    buffer.append("58817162");
    buffer.replaceAll("[0-9]+", "dodo");
    assertThat(buffer.toString(), is("dodo"));
   
    buffer.setLength(0);
    buffer.append("This was my 43356 birthday on the 233120 street 2928010.");
    buffer.replaceAll("[0-9]+", "abce");
    assertThat(buffer.toString(), is("This was my abce birthday on the abce street abce."));
    
  }

  @ParameterizedTest
  @MethodSource("data_buffers")
  public void replaceFirst(StringFBuilder buffer) {
    
    buffer.append("This was my 3 birthday on the 2 street.");
    buffer.replaceFirst("[0-9]+", "abce");
    assertThat(buffer.toString(), is("This was my abce birthday on the 2 street."));
    
    buffer.setLength(0);
    buffer.append("58817162");
    buffer.replaceFirst("[0-9]+", "dodo");
    assertThat(buffer.toString(), is("dodo"));
    
  }

  @ParameterizedTest
  @MethodSource("data_buffers")
  public void replaceLast(StringFBuilder buffer) {
    
    buffer.append("This was my 3 birthday on the 2 street.");
    buffer.replaceLast("[0-9]+", "abce");
    assertThat(buffer.toString(), is("This was my 3 birthday on the abce street."));
    
    buffer.setLength(0);
    buffer.append("58817162");
    buffer.replaceLast("[0-9]+", "dodo");
    assertThat(buffer.toString(), is("dodo"));
    
  }
  
  @ParameterizedTest
  @MethodSource("data_buffers")
  public void capacity(StringFBuilder buffer) {

    var capacity = buffer.capacity();
    assertTrue(capacity > 0);
    
    buffer.ensureCapacity(capacity + 16384);
    int newcapacity = buffer.capacity();
    assertThat(newcapacity, is(capacity + 16384));
    
    var length = buffer.length();
    buffer.trimToSize();
    var shrinkedcapacity = buffer.capacity();
    assertThat(shrinkedcapacity, is(length));
    
  }
  
  @SuppressWarnings("exports")
  public static Stream<Arguments> data_caseChange() {
    return Stream.of(
      Arguments.of(new String[] {""}, new String[] {""}),
      Arguments.of(new String[] {"hello"}, new String[] {"HELLO"}),
      Arguments.of(new String[] {"hello", "bibo"}, new String[] {"HELLO", "BIBO"})
    );
  }
  
  @ParameterizedTest
  @MethodSource("data_caseChange")
  public void toUpperCase(String[] current, String[] expected) {
    for (var i = 0; i < current.length; i++) {
      assertThat(create(current[i]).toUpperCase().toString(), is(expected[i]));
    }
  }

  @ParameterizedTest
  @MethodSource("data_caseChange")
  public void toLowerCase(String[] expected, String[] current) {
    for (var i = 0; i < current.length; i++) {
      assertThat(create(current[i]).toLowerCase().toString(), is(expected[i]));
    }
  }

  @Test
  public void appendIfMissing() {
    assertThat(create("Hello/").appendIfMissing("/").toString(), is("Hello/"));
    assertThat(create("Hello").appendIfMissing("/").toString(), is("Hello/"));
  }

  @Test
  public void prependIfMissing() {
    assertThat(create("/Hello").prependIfMissing("/").toString(), is("/Hello"));
    assertThat(create("Hello").prependIfMissing("/").toString(), is("/Hello"));
  }

  @Test
  public void removeEnd() {
    assertThat(create("startHello").removeEnd("Dodo").toString(), is("startHello"));
    assertThat(create("startHello").removeEnd("Hello").toString(), is("start"));
    assertThat(create("startHello").removeEnd(true, "hello").toString(), is("startHello"));
  }
  
  @Test
  public void removeStart() {
    assertThat(create("startHello").removeStart("stop").toString(), is("startHello"));
    assertThat(create("startHello").removeStart("start").toString(), is("Hello"));
    assertThat(create("startHello").removeStart(true, "Start").toString(), is("startHello"));
  }
  
  @Test
  public void replaceAll() {
    
    var replacements = new HashMap<String, String>();
    replacements.put("name", "Daniel Kasmeroglu");
    replacements.put("company", "Kasisoft");
    
    var buffer1 = create("The pseudo company company is driven by name [company]");
    buffer1.replaceAll(replacements);
    assertThat(buffer1.toString(), is( "The pseudo Kasisoft Kasisoft is driven by Daniel Kasmeroglu [Kasisoft]" ) );

    var buffer2 = create("The pseudo company ${company} is driven by ${name} [${company}]") ;
    buffer2.replaceAll(replacements, "${%s}");
    assertThat(buffer2.toString(), is( "The pseudo company Kasisoft is driven by Daniel Kasmeroglu [Kasisoft]" ) );

  }
  
  @SuppressWarnings("exports")
  public static Stream<Arguments> data_regionReplaceSimple() {
    return Stream.of(
      Arguments.of(create("this is // my text // without a section // dodo //"), "", "this is  without a section "),
      Arguments.of(create("this is // // my text // without a section // dodo //"), "", "this is  my text  dodo //"),
      Arguments.of(create("this is // // my text // without a section // dodo // //"), "", "this is  my text  dodo "),
      Arguments.of(create("this is // my text // without a section // dodo //"), "bibo", "this is bibo without a section bibo"),
      Arguments.of(create("this is // // my text // without a section // dodo //"), "bibo", "this is bibo my text bibo dodo //"),
      Arguments.of(create("this is // // my text // without a section // dodo // //"), "bibo", "this is bibo my text bibo dodo bibo")
    );
  }
  
  @ParameterizedTest
  @MethodSource("data_regionReplaceSimple")
  public void regionReplaceSimple(StringFBuilder buffer, String replacement, String expected) {
    assertThat(buffer.replaceRegions("//", replacement).toString(), is( expected ) );
  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_regionReplaceFunction() {
    var mapping = new HashMap<String, String>();
    mapping.put("my text", "changed text");
    mapping.put("dodo", "dodo text");
    var replacement = (Function<String, CharSequence>) $ -> mapping.get($.trim());
    return Stream.of(
      Arguments.of(create("this is // my text // without a section // dodo //"), replacement, "this is changed text without a section dodo text"),
      Arguments.of(create("this is // // my text // without a section // dodo //"), replacement, "this is  my text  dodo //"),
      Arguments.of(create("this is // // my text // without a section // dodo // //"), replacement, "this is  my text  dodo ")
    );
  }
  
  @ParameterizedTest
  @MethodSource("data_regionReplaceFunction")
  public void regionReplaceFunction(StringFBuilder buffer, Function<String, CharSequence> replacement, String expected) {
    assertThat(buffer.replaceRegions("//", replacement).toString(), is(expected));
  }

  @Test
  public void lastIndexOf() {
    var input = "hello world is here";
    assertThat(new StringFBuffer(input).lastIndexOf('y', 'z'), is(-1));
    assertThat(new StringFBuffer(input).lastIndexOf('w', 'o'), is(7));
    assertThat(new StringFBuffer(input).lastIndexOf(5, 'y', 'z'), is(-1));
    assertThat(new StringFBuffer(input).lastIndexOf(5, 'w', 'o'), is(4));
  }

  @Test
  public void indexOf() {
    var input = "hello world is here";
    assertThat(new StringFBuffer(input).indexOf('y', 'z'), is(-1));
    assertThat(new StringFBuffer(input).indexOf('w', 'o'), is(4));
    assertThat(new StringFBuffer(input).indexOf(5, 'y', 'z'), is(-1));
    assertThat(new StringFBuffer(input).indexOf(5, 'w', 'o'), is(6));
  }

  private static StringFBuilder create(String input) {
    return new StringFBuilder(input);
  }

  private static StringFBuilder create() {
    return new StringFBuilder();
  }
  
  private static StringFBuilder create(int capacity) {
    return new StringFBuilder(capacity);
  }

} /* ENDCLASS */
