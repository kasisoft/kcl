package com.kasisoft.libs.common.ui.component;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.ui.event.*;

import lombok.experimental.*;

import lombok.*;

import javax.swing.event.*;

import javax.swing.*;

import java.awt.event.*;

import java.awt.*;

/**
 * A JSplitPane like variety that supports more than two parts.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KMultiSplitPane extends JPanel {

  JSplitPane[]            chain;
  JPanel[]                panels;
  Component[]             components;
  ChangeEventDispatcher   changeeventdispatcher;
  
  /**
   * Initialises this instance without continuous layouting.
   * 
   * @param orientation   The orientation for the contained fields. Not <code>null</code>.
   * @param count         The number of fields to be provided. Minimum allowed value is 2.
   */
  public KMultiSplitPane( @NonNull Orientation orientation, int count ) {
    this( orientation, count, false );
  }
  
  /**
   * Initialises this instance with configurable continuous layouting.
   * 
   * @param orientation        The orientation for the contained fields. Not <code>null</code>.
   * @param count              The number of fields to be provided. Minimum allowed value is 2.
   * @param continuouslayout   <code>true</code> <=> Enable continuous layouting while the divider location is still 
   *                                                 being changed.
   */
  public KMultiSplitPane( @NonNull Orientation orientation, int count, boolean continuouslayout ) {
    super( new BorderLayout() );
    
    changeeventdispatcher = new ChangeEventDispatcher();
    
    ComponentListener l   = new LocalComponentListener( this );
    
    components            = new Component[ count ];
    panels                = createPanels( count );
    
    chain                 = createChain( orientation, count, continuouslayout );
    
    add( chain[0], BorderLayout.CENTER );
    
    for( int i = 0; i < panels.length; i++ ) {
      panels[i].addComponentListener(l);
    }

  }
  
  /**
   * Creates <param>count - 1</param> JSplitPane instances. Each field will be set into the top component (except for
   * the last one). The bottom component will be set to the splitpane for the next panel (except for the last one).
   * 
   * @param orientation        The orientation for the contained fields. Not <code>null</code>.
   * @param count              The number of fields to be provided. Minimum allowed value is 2.
   * @param continuouslayout   <code>true</code> <=> Enable continuous layouting while the divider location is still 
   *                                                 being changed.
   *                                                  
   * @return   A chainged list of JSplitPane instances. Not <code>null</code>.
   */
  private JSplitPane[] createChain( Orientation orientation, int count, boolean continuouslayout ) {
    JSplitPane[] result = new JSplitPane[ count - 1 ];
    for( int i = 0; i < result.length; i++ ) {
      result[i] = new JSplitPane( orientation.getSplitPaneOrientation(), continuouslayout );
    }
    // set the next splitpane into the bottom of the previous splitpane
    for( int i = 1; i < result.length; i++ ) {
      result[ i - 1 ].setBottomComponent( result[i] );
    }
    // set the panels into all top parts (except for the last one)
    for( int i = 0; i < result.length; i++ ) {
      result[i].setTopComponent( panels[i] );
    }
    result[ result.length - 1 ].setBottomComponent( panels[ result.length ] );
    return result;
  }
  
  /**
   * Create panels which will be used to collect the fields.
   * 
   * @param count   The number of fields that will be used for this split pane.
   * 
   * @return   A list of panels used to be set to fields. Not <code>null</code>.
   */
  private JPanel[] createPanels( int count ) {
    JPanel[] result = new JPanel[ count ];
    for( int i = 0; i < result.length; i++ ) {
      result[i] = new JPanel( new BorderLayout() );
    }
    return result;
  }
  
  /**
   * Registers the supplied listener to become informed upon changes.
   * 
   * @param l   The listener which becomes informed upon changes. Not <code>null</code>.
   */
  public void addChangeListener( @NonNull ChangeListener l ) {
    changeeventdispatcher.addListener(l);
  }
  
  /**
   * Prevents further notification for the supplied listener.
   * 
   * @param l   The listener that won't be notified anymore. Not <code>null</code>.
   */
  public void removeChangeListener( @NonNull ChangeListener l ) {
    changeeventdispatcher.removeListener(l);
  }

  /**
   * Fires the supplied ChangeEvent so every currently registered listener will be informed.
   * 
   * @param evt   The ChangeEvent that will be delivered. Not <code>null</code>.
   */
  protected void fireChangeEvent( @NonNull ChangeEvent evt ) {
    changeeventdispatcher.fireEvent( evt );
  }

  /**
   * Returns a list of divider locations. The number of divider locations corresponds to the number of fields - 1 .
   * 
   * @return   A list of divider locations. Not <code>null</code>.
   */
  public int[] getDividerLocations() {
    int[] result = new int[ chain.length ];
    for( int i = 0; i < chain.length; i++ ) {
      result[i] = chain[i].getDividerLocation();
    }
    return result;
  }
  
  /**
   * Changes the divider locations for this split panel.
   * 
   * @param newlocations   The new divider locations for this split panel. Not <code>null</code>. 
   */
  public void setDividerLocations( @NonNull int[] newlocations ) {
    if( newlocations.length <= chain.length ) {
      for( int i = 0; i < newlocations.length; i++ ) {
        chain[i].setDividerLocation( newlocations[i] );
      }
    }
  }
  
  /**
   * Returns the number of fields available within this component.
   * 
   * @return   The number of fields available within this component. 
   */
  public int getFieldCount() {
    return panels.length;
  }

  /**
   * Returns the component currently stored at the supplied index.
   * 
   * @param index   The position of the desired field. The value must be within the range {0, {@link #getFieldCount()} - 1} .
   * 
   * @return   The field which was stored at the desired position. Maybe <code>null</code>.
   */
  public Component getField( int index ) {
    Component result = components[ index ];
    if( result != null ) {
      panels[ index ].remove( result );
    }
    return result;
  }

  /**
   * Changes the component currently stored at the supplied index.
   * 
   * @param index       The position of the desired field. The value must be within the range {0, {@link #getFieldCount()} - 1} .
   * @param component   The Component which has to be set. Not <code>null</code>.
   */
  public void setField( int index, @NonNull Component component ) {
    components[ index ] = component;
    panels[ index ].add( component, BorderLayout.CENTER );
  }
  
  @AllArgsConstructor @FieldDefaults(level = AccessLevel.PRIVATE)
  private static class LocalComponentListener implements ComponentListener {
    
    KMultiSplitPane   owner;
    
    @Override
    public void componentResized( ComponentEvent e ) {
      owner.fireChangeEvent( new ChangeEvent( owner ) );
    }

    @Override
    public void componentMoved( ComponentEvent e ) {
      owner.fireChangeEvent( new ChangeEvent( owner ) );
    }

    @Override
    public void componentShown( ComponentEvent e ) {
      owner.fireChangeEvent( new ChangeEvent( owner ) );
    }

    @Override
    public void componentHidden( ComponentEvent e ) {
      owner.fireChangeEvent( new ChangeEvent( owner ) );
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
