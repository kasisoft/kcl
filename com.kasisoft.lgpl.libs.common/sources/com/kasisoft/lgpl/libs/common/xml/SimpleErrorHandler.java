/**
 * Name........: SimpleErrorHandler
 * Description.: Default implementation of an ErrorHandler.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.xml;

import com.kasisoft.lgpl.libs.common.constants.*;

import com.kasisoft.lgpl.tools.diagnostic.*;

import org.xml.sax.*;

import java.util.*;

/**
 * Default implementation of an ErrorHandler.
 */
@KDiagnostic
public class SimpleErrorHandler implements ErrorHandler {

  private int               errorcount;
  private List<XmlFault>    faults;
  
  /**
   * Initialises this basic error handler.
   */
  public SimpleErrorHandler() {
    errorcount    = 0;
    faults        = new ArrayList<XmlFault>();
  }
  
  /**
   * Returns <code>true</code> if at least one error occured.
   * 
   * @return   <code>true</code> <=> At least one error occured.
   */
  public boolean hasErrors() {
    return errorcount > 0;
  }
  
  /**
   * Returns the faults which have been collected.
   * 
   * @return   The faults which have been collected. Not <code>null</code>.
   */
  public XmlFault[] getFaults() {
    return faults.toArray( new XmlFault[ faults.size() ] );
  }
  
  /**
   * Returns a full text representation of this fault used for presentations.
   * 
   * @return   A full text representation of this fault used for presentations.
   *           Neither <code>null</code> nor empty if {@link #hasErrors()}.
   */
  public String getFaultMessage() {
    String       linesep  = SystemProperty.LineSeparator.getValue();
    StringBuffer buffer   = new StringBuffer();
    for( int i = 0; i < faults.size(); i++ ) {
      buffer.append( faults.get(i).getFaultMessage() );
      buffer.append( linesep );
    }
    return buffer.toString();
  }
  
  /**
   * {@inheritDoc}
   */
  public void error( SAXParseException ex ) throws SAXException {
    errorcount++;
    faults.add( new XmlFault( false, ex ) );
  }

  /**
   * {@inheritDoc}
   */
  public void fatalError( SAXParseException ex ) throws SAXException {
    errorcount++;
    faults.add( new XmlFault( false, ex ) );
  }

  /**
   * {@inheritDoc}
   */
  public void warning( SAXParseException ex ) throws SAXException {
    faults.add( new XmlFault( true, ex ) );
  }

} /* ENDCLASS */
