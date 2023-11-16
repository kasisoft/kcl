package com.kasisoft.libs.common.xml;

import org.xml.sax.*;

import jakarta.validation.constraints.*;

import java.util.*;

/**
 * Default implementation of an ErrorHandler.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class XmlErrorHandler implements ErrorHandler {

    private int            errorcount;
    private List<XmlFault> faults;

    /**
     * Initialises this basic error handler.
     */
    public XmlErrorHandler() {
        errorcount = 0;
        faults     = new ArrayList<>(50);
    }

    /**
     * Returns <code>true</code> if at least one error occured.
     *
     * @return <code>true</code> At least one error occured.
     */
    public boolean hasErrors() {
        return errorcount > 0;
    }

    /**
     * Returns the faults which have been collected.
     *
     * @return The faults which have been collected.
     */
    @NotNull
    public XmlFault[] getFaults() {
        return faults.toArray(new XmlFault[faults.size()]);
    }

    /**
     * Returns a full text representation of this fault used for presentations.
     *
     * @return A full text representation of this fault used for presentations. if {@link #hasErrors()}.
     */
    @NotNull
    public String getFaultMessage() {
        var buffer = new StringBuilder();
        for (var i = 0; i < faults.size(); i++) {
            buffer.append(faults.get(i).getFaultMessage());
            buffer.append('\n');
        }
        return buffer.toString();
    }

    @Override
    public void error(@NotNull SAXParseException ex) throws SAXException {
        errorcount++;
        faults.add(XmlFault.of(XmlFault.FaultType.error, ex));
    }

    @Override
    public void fatalError(@NotNull SAXParseException ex) throws SAXException {
        errorcount++;
        faults.add(XmlFault.of(XmlFault.FaultType.fatal, ex));
    }

    @Override
    public void warning(@NotNull SAXParseException ex) throws SAXException {
        faults.add(XmlFault.of(XmlFault.FaultType.warning, ex));
    }

} /* ENDCLASS */
