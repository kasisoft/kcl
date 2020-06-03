package com.kasisoft.libs.common.text;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.function.Function;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Common tests for StringLike types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractStringLikeTestCase<T extends StringLike<T>> {

  Class<T>   stringLikeType;
  
  @DataProvider(name = "dataStringBuffers")
  public Object[][] dataStringBuffers() {
    return new Object[][] {
      new Object[] {create()},
      new Object[] {create(256)},
      new Object[] {create("")},
    };
  }
  
  @Test(groups = "all")
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
  
  @Test(groups = "all")
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
  
  @Test(groups = "all")
  public void getChars() {
    
    var charray = new char[20];
    Arrays.fill(charray, 'A');
    var buffer  = create("Hello World");
    buffer.getChars(2, -5, charray, -10);
    assertThat(String.valueOf(charray), is("AAAAAAAAAAllo AAAAAA"));

  }

  @Test(groups = "all")
  public void subSequence() {
    var buffer = create("Hello World");
    var seq    = buffer.subSequence(0, -2);
    assertThat(String.valueOf(seq), is("Hello Wor"));
  }

  @Test(groups = "all")
  public void replace() {
    var buffer = create("Hello World");
    buffer.replace(-5, 0, "Fred");
    assertThat(buffer.toString(), is("Hello Fred"));
  }
  
  @Test(groups = "all")
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
    assertTrue(stringLikeType.isAssignableFrom(read.getClass()));
    
    var readBuffer = (T) read;
    assertThat(buffer.compareTo(readBuffer), is(0));
    
  }
  
  @Test(groups = "all")
  public void compareTo() throws Exception {
    var buffer = create("Dummy");
    assertTrue(buffer.compareTo(buffer) == 0);
  }

  @Test(groups = "all")
  public void firstUp() {
    
    var buffer1 = create("Dummy");
    buffer1.firstUp();
    assertThat(buffer1.toString(), is("Dummy"));

    var buffer2 = create("dummy");
    buffer2.firstUp();
    assertThat(buffer2.toString(), is("Dummy"));

  }

  @Test(groups = "all")
  public void firstDown() {
    
    var buffer1 = create("Dummy");
    buffer1.firstDown();
    assertThat(buffer1.toString(), is("dummy"));

    var buffer2 = create("dummy");
    buffer2.firstDown();
    assertThat(buffer2.toString(), is("dummy"));

  }

  @Test(groups = "all")
  public void camelCase() {
    assertThat(create("").camelCase().toString(), is(""));
    assertThat(create("simple").camelCase().toString(), is("simple"));
    assertThat(create("simpleTon").camelCase().toString(), is("simpleTon"));
    assertThat(create("simple_ton").camelCase().toString(), is("simpleTon"));
    assertThat(create("simple__ton").camelCase().toString(), is("simpleTon"));
    assertThat(create("simple ton").camelCase().toString(), is("simpleTon"));
    assertThat(create("Simple-ton").camelCase().toString(), is("simpleTon"));
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void appendF(T buffer) {
    buffer.appendF("My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf(17));
    assertThat(buffer.toString(), is("My test is this: Hello World ! Not 0x11 !"));
  }
  
  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void charAt(T buffer) {
    buffer.appendF("My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf(17));
    assertThat(buffer.charAt(0), is('M'));
    assertThat(buffer.charAt(-1), is('!'));
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void substring(T buffer) {
    buffer.appendF("My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf(17));
    assertThat(buffer.substring(0, 2), is("My"));
    assertThat(buffer.substring(-10), is("Not 0x11 !"));
  }
  
  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void trimLeading(T buffer) {
    buffer.appendF("\r\n   My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf(17));
    buffer.trimLeading();
    assertThat(buffer.toString(), is("My test is this: Hello World ! Not 0x11 !"));
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void trimTrailing(T buffer) {
    buffer.appendF("My test is this: %s ! Not 0x%02x !\r\n   ", "Hello World", Integer.valueOf(17));
    buffer.trimTrailing();
    assertThat(buffer.toString(), is("My test is this: Hello World ! Not 0x11 !"));
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void trim(T buffer) {
    buffer.appendF("\r\n   My test is this: %s ! Not 0x%02x !\r\n   ", "Hello World", Integer.valueOf(17));
    buffer.trim();
    assertThat(buffer.toString(), is("My test is this: Hello World ! Not 0x11 !"));
  }
  
  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void endsWith(T buffer) {
    
    buffer.appendF("The frog is here !");
    
    assertTrue  (buffer.endsWith(true, "here !"));
    assertFalse (buffer.endsWith(true, "HERE !"));
    assertTrue  (buffer.endsWith(false, "HERE !"));
    assertTrue  (buffer.endsWith("here !"));
    assertFalse (buffer.endsWith("HERE !"));
    assertFalse (buffer.endsWith(true, "The frog is here ! Oops !"));
    assertFalse (buffer.endsWith(false, "The frog is here ! Oops !"));
    
  }

  @Test(groups = "all")
  public void endsWithMany() {
    
    var buffer = create("The frog is here !");
    
    assertNull(buffer.endsWithMany("Here !"));
    assertThat(buffer.endsWithMany(false, "Here !"), is("Here !"));
    assertThat(buffer.endsWithMany("here !"), is("here !"));

  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void startsWith(T buffer) {
    
    buffer.appendF("The frog is here !");
    
    assertTrue  (buffer.startsWith(true, "The"));
    assertFalse (buffer.startsWith(true, "THE"));
    assertTrue  (buffer.startsWith(false, "THE"));
    assertTrue  (buffer.startsWith("The"));
    assertFalse (buffer.startsWith("THE"));
    assertFalse (buffer.startsWith(true, "The frog is here ! Oops !"));
    assertFalse (buffer.startsWith(false, "The frog is here ! Oops !"));
    
  }

  @Test(groups = "all")
  public void startsWithMany() {
    
    var buffer = create("The frog is here !");
    
    assertNull(buffer.startsWithMany("the"));
    assertThat(buffer.startsWithMany(false, "the"), is("the"));
    assertThat(buffer.startsWithMany("The"), is("The"));
    
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void equals(T buffer) {
    buffer.appendF("The frog is here !");
    assertTrue  (buffer.equals(true, "The frog is here !"));
    assertFalse (buffer.equals(true, "THE FROG IS HERE !"));
    assertTrue  (buffer.equals(false, "THE FROG IS HERE !"));
    assertTrue  (buffer.equals("The frog is here !"));
    assertFalse (buffer.equals("THE FROG IS HERE !"));
  }
  
  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void remove(T buffer) {
    buffer.appendF("Moloko was a great band !");
    assertThat(buffer.remove("oa").toString(), is("Mlk ws  gret bnd !"));
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void reverse(T buffer) {
    buffer.append("The frog is here !");
    buffer.reverse();
    assertThat(buffer.toString(), is("! ereh si gorf ehT"));
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void indexOf(T buffer) {
    buffer.append("The frog is here !");
    assertThat(buffer.indexOf("frog"), is(4));
    assertThat(buffer.indexOf("Flansch"), is(-1));
    assertThat(buffer.indexOf("frog", 5), is(-1));
    assertThat(buffer.indexOf("Flansch", 5), is(-1));
    assertThat(buffer.indexOf("is", "frog"), is(4));
    assertThat(buffer.indexOf("is", "Flansch"), is(9));
    assertThat(buffer.indexOf("co", "Flansch"), is(-1));
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void lastIndexOf(T buffer) {
    buffer.append("The frog is here !");
    assertThat(buffer.lastIndexOf("frog"), is( 4));
    assertThat(buffer.lastIndexOf("Flansch"), is(-1));
    assertThat(buffer.lastIndexOf("frog", 5), is(4));
    assertThat(buffer.lastIndexOf("Flansch", 5), is(-1));
    assertThat(buffer.lastIndexOf("frog", "is"), is(9));
    assertThat(buffer.lastIndexOf("Flansch"), is(-1));
  }
  
  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void replace(T buffer) {
    buffer.append("The frog is here !");
    assertThat(buffer.replace('e', 'a').toString(), is("Tha frog is hara !"));
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void split(T buffer) {
    
    buffer.append("This was my 3 birthday on the 2 street.");
    String[] expected = new String[] {
      "This", "was", "my", "3", "bir", "hday", "on", "he", "2", "s", "ree", "."
    };
    String[] parts = buffer.split(" t");
    assertThat(parts, is(notNullValue()));
    assertThat(parts.length, is(expected.length));
    assertThat(parts, is(expected));
    
    buffer.setLength(0);
    buffer.append("58817162");
    parts = buffer.split("0123456789");
    assertNotNull(parts);
    assertThat(parts.length, is(0));
    
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void splitRegex(T buffer) {
    
    buffer.append("This was my 3 birthday on the 2 street.");
    String[] expected = new String[] {
      "This was my ", " birthday on the ", " street."
    };
    String[] parts = buffer.splitRegex("[0-9]+");
    assertThat(parts, is(notNullValue()));
    assertThat(parts.length, is(expected.length));
    assertThat(parts, is(expected));
    
    buffer.setLength(0);
    buffer.append("58817162");
    parts = buffer.splitRegex("[0-9]+");
    assertNotNull(parts);
    assertThat(parts.length, is(0));
    
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void replaceAll(T buffer) {
    
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

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void replaceFirst(T buffer) {
    
    buffer.append("This was my 3 birthday on the 2 street.");
    buffer.replaceFirst("[0-9]+", "abce");
    assertThat(buffer.toString(), is("This was my abce birthday on the 2 street."));
    
    buffer.setLength(0);
    buffer.append("58817162");
    buffer.replaceFirst("[0-9]+", "dodo");
    assertThat(buffer.toString(), is("dodo"));
    
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void replaceLast(T buffer) {
    
    buffer.append("This was my 3 birthday on the 2 street.");
    buffer.replaceLast("[0-9]+", "abce");
    assertThat(buffer.toString(), is("This was my 3 birthday on the abce street."));
    
    buffer.setLength(0);
    buffer.append("58817162");
    buffer.replaceLast("[0-9]+", "dodo");
    assertThat(buffer.toString(), is("dodo"));
    
  }
  
  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void capacity(T buffer) {

    int capacity = buffer.capacity();
    assertTrue(capacity > 0);
    
    buffer.ensureCapacity(capacity + 16384);
    int newcapacity = buffer.capacity();
    assertThat(newcapacity, is(capacity + 16384));
    
    int length = buffer.length();
    buffer.trimToSize();
    int shrinkedcapacity = buffer.capacity();
    assertThat(shrinkedcapacity, is(length));
    
  }
  
  @DataProvider(name = "caseChangeData")
  public Object[][] caseChangeData() {
    return new Object[][] {
      { new String[] { ""              }, new String[] { ""              } },
      { new String[] { "hello"         }, new String[] { "HELLO"         } },
      { new String[] { "hello", "bibo" }, new String[] { "HELLO", "BIBO" } },
    };
  }
  
  @Test(dataProvider = "caseChangeData", groups = "all")
  public void toUpperCase(String[] current, String[] expected) {
    for (int i = 0; i < current.length; i++) {
      assertThat(create(current[i]).toUpperCase().toString(), is(expected[i]));
    }
  }

  @Test(dataProvider = "caseChangeData", groups = "all")
  public void toLowerCase(String[] expected, String[] current) {
    for (int i = 0; i < current.length; i++) {
      assertThat(create(current[i]).toLowerCase().toString(), is(expected[i]));
    }
  }

  @Test(groups = "all")
  public void appendIfMissing() {
    assertThat(create("Hello/").appendIfMissing("/").toString(), is("Hello/"));
    assertThat(create("Hello").appendIfMissing("/").toString(), is("Hello/"));
  }

  @Test(groups = "all")
  public void prependIfMissing() {
    assertThat(create("/Hello").prependIfMissing("/").toString(), is("/Hello"));
    assertThat(create("Hello").prependIfMissing("/").toString(), is("/Hello"));
  }

  @Test(groups = "all")
  public void removeEnd() {
    assertThat(create("startHello").removeEnd("Dodo").toString(), is("startHello"));
    assertThat(create("startHello").removeEnd("Hello").toString(), is("start"));
    assertThat(create("startHello").removeEnd(true, "hello").toString(), is("startHello"));
  }
  
  @Test(groups = "all")
  public void removeStart() {
    assertThat(create("startHello").removeStart("stop").toString(), is("startHello"));
    assertThat(create("startHello").removeStart("start").toString(), is("Hello"));
    assertThat(create("startHello").removeStart(true, "Start").toString(), is("startHello"));
  }
  
  @Test(groups = "all")
  public void replaceAll() {
    
    var replacements = new HashMap<String, String>();
    replacements.put( "name"    , "Daniel Kasmeroglu" );
    replacements.put( "company" , "Kasisoft"          );
    
    var buffer1 = create("The pseudo company company is driven by name [company]");
    buffer1.replaceAll(replacements);
    assertThat(buffer1.toString(), is( "The pseudo Kasisoft Kasisoft is driven by Daniel Kasmeroglu [Kasisoft]" ) );

    var buffer2 = create("The pseudo company ${company} is driven by ${name} [${company}]") ;
    buffer2.replaceAll(replacements, "${%s}");
    assertThat(buffer2.toString(), is( "The pseudo company Kasisoft is driven by Daniel Kasmeroglu [Kasisoft]" ) );

  }
  
  @DataProvider(name = "createRegionReplaceSimple")
  public Object[][] createRegionReplaceSimple() {
    return new Object[][] {
      {create("this is // my text // without a section // dodo //"), "", "this is  without a section "},
      {create("this is // // my text // without a section // dodo //"), "", "this is  my text  dodo //"},
      {create("this is // // my text // without a section // dodo // //"), "", "this is  my text  dodo "},
      {create("this is // my text // without a section // dodo //"), "bibo", "this is bibo without a section bibo"},
      {create("this is // // my text // without a section // dodo //"), "bibo", "this is bibo my text bibo dodo //"},
      {create("this is // // my text // without a section // dodo // //"), "bibo", "this is bibo my text bibo dodo bibo"},
    };
  }
  
  @Test(groups = "all", dataProvider = "createRegionReplaceSimple")
  public void regionReplaceSimple(T buffer, String replacement, String expected) {
    assertThat(buffer.replaceRegions("//", replacement).toString(), is( expected ) );
  }

  @DataProvider(name = "createRegionReplaceFunction")
  public Object[][] createRegionReplaceFunction() {
    Map<String, String> mapping = new HashMap<>();
    mapping.put("my text", "changed text");
    mapping.put("dodo", "dodo text");
    Function<String, CharSequence> replacement = $ -> mapping.get($.trim());
    return new Object[][] {
      {create("this is // my text // without a section // dodo //"), replacement, "this is changed text without a section dodo text"},
      {create("this is // // my text // without a section // dodo //"), replacement, "this is  my text  dodo //"},
      {create("this is // // my text // without a section // dodo // //"), replacement, "this is  my text  dodo "},
    };
  }
  
  @Test(groups = "all", dataProvider = "createRegionReplaceFunction")
  public void regionReplaceFunction(T buffer, Function<String, CharSequence> replacement, String expected) {
    assertThat(buffer.replaceRegions("//", replacement).toString(), is(expected));
  }

  protected abstract T create(String input);

  protected abstract T create();
  
  protected abstract T create(int capacity);

} /* ENDCLASS */
