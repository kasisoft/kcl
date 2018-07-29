package com.kasisoft.libs.common.ui.component;

import com.kasisoft.libs.common.workspace.*;

import com.kasisoft.libs.common.config.*;

import com.kasisoft.libs.common.xml.adapters.*;

import com.kasisoft.libs.common.text.*;

import lombok.experimental.*;

import lombok.*;

import javax.swing.*;

import java.awt.*;

import java.nio.file.*;

import java.io.*;

/**
 * Modified file chooser allowing to store some information within the workspace.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KPathChooser extends JFileChooser implements WorkspacePersistent {

  String                 property;
  SimpleProperty<Path>   propertyDir;
  
  /**
   * Constructs a <code>FileChooser</code> instance.
   */
  public KPathChooser() {
    this( null );
  }
  
  /**
   * Constructs a <code>FileChooser</code> using a given path identified through the supplied property.
   * 
   * @param wsprop   The property which is used to store configuration information. Maybe <code>null</code>.
   */
  public KPathChooser( String wsprop ) {
    super( (File) null );
    property = StringFunctions.cleanup( wsprop );
    if( property != null ) {
      propertyDir = new SimpleProperty<>( String.format( "%s.dir", property ), new NioPathAdapter() );
    }
  }

  @Override
  public int showDialog( Component parent, String approvebuttontext ) {
    return super.showDialog( parent, approvebuttontext );
  }

  @Override
  public Dimension getMaximumSize() {
    return getPreferredSize();
  }

  @Override
  public void loadPersistentSettings() {
    if( property != null ) {
      Path value = propertyDir.getValue( Workspace.getInstance().getProperties() );
      if( value != null ) {
        setCurrentDirectory( value.toFile() );
      }
    }
  }

  @Override
  public void savePersistentSettings() {
    if( property != null ) {
      File dir = getCurrentDirectory();
      if( dir != null ) {
        propertyDir.setValue( Workspace.getInstance().getProperties(), dir.toPath() );
      }
    }
  }

} /* ENDCLASS */