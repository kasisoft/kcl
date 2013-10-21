/**
 * Name........: SwingFunctions
 * Description.: Collection of Swing related utility functions.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.ui;

//import com.kasisoft.libs.common.workspace.*;
import com.kasisoft.libs.common.util.*;

import javax.swing.table.*;

import javax.swing.*;

import java.awt.*;
import java.awt.image.*;

/**
 * Collection of Swing related utility functions.
 */
public class SwingFunctions {

  /**
   * Creates a popup menu from the supplied items. Only enabled items will be considered. Each <code>null</code> item 
   * will be used to insert a separator instead.
   * 
   * @param menuitems   The list of menuitems to use for the popup menu. Maybe <code>null</code>.
   * 
   * @return   The popup menu itself or <code>null</code> in case there's no enabled element.
   */
  public static JPopupMenu createPopupMenu( JMenuItem ... menuitems ) {
    if( (menuitems == null) || (menuitems.length == 0) ) {
      return null;
    }
    ExtArrayList<JMenuItem> items = new ExtArrayList<JMenuItem>();
    for( int i = 0; i < menuitems.length; i++ ) {
      if( menuitems[i] == null ) {
        if( (! items.isEmpty()) && (items.get(-1) != null) ) {
          // we're only adding null entries if the last one wasn't one
          items.add( null );
        }
      } else if( menuitems[i].isEnabled() ) {
        items.add( menuitems[i] );
      }
    }
    items.trim();
    if( items.isEmpty() ) {
      return null;
    }
    JPopupMenu result = new JPopupMenu();
    for( int i = 0; i < items.size(); i++ ) {
      if( items.get(i) != null ) {
        result.add( items.get(i) );
      } else {
        result.add( new JSeparator( SwingConstants.HORIZONTAL ) );
      }
    }
    return result;
  }
  
  /**
   * Loads the width of each column of the supplied table.
   * 
   * @param table   The JTable which widths shall be returned. Not <code>null</code>.
   *  
   * @return   The widths of the supplied tables. Not <code>null</code>.
   */
  public static int[] getTableColumnWidths( JTable table ) {
    TableColumnModel  model  = table.getColumnModel();
    int[]             result = new int[ model.getColumnCount() ];
    for( int i = 0; i < result.length; i++ ) {
      result[i] = model.getColumn(i).getWidth();
    }
    return result;
  }

  /**
   * Applies the widths to the columns of a JTable. Additional width values or columns will not be used in case the 
   * column count has been changed since getting the widhts using the method {@link #getTableColumnWidths(JTable)}.
   * 
   * @param table    The JTable which column widths shall be changed. Not <code>null</code>.
   * @param widths   The widths for the corresponding columns. Not <code>null</code>.
   */
  public static void setTableColumnWidths( JTable table, int[] widths ) {
    TableColumnModel model  = table.getColumnModel();
    int              length = Math.min( widths.length, model.getColumnCount() );
    for( int i = 0; i < length; i++ ) {
      TableColumn column   = model.getColumn(i);
      int         minwidth = column.getMinWidth();
      column.setMinWidth  ( widths[i] );
      column.setWidth     ( widths[i] );
      column.setMinWidth  ( minwidth  );
    }
  }
  
  /**
   * Creates an Image from the supplied component.
   *
   * @param comp   The Component which has to be returned as an Image. Not <code>null</code>.
   *
   * @return   A visual representation of the supplied component. Not <code>null</code>.
   */
  public static Image createImage( Component comp ) {
    BufferedImage image = new BufferedImage( comp.getWidth(), comp.getHeight(), BufferedImage.TYPE_INT_RGB );
    comp.paint( image.getGraphics() );
    return image;
  }
  
  /**
   * Sets up a JButton instance with an icon and limits it to a minimum size.
   * 
   * @param icon     The icon used to display within the button. Not <code>null</code>.
   * @param deltax   A horizontal margin.
   * @param deltay   A vertical margin.
   * 
   * @return   The newly created button. Not <code>null</code>.
   */
  public static JButton createButton( Icon icon, int deltax, int deltay ) {
    JButton result = new JButton( icon );
    setMinimumSize( result, icon.getIconWidth() + 2 * deltax, icon.getIconHeight() + 2 * deltay );
    return result;
  }
  
  /**
   * Makes sure that the supplied component will get at least a minimum dimension.
   * 
   * @param component   The control which shall become the minimum size. Not <code>null</code>.
   * @param width       The width of the component.
   * @param height      The height of the component.
   */
  public static void setMinimumSize( JComponent component, int width, int height ) {
    Dimension prefsize    = component.getPreferredSize();
    Dimension newprefsize = new Dimension( prefsize.width, prefsize.height );
    if( newprefsize.width < width ) {
      newprefsize.width = width;
    }
    if( newprefsize.height < height ) {
      newprefsize.height = height;
    }
    component . setMinimumSize   ( new Dimension( width, height ) );
    component . setPreferredSize ( newprefsize );
  }

  /**
   * Makes sure that the supplied component will get and hold a specific dimension.
   * 
   * @param component   The control which shall become a fixed size. Not <code>null</code>.
   * @param width       The width of the component.
   * @param height      The height of the component.
   */
  public static void setFixedSize( Component component, int width, int height ) {
    component . setMaximumSize   ( new Dimension( width, height ) );
    component . setMinimumSize   ( new Dimension( width, height ) );
    component . setPreferredSize ( new Dimension( width, height ) );
  }

  /**
   * Invokes an update on a complete component tree.
   * 
   * @param component   The component tree that shall be updated completely. Not <code>null</code>.
   */
  public static void updateComponentTreeUI( final Component component ) {
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        SwingUtilities.updateComponentTreeUI( component );
      }
    };
    if( SwingUtilities.isEventDispatchThread() ) {
      runnable.run();
    } else {
      SwingUtilities.invokeLater( runnable );
    }
  }

  /**
   * Modifies the boundaries of a component so it's being centered related to another component.
   * 
   * @param relocatable   The component which boundaries have to be adjusted. Not <code>null</code>.
   * @param related       The related container. If <code>null</code> the screen dimensions are used.
   *                      You can alternatively use {@link #center(Component)}.
   */
  public static void center( Component relocatable, Component related ) {
    if( related == null ) {
      center( relocatable );
    } else {
      Dimension relosize    = relocatable.getSize();
      Dimension relatedsize = related.getSize();
      int       width       = relatedsize.width  - relosize.width;
      int       height      = relatedsize.height - relosize.height;
      Point     location    = related.getLocation();
      relocatable.setBounds( location.x + width / 2, location.y + height / 2, relosize.width, relosize.height );
    }
  }
  

  /**
   * Modifies the boundaries of the supplied component so it will be centered on the screen. If the size of the 
   * component exceeds the size of the screen it's location will be modified accordingly to make sure it's in a visible 
   * area.
   * 
   * @param relocatable   The component which boundaries have to be adjusted. Not <code>null</code>.
   */
  public static void center( Component relocatable ) {
    Dimension relosize    = relocatable.getSize();
    Dimension screensize  = Toolkit.getDefaultToolkit().getScreenSize();
    int       width       = screensize.width  - relosize.width;
    int       height      = screensize.height - relosize.height;
    relocatable.setBounds( Math.max( width / 2, 0 ), Math.max( height / 2, 0 ), relosize.width, relosize.height );
  }
  
} /* ENDCLASS */
