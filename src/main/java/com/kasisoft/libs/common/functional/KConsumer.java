package com.kasisoft.libs.common.functional;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FunctionalInterface
public interface KConsumer<I> {

  void accept(I input) throws Exception;
  
} /* ENDINTERFACE */
