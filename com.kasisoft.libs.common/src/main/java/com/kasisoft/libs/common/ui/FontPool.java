/**
 * Name........: FontPool
 * Description.: Simple helper classes used to manage fonts. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.ui;

import com.kasisoft.libs.common.util.*;

import java.util.*;
import java.util.List;

import java.awt.geom.*;
import java.awt.*;

/**
 * Simple helper classes used to manage fonts.
 */
public class FontPool {

  private Map<String,Font>   fonts;
  private List<String>       familynames;
  
  /**
   * Initialises this pool of Fonts.
   */
  public FontPool() {
    GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment(); 
    fonts                   = new Hashtable<String,Font>();
    familynames             = ArrayFunctions.addAll( new ArrayList<String>(), env.getAvailableFontFamilyNames() );
    Font[] fontlist         = env.getAllFonts();
    for( int i = 0; i < fontlist.length; i++ ) {
      if( fontlist[i].isPlain() ) {
        fonts.put( fontlist[i].getFamily(), fontlist[i] );
      }
    }
    Collections.sort( familynames );
  }

  /**
   * Returns <code>true</code> if a Font for the supplied family is available.
   * 
   * @param familyname   The name of the Font family. Neither <code>null</code> nor empty.
   * 
   * @return   <code>true</code> <=> A Font for the supplied family is available.
   */
  public boolean isKnownFamily( String familyname ) {
    return fonts.containsKey( familyname );
  }
  
  /**
   * Returns a plain Font for the supplied family if it's available.
   * 
   * @param familyname   The name of the Font family. Neither <code>null</code> nor empty.
   * 
   * @return   The Font instance if {@link #isKnownFamily(String)} is <code>true</code> else 
   *           <code>null</code>.
   */
  public Font getFont( String familyname ) {
    return fonts.get( familyname );
  }
  
  /**
   * Returns a list of all Font families currently known.
   * 
   * @return   A list of all Font families currently known. Not <code>null</code>.
   */
  public String[] getFamilynames() {
    String[] result = new String[ familynames.size() ];
    familynames.toArray( result );
    return result;
  }
  
  /**
   * @see Font#deriveFont(int, float)
   * 
   * @param familyname   The family name which Font has to be returned. Neither <code>null</code> nor empty.
   */
  public Font deriveFont( String familyname, int style, float size ) {
    Font font = fonts.get( familyname );
    return font.deriveFont( style, size );
  }

  /**
   * @see Font#deriveFont(int, AffineTransform)
   * 
   * @param familyname   The family name which Font has to be returned. Neither <code>null</code> nor empty.
   */
  public Font deriveFont( String familyname, int style, AffineTransform trans ) {
    Font font = fonts.get( familyname );
    return font.deriveFont( style, trans );
  }

  /**
   * @see Font#deriveFont(float)
   * 
   * @param familyname   The family name which Font has to be returned. Neither <code>null</code> nor empty.
   */
  public Font deriveFont( String familyname, float size ) {
    Font font = fonts.get( familyname );
    return font.deriveFont( size );
  }

  /**
   * @see Font#deriveFont(AffineTransform)
   * 
   * @param familyname   The family name which Font has to be returned. Neither <code>null</code> nor empty.
   */
  public Font deriveFont( String familyname, AffineTransform trans ){
    Font font = fonts.get( familyname );
    return font.deriveFont( trans );
  }

  /**
   * @see Font#deriveFont(int)
   * 
   * @param familyname   The family name which Font has to be returned. Neither <code>null</code> nor empty.
   */
  public Font deriveFont( String familyname, int style ){
    Font font = fonts.get( familyname );
    return font.deriveFont( style );
  }

} /* ENDCLASS */
