package com.kasisoft.libs.common.ui.component;

import lombok.experimental.*;

import lombok.*;

import javax.swing.text.*;

/**
 * Textfield variety which simply filters characters, so they cannot be used while entering some input.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KFilteringTextField extends KValidationTextField {

  @Getter String    allowed;
  StringBuilder     buffer;
  CustomFilter      filter;
  
  /**
   * Sets up this field to filter some content.
   */
  public KFilteringTextField() {
    super();
    allowed = null;
    buffer  = new StringBuilder();
    filter  = new CustomFilter( this );
    ((AbstractDocument) super.getDocument()).setDocumentFilter( filter );
  }

  @Override
  public void setDocument( @NonNull Document newdocument ) {
    Document olddocument = super.getDocument();
    if( olddocument instanceof AbstractDocument ) {
      ((AbstractDocument) newdocument).setDocumentFilter( filter );
    }
    super.setDocument( newdocument );
    if( newdocument instanceof AbstractDocument ) {
      ((AbstractDocument) newdocument).setDocumentFilter( filter );
    }
  }

  /**
   * Changes the allowed characters.
   * 
   * @param newallowed   The new allowed characters. A value of <code>null</code> means to allow
   *                     each character.
   */
  public void setAllowed( String newallowed ) {
    allowed = newallowed;
    setText( getText() );
  }
  
  /**
   * Modifies the supplied String so it won't contain any filtered character anymore.
   * 
   * @param offs    The insertion position.
   * @param input   The String which might need to be filtered. Not <code>null</code>.
   * 
   * @return   The cleaned String. Not <code>null</code>.
   */
  protected String calculateInsertionString( int offs, @NonNull String input ) {
    if( (allowed == null) || (allowed.length() == 0) ) {
      return input;
    }
    synchronized( buffer ) {
      buffer.setLength(0);
      for( int i = 0; i < input.length(); i++ ) {
        if( allowed.indexOf( input.charAt(i) ) != -1 ) {
          buffer.append( input.charAt(i) );
        }
      }
      return buffer.toString();
    }
  }

  private static class CustomFilter extends DocumentFilter {

    private KFilteringTextField   owner;
    
    public CustomFilter( KFilteringTextField pthis ) {
      owner = pthis;
    }
    
    @Override
    public void insertString( FilterBypass fb, int offset, String text, AttributeSet attrs ) throws BadLocationException {
      super.insertString( fb, offset, owner.calculateInsertionString( offset, text ), attrs );
    }

    @Override
    public void replace( FilterBypass fb, int offset, int length, String text, AttributeSet attrs ) throws BadLocationException {
      super.remove( fb, offset, length );
      super.insertString( fb, offset, owner.calculateInsertionString( offset, text ), attrs );
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
