package com.kasisoft.libs.common.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.testng.annotations.*;

import java.util.*;

import java.nio.file.*;

/**
 * Testcases for the class 'AbstractCmdLineBuilder'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class AbstractCmdLineBuilderTest {

  private static class Result {
    
    boolean        value;
    String         stringValue;
    Path           pathValue;
    List<String>   stringValues = new ArrayList<>();
    List<Path>     pathValues = new ArrayList<>();
    
  } /* ENDCLASS */
  
  private static class ResultBuilder extends AbstractCmdLineBuilder<ResultBuilder, Result> {

    Result   obj;
    
    public ResultBuilder() {
      obj = new Result();
    }
    
    public ResultBuilder cfgFlag() {
      withFlag( "--value", this::value );
      return this;
    }

    public ResultBuilder cfgString( boolean required ) {
      withParameter( "--string", required, String::valueOf, this::stringValue );
      return this;
    }

    public ResultBuilder cfgPath( boolean required ) {
      withParameter( "--path", required, Paths::get, this::pathValue );
      return this;
    }

    public ResultBuilder cfgStrings( boolean required ) {
      withManyParameter( "--strings", required, String::valueOf, this::stringValues );
      return this;
    }

    public ResultBuilder cfgPaths( boolean required ) {
      withManyParameter( "--paths", required, Paths::get, this::pathValues );
      return this;
    }

    public ResultBuilder value( boolean value ) {
      obj.value = value;
      return this;
    }

    public ResultBuilder stringValue( String value ) {
      obj.stringValue = value;
      return this;
    }

    public ResultBuilder stringValues( String value ) {
      obj.stringValues.add( value );
      return this;
    }

    public ResultBuilder pathValue( Path value ) {
      obj.pathValue = value;
      return this;
    }

    public ResultBuilder pathValues( Path value ) {
      obj.pathValues.add( value );
      return this;
    }

    @Override
    protected Result buildImpl() {
      return obj;
    }
    
  } /* ENDCLASS */
  
  @Test(groups="all")
  public void flag() {

    String[] args1   = new String[] {};
    Result   result1 = new ResultBuilder().cfgFlag().parse( args1 ).build();
    assertNotNull( result1 );
    assertFalse( result1.value );
    
    String[] args2   = new String[] { "--value" };
    Result   result2 = new ResultBuilder().cfgFlag().parse( args2 ).build();
    assertNotNull( result2 );
    assertTrue( result2.value );

    String[] args3   = new String[] { "--value", "--value" };
    Result   result3 = new ResultBuilder().cfgFlag().parse( args3 ).build();
    assertNotNull( result3 );
    assertTrue( result3.value );

  }

  @Test(groups="all")
  public void string() {

    String[] args1   = new String[] {};
    Result   result1 = new ResultBuilder().cfgString( false ).parse( args1 ).build();
    assertNotNull( result1 );
    assertNull( result1.stringValue );
    
    String[] args2   = new String[] { "--string" };
    Result   result2 = new ResultBuilder().cfgString( false ).parse( args2 ).build();
    assertNotNull( result2 );
    assertNull( result2.stringValue );

    String[] args3   = new String[] { "--string", "bibo" };
    Result   result3 = new ResultBuilder().cfgString( false ).parse( args3 ).build();
    assertNotNull( result3 );
    assertThat( result3.stringValue, is( "bibo" ) );

    String[] args4   = new String[] { "--string", "bibo", "--string", "dodo" };
    Result   result4 = new ResultBuilder().cfgString( false ).parse( args4 ).build();
    assertNotNull( result4 );
    assertThat( result4.stringValue, is( "bibo" ) );

    String[] args5   = new String[] {};
    Result   result5 = new ResultBuilder().cfgString( true ).parse( args5 ).build();
    assertNull( result5 );
    
    String[] args6   = new String[] { "--string" };
    Result   result6 = new ResultBuilder().cfgString( true ).parse( args6 ).build();
    assertNull( result6 );

    String[] args7   = new String[] { "--string", "bibo" };
    Result   result7 = new ResultBuilder().cfgString( true ).parse( args7 ).build();
    assertNotNull( result7 );
    assertThat( result7.stringValue, is( "bibo" ) );

    String[] args8   = new String[] { "--string", "bibo", "--string", "dodo" };
    Result   result8 = new ResultBuilder().cfgString( true ).parse( args8 ).build();
    assertNotNull( result8 );
    assertThat( result8.stringValue, is( "bibo" ) );
    
  }

  @Test(groups="all")
  public void path() {

    String[] args1   = new String[] {};
    Result   result1 = new ResultBuilder().cfgPath( false ).parse( args1 ).build();
    assertNotNull( result1 );
    assertNull( result1.pathValue );
    
    String[] args2   = new String[] { "--path" };
    Result   result2 = new ResultBuilder().cfgPath( false ).parse( args2 ).build();
    assertNotNull( result2 );
    assertNull( result2.pathValue );

    String[] args3   = new String[] { "--path", "/tmp/dodo" };
    Result   result3 = new ResultBuilder().cfgPath( false ).parse( args3 ).build();
    assertNotNull( result3 );
    assertThat( result3.pathValue, is( Paths.get( "/tmp/dodo" ) ) );

    String[] args4   = new String[] { "--path", "/tmp/dodo", "--path", "/tmp/bibo" };
    Result   result4 = new ResultBuilder().cfgPath( false ).parse( args4 ).build();
    assertNotNull( result4 );
    assertThat( result4.pathValue, is( Paths.get( "/tmp/dodo" ) ) );

    String[] args5   = new String[] {};
    Result   result5 = new ResultBuilder().cfgPath( true ).parse( args5 ).build();
    assertNull( result5 );
    
    String[] args6   = new String[] { "--path" };
    Result   result6 = new ResultBuilder().cfgPath( true ).parse( args6 ).build();
    assertNull( result6 );

    String[] args7   = new String[] { "--path", "/tmp/dodo" };
    Result   result7 = new ResultBuilder().cfgPath( true ).parse( args7 ).build();
    assertNotNull( result7 );
    assertThat( result7.pathValue, is( Paths.get( "/tmp/dodo" ) ) );

    String[] args8   = new String[] { "--path", "/tmp/dodo", "--path", "/tmp/bibo" };
    Result   result8 = new ResultBuilder().cfgPath( true ).parse( args8 ).build();
    assertNotNull( result8 );
    assertThat( result8.pathValue, is( Paths.get( "/tmp/dodo" ) ) );
    
  }

  @Test(groups="all")
  public void strings() {

    String[] args1   = new String[] {};
    Result   result1 = new ResultBuilder().cfgStrings( false ).parse( args1 ).build();
    assertNotNull( result1 );
    assertNotNull( result1.stringValues );
    assertTrue( result1.stringValues.isEmpty() );
    
    String[] args2   = new String[] { "--strings" };
    Result   result2 = new ResultBuilder().cfgStrings( false ).parse( args2 ).build();
    assertNotNull( result2 );
    assertNotNull( result2.stringValues );
    assertTrue( result1.stringValues.isEmpty() );

    String[] args3   = new String[] { "--strings", "dodo" };
    Result   result3 = new ResultBuilder().cfgStrings( false ).parse( args3 ).build();
    assertNotNull( result3 );
    assertNotNull( result3.stringValues );
    assertThat( result3.stringValues.size(), is(1) );
    assertThat( result3.stringValues.get(0), is( "dodo" ) );

    String[] args4   = new String[] { "--strings", "dodo", "--strings", "bibo" };
    Result   result4 = new ResultBuilder().cfgStrings( false ).parse( args4 ).build();
    assertNotNull( result4 );
    assertNotNull( result4.stringValues );
    assertThat( result4.stringValues.size(), is(2) );
    assertThat( result4.stringValues.get(0), is("dodo" ) );
    assertThat( result4.stringValues.get(1), is("bibo" ) );

    String[] args5   = new String[] {};
    Result   result5 = new ResultBuilder().cfgStrings( true ).parse( args5 ).build();
    assertNull( result5 );
    
    String[] args6   = new String[] { "--strings" };
    Result   result6 = new ResultBuilder().cfgStrings( true ).parse( args6 ).build();
    assertNull( result6 );

    String[] args7   = new String[] { "--strings", "dodo" };
    Result   result7 = new ResultBuilder().cfgStrings( true ).parse( args7 ).build();
    assertNotNull( result7 );
    assertNotNull( result7.stringValues );
    assertThat( result7.stringValues.size(), is(1) );
    assertThat( result7.stringValues.get(0), is( "dodo" ) );

    String[] args8   = new String[] { "--strings", "dodo", "--strings", "bibo" };
    Result   result8 = new ResultBuilder().cfgStrings( true ).parse( args8 ).build();
    assertNotNull( result8 );
    assertNotNull( result8.stringValues );
    assertThat( result8.stringValues.size(), is(2) );
    assertThat( result8.stringValues.get(0), is( "dodo" ) );
    assertThat( result8.stringValues.get(1), is( "bibo" ) );
    
  }

  @Test(groups="all")
  public void paths() {

    String[] args1   = new String[] {};
    Result   result1 = new ResultBuilder().cfgPaths( false ).parse( args1 ).build();
    assertNotNull( result1 );
    assertNotNull( result1.pathValues );
    assertTrue( result1.pathValues.isEmpty() );
    
    String[] args2   = new String[] { "--paths" };
    Result   result2 = new ResultBuilder().cfgPaths( false ).parse( args2 ).build();
    assertNotNull( result2 );
    assertNotNull( result2.pathValues );
    assertTrue( result1.pathValues.isEmpty() );

    String[] args3   = new String[] { "--paths", "/tmp/dodo" };
    Result   result3 = new ResultBuilder().cfgPaths( false ).parse( args3 ).build();
    assertNotNull( result3 );
    assertNotNull( result3.pathValues );
    assertThat( result3.pathValues.size(), is(1) );
    assertThat( result3.pathValues.get(0), is( Paths.get( "/tmp/dodo" ) ) );

    String[] args4   = new String[] { "--paths", "/tmp/dodo", "--paths", "/tmp/bibo" };
    Result   result4 = new ResultBuilder().cfgPaths( false ).parse( args4 ).build();
    assertNotNull( result4 );
    assertNotNull( result4.pathValues );
    assertThat( result4.pathValues.size(), is(2) );
    assertThat( result4.pathValues.get(0), is( Paths.get( "/tmp/dodo" ) ) );
    assertThat( result4.pathValues.get(1), is( Paths.get( "/tmp/bibo" ) ) );

    String[] args5   = new String[] {};
    Result   result5 = new ResultBuilder().cfgPaths( true ).parse( args5 ).build();
    assertNull( result5 );
    
    String[] args6   = new String[] { "--paths" };
    Result   result6 = new ResultBuilder().cfgPaths( true ).parse( args6 ).build();
    assertNull( result6 );

    String[] args7   = new String[] { "--paths", "/tmp/dodo" };
    Result   result7 = new ResultBuilder().cfgPaths( true ).parse( args7 ).build();
    assertNotNull( result7 );
    assertNotNull( result7.pathValues );
    assertThat( result7.pathValues.size(), is(1) );
    assertThat( result7.pathValues.get(0), is( Paths.get( "/tmp/dodo" ) ) );

    String[] args8   = new String[] { "--paths", "/tmp/dodo", "--paths", "/tmp/bibo" };
    Result   result8 = new ResultBuilder().cfgPaths( true ).parse( args8 ).build();
    assertNotNull( result8 );
    assertNotNull( result8.pathValues );
    assertThat( result8.pathValues.size(), is(2) );
    assertThat( result8.pathValues.get(0), is( Paths.get( "/tmp/dodo" ) ) );
    assertThat( result8.pathValues.get(1), is( Paths.get( "/tmp/bibo" ) ) );
    
  }

} /* ENDCLASS */
