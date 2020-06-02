package com.kasisoft.libs.common.old.xml.adapters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.kasisoft.libs.common.KclException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.function.BiConsumer;

import java.awt.Insets;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for the type 'InsetsAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InsetsAdapterTest {

  BiConsumer<Object,Exception> errhandler = new BiConsumer<Object,Exception>() {

    @Override
    public void accept( Object source, Exception cause ) {
      if( cause instanceof RuntimeException ) {
        throw ((RuntimeException) cause);
      } else {
        throw new RuntimeException( cause );
      }
    }
    
  };

  InsetsAdapter adapter = new InsetsAdapter( errhandler, null, null, ":" );

  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
    return new Object[][] {
      { null            , null                                 },
      { "-13:42:10:20"  , new Insets( -13  , 42 ,  10 ,  20 )  },
      { "0:0:50:28"     , new Insets(   0  ,  0 ,  50 ,  28 )  },
      { "0:0:-50:-28"   , new Insets(   0  ,  0 , -50 , -28 )  },
    };
  }

  @DataProvider(name="createInvalidUnmarshalling")
  public Object[][] createInvalidUnmarshalling() {
    return new Object[][] {
      { "12:-13:42" , null },
      { "0:"        , null },
      { "a:b"       , null },
    };
  }

  @DataProvider(name="createMarshalling")
  public Object[][] createMarshalling() {
    return new Object[][] {
      { null                               , null            },
      { new Insets( -13  , 42 , 30 , 70 )  , "-13:42:30:70"  },
      { new Insets(   0  ,  0 , 12 , 29 )  , "0:0:12:29"     },
    };
  }

  @Test(dataProvider="createUnmarshalling", groups="all")
  public void unmarshal( String value, Insets expected ) throws Exception {
    assertThat( adapter.unmarshal( value ), is( expected ) );
  }

  @Test(dataProvider="createInvalidUnmarshalling", expectedExceptions={KclException.class,NumberFormatException.class}, groups="all")
  public void invalidUnmarshal( String value, Insets expected ) throws Exception {
    assertThat( adapter.unmarshal( value ), is( expected ) );
  }

  @Test(dataProvider="createMarshalling", groups="all")
  public void marshal( Insets value, String expected ) throws Exception {
    assertThat( adapter.marshal( value ), is( expected ) );
  }
  
} /* ENDCLASS */