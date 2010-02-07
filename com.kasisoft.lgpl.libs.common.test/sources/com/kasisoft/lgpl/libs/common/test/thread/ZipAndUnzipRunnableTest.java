/**
 * Name........: ZipAndUnzipRunnableTest
 * Description.: Test for the classes 'ZipRunnable' and 'UnzipRunnable'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.test.thread;

import com.kasisoft.lgpl.libs.common.thread.*;

import com.kasisoft.lgpl.libs.common.io.*;

import com.kasisoft.lgpl.libs.common.test.framework.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

import java.io.*;

/**
 * Test for the classes 'ZipRunnable' and 'UnzipRunnable'.
 */
@Test(groups="all")
public class ZipAndUnzipRunnableTest {
  
  private File   directory;
  private File   unpackeddir;
  private File   destfile;
  
  @SuppressWarnings("unused")
  @BeforeClass
  private void setup() {
    destfile      = IoFunctions.newTempFile( "file-", ".zip" );
    directory     = IoFunctions.newTempFile( "temp-" );
    directory.mkdirs();
    unpackeddir   = IoFunctions.newTempFile( "temp-" );
    unpackeddir.mkdirs();
    Utilities.createFileSystemStructure( directory );
  }
  
  @Test
  public void zip() throws InterruptedException {
    ZipRunnable runnable = new ZipRunnable( destfile, directory );
    Thread      thread   = new Thread( runnable );
    thread.start();
    thread.join();
    Assert.assertTrue( runnable.hasCompleted() );
  }
  
  @Test(dependsOnMethods="zip")
  public void unzip() throws InterruptedException {
    UnzipRunnable runnable = new UnzipRunnable( destfile, unpackeddir );
    Thread        thread   = new Thread( runnable );
    thread.start();
    thread.join();
    Assert.assertTrue( runnable.hasCompleted() );
    Assert.assertEquals( unpackeddir, directory, "Resources aren't equal !" );
  }
  
  @Test
  public void deleteFiles() throws InterruptedException {
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
