package com.kasisoft.libs.common.types;

import com.kasisoft.libs.common.utils.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

import java.util.*;

import java.awt.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ScreenInfo implements Comparable<ScreenInfo> {

  public enum ComparisonMode implements Comparator<ScreenInfo> {

    Ratio      (ScreenInfo::compareByRatio     , ScreenInfo::getRatioPixels),
    PixelCount (ScreenInfo::compareByPixelCount, ScreenInfo::getPixels),
    Width      (ScreenInfo::compareByWidth     , ScreenInfo::getWidth),
    Height     (ScreenInfo::compareByHeight    , ScreenInfo::getHeight);

    private Comparator<ScreenInfo>          comparator;
    private Function<ScreenInfo, Integer>   getter;

    ComparisonMode(Comparator<ScreenInfo> comp, Function<ScreenInfo, Integer> get) {
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

  private String                  id;
  private GraphicsConfiguration   graphicsConfiguration;
  private GraphicsDevice          screen;
  private KSize                   size;
  private int                     width;
  private int                     height;
  private int                     ratioX;
  private int                     ratioY;
  private int                     pixels;
  private int                     ratioPixels;

  public ScreenInfo(String idString, GraphicsConfiguration gc, GraphicsDevice scr, int sWidth, int sHeight) {
    id                    = idString;
    graphicsConfiguration = gc;
    screen                = scr;
    width                 = sWidth;
    height                = sHeight;
    pixels                = width * height;
    int div               = MiscFunctions.gcd(width, height);
    ratioX                = width  / div;
    ratioY                = height / div;
    ratioPixels           = ratioX * ratioY;
    size                  = new KSize(width, height);
  }

  public boolean isFullScreenSupported() {
    return screen.isFullScreenSupported();
  }

  @Override
  public int compareTo(@NotNull ScreenInfo other) {
    var result = Integer.compare(width, other.width);
    if (result == 0) {
      result = Integer.compare(height, other.height);
    }
    if (result == 0) {
      result = id.compareTo(other.id);
    }
    return result;
  }

  private static int compareByRatio(@NotNull ScreenInfo s1, @NotNull ScreenInfo s2) {
    return Integer.compare(s2.ratioPixels, s1.ratioPixels);
  }

  private static int compareByPixelCount(@NotNull ScreenInfo s1, @NotNull ScreenInfo s2) {
    return Integer.compare(s2.pixels, s1.pixels);
  }

  private static int compareByWidth(@NotNull ScreenInfo s1, @NotNull ScreenInfo s2) {
    return Integer.compare(s2.width, s1.width);
  }

  private static int compareByHeight(@NotNull ScreenInfo s1, @NotNull ScreenInfo s2) {
    return Integer.compare(s2.height, s1.height);
  }

  private static int difference(@NotNull ScreenInfo s1, @NotNull ScreenInfo s2, @NotNull Function<ScreenInfo, Integer> getter) {
    var val1 = getter.apply(s1);
    var val2 = getter.apply(s2);
    var max  = Math.max(val1, val2);
    var min  = Math.min(val1, val2);
    var v    = (max - min) / max;
    return (int) (v * 100.0);
  }

  public String getId() {
    return id;
  }

  public GraphicsConfiguration getGraphicsConfiguration() {
    return graphicsConfiguration;
  }

  public GraphicsDevice getScreen() {
    return screen;
  }

  public KSize getSize() {
    return size;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public int getRatioX() {
    return ratioX;
  }

  public int getRatioY() {
    return ratioY;
  }

  public int getPixels() {
    return pixels;
  }

  public int getRatioPixels() {
    return ratioPixels;
  }

  @Override
  public String toString() {
    return id;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof ScreenInfo) {
      return id.equals(((ScreenInfo) other).id);
    }
    return false;
  }
} /* ENDCLASS */
