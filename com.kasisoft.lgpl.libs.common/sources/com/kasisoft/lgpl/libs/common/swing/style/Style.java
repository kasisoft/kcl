/**
 * Name........: Style
 * Description.: Simple style which combines some visualisations attributes. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.swing.style;

import com.kasisoft.lgpl.tools.diagnostic.*;

import java.awt.*;

/**
 * Simple style which combines some visualisations attributes. This class is mainly considered
 * to be a simple data structure so it's legal to have attributes with <code>null</code> values.
 * The value itself needs to be accessed through the manager which makes sure that there aren't
 * <code>null</code> values. These are filled with default settings.  
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class Style implements Comparable<Style> {

  private Font        font;
  private Color       foreground;
  private Color       background;
  private Alignment   alignment;
  private String      name;
  
  /**
   * Initialises this Style with a specified name for later reference.
   * 
   * @param stylename   The name of the Style. Neither <code>null</code> nor empty.
   */
  public Style( @KNotEmpty(name="stylename") String stylename ) {
    name       = stylename;
    font       = null;
    foreground = null;
    background = null;
    alignment  = null;
  }
  
  /**
   * Copy constructor for internal use.
   * 
   * @param source   The source of the copyied data. Not <code>null</code>.
   */
  Style( @KNotNull(name="source") Style source ) {
    name       = source.name;
    font       = source.font;
    foreground = source.foreground;
    background = source.background;
    alignment  = source.alignment;
  }

  /**
   * Copy constructor which allows to change the Style name..
   * 
   * @param stylename   The name of the Style. Neither <code>null</code> nor empty.
   * @param source      The source of the copyied data. Not <code>null</code>.
   */
  public Style( 
    @KNotEmpty(name="stylename")   String   stylename, 
    @KNotNull(name="source")       Style    source 
  ) {
    name       = stylename;
    font       = source.font;
    foreground = source.foreground;
    background = source.background;
    alignment  = source.alignment;
  }

  /**
   * Returns the Font to be used for this Style.
   * 
   * @return   The Font to be used for this Style. Maybe <code>null</code>.
   */
  public Font getFont() {
    return font;
  }

  /**
   * Changes the Font to be used for this Style.
   * 
   * @param newfont   The new Font to be used for this Style. Maybe <code>null</code>.
   */
  public void setFont( Font newfont ) {
    font = newfont;
  }

  /**
   * Returns the foreground Color to be used for this Style.
   * 
   * @return   The foreground Color to be used for this Style. Maybe <code>null</code>.
   */
  public Color getForeground() {
    return foreground;
  }

  /**
   * Changes the foreground Color to be used for this Style.
   *  
   * @param newforeground   The new foreground Color to be used for this Style. 
   *                        Maybe <code>null</code>.
   */
  public void setForeground( Color newforeground ) {
    foreground = newforeground;
  }

  /**
   * Returns the background Color to be used for this Style.
   * 
   * @return   The background Color to be used for this Style. Maybe <code>null</code>.
   */
  public Color getBackground() {
    return background;
  }

  /**
   * Changes the background Color to be used for this Style.
   *  
   * @param newbackground   The new background Color to be used for this Style. 
   *                        Maybe <code>null</code>.
   */
  public void setBackground( Color newbackground ) {
    background = newbackground;
  }

  /**
   * Returns the Alignment to be used for this Style.
   * 
   * @return   The Alignment to be used for this Style. Maybe <code>null</code>.
   */
  public Alignment getAlignment() {
    return alignment;
  }

  /**
   * Changes the Alignment to be used for this Style.
   * 
   * @param newalignment   The new Alignment to be used for this Style. Maybe <code>null</code>.
   */
  public void setAlignment( Alignment newalignment ) {
    alignment = newalignment;
  }

  /**
   * Returns the name for this Style.
   * 
   * @return   The name for this Style. Neither <code>null</code> nor empty.
   */
  public String getName() {
    return name;
  }
  
  /**
   * Transfers the settings from the supplied Style to this one (not changing the name !).
   * 
   * @param other   The other Style providing the settings. Not <code>null</code>.
   */
  public void set( @KNotNull(name="other") Style other ) {
    alignment  = other . getAlignment  ();
    background = other . getBackground ();
    foreground = other . getForeground ();
    font       = other . getFont       ();
  }

  /**
   * Transfers only the available settings from the supplied Style to this one 
   * (not changing the name !).
   * 
   * @param other   The other Style providing the settings. Not <code>null</code>.
   */
  public void merge( @KNotNull(name="other") Style other ) {
    if( other.getAlignment() != null ) {
      alignment = other.getAlignment();
    }
    if( other.getBackground() != null ) {
      background = other.getBackground();
    }
    if( other.getForeground() != null ) {
      foreground = other.getForeground();
    }
    if( other.getFont() != null ) {
      font = other.getFont();
    }
  }
  
  /**
   * Returns <code>true</code> if each attribut is set.
   * 
   * @return   <code>true</code> <=> Each attribute has been set.
   */
  public boolean isComplete() {
    return
      (alignment  != null) &&
      (background != null) &&
      (foreground != null) &&
      (font       != null);
  }

  /**
   * {@inheritDoc}
   */
  public String toString() {
    return name;
  }

  /**
   * {@inheritDoc}
   */
  public int compareTo( Style other ) {
    return name.compareTo( other.name );
  }
  
} /* ENDCLASS */
