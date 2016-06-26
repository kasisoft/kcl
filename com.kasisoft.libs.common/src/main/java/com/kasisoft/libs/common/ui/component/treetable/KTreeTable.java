package com.kasisoft.libs.common.ui.component.treetable;

import com.kasisoft.libs.common.ui.*;

import lombok.experimental.*;

import lombok.*;

import javax.swing.event.*;

import javax.swing.table.*;

import javax.swing.tree.*;

import javax.swing.*;

import java.awt.event.*;

import java.awt.*;

/**
 * A classic tree table based upon the JTable. The tree functionality is realized through the rendering of
 * the nodes.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KTreeTable extends JTable {

  static final TreeTableCellRenderer  DEFAULT_RENDERER = new DefaultTreeTableCellRenderer();
  
  @Getter
  TreeTableModel      treeTableModel;
  
  TreeTableRenderer   renderer;
  LocalListener       locallistener;
  
  /**
   * Initializes an empty tree table.
   */
  public KTreeTable() {
    this( null );
  }
  
  /**
   * Initializes a tree table with the supplied model serving the data.
   * 
   * @param ttm   The model serving the data. If <code>null</code> an empty tree model is being used.
   */
  public KTreeTable( TreeTableModel ttm ) {
    super( ttm == null ? new DefaultTreeTableModel() : ttm );
    treeTableModel  = (TreeTableModel) super.getModel();
    renderer        = new TreeTableRenderer( this );
    locallistener   = new LocalListener( this );
    setRowHeight(24);
    renderer.setRowHeight(24);
    setDefaultRenderer( Object   . class , renderer );
    setDefaultRenderer( TreeNode . class , renderer );
    addMouseListener( locallistener );
    fixSizes();
    getSelectionModel().addListSelectionListener( locallistener );
    setSelectionModel( getSelectionModel() );
    setAutoCreateColumnsFromModel( true );
  }

  @Override
  public void setSelectionModel( ListSelectionModel newmodel ) {
    if( locallistener != null ) {
      getSelectionModel().removeListSelectionListener( locallistener );
    }
    super.setSelectionModel( newmodel );
    if( locallistener != null ) {
      newmodel.addListSelectionListener( locallistener );
    }
  }

  /**
   * Gives access to the JTree portion allowing a TreeCellRenderer to be used.
   * 
   * @return   A JTree component used fro the rendering process. Not <code>null</code>.
   */
  JTree getJTree() {
    return renderer;
  }

  /**
   * Enables a state which allows to render this component.
   */
  public void makePrintable() {
    expandAll();
    fixSizes();
    clearSelection();
  }

  /**
   * Changes the current model.
   * 
   * @param ttm   The new model. Maybe <code>null</code>.
   */
  public void setTreeTableModel( TreeTableModel ttm ) {
    if( ttm == null ) {
      ttm = new DefaultTreeTableModel();
    }
    if( ttm != getTreeTableModel() ) {
      super.setModel( ttm );
      treeTableModel = ttm;
      renderer.setTreeTableModel( treeTableModel );
      fixSizes();
    }
  }

  /**
   * Expands a row.
   * 
   * @param row   The row which shall be expanded.
   */
  private void expandRowImpl( int row ) {
    treeTableModel.expand( row );
    renderer.expandRow( row );
  }
  
  /**
   * Collapses a row.
   * 
   * @param row   The row which shall be collapsed.
   */
  private void collapseRowImpl( int row ) {
    treeTableModel.collapse( row );
    renderer.collapseRow( row );
  }
  
  /**
   * Expands a row.
   * 
   * @param row   The row which shall be expanded.
   */
  public void expandRow( int row ) {
    if( renderer.isCollapsed( row ) ) {
      clearSelection();
      expandRowImpl( row );
      setRowSelectionInterval( row, row );
      repaint();
    }
  }

  /**
   * Collapses a row.
   * 
   * @param row   The row which shall be collapsed.
   */
  public void collapseRow( int row ) {
    if( renderer.isExpanded( row ) ) {
      clearSelection();
      collapseRowImpl( row );
      setRowSelectionInterval( row, row );
      repaint();
    }
  }

  /**
   * Expands all collapsed rows.
   */
  public void expandAll() {
//    clearSelection();
//    int row = 0;
//    while( row < treeTableModel.getRowCount() ) {
//      expandRow( row );
//      row++;
//    }
//    repaint();
  }

  /**
   * Collapses all expanded rows.
   */
  public void collapseAll() {
//    clearSelection();
//    int row = treeTableModel.getRowCount() - 1;
//    while( row >= 0 ) {
//      collapseRow( row );
//      row--;
//    }
//    repaint();
  }

  /**
   * Ensures a preferred size per column.
   */
  private void fixSizes() {
    for( int i = 0; i < treeTableModel.getColumnCount(); i++ ) {
      fixSize(i);
    }
  }
  
  /**
   * Ensures a preferred size for a certain column.
   * 
   * @param col   The index of the column.
   */
  private void fixSize( int col ) {
    TableColumnModel tcm    = getColumnModel();
    while( tcm.getColumnCount() < (col + 1) ) {
      tcm.addColumn( new TableColumn() );
    }
    TableColumn      column = tcm.getColumn( col );
    int              size   = getSize( col );
    column.setMinWidth( size );
    column.setPreferredWidth( size );
    column.setWidth( size );
  }

  /**
   * Returns the width for the supplied column.
   * 
   * @param col   The index of the column.
   * 
   * @return   The maximum preferred width.
   */
  private int getSize( int col ) {
    int result = 0;
    for( int i = 0; i < treeTableModel.getRowCount(); i++ ) {
      TableCellRenderer tcr  = getCellRenderer( i, col );
      Component         comp = tcr.getTableCellRendererComponent( this, treeTableModel.getValueAt( i, col ), false, false, i, 0 );
      Dimension         dim  = comp.getPreferredSize ();
      result                 = Math.max( result, dim.width );
    }
    return result + 20; // 20 for a little it more space
  }

  @Override
  public void setRowHeight( int height ) {
    super.setRowHeight( height );
    if( renderer != null ) {
      renderer.setRowHeight( height );
    }
  }

  /**
   * Changes the renderer for this tree table instance.
   * 
   * @param cellrenderer   The new renderer. Maybe <code>null</code>.
   */
  public void setTreeTableCellRenderer( TreeTableCellRenderer cellrenderer ) {
    renderer.setTreeTableCellRenderer( cellrenderer );
  }

  /**
   * Expands/Collapses a row depending on it's current state.
   * 
   * @param row   The row index which shall be toggled.
   */
  public void toggleRow( int row ) {
    if( renderer.isCollapsed( row ) ) {
      expandRow( row );
    } else if( renderer.isExpanded( row ) ) {
      collapseRow( row );
    }
  }

  /**
   * This listener is actually responsible to realize the behaviour of the tree table.
   * 
   * @author daniel.kasmeroglu@kasisoft.net
   */
  private static class LocalListener extends MouseAdapter implements ListSelectionListener {

    private KTreeTable   pthis;

    public LocalListener( KTreeTable ref ) {
      pthis = ref;
    }

    @Override
    public void mouseClicked( MouseEvent evt ) {

      if( evt.getButton() == MouseEvent.BUTTON1 ) {

        int row = pthis.rowAtPoint( evt.getPoint() );
        if( row >= 0 ) {

          Rectangle rect = pthis.renderer.getRowBounds( row );
          if( rect != null ) {
            
            int       max  = rect.x;

            // hardcoded, so a bit ugly but apropriate for most cases.
            int       min  = Math.max( max - 30, 0 );

            if( max == 0 ) {
              min = 0;
              max = 30;
            }

            if( (evt.getX() >= min) && (evt.getX() <= max) ) {
              pthis.toggleRow( row );
            }

          }

        }

      }

    }

    @Override
    public void valueChanged( ListSelectionEvent evt ) {
      int[] selectedrows = pthis.getSelectedRows();
      if( selectedrows != null ) {
        pthis.renderer.clearSelection();
        for( int i = 0; i < selectedrows.length; i++ ) {
          pthis.renderer.addSelectionInterval( selectedrows[i], selectedrows[i] );
        }
      }
    }

  } /* ENDCLASS */

  /**
   * @author daniel.kasmeroglu@kasisoft.net
   */
  @FieldDefaults(level = AccessLevel.PRIVATE)
  private static class TreeTableRenderer extends JTree implements TableCellRenderer, TreeCellRenderer {

    int                     visiblerow;
    KTreeTable              treetable;
    TreeTableCellRenderer   renderer;

    /**
     * Initializes this renderer using the supplied owner.
     * 
     * @param ktreetable   The owning tree table. Not <code>null</code>.
     */
    public TreeTableRenderer( KTreeTable ktreetable ) {
      visiblerow      = 0;
      renderer        = DEFAULT_RENDERER;
      treetable       = ktreetable;
      setTreeTableModel( ktreetable.getTreeTableModel() );
      setCellRenderer( this );
    }

    /**
     * Changes the current treetable cell renderer.
     * 
     * @param ttc   The current treetable cell renderer.
     */
    public void setTreeTableCellRenderer( TreeTableCellRenderer ttc ) {
      renderer  = ttc;
      if( renderer == null ) {
        renderer = DEFAULT_RENDERER;
      }
    }

    /**
     * Applies a new model.
     * 
     * @param model   The new model.
     */
    public void setTreeTableModel( TreeTableModel model ) {
      setModel( model );
      setRootVisible( ! model.isIgnoreRoot() );
      SwingFunctions.expandTree( this );
    }

    @Override
    public void setBounds( int x, int y, int w, int h ) {
      super.setBounds( x, 0, w, treetable.getHeight() );
    }

    @Override
    public void paint( Graphics g ) {
      // row heights may be variable so we should take care of this fact
      int height = 0;
      for( int i = 0; i < visiblerow; i++ ) {
        height += treetable.getRowHeight(i);
      }
      setRowHeight( treetable.getRowHeight( visiblerow ) );
      g.translate( 0, -height );
      super.paint(g);
    }

    @Override
    public Component getTableCellRendererComponent( JTable table, Object value , boolean isselected, boolean hasfocus, int row, int column ) {
      if( column > 0 ) {
        return
          renderer.getTreeTableCellRendererComponent(
            treetable, (TreeTableNode) treetable.getValueAt( row, 0 ), value, isselected, hasfocus, row, column 
          );
      } else {
        visiblerow  = row;
        if( isselected ) {
          setForeground( table.getSelectionForeground() );
          setBackground( table.getSelectionBackground() );
        } else {
          setForeground( table.getForeground() );
          setBackground( table.getBackground() );
        }
        return this;
      }
    }
    
    @Override
    public Component getTreeCellRendererComponent( JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasfocus ) {
      TreeTableNode node = (TreeTableNode) value;
      return renderer.getTreeTableCellRendererComponent( treetable, node, node.getNodeValue(), selected, hasfocus, row );
    }

  } /* ENDCLASS */

} /* ENDCLASS */