package com.kasisoft.libs.common.ui.component;

import com.kasisoft.libs.common.workspace.*;

import com.kasisoft.libs.common.config.*;

import com.kasisoft.libs.common.xml.adapters.*;

import com.kasisoft.libs.common.ui.layout.*;

import com.kasisoft.libs.common.ui.event.*;

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.text.*;

import lombok.experimental.*;

import lombok.*;

import javax.swing.text.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import java.util.function.*;

import java.awt.event.*;

import java.awt.*;

import java.nio.file.*;

import java.io.*;

/**
 * Component which is used to enter a path location.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KPathPanel extends JPanel implements WorkspacePersistent {

  PathChangeEventDispatcher   dispatcher;
  JButton                     select;
  KPathChooser                filechooser;
  KTextField                  textfield;
  LocalBehaviour              localbehaviour;
  Path                        path;
  Color                       foreground;
  Color                       errorColor;
  FileFilter                  filefilter;
  boolean                     saving;
  Path                        lastvalid;
  
  String                      property;
  SimpleProperty<Path>        pathProperty;
  
  
  /**
   * Initialises this panel used to enter a Path for opening.
   */
  public KPathPanel() {
    this( null, null );
  }

  /**
   * Initialises this panel used to enter a Path for opening.
   * 
   * @param wsprop   A property used to store the location of the file within the workspace.
   */
  public KPathPanel( String wsprop ) {
    this( wsprop, null );
  }

  /**
   * Initialises this panel used to enter a Path for opening.
   * 
   * @param path   The Path which has to be used initially. Maybe <code>null</code>.
   */
  public KPathPanel( Path path ) {
    this( null, path );
  }
  
  /**
   * Initialises this panel used to enter a Path for opening.
   * 
   * @param wsprop   A property used to store the location of the file within the workspace.
   * @param path     The Path which has to be used initially. Maybe <code>null</code>.
   */
  public KPathPanel( String wsprop, Path path ) {
    super( new SmartGridLayout( 1, 2, 3, 3 ) );
    init( wsprop );
    setup();
    if( path != null ) {
      setPath( path );
    }
  }
  
  /**
   * Initialises this widget.
   * 
   * @param wsprop   A property used to store the location of the path within the workspace.
   */
  private void init( String wsprop ) {
    dispatcher     = new PathChangeEventDispatcher();
    lastvalid      = null;
    saving         = false;
    path           = null;
    filefilter     = null;
    errorColor     = Color.red;
    localbehaviour = new LocalBehaviour();
    filechooser    = new KPathChooser();
    select         = new JButton( "..." );
    textfield      = new KTextField();
    foreground     = textfield.getForeground();
    textfield.setDocument( localbehaviour );
    property       = StringFunctions.cleanup( wsprop );
    if( property != null ) {
      pathProperty = new SimpleProperty<>( property, new NioPathAdapter() );
    }
  }

  /**
   * Arranges and composes it's elements.
   */
  private void setup() {
    add( textfield , SmartGridLayout.FIXMINHEIGHT );
    add( select    , SmartGridLayout.FIXMINSIZE   );
    select.addActionListener( localbehaviour );
  }

  /**
   * Invokes the file selection dialog.
   */
  private void selectFile() {
    int result = -1;
    if( saving ) {
      result = filechooser.showSaveDialog( this );
    } else {
      result = filechooser.showOpenDialog( this );
    }
    if( result == JFileChooser.APPROVE_OPTION ) {
      File file = filechooser.getSelectedFile();
      if( filechooser.getFileFilter() instanceof BasicFileFilter ) {
        BasicFileFilter filter = (BasicFileFilter) filechooser.getFileFilter();
        file                   = filter.fixFile( file );
      }
      setFileFromFileChooser( file.toPath() );
    }
  }

  /**
   * Registers the supplied listener to become informed upon changes.
   * 
   * @param listener   The listener which becomes informed upon changes. Not <code>null</code>.
   */
  public void addPathChangeListener( @NonNull PathChangeListener listener ) {
    dispatcher.addListener( listener );
  }
  
  /**
   * Prevents further notification for the supplied listener.
   * 
   * @param listener   The listener that won't be notified anymore. Not <code>null</code>.
   */
  public void removePathChangeListener( @NonNull PathChangeListener listener ) {
    dispatcher.removeListener( listener );
  }
  
  private void setPathImpl( Path newpath ) {
    path = newpath;
    if( path != null ) {
      if( Files.isDirectory( path ) ) {
        filechooser.setCurrentDirectory( path.toFile() );
      } else {
        filechooser.setCurrentDirectory( path.getParent().toAbsolutePath().toFile() );
      }
      filechooser.setSelectedFile( path.toFile() );
    }
  }
  
  /**
   * Changes the current File used for this panel.
   * 
   * @param newpath   The new File instance. Maybe <code>null</code>.
   */
  public void setPath( Path newpath ) {
    setPathImpl( newpath );
    synchronized( this ) {
      // make sure that only the text will be altered
      localbehaviour.setEnabled( false );
      if( path == null ) {
        textfield.setText( "" );
      } else {
        textfield.setText( path.toString() );
      }
      localbehaviour.setEnabled( true );
    }
    checkValidity();
  }
  
  /**
   * Adjusts the data according to a textual change.
   * 
   * @param newfile   The new textual path of the File.
   */
  private void setFileFromTextfield( String newfile ) {
    if( newfile != null ) {
      path = Paths.get( newfile );
    } else {
      path = null;
    }
    setPathImpl( path );
    checkValidity();
  }
  
  /**
   * Adjusts the data according to a new File selection.
   * 
   * @param newfile   The newly selected File instance.
   */
  private void setFileFromFileChooser( Path newpath ) {
    path = newpath;
    if( path == null ) {
      textfield.setText( "" );
    } else {
      textfield.setText( path.toString() );
    }
    checkValidity();
  }
  
  /**
   * Returns the currently selected File instance. This method returns <code>null</code> if no path has been 
   * selected or this panel is used to select a Path for opening while the Path doesn't exist.
   * 
   * @return   The currently selected File instance.
   */
  public Path getPath() {
    if( ! saving ) {
      if( (path != null) && (!Files.exists( path )) ) {
        return null;
      }
    }
    return path;
  }
  
  /**
   * Enables this panel to use a path for saving.
   * 
   * @param enablesaving   <code>true</code> <=> Use this panel for a saving path.
   */
  public void setSaving( boolean enablesaving ) {
    saving = enablesaving;
  }
  
  /**
   * Returns <code>true</code> if the panel shall be used to setup a saving path.
   * 
   * @return   <code>true</code> <=> The panel shall be used to setup a saving path.
   */
  public boolean isSaving() {
    return saving;
  }
  
  /**
   * Changes the current FileFilter to be used.
   * 
   * @param filter   The new FileFilter to be used. Not <code>null</code>.
   */
  public void setFileFilter( @NonNull FileFilter filter ) {
    filefilter = filter;
    filechooser.setFileFilter( filter );
  }

  /**
   * Changes the current FileFilter to be used.
   * 
   * @param filter   The new FileFilter to be used. Not <code>null</code>.
   */
  public void setFileFilter( @NonNull Predicate<Path> filter ) {
    filefilter = new FileFilter() {

      @Override
      public boolean accept( File file ) {
        return filter.test( file.toPath() );
      }

      @Override
      public String getDescription() {
        return null;
      }
      
    };
    filechooser.setFileFilter( filefilter );
  }

  /**
   * Changes the file selection mode for the file chooser. Possible values are {@link JFileChooser#FILES_ONLY},
   * {@link JFileChooser#DIRECTORIES_ONLY} or {@value JFileChooser#FILES_AND_DIRECTORIES}.
   * 
   * @param mode   The mode allowing to configure the selectable entries.
   */
  public void setFileSelectionMode( int mode ) {
    filechooser.setFileSelectionMode( mode );
  }
  
  /**
   * Adds the supplied FileFilter as a possible allowed filter criteria.
   * 
   * @param filter   A FileFilter which might be used to select an entry. Not <code>null</code>.
   */
  public void addChoosableFileFilter( FileFilter filter ) {
    filechooser.addChoosableFileFilter( filter );
  }
  
  /**
   * Enables/disables the use of the FileFilter allowing to accept all entries.
   * 
   * @param enable   <code>true</code> <=> Enable the use of a FileFilter which accepts all entries.
   */
  public void setAcceptAll( boolean enable ) {
    filechooser.setAcceptAllFileFilterUsed( enable );
  }
  
  /**
   * Checks the validity of the current input and adjusts the foreground of the displa according to this.
   */
  @SuppressWarnings("null")
  private void checkValidity() {
    Path    path  = getPath();
    boolean valid = path != null;
    if( valid && (! saving) ) {
      if( path == null ) {
        valid = false;
      }
    }
    if( valid && (filefilter != null) ) {
      valid = filefilter.accept( path.toFile() );
    }
    if( valid ) {
      textfield.setForeground( foreground );
      dispatcher.fireEvent( new PathChangeEvent( this, path ) );
      lastvalid = path;
    } else {
      textfield.setForeground( errorColor );
      if( lastvalid != null ) {
        dispatcher.fireEvent( new PathChangeEvent( this, null ) );
      }
      lastvalid = null;
    }
  }

  @Override
  public void loadPersistentSettings() {
    if( property != null ) {
      Path path = pathProperty.getValue( Workspace.getInstance().getProperties() );
      if( path != null ) {
        setPath( path );
      }
    }
  }

  @Override
  public void savePersistentSettings() {
    if( property != null ) {
      pathProperty.setValue( Workspace.getInstance().getProperties(), getPath() );
    }
  }

  /**
   * Implementation of custom behaviour.
   */
  private class LocalBehaviour extends PlainDocument implements ActionListener {

    boolean   enabled = true;
    
    /**
     * Enables/disables the specialised handling of text manipulations.
     * 
     * @param enable   <code>true</code> <=> Enables the specialised handling of text manipulation.
     */
    public void setEnabled( boolean enable ) {
      enabled = true;
    }

    @Override
    public void insertString( int offset, String str, AttributeSet attrs ) throws BadLocationException {
      synchronized( KPathPanel.this ) {
        super.insertString( offset, str, attrs );
        if( enabled ) {
          setFileFromTextfield( textfield.getText() );
        }
      }
    }

    @Override
    public void remove( int offset, int len ) throws BadLocationException {
      synchronized( KPathPanel.this ) {
        super.remove( offset, len );
        if( enabled ) {
          setFileFromTextfield( textfield.getText() );
        }
      }
    }

    @Override
    public void actionPerformed( ActionEvent evt ) {
      selectFile();
    }

  } /* ENDCLASS */

} /* ENDCLASS */
