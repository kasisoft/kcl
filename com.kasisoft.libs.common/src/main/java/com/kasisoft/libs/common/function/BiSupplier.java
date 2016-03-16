package com.kasisoft.libs.common.function;

import com.kasisoft.libs.common.util.*;

@FunctionalInterface
public interface BiSupplier<T1,T2> {

  Pair<T1,T2> get();
  
}
