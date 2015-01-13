package com.kasisoft.libs.common.xml.adapters;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Tests for the type 'BooleanAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BooleanAdapterTest {

  BooleanAdapter adapter = new BooleanAdapter();
  
  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
    return new Object[][] {
      { null    , null          },
      { "true"  , Boolean.TRUE  },
      { "false" , Boolean.FALSE },
      { "ja"    , Boolean.TRUE  },
      { "nein"  , Boolean.FALSE },
      { "an"    , Boolean.TRUE  },
      { "ein"   , Boolean.TRUE  },
      { "aus"   , Boolean.FALSE },
      { "on"    , Boolean.TRUE  },
      { "off"   , Boolean.FALSE },
      { "0"     , Boolean.FALSE },
      { "1"     , Boolean.TRUE  },
      { "-1"    , Boolean.TRUE  },
      { ""      , Boolean.FALSE },
    };
  }

  @DataProvider(name="createMarshalling")
  public Object[][] createMarshalling() {
    return new Object[][] {
      { null          , null    },
      { Boolean.TRUE  , "true"  },
      { Boolean.FALSE , "false" }, 
    };
  }

  @Test(dataProvider="createUnmarshalling", groups="all")
  public void unmarshal( String value, Boolean expected ) throws Exception {
    assertThat( adapter.unmarshal( value ), is( expected ) );
  }
  
  @Test(dataProvider="createMarshalling", groups="all")
  public void marshal( Boolean value, String expected ) throws Exception {
    assertThat( adapter.marshal( value ), is( expected ) );
  }
  
} /* ENDCLASS */
