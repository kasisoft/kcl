package com.kasisoft.libs.common.util;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 * 
 * @deprecated [21-Jul-2016:KASI]   This class has been moved into the package com.kasisoft.libs.common.text.
 *                                  This replacement will be deleted with version 2.3.
 */
@Deprecated
public class StringFBuffer extends com.kasisoft.libs.common.text.StringFBuffer {

  public StringFBuffer() {
    super();
  }

  public StringFBuffer( CharSequence seq ) {
    super( seq );
  }

  public StringFBuffer( int capacity ) {
    super( capacity );
  }

  public StringFBuffer( String str ) {
    super( str );
  }

} /* ENDCLASS */
