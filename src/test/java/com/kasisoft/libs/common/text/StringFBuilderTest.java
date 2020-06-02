package com.kasisoft.libs.common.text;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Testcase for the class 'StringFBuilder'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class StringFBuilderTest {

  @DataProvider(name = "dataStringBuffers")
  public Object[][] dataStringBuffers() {
    return new Object[][] {
      new Object[] {new StringFBuilder()   },
      new Object[] {new StringFBuilder(256)},
      new Object[] {new StringFBuilder("") },
    };
  }
  
  @Test(groups = "all")
  public void append() {
    
    var buffer1 = new StringFBuilder();
    buffer1.append('h');
    assertThat(buffer1.toString(), is("h"));

    var buffer2 = new StringFBuilder();
    buffer2.append(false);
    assertThat(buffer2.toString(), is("false"));

    var buffer3 = new StringFBuilder();
    buffer3.append(true);
    assertThat(buffer3.toString(), is("true"));

    var buffer4 = new StringFBuilder();
    buffer4.append("text");
    assertThat(buffer4.toString(), is("text"));

    var buffer5 = new StringFBuilder();
    buffer5.append((byte) 65);
    assertThat(buffer5.toString(), is("65"));

    var buffer6 = new StringFBuilder();
    buffer6.append((short) 1882);
    assertThat(buffer6.toString(), is("1882"));

    var buffer7 = new StringFBuilder();
    buffer7.append(9_2981);
    assertThat(buffer7.toString(), is("92981"));

    var buffer8 = new StringFBuilder();
    buffer8.append(9_298_191_391_921L);
    assertThat(buffer8.toString(), is("9298191391921"));

    var buffer9 = new StringFBuilder();
    buffer9.append(3.193819388131);
    assertThat(buffer9.toString(), is("3.193819388131"));

    var buffer10 = new StringFBuilder();
    buffer10.append((float) -4.138813);
    assertThat(buffer10.toString(), is("-4.138813"));

    var buffer11 = new StringFBuilder();
    buffer11.append(Boolean.TRUE);
    assertThat(buffer11.toString(), is("true"));

    var buffer12 = new StringFBuilder();
    buffer12.append("my text", 3, -2);
    assertThat(buffer12.toString(), is("te"));
    
    var buffer13 = new StringFBuilder();
    buffer13.append(new char[] {'H', 'e', 'l', 'l', 'o'});
    assertThat(buffer13.toString(), is("Hello"));

    var buffer14 = new StringFBuilder();
    buffer14.append(new char[] {'m', 'y', ' ', 't', 'e', 'x', 't'}, 3, 2);
    assertThat(buffer14.toString(), is("te"));

 
  }

  @Test(groups = "all")
  public void insert() {
    
    var buffer1 = new StringFBuilder("Hello World");
    buffer1.insert(-5, 'h');
    assertThat(buffer1.toString(), is("Hello hWorld"));

    var buffer2 = new StringFBuilder("Hello World");
    buffer2.insert(-5, false);
    assertThat(buffer2.toString(), is("Hello falseWorld"));

    var buffer3 = new StringFBuilder("Hello World");
    buffer3.insert(-5, true);
    assertThat(buffer3.toString(), is("Hello trueWorld"));

    var buffer4 = new StringFBuilder("Hello World");
    buffer4.insert(-5, "text");
    assertThat(buffer4.toString(), is("Hello textWorld"));

    var buffer5 = new StringFBuilder("Hello World");
    buffer5.insert(-5, (byte) 65);
    assertThat(buffer5.toString(), is("Hello 65World"));

    var buffer6 = new StringFBuilder("Hello World");
    buffer6.insert(-5, (short) 1882);
    assertThat(buffer6.toString(), is("Hello 1882World"));

    var buffer7 = new StringFBuilder("Hello World");
    buffer7.insert(-5, 9_2981);
    assertThat(buffer7.toString(), is("Hello 92981World"));

    var buffer8 = new StringFBuilder("Hello World");
    buffer8.insert(-5, 9_298_191_391_921L);
    assertThat(buffer8.toString(), is("Hello 9298191391921World"));

    var buffer9 = new StringFBuilder("Hello World");
    buffer9.insert(-5, 3.193819388131);
    assertThat(buffer9.toString(), is("Hello 3.193819388131World"));

    var buffer10 = new StringFBuilder("Hello World");
    buffer10.insert(-5, (float) -4.138813);
    assertThat(buffer10.toString(), is("Hello -4.138813World"));

    var buffer11 = new StringFBuilder("Hello World");
    buffer11.insert(-5, Boolean.TRUE);
    assertThat(buffer11.toString(), is("Hello trueWorld"));

    var buffer12 = new StringFBuilder("Hello World");
    buffer12.insert(-5, "my text", 3, -2);
    assertThat(buffer12.toString(), is("Hello teWorld"));
    
    var buffer13 = new StringFBuilder("Hello World");
    buffer13.insert(-5, new char[] {'H', 'e', 'l', 'l', 'o'});
    assertThat(buffer13.toString(), is("Hello HelloWorld"));

    var buffer14 = new StringFBuilder("Hello World");
    buffer14.insert(-5, new char[] {'m', 'y', ' ', 't', 'e', 'x', 't'}, 3, 2);
    assertThat(buffer14.toString(), is("Hello teWorld"));

  }
  
  @Test(groups = "all")
  public void getChars() {
    
    var charray = new char[20];
    Arrays.fill(charray, 'A');
    var buffer  = new StringFBuilder("Hello World");
    buffer.getChars(2, -5, charray, -10);
    assertThat(String.valueOf(charray), is("AAAAAAAAAAllo AAAAAA"));

  }
  
  @Test(groups = "all")
  public void subSequence() {
    var          buffer  = new StringFBuilder("Hello World");
    CharSequence seq     = buffer.subSequence(0, -2);
    assertThat(String.valueOf(seq), is("Hello Wor"));
  }

  @Test(groups = "all")
  public void replace() {
    var          buffer  = new StringFBuilder("Hello World");
    buffer.replace(-5, 0, "Fred");
    assertThat(buffer.toString(), is("Hello Fred"));
  }
  
  @Test(groups = "all")
  public void serialization() throws Exception {
    
    StringFBuilder          buffer      = new StringFBuilder("Dummy");
    
    ByteArrayOutputStream   byteout     = new ByteArrayOutputStream();
    try (ObjectOutputStream objectout   = new ObjectOutputStream(byteout)) {
      objectout.writeObject(buffer);
    }
    
    Object                  read        = null;
    ByteArrayInputStream    bytein      = new ByteArrayInputStream(byteout.toByteArray());
    try (ObjectInputStream  objectin    = new ObjectInputStream(bytein)) {
      read = objectin.readObject();
    }
    
    assertNotNull(read);
    assertTrue(read instanceof StringFBuilder);
    
    StringFBuilder readBuffer = (StringFBuilder) read;
    assertThat(buffer.compareTo(readBuffer), is(0));
    
  }
  
  @Test(groups = "all")
  public void compareTo() throws Exception {
    StringFBuilder buffer = new StringFBuilder("Dummy");
    assertTrue(buffer.compareTo(buffer) == 0);
  }
  
  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void appendF(StringFBuilder buffer) {
    buffer.appendF("My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf(17));
    assertThat(buffer.toString(), is( "My test is this: Hello World ! Not 0x11 !"));
  }
  
  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void charAt(StringFBuilder buffer) {
    buffer.appendF("My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf(17));
    assertThat(buffer.charAt(0)  , is( 'M' ));
    assertThat(buffer.charAt(-1) , is( '!' ));
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void substring(StringFBuilder buffer) {
    buffer.appendF("My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf(17));
    assertThat(buffer.substring(0, 2) , is("My"));
    assertThat(buffer.substring(-10) , is("Not 0x11 !"));
  }
  
  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void trimLeading(StringFBuilder buffer) {
    buffer.appendF("\r\n   My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf(17));
    buffer.trimLeading();
    assertThat(buffer.toString(), is("My test is this: Hello World ! Not 0x11 !"));
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void trimTrailing(StringFBuilder buffer) {
    buffer.appendF("My test is this: %s ! Not 0x%02x !\r\n   ", "Hello World", Integer.valueOf(17));
    buffer.trimTrailing();
    assertThat(buffer.toString(), is( "My test is this: Hello World ! Not 0x11 !"));
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void trim(StringFBuilder buffer) {
    buffer.appendF("\r\n   My test is this: %s ! Not 0x%02x !\r\n   ", "Hello World", Integer.valueOf(17));
    buffer.trim();
    assertThat(buffer.toString(), is( "My test is this: Hello World ! Not 0x11 !"));
  }
  
  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void endsWith(StringFBuilder buffer) {
    buffer.appendF("The frog is here !");
    assertTrue  (buffer.endsWith(true  , "here !"));
    assertFalse (buffer.endsWith(true  , "HERE !"));
    assertTrue  (buffer.endsWith(false , "HERE !"));
    assertTrue  (buffer.endsWith("here !"));
    assertFalse (buffer.endsWith("HERE !"));
    assertFalse (buffer.endsWith(true  , "The frog is here ! Oops !"));
    assertFalse (buffer.endsWith(false , "The frog is here ! Oops !"));
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void startsWith(StringFBuilder buffer) {
    buffer.appendF("The frog is here !" );
    assertTrue  (buffer.startsWith( true  , "The"));
    assertFalse (buffer.startsWith( true  , "THE"));
    assertTrue  (buffer.startsWith( false , "THE"));
    assertTrue  (buffer.startsWith( "The"));
    assertFalse (buffer.startsWith( "THE"));
    assertFalse (buffer.startsWith( true  , "The frog is here ! Oops !"));
    assertFalse (buffer.startsWith( false , "The frog is here ! Oops !"));
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void equals(StringFBuilder buffer) {
    buffer.appendF("The frog is here !");
    assertTrue  (buffer.equals( true  , "The frog is here !"));
    assertFalse (buffer.equals( true  , "THE FROG IS HERE !"));
    assertTrue  (buffer.equals( false , "THE FROG IS HERE !"));
    assertTrue  (buffer.equals( "The frog is here !"));
    assertFalse (buffer.equals( "THE FROG IS HERE !"));
  }
  
  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void remove(StringFBuilder buffer) {
    buffer.appendF("Moloko was a great band !");
    assertThat(buffer.remove("oa").toString(), is("Mlk ws  gret bnd !"));
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void reverse(StringFBuilder buffer) {
    buffer.append("The frog is here !");
    buffer.reverse();
    assertThat(buffer.toString(), is("! ereh si gorf ehT"));
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void indexOf( StringFBuilder buffer ) {
    buffer.append("The frog is here !" );
    assertThat(buffer.indexOf("frog"   ), is(4));
    assertThat(buffer.indexOf("Flansch"), is(-1));
    assertThat(buffer.indexOf("frog"    , 5), is(-1));
    assertThat(buffer.indexOf("Flansch" , 5), is(-1));
    assertThat(buffer.indexOf("is", "frog"   ), is(4));
    assertThat(buffer.indexOf("is", "Flansch"), is(9));
    assertThat(buffer.indexOf("co", "Flansch"), is(-1));
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void lastIndexOf(StringFBuilder buffer) {
    buffer.append("The frog is here !");
    assertThat(buffer.lastIndexOf("frog"   ), is( 4));
    assertThat(buffer.lastIndexOf("Flansch"), is(-1));
    assertThat(buffer.lastIndexOf("frog"    , 5), is(4));
    assertThat(buffer.lastIndexOf("Flansch" , 5), is(-1));
    assertThat(buffer.lastIndexOf("frog", "is"), is(9));
    assertThat(buffer.lastIndexOf("Flansch"   ), is(-1));
  }
  
  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void replace(StringFBuilder buffer) {
    buffer.append("The frog is here !");
    assertThat(buffer.replace('e', 'a').toString(), is("Tha frog is hara !"));
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void split(StringFBuilder buffer) {
    
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
  public void splitRegex(StringFBuilder buffer) {
    
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

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void replaceFirst(StringFBuilder buffer) {
    
    buffer.append("This was my 3 birthday on the 2 street.");
    buffer.replaceFirst("[0-9]+", "abce");
    assertThat(buffer.toString(), is("This was my abce birthday on the 2 street."));
    
    buffer.setLength(0);
    buffer.append("58817162");
    buffer.replaceFirst("[0-9]+", "dodo");
    assertThat(buffer.toString(), is("dodo"));
    
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void replaceLast(StringFBuilder buffer) {
    
    buffer.append("This was my 3 birthday on the 2 street.");
    buffer.replaceLast("[0-9]+", "abce");
    assertThat(buffer.toString(), is("This was my 3 birthday on the abce street."));
    
    buffer.setLength(0);
    buffer.append("58817162");
    buffer.replaceLast("[0-9]+", "dodo");
    assertThat(buffer.toString(), is("dodo"));
    
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void capacity(StringFBuilder buffer) {

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

} /* ENDCLASS */
