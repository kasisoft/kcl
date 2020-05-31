package com.kasisoft.libs.common.old.model;

import com.kasisoft.libs.common.old.util.MiscFunctions;

import java.util.function.Function;

import java.util.Comparator;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Getter
@EqualsAndHashCode(of = "id")
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScreenInfo implements Comparable<ScreenInfo> {
  
  @FieldDefaults(level = AccessLevel.PRIVATE)
  public enum ComparisonMode implements Comparator<ScreenInfo> {
    
    Ratio      ( ScreenInfo::compareByRatio      , ScreenInfo::getRatioPixels ),
    PixelCount ( ScreenInfo::compareByPixelCount , ScreenInfo::getPixels      ),
    Width      ( ScreenInfo::compareByWidth      , ScreenInfo::getWidth       ),
    Height     ( ScreenInfo::compareByHeight     , ScreenInfo::getHeight      );
    
    Comparator<ScreenInfo>          comparator;
    Function<ScreenInfo, Integer>   getter;
    
    ComparisonMode( Comparator<ScreenInfo> comp, Function<ScreenInfo, Integer> get ) {
      comparator = comp;
      getter     = get;
    }

    public int difference( ScreenInfo s1, ScreenInfo s2 ) {
      return ScreenInfo.difference( s1, s2, getter );
    }
    
    @Override
    public int compare( ScreenInfo s1, ScreenInfo s2 ) {
      return comparator.compare( s1, s2 );
    }
    
  } /* ENDENUM */
  
  String                  id;
  GraphicsConfiguration   graphicsConfiguration;
  GraphicsDevice          screen;
  KSize                   size;
  int                     width;
  int                     height;
  int                     ratioX;
  int                     ratioY;
  int                     pixels;
  int                     ratioPixels;
  
  public ScreenInfo( String idString, GraphicsConfiguration gc, GraphicsDevice scr, int sWidth, int sHeight ) {
    id                    = idString;
    graphicsConfiguration = gc;
    screen                = scr;
    width                 = sWidth;
    height                = sHeight;
    pixels                = width * height;
    int div               = MiscFunctions.gcd( width, height );
    ratioX                = width  / div;
    ratioY                = height / div;
    ratioPixels           = ratioX * ratioY;
    size                  = new KSize( width, height );
  }
  
  public boolean isFullScreenSupported() {
    return screen.isFullScreenSupported();
  }

  @Override
  public int compareTo( ScreenInfo other ) {
    int result = Integer.compare( width,  other.width );
    if( result == 0 ) {
      result = Integer.compare( height, other.height );
    }
    if( result == 0 ) {
      result = id.compareTo( other.id );
    }
    return result;
  }
  
  private static int compareByRatio( ScreenInfo s1, ScreenInfo s2 ) {
    return Integer.compare( s2.ratioPixels, s1.ratioPixels );
  }
  
  private static int compareByPixelCount( ScreenInfo s1, ScreenInfo s2 ) {
    return Integer.compare( s2.pixels, s1.pixels );
  }

  private static int compareByWidth( ScreenInfo s1, ScreenInfo s2 ) {
    return Integer.compare( s2.width, s1.width );
  }

  private static int compareByHeight( ScreenInfo s1, ScreenInfo s2 ) {
    return Integer.compare( s2.height, s1.height );
  }

  private static int difference( ScreenInfo s1, ScreenInfo s2, Function<ScreenInfo, Integer> getter ) {
    int     val1 = getter.apply( s1 );
    int     val2 = getter.apply( s2 );
    double  max  = Math.max( val1, val2 );
    double  min  = Math.min( val1, val2 );
    double  v    = (max - min) / max;
    return (int) (v * 100.0);
  }

} /* ENDCLASS */
