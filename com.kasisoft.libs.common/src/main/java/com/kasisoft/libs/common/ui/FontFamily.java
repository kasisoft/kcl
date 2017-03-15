package com.kasisoft.libs.common.ui;

import lombok.experimental.*;

import lombok.*;

import java.util.*;

import java.awt.*;
import java.awt.geom.*;
 
/**
 * Collection of widely known family names. There's no guarantuee that these Fonts exist on a specific system except for 
 * {@link #SansSerif}, {@link #Serif} and {@link #Monospaced} which is guaranteed by the java api.
 * 
 * Note: If a Font is not known the Monospaced variety is deliverd since it's definitely existing.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class FontFamily {

  public static final FontFamily  Arial;
  public static final FontFamily  ArialBlack;
  public static final FontFamily  CourierNew;
  public static final FontFamily  Dialog;
  public static final FontFamily  DialogInput;
  public static final FontFamily  LucidaConsole;
  public static final FontFamily  LucidaSansUnicode;
  public static final FontFamily  MicrosoftSansSerif;
  public static final FontFamily  Monospaced;
  public static final FontFamily  SansSerif;
  public static final FontFamily  Serif;
  public static final FontFamily  Symbol;
  public static final FontFamily  Tahoma;
  public static final FontFamily  TimesNewRoman;
  
  private static final Map<String, FontFamily>   FONTFAMILIES;
  
  static {
    FONTFAMILIES        = new Hashtable<>();
    Arial               = new FontFamily( "Arial"                );
    ArialBlack          = new FontFamily( "Arial Black"          );
    CourierNew          = new FontFamily( "Courier New"          );
    Dialog              = new FontFamily( "Dialog"               );
    DialogInput         = new FontFamily( "DialogInput"          );
    LucidaConsole       = new FontFamily( "Lucida Console"       );
    LucidaSansUnicode   = new FontFamily( "Lucida Sans Unicode"  );
    MicrosoftSansSerif  = new FontFamily( "Microsoft Sans Serif" );
    Monospaced          = new FontFamily( "Monospaced"           );
    SansSerif           = new FontFamily( "SansSerif"            );
    Serif               = new FontFamily( "Serif"                );
    Symbol              = new FontFamily( "Symbol"               );
    Tahoma              = new FontFamily( "Tahoma"               );
    TimesNewRoman       = new FontFamily( "Times New Roman"      );
  }

  /** Neither <code>null</code> nor empty. */
  @Getter String   familyname;
  
  /**
   * Initialises this value with the supplied Font family name.
   * 
   * @param name   The Font family name. Neither <code>null</code> nor empty.
   */
  public FontFamily( String name ) {
    familyname = name;
    FONTFAMILIES.put( familyname, this );
  }

  /**
   * Returns <code>true</code> if this Font family is known to the supplied pool.
   * 
   * @param fontpool   The FontPool instance used for the test. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> This Font family is known to the supplied FontPool instance.
   */
  public boolean isKnown( @NonNull FontPool fontpool ) {
    return fontpool.isKnownFamily( familyname );
  }
  
  /**
   * @see Font#deriveFont(int, float)
   * 
   * @param fontpool   The FontPool providing the derived Font. Not <code>null</code>.
   */
  public Font deriveFont( @NonNull FontPool fontpool, int style, float size ) {
    if( fontpool.isKnownFamily( familyname ) ) {
      return fontpool.deriveFont( familyname, style, size );
    } else {
      return fontpool.deriveFont( Monospaced.familyname, style, size );
    }
  }

  /**
   * @see Font#deriveFont(int, AffineTransform)
   * 
   * @param fontpool   The FontPool providing the derived Font. Not <code>null</code>.
   */
  public Font deriveFont( @NonNull FontPool fontpool, int style, @NonNull AffineTransform trans ) {
    if( fontpool.isKnownFamily( familyname ) ) {
      return fontpool.deriveFont( familyname, style, trans );
    } else {
      return fontpool.deriveFont( Monospaced.familyname, style, trans );
    }
  }

  /**
   * @see Font#deriveFont(float)
   * 
   * @param fontpool   The FontPool providing the derived Font. Not <code>null</code>.
   */
  public Font deriveFont( @NonNull FontPool fontpool, float size ) {
    if( fontpool.isKnownFamily( familyname ) ) {
      return fontpool.deriveFont( familyname, size );
    } else {
      return fontpool.deriveFont( Monospaced.familyname, size );
    }
  }

  /**
   * @see Font#deriveFont(AffineTransform)
   * 
   * @param fontpool   The FontPool providing the derived Font. Not <code>null</code>.
   */
  public Font deriveFont( @NonNull FontPool fontpool, @NonNull AffineTransform trans ) {
    if( fontpool.isKnownFamily( familyname ) ) {
      return fontpool.deriveFont( familyname, trans );
    } else {
      return fontpool.deriveFont( Monospaced.familyname, trans );
    }
  }

  /**
   * @see Font#deriveFont(int)
   * 
   * @param fontpool   The FontPool providing the derived Font. Not <code>null</code>.
   */
  public Font deriveFont( @NonNull FontPool fontpool, int style ) {
    if( fontpool.isKnownFamily( familyname ) ) {
      return fontpool.deriveFont( familyname, style );
    } else {
      return fontpool.deriveFont( Monospaced.familyname, style );
    }
  }

  /**
   * Returns the plain Font associated with the supplied FontPool instance.
   *  
   * @param fontpool   The FontPool providing the Font. Not <code>null</code>.
   * 
   * @return   The plain Font for this Font family. Not <code>null</code>.
   */
  public Font getFont( @NonNull FontPool fontpool ) {
    if( fontpool.isKnownFamily( familyname ) ) {
      return fontpool.getFont( familyname );
    } else {
      return fontpool.getFont( Monospaced.familyname );
    }
  }
  
  public static FontFamily[] values() {
    return FONTFAMILIES.values().toArray( new FontFamily[ FONTFAMILIES.size() ] );
  }
  
  /**
   * Determines the constant value which matches the supplied Font family name.
   * 
   * @param name   The Font family name used to access the value. Neither <code>null</code> nor empty.
   * 
   * @return   The constant value. Maybe <code>null</code> if the suppleid value could not be matched.
   */
  public static FontFamily valueByFamilyname( String name ) {
    for( FontFamily fontfamily : FontFamily.values() ) {
      if( fontfamily.familyname.equalsIgnoreCase( name ) ) {
        return fontfamily;
      }
    }
    return null;
  }
  
} /* ENDENUM */
