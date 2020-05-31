package com.kasisoft.libs.common.old.thread;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import com.kasisoft.libs.common.old.io.IoFunctions;
import com.kasisoft.libs.common.old.test.framework.Utilities;

import org.testng.annotations.Test;

import java.io.File;

/**
 * Test for the class 'FileDeleteRunnable'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class FileDeleteRunnableTest {
  
  @Test(groups="all")
  public void deleteFiles() throws InterruptedException {
    File                tempdir   = IoFunctions.newTempFile( "temp-", null );
    tempdir.mkdirs();
    Utilities.createFileSystemStructure( tempdir );
    File[]              files     = tempdir.listFiles();
    assertNotNull( files );
    FileDeleteRunnable  runnable  = new FileDeleteRunnable( files );
    Thread              thread    = new Thread( runnable );
    thread.start();
    thread.join();
    assertTrue( runnable.hasCompleted() );
    File[] children = tempdir.listFiles();
    assertThat( children, is( notNullValue() ) );
    assertThat( children.length, is(0) );
  }
  
} /* ENDCLASS */
