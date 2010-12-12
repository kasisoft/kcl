/**
 * Name........: Workspace
 * Description.: The Workspace allows to store various configuration information during the runtime.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.workspace;

import com.kasisoft.lgpl.libs.common.constants.*;

import com.kasisoft.lgpl.libs.common.util.*;

import com.kasisoft.lgpl.libs.common.base.*;
import com.kasisoft.lgpl.tools.diagnostic.*;

import java.io.*;

/**
 * The Workspace allows to store various configuration information during the runtime.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public final class Workspace {

  private static Workspace       instance = null;
  
  private File            settingsfile;
  private ExtProperties   properties;
  private boolean         isnew;
  
  /**
   * Initialises this workspace used for temporary savings.
   */
  private Workspace() {
    settingsfile  = null;
    isnew         = false;
    properties    = new ExtProperties();
  }
  
  /**
   * Initialises this workspace using the supplied settings File for persistency.
   * 
   * @param settings   The File where the settings have to be stored. Must be a writable File.
   *                   Not <code>null</code>.
   * 
   * @throws FailureException   Loading failed for some reason.
   */
  private Workspace( @KFile(name="settings", right=KFile.Right.Write) File settings ) throws FailureException {
    this();
    settingsfile = settings;
    isnew        = true;
    if( settingsfile.isFile() ) {
      isnew = false;
      loadSettings();
    }
    Runtime.getRuntime().addShutdownHook( new ShutdownWorkspace() );
  }
  
  /**
   * Returns <code>true</code> in case the configuration file had to be created while setting
   * up this instance.
   * 
   * @return   <code>true</code> <=> The configuration file had to be created.
   */
  public synchronized boolean isNew() {
    return isnew;
  }

  /**
   * Stores the current settings.
   * 
   * @throws FailureException   Saving the settings failed.
   */
  public synchronized void saveSettings() throws FailureException {
    properties.store( settingsfile, Encoding.UTF8 );
    isnew  = false;
  }
  
  /**
   * Loads the current settings.
   * 
   * @throws FailureException   Loading the settings failed.
   */
  private void loadSettings() throws FailureException {
    properties.load( settingsfile, Encoding.UTF8 );
  }

  /**
   * Returns a Workspace instance used for the current runtime. If the property
   * {@link CommonLibraryConstants#PROP_APPLICATIONFILE} has not been set, it will return
   * a dummy instance always relying on default values.
   * 
   * @return   A Workspace instance used for the current runtime. Not <code>null</code>.
   * 
   * @throws FailureException   Loading existing settings failed for some reason.
   */
  public static final synchronized Workspace getInstance() {
    if( instance == null ) {
      File appfile = CommonProperty.Application.getValue();
      if( appfile == null ) {
        // no settings available, so we're providing a dummy instance instead
        instance = new Workspace();
      } else {
        instance  = new Workspace( appfile );
      }
    }
    return instance;
  }
  
  /**
   * Shutdown hook for this Workspace instance.
   */
  private class ShutdownWorkspace extends Thread {
    
    /**
     * {@inheritDoc}
     */
    public void run() {
      Workspace.this.saveSettings();
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */