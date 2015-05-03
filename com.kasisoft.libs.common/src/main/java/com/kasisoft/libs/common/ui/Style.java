package com.kasisoft.libs.common.ui;

import com.kasisoft.libs.common.constants.*;

import lombok.experimental.*;

import lombok.*;

import java.awt.*;

/**
 * Simple style which combines some visualisations attributes. This class is mainly considered to be a simple data 
 * structure so it's legal to have attributes with <code>null</code> values. The value itself needs to be accessed 
 * through the manager which makes sure that there aren't <code>null</code> values. These are filled with default 
 * settings.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Style implements Comparable<Style> {

  Font        font;
  Color       foreground;
  Color       background;
  Alignment   alignment;
  Integer     padding;
  
  @Setter(AccessLevel.PRIVATE)
  String      name;
  
  /**
   * Initialises this Style with a specified name for later reference.
   * 
   * @param stylename   The name of the Style. Neither <code>null</code> nor empty.
   */
  public Style( @NonNull String stylename ) {
    name       = stylename;
    font       = null;
    foreground = null;
    background = null;
    alignment  = null;
    padding    = null;
  }
  
  /**
   * Copy constructor for internal use.
   * 
   * @param source   The source of the copyied data. Not <code>null</code>.
   */
  Style( @NonNull Style source ) {
    name       = source.name;
    font       = source.font;
    foreground = source.foreground;
    background = source.background;
    alignment  = source.alignment;
    padding    = source.padding;
  }

  /**
   * Copy constructor which allows to change the Style name..
   * 
   * @param stylename   The name of the Style. Neither <code>null</code> nor empty.
   * @param source      The source of the copyied data. Not <code>null</code>.
   */
  public Style( @NonNull String stylename, @NonNull Style source ) {
    name       = stylename;
    font       = source.font;
    foreground = source.foreground;
    background = source.background;
    alignment  = source.alignment;
    padding    = source.padding;
  }

  /**
   * Transfers the settings from the supplied Style to this one (not changing the name !).
   * 
   * @param other   The other Style providing the settings. Not <code>null</code>.
   */
  public void set( @NonNull Style other ) {
    alignment  = other . getAlignment  ();
    background = other . getBackground ();
    foreground = other . getForeground ();
    font       = other . getFont       ();
    padding    = other . getPadding    ();
  }

  /**
   * Transfers only the available settings from the supplied Style to this one 
   * (not changing the name !).
   * 
   * @param other   The other Style providing the settings. Not <code>null</code>.
   */
  public void merge( @NonNull Style other ) {
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
    if( other.getPadding() != null ) {
      padding = other.getPadding();
    }
  }
  
  @Override
  public int compareTo( @NonNull Style other ) {
    return name.compareTo( other.name );
  }
  
} /* ENDCLASS */
