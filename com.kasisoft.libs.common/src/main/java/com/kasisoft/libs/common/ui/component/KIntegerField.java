/**
 * Name........: KIntegerField
 * Description.: Field used to support numerical values only.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.ui.component;

import com.kasisoft.libs.common.validation.*;
import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.event.*;

import javax.swing.event.*;

/**
 * Field used to support numerical values only.
 */
public class KIntegerField extends KFilteringTextField {

  private long                    minimum;
  private long                    maximum;
  private Long                    value;
  private StringBuffer            buffer;
  private ChangeEventDispatcher   changeeventdispatcher;
  
  /**
   * Sets up this field allowing to enter numerical values.
   */
  public KIntegerField() {
    this( (Long) null, (Long) null );
  }

  /**
   * Sets up this field allowing to enter numerical values.
   * 
   * @param type   The primitive used to identify the range.
   *               If <code>null</code> or non-decimal the maximum range is setup.
   */
  public KIntegerField( Primitive type ) {
    this( type, false );
  }

  /**
   * Sets up this field allowing to enter numerical values.
   * 
   * @param type       The primitive used to identify the range.
   *                   If <code>null</code> or non-decimal the maximum range is setup.
   * @param unsigned   <code>true</code> <=> Use an unsigned range.
   */
  public KIntegerField( Primitive type, boolean unsigned ) {
    this( value( type, unsigned, true ), value( type, unsigned, false ) );
  }
  
  /**
   * Sets up this field allowing to enter numerical values.
   * 
   * @param min   The minimal allowed value. Maybe <code>null</code>.
   * @param max   The maximal allowed value. Maybe <code>null</code>.
   */
  public KIntegerField( Long min, Long max ) {
    super();
    value                 = null;
    changeeventdispatcher = new ChangeEventDispatcher();
    buffer                = new StringBuffer();
    minimum               = min == null ? Long.MIN_VALUE : min.intValue();
    maximum               = max == null ? Long.MAX_VALUE : max.intValue();
    if( minimum >= 0 ) {
      setAllowed( "0123456789" );
    } else {
      setAllowed( "-0123456789" );
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
      tooltiptext = String.format( " %d .. %d ", Long.valueOf( minimum ), Long.valueOf( maximum ) );
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
  public Long getValue() {
    return value;
  }

  /**
   * Changes the current numerical value.
   * 
   * @param newvalue   The new numerical value.
   */
  public void setValue( Long newvalue ) {
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
    if( (text.length() == 0) || "-".equals( text ) ) {
      value = null;
    } else {
      value = Long.valueOf( text );
    }
  }
  
  /**
   * Changes the currently allowed minimum value.
   * 
   * @param newminimum   The new allowed minimum value.
   */
  public void setMinimum( Long newminimum ) {
    if( newminimum == null ) {
      minimum = Long.MIN_VALUE;
    } else {
      minimum = newminimum.intValue();
    }
    if( minimum >= 0 ) {
      setAllowed( "0123456789" );
    } else {
      setAllowed( "-0123456789" );
    }
    validityCheck();
  }

  /**
   * Returns the currently allowed minimum value.
   * 
   * @return   The currently allowed minimum value.
   */
  public long getMinimum() {
    return minimum;
  }
  
  /**
   * Changes the currently allowed maximum value.
   * 
   * @param newmaximum   The new allowed maximum value.
   */
  public void setMaximum( Long newmaximum ) {
    if( newmaximum == null ) {
      maximum = Long.MAX_VALUE;
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
  public long getMaximum() {
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
      
      buffer.setLength(0);
      buffer.append( input );
      
      for( int i = buffer.length() - 1; i > 0; i-- ) {
        if( buffer.charAt(i) == '-' ) {
          buffer.deleteCharAt(i);
        }
      }
      if( (buffer.length() > 0) && (buffer.charAt(0) == '-') ) {
        boolean hasminus = (super.getText().length() > 0) && (super.getText().charAt(0) == '-');
        if( (minimum >= 0) || hasminus || (offset > 0) ) {
          buffer.deleteCharAt(0);
        }
      }
      
      return buffer.toString();
      
    }
    
  }
  
  /**
   * Returns a value for a boundary.
   * 
   * @param primitive   The primitive used to <i>specify</i> the boundary. Maybe <code>null</code>.
   * @param unsigned    <code>true</code> <=> Access an unsigned range.
   * @param min         <code>true</code> <=> Ask for the lower boundary.
   * 
   * @return   The boundary value. Maybe <code>null</code>.
   */
  private static final Long value( Primitive primitive, boolean unsigned, boolean min ) {
    if( (primitive == null) || (! primitive.supportsMinMax()) ) {
      return null;
    }
    if( unsigned ) {
      if( min ) {
        return Long.valueOf(0);
      } else if( primitive == Primitive.PLong ) {
        return null;
      } else {
        return Long.valueOf( primitive.getUnsignedMax() );
      }
    } else {
      if( min ) {
        return Long.valueOf( primitive.getMin() );
      } else {
        return Long.valueOf( primitive.getMax() );
      }
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
        long value = Long.parseLong( input );
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
