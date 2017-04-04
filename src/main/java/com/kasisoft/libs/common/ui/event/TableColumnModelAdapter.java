package com.kasisoft.libs.common.ui.event;

import lombok.*;

import javax.swing.event.*;

/**
 * Default implementation of TableColumnModelListener.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
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
