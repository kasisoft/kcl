package com.kasisoft.libs.common.functional;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FunctionalInterface
public interface KSupplier<O> {

  O get() throws Exception;
  
} /* ENDINTERFACE */
