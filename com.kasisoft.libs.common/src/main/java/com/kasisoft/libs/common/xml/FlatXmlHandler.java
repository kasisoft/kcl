package com.kasisoft.libs.common.xml;

import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.util.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.util.*;

import java.io.*;

/**
 * Simple converter which allows to create flat representations of a XML document. This converter simply implements a 
 * DefaultHandler used in conjunction with the SAX Parser. An OutputStream must be supplied in order to generate the 
 * output. The OutputStream must be set each time this handler is used since it will be discarded from the handler after 
 * a conversion has taken place.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class FlatXmlHandler extends DefaultHandler {

  private Stack<String>    elements;
  private StringFBuilder   path;
  
  private StringBuilder    buffer;
  private OutputStream     dest;

  private boolean          trimvalues;
  private boolean          escaping;
  private boolean          attributes;
  private String           newline;
  private Encoding         encoding;
  
  /**
   * Initialises this generator. An instance may be used multiple times.
   */
  public FlatXmlHandler() {
    super();
    elements   = new Stack<>();
    path       = new StringFBuilder();
    buffer     = new StringBuilder();
    dest       = null;
    trimvalues = true;
    attributes = true;
    escaping   = true;
    newline    = SysProperty.LineSeparator.getValue( System.getProperties(), "\n" );
    encoding   = Encoding.UTF8;
  }
  
  /**
   * Returns <code>true</code> if attributes will be written, too.
   * 
   * @return   <code>true</code> <=> Attributes will be written, too.
   */
  public boolean isAttributes() {
    return attributes;
  }
  
  /**
   * Enables/disables the generation of attribute values.
   * 
   * @param enable   <code>true</code> <=> Enables the generation of attribute values.
   */
  public void setAttributes( boolean enable ) {
    attributes = enable;
  }
  
  /**
   * Returns the line separator sequence. Default is System.getProperty( 'line.separator' ).
   * 
   * @return   The line separator sequence. Not <code>null</code>.
   */
  public String getNewline() {
    return newline;
  }
  
  /**
   * Changes the current line separator sequence.
   * 
   * @param linesep   The new line separator sequence.
   */
  public void setNewline( String linesep ) {
    newline = linesep;
    if( newline == null ) {
      newline = "";
    }
  }
  
  /**
   * Returns <code>true</code> if line separators will be escaped so the output values won't contain
   * line separators anymore. Default is <code>true</code>.
   * 
   * @return   <code>true</code> <=> The line separators will be escaped.
   */
  public boolean isEscaping() {
    return escaping;
  }
  
  /**
   * Enables/disables escaping of line separators within values.
   * 
   * @param enable   <code>true</code> <=> The line separators in values shall be escaped.
   */
  public void setEscaping( boolean enable ) {
    escaping = enable;
  }

  /**
   * Returns <code>true</code> if values will be trimmed. Default is <code>true</code>.
   * 
   * @return   <code>true</code> <=> Values will be trimmed.
   */
  public boolean isTrimValues() {
    return trimvalues;
  }
  
  /**
   * Enables/disables the trimming of values.
   * 
   * @param enable   <code>true</code> <=> Values will be trimmed.
   */
  public void setTrimValues( boolean enable ) {
    trimvalues = enable;
  }
  
  /**
   * Sets the current target which is used to receive the flattened XML structure.
   * 
   * @param target   The target which is used to receive the flattened XML structure. Not <code>null</code>.
   */
  public void setTarget( OutputStream target ) {
    dest = target;
  }
  
  /**
   * Writes the supplied content to the OutputStream.
   * 
   * @param value    The value stored by the key. Not <code>null</code>.
   * 
   * @throws SAXException   Writing to the target failed for some reason.
   */
  private void write( String value ) throws SAXException {
    if( trimvalues ) {
      value = value.trim();
    }
    try {
      String text = String.format( "%s=%s%s", path.toString(), escape( value ), newline );
      dest.write( encoding.encode( text ) );
    } catch( IOException ex ) {
      throw new SAXException(ex);
    }
  }
  
  /**
   * Creates a simple one line representation of the supplied input.
   * 
   * @param input   The input which has to be broken on one line. Not <code>null</code>.
   * 
   * @return   The input which can be placed on one line.
   */
  private String escape( String input ) {
    if( escaping ) {
      input = input.replaceAll( "\n", "&#10;" );
      input = input.replaceAll( "\r", "&#13;" );
    }
    return input;
  }
  
  @Override
  public void startDocument() throws SAXException {
    elements.clear();
    path.setLength(0);
    buffer.setLength(0);
    if( dest == null ) {
      dest = System.out;
    }
  }

  @Override
  public void endDocument() throws SAXException {
    if( dest == System.out ) {
      dest = null;
    }
  }
  
  @Override
  public void startElement( String uri, String localname, String qname, Attributes attributes ) throws SAXException {
    String name = qname != null ? qname : localname;
    elements.push( name );
    path.appendF( "%s/", name );
    if( this.attributes ) {
      String[] names = new String[ attributes.getLength() ];
      for( int i = 0; i < attributes.getLength(); i++ ) {
        names[i] = attributes.getQName(i);
      }
      Arrays.sort( names );
      for( int i = 0; i < names.length; i++ ) {
        path.appendF( "@%s", names[i] );
        write( attributes.getValue( names[i] ) );
        path.setLength( path.length() - names[i].length() - 1 );
      }
    }
  }

  @Override
  public void endElement( String uri, String localname, String qname ) throws SAXException {
    path.append( "text()" );
    write( buffer.toString() );
    String name = elements.pop();
    path.setLength( path.length() - name.length() - 1  - "text()".length() );
    buffer.setLength(0);
  }

  @Override
  public void characters( char[] ch, int start, int length ) throws SAXException {
    buffer.append( ch, start, length );
  }

} /* ENDCLASS */
