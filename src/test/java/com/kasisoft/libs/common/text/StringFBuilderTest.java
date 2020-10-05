package com.kasisoft.libs.common.text;

import org.testng.annotations.*;

/**
 * Testcase for the class 'StringFBuilder'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Test(groups = "all")
public class StringFBuilderTest extends AbstractStringLikeTestCase<StringFBuilder> {

  public StringFBuilderTest() {
    super(StringFBuilder.class);
  }
  
  @Override
  protected StringFBuilder create(String input) {
    return new StringFBuilder(input);
  }

  @Override
  protected StringFBuilder create() {
    return new StringFBuilder();
  }
  
  @Override
  protected StringFBuilder create(int capacity) {
    return new StringFBuilder(capacity);
  }

} /* ENDCLASS */
