package com.kasisoft.libs.common.util;

/**
 * Simple class used to work as a container (f.e. out-parameters).
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 * 
 * @deprecated [10-Jun-2016:KASI]   This type had been moved into the package com.kasisoft.libs.common.model
 *                                  and will be removed with version 2.3.
 */
@Deprecated
public class Pair<T1, T2> extends com.kasisoft.libs.common.model.Pair<T1, T2> {

  public Pair() {
    super();
  }

  public Pair( T1 value1, T2 value2 ) {
    super( value1, value2 );
  }

} /* ENDCLASS */
