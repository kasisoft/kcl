package com.kasisoft.libs.common.ui.component;

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
public class KDoubleField extends KFilteringTextField {

  @Getter double          minimum;
  @Getter double          maximum;
          String          tooltiptext;
  
  @Getter Double          value;
  
  StringBuilder           buffer;
  ChangeEventDispatcher   changeeventdispatcher;
  
  @Getter @Setter 
  boolean                 emptyValid;
  
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
    emptyValid            = false;
    changeeventdispatcher = new ChangeEventDispatcher();
    buffer                = new StringBuilder();
    minimum               = min == null ? -Double.MAX_VALUE : min.doubleValue();
    maximum               = max == null ?  Double.MAX_VALUE : max.doubleValue();
    if( minimum >= 0 ) {
      setAllowed( ".0123456789" );
    } else {
      setAllowed( "-.0123456789" );
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
      text = String.format( " %3.3e .. %3.3e ", Double.valueOf( minimum ), Double.valueOf( maximum ) );
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
  public void setValue( Double newvalue ) {
    if( newvalue == null ) { 
      super.setText( "" );
    } else {
      super.setText( newvalue.toString() );
    }
    value = newvalue;
  }
  
  @Override
  protected void accept( @NonNull String text ) {
    try {
      value = Double.valueOf( text );
    } catch( Exception ex ) {
      // fragments like -, -0, -., -0.
      value = null;
    }
  }
  
  /**
   * Changes the currently allowed minimum value. If the minimum value is bigger than the current maximum these values
   * will be exchanged.
   * 
   * @param newminimum   The new allowed minimum value. Maybe <code>null</code>.
   */
  public void setMinimum( Double newminimum ) {
    if( (newminimum == null) || Double.isNaN( newminimum.doubleValue() ) ) {
      minimum = -Double.MAX_VALUE;
    } else {
      minimum = newminimum.doubleValue();
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
  public void setMaximum( Double newmaximum ) {
    if( (newmaximum == null) || Double.isNaN( newmaximum.doubleValue() ) ) {
      maximum = Double.MAX_VALUE;
    } else {
      maximum = newmaximum.intValue();
    }
    swap();
    validityCheck();
  }

  private void swap() {
    if( minimum > maximum ) {
      double value = maximum;
      maximum      = minimum;
      minimum      = value;
    }
    if( minimum >= 0 ) {
      setAllowed( ".0123456789" );
    } else {
      setAllowed( "-.0123456789" );
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
  private class LocalBehaviour implements Predicate<String>, DocumentListener {

    KDoubleField    pthis;
    
    public LocalBehaviour( KDoubleField ref ) {
      pthis = ref;
    }
    
    @Override
    public boolean test( String input ) {
      if( input.length() == 0 ) {
        return pthis.emptyValid;
      }
      try {
        double value = Double.parseDouble( input );
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
