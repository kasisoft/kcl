/**
 * Name........: MagicNumber
 * Description.: Collection of magic numbers/keys.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.constants;

import java.util.zip.*;

/**
 * Collection of magic numbers/keys.
 * 
 * @deprecated [16-Jan-2014:KASI]   Use 
 */
@Deprecated
public enum MagicNumber {

  GZIP( 0, 2, new GZIPTest() );
  
  private int          offset;
  private int          required;
  private NumberTest   tester;
  
  MagicNumber( int pos, int size, NumberTest numtester ) {
    offset    = pos;
    required  = size;
    tester    = numtester;
  }
  
  /**
   * Returns <code>true</code> if the supplied data block contains this magic number.
   * 
   * @param data   A data block to be tested. Maybe <code>null</code>.
   * 
   * @return   <code>true</code> <=> The supplied block is supported by this magic number.
   */
  public boolean find( byte[] data ) {
    if( data != null ) {
      if( data.length < (offset + required) ) {
        return false;
      }
      return tester.test( data );
    }
    return false;
  }
  
  /**
   * Identifies the magic number for the supplied data block.
   * 
   * @param data   The data block that is supposed to be tested. Maybe <code>null</code>.
   * 
   * @return   A matching magic number or <code>null</code> if none could be found.
   */
  public static MagicNumber identify( byte[] data ) {
    if( data != null ) {
      for( MagicNumber num : MagicNumber.values() ) {
        if( num.find( data ) ) {
          return num;
        }
      }
    }
    return null;
  }
  
  private static interface NumberTest {
    
    boolean test( byte[] data );
    
  } /* ENDINTERFACE */
  
  private static class GZIPTest implements NumberTest {
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean test( byte[] data ) {
      return ( ((data[1] << 8) | data[0]) & 0x0000FFFF ) == GZIPInputStream.GZIP_MAGIC;
    }
    
  } /* ENDCLASS */
  
} /* ENDENUM */
