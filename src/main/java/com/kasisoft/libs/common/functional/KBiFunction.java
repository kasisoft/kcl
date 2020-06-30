package com.kasisoft.libs.common.functional;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FunctionalInterface
public interface KBiFunction<I1, I2, O> {

  O apply(I1 input1, I2 input2) throws Exception;
  
} /* ENDINTERFACE */
