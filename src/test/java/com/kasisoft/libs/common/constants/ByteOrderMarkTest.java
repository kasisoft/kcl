package com.kasisoft.libs.common.constants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.kasisoft.libs.common.io.IoFunctions;

import com.kasisoft.libs.common.AbstractTestCase;

import org.testng.annotations.Test;

import java.nio.file.Path;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ByteOrderMarkTest extends AbstractTestCase {

  private void testForBom(ByteOrderMark expected, String filename) {
    Path          utf8    = getResource(filename);
    byte[]        data1   = IoFunctions.loadBytes(utf8, 100);
    ByteOrderMark bom     = ByteOrderMark.identify(data1).orElseThrow(() -> new AssertionError());
    assertThat(bom, is(expected));
  }
  
  @Test
  public void identify() {
    testForBom(ByteOrderMark.UTF8, "utf8.txt");
    testForBom(ByteOrderMark.UTF16LE, "utf16le.txt");
    testForBom(ByteOrderMark.UTF16BE, "utf16be.txt");
  }
  
} /* ENDCLASS */
