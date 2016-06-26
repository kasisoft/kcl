package com.kasisoft.libs.common.ui.component.treetable;

import javax.swing.table.*;

import javax.swing.tree.*;

/**
 * A combination of TreeModel and TableModle.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface TreeTableModel extends TreeModel, TableModel {
  
  /**
   * Returns the node associated with the supplied row.
   * 
   * @param row   The row in question.
   * 
   * @return   The node associated with the supplied row. Maybe <code>null</code>.
   */
  TreeTableNode getNode( int row );
  
  /**
   * Collapses the supplied row.
   * 
   * @param row   The index of the row which shall be collapsed.
   */
  void collapse( int row );
  
  /**
   * Expands the supplied row.
   * 
   * @param row   The index of the row which shall be expanded.
   */
  void expand( int row );
  
  /**
   * Returns <code>true</code> if the supplied row is collapsed.
   * 
   * @param row   The index of the row to be tested.
   * 
   * @return   <code>true</code> <=> The supplied row is collapsed.
   */
  boolean isCollapsed( int row );
  
  /**
   * Returns <code>true</code> if the supplied row is expanded.
   * 
   * @param row   The index of the row to be tested.
   * 
   * @return   <code>true</code> <=> The supplied row is expanded.
   */
  boolean isExpanded( int row );
  
  /**
   * Returns <code>true</code> if the root is part of the data.
   * 
   * @return   <code>true</code> <=> The root is part of the data.
   */
  boolean isIgnoreRoot();
  
} /* ENDINTERFACE */
