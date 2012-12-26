/**
 * Name........: KDoubleField
 * Description.: Field used to support numerical values only.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.ui.component;

import com.kasisoft.libs.common.ui.event.*;
import com.kasisoft.libs.common.validation.*;

import javax.swing.event.*;

/**
 * Field used to support numerical values only.
 */
public class KDoubleField extends KFilteringTextField {

  private double                  minimum;
  private double                  maximum;
  private Double                  value;
  private StringBuffer            buffer;
  private ChangeEventDispatcher   changeeventdispatcher;
  
  /**
   * Sets up this field allowing to enter numerical values.
   */
  public KDoubleField() {
    this( (Double) null, (Double) null );
  }

  /**
   * Sets up this field allowing to enter numerical values.
   * 
   * @param min      The minimal allowed value. Maybe <code>null</code>.
   * @param max      The maximal allowed value. Maybe <code>null</code>.
   */
  public KDoubleField( Double min, Double max ) {
    super();
    value                 = null;
    changeeventdispatcher = new ChangeEventDispatcher();
    buffer                = new StringBuffer();
    minimum               = min == null ? -Double.MAX_VALUE : min.doubleValue();
    maximum               = max == null ?  Double.MAX_VALUE : max.doubleValue();
    if( minimum >= 0 ) {
      setAllowed( ".0123456789" );
    } else {
      setAllowed( "-.0123456789" );
    }
    LocalBehaviour localbehaviour = new LocalBehaviour();
    setValidationConstraint( localbehaviour );
    getDocument().addDocumentListener( localbehaviour );
    setToolTipText( null );
  }

  /**
   * {@inheritDoc}
   * 
   * If <code>tooltiptext</code> is null or empty a default tooltip text is generated.
   */
  @Override
  public void setToolTipText( String tooltiptext ) {
    if( (tooltiptext == null) || (tooltiptext.length() == 0) ) {
      tooltiptext = String.format( " %3.3f .. %3.3f ", Double.valueOf( minimum ), Double.valueOf( maximum ) );
    }
    super.setToolTipText( tooltiptext );
  }
  
  /**
   * Registers the supplied listener to become informed upon changes.
   * 
   * @param l   The listener which becomes informed upon changes. Not <code>null</code>.
   */
  public void addChangeListener( ChangeListener l ) {
    changeeventdispatcher.addListener(l);
  }
  
  /**
   * Prevents further notification for the supplied listener.
   * 
   * @param l   The listener that won't be notified anymore. Not <code>null</code>.
   */
  public void removeChangeListener( ChangeListener l ) {
    changeeventdispatcher.removeListener(l);
  }

  /**
   * Fires the supplied ChangeEvent so every currently registered listener will be informed.
   * 
   * @param evt   The ChangeEvent that will be delivered. Not <code>null</code>.
   */
  protected void fireChangeEvent( ChangeEvent evt ) {
    changeeventdispatcher.fireEvent( evt );
  }

  /**
   * Returns the numerical value currently stored in this widget only in case it's valid.
   * 
   * @return   The numerical value currently stored in this widget. Non <code>null</code> if valid.
   */
  public Double getValue() {
    return value;
  }

  /**
   * Changes the current numerical value.
   * 
   * @param newvalue   The new numerical value.
   */
  public void setValue( Double newvalue ) {
    if( newvalue == null ) { 
      super.setText( "" );
    } else {
      super.setText( newvalue.toString() );
    }
    value = newvalue;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected void accept( String text ) {
    try {
      value = Double.valueOf( text );
    } catch( NumberFormatException ex ) {
      // fragments like -, -0, -., -0.
    }
  }
  
  /**
   * Changes the currently allowed minimum value.
   * 
   * @param newminimum   The new allowed minimum value.
   */
  public void setMinimum( Double newminimum ) {
    if( (newminimum == null) || Double.isNaN( newminimum.doubleValue() ) ) {
      minimum = -Double.MAX_VALUE;
    } else {
      minimum = newminimum.doubleValue();
    }
    if( minimum >= 0 ) {
      setAllowed( ".0123456789" );
    } else {
      setAllowed( "-.0123456789" );
    }
    validityCheck();
  }

  /**
   * Returns the currently allowed minimum value.
   * 
   * @return   The currently allowed minimum value.
   */
  public double getMinimum() {
    return minimum;
  }
  
  /**
   * Changes the currently allowed maximum value.
   * 
   * @param newmaximum   The new allowed maximum value.
   */
  public void setMaximum( Double newmaximum ) {
    if( (newmaximum == null) || Double.isNaN( newmaximum.doubleValue() ) ) {
      maximum = -Double.MAX_VALUE;
    } else {
      maximum = newmaximum.intValue();
    }
    validityCheck();
  }

  /**
   * Returns the currently allowed maximum value.
   * 
   * @return   The currently allowed maximum value.
   */
  public double getMaximum() {
    return maximum;
  }

  /**
   * Generates an event to inform about the changed value.
   */
  private void valueChanged() {
    fireChangeEvent( new ChangeEvent( this ) );
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected String calculateInsertionString( int offset, String input ) {
    
    // just drop unsupported characters
    input = super.calculateInsertionString( offset, input );
    
    synchronized( buffer ) {
      
      String currenttext = super.getText();
      
      buffer.setLength(0);
      buffer.append( input );
      
      for( int i = buffer.length() - 1; i > 0; i-- ) {
        if( buffer.charAt(i) == '-' ) {
          buffer.deleteCharAt(i);
        }
      }
      
      if( (buffer.length() > 0) && (buffer.charAt(0) == '-') ) {
        boolean hasminus = (currenttext.length() > 0) && (currenttext.charAt(0) == '-');
        if( (minimum >= 0) || hasminus || (offset > 0) ) {
          buffer.deleteCharAt(0);
        }
      }
      
      int dot = buffer.indexOf(".");
      if( dot != -1 ) {
        for( int i = buffer.length() - 1; i > dot; i-- ) {
          if( buffer.charAt(i) == '.' ) {
            buffer.deleteCharAt(i);
          }
        }
        if( currenttext.indexOf('.') != -1 ) {
          buffer.deleteCharAt( dot );
        }
      }
      return buffer.toString();
      
    }
    
  }

  /**
   * Implementation of custom behaviour.
   */
  private class LocalBehaviour implements ValidationConstraint<String>, DocumentListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean check( String input ) {
      if( input.length() == 0 ) {
        return true;
      }
      try {
        double value = Double.parseDouble( input );
        return (value >= minimum) && (value <= maximum);
      } catch( NumberFormatException ex ) {
        return false;
      }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changedUpdate( DocumentEvent evt ) {
      valueChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertUpdate( DocumentEvent evt ) {
      valueChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeUpdate( DocumentEvent evt ) {
      valueChanged();
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
