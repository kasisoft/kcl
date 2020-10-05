package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.*;

import org.testng.annotations.*;

import lombok.experimental.*;

import lombok.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnumerationAdapterTest {

  enum LordOfTheRings {
    
    Gandalf ,
    Bilbo   ,
    Boromir ;
    
  }

  private EnumerationAdapter<LordOfTheRings> adapter_ci = new EnumerationAdapter<>(LordOfTheRings.class);
  private EnumerationAdapter<LordOfTheRings> adapter    = new EnumerationAdapter<>(LordOfTheRings.class).withIgnoreCase(false);
  
  @DataProvider(name = "data_decode_ci")
  public Object[][] data_decode_ci() {
    return new Object[][] {
      {null     , null},
      {"gandalf", LordOfTheRings.Gandalf},
      {"bilbo"  , LordOfTheRings.Bilbo},
      {"boromir", LordOfTheRings.Boromir},
    };
  }

  @Test(dataProvider = "data_decode_ci", groups = "all")
  public void decode_ci(String value, LordOfTheRings expected) throws Exception {
    assertThat(adapter_ci.decode(value), is(expected));
  }

  @DataProvider(name = "data_invalidDecode_ci")
  public Object[][] data_invalidDecode_ci() {
    return new Object[][] {
      {"gollum", null},
    };
  }

  @Test(dataProvider = "data_invalidDecode_ci", expectedExceptions = KclException.class, groups = "all")
  public void invalidDecode_ci(String value, LordOfTheRings expected) throws Exception {
    assertThat(adapter_ci.decode(value), is(expected));
  }

  @DataProvider(name = "data_encode_ci")
  public Object[][] data_encode_ci() {
    return new Object[][] {
      {null                  , null},
      {LordOfTheRings.Gandalf, "Gandalf"},
      {LordOfTheRings.Bilbo  , "Bilbo"},
      {LordOfTheRings.Boromir, "Boromir"},
    };
  }

  @Test(dataProvider = "data_encode_ci", groups = "all")
  public void encode_ci(LordOfTheRings value, String expected) throws Exception {
    assertThat(adapter_ci.encode(value), is(expected));
  }
  
  @DataProvider(name = "data_decode")
  public Object[][] data_decode() {
    return new Object[][] {
      {null     , null},
      {"Gandalf", LordOfTheRings.Gandalf},
      {"Bilbo"  , LordOfTheRings.Bilbo},
      {"Boromir", LordOfTheRings.Boromir},
    };
  }

  @Test(dataProvider = "data_decode", groups = "all")
  public void decode(String value, LordOfTheRings expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

  @DataProvider(name = "data_invalidDecode")
  public Object[][] data_invalidDecode() {
    return new Object[][] {
      {"Gollum", null},
    };
  }

  @Test(dataProvider = "data_invalidDecode", expectedExceptions = KclException.class, groups = "all")
  public void invalidDecode(String value, LordOfTheRings expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

  @DataProvider(name = "data_encode")
  public Object[][] data_encode() {
    return new Object[][] {
      {null                  , null},
      {LordOfTheRings.Gandalf, "Gandalf"},
      {LordOfTheRings.Bilbo  , "Bilbo"},
      {LordOfTheRings.Boromir, "Boromir"},
    };
  }

  @Test(dataProvider = "data_encode", groups = "all")
  public void encode(LordOfTheRings value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
} /* ENDCLASS */
