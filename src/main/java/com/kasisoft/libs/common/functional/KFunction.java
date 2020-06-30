package com.kasisoft.libs.common.functional;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FunctionalInterface
public interface KFunction<I, O> {

  O apply(I input) throws Exception;
  
} /* ENDINTERFACE */
