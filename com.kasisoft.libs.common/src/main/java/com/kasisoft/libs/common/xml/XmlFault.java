package com.kasisoft.libs.common.xml;

import org.xml.sax.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Simple datastructure representing a fault within a xml document.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class XmlFault {
  
  public enum FaultType {
    warning, error, fatal;
  }

                  FaultType   type;
  
  @Getter @Setter int         line;
  @Getter @Setter int         column;
  
  /** Neither <code>null</code> nor empty. */
  @Getter @Setter String      message;
  
  /**
   * Initialises this datastructure from the supplied exception.
   * 
   * @param faulttype   The kind of issue represented by this record. Not <code>null</code>.
   * @param ex          The original exception. Not <code>null</code>.
   */
  public XmlFault( @NonNull FaultType faulttype, @NonNull SAXParseException ex ) {
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
  public XmlFault( @NonNull FaultType faulttype, @NonNull String msg ) {
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

} /* ENDCLASS */
