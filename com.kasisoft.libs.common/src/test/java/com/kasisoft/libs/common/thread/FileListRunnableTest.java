package com.kasisoft.libs.common.thread;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import org.testng.annotations.*;

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.test.framework.*;

import java.util.*;

import java.io.*;

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
