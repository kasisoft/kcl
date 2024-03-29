package com.kasisoft.libs.common.xml;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.text.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

import java.util.*;

/**
 * Helper class which allows to generate xml files.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class XmlGenerator<T extends XmlGenerator<T>> {

    private StringBuilder                      builder;
    private Encoding                           encoding;
    private Stack<String>                      tags;
    private StringBuilder                      indentation;
    private String                             indent;
    private BiConsumer<Object, Object>         handleInvalidAttribute;
    private BiFunction<String, Object, String> attributeValueConverter;

    /**
     * Creates this generator using a certain encoding and a specific indentation size.
     */
    public XmlGenerator() {
        this(null, null);
    }

    /**
     * Creates this generator using a certain encoding and the default indentation size.
     *
     * @param encoding
     *            The encoding to be used.
     */
    public XmlGenerator(Encoding encoding) {
        this(encoding, null);
    }

    /**
     * Creates this generator using the default encoding and a specific indentation size.
     *
     * @param indent
     *            The indentation size.
     */
    public XmlGenerator(@Min(0) int indent) {
        this(null, indent);
    }

    /**
     * Creates this generator using a certain encoding and a specific indentation size.
     *
     * @param csEncoding
     *            The encoding to be used.
     * @param indentsize
     *            The indentation size.
     */
    public XmlGenerator(Encoding csEncoding, Integer indentsize) {
        builder                 = new StringBuilder();
        encoding                = Encoding.getEncoding(csEncoding);
        tags                    = new Stack<>();
        indentation             = new StringBuilder();
        indent                  = " ".repeat(indentsize != null ? indentsize.intValue() : 2);
        handleInvalidAttribute  = this::handleInvalidAttribute;
        attributeValueConverter = this::attributeValueConverter;
    }

    /**
     * Changes the handler which is used when an invalid attribute key has been detected.
     *
     * @param handler
     *            The handler to be used.
     * @return this
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public synchronized T withInvalidAttributeHandler(BiConsumer<Object, Object> handler) {
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
     * @param converter
     *            The converter which is used for attribute values.
     * @return this
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public synchronized T withAttributeValueConverter(BiFunction<String, Object, String> converter) {
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
     * @param key
     *            The invalid attribute key.
     * @param val
     *            The corresponding attribute value.
     */
    private void handleInvalidAttribute(Object key, Object val) {
    }

    /**
     * This converter simply provides the textual presentation of the value.
     *
     * @param key
     *            The attribute name that is currently being handled.
     * @param val
     *            The attribute value.
     * @return The converted attribute value.
     */
    @NotNull
    private String attributeValueConverter(@NotBlank String key, Object val) {
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
     * @param attributes
     *            A list of pairs establishing the attribute map.
     * @return A map of attributes.
     */
    @NotNull
    private Map<String, Object> asMap(Object ... attributes) {
        Map<String, Object> result = null;
        if ((attributes != null) && (attributes.length >= 2)) {
            result = new HashMap<>();
            for (var i = 0; i < attributes.length; i += 2) {
                var key  = attributes[i];
                var val  = attributes[i + 1];
                var name = key instanceof String str ? StringFunctions.cleanup(str) : null;
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
     * @param map
     *            The map that's supposed to be returned if possible.
     * @return The supplied map or a substitute.
     */
    @NotNull
    private Map<String, Object> asMap(@NotNull Map<String, Object> map) {
        if ((map == null) || map.isEmpty()) {
            return Collections.emptyMap();
        } else {
            return map;
        }
    }

    /**
     * Writes a complete tag.
     *
     * @param tag
     *            The tag name.
     * @param attributes
     *            The attributes associated with this tag.
     * @return this
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public synchronized T tagV(@NotBlank String tag, Object ... attributes) {
        tag(tag, null, asMap(attributes));
        return (T) this;
    }

    /**
     * Writes a complete tag.
     *
     * @param tag
     *            The tag name.
     * @param text
     *            The text contained with this tag.
     * @param attributes
     *            The attributes associated with this tag.
     * @return this
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public synchronized T tagV(@NotBlank String tag, String text, Object ... attributes) {
        tag(tag, text, asMap(attributes));
        return (T) this;
    }

    /**
     * Writes a complete tag.
     *
     * @param tag
     *            The tag name.
     * @param attributes
     *            The attributes associated with this tag.
     * @return this
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public synchronized T tag(@NotBlank String tag, Object[] attributes) {
        tag(tag, null, asMap(attributes));
        return (T) this;
    }

    /**
     * Writes a complete tag.
     *
     * @param tag
     *            The tag name.
     * @param text
     *            The text contained with this tag.
     * @return this
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public synchronized T tag(@NotBlank String tag, String text) {
        tag(tag, text, (Map<String, Object>) null);
        return (T) this;
    }

    /**
     * Writes a complete tag.
     *
     * @param tag
     *            The tag name.
     * @param text
     *            The text contained with this tag.
     * @param attributes
     *            The attributes associated with this tag.
     * @return this
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public synchronized T tag(@NotBlank String tag, String text, Object[] attributes) {
        tag(tag, text, asMap(attributes));
        return (T) this;
    }

    /**
     * Writes a complete tag.
     *
     * @param tag
     *            The tag name.
     * @param attributes
     *            The attributes associated with this tag.
     * @return this
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public synchronized T tag(@NotBlank String tag, Map<String, Object> attributes) {
        tag(tag, null, attributes);
        return (T) this;
    }

    /**
     * Writes a complete tag.
     *
     * @param tag
     *            The tag name.
     * @param text
     *            The text contained with this tag.
     * @param attributes
     *            The attributes associated with this tag.
     * @return this
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public synchronized T tag(String tag, String text, Map<String, Object> attributes) {
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
     * @param tag
     *            The tag name.
     * @param attributes
     *            The attributes associated with this tag.
     * @return this
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public synchronized T openTagV(@NotBlank String tag, Object ... attributes) {
        openTag(tag, asMap(attributes));
        return (T) this;
    }

    /**
     * Opens a tag which potentially contains other tags.
     *
     * @param tag
     *            The tag name.
     * @return this
     */
    @NotNull
    public synchronized T openTag(@NotBlank String tag) {
        return openTag(tag, (Map<String, Object>) null);
    }

    /**
     * Opens a tag which potentially contains other tags.
     *
     * @param tag
     *            The tag name.
     * @param attributes
     *            The attributes associated with this tag.
     * @return this
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public synchronized T openTag(@NotBlank String tag, Object[] attributes) {
        openTag(tag, asMap(attributes));
        return (T) this;
    }

    /**
     * Opens a tag which potentially contains other tags.
     *
     * @param tag
     *            The tag name.
     * @param attributes
     *            The attributes associated with this tag.
     * @return this
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public synchronized T openTag(@NotBlank String tag, Map<String, Object> attributes) {
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
     * @return this
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public synchronized T closeTag() {
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
     * @param text
     *            The text that has to be written in a CDATA block.
     * @return this
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public synchronized T cdata(String text) {
        if (text != null) {
            builder.append("<![CDATA[%s]]>\n".formatted(text));
        }
        return (T) this;
    }

    /**
     * Writes a processing instruction.
     *
     * @return this
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public synchronized T processingInstruction() {
        builder.append("<?xml version=\"1.0\" encoding=\"%s\"?>\n".formatted(encoding.getEncoding()));
        return (T) this;
    }

    /**
     * Writes a single line comment.
     *
     * @param comment
     *            The comment that shall be rendered.
     * @return this
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public synchronized T comment(String comment) {
        if (comment != null) {
            builder.append("%s<!-- %s -->\n".formatted(indentation, escapeXml(comment)));
        }
        return (T) this;
    }

    /**
     * Writes a multi line comment.
     *
     * @param comment
     *            The comment that shall be rendered.
     * @return this
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public synchronized T multilineComment(String comment) {
        if (comment != null) {
            builder.append("%s<!-- ~~~~~~~~~~~~~~~~~\n".formatted(indentation));
            var parts = comment.trim().split("\n");
            for (var line : parts) {
                if (line.isBlank()) {
                    builder.append("\n");
                } else {
                    builder.append("%s%s\n".formatted(indentation, line));
                }
            }
            builder.append("%s~~~~~~~~~~~~~~~~~~ -->\n".formatted(indentation));
        }
        return (T) this;
    }

    /**
     * Writes all attributes sorted by their key names.
     *
     * @param attributes
     *            The attributes that shall be written.
     */
    private void writeAttributes(@NotNull Map<String, Object> attributes) {
        var keys = new ArrayList<String>(attributes.keySet());
        Collections.sort(keys);
        for (var key : keys) {
            var val = attributeValueConverter.apply(key, attributes.get(key));
            if (val != null) {
                builder.append(" %s=\"%s\"".formatted(key, escapeXml(val)));
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
     * @return The current xml content.
     */
    @NotNull
    public synchronized String toXml() {
        stop();
        return builder.toString();
    }

    /**
     * Escapes the special characters for XML.
     *
     * @param text
     *            The text that will require escaping.
     * @return The escaped character.
     */
    @NotNull
    private String escapeXml(@NotNull String text) {
        if (text.length() > 0) {
            return XmlFunctions.escapeXml(text);
        }
        return Empty.NO_STRING;
    }

    @Override
    public int hashCode() {
        final int prime  = 31;
        int       result = 1;
        result = prime * result + ((builder == null) ? 0 : builder.hashCode());
        return result;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        var other = (XmlGenerator) obj;
        if (builder == null) {
            if (other.builder != null)
                return false;
        } else if (!builder.equals(other.builder))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "XmlGenerator [builder=" + builder + "]";
    }

    // makes usage somewhat prettier as inheriting XmlGenerator is often unnecessary
    public static class DefaultXmlGenerator extends XmlGenerator<DefaultXmlGenerator> {

        public DefaultXmlGenerator() {
            super();
        }

        public DefaultXmlGenerator(Encoding encoding) {
            super(encoding);
        }

        public DefaultXmlGenerator(@Min(0) int indent) {
            super(indent);
        }

        public DefaultXmlGenerator(Encoding csEncoding, Integer indentsize) {
            super(csEncoding, indentsize);
        }

    } /* ENDCLASS */

} /* ENDCLASS */
