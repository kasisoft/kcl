package com.kasisoft.libs.common.old.ui;

import com.kasisoft.libs.common.old.function.Predicates;
import com.kasisoft.libs.common.old.util.ExtArrayList;
import com.kasisoft.libs.common.types.Pair;
import com.kasisoft.libs.common.types.ScreenInfo;
import com.kasisoft.libs.common.types.ScreenInfo.ComparisonMode;

import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import javax.swing.tree.TreeNode;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import lombok.NonNull;

/**
 * Collection of Swing related utility functions.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class SwingFunctions {

  /**
   * Returns the preferred size for the supplied component assuming that this component is supposed to spread at least
   * over a specific amount of characters.
   * 
   * @param size        The preferred size. Not <code>null</code>
   * @param component   The component which preferred size is desired. Not <code>null</code>.
   * @param minchars    The minimum amount of chars that has to be used.
   *  
   * @return   The minimum size. Not <code>null</code>.
   */
  public static Dimension getAdjustedPreferredSize( @NonNull Dimension size, @NonNull Component component, int minchars ) {
    if( (size.height == 0) || (size.width == 0) ) {
      FontMetrics metrics = component.getFontMetrics( component.getFont() );
      if( size.height == 0 ) {
        size.height = metrics.getHeight();
      }
      size.width = Math.max( size.width, metrics.charWidth( 'W' ) * Math.max( 1, minchars ) );
    }
    return size;
  }

  /**
   * Returns the minimum size for the supplied component. This function performs an adjustment towards the preferred 
   * size.
   * 
   * @param component   The component which minimum size is desired. Not <code>null</code>.
   *  
   * @return   The minimum size. Not <code>null</code>.
   */
  public static Dimension getAdjustedMinimumSize( @NonNull Dimension size, @NonNull Component component ) {
    return adjust( size, component );
  }
  
  /**
   * Returns the maximum size for the supplied component. This function performs an adjustment towards the preferred 
   * size.
   * 
   * @param component   The component which maximum size is desired. Not <code>null</code>.
   *  
   * @return   The maximum size. Not <code>null</code>.
   */
  public static Dimension getAdjustedMaximumSize( @NonNull Dimension size, @NonNull Component component ) {
    return adjust( size, component );
  }
  
  /**
   * Adjusts a dimension towards the preferred size.
   * 
   * @param toadjust    The Dimension instance which is supposed to be adjusted. Not <code>null</code>.
   * @param component   The component which provides the preferred size. Not <code>null</code>.
   * 
   * @return   The adjusted size. Not <code>null</code>.
   */
  private static Dimension adjust( Dimension toadjust, Component component ) {
    if( (toadjust.width == 0) || (toadjust.height == 0) ) {
      Dimension preferred = component.getPreferredSize();
      if( toadjust.width == 0 ) {
        toadjust.width = preferred.width;
      }
      if( toadjust.height == 0 ) {
        toadjust.height = preferred.height;
      }
    }
    return toadjust;
  }

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
    ExtArrayList<JMenuItem> items = new ExtArrayList<>();
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
  public static int[] getTableColumnWidths( @NonNull JTable table ) {
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
  public static void setTableColumnWidths( @NonNull JTable table, @NonNull int[] widths ) {
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
  public static BufferedImage createImage( @NonNull Component comp ) {
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
  public static JButton createButton( @NonNull Icon icon, int deltax, int deltay ) {
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
  public static void setMinimumSize( @NonNull JComponent component, int width, int height ) {
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
  public static void setFixedSize( @NonNull Component component, int width, int height ) {
    component . setMaximumSize   ( new Dimension( width, height ) );
    component . setMinimumSize   ( new Dimension( width, height ) );
    component . setPreferredSize ( new Dimension( width, height ) );
  }

  /**
   * Invokes an update on a complete component tree.
   * 
   * @param component   The component tree that shall be updated completely. Not <code>null</code>.
   */
  public static void updateComponentTreeUI( final @NonNull Component component ) {
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
  
  public static void forComponentTreeDo( @NonNull Component component, @NonNull Predicate<Component> testComponent, @NonNull Consumer<Component> handleComponent ) {
    forComponentTreeDo( component, testComponent, handleComponent, Predicates.acceptNone(), ($c, $b) -> {} );
  }
  
  public static void forComponentTreeDo( @NonNull Component component, @NonNull Predicate<Component> testComponent, @NonNull Consumer<Component> handleComponent, @NonNull Predicate<Border> testBorder, @NonNull BiConsumer<JComponent, Border> handleBorder ) {
    if( component instanceof JComponent ) {
      JComponent jcomponent = (JComponent) component;
      Border     border     = jcomponent.getBorder();
      if( (border != null) && testBorder.test( border ) ) {
        handleBorder.accept( jcomponent, border );
      }
    }
    if( testComponent.test( component ) ) {
      handleComponent.accept( component );
    }
    if( component instanceof Container ) {
      Container container = (Container) component;
      for( int i = 0; i < container.getComponentCount(); i++ ) {
        forComponentTreeDo( container.getComponent(i), testComponent, handleComponent, testBorder, handleBorder );
      }
    }
  }

  /**
   * Modifies the boundaries of a component so it's being centered related to another component.
   * 
   * @param relocatable   The component which boundaries have to be adjusted. Not <code>null</code>.
   * @param related       The related container. If <code>null</code> the screen dimensions are used.
   *                      You can alternatively use {@link #center(Component)}.
   */
  public static void center( @NonNull Component relocatable, Component related ) {
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
  public static void center( @NonNull Component relocatable ) {
    Dimension relosize    = relocatable.getSize();
    Dimension screensize  = relocatable.getGraphicsConfiguration().getBounds().getSize();
    int       width       = screensize.width  - relosize.width;
    int       height      = screensize.height - relosize.height;
    relocatable.setBounds( Math.max( width / 2, 0 ), Math.max( height / 2, 0 ), relosize.width, relosize.height );
  }

  /**
   * Modifies the boundaries of the supplied component so it will be centered on the screen. If the size of the 
   * component exceeds the size of the screen it's location will be modified accordingly to make sure it's in a visible 
   * area.
   * 
   * @param relocatable   The component which boundaries have to be adjusted. Not <code>null</code>.
   * @param screenInfo    The screen where the component shall be centered on. Not <code>null</code>.
   */
  public static void center( @NonNull Component relocatable, @NonNull ScreenInfo screenInfo ) {
    Dimension relosize    = relocatable.getSize();
    int       width       = screenInfo.getWidth()  - relosize.width;
    int       height      = screenInfo.getHeight() - relosize.height;
    Rectangle bounds      = screenInfo.getGraphicsConfiguration().getBounds();
    relocatable.setBounds( bounds.x + Math.max( width / 2, 0 ), bounds.y + Math.max( height / 2, 0 ), relosize.width, relosize.height );
  }

  /**
   * Invokes the supplied Runnable instance within the event dispatch thread and waits for it's completion. Any error
   * will be thrown as a {@link RuntimeException}.
   *  
   * @param runnable   The Runnable instance that is supposed to be invoked. Not <code>null</code>.
   */
  public static void invokeAndWait( @NonNull Runnable runnable ) {
    if( SwingUtilities.isEventDispatchThread() ) {
      runnable.run();
    } else {
      try {
        SwingUtilities.invokeAndWait( runnable );
      } catch( Exception ex ) {
        throw new RuntimeException( ex );
      }
    }
  }

  /**
   * Invokes the supplied Runnable instance within the event dispatch thread as soon as possible without waiting for 
   * it's completion. Any error will be thrown as a {@link RuntimeException}.
   *  
   * @param runnable   The Runnable instance that is supposed to be invoked. Not <code>null</code>.
   */
  public static void invokeLater( @NonNull Runnable runnable ) {
    if( SwingUtilities.isEventDispatchThread() ) {
      new Thread( runnable ).start();
    } else {
      try {
        SwingUtilities.invokeLater( runnable );
      } catch( Exception ex ) {
        throw new RuntimeException( ex );
      }
    }
  }
  
  /**
   * Expands all rows from the supplied tree.
   * 
   * @param tree   The tree which rows shall be expanded. Not <code>null</code>.
   */
  public static void expandTree( @NonNull JTree tree ) {
    for( int i = 0; i < tree.getRowCount(); i++ ) {
      tree.expandRow(i);
    }
  }

  /**
   * Collects the complete subtree provided by the supplied node. The supplied node is NOT part of the resulting list.
   * 
   * @param node   The node which provides the subtree. Not <code>null</code>.
   * 
   * @return   A list with all nodes. Not <code>null</code>.
   */
  public static <T extends TreeNode> List<T> collectChildren( @NonNull T node ) {
    List<T> result = new ArrayList<>( node.getChildCount() );
    collectChildren( result, node );
    return result;
  }
     
  /**
   * Collects the complete node tree.
   * 
   * @param receiver   A list with all nodes. Not <code>null</code>.
   * @param node       The node which provides the subtree. Not <code>null</code>.
   */
  private static <T extends TreeNode> void collectChildren( List<T> receiver, T node ) {
    for( int i = 0; i < node.getChildCount(); i++ ) {
      T child = (T) node.getChildAt(i);
      receiver.add( child );
      collectChildren( receiver, child );
    }
  }
  
  public static Frame getFrame( Component component ) {
    Frame result = null;
    if( component instanceof Frame ) {
      result = (Frame) component;
    } else if( component != null ) {
      result = getFrame( component.getParent() );
    }
    return result;
  }

  /**
   * Returns a list of available screens for the current setup.
   * 
   * @return  A list of available screens for the current setup. Not <code>null</code>
   */
  public static List<ScreenInfo> getScreenInfos() {
    GraphicsEnvironment ge      = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice[]    screens = ge.getScreenDevices();
    List<ScreenInfo>    result  = new ArrayList<>( screens.length );
    for( GraphicsDevice screen : screens ) {
      GraphicsConfiguration gc      = screen.getDefaultConfiguration();
      Rectangle             bounds  = gc.getBounds();
      result.add( new ScreenInfo( screen.getIDstring(), gc, screen, bounds.width, bounds.height ) );
    }
    Collections.sort( result );
    return result;
  }
  
  public static ScreenInfo getScreenInfo( List<Pair<ComparisonMode, Integer>> priorization ) {
    List<ScreenInfo> available = getScreenInfos();
    ScreenInfo       result    = null;
    if( (available.size() > 1) && (priorization != null) && (priorization.size() > 0) ) {
      // reduce the list depending on the priorization of the comparators
      int pos = 0;
      while( (available.size() > 1) && (pos < priorization.size()) ) {
        Pair<ComparisonMode, Integer> priority = priorization.get( pos++ );
        reduceScreens( available, priority.getValue(), priority.getKey() );
      }
    }
    if( available.size() == 1 ) {
      // only one possibility available, so use it
      result = available.get(0);
    } else if( available.size() > 1 ) {
      // if there are comparators available use the first one which has the highest priority
      if( ! priorization.isEmpty() ) {
        Collections.sort( available, priorization.get(0).getKey() );
      }
      result = available.get(0);
    }
    return result;
  }
  
  private static void reduceScreens( List<ScreenInfo> screeninfos, int threshold, ComparisonMode mode ) {
    Collections.sort( screeninfos, mode );
    ScreenInfo first = screeninfos.get(0);
    for( int i = screeninfos.size() - 1; i >= 1; i-- ) {
      ScreenInfo other = screeninfos.get(i);
      if( mode.difference( first, other ) > threshold ) {
        screeninfos.remove(i);
      }
    }
  }
  
} /* ENDCLASS */
