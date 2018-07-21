package com.kasisoft.libs.common.constants;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

/**
 * Test for the constants 'Iso639'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Iso639Test {

  @DataProvider(name="createValues")
  public Object[][] createValues() {
    Iso639[]   values = Iso639.values();
    Object[][] result = new Object[ values.length ][];
    for( int i = 0; i < values.length; i++ ) {
      result[i] = new Object[] { values[i] };
    }
    return result;
  }

  @Test(dataProvider="createValues",groups="all")
  public void validCode( Iso639 value ) {
    assertThat( value.getBibliography (), is( notNullValue() ) );
    assertThat( value.getTerminology  (), is( notNullValue() ) );
    assertThat( value.getBibliography ().length(), is(3) );
    assertThat( value.getTerminology  ().length(), is(3) );
    if( value.getAlpha2() != null ) {
      assertThat( value.getAlpha2().length(), is(2) );
    }
  }
  
  @DataProvider(name="createAlpha2")
  public Object[][] createAlpha2() {
    Iso639[]   values = Iso639.values();
    int        count  = 0;
    for( Iso639 value : values ) {
      if( value.getAlpha2() != null ) {
        count++;
      }
    }
    Object[][] result = new Object[ count ][];
    int        j      = 0;
    for( Iso639 value : values ) {
      if( value.getAlpha2() != null ) {
        result[j++] = new Object[] { value, value.getAlpha2() };
      }
    }
    return result;
  }

  @Test(dataProvider="createAlpha2",groups="all")
  public void byAlpha2( Iso639 expected, String alpha2 ) {
    Iso639 identified = Iso639.valueByAlpha2( alpha2 );
    assertThat( identified, is( notNullValue() ) );
    assertThat( identified, is( expected ) );
  }

  @DataProvider(name="createBibliography")
  public Object[][] createBibliography() {
    Iso639[]   values = Iso639.values();
    Object[][] result = new Object[ values.length ][];
    for( int i = 0; i < values.length; i++ ) {
      result[i] = new Object[] { values[i], values[i].getBibliography() };
    }
    return result;
  }
  
  @Test(dataProvider="createBibliography",groups="all")
  public void byBibliography( Iso639 expected, String bibliography ) {
    Iso639 identified = Iso639.valueByBibliography( bibliography );
    assertThat( identified, is( notNullValue() ) );
    assertThat( identified, is( expected ) );
  }
  
  @DataProvider(name="createTerminology")
  public Object[][] createTerminology() {
    Iso639[]   values = Iso639.values();
    Object[][] result = new Object[ values.length ][];
    for( int i = 0; i < values.length; i++ ) {
      result[i] = new Object[] { values[i], values[i].getTerminology() };
    }
    return result;
  }
  
  @Test(dataProvider="createTerminology",groups="all")
  public void byTerminology( Iso639 expected, String terminology ) {
    Iso639 identified = Iso639.valueByTerminology( terminology );
    assertThat( identified, is( notNullValue() ) );
    assertThat( identified, is( expected ) );
  }

} /* ENDCLASS */
