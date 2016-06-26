package com.kasisoft.libs.common.ui.component.treetable;

import javax.swing.tree.*;

/**
 * TreeNode extension which allows to be used within the tree table.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface TreeTableNode extends TreeNode {

  /**
   * Returns the number of available columns (excluding this node).
   * 
   * @return   The number of available columns.
   */
  int getColumnCount();
  
  /**
   * Returns <code>true</code> if the supplied column can be edited.
   * 
   * @param column   The column in question.
   * 
   * @return   <code>true</code> <=> The supplied column can be edited.
   */
  boolean isColumnEditable( int column );
  
  /**
   * Returns <code>true</code> if the node value can be edited.
   * 
   * @return   <code>true</code> <=> The node value can be edited.
   */
  boolean isNodeEditable();
  
  /**
   * Changes the value of column.
   * 
   * @param column   The column that shall be altered.
   * @param value    The new value. Maybe <code>null</code>.
   */
  void setValue( int column, Object value );
  
  /**
   * Returns the value from the supplied column.
   * 
   * @param column   The index for the desired value.
   * 
   * @return   The old value. Maybe <code>null</code>.
   */
  Object getValue( int column );
  
  /**
   * Returns the current node value.
   * 
   * @return   The current node value. Maybe <code>null</code>.
   */
  Object getNodeValue();
  
} /* ENDINTERFACE */
