package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.kasisoft.libs.common.KclException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Date;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@SuppressWarnings("deprecation")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DateAdapterTest {

  DateAdapter adapter = new DateAdapter().withPattern("dd'.'MM'.'yyyy");
  
  @DataProvider(name = "data_decode")
  public Object[][] data_decode() {
    return new Object[][] {
      {null        , null},
      {"12.04.1987", new Date(87, 3, 12)},
      {"03.07.1964", new Date(64, 6, 3)},
    };
  }

  @Test(dataProvider = "data_decode", groups = "all")
  public void decode(String value, Date expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @DataProvider(name = "data_encode")
  public Object[][] data_encode() {
    return new Object[][] {
      {null               , null},
      {new Date(87, 3, 12), "12.04.1987"},
      {new Date(64, 6, 3) , "03.07.1964"},
    };
  }

  @Test(dataProvider = "data_encode", groups = "all")
  public void encode(Date value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
  @Test(groups = "all", expectedExceptions = KclException.class)
  public void decodeFailure() {
    adapter.decode("jfjjfjfjf");
  }
  
} /* ENDCLASS */
