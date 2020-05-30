package com.kasisoft.libs.common.old.ui.component.treetable;

import java.awt.*;

/**
 * Api to be used while renderering tree table cells.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface TreeTableCellRenderer {

  /**
   * Returns a component used to render a certain value.
   * 
   * @param treetable   The owning tree table. Not <code>null</code>.
   * @param treenode    The currently used node. Not <code>null</code>.
   * @param value       The value to be rendered. Maybe <code>null</code>.
   * @param selected    <code>true</code> <=> Render selection state.
   * @param hasfocus    <code>true</code> <=> Render focused state.
   * @param row         The row the node belongs to.
   * @param column      The column the value belongs to.
   * 
   * @return   The component to be rendered. Not <code>null</code>.
   */
  Component getTreeTableCellRendererComponent( KTreeTable treetable, TreeTableNode treenode, Object value, boolean selected, boolean hasfocus, int row, int column );

  /**
   * Returns a component used to render a certain value provided by the node.
   * 
   * @param treetable   The owning tree table. Not <code>null</code>.
   * @param treenode    The currently used node. Not <code>null</code>.
   * @param value       The value to be rendered. Maybe <code>null</code>.
   * @param selected    <code>true</code> <=> Render selection state.
   * @param hasfocus    <code>true</code> <=> Render focused state.
   * @param row         The row the node belongs to.
   * 
   * @return   The component to be rendered. Not <code>null</code>.
   */
  Component getTreeTableCellRendererComponent( KTreeTable treetable, TreeTableNode treenode, Object value, boolean selected, boolean hasfocus, int row );

} /* ENDINTERFACE */