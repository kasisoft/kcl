package com.kasisoft.libs.common.old.constants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
  
  @DataProvider(name="createDataToString")
  public Object[][] createDataToString() {
    return new Object[][] {
      { "This is my test phrase", Digest.MD2    , "5a6ce9fb168eb92bb912be6c90102572" },
      { "This is my test phrase", Digest.MD5    , "76a2d4204bcd9ea5aac40ebf833f7345" },
      { "This is my test phrase", Digest.SHA1   , "bc73f7c09da0c9791cb38e93a4d3bf22e53a467d" },
      { "This is my test phrase", Digest.SHA256 , "6d73554fcfa28065a41eb49038049f848b91efc6af26ab0035e709bb4bf63cc7" },
      { "This is my test phrase", Digest.SHA384 , "5811a79674c3a6eb1a6243a27f9cd06244ad5b3096a5554f902652c686c89092f8a7a8891b93c09f7f2743be1b0dc83f" },
      { "This is my test phrase", Digest.SHA512 , "f5aa01867cd292624701b1bd56746569049314d2e940a12b1f43d98b73c97f28972856eb8832f2ac6f18f694bf252056271ad625c187d628185525a2744de4eb" },
    };
  };
  
  @Test(dataProvider="createDataToString", groups="all")
  public void digestToString( String text, Digest digest, String expectedVal ) {
    assertThat( digest.digestToString( text.getBytes() ), is( expectedVal ) );
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