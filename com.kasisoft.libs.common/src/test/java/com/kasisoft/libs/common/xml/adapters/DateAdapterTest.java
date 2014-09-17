package com.kasisoft.libs.common.xml.adapters;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

/**
 * Tests for the type 'DateAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@SuppressWarnings("deprecation")
public class DateAdapterTest {

  private DateAdapter adapter = new DateAdapter( "dd.MM.yyyy", Locale.GERMAN );
  
  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
    return new Object[][] {
      { null    , null          },
      { "12.4.1987" , new Date( 87, 3, 12 ) },
      { "3.07.1964" , new Date( 64, 6, 3  ) },
    };
  }

  @DataProvider(name="createMarshalling")
  public Object[][] createMarshalling() {
    return new Object[][] {
      { null          , null    },
      { new Date( 87, 3, 12 ), "12.04.1987"  },
      { new Date( 64, 6, 3  ), "03.07.1964"  },
    };
  }

  @Test(dataProvider="createUnmarshalling", groups="all")
  public void unmarshal( String value, Date expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }
  
  @Test(dataProvider="createMarshalling", groups="all")
  public void marshal( Date value, String expected ) throws Exception {
    Assert.assertEquals( adapter.marshal( value ), expected );
  }
  
} /* ENDCLASS */
