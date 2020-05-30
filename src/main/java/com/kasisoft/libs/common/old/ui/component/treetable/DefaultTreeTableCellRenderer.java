package com.kasisoft.libs.common.old.ui.component.treetable;

import lombok.experimental.*;

import lombok.*;

import javax.swing.table.*;

import javax.swing.tree.*;

import javax.swing.*;

import java.awt.*;

/**
 * Default implementation of the TreeTableCellRenderer which essentially relies on the DefaultTreeCellRenderer
 * and the DefaultTableCellRenderer.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefaultTreeTableCellRenderer implements TreeTableCellRenderer {

  private static final JLabel DUMMY = new JLabel();

  DefaultTreeCellRenderer    treecellrenderer;
  DefaultTableCellRenderer   tablecellrenderer;

  /**
   * Initializes this default implementation.
   */
  public DefaultTreeTableCellRenderer() {
    treecellrenderer  = new DefaultTreeCellRenderer();
    tablecellrenderer = new DefaultTableCellRenderer();
  }

  /**
   * @see DefaultTreeCellRenderer#getDefaultLeafIcon()
   */
  public Icon getDefaultLeafIcon() {
    return treecellrenderer.getDefaultLeafIcon();
  }

  /**
   * @see DefaultTreeCellRenderer#getDefaultOpenIcon()
   */
  public Icon getDefaultOpenIcon() {
    return treecellrenderer.getDefaultOpenIcon();
  }
  
  /**
   * @see DefaultTreeCellRenderer#getDefaultClosedIcon()
   */
  public Icon getDefaultClosedIcon() {
    return treecellrenderer.getDefaultClosedIcon();
  }

  /**
   * @see DefaultTreeCellRenderer#getLeafIcon()
   */
  public Icon getLeafIcon() {
    return treecellrenderer.getLeafIcon();
  }
  
  /**
   * @see DefaultTreeCellRenderer#getOpenIcon()
   */
  public Icon getOpenIcon() {
    return treecellrenderer.getOpenIcon();
  }

  /**
   * @see DefaultTreeCellRenderer#getClosedIcon()
   */
  public Icon getClosedIcon() {
    return treecellrenderer.getClosedIcon();
  }

  /**
   * @see DefaultTreeCellRenderer#setLeafIcon(Icon)
   */
  public void setLeafIcon( Icon icon ) {
    treecellrenderer.setLeafIcon( icon );
  }

  /**
   * @see DefaultTreeCellRenderer#setOpenIcon(Icon)
   */
  public void setOpenIcon( Icon icon ) {
    treecellrenderer.setOpenIcon( icon );
  }

  /**
   * @see DefaultTreeCellRenderer#setClosedIcon(Icon)
   */
  public void setClosedIcon( Icon icon ) {
    treecellrenderer.setClosedIcon( icon );
  }

  @Override
  public Component getTreeTableCellRendererComponent( KTreeTable treetable, TreeTableNode treenode, Object value, boolean selected, boolean hasfocus, int row ) {
    return getTreeTableCellRendererComponent( treetable, treenode, value, selected, hasfocus, row, -1 );
  }
  
  @Override
  public Component getTreeTableCellRendererComponent( KTreeTable treetable, TreeTableNode treenode, Object value, boolean selected, boolean hasfocus, int row, int column ) {
    JComponent result = null;
    if( column == -1 ) {
      // we're rendering the column for the node
      JTree tree  = treetable.getJTree();
      if( tree == null ) {
        return DUMMY;
      }
      result = (JComponent) treecellrenderer.getTreeCellRendererComponent( tree, value, selected, tree.isExpanded( row ), treenode.isLeaf(), row, hasfocus );
    } else {
      result = (JComponent) tablecellrenderer.getTableCellRendererComponent( treetable, value, selected, hasfocus, row, column );
    }
    return result;
  }

} /* ENDCLASS */