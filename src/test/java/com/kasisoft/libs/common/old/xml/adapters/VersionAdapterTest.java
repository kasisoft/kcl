package com.kasisoft.libs.common.old.xml.adapters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.kasisoft.libs.common.types.Version;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.text.ParseException;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for the type 'VersionAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VersionAdapterTest {

  VersionAdapter adapter = new VersionAdapter( true, true );
  
  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
    try {
      return new Object[][] {
        { null              , null                                         },
        { "1.1.1.qualifier" , new Version( "1.1.1.qualifier", true, true ) },
      };
    } catch( ParseException ex ) {
      throw new RuntimeException(ex);
    }
  }

  @DataProvider(name="createMarshalling")
  public Object[][] createMarshalling() {
    return new Object[][] {
      { null                                                 , null               },
      { new Version( 1, 1, Integer.valueOf(1), "qualifier" ) , "1.1.1.qualifier"  },
    };
  }

  @Test(dataProvider="createUnmarshalling", groups="all")
  public void unmarshal( String value, Version expected ) throws Exception {
    assertThat( adapter.unmarshal( value ), is( expected ) );
  }
  
  @Test(dataProvider="createMarshalling", groups="all")
  public void marshal( Version value, String expected ) throws Exception {
    assertThat( adapter.marshal( value ), is( expected ) );
  }
  
} /* ENDCLASS */
