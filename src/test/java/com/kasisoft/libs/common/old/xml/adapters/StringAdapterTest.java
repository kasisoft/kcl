package com.kasisoft.libs.common.old.xml.adapters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for the type 'StringAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StringAdapterTest {

  StringAdapter adapter = new StringAdapter();
  
  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
    return new Object[][] {
      { null    , null  },
      { ""      , ""    },
      { "bla"   , "bla" },
    };
  }

  @DataProvider(name="createMarshalling")
  public Object[][] createMarshalling() {
    return new Object[][] {
        { null    , null  },
        { ""      , ""    },
        { "bla"   , "bla" },
    };
  }

  @Test(dataProvider="createUnmarshalling", groups="all")
  public void unmarshal( String value, String expected ) throws Exception {
    assertThat( adapter.unmarshal( value ), is( expected ) );
  }
  
  @Test(dataProvider="createMarshalling", groups="all")
  public void marshal( String value, String expected ) throws Exception {
    assertThat( adapter.marshal( value ), is( expected ) );
  }

} /* ENDCLASS */
