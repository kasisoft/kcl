package com.kasisoft.libs.common.ui;

/**
 * Each implementor allows to select a certain style depending on some data.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface StyleSelector<T> {

  /**
   * Returns the name of a style for the supplied data.
   * 
   * @param data   The data which style is desired. Maybe <code>null</code>.
   * 
   * @return   The name of a style or <code>null</code>.
   */
  String selectStyle( T data );
  
} /* ENDINTERFACE */
