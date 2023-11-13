package com.kasisoft.libs.common.test.types;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.*;

import com.kasisoft.libs.common.types.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class GeoLocationTest {

  @Test
  public void simple() {
    var location = new GeoLocation(52.1, 10.2);
    assertThat(location.getLatitude(), is(52.1));
    assertThat(location.getLongitude(), is(10.2));
    assertTrue(location.isValid());
  }

  @Test
  public void invalid() {
    var location1 = new GeoLocation(Double.NaN, 10.2);
    var location2 = new GeoLocation(52.1, Double.NaN);
    var location3 = new GeoLocation(Double.NaN, Double.NaN);
    assertFalse(location1.isValid());
    assertFalse(location2.isValid());
    assertFalse(location3.isValid());
  }

} /* ENDCLASS */
