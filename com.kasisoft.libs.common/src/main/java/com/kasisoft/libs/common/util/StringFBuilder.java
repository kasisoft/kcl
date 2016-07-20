package com.kasisoft.libs.common.util;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 * 
 * @deprecated [21-Jul-2016:KASI]   This class has been moved into the package com.kasisoft.libs.common.text.
 *                                  This replacement will be deleted with version 2.3.
 */
@Deprecated
public class StringFBuilder extends com.kasisoft.libs.common.text.StringFBuilder {

  public StringFBuilder() {
    super();
  }

  public StringFBuilder( CharSequence seq ) {
    super( seq );
  }

  public StringFBuilder( int capacity ) {
    super( capacity );
  }

  public StringFBuilder( String str ) {
    super( str );
  }

} /* ENDCLASS */
