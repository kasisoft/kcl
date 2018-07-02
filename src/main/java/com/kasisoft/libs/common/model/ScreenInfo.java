package com.kasisoft.libs.common.model;

import com.kasisoft.libs.common.util.*;

import lombok.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Getter
@ToString
public class ScreenInfo {

  String   id;
  int      width;
  int      height;
  int      ratioX;
  int      ratioY;
  
  public ScreenInfo( String idString, int sWidth, int sHeight ) {
    id      = idString;
    width   = sWidth;
    height  = sHeight;
    int div = MiscFunctions.gcd( width, height );
    ratioX  = width  / div;
    ratioY  = height / div;
  }
  
} /* ENDCLASS */
