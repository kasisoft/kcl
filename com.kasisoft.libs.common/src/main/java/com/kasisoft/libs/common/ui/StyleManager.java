package com.kasisoft.libs.common.ui;

import com.kasisoft.libs.common.constants.*;

import lombok.experimental.*;

import lombok.*;

import javax.swing.*;
import javax.swing.border.*;

import java.util.*;

import java.awt.*;

/**
 * Management class which allows to control various styles by their names. The use of styles is similar to their use in 
 * office programs.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StyleManager {

  static final float DEFAULT_SIZE = 12;

  static StyleManager     instance = null;
  
  Map<String,Style>                 styles;
  Map<String,WriteProtectedStyle>   writeprotected;
  
  @Getter
  Style                             defaultStyle;
  
  @Getter
  FontPool                          fontPool;
  
  /**
   * Initialises this manager for styles.
   */
  private StyleManager() {
    styles         = new Hashtable<>();
    writeprotected = new Hashtable<>();
    fontPool       = new FontPool();
    defaultStyle   = newDefaultStyle();
  }
  
  /**
   * Returns a newly created default Style instance.
   * 
   * @return   A newly created default Style instance. Not <code>null</code>.
   */
  private Style newDefaultStyle() {
    Style result = new Style( "default" );
    result.setAlignment( Alignment.Center );
    result.setForeground( Color.black );
    result.setBackground( Color.white );
    result.setFont( FontFamily.SansSerif.getFont( fontPool ).deriveFont( DEFAULT_SIZE ) );
    return new WriteProtectedStyle( result );
  }
  
  /**
   * Adds the supplied Style instances to this manager. If a Style already exists the attributes will be merged.
   * 
   * @param newstyles   The new Style instances which have to be added. Not <code>null</code>.
   */
  public synchronized void addStyles( @NonNull Style ... newstyles ) {
    for( Style style : newstyles ) {
      addStyle( style ); 
    }
  }
  
  /**
   * Adds the supplied Style to this manager. If the Style already exists the attributes will be merged.
   * 
   * @param newstyle   The new Style which has to be added. Not <code>null</code>.
   */
  public synchronized void addStyle( @NonNull Style newstyle ) {
    String name  = newstyle.getName();
    Style  style = styles.get( name );
    if( style != null ) {
      // the style has been added already
      style.merge( newstyle );
      writeprotected.get( name ).forcedMerge( newstyle );
    } else {
      // we need to create a new style (a copy, so the user can't alter it without explicitly merging it)
      style = new Style( newstyle );
      styles.put( name, style );
      // we're creating a write protected instance which has each attribute set
      WriteProtectedStyle protectedstyle = new WriteProtectedStyle( name, defaultStyle );
      protectedstyle.forcedMerge( style );
      writeprotected.put( name, protectedstyle );
    }
  }
  
  /**
   * Returns the Style for the supplied name. The returned Style cannot be altered by any of it's methods. If this 
   * manager doesn't contain a Style with the supplied name the default Style instance will be returned.
   * 
   * @param name   The name of the desired Style. Neither <code>null</code> nor empty.
   * 
   * @return   The Style for the supplied name. Not <code>null</code>.
   */
  public synchronized Style getStyle( @NonNull String name ) {
    Style result = writeprotected.get( name );
    if( result == null ) {
      result = defaultStyle;
    }
    return result;
  }
  
  /**
   * Applies the settings of a Style to a specific component.
   * 
   * @param destination   The component which will be altered according to the style. Not <code>null</code>.
   * @param stylename     The name of the style. Neither <code>null</code> nor empty.
   */
  public void applyStyle( @NonNull JComponent destination, @NonNull String stylename ) {
    Style     style     = getStyle( stylename );
    Alignment alignment = style.getAlignment();
    if( alignment != null ) {
      alignment.set( destination );
    }
    Font font = style.getFont();
    if( font != null ) {
      destination.setFont( style.getFont() );
    }
    Color foreground = style.getForeground();
    if( foreground != null ) {
      destination.setForeground( foreground );
    }
    Color background = style.getBackground();
    if( background != null ) {
      destination.setBackground( background );
    }
    Integer padding = style.getPadding();
    if( padding != null ) {
      Border oldborder = destination.getBorder();
      Border spacing   = BorderFactory.createEmptyBorder( padding.intValue(), padding.intValue(), padding.intValue(), padding.intValue() );
      if( oldborder == null ) {
        destination.setBorder( spacing );
      } else {
        destination.setBorder( BorderFactory.createCompoundBorder( oldborder, spacing ) );
      }
    }
  }
  
  /**
   * Returns an instance of the StyleManager allowing to manage styles.
   * 
   * @return   An instance of the StyleManager allowing to manage styles. Not <code>null</code>.
   */
  public static final synchronized StyleManager getInstance() {
    if( instance == null ) {
      instance = new StyleManager();
    }
    return instance;
  }
  
  /**
   * Variety of the Style class which doesn't support to alter attributes anymore.
   */
  private static class WriteProtectedStyle extends Style {

    /**
     * Initialises this Style with the settings of the supplied Style.
     * 
     * @param source   The Style which provides the current settings. Not <code>null</code>.
     */
    public WriteProtectedStyle( Style source ) {
      super( source );
    }

    /**
     * Initialises this Style with the settings of the supplied Style.
     * 
     * @param stylename   The name of the Style. Neither <code>null</code> nor empty.
     * @param source      The Style which provides the current settings. Not <code>null</code>.
     */
    public WriteProtectedStyle( String stylename, Style source ) {
      super( stylename, source );
    }

    /**
     * @see #merge(Style)
     * 
     * This method is only known within the StyleManager class.
     */
    private void forcedMerge( Style other ) {
      super.merge( other );
    }
    
    @Override
    public void merge( Style other ) {
    }

    @Override
    public void set( Style other ) {
    }

    @Override
    public void setAlignment( Alignment newalignment ) {
    }

    @Override
    public void setBackground( Color newbackground ) {
    }

    @Override
    public void setFont( Font newfont ) {
    }

    @Override
    public void setForeground( Color newforeground ) {
    }

  } /* ENDCLASS */
  
} /* ENDCLASS */
