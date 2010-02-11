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
public class XmlFault {

  private boolean   warning;
  private String    message;
  private int       column;
  private int       line;
  
  /**
   * Initialises this datastructure from the supplied exception.
   * 
   * @param iswarning   <code>true</code> <=> The cause is a warning.
   * @param ex          The original exception. Not <code>null</code>.
   */
  public XmlFault( boolean iswarning, @KNotEmpty(name="ex") SAXParseException ex ) {
    warning   = iswarning;
    message   = ex.getMessage();
    column    = ex.getColumnNumber();
    line      = ex.getLineNumber();
  }

  /**
   * Returns a full text representation of this fault used for presentations.
   * 
   * @return   A full text representation of this fault used for presentations.
   *           Neither <code>null</code> nor empty.
   */
  public String getFaultMessage() {
    return String.format( "[%s] ( %d, %d ) : %s", warning ? "W" : "E", Integer.valueOf( line ), Integer.valueOf( column ), message );
  }
  
  /**
   * Returns <code>true</code> if this datastructure is related to a warning.
   * 
   * @return   This datastructure is related to a warning.
   */
  public boolean isWarning() {
    return warning;
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
   * Returns the conflicting column number.
   * 
   * @return   The conflicting column number.
   */
  public int getColumn() {
    return column;
  }

  /**
   * Returns the conflicting line number.
   * 
   * @return   The conflicting line number.
   */
  public int getLine() {
    return line;
  }
  
} /* ENDCLASS */
