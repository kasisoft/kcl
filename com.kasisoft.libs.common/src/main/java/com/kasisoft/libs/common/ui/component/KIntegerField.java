package com.kasisoft.libs.common.ui.component;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.ui.event.*;

import lombok.experimental.*;

import lombok.*;

import javax.swing.event.*;

import java.util.function.*;

/**
 * Field used to support numerical values only.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KIntegerField extends KFilteringTextField {

  @Getter long            minimum;
  @Getter long            maximum;
          String          tooltiptext;
  
  @Getter Long            value;
  
  StringBuilder           buffer;
  ChangeEventDispatcher   changeeventdispatcher;
  
  @Getter @Setter 
  boolean                 emptyValid;
  
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
    emptyValid            = false;
    changeeventdispatcher = new ChangeEventDispatcher();
    buffer                = new StringBuilder();
    minimum               = min == null ? Long.MIN_VALUE : min.intValue();
    maximum               = max == null ? Long.MAX_VALUE : max.intValue();
    if( minimum >= 0 ) {
      setAllowed( "0123456789" );
    } else {
      setAllowed( "-0123456789" );
    }
    LocalBehaviour localbehaviour = new LocalBehaviour( this );
    setValidationConstraint( localbehaviour );
    getDocument().addDocumentListener( localbehaviour );
    setToolTipText( null );
  }

  /**
   * If <code>tooltiptext</code> is null or empty a default tooltip text is generated.
   */
  @Override
  public void setToolTipText( String text ) {
    if( (text == null) || (text.length() == 0) ) {
      text = String.format( " %d .. %d ", Long.valueOf( minimum ), Long.valueOf( maximum ) );
    }
    super.setToolTipText( text );
    tooltiptext = text;
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
   * Changes the current numerical value.
   * 
   * @param newvalue   The new numerical value. Maybe <code>null</code>.
   */
  public void setValue( Long newvalue ) {
    if( newvalue == null ) { 
      super.setText( "" );
    } else {
      super.setText( newvalue.toString() );
    }
    value = newvalue;
  }
  
  @Override
  protected void accept( @NonNull String text ) {
    if( (text.length() == 0) || "-".equals( text ) ) {
      value = null;
    } else {
      value = Long.valueOf( text );
    }
  }
  
  /**
   * Changes the currently allowed minimum value. If the minimum value is bigger than the current maximum these values
   * will be exchanged.
   * 
   * @param newminimum   The new allowed minimum value. Maybe <code>null</code>.
   */
  public void setMinimum( Long newminimum ) {
    if( newminimum == null ) {
      minimum = Long.MIN_VALUE;
    } else {
      minimum = newminimum.intValue();
    }
    swap();
    validityCheck();
  }

  /**
   * Changes the currently allowed maximum value. If the maximum value is bigger than the current minimum these values
   * will be exchanged.
   * 
   * @param newmaximum   The new allowed maximum value. Maybe <code>null</code>.
   */
  public void setMaximum( Long newmaximum ) {
    if( newmaximum == null ) {
      maximum = Long.MAX_VALUE;
    } else {
      maximum = newmaximum.intValue();
    }
    swap();
    validityCheck();
  }

  private void swap() {
    if( minimum > maximum ) {
      long value   = maximum;
      maximum      = minimum;
      minimum      = value;
    }
    if( minimum >= 0 ) {
      setAllowed( "0123456789" );
    } else {
      setAllowed( "-0123456789" );
    }
    setToolTipText( tooltiptext );
  }
  
  /**
   * Generates an event to inform about the changed value.
   */
  private void valueChanged() {
    fireChangeEvent( new ChangeEvent( this ) );
  }
  
  @Override
  protected String calculateInsertionString( int offset, @NonNull String input ) {
    
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
  private class LocalBehaviour implements Predicate<String>, DocumentListener {

    KIntegerField    pthis;
    
    public LocalBehaviour( KIntegerField ref ) {
      pthis = ref;
    }
    
    @Override
    public boolean test( String input ) {
      if( input.length() == 0 ) {
        return pthis.emptyValid;
      }
      try {
        long value = Long.parseLong( input );
        return (value >= minimum) && (value <= maximum);
      } catch( Exception ex ) {
        return false;
      }
    }

    @Override
    public void changedUpdate( DocumentEvent evt ) {
      valueChanged();
    }

    @Override
    public void insertUpdate( DocumentEvent evt ) {
      valueChanged();
    }

    @Override
    public void removeUpdate( DocumentEvent evt ) {
      valueChanged();
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
