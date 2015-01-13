package com.kasisoft.libs.common.constants;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

/**
 * Tests for the constants 'Digest'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class DigestTest {

  @DataProvider(name="createData")
  public Object[][] createData() {
    return new Object[][] {
      { "This is my test phrase", Digest.MD2    },
      { "This is my test phrase", Digest.MD5    },
      { "This is my test phrase", Digest.SHA1   },
      { "This is my test phrase", Digest.SHA256 },
      { "This is my test phrase", Digest.SHA384 },
      { "This is my test phrase", Digest.SHA512 },
    };
  };

  @Test(dataProvider="createData", groups="all")
  public void digest( String text, Digest digest ) {
    byte[] data         = text.getBytes();
    byte[] hash_first   = digest.digest( data ); 
    byte[] hash_second  = digest.digest( data );
    assertThat( hash_first, is( hash_second ) );
  }
  
  @DataProvider(name="valueByNameData")
  public Object[][] valueByNameData() {
    Digest[]    digests = Digest.values();
    Object[][]  result  = new Object[ digests.length + 1 ][];
    for( int i = 0; i < digests.length; i++ ) {
      result[i] = new Object[] { digests[i].getAlgorithm(), digests[i] };
    }
    result[ digests.length ] = new Object[] { "Oopsi", null };
    return result;
  }
  
  @Test(dataProvider="valueByNameData", groups="all")
  public void valueByName( String name, Digest expected ) {
    assertThat( Digest.valueByName( name ), is( expected ) );
  }
  
} /* ENDCLASS */
