package com.kasisoft.libs.common.i18n;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.*;

import java.util.*;

/**
 * Tests for the i18n class 'I18NSupport'.
 * 
 * Requires the default locale to be Locale#GERMANY.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class I18NSupportTest {

  @Test
  public void correctMessages1() {
    assertThat(Messages1.m0, is("Default.0"    ));
    assertThat(Messages1.m1, is("Translation.1"));
    assertThat(Messages1.m2, is("Translation.2"));
    assertThat(Messages1.m3, is("Translation.3"));
    assertThat(Messages1.m4, is("wrong"        ));
  }

  @Test
  public void correctMessages2() {
    assertThat(Messages2.m0, is("Default.0"    ));
    assertThat(Messages2.m1, is("Translation.1"));
    assertThat(Messages2.m2, is("Translation.2"));
    assertThat(Messages2.m3, is("Translation.3"));
    assertThat(Messages2.m4, is("wrong"        ));
  }

  @Test
  public void correctMessages3() {
    assertThat(Messages3.m0, is("Default.0"    ));
    assertThat(Messages3.m1, is("Translation.1"));
    assertThat(Messages3.m2, is("Translation.2"));
    assertThat(Messages3.m3, is("Translation.3"));
    assertThat(Messages3.m4, is("wrong"        ));
  }

  @Test
  public void correctMessages4() {
    assertThat(Messages4.m0, is("Default.0"));
    assertThat(Messages4.m1, is("Translation.1"));
    assertThat(Messages4.m3, is("Translation.3"));
    assertThat(Messages4.m4, is("wrong"));
    assertThat(Messages4.m2.toString()    , is("Translation.%s.2"  ));
    assertThat(Messages4.m2.format("test"), is("Translation.test.2"));
  }

  @Test
  public void correctMessages5() {
    
    assertThat(Messages5.m0, is("Default.0"));
    assertThat(Messages5.m1, is("Übersetzung.1"));
    assertThat(Messages5.m2, is("Übersetzung.%s.2"));
    assertThat(Messages5.m3, is("Translation.3"));
    assertThat(Messages5.m4, is("falsch"));
    assertThat(Messages5.m5.format("Huppi"), is("Der Text war 'Huppi'"));
    assertThat(Messages5.m6, is("Gollum"));
    
    // change the language
    I18NSupport.initialize(Locale.US, Messages5.class);

    assertThat(Messages5.m0, is("Default.0"));
    assertThat(Messages5.m1, is("Translation.1"));
    assertThat(Messages5.m2, is("Translation.%s.2"));
    assertThat(Messages5.m3, is("Translation.3"));
    assertThat(Messages5.m4, is("wrong"));
    assertThat(Messages5.m5.format("Huppi"), is("The us text was 'Huppi'"));
    assertThat(Messages5.m6, is("Gollum"));

  }

} /* ENDCLASS */
