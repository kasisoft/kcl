package com.kasisoft.libs.common.constants;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.*;

import org.junit.jupiter.api.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ByteOrderMarkTest {

  private static TestResources TEST_RESOURCES = TestResources.createTestResources(ByteOrderMarkTest.class);

  private void testIdentify(ByteOrderMark expected, String filename) {
    var utf8    = TEST_RESOURCES.getResource(filename);
    var data1   = IoFunctions.loadBytes(utf8, 100);
    var bom     = ByteOrderMark.identify(data1);
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
    assertNull(identified);
  }

  private void testStartsWith(ByteOrderMark expected, String filename) {
    var utf8    = TEST_RESOURCES.getResource(filename);
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
