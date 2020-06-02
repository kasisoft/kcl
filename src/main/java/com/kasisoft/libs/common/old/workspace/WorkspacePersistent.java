package com.kasisoft.libs.common.old.workspace;

/**
 * Each implementor allows to store infos into the workspace.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface WorkspacePersistent {

  void loadPersistentSettings();
  
  void savePersistentSettings();
  
} /* ENDINTERFACE */