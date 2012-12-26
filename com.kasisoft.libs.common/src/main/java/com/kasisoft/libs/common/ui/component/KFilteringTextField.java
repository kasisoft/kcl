/**
 * Name........: KFilteringTextField
 * Description.: Textfield variety which simply filters characters, so they cannot be used while entering some input. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.ui.component;

import javax.swing.text.*;

/**
 * Textfield variety which simply filters characters, so they cannot be used while entering some input.
 */
public class KFilteringTextField extends KValidationTextField {

  private String         allowed;
  private StringBuffer   buffer;
  private CustomFilter   filter;
  
  /**
   * Sets up this field to filter some content.
   */
  public KFilteringTextField() {
    super();
    allowed = null;
    buffer  = new StringBuffer();
    filter  = new CustomFilter( this );
    ((AbstractDocument) super.getDocument()).setDocumentFilter( filter );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDocument( Document newdocument ) {
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
   * Returns a list of allowed characters.
   * 
   * @return   A list of allowed characters.
   */
  public String getAllowed() {
    return allowed;
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
   * @param input   The String which might need to be filtered.
   * 
   * @return   The cleaned String. Not <code>null</code>.
   */
  protected String calculateInsertionString( int offs, String input ) {
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
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void insertString( FilterBypass fb, int offset, String text, AttributeSet attrs ) throws BadLocationException {
      super.insertString( fb, offset, owner.calculateInsertionString( offset, text ), attrs );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void replace( FilterBypass fb, int offset, int length, String text, AttributeSet attrs ) throws BadLocationException {
      super.remove( fb, offset, length );
      super.insertString( fb, offset, owner.calculateInsertionString( offset, text ), attrs );
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
