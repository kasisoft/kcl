package com.kasisoft.libs.common.ui;

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
public class KPathChooser extends JFileChooser {

  SimpleProperty<Path>   property;
  
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
    wsprop = StringFunctions.cleanup( wsprop );
    if( wsprop != null ) {
      property = new SimpleProperty<>( wsprop, new NioPathAdapter() );
    }
    resetCurrent();
  }

  @Override
  public int showDialog( Component parent, String approvebuttontext ) {
    int result = super.showDialog( parent, approvebuttontext );
    if( result != JFileChooser.APPROVE_OPTION ) {
      resetCurrent();
    } else {
      if( property != null ) {
        File file = getSelectedFile();
        if( file != null ) {
          property.setValue( Workspace.getInstance().getProperties(), file.toPath() );
        }
      }
    }
    return result;
  }

  @Override
  public Dimension getMaximumSize() {
    return getPreferredSize();
  }

  /**
   * This function resets the current filechooser and adjusts the settings according to the current property 
   * value.
   */
  private void resetCurrent() {
    Path location = null;
    if( property != null ) {
      location = property.getValue( Workspace.getInstance().getProperties() );
    }
    if( (location != null) && Files.exists( location ) ) {
      Path   dir  = null;
      String name = null;
      if( Files.isRegularFile( location ) ) {
        dir  = location.getParent();
        name = location.getFileName().toString();
      } else if( Files.isDirectory( location ) ) {
        dir  = location;
      }
      if( dir != null ) {
        setCurrentDirectory( dir.toFile() );
      }
      if( name != null ) {
        setName( name );
      }
    }
  }

} /* ENDCLASS */