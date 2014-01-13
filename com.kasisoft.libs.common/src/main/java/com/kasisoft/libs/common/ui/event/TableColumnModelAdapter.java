/**
 * Name........: TableColumnModelAdapter
 * Description.: Default implementation of TableColumnModelListener. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.ui.event;

import javax.swing.event.*;

import lombok.*;

/**
 * Default implementation of TableColumnModelListener.
 */
public class TableColumnModelAdapter implements TableColumnModelListener {

  @Override
  public void columnAdded( @NonNull TableColumnModelEvent evt ) {
  }

  @Override
  public void columnRemoved( @NonNull TableColumnModelEvent evt ) {
  }

  @Override
  public void columnMoved( @NonNull TableColumnModelEvent evt ) {
  }

  @Override
  public void columnMarginChanged( @NonNull ChangeEvent evt ) {
  }

  @Override
  public void columnSelectionChanged( @NonNull ListSelectionEvent evt ) {
  }

} /* ENDCLASS */
