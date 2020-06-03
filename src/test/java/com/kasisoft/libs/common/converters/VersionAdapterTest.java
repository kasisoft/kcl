package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.kasisoft.libs.common.types.Version;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for the type 'VersionAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VersionAdapterTest {

  VersionAdapter adapter = new VersionAdapter().withMicro(true).withQualifier(true);
  
  @DataProvider(name = "dataDecode")
  public Object[][] createUnmarshalling() {
    return new Object[][] {
      { null              , null                                         },
      { "1.1.1.qualifier" , new Version( "1.1.1.qualifier", true, true ) },
    };
  }

  @DataProvider(name = "dataEncode")
  public Object[][] createMarshalling() {
    return new Object[][] {
      { null                                                 , null               },
      { new Version( 1, 1, Integer.valueOf(1), "qualifier" ) , "1.1.1.qualifier"  },
    };
  }

  @Test(dataProvider = "dataDecode", groups = "all")
  public void decode( String value, Version expected ) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @Test(dataProvider = "dataEncode", groups = "all")
  public void encode(Version value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
} /* ENDCLASS */
