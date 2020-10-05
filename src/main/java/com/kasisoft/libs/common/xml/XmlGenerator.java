package com.kasisoft.libs.common.xml;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.text.*;

import javax.validation.constraints.*;

import java.util.function.*;

import java.util.*;

import lombok.experimental.FieldDefaults;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.AccessLevel;

/**
 * Helper class which allows to generate xml files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "builder") @ToString(of = "builder")
public class XmlGenerator<T extends XmlGenerator<T>> {

  StringFBuilder                      builder;
  Encoding                            encoding;
  Stack<String>                       tags;
  StringBuilder                       indentation;
  String                              indent;
  
  BiConsumer<Object, Object>          handleInvalidAttribute;
  BiFunction<String, Object, String>  attributeValueConverter;
  
  /**
   * Creates this generator using a certain encoding and a specific indentation size.
   */
  public XmlGenerator() {
    this(null, null);
  }

  /**
   * Creates this generator using a certain encoding and the default indentation size.
   * 
   * @param encoding   The encoding to be used.
   */
  public XmlGenerator(Encoding encoding) {
    this(encoding, null);
  }

  /**
   * Creates this generator using the default encoding and a specific indentation size.
   * 
   * @param indent   The indentation size.
   */
  public XmlGenerator(@Min(0) int indent) {
    this(null, indent);
  }

  /**
   * Creates this generator using a certain encoding and a specific indentation size.
   * 
   * @param csEncoding   The encoding to be used. 
   * @param indentsize   The indentation size.
   */
  public XmlGenerator(Encoding csEncoding, Integer indentsize) {
    builder                 = new StringFBuilder();
    encoding                = Encoding.getEncoding(csEncoding);
    tags                    = new Stack<>();
    indentation             = new StringBuilder();
    indent                  = StringFunctions.fillString(indentsize != null ? indentsize.intValue() : 2, ' ');
    handleInvalidAttribute  = this::handleInvalidAttribute;
    attributeValueConverter = this::attributeValueConverter;
  }
  
  /**
   * Changes the handler which is used when an invalid attribute key has been detected.
   * 
   * @param handler   The handler to be used.
   * 
   * @return   this
   */
  public synchronized @NotNull T withInvalidAttributeHandler(BiConsumer<Object, Object> handler) {
    if (handler != null) {
      handleInvalidAttribute = handler;
    } else {
      handleInvalidAttribute = this::handleInvalidAttribute;
    }
    return (T) this;
  }
  
  /**
   * Changes the converter used for attribute values.
   * 
   * @param converter   The converter which is used for attribute values.
   * 
   * @return   this
   */
  public synchronized @NotNull T withAttributeValueConverter(BiFunction<String, Object, String> converter) {
    if (converter != null) {
      attributeValueConverter = converter;
    } else {
      attributeValueConverter = this::attributeValueConverter;
    }
    return (T) this;
  }
  
  /**
   * The default implementation allowing to deal with unsupported attribute keys.
   * 
   * @param key   The invalid attribute key.
   * @param val   The corresponding attribute value.
   */
  private void handleInvalidAttribute(Object key, Object val) {
  }
  
  /**
   * This converter simply provides the textual presentation of the value.
   * 
   * @param key   The attribute name that is currently being handled.
   * @param val   The attribute value.
   * 
   * @return   The converted attribute value.
   */
  private @NotNull String attributeValueConverter(@NotBlank String key, Object val) {
    return String.valueOf(val);
  }
  
  /**
   * Resets the inner state of this generator allowing it to be reused.
   */
  public synchronized void reset() {
    builder.setLength(0);
    tags.clear();
    indentation.setLength(0);
  }
  
  /**
   * Performs an indent for the upcoming generation.
   */
  private void indent() {
    indentation.append(indent);
  }
  
  /**
   * Performs a dedent for the upcoming generation.
   */
  private void dedent() {
    var newlength = Math.max(indentation.length() - indent.length(), 0);
    indentation.setLength(newlength);
  }
  
  /**
   * Generates a map of attributes from the supplied list of pairs.
   * 
   * @param attributes   A list of pairs establishing the attribute map.
   * 
   * @return   A map of attributes.
   */
  private @NotNull Map<String, Object> asMap(Object ... attributes) {
    Map<String, Object> result = null;
    if ((attributes != null) && (attributes.length >= 2)) {
      result = new HashMap<>();
      for (var i = 0; i < attributes.length; i += 2) {
        var key  = attributes[i];
        var val  = attributes[i + 1];
        var name = key instanceof String ? StringFunctions.cleanup((String) key) : null;
        if (name != null) {
          result.put(name, val); 
        } else {
          handleInvalidAttribute.accept(key, val);
        }
      }
    }
    return asMap(result);
  }
  
  /**
   * Ensures that a non-null map is returned.
   * 
   * @param map   The map that's supposed to be returned if possible.
   * 
   * @return   The supplied map or a substitute.
   */
  private @NotNull Map<String, Object> asMap(@NotNull Map<String, Object> map) {
    if( (map == null) || map.isEmpty() ) {
      return Collections.emptyMap();
    } else {
      return map;
    }
  }

  /**
   * Writes a complete tag. 
   * 
   * @param tag          The tag name.
   * @param attributes   The attributes associated with this tag.
   * 
   * @return   this
   */
  public synchronized @NotNull T tagV(@NotBlank String tag, Object ... attributes) {
    tag (tag, null, asMap(attributes));
    return (T) this;
  }

  /**
   * Writes a complete tag. 
   * 
   * @param tag          The tag name.
   * @param text         The text contained with this tag.
   * @param attributes   The attributes associated with this tag.
   * 
   * @return   this
   */
  public synchronized @NotNull T tagV(@NotBlank String tag, String text, Object ... attributes) {
    tag(tag, text, asMap(attributes));
    return (T) this;
  }

  /**
   * Writes a complete tag. 
   * 
   * @param tag          The tag name.
   * @param attributes   The attributes associated with this tag.
   * 
   * @return   this
   */
  public synchronized @NotNull T tag(@NotBlank String tag, Object[] attributes) {
    tag(tag, null, asMap(attributes));
    return (T) this;
  }

  /**
   * Writes a complete tag. 
   * 
   * @param tag    The tag name.
   * @param text   The text contained with this tag.
   * 
   * @return   this
   */
  public synchronized @NotNull T tag(@NotBlank String tag, String text) {
    tag(tag, text, (Map<String, Object>) null);
    return (T) this;
  }
  
  /**
   * Writes a complete tag. 
   * 
   * @param tag          The tag name.
   * @param text         The text contained with this tag.
   * @param attributes   The attributes associated with this tag.
   * 
   * @return   this
   */
  public synchronized @NotNull T tag(@NotBlank String tag, String text, Object[] attributes) {
    tag(tag, text, asMap(attributes));
    return (T) this;
  }
  
  /**
   * Writes a complete tag. 
   * 
   * @param tag          The tag name.
   * @param attributes   The attributes associated with this tag.
   * 
   * @return   this
   */
  public synchronized @NotNull T tag(@NotBlank String tag, Map<String, Object> attributes) {
    tag(tag, null, attributes);
    return (T) this;
  }
  
  /**
   * Writes a complete tag. 
   * 
   * @param tag          The tag name.
   * @param text         The text contained with this tag.
   * @param attributes   The attributes associated with this tag.
   * 
   * @return   this
   */
  public synchronized @NotNull T tag(String tag, String text, Map<String, Object> attributes) {
    tag        = StringFunctions.cleanup(tag);
    attributes = asMap(attributes);
    text       = StringFunctions.cleanup(text);
    builder.append(indentation);
    builder.append('<').append(tag);
    if (!attributes.isEmpty()) {
      writeAttributes(attributes);
    }
    if (text != null) {
      if (text.indexOf('\n') != -1) {
        builder.append('>').append('\n');
        indent();
        builder.append(indentation).append(escapeXml(text)).append('\n');
        dedent();
        builder.append(indentation);
      } else {
        builder.append('>').append(escapeXml(text));
      }
      builder.append("</").append(tag).append('>').append('\n');
    } else {
      builder.append("/>").append('\n');
    }
    return (T) this;
  }

  /**
   * Opens a tag which potentially contains other tags. 
   * 
   * @param tag          The tag name.
   * @param attributes   The attributes associated with this tag.
   * 
   * @return   this
   */
  public synchronized @NotNull T openTagV(@NotBlank String tag, Object ... attributes) {
    openTag(tag, asMap(attributes));
    return (T) this;
  }

  /**
   * Opens a tag which potentially contains other tags. 
   * 
   * @param tag   The tag name.
   * 
   * @return   this
   */
  public synchronized @NotNull T openTag(@NotBlank String tag) {
    return openTag(tag, (Map<String, Object>) null);
  }
    
  /**
   * Opens a tag which potentially contains other tags. 
   * 
   * @param tag          The tag name.
   * @param attributes   The attributes associated with this tag.
   * 
   * @return   this
   */
  public synchronized @NotNull T openTag(@NotBlank String tag, Object[] attributes) {
    openTag(tag, asMap(attributes));
    return (T) this;
  }
  
  /**
   * Opens a tag which potentially contains other tags. 
   * 
   * @param tag          The tag name.
   * @param attributes   The attributes associated with this tag.
   * 
   * @return   this
   */
  public synchronized @NotNull T openTag(@NotBlank String tag, Map<String, Object> attributes) {
    tag        = StringFunctions.cleanup(tag);
    attributes = asMap(attributes);
    builder.append(indentation);
    builder.append('<').append(tag);
    if (!attributes.isEmpty()) {
      writeAttributes(attributes);
    }
    builder.append('>').append('\n');
    indent();
    tags.push(tag);
    return (T) this;
  }

  /**
   * Closes the last tag that had been opened. 
   * 
   * @return   this
   */
  public synchronized @NotNull T closeTag() {
    if (!tags.isEmpty()) {
      dedent();
      var tag = tags.pop();
      builder.append(indentation).append('<').append('/').append(tag).append('>').append('\n');
    }
    return (T) this;
  }

  /**
   * Writes the supplied text in a CDATA block.
   * 
   * @param text   The text that has to be written in a CDATA block.
   * 
   * @return   this
   */
  public synchronized @NotNull T cdata(String text) {
    if (text != null) {
      builder.appendF("<![CDATA[%s]]>\n", text);
    }
    return (T) this;
  }
  
  /**
   * Writes a processing instruction.
   * 
   * @return   this
   */
  public synchronized @NotNull T processingInstruction() {
    builder.appendF("<?xml version=\"1.0\" encoding=\"%s\"?>\n", encoding.getEncoding());
    return (T) this;
  }

  /**
   * Writes a single line comment.
   * 
   * @param comment   The comment that shall be rendered.
   * 
   * @return   this
   */
  public synchronized @NotNull T comment(String comment) {
    if (comment != null) {
      builder.appendF("%s<!-- %s -->\n", indentation, escapeXml(comment));
    }
    return (T) this;
  }

  /**
   * Writes a multi line comment.
   * 
   * @param comment   The comment that shall be rendered.
   * 
   * @return   this
   */
  public synchronized @NotNull T multilineComment(String comment) {
    if (comment != null) {
      builder.appendF("%s<!-- ~~~~~~~~~~~~~~~~~ \n", indentation);
      var tokenizer = new StringTokenizer(comment, "\n", false);
      while (tokenizer.hasMoreTokens()) {
        var line = StringFunctions.cleanup(tokenizer.nextToken());
        if (line == null) {
          builder.append('\n');
        } else {
          builder.appendF("%s%s\n", indentation, line);
        }
      }
      builder.appendF("%s~~~~~~~~~~~~~~~~~~ -->\n", indentation);
    }
    return (T) this;
  }
  
  /**
   * Writes all attributes sorted by their key names.
   * 
   * @param attributes   The attributes that shall be written.
   */
  private void writeAttributes(@NotNull Map<String, Object> attributes) {
    var keys = new ArrayList<String>(attributes.keySet());
    Collections.sort(keys);
    for (var key : keys) {
      var val = attributeValueConverter.apply(key, attributes.get(key));
      if (val != null) {
        builder.appendF(" %s=\"%s\"", key, escapeXml(val));
      }
    }
  }
  
  private void stop() {
    while (!tags.isEmpty()) {
      closeTag();
    }
  }
  
  /**
   * Returns the current xml content.
   * 
   * @return   The current xml content.
   */
  public synchronized @NotNull String toXml() {
    stop();
    return builder.toString();
  }

  /**
   * Escapes the special characters for XML.
   * 
   * @param text  The text that will require escaping.
   * 
   * @return   The escaped character.
   */
  private @NotNull String escapeXml(@NotNull String text) {
    if (text.length() > 0) {
      return XmlFunctions.escapeXml(text);
    }
    return Empty.NO_STRING;
  }
  
} /* ENDCLASS */
