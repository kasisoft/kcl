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
 * Testcase for the class 'StringFBuffer'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class StringFBufferTest {

  @DataProvider(name = "dataStringBuffers")
  public Object[][] dataStringBuffers() {
    return new Object[][] {
      new Object[] {new StringFBuffer()   },
      new Object[] {new StringFBuffer(256)},
      new Object[] {new StringFBuffer("") },
    };
  }
  
  @Test(groups = "all")
  public void append() {
    
    var buffer1 = new StringFBuffer();
    buffer1.append('h');
    assertThat(buffer1.toString(), is("h"));

    var buffer2 = new StringFBuffer();
    buffer2.append(false);
    assertThat(buffer2.toString(), is("false"));

    var buffer3 = new StringFBuffer();
    buffer3.append(true);
    assertThat(buffer3.toString(), is("true"));

    var buffer4 = new StringFBuffer();
    buffer4.append("text");
    assertThat(buffer4.toString(), is("text"));

    var buffer5 = new StringFBuffer();
    buffer5.append((byte) 65);
    assertThat(buffer5.toString(), is("65"));

    var buffer6 = new StringFBuffer();
    buffer6.append((short) 1882);
    assertThat(buffer6.toString(), is("1882"));

    var buffer7 = new StringFBuffer();
    buffer7.append(9_2981);
    assertThat(buffer7.toString(), is("92981"));

    var buffer8 = new StringFBuffer();
    buffer8.append(9_298_191_391_921L);
    assertThat(buffer8.toString(), is("9298191391921"));

    var buffer9 = new StringFBuffer();
    buffer9.append(3.193819388131);
    assertThat(buffer9.toString(), is("3.193819388131"));

    var buffer10 = new StringFBuffer();
    buffer10.append((float) -4.138813);
    assertThat(buffer10.toString(), is("-4.138813"));

    var buffer11 = new StringFBuffer();
    buffer11.append(Boolean.TRUE);
    assertThat(buffer11.toString(), is("true"));

    var buffer12 = new StringFBuffer();
    buffer12.append("my text", 3, -2);
    assertThat(buffer12.toString(), is("te"));
    
    var buffer13 = new StringFBuffer();
    buffer13.append(new char[] {'H', 'e', 'l', 'l', 'o'});
    assertThat(buffer13.toString(), is("Hello"));

    var buffer14 = new StringFBuffer();
    buffer14.append(new char[] {'m', 'y', ' ', 't', 'e', 'x', 't'}, 3, 2);
    assertThat(buffer14.toString(), is("te"));

  }

  
  @Test(groups = "all")
  public void insert() {
    
    var buffer1 = new StringFBuffer("Hello World");
    buffer1.insert(-5, 'h');
    assertThat(buffer1.toString(), is("Hello hWorld"));

    var buffer2 = new StringFBuffer("Hello World");
    buffer2.insert(-5, false);
    assertThat(buffer2.toString(), is("Hello falseWorld"));

    var buffer3 = new StringFBuffer("Hello World");
    buffer3.insert(-5, true);
    assertThat(buffer3.toString(), is("Hello trueWorld"));

    var buffer4 = new StringFBuffer("Hello World");
    buffer4.insert(-5, "text");
    assertThat(buffer4.toString(), is("Hello textWorld"));

    var buffer5 = new StringFBuffer("Hello World");
    buffer5.insert(-5, (byte) 65);
    assertThat(buffer5.toString(), is("Hello 65World"));

    var buffer6 = new StringFBuffer("Hello World");
    buffer6.insert(-5, (short) 1882);
    assertThat(buffer6.toString(), is("Hello 1882World"));

    var buffer7 = new StringFBuffer("Hello World");
    buffer7.insert(-5, 9_2981);
    assertThat(buffer7.toString(), is("Hello 92981World"));

    var buffer8 = new StringFBuffer("Hello World");
    buffer8.insert(-5, 9_298_191_391_921L);
    assertThat(buffer8.toString(), is("Hello 9298191391921World"));

    var buffer9 = new StringFBuffer("Hello World");
    buffer9.insert(-5, 3.193819388131);
    assertThat(buffer9.toString(), is("Hello 3.193819388131World"));

    var buffer10 = new StringFBuffer("Hello World");
    buffer10.insert(-5, (float) -4.138813);
    assertThat(buffer10.toString(), is("Hello -4.138813World"));

    var buffer11 = new StringFBuffer("Hello World");
    buffer11.insert(-5, Boolean.TRUE);
    assertThat(buffer11.toString(), is("Hello trueWorld"));

    var buffer12 = new StringFBuffer("Hello World");
    buffer12.insert(-5, "my text", 3, -2);
    assertThat(buffer12.toString(), is("Hello teWorld"));
    
    var buffer13 = new StringFBuffer("Hello World");
    buffer13.insert(-5, new char[] {'H', 'e', 'l', 'l', 'o'});
    assertThat(buffer13.toString(), is("Hello HelloWorld"));

    var buffer14 = new StringFBuffer("Hello World");
    buffer14.insert(-5, new char[] {'m', 'y', ' ', 't', 'e', 'x', 't'}, 3, 2);
    assertThat(buffer14.toString(), is("Hello teWorld"));

  }
  
  @Test(groups = "all")
  public void getChars() {
    
    var charray = new char[20];
    Arrays.fill(charray, 'A');
    var buffer  = new StringFBuffer("Hello World");
    buffer.getChars(2, -5, charray, -10);
    assertThat(String.valueOf(charray), is("AAAAAAAAAAllo AAAAAA"));

  }

  @Test(groups = "all")
  public void subSequence() {
    var          buffer  = new StringFBuffer("Hello World");
    CharSequence seq     = buffer.subSequence(0, -2);
    assertThat(String.valueOf(seq), is("Hello Wor"));
  }

  @Test(groups = "all")
  public void replace() {
    var          buffer  = new StringFBuffer("Hello World");
    buffer.replace(-5, 0, "Fred");
    assertThat(buffer.toString(), is("Hello Fred"));
  }
  
  @Test(groups = "all")
  public void serialization() throws Exception {
    
    StringFBuffer           buffer      = new StringFBuffer("Dummy");
    
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
    assertTrue(read instanceof StringFBuffer);
    
    StringFBuffer readBuffer = (StringFBuffer) read;
    assertThat(buffer.compareTo(readBuffer), is(0));
    
  }
  
  @Test(groups = "all")
  public void compareTo() throws Exception {
    StringFBuffer buffer = new StringFBuffer("Dummy");
    assertTrue(buffer.compareTo(buffer) == 0);
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void appendF(StringFBuffer buffer) {
    buffer.appendF("My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf(17));
    assertThat(buffer.toString(), is("My test is this: Hello World ! Not 0x11 !"));
  }
  
  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void charAt(StringFBuffer buffer) {
    buffer.appendF("My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf(17));
    assertThat(buffer.charAt(0), is('M'));
    assertThat(buffer.charAt(-1), is('!'));
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void substring(StringFBuffer buffer) {
    buffer.appendF("My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf(17));
    assertThat(buffer.substring(0, 2), is("My"));
    assertThat(buffer.substring(-10), is("Not 0x11 !"));
  }
  
  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void trimLeading(StringFBuffer buffer) {
    buffer.appendF("\r\n   My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf(17));
    buffer.trimLeading();
    assertThat(buffer.toString(), is("My test is this: Hello World ! Not 0x11 !"));
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void trimTrailing(StringFBuffer buffer) {
    buffer.appendF("My test is this: %s ! Not 0x%02x !\r\n   ", "Hello World", Integer.valueOf(17));
    buffer.trimTrailing();
    assertThat(buffer.toString(), is("My test is this: Hello World ! Not 0x11 !"));
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void trim(StringFBuffer buffer) {
    buffer.appendF("\r\n   My test is this: %s ! Not 0x%02x !\r\n   ", "Hello World", Integer.valueOf(17));
    buffer.trim();
    assertThat(buffer.toString(), is("My test is this: Hello World ! Not 0x11 !"));
  }
  
  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void endsWith(StringFBuffer buffer) {
    buffer.appendF("The frog is here !");
    assertTrue  (buffer.endsWith(true, "here !"));
    assertFalse (buffer.endsWith(true, "HERE !"));
    assertTrue  (buffer.endsWith(false, "HERE !"));
    assertTrue  (buffer.endsWith("here !"));
    assertFalse (buffer.endsWith("HERE !"));
    assertFalse (buffer.endsWith(true, "The frog is here ! Oops !"));
    assertFalse (buffer.endsWith(false, "The frog is here ! Oops !"));
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void startsWith(StringFBuffer buffer) {
    buffer.appendF("The frog is here !");
    assertTrue  (buffer.startsWith(true, "The"));
    assertFalse (buffer.startsWith(true, "THE"));
    assertTrue  (buffer.startsWith(false, "THE"));
    assertTrue  (buffer.startsWith("The"));
    assertFalse (buffer.startsWith("THE"));
    assertFalse (buffer.startsWith(true, "The frog is here ! Oops !"));
    assertFalse (buffer.startsWith(false, "The frog is here ! Oops !"));
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void equals(StringFBuffer buffer) {
    buffer.appendF("The frog is here !");
    assertTrue  (buffer.equals(true, "The frog is here !"));
    assertFalse (buffer.equals(true, "THE FROG IS HERE !"));
    assertTrue  (buffer.equals(false, "THE FROG IS HERE !"));
    assertTrue  (buffer.equals("The frog is here !"));
    assertFalse (buffer.equals("THE FROG IS HERE !"));
  }
  
  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void remove(StringFBuffer buffer) {
    buffer.appendF("Moloko was a great band !");
    assertThat(buffer.remove("oa").toString(), is("Mlk ws  gret bnd !"));
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void reverse(StringFBuffer buffer) {
    buffer.append("The frog is here !");
    buffer.reverse();
    assertThat(buffer.toString(), is("! ereh si gorf ehT"));
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void indexOf(StringFBuffer buffer) {
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
  public void lastIndexOf(StringFBuffer buffer) {
    buffer.append("The frog is here !");
    assertThat(buffer.lastIndexOf("frog"), is( 4));
    assertThat(buffer.lastIndexOf("Flansch"), is(-1));
    assertThat(buffer.lastIndexOf("frog", 5), is(4));
    assertThat(buffer.lastIndexOf("Flansch", 5), is(-1));
    assertThat(buffer.lastIndexOf("frog", "is"), is(9));
    assertThat(buffer.lastIndexOf("Flansch"), is(-1));
  }
  
  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void replace(StringFBuffer buffer) {
    buffer.append("The frog is here !");
    assertThat(buffer.replace('e', 'a').toString(), is("Tha frog is hara !"));
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void split(StringFBuffer buffer) {
    
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
  public void splitRegex(StringFBuffer buffer) {
    
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
  public void replaceAll(StringFBuffer buffer) {
    
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
  public void replaceFirst(StringFBuffer buffer) {
    
    buffer.append("This was my 3 birthday on the 2 street.");
    buffer.replaceFirst("[0-9]+", "abce");
    assertThat(buffer.toString(), is("This was my abce birthday on the 2 street."));
    
    buffer.setLength(0);
    buffer.append("58817162");
    buffer.replaceFirst("[0-9]+", "dodo");
    assertThat(buffer.toString(), is("dodo"));
    
  }

  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void replaceLast(StringFBuffer buffer) {
    
    buffer.append("This was my 3 birthday on the 2 street.");
    buffer.replaceLast("[0-9]+", "abce");
    assertThat(buffer.toString(), is("This was my 3 birthday on the abce street."));
    
    buffer.setLength(0);
    buffer.append("58817162");
    buffer.replaceLast("[0-9]+", "dodo");
    assertThat(buffer.toString(), is("dodo"));
    
  }
  
  @Test(dataProvider = "dataStringBuffers", groups = "all")
  public void capacity(StringFBuffer buffer) {

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
  
//  @Override
//  public synchronized int codePointBefore(int index) {
//    return origin.codePointBefore(adjustIndex(index));
//  }
//
//  @Override
//  public synchronized int codePointCount(int begin, int end) {
//    return origin.codePointCount(adjustIndex(begin), adjustIndex(end));
//  }
//
//  @Override
//  public synchronized int offsetByCodePoints(int index, int codepointoffset) {
//    return origin.offsetByCodePoints(adjustIndex(index), codepointoffset);
//  }
//
//  @Override
//  public synchronized void getChars(int start, int end, @NotNull char[] destination, int destbegin) {
//    origin.getChars(adjustIndex(start), adjustIndex(end), destination, adjustIndex(destination.length, destbegin));
//  }
//
//  @Override
//  public void setCodepointAt(int index, int codepoint) {
//    index     = adjustIndex(index);
//    var count = Character.charCount(codepoint);
//    delete(index, index + count);
//  }
//
//  @Override
//  public synchronized StringFBuffer append(@NotNull Object obj) {
//    origin.append(obj);
//    return this;
//  }
//
//  @Override
//  public synchronized StringFBuffer append(@NotNull CharSequence sequence, int start, int end) {
//    origin.append(sequence, adjustIndex(sequence.length(), start), adjustIndex(sequence.length(), end));
//    return this;
//  }
//
//  @Override
//  public synchronized StringFBuffer append(@NotNull char[] charray) {
//    origin.append(charray);
//    return this;
//  }
//
//  @Override
//  public synchronized StringFBuffer append(@NotNull char[] charray, int offset, int length) {
//    origin.append(charray, offset, length);
//    return this;
//  }
//  
//  @Override
//  public synchronized StringFBuffer append(boolean value) {
//    origin.append(value);
//    return this;
//  }
//
//  @Override
//  public synchronized StringFBuffer append(char value) {
//    origin.append(value);
//    return this;
//  }
//
//  @Override
//  public synchronized StringFBuffer append(int value) {
//    origin.append(value);
//    return this;
//  }
//
//  @Override
//  public synchronized StringFBuffer appendCodePoint(int codepoint) {
//    origin.appendCodePoint(codepoint);
//    return this;
//  }
//
//  @Override
//  public synchronized StringFBuffer append(long value) {
//    origin.append(value);
//    return this;
//  }
//
//  @Override
//  public synchronized StringFBuffer append(float value) {
//    origin.append(value);
//    return this;
//  }
//
//  @Override
//  public synchronized StringFBuffer append(double value) {
//    origin.append(value);
//    return this;
//  }
//
//  @Override
//  public synchronized StringFBuffer replace(int start, int end, @NotNull String str) {
//    origin.replace(adjustIndex(start), adjustIndex(end), str);
//    return this;
//  }
//
//  @Override
//  public synchronized CharSequence subSequence(int start, int end) {
//    return origin.subSequence(adjustIndex(start), adjustIndex(end));
//  }
//
//  @Override
//  public synchronized StringFBuffer insert(int index, @NotNull char[] charray, int offset, int length) {
//    origin.insert(adjustIndex(index), charray, adjustIndex(charray.length, offset), length);
//    return this;
//  }
//
//  @Override
//  public synchronized StringFBuffer insert(int offset, @NotNull Object obj) {
//    origin.insert(adjustIndex(offset), obj);
//    return this;
//  }
//
//  @Override
//  public synchronized StringFBuffer insert(int offset, @NotNull char[] value) {
//    origin.insert(adjustIndex(offset), value);
//    return this;
//  }
//
//  @Override
//  public synchronized StringFBuffer insert(int offset, @NotNull CharSequence value) {
//    origin.insert(adjustIndex(offset), value);
//    return this;
//  }
//
//  @Override
//  public synchronized StringFBuffer insert(int offset, @NotNull CharSequence value, int start, int end) {
//    origin.insert(adjustIndex(offset), value, adjustIndex(value.length(), start), adjustIndex(value.length(), end));
//    return this;
//  }
//
//  @Override
//  public synchronized StringFBuffer insert(int offset, boolean value) {
//    origin.insert(adjustIndex(offset), value);
//    return this;
//  }
//
//  @Override
//  public synchronized StringFBuffer insert(int offset, char value) {
//    origin.insert(adjustIndex(offset), value);
//    return this;
//  }
//
//  @Override
//  public synchronized StringFBuffer insert(int offset, int value) {
//    origin.insert(adjustIndex(offset), value);
//    return this;
//  }
//
//  @Override
//  public synchronized StringFBuffer insert(int offset, long value) {
//    origin.insert(adjustIndex(offset), value);
//    return this;
//  }
//
//  @Override
//  public synchronized StringFBuffer insert(int offset, float value) {
//    origin.insert(adjustIndex(offset), value);
//    return this;
//  }
//
//  @Override
//  public synchronized StringFBuffer insert(int offset, double value) {
//    origin.insert(adjustIndex(offset), value);
//    return this;
//  }
//
//  @Override
//  public synchronized IntStream chars() {
//    return origin.chars();
//  }
//  
//  @Override
//  public synchronized IntStream codePoints() {
//    return origin.codePoints();
//  }
//  
//  @Override
//  public synchronized int compareTo(@NotNull StringFBuffer another) {
//    if (this == another) {
//      return 0;
//    }
//    return origin.compareTo(another.origin);
//  }
//  
//  private void writeObject(ObjectOutputStream s) throws IOException {
//    s.writeObject(origin);
//  }
//
//  private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
//    origin = (StringBuffer) s.readObject();
//  }

} /* ENDCLASS */
