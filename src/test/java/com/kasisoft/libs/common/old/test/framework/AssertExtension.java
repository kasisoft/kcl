package com.kasisoft.libs.common.old.test.framework;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import com.kasisoft.libs.common.old.io.*;

import java.util.*;

import java.io.*;

/**
 * Assertion tool class. Presents assertion methods with a more natural parameter order.
 * The order is always <B>actualValue</B>, <B>expectedValue</B> [, message].
 *
 * @author <a href='mailto:the_mindstorm@evolva.ro'>Alexandru Popescu</a>
 */
public class AssertExtension {
 
  @SuppressWarnings("null")
  public static void assertEquals(final File actual, final File expected ) {
    if(expected == actual) {
      return;
    }
    if(expected == null) {
      fail("expected a null File, but not null found. ");
    }
    if(actual == null) {
      fail("expected not null File, but null found. ");
    }
    File canactual = null;
    File canexpected = null;
    try {
      canactual = actual.getCanonicalFile();
      canexpected = expected.getCanonicalFile();
    } catch(IOException ex) {
      fail(ex.getMessage());
    }
    if(canexpected.exists()) {
      if(!canactual.exists()) {
        fail("actual File does not exist.");
      }
    } else {
      if(canactual.exists()) {
        fail("actual File should not exist.");
      }
    }
    if(canexpected.isFile()) {
      if(!canactual.isFile()) {
        fail("actual File is supposed to denote a file .");
      }
      assertThat( canactual.length(), is( canexpected.length() ) );
      byte[] expecteddata = IoFunctions.loadBytes( canexpected, null );
      byte[] actualdata   = IoFunctions.loadBytes( canactual, null );
      assertThat( actualdata, is( expecteddata ) );
    } else {
      if(canactual.isFile()) {
        fail("actual File is not supposed to denote a file .");
      }
    }
    if(canexpected.isDirectory()) {
      File[] expectedchildren = canexpected.listFiles();
      File[] actualchildren   = canactual.listFiles();
      assertNotNull( expectedchildren );
      assertNotNull( actualchildren );
      if(expectedchildren.length != actualchildren.length) {
        fail("invalid.1");
      }
      Arrays.sort( expectedchildren );
      Arrays.sort( actualchildren   );
      for(int i = 0; i < expectedchildren.length; i++) {
        assertEquals( actualchildren[i], expectedchildren[i] );
      }
    }
  }

} /* ENDCLASS */
