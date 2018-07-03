package com.kasisoft.libs.common.model;

import com.kasisoft.libs.common.util.*;

import java.util.*;

import lombok.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Getter
@ToString
public class ScreenInfo {

  public static final Comparator<ScreenInfo> BY_RATIO       = ScreenInfo::compareByRatio;

  public static final Comparator<ScreenInfo> BY_PIXEL_COUNT = ScreenInfo::compareByPixelCount;
  
  public static final Comparator<ScreenInfo> BY_WIDTH       = ScreenInfo::compareByWidth;
  
  public static final Comparator<ScreenInfo> BY_HEIGHT      = ScreenInfo::compareByHeight;
  
  String   id;
  int      width;
  int      height;
  int      ratioX;
  int      ratioY;
  int      pixels;
  
  public ScreenInfo( String idString, int sWidth, int sHeight ) {
    id      = idString;
    width   = sWidth;
    height  = sHeight;
    pixels  = width * height;
    int div = MiscFunctions.gcd( width, height );
    ratioX  = width  / div;
    ratioY  = height / div;
  }

  private static int compareByRatio( ScreenInfo s1, ScreenInfo s2 ) {
    int result = Integer.compare( s1.ratioX, s2.ratioX );
    if( result == 0 ) {
      result = Integer.compare( s1.ratioY, s2.ratioY );
    }
    return result;
  }
  
  private static int compareByPixelCount( ScreenInfo s1, ScreenInfo s2 ) {
    return Integer.compare( s1.pixels, s2.pixels );
  }

  private static int compareByWidth( ScreenInfo s1, ScreenInfo s2 ) {
    return Integer.compare( s1.width, s2.width );
  }

  private static int compareByHeight( ScreenInfo s1, ScreenInfo s2 ) {
    return Integer.compare( s1.height, s2.height );
  }

} /* ENDCLASS */
