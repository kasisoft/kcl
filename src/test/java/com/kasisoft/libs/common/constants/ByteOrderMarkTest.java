package com.kasisoft.libs.common.constants;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.*;

import org.testng.annotations.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ByteOrderMarkTest extends AbstractTestCase {

  private void testIdentify(ByteOrderMark expected, String filename) {
    var utf8    = getResource(filename);
    var data1   = IoFunctions.loadBytes(utf8, 100);
    var bom     = ByteOrderMark.identify(data1).orElseThrow(() -> new AssertionError());
    assertThat(bom, is(expected));
  }
  
  @Test
  public void identify() {
    testIdentify(ByteOrderMark.UTF8, "utf8.txt");
    testIdentify(ByteOrderMark.UTF16LE, "utf16le.txt");
    testIdentify(ByteOrderMark.UTF16BE, "utf16be.txt");
  }

  
  @Test
  public void identify__NotMatching() {
    var identified = ByteOrderMark.identify("simple".getBytes());
    assertNotNull(identified);
    assertFalse(identified.isPresent());
  }

  private void testStartsWith(ByteOrderMark expected, String filename) {
    var utf8    = getResource(filename);
    var data1   = IoFunctions.loadBytes(utf8, 100);
    assertTrue(expected.startsWith(data1));
  }

  @Test
  public void startsWith() {
    testStartsWith(ByteOrderMark.UTF8, "utf8.txt");
    testStartsWith(ByteOrderMark.UTF16LE, "utf16le.txt");
    testStartsWith(ByteOrderMark.UTF16BE, "utf16be.txt");
  }


} /* ENDCLASS */
