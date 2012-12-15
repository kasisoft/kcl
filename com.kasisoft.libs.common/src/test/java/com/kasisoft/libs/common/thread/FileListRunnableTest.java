/**
 * Name........: FileListRunnableTest
 * Description.: Test for the class 'FileListRunnable'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.thread;

import com.kasisoft.libs.common.io.*;
import com.kasisoft.libs.common.test.framework.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

import java.io.*;

/**
 * Test for the class 'FileListRunnable'.
 */
@Test(groups="all")
public class FileListRunnableTest {
  
  @Test
  public void listFiles() throws InterruptedException {
    File                tempdir   = IoFunctions.newTempFile( "temp-", null );
    tempdir.mkdirs();
    List<File>          created   = Utilities.createFileSystemStructure( tempdir );
    Collections.sort( created );
    FileListRunnable    runnable  = new FileListRunnable( tempdir.listFiles() );
    Thread              thread    = new Thread( runnable );
    thread.start();
    thread.join();
    Assert.assertTrue( runnable.hasCompleted() );
    List<File> files = runnable.getAllFiles();
    Assert.assertEquals( files.size(), created.size() );
    Collections.sort( files );
    Assert.assertEquals( files, created );
  }
  
} /* ENDCLASS */
