package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.util.*;

import org.testng.annotations.*;

import org.testng.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Tests for the type 'EnumerationAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnumerationAdapterTest {

  static enum LordOfTheRings {
    
    Gandalf ,
    Bilbo   ,
    Boromir ;
    
  }
  
  private SimpleErrorHandler errhandler = new SimpleErrorHandler() {

    @Override
    public void failure( Object source, String message, Exception cause ) {
      if( cause instanceof RuntimeException ) {
        throw ((RuntimeException) cause);
      } else {
        throw new RuntimeException( cause );
      }
    }
    
  };

  private EnumerationAdapter<LordOfTheRings> adapter = new EnumerationAdapter<>( errhandler, null, null, LordOfTheRings.class, true );
  
  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
    return new Object[][] {
      { null      , null                    },
      { "gandalf" , LordOfTheRings.Gandalf  },
      { "bilbo"   , LordOfTheRings.Bilbo    },
      { "boromir" , LordOfTheRings.Boromir  },
    };
  }

  @DataProvider(name="createInvalidUnmarshalling")
  public Object[][] createInvalidUnmarshalling() {
    return new Object[][] {
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

  @Test(dataProvider="createUnmarshalling", groups="all")
  public void unmarshal( String value, LordOfTheRings expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }

  @Test(dataProvider="createInvalidUnmarshalling", expectedExceptions=FailureException.class, groups="all")
  public void invalidUnmarshal( String value, LordOfTheRings expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }

  @Test(dataProvider="createMarshalling", groups="all")
  public void marshal( LordOfTheRings value, String expected ) throws Exception {
    Assert.assertEquals( adapter.marshal( value ), expected );
  }

} /* ENDCLASS */
