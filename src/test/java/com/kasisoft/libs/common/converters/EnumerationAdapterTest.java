package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.kasisoft.libs.common.KclException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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

  private EnumerationAdapter<LordOfTheRings> adapter = new EnumerationAdapter<>(LordOfTheRings.class);
  
  @DataProvider(name = "dataDecode")
  public Object[][] dataDecode() {
    return new Object[][] {
      {null     , null},
      {"gandalf", LordOfTheRings.Gandalf},
      {"bilbo"  , LordOfTheRings.Bilbo},
      {"boromir", LordOfTheRings.Boromir},
    };
  }

  @DataProvider(name = "dataInvalidDecode")
  public Object[][] dataInvalidDecode() {
    return new Object[][] {
      {"gollum", null},
    };
  }

  @DataProvider(name = "dataEncode")
  public Object[][] dataEncode() {
    return new Object[][] {
      {null                  , null},
      {LordOfTheRings.Gandalf, "Gandalf"},
      {LordOfTheRings.Bilbo  , "Bilbo"},
      {LordOfTheRings.Boromir, "Boromir"},
    };
  }

  @Test(dataProvider = "dataDecode", groups = "all")
  public void decode(String value, LordOfTheRings expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

  @Test(dataProvider = "dataInvalidDecode", expectedExceptions = KclException.class, groups = "all")
  public void invalidDecode(String value, LordOfTheRings expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

  @Test(dataProvider = "dataEncode", groups = "all")
  public void encode(LordOfTheRings value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }

} /* ENDCLASS */
