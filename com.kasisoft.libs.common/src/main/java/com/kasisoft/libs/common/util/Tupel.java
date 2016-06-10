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
public class Tupel<T> extends com.kasisoft.libs.common.model.Tupel<T> {

  /**
   * Changes the current values.
   * 
   * @param newvalues   The new values. Maybe <code>null</code>.
   */
  public Tupel( T ... newvalues ) {
    super( newvalues );
  }

} /* ENDCLASS */
