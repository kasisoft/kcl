package com.kasisoft.libs.common.workspace;

/**
 * Each implementor allows to store infos into the workspace.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface WorkspacePersistent {

  /**
   * Returns the property used to persist the data.
   * 
   * @return   The property used to persist the data. Maybe <code>null</code>.
   */
  String getPersistentProperty();
  
  void loadPersistentSettings();
  
  void savePersistentSettings();
  
} /* ENDINTERFACE */
