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
public class Triple<T1, T2, T3> extends com.kasisoft.libs.common.model.Triple<T1, T2, T3> {

  public Triple() {
    super();
  }

  public Triple( T1 value1, T2 value2, T3 value3 ) {
    super( value1, value2, value3 );
  }

  
} /* ENDCLASS */
