package com.kasisoft.libs.common.xml;

import org.xml.sax.*;

import jakarta.validation.constraints.*;

/**
 * Simple datastructure representing a fault within a xml document.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public record XmlFault(FaultType type, int line, int column, String message) {

    public enum FaultType {
        warning, error, fatal;
    } /* ENDENUM */

    public static XmlFault of(@NotNull FaultType faulttype, @NotNull SAXParseException ex) {
        return new XmlFault(faulttype, ex.getLineNumber(), ex.getColumnNumber(), ex.getMessage());
    }

    public static XmlFault of(@NotNull FaultType faulttype, @NotNull String msg) {
        return new XmlFault(faulttype, -1, -1, msg);
    }

    /**
     * Returns a full text representation of this fault used for presentations.
     *
     * @return A full text representation of this fault used for presentations.
     */
    @NotBlank
    public String getFaultMessage() {
        return "[%s] ( %d, %d ) : %s".formatted(type, line, column, message);
    }

    /**
     * Returns <code>true</code> if this datastructure is related to a warning.
     *
     * @return This datastructure is related to a warning.
     */
    public boolean isWarning() {
        return type == FaultType.warning;
    }

    /**
     * Returns <code>true</code> if this datastructure is related to an error.
     *
     * @return This datastructure is related to an error.
     */
    public boolean isError() {
        return type == FaultType.error;
    }

    /**
     * Returns <code>true</code> if this datastructure is related to a fatal error.
     *
     * @return This datastructure is related to a fatal error.
     */
    public boolean isFatal() {
        return type == FaultType.fatal;
    }

} /* ENDRECORD */
