package com.kasisoft.libs.common.old.thread;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertTrue;

import com.kasisoft.libs.common.old.io.IoFunctions;
import com.kasisoft.libs.common.old.test.framework.Utilities;

import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import java.io.File;

/**
 * Test for the class 'FileListRunnable'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class FileListRunnableTest {
  
  @Test(groups="all")
  public void listFiles() throws InterruptedException {
    File                tempdir   = IoFunctions.newTempFile( "temp-", null );
    tempdir.mkdirs();
    List<File>          created   = Utilities.createFileSystemStructure( tempdir );
    Collections.sort( created );
    FileListRunnable    runnable  = new FileListRunnable( tempdir.listFiles() );
    Thread              thread    = new Thread( runnable );
    thread.start();
    thread.join();
    assertTrue( runnable.hasCompleted() );
    List<File> files = runnable.getAllFiles();
    assertThat( files.size(), is( created.size() ) );
    Collections.sort( files );
    assertThat( files, is( created ) );
  }
  
} /* ENDCLASS */
