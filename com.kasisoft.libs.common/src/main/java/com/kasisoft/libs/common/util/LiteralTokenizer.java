package com.kasisoft.libs.common.util;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 * 
 * @deprecated [21-Jul-2016:KASI]   This class has been moved into the package com.kasisoft.libs.common.text.
 *                                  This replacement will be deleted with version 2.3.
 */
@Deprecated
public class LiteralTokenizer extends com.kasisoft.libs.common.text.LiteralTokenizer {

  public LiteralTokenizer( String data, boolean returnliterals, String... delimiters ) {
    super( data, returnliterals, delimiters );
  }

  public LiteralTokenizer( String data, String... delimiters ) {
    super( data, delimiters );
  }

} /* ENDCLASS */
