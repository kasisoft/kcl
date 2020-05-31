package com.kasisoft.libs.common.old.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.NonNull;

/**
 * Simple helper classes used to manage fonts.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FontPool {

  Map<String,Font>   fonts;
  List<String>       familynames;
  
  /**
   * Initialises this pool of Fonts.
   */
  public FontPool() {
    GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment(); 
    fonts                   = new Hashtable<>();
    familynames             = new ArrayList<>( Arrays.asList( env.getAvailableFontFamilyNames() ) );
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
  public boolean isKnownFamily( @NonNull String familyname ) {
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
  public Font getFont( @NonNull String familyname ) {
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
  public Font deriveFont( @NonNull String familyname, int style, float size ) {
    Font font = fonts.get( familyname );
    return font.deriveFont( style, size );
  }

  /**
   * @see Font#deriveFont(int, AffineTransform)
   * 
   * @param familyname   The family name which Font has to be returned. Neither <code>null</code> nor empty.
   */
  public Font deriveFont( @NonNull String familyname, int style, @NonNull AffineTransform trans ) {
    Font font = fonts.get( familyname );
    return font.deriveFont( style, trans );
  }

  /**
   * @see Font#deriveFont(float)
   * 
   * @param familyname   The family name which Font has to be returned. Neither <code>null</code> nor empty.
   */
  public Font deriveFont( @NonNull String familyname, float size ) {
    Font font = fonts.get( familyname );
    return font.deriveFont( size );
  }

  /**
   * @see Font#deriveFont(AffineTransform)
   * 
   * @param familyname   The family name which Font has to be returned. Neither <code>null</code> nor empty.
   */
  public Font deriveFont( @NonNull String familyname, @NonNull AffineTransform trans ){
    Font font = fonts.get( familyname );
    return font.deriveFont( trans );
  }

  /**
   * @see Font#deriveFont(int)
   * 
   * @param familyname   The family name which Font has to be returned. Neither <code>null</code> nor empty.
   */
  public Font deriveFont( @NonNull String familyname, int style ){
    Font font = fonts.get( familyname );
    return font.deriveFont( style );
  }

} /* ENDCLASS */
