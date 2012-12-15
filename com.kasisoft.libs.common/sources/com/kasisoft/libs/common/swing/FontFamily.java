/**
 * Name........: FontFamily
 * Description.: Collection of widely known family names. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.swing;

import com.kasisoft.lgpl.tools.diagnostic.*;

import java.awt.geom.*;
import java.awt.*;
 
/**
 * Collection of widely known family names. There's no guarantuee that these Fonts exist on a
 * specific system except for {@link #SansSerif}, {@link #Serif} and {@link #Monospaced} which
 * is guaranteed by the java api.
 * 
 * Note: If a Font is not known the Monospaced variety is deliverd since it's definitely existing.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public enum FontFamily {

  Arial              ( "Arial"                ),
  ArialBlack         ( "Arial Black"          ),
  CourierNew         ( "Courier New"          ),
  Dialog             ( "Dialog"               ),
  DialogInput        ( "DialogInput"          ),
  LucidaConsole      ( "Lucida Console"       ),
  LucidaSansUnicode  ( "Lucida Sans Unicode"  ),
  MicrosoftSansSerif ( "Microsoft Sans Serif" ),
  Monospaced         ( "Monospaced"           ),
  SansSerif          ( "SansSerif"            ),
  Serif              ( "Serif"                ),
  Symbol             ( "Symbol"               ),
  Tahoma             ( "Tahoma"               ),
  TimesNewRoman      ( "Times New Roman"      );
  
  
  private String   familyname;
  
  /**
   * Initialises this value with the supplied Font family name.
   * 
   * @param name   The Font family name. Neither <code>null</code> nor empty.
   */
  FontFamily( String name ) {
    familyname = name;
  }

  /**
   * Returns the name of the Font family.
   * 
   * @return   The name of the Font family. Neither <code>null</code> nor empty.
   */
  public String getFamilyname() {
    return familyname;
  }
  
  /**
   * Returns <code>true</code> if this Font family is known to the supplied pool.
   * 
   * @param fontpool   The FontPool instance used for the test. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> This Font family is known to the supplied FontPool instance.
   */
  public boolean isKnown( @KNotNull(name="fontpool") FontPool fontpool ) {
    return fontpool.isKnownFamily( familyname );
  }
  
  /**
   * @see Font#deriveFont(int, float)
   * 
   * @param fontpool   The FontPool providing the derived Font. Not <code>null</code>.
   */
  public Font deriveFont( @KNotNull(name="fontpool") FontPool fontpool, int style, float size ) {
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
  public Font deriveFont( @KNotNull(name="fontpool") FontPool fontpool, int style, AffineTransform trans ) {
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
  public Font deriveFont( @KNotNull(name="fontpool") FontPool fontpool, float size ) {
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
  public Font deriveFont( @KNotNull(name="fontpool") FontPool fontpool, AffineTransform trans ) {
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
  public Font deriveFont( @KNotNull(name="fontpool") FontPool fontpool, int style ) {
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
  public Font getFont( @KNotNull(name="fontpool") FontPool fontpool ) {
    if( fontpool.isKnownFamily( familyname ) ) {
      return fontpool.getFont( familyname );
    } else {
      return fontpool.getFont( Monospaced.familyname );
    }
  }
  
  /**
   * Determines the constant value which matches the supplied Font family name.
   * 
   * @param name   The Font family name used to access the value. Neither <code>null</code> nor empty.
   * 
   * @return   The constant value. Maybe <code>null</code> if the suppleid value could not be matched.
   */
  public static final FontFamily valueByFamilyname( @KNotEmpty(name="name") String name ) {
    for( FontFamily fontfamily : FontFamily.values() ) {
      if( fontfamily.familyname.equals( name ) ) {
        return fontfamily;
      }
    }
    return null;
  }
  
} /* ENDENUM */
