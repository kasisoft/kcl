package com.kasisoft.libs.common.xml;

import org.xml.sax.*;

import jakarta.validation.constraints.*;

/**
 * Simple datastructure representing a fault within a xml document.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class XmlFault {

  public enum FaultType {
    warning, error, fatal;
  } /* ENDENUM */

  private FaultType   type;
  private int         line;
  private int         column;
  private String      message;

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
    return "[%s] ( %d, %d ) : %s".formatted(type, line, column, message);
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

  public FaultType getType() {
    return type;
  }

  public void setType(FaultType type) {
    this.type = type;
  }

  public int getLine() {
    return line;
  }

  public void setLine(int line) {
    this.line = line;
  }

  public int getColumn() {
    return column;
  }

  public void setColumn(int column) {
    this.column = column;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + column;
    result = prime * result + line;
    result = prime * result + ((message == null) ? 0 : message.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    XmlFault other = (XmlFault) obj;
    if (column != other.column)
      return false;
    if (line != other.line)
      return false;
    if (message == null) {
      if (other.message != null)
        return false;
    } else if (!message.equals(other.message))
      return false;
    if (type != other.type)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "XmlFault [type=" + type + ", line=" + line + ", column=" + column + ", message=" + message + "]";
  }

} /* ENDCLASS */
