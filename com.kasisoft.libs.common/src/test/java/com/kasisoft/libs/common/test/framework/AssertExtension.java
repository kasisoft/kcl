package com.kasisoft.libs.common.test.framework;

import com.kasisoft.libs.common.util.*;

import org.testng.*;

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
  static public void assertEquals(final File actual, final File expected ) {
    if(expected == actual) {
      return;
    }
    if(expected == null) {
      Assert.fail("expected a null File, but not null found. ");
    }
    if(actual == null) {
      Assert.fail("expected not null File, but null found. ");
    }
    File canactual = null;
    File canexpected = null;
    try {
      canactual = actual.getCanonicalFile();
      canexpected = expected.getCanonicalFile();
    } catch(IOException ex) {
      Assert.fail(ex.getMessage());
    }
    if(canexpected.exists()) {
      if(!canactual.exists()) {
        Assert.fail("actual File does not exist.");
      }
    } else {
      if(canactual.exists()) {
        Assert.fail("actual File should not exist.");
      }
    }
    if(canexpected.isFile()) {
      if(!canactual.isFile()) {
        Assert.fail("actual File is supposed to denote a file .");
      }
      Assert.assertEquals(canactual.length(), canexpected.length());
      byte[] expecteddata = loadFile(canexpected);
      byte[] actualdata   = loadFile(canactual);
      Assert.assertEquals(actualdata, expecteddata);
    } else {
      if(canactual.isFile()) {
        Assert.fail("actual File is not supposed to denote a file .");
      }
    }
    if(canexpected.isDirectory()) {
      File[] expectedchildren = canexpected.listFiles();
      File[] actualchildren = canactual.listFiles();
      if(expectedchildren.length != actualchildren.length) {
        Assert.fail("invalid.1");
      }
      Arrays.sort(expectedchildren);
      Arrays.sort(actualchildren);
      for(int i = 0; i < expectedchildren.length; i++) {
        assertEquals(actualchildren[i], expectedchildren[i]);
      }
    }
  }
  
  private static byte[] loadFile(File file) {
    byte[]      result   = new byte[ (int) file.length() ];
    InputStream instream = null;
    try {
      instream = new FileInputStream( file );
      if(instream.read(result) != result.length) {
        Assert.fail("Couldn't read data from file '" + file + "' !");
      }
    } catch( IOException ex ) {
      Assert.fail("File loading fails. Cause: " + ex.getMessage());
    } finally {
      MiscFunctions.close( instream );
    }
    return result;
  }

}
