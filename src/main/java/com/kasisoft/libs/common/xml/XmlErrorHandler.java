package com.kasisoft.libs.common.xml;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Default implementation of an ErrorHandler.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class XmlErrorHandler implements ErrorHandler {

  int              errorcount;
  List<XmlFault>   faults;
  
  /**
   * Initialises this basic error handler.
   */
  public XmlErrorHandler() {
    errorcount  = 0;
    faults      = new ArrayList<>(50);
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
  public @NotNull XmlFault[] getFaults() {
    return faults.toArray(new XmlFault[faults.size()]);
  }
  
  /**
   * Returns a full text representation of this fault used for presentations.
   * 
   * @return   A full text representation of this fault used for presentations. Neither <code>null</code> nor empty 
   *           if {@link #hasErrors()}.
   */
  public @NotNull String getFaultMessage() {
    var buffer  = new StringBuilder();
    for (var i = 0; i < faults.size(); i++) {
      buffer.append(faults.get(i).getFaultMessage());
      buffer.append('\n');
    }
    return buffer.toString();
  }
  
  @Override
  public void error(@NotNull SAXParseException ex) throws SAXException {
    errorcount++;
    faults.add(newFault(XmlFault.FaultType.error, ex));
  }

  @Override
  public void fatalError(@NotNull SAXParseException ex) throws SAXException {
    errorcount++;
    faults.add( newFault(XmlFault.FaultType.fatal, ex));
  }

  @Override
  public void warning(@NotNull SAXParseException ex) throws SAXException {
    faults.add(newFault(XmlFault.FaultType.warning, ex));
  }

  /**
   * This function can be overridden in order to refined the generated error message.
   * 
   * @param type   The error type. Not <code>null</code>.
   * @param ex     The cause of the failure. Not <code>null</code>.
   * 
   * @return   A freshly created error instance. Not <code>null</code>.
   */
  protected @NotNull XmlFault newFault(@NotNull XmlFault.FaultType type, @NotNull SAXParseException ex) {
    return new XmlFault(type, ex);
  }
  
} /* ENDCLASS */
