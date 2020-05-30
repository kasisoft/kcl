package com.kasisoft.libs.common.old.sys;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

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
    assertThat( exitcode1, is(13) );
    
    int exitcode2 = SystemFunctions.executeWithoutExit( new Runnable() {
      @Override
      public void run() {
        System.out.println("No exit here");
      }
    });
    assertThat( exitcode2, is(0) );
    
  }

} /* ENDCLASS */
