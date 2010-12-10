/**
 * Name........: EnumerationAdapterTest
 * Description.: Tests for the type 'EnumerationAdapter'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.util;

import com.kasisoft.lgpl.libs.common.xml.adapters.*;

import org.testng.annotations.*;

import org.testng.*;

/**
 * Tests for the type 'EnumerationAdapter'.
 */
@Test(groups="all")
public class EnumerationAdapterTest {

  private static enum LordOfTheRings {
    
    Gandalf ,
    Bilbo   ,
    Boromir ;
    
  }
  
  private EnumerationAdapter<LordOfTheRings> adapter = new EnumerationAdapter<LordOfTheRings>( LordOfTheRings.class, true );
  
  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
    return new Object[][] {
      { null      , null                    },
      { "gandalf" , LordOfTheRings.Gandalf  },
      { "bilbo"   , LordOfTheRings.Bilbo    },
      { "boromir" , LordOfTheRings.Boromir  },
      { "gollum"  , null                    },
    };
  }

  @DataProvider(name="createMarshalling")
  public Object[][] createMarshalling() {
    return new Object[][] {
      { null                    , null      },
      { LordOfTheRings.Gandalf  , "Gandalf" },
      { LordOfTheRings.Bilbo    , "Bilbo"   },
      { LordOfTheRings.Boromir  , "Boromir" },
    };
  }

  @Test(dataProvider="createUnmarshalling")
  public void unmarshal( String value, LordOfTheRings expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }
  
  @Test(dataProvider="createMarshalling")
  public void marshal( LordOfTheRings value, String expected ) throws Exception {
    Assert.assertEquals( adapter.marshal( value ), expected );
  }

} /* ENDCLASS */
