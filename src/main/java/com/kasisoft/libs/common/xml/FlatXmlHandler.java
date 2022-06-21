package com.kasisoft.libs.common.xml;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.text.*;

import com.kasisoft.libs.common.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import javax.validation.constraints.*;

import java.util.*;

import java.io.*;

/**
 * Simple converter which allows to create flat representations of a XML document. This converter simply implements a 
 * DefaultHandler used in conjunction with the SAX Parser. An OutputStream must be supplied in order to generate the 
 * output. The OutputStream must be set each time this handler is used since it will be discarded from the handler after 
 * a conversion has taken place.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class FlatXmlHandler extends DefaultHandler {

  private Stack<String>   elements;
  private StringFBuilder  path;
  private StringBuilder   buffer;
  private Encoding        encoding;
  private OutputStream    target;
  private String          newline;
  private boolean         trimValues;
  private boolean         escaping;
  private boolean         attributes;

  /**
   * Initialises this generator. An instance may be used multiple times.
   */
  public FlatXmlHandler() {
    super();
    elements   = new Stack<>();
    path       = new StringFBuilder();
    buffer     = new StringBuilder();
    target     = null;
    trimValues = true;
    attributes = true;
    escaping   = true;
    newline    = KclConfig.LINE_ENDING;
    encoding   = Encoding.UTF8;
  }
  
  public void setTarget(OutputStream target) {
    this.target = target;
  }
  
  public String getNewline() {
    return newline;
  }
  
  /**
   * Changes the current line separator sequence.
   * 
   * @param linesep   The new line separator sequence.
   */
  public void setNewline(String linesep) {
    newline = linesep;
    if (newline == null) {
      newline = KclConfig.LINE_ENDING;
    }
  }
  
  public boolean isTrimValues() {
    return trimValues;
  }
  
  public void setTrimValues(boolean trimValues) {
    this.trimValues = trimValues;
  }
  
  public boolean isEscaping() {
    return escaping;
  }
  
  public void setEscaping(boolean escaping) {
    this.escaping = escaping;
  }
  
  public boolean isAttributes() {
    return attributes;
  }
  
  public void setAttributes(boolean attributes) {
    this.attributes = attributes;
  }
  
  /**
   * Writes the supplied content to the OutputStream.
   * 
   * @param value    The value stored by the key.
   * 
   * @throws SAXException   Writing to the target failed for some reason.
   */
  private void write(@NotNull String value) throws SAXException {
    if (trimValues) {
      value = value.trim();
    }
    try {
      var text = String.format("%s=%s%s", path.toString(), escape(value), newline);
      target.write(encoding.encode(text));
    } catch (Exception ex) {
      throw new SAXException(ex);
    }
  }
  
  /**
   * Creates a simple one line representation of the supplied input.
   * 
   * @param input   The input which has to be broken on one line.
   * 
   * @return   The input which can be placed on one line.
   */
  private String escape(@NotNull String input) {
    if (escaping) {
      input = input.replaceAll("\n", "&#10;");
      input = input.replaceAll("\r", "&#13;");
    }
    return input;
  }
  
  @Override
  public void startDocument() throws SAXException {
    elements.clear();
    path.setLength(0);
    buffer.setLength(0);
    if (target == null) {
      target = System.out;
    }
  }

  @Override
  public void endDocument() throws SAXException {
    if (target == System.out) {
      target = null;
    }
  }
  
  @Override
  public void startElement(String uri, String localname, String qname, Attributes attributes) throws SAXException {
    var name = qname != null ? qname : localname;
    elements.push(name);
    path.appendF("%s/", name);
    if (this.attributes) {
      var names = new String[attributes.getLength()];
      for (var i = 0; i < attributes.getLength(); i++) {
        names[i] = attributes.getQName(i);
      }
      Arrays.sort(names);
      for (var i = 0; i < names.length; i++) {
        path.appendF("@%s", names[i]);
        write(attributes.getValue(names[i]));
        path.setLength(path.length() - names[i].length() - 1);
      }
    }
  }

  @Override
  public void endElement(String uri, String localname, String qname) throws SAXException {
    path.append("text()");
    write(buffer.toString());
    var name = elements.pop();
    path.setLength(path.length() - name.length() - 1 - "text()".length());
    buffer.setLength(0);
  }

  @Override
  public void characters(@NotNull char[] ch, int start, int length) throws SAXException {
    buffer.append(ch, start, length);
  }

} /* ENDCLASS */
