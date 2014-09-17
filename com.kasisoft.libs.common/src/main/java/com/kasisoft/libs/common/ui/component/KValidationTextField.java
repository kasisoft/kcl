package com.kasisoft.libs.common.ui.component;

import com.kasisoft.libs.common.ui.event.*;
import com.kasisoft.libs.common.validation.*;

import javax.swing.event.*;

import javax.swing.text.*;

import javax.swing.*;

import java.awt.*;

import lombok.*;

/**
 * TextField variety which allows to run checks on the entered content and colors the input accordingly.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class KValidationTextField extends JTextField implements KValidationComponent {

  private Color                          invalidcolor;
  private Color                          validcolor;
  private boolean                        valid;
  private LocalBehaviour                 localbehaviour;
  private ValidationEventDispatcher      validationdispatcher;
  private ValidationConstraint<String>   validationconstraint;
  
  /**
   * Sets up this validating text field.
   */
  public KValidationTextField() {
    super();
    commonInit();
  }
  
  /**
   * Sets up this validating text field with the supplied content.
   * 
   * @param content   The content to display initially. Not <code>null</code>.
   */
  public KValidationTextField( @NonNull String content ) {
    super( content );
    commonInit();
  }

  /**
   * Do some common initialisations.
   */
  private void commonInit() {
    validcolor            = super.getForeground();
    invalidcolor          = Color.red;
    valid                 = true;
    validationconstraint  = null;
    localbehaviour        = new LocalBehaviour( this );
    validationdispatcher  = new ValidationEventDispatcher();
    getDocument().addDocumentListener( localbehaviour );
    setInputVerifier( new InputVerifier() {

      @Override
      public boolean verify( JComponent input ) {
        KValidationTextField field = (KValidationTextField) input;
        return field.isValid();
      }
      
    });
  }
  
  /**
   * Changes the constraint that is used to check the validity.
   * 
   * @param constraint   The constraint that is used to check the validity. Maybe <code>null</code>.
   */
  public void setValidationConstraint( ValidationConstraint<String> constraint ) {
    validationconstraint = constraint;
    validityCheck();
  }
  
  /**
   * Returns the constraint that is used to check the validity.
   * 
   * @return   The constraint that is used to check the validity. Maybe <code>null</code>.
   */
  public ValidationConstraint<String> getValidationConstraint() {
    return validationconstraint;
  }

  @Override
  public void addValidationListener( @NonNull ValidationListener l ) {
    validationdispatcher.addListener(l);
  }
  
  @Override
  public void removeValidationListener( @NonNull ValidationListener l ) {
    validationdispatcher.removeListener(l);
  }
  
  /**
   * Fires the supplied ValidityEvent so every currently registered listener will be informed.
   * 
   * @param evt   The ValidityEvent that will be delivered. Not <code>null</code>.
   */
  protected void fireValidationEvent( @NonNull ValidationEvent evt ) {
    validationdispatcher.fireEvent( evt );
  }

  @Override
  public void setDocument( Document newdocument ) {
    Document olddocument = getDocument();
    if( olddocument != null ) {
      olddocument.removeDocumentListener( localbehaviour );
    }
    super.setDocument( newdocument );
    if( newdocument != null ) {
      newdocument.addDocumentListener( localbehaviour );
    }
  }
  
  @Override
  public boolean isValid() {
    return valid;
  }
  
  /**
   * Changes the color for invalid content.
   * 
   * @param newinvalidcolor    The new color for invalid content. Not <code>null</code>.
   */
  public void setInvalidColor( @NonNull Color newinvalidcolor ) {
    invalidcolor = newinvalidcolor;
    if( ! isValid() ) {
      super.setForeground( invalidcolor );
    }
  }
  
  /**
   * Returns the Color for invalid content.
   * 
   * @return   The Color for invalid content.
   */
  public Color getInvalidColor() {
    return invalidcolor;
  }
  
  /**
   * Changes the foreground Color used to visualise valid content.
   * 
   * @param newvalidcolor   The Color used to visualise valid content. Not <code>null</code>.
   */
  public void setValidColor( @NonNull Color newvalidcolor ) {
    validcolor = newvalidcolor;
    if( isValid() ) {
      super.setForeground( validcolor );
    }
  }
  
  /**
   * Returns the foreground Color used to visualise valid content.
   * 
   * @return   The foreground Color used to visualise valid content.
   */
  public Color getValidColor() {
    return validcolor;
  }
  
  /**
   * Calculates the current validity.
   * 
   * @param text   The text that is supposed to be tested. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The current input is valid.
   */
  private boolean calculatedValidity( @NonNull String text ) {
    if( validationconstraint == null ) {
      return true;
    }
    return validationconstraint.check( text );
  }
  
  /**
   * Checks the current validity and propagates and generates a notification on change.
   */
  protected synchronized void validityCheck() {
    String  text        = super.getText();
    boolean newvalidity = calculatedValidity( text );
    if( newvalidity ) {
      accept( text );
    }
    if( newvalidity == valid ) {
      // nothing has changed
      return;
    }
    valid = newvalidity;
    super.setForeground( valid ? validcolor : invalidcolor );
    fireValidationEvent( new ValidationEvent( this, valid ) );
  }
  
  /**
   * Will be invoked whenever some input has been identified as valid. This function is invoked before events are being
   * fired.
   * 
   * @param text   The valid text. Not <code>null</code>.
   */
  protected void accept( @NonNull String text ) {
  }
  
  /**
   * Implementation of custom behaviour.
   */
  private static class LocalBehaviour implements DocumentListener {

    private KValidationTextField   owner;
    
    public LocalBehaviour( KValidationTextField pthis ) {
      owner = pthis;
    }
    
    @Override
    public void changedUpdate( DocumentEvent evt ) {
      owner.validityCheck();
    }

    @Override
    public void insertUpdate( DocumentEvent evt ) {
      owner.validityCheck();
    }

    @Override
    public void removeUpdate( DocumentEvent evt ) {
      owner.validityCheck();
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
