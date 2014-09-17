package com.kasisoft.libs.common.sys;

import org.testng.annotations.*;

import org.testng.*;

/**
 * Test for the class 'SystemFunctions'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class SystemFunctionsTest {

  
  @Test(groups="all")
  public void executeWithoutExit() {

    int exitcode1 = SystemFunctions.executeWithoutExit( new Runnable() {
      @Override
      public void run() {
        System.exit(13);
      }
    });
    Assert.assertEquals( exitcode1, 13 );
    
    int exitcode2 = SystemFunctions.executeWithoutExit( new Runnable() {
      @Override
      public void run() {
        System.out.println("No exit here");
      }
    });
    Assert.assertEquals( exitcode2, 0 );
    
  }

} /* ENDCLASS */
