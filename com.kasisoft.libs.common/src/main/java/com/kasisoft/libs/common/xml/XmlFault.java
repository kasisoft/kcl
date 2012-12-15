/**
 * Name........: XmlFault
 * Description.: Simple datastructure representing a fault within a xml document. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.xml;

import com.kasisoft.lgpl.tools.diagnostic.*;

import org.xml.sax.*;

/**
 * Simple datastructure representing a fault within a xml document.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class XmlFault {
  
  public static enum FaultType {
    warning, error, fatal;
  }

  private FaultType   type;
  private String      message;
  private int         column;
  private int         line;
  
  /**
   * Initialises this datastructure from the supplied exception.
   * 
   * @param faulttype   The kind of issue represented by this record. Not <code>null</code>.
   * @param ex          The original exception. Not <code>null</code>.
   */
  public XmlFault( @KNotNull(name="faulttype") FaultType faulttype, @KNotEmpty(name="ex") SAXParseException ex ) {
    type    = faulttype;
    message = ex.getMessage();
    column  = ex.getColumnNumber();
    line    = ex.getLineNumber();
  }

  /**
   * Initialises this datastructure from the supplied message.
   * 
   * @param faulttype   The kind of issue represented by this record. Not <code>null</code>.
   * @param msg         The original message. Not <code>null</code>.
   */
  public XmlFault( @KNotNull(name="faulttype") FaultType faulttype, @KNotEmpty(name="msg") String msg ) {
    type    = faulttype;
    message = msg;
    column  = -1;
    line    = -1;
  }

  /**
   * Returns a full text representation of this fault used for presentations.
   * 
   * @return   A full text representation of this fault used for presentations.
   *           Neither <code>null</code> nor empty.
   */
  public String getFaultMessage() {
    return String.format( "[%s] ( %d, %d ) : %s", type, Integer.valueOf( line ), Integer.valueOf( column ), message );
  }
  
  /**
   * Returns <code>true</code> if this datastructure is related to a warning.
   * 
   * @return   This datastructure is related to a warning.
   */
  public boolean isWarning() {
    return type == FaultType.warning;
  }

  /**
   * Returns <code>true</code> if this datastructure is related to an error.
   * 
   * @return   This datastructure is related to an error.
   */
  public boolean isError() {
    return type == FaultType.error;
  }

  /**
   * Returns <code>true</code> if this datastructure is related to a fatal error.
   * 
   * @return   This datastructure is related to a fatal error.
   */
  public boolean isFatal() {
    return type == FaultType.fatal;
  }

  /**
   * Returns the failure message.
   * 
   * @return   The failure message. Neither <code>null</code> nor empty.
   */
  public String getMessage() {
    return message;
  }
  
  /**
   * Changes the current message.
   * 
   * @param newmessage   The new message. Neither <code>null</code> nor empty.
   */
  public void setMessage( @KNotEmpty(name="newmessage") String newmessage ) {
    message = newmessage;
  }
  
  /**
   * Returns the conflicting column number.
   * 
   * @return   The conflicting column number.
   */
  public int getColumn() {
    return column;
  }
  
  /**
   * Changes the current column information.
   * 
   * @param newcolumn   The new column.
   */
  public void setColumn( int newcolumn ) {
    column = newcolumn;
  }

  /**
   * Returns the conflicting line number.
   * 
   * @return   The conflicting line number.
   */
  public int getLine() {
    return line;
  }
  
  /**
   * Changes the current line information.
   * 
   * @param newline   The new line information.
   */
  public void setLine( int newline ) {
    line = newline;
  }
  
} /* ENDCLASS */
