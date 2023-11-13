package com.kasisoft.libs.common.test.i18n;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.i18n.*;

import org.junit.jupiter.api.*;

/**
 * Tests for the i18n class 'I18NSupport'.
 *
 * Requires the default locale to be Locale#GERMANY.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class I18NStringTest {

  I18NString   formatterWithArgs    = new I18NString("Hello '%s' %s");
  I18NString   formatterWithoutArgs = new I18NString("Constant string");

  @Test
  public void format() {

    Object[] args = null;
    assertThat(formatterWithArgs    . format(args            ), is("Hello '%s' %s"  ) );
    assertThat(formatterWithArgs    . format(Empty.NO_OBJECTS), is("Hello '%s' %s"  ) );
    assertThat(formatterWithoutArgs . format(args            ), is("Constant string") );
    assertThat(formatterWithoutArgs . format(Empty.NO_OBJECTS), is("Constant string") );

    assertThat(formatterWithArgs    . format("World"         ), is("Hello '%s' %s"  ) );
    assertThat(formatterWithoutArgs . format("World"         ), is("Constant string") );

  }

} /* ENDCLASS */
