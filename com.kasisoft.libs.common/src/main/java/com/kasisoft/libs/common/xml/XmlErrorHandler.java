package com.kasisoft.libs.common.xml;

import com.kasisoft.libs.common.constants.*;

import org.xml.sax.*;

import java.util.*;

import lombok.*;

/**
 * Default implementation of an ErrorHandler.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class XmlErrorHandler implements ErrorHandler {

  private int               errorcount;
  private List<XmlFault>    faults;
  
  /**
   * Initialises this basic error handler.
   */
  public XmlErrorHandler() {
    errorcount    = 0;
    faults        = new ArrayList<>();
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
   * @return   A full text representation of this fault used for presentations. Neither <code>null</code> nor empty 
   *           if {@link #hasErrors()}.
   */
  public String getFaultMessage() {
    String        linesep = SysProperty.LineSeparator.getValue( System.getProperties() );
    StringBuilder buffer  = new StringBuilder();
    for( int i = 0; i < faults.size(); i++ ) {
      buffer.append( faults.get(i).getFaultMessage() );
      buffer.append( linesep );
    }
    return buffer.toString();
  }
  
  @Override
  public void error( @NonNull SAXParseException ex ) throws SAXException {
    errorcount++;
    faults.add( newFault( XmlFault.FaultType.error, ex ) );
  }

  @Override
  public void fatalError( @NonNull SAXParseException ex ) throws SAXException {
    errorcount++;
    faults.add( newFault( XmlFault.FaultType.fatal, ex ) );
  }

  @Override
  public void warning( @NonNull SAXParseException ex ) throws SAXException {
    faults.add( newFault( XmlFault.FaultType.warning, ex ) );
  }

  /**
   * This function can be overridden in order to refined the generated error message.
   * 
   * @param type   The error type. Not <code>null</code>.
   * @param ex     The cause of the failure. Not <code>null</code>.
   * 
   * @return   A freshly created error instance. Not <code>null</code>.
   */
  protected XmlFault newFault( @NonNull XmlFault.FaultType type, @NonNull SAXParseException ex ) {
    return new XmlFault( type, ex );
  }
  
} /* ENDCLASS */
