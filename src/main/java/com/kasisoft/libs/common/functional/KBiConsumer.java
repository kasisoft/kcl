package com.kasisoft.libs.common.functional;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FunctionalInterface
public interface KBiConsumer<I1, I2> {

  void accept(I1 input1, I2 input2) throws Exception;
  
} /* ENDINTERFACE */
