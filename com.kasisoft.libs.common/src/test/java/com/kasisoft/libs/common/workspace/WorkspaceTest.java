package com.kasisoft.libs.common.workspace;

import org.testng.annotations.*;

import org.testng.*;

import java.awt.*;

import java.io.*;

/**
 * Tests for the class 'Workspace'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class WorkspaceTest {

  @DataProvider(name="workspaces")
  public Object[][] workspaces() {
    return new Object[][] {
      { Workspace.getInstance() },
      { Workspace.getInstance( new File( "local.workspace" ) ) },
    };
  }
  
  @Test(dataProvider="workspaces", groups="all")
  public void getAndSetString( Workspace workspace ) {
    workspace.setString( "bibo", "frog" );
    Assert.assertEquals( workspace.getString( "bibo", null ), "frog" );
  }

  @Test(dataProvider="workspaces", groups="all")
  public void getAndSetRectangle( Workspace workspace ) {
    workspace.setRectangle( "component", new Rectangle( 40, 40, 300, 120 ) );
    Assert.assertEquals( workspace.getRectangle( "component", null ), new Rectangle( 40, 40, 300, 120 ) );
  }

} /* ENDCLASS */
