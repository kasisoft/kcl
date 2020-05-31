package com.kasisoft.libs.common.old.xml.adapters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.kasisoft.libs.common.KclException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.function.BiConsumer;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for the type 'EnumerationAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnumerationAdapterTest {

  enum LordOfTheRings {
    
    Gandalf ,
    Bilbo   ,
    Boromir ;
    
  }
  
  private BiConsumer<Object,Exception> errhandler = new BiConsumer<Object,Exception>() {

    @Override
    public void accept( Object source, Exception cause ) {
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
    assertThat( adapter.unmarshal( value ), is( expected ) );
  }

  @Test(dataProvider="createInvalidUnmarshalling", expectedExceptions=KclException.class, groups="all")
  public void invalidUnmarshal( String value, LordOfTheRings expected ) throws Exception {
    assertThat( adapter.unmarshal( value ), is( expected ) );
  }

  @Test(dataProvider="createMarshalling", groups="all")
  public void marshal( LordOfTheRings value, String expected ) throws Exception {
    assertThat( adapter.marshal( value ), is( expected ) );
  }

} /* ENDCLASS */
