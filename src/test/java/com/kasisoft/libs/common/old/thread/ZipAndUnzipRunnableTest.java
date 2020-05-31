package com.kasisoft.libs.common.old.thread;

import static org.testng.Assert.assertTrue;

import com.kasisoft.libs.common.old.io.IoFunctions;
import com.kasisoft.libs.common.old.test.framework.AssertExtension;
import com.kasisoft.libs.common.old.test.framework.Utilities;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.testng.Assert;

import java.util.Collections;
import java.util.List;

import java.io.File;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Test for the classes 'ZipRunnable' and 'UnzipRunnable'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ZipAndUnzipRunnableTest {
  
  File   directory;
  File   unpackeddir;
  File   destfile;
  
  @BeforeClass
  private void setup() {
    destfile      = IoFunctions.newTempFile( "file-", ".zip" );
    directory     = IoFunctions.newTempFile( "temp-" );
    directory.mkdirs();
    unpackeddir   = IoFunctions.newTempFile( "temp-" );
    unpackeddir.mkdirs();
    Utilities.createFileSystemStructure( directory );
  }
  
  @Test(groups="all")
  public void zip() throws InterruptedException {
    ZipRunnable runnable = new ZipRunnable( destfile, directory );
    Thread      thread   = new Thread( runnable );
    thread.start();
    thread.join();
    assertTrue( runnable.hasCompleted() );
  }
  
  @Test(dependsOnMethods="zip",groups="all")
  public void unzip() throws InterruptedException {
    UnzipRunnable runnable = new UnzipRunnable( destfile, unpackeddir );
    Thread        thread   = new Thread( runnable );
    thread.start();
    thread.join();
    assertTrue( runnable.hasCompleted() );
    AssertExtension.assertEquals( unpackeddir, directory );
  }
  
  @Test(groups="all")
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
