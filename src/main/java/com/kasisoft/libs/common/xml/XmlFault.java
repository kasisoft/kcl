package com.kasisoft.libs.common.xml;

import org.xml.sax.*;

import javax.validation.constraints.*;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.Data;

/**
 * Simple datastructure representing a fault within a xml document.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class XmlFault {
  
  public enum FaultType {
    warning, error, fatal;
  } /* ENDENUM */

  FaultType   type;
  int         line;
  int         column;
  String      message;
  
  /**
   * Initialises this datastructure from the supplied exception.
   * 
   * @param faulttype   The kind of issue represented by this record.
   * @param ex          The original exception.
   */
  public XmlFault(@NotNull FaultType faulttype, @NotNull SAXParseException ex) {
    type    = faulttype;
    message = ex.getMessage();
    column  = ex.getColumnNumber();
    line    = ex.getLineNumber();
  }

  /**
   * Initialises this datastructure from the supplied message.
   * 
   * @param faulttype   The kind of issue represented by this record.
   * @param msg         The original message.
   */
  public XmlFault(@NotNull FaultType faulttype, @NotNull String msg) {
    type    = faulttype;
    message = msg;
    column  = -1;
    line    = -1;
  }

  /**
   * Returns a full text representation of this fault used for presentations.
   * 
   * @return   A full text representation of this fault used for presentations.
   */
  public @NotBlank  String getFaultMessage() {
    return String.format("[%s] ( %d, %d ) : %s", type, line, column, message);
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
