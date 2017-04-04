package com.kasisoft.libs.common.ui.component.treetable;

import lombok.*;

import javax.swing.event.*;

import javax.swing.tree.*;

import java.util.function.*;

import java.util.*;

/**
 * Default implementation of a TreeTableModel. This model can be used by a JTree or a an JTable instance.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class DefaultTreeTableModel extends DefaultTreeModel implements TreeTableModel {

  private static final TreeTableNode EMPTY = new DefaultTreeTableNode( "EMPTY", new Object[0] );
  
  Map<TreeTableNode, RowRecord>   mapping;
  int                             columnCount;
  List<RowRecord>                 rows;
  EventListenerList               tableListeners;
  boolean                         ignoreRoot;
  Function<Integer, String>       columnNames;
  Function<Integer, Class<?>>     columnClasses;
  
  /**
   * Initializes this model using an empty tree.
   */
  public DefaultTreeTableModel() {
    this( null, false, null, null );
  }

  /**
   * Initializes this model using an empty tree.
   * 
   * @param noRoot   <code>true</code> <=> Don't use root node as part of the data.
   */
  public DefaultTreeTableModel( boolean noRoot ) {
    this( null, noRoot, null, null );
  }

  /**
   * Initializes this model using the supplied root node.
   * 
   * @param root   The root node providing the structure and it's data. Not <code>null</code>.
   */
  public DefaultTreeTableModel( TreeTableNode root ) {
    this( root, false, null, null );
  }
  
  /**
   * Initializes this model using the supplied root node.
   * 
   * @param root          The root node providing the structure and it's data. Not <code>null</code>.
   * @param noRoot        <code>true</code> <=> Don't use root node as part of the data.
   * @param colNames      A mapping for column indexes to column names. If <code>null</code> it will follow a pattern <code>col-idx</code>.
   * @param colClasses    A mapping for column indexes to column types. If <code>null</code> the default type Object is being used.
   */
  public DefaultTreeTableModel( TreeTableNode root, boolean noRoot, Function<Integer, String> colNames, Function<Integer, Class<?>> colClasses ) {
    super( root != null ? root : EMPTY, false );
    mapping        = new HashMap<>();
    rows           = new ArrayList<>();
    tableListeners = new EventListenerList();
    columnNames    = colNames != null ? colNames : this::columnNamesDefault;
    columnClasses  = colClasses != null ? colClasses : this::columnClassesDefault;
    ignoreRoot     = noRoot;
    setup();
  }
  
  /**
   * Process all nodes and prepare a record for the display within a table.
   */
  private void setup() {
    List<TreeTableNode> nodes = list( (TreeTableNode) getRoot() );
    nodes.forEach( $ -> mapping.put( $, new RowRecord($) ) );
    nodes.stream().map( mapping::get ).forEach( rows::add );
    columnCount = nodes.parallelStream().map( $ -> $.getColumnCount() ).reduce( 0, Math::max );
  }
  
  /**
   * List all nodes of the supplied parent.
   * 
   * @param parent   The parental node which children shall be listed. This instance will be included. 
   *                 Not <code>null</code>.
   *                 
   * @return   A list of children. Not <code>null</code>.
   */
  private List<TreeTableNode> list( TreeTableNode parent ) {
    List<TreeTableNode> result = new ArrayList<>();
    list( result, parent );
    return result;
  }
  
  /**
   * List all nodes of the supplied parent.
   *
   * @param receiver   The list that will be filled. Not <code>null</code>.
   * @param parent     The parental node which children shall be listed. This instance will be included. 
   *                   Not <code>null</code>.
   */
  private void list( List<TreeTableNode> receiver, TreeTableNode parent ) {
    receiver.add( parent );
    for( int i = 0; i < parent.getChildCount(); i++ ) {
      list( receiver, (TreeTableNode) parent.getChildAt(i) );
    }
  }
  
  /**
   * Default implementation for the name mapping.
   * 
   * @param index   The index of the column.
   * 
   * @return   The name to be used for the supplied column index.
   */
  private String columnNamesDefault( int index ) {
    return String.format( "col-%d", index );
  }
  
  /**
   * Default implementation for the type mapping.
   * 
   * @param index   The index of the column.
   * 
   * @return   The type to be used for the supplied column index.
   */
  private Class<?> columnClassesDefault( int index ) {
    return Object.class;
  }

  @Override
  public void reload( TreeNode node ) {
    // disabled as we need to make sure that TreeTableNode instances are being used.
  }

  /**
   * Reloads the content of the supplied node.
   * 
   * @param node   The node which content shall be reloaded. Not <code>null</code>.
   */
  public void reload( @NonNull TreeTableNode node ) {
    super.reload( node );
  }

  @Override
  public int getColumnCount() {
    return columnCount + 1;
  }

  @Override
  public int getRowCount() {
    return rows.size();
  }

  @Override
  public String getColumnName( int column ) {
    return columnNames.apply( column );
  }

  @Override
  public Class<?> getColumnClass( int column ) {
    return columnClasses.apply( column );
  }

  @Override
  public boolean isCellEditable( int row, int column ) {
    TreeTableNode node = getNode( row );
    if( column == 0 ) {
      return node.isNodeEditable();
    } else {
      return node.isColumnEditable( column - 1 );
    }
  }

  @Override
  public Object getValueAt( int row, int column ) {
    TreeTableNode node = getNode( row );
    if( column == 0 ) {
      return node.getNodeValue();
    } else {
      return node.getValue( column - 1 );
    }
  }

  @Override
  public void setValueAt( Object value, int row, int column ) {
    TreeTableNode node = getNode( row );
    if( column == 0 ) {
      if( value instanceof TreeTableNode ) {
        TreeTableNode newnode = (TreeTableNode) value;
        if( newnode == node ) {
          reload( node );
        } else {
          /** @todo   The old node as well as it's children must be replaced. */
        }
      }
    } else {
      node.setValue( column - 1, value );
    }
  }

  @Override
  public TreeTableNode getNode( int row ) {
    RowRecord record = rows.get( row );
    return record.getNode();
  }
  
  @Override
  public void addTableModelListener( TableModelListener l ) {
    tableListeners.add( TableModelListener.class, l );
    
  }

  @Override
  public void removeTableModelListener( TableModelListener l ) {
    tableListeners.remove( TableModelListener.class, l );
  }
  
  /**
   * Notifies all listeners that rows in the range <code>[firstRow, lastRow]</code>, inclusive, have been inserted.
   *
   * @param  firstRow   The first row.
   * @param  lastRow    The last row.
   *
   * @see TableModelEvent
   * @see EventListenerList
   */
  private void fireTableRowsInserted(int firstRow, int lastRow) {
    fireTableChanged( new TableModelEvent( this, firstRow, lastRow, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT ) );
  }

  /**
   * Notifies all listeners that rows in the range <code>[firstRow, lastRow]</code>, inclusive, have been deleted.
   *
   * @param firstRow   The first row.
   * @param lastRow    The last row.
   *
   * @see TableModelEvent
   * @see EventListenerList
   */
  private void fireTableRowsDeleted(int firstRow, int lastRow) {
    fireTableChanged( new TableModelEvent( this, firstRow, lastRow, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE ) );
  }

  /**
   * Forwards the given notification event to all <code>TableModelListeners</code> that registered themselves 
   * as listeners for this table model.
   *
   * @param evt  The event to be forwarded
   *
   * @see #addTableModelListener
   * @see TableModelEvent
   * @see EventListenerList
   */
  private void fireTableChanged( TableModelEvent evt ) {
    Object[] listeners = listenerList.getListenerList();
    for( int i = listeners.length - 2, j = listeners.length - 1; i >= 0; i -= 2, j -= 2 ) {
      if( listeners[i] == TableModelListener.class ) {
        ((TableModelListener) listeners[j]).tableChanged( evt );
      }
    }
  }
  
  @Override
  public void collapse( int row ) {
    RowRecord record = rows.get( row );
    if( record.isExpanded() ) {
      record.collapse( this, row );
    }
  }

  @Override
  public void expand( int row ) {
    RowRecord record = rows.get( row );
    if( record.isCollapsed() ) {
      record.expand( this, row );
    }
  }

  @Override
  public boolean isCollapsed( int row ) {
    return rows.get( row ).isCollapsed();
  }

  @Override
  public boolean isExpanded( int row ) {
    return rows.get( row ).isExpanded();
  }
  
  @Override
  public boolean isIgnoreRoot() {
    return ignoreRoot;
  }
  
  /**
   * Each row record simply contains the node, it's state and if collapsed the row records of the nodes children. 
   * 
   * @author daniel.kasmeroglu@kasisoft.net
   */
  private static class RowRecord {
    
    TreeTableNode     expanded      = null;
    TreeTableNode     collapsed     = null;
    int               collapseCount = 0;
    List<RowRecord>   rows          = new ArrayList<>();
    
    public RowRecord( TreeTableNode node ) {
      expanded = node;
    }
    
    public TreeTableNode getNode() {
      if( expanded != null ) {
        return expanded;
      } else {
        return collapsed;
      }
    }
    
    public boolean isExpanded() {
      return expanded != null;
    }
    
    public boolean isCollapsed() {
      return collapsed != null;
    }
    
    public boolean expand( DefaultTreeTableModel owner, int row ) {
      boolean result = false;
      collapseCount--;
      if( collapseCount == 0 ) {
        expanded  = collapsed;
        collapsed = null;
        result    = true;
        if( !rows.isEmpty() ) {
          owner.rows.addAll( row + 1, rows );
          rows.clear();
          owner.fireTableRowsInserted( row + 1, row + 1 + rows.size() - 1 );
        }
      }
      return result;
    }
    
    public boolean collapse( DefaultTreeTableModel owner, int row ) {
      boolean result = false;
      if( collapseCount == 0 ) {
        collapsed = expanded;
        expanded  = null;
        result    = true;
        List<TreeTableNode> nodes = owner.list( collapsed );
        nodes.remove(0); // collapsed
        nodes.forEach( $ -> rows.add( owner.mapping.get($) ) );
        rows.forEach( owner.rows::remove );
        owner.fireTableRowsDeleted( row + 1, row + 1 + nodes.size() - 1 );
      }
      collapseCount++;
      return result;
    }
    
  } /* ENDCLASS */

} /* ENDCLASS */
