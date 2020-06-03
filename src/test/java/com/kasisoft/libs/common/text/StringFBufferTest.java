package com.kasisoft.libs.common.text;

/**
 * Testcase for the class 'StringFBuffer'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
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
