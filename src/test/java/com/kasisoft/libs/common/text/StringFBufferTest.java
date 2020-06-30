package com.kasisoft.libs.common.text;

import org.testng.annotations.Test;

/**
 * Testcase for the class 'StringFBuffer'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Test(groups = "all")
public class StringFBufferTest extends AbstractStringLikeTestCase<StringFBuffer> {

  public StringFBufferTest() {
    super(StringFBuffer.class);
  }

  @Override
  protected StringFBuffer create(String input) {
    return new StringFBuffer(input);
  }

  @Override
  protected StringFBuffer create() {
    return new StringFBuffer();
  }
  
  @Override
  protected StringFBuffer create(int capacity) {
    return new StringFBuffer(capacity);
  }

} /* ENDCLASS */
