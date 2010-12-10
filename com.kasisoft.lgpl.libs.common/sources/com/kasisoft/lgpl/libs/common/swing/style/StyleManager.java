/**
 * Name........: StyleManager
 * Description.: Management class which allows to control various styles by their names. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.swing.style;

import com.kasisoft.lgpl.tools.diagnostic.*;

import javax.swing.*;

import java.util.*;
import java.awt.*;

/**
 * Management class which allows to control various styles by their names. The use of styles is
 * similar to their use in office programs.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class StyleManager {

  private static final float DEFAULT_SIZE = 12;

  private static StyleManager     instance = null;
  
  private Map<String,Style>                 styles;
  private Map<String,WriteProtectedStyle>   writeprotected;
  private Style                             defaultstyle;
  private FontPool                          fonts;
  
  /**
   * Initialises this manager for styles.
   */
  private StyleManager() {
    styles         = new Hashtable<String,Style>();
    writeprotected = new Hashtable<String,WriteProtectedStyle>();
    fonts          = new FontPool();
    defaultstyle   = newDefaultStyle();
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
    result.setFont( FontFamily.SansSerif.getFont( fonts ).deriveFont( DEFAULT_SIZE ) );
    return new WriteProtectedStyle( result );
  }
  
  /**
   * Returns the FontPool instance used together with this StyleManager.
   * 
   * @return   The FontPool instance used together with this StyleManager. Not <code>null</code>.
   */
  public FontPool getFontPool() {
    return fonts;
  }
  
  /**
   * Adds the supplied Style to this manager. If the Style already exists the attributes will
   * be merged.
   * 
   * @param newstyle   The new Style which has to be added. Not <code>null</code>.
   */
  public synchronized void addStyle( @KNotNull(name="newstyle") Style newstyle ) {
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
      WriteProtectedStyle protectedstyle = new WriteProtectedStyle( name, defaultstyle );
      protectedstyle.forcedMerge( style );
      writeprotected.put( name, protectedstyle );
    }
  }
  
  /**
   * Returns the Style for the supplied name. The returned Style cannot be altered by any of it'S
   * methods. If this manager doesn't contain a Style with the supplied name the default Style
   * instance will be returned.
   * 
   * @param name   The name of the desired Style. Neither <code>null</code> nor empty.
   * 
   * @return   The Style for the supplied name. Not <code>null</code>.
   */
  public synchronized Style getStyle( 
    @KNotEmpty(name="name")   String   name 
  ) {
    Style result = writeprotected.get( name );
    if( result == null ) {
      result = defaultstyle;
    }
    return result;
  }
  
  /**
   * Returns the Style which is used as the default. The returned Style cannot be altered by any of 
   * it'S methods. Each member of the Style is guaranteed to be set.
   * 
   * @return   The default Style. Not <code>null</code>.
   */
  public Style getDefaultStyle() {
    return defaultstyle;
  }
  
  /**
   * Applies the settings of a Style to a specific component.
   * 
   * @param destination   The component which will be altered according to the style. 
   *                      Not <code>null</code>.
   * @param stylename     The name of the style. Neither <code>null</code> nor empty.
   */
  public void applyStyle( 
    @KNotNull(name="destination")   JComponent   destination, 
    @KNotEmpty(name="stylename")    String       stylename 
  ) {
    Style style = StyleManager.getInstance().getStyle( stylename );
    if( destination instanceof JLabel ) {
      ((JLabel) destination).setHorizontalAlignment( style.getAlignment().getAlignmentX() );
    } else if( destination instanceof JTextField ) {
      ((JTextField) destination).setHorizontalAlignment( style.getAlignment().getAlignmentX() );
    }
    destination.setFont( style.getFont() );
    destination.setForeground( style.getForeground() );
    destination.setBackground( style.getBackground() );
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
    
    /**
     * {@inheritDoc}
     */
    public void merge( Style other ) {
    }

    /**
     * {@inheritDoc}
     */
    public void set( Style other ) {
    }

    /**
     * {@inheritDoc}
     */
    public void setAlignment( Alignment newalignment ) {
    }

    /**
     * {@inheritDoc}
     */
    public void setBackground( Color newbackground ) {
    }

    /**
     * {@inheritDoc}
     */
    public void setFont( Font newfont ) {
    }

    /**
     * {@inheritDoc}
     */
    public void setForeground( Color newforeground ) {
    }

  } /* ENDCLASS */
  
} /* ENDCLASS */
