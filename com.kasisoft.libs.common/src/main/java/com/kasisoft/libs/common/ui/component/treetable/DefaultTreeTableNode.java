package com.kasisoft.libs.common.ui.component.treetable;

import lombok.experimental.*;

import lombok.*;

import javax.swing.tree.*;

/**
 * Default implementation of the TreeTableNode.
 *  
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefaultTreeTableNode extends DefaultMutableTreeNode implements TreeTableNode {

  String   text;
  
  /**
   * Initializes this node with a certain name and some data.
   * 
   * @param name       The name of the node. Not <code>null</code>.
   * @param rowdata    The row data. Not <code>null</code>.
   */
  public DefaultTreeTableNode( @NonNull String name, @NonNull Object[] rowdata ) {
    super( rowdata );
    text = name;
  }
  
  /**
   * Returns the row data associated with this node.
   * 
   * @return   The row data associated with this node. Not <code>null</code>.
   */
  public Object[] getRowData() {
    return (Object[]) getUserObject();
  }
  
  @Override
  public int getColumnCount() {
    return ((Object[]) getUserObject()).length;
  }

  @Override
  public boolean isColumnEditable( int column ) {
    return false;
  }

  @Override
  public boolean isNodeEditable() {
    return false;
  }

  @Override
  public Object getValue( int column ) {
    return getRowData()[column];
  }

  @Override
  public void setValue( int column, Object value ) {
    getRowData()[ column ] = value;
  }

  @Override
  public Object getNodeValue() {
    return this;
  }
  
  @Override
  public String toString() {
    return text;
  }

} /* ENDCLASS */
