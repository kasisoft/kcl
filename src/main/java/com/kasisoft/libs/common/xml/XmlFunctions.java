package com.kasisoft.libs.common.xml;

import org.xml.sax.*;

import org.w3c.dom.*;

import com.kasisoft.libs.common.functional.*;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.text.*;

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import java.lang.reflect.*;

import java.util.function.*;

import java.util.*;

import java.nio.file.*;

import java.net.*;

import java.io.*;

/**
 * Collection of xml related functions.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
@SuppressWarnings("unchecked")
public final class XmlFunctions {

    private static final Map<String, String> XML2NORMAL    = new HashMap<>();
    private static final Map<String, String> XML2NORMAL_LE = new HashMap<>();
    private static final Map<String, String> NORMAL2XML    = new HashMap<>();
    private static final Map<String, String> NORMAL2XML_LE = new HashMap<>();

    static {

        XML2NORMAL.put("&apos;", "'");
        XML2NORMAL.put("&gt;", ">");
        XML2NORMAL.put("&lt;", "<");
        XML2NORMAL.put("&amp;", "&");
        XML2NORMAL.put("&quot;", "\"");

        XML2NORMAL_LE.putAll(XML2NORMAL);
        XML2NORMAL_LE.put("\\n", "\n");
        XML2NORMAL_LE.put("\\r", "\r");
        XML2NORMAL_LE.put("&#10;", "\n");
        XML2NORMAL_LE.put("&#13;", "\r");

        NORMAL2XML.put("'", "&apos;");
        NORMAL2XML.put(">", "&gt;");
        NORMAL2XML.put("<", "&lt;");
        NORMAL2XML.put("&", "&amp;");
        NORMAL2XML.put("\"", "&quot;");

        NORMAL2XML_LE.putAll(NORMAL2XML);
        NORMAL2XML_LE.put("\n", "\\n");
        NORMAL2XML_LE.put("\r", "\\r");
        NORMAL2XML_LE.put("\n", "&#10;");
        NORMAL2XML_LE.put("\r", "&#13;");

    }

    public static TransformerFactory newTransformerFactory() {
        return TransformerFactory.newInstance();
    }

    @NotNull
    public static Function<@NotNull Element, String> getAttribute(@NotBlank String attribute) {
        return $ -> StringFunctions.cleanup($.getAttribute(attribute));
    }

    /**
     * Reads the content of the supplied stream.
     *
     * @param input
     *            The stream which provides the xml content.
     * @param config
     *            A configuration for the xml parser.
     * @return The Document node itself.
     */
    @NotNull
    public static Document readDocument(@NotNull URL input, @NotNull XmlParserConfiguration config) {
        return IoSupportFunctions.forInputStream(input, $ -> readDocument($, config));
    }

    /**
     * Reads the content of the supplied stream.
     *
     * @param input
     *            The stream which provides the xml content.
     * @param config
     *            A configuration for the xml parser.
     * @return The Document node itself.
     */
    @NotNull
    public static Document readDocument(@NotNull URI input, @NotNull XmlParserConfiguration config) {
        return IoSupportFunctions.forInputStream(input, $ -> readDocument($, config));
    }

    /**
     * Reads the content of the supplied stream.
     *
     * @param input
     *            The stream which provides the xml content.
     * @param config
     *            A configuration for the xml parser.
     * @return The Document node itself.
     */
    @NotNull
    public static Document readDocument(@NotNull File input, @NotNull XmlParserConfiguration config) {
        return IoSupportFunctions.forInputStream(input, $ -> readDocument($, config));
    }

    /**
     * Reads the content of the supplied stream.
     *
     * @param input
     *            The stream which provides the xml content.
     * @param config
     *            A configuration for the xml parser.
     * @return The Document node itself.
     */
    @NotNull
    public static Document readDocument(@NotNull Path input, @NotNull XmlParserConfiguration config) {
        return IoSupportFunctions.forInputStream(input, $ -> readDocument($, config));
    }

    /**
     * Reads the content of the supplied stream.
     *
     * @param input
     *            The stream which provides the xml content.
     * @param config
     *            A configuration for the xml parser.
     * @return The Document node itself.
     */
    @NotNull
    public static Document readDocument(@NotNull Reader input, @NotNull XmlParserConfiguration config) {
        return readDocument(config, $ -> {
            var insource = new InputSource(input);
            if (config.baseurl() != null) {
                insource.setSystemId(config.baseurl().toExternalForm());
            }
            return $.parse(insource);
        });
    }

    /**
     * Reads the content of the supplied stream.
     *
     * @param input
     *            The stream which provides the xml content.
     * @param config
     *            A configuration for the xml parser.
     * @return The Document node itself.
     */
    @NotNull
    public static Document readDocument(@NotNull InputStream input, @NotNull XmlParserConfiguration config) {
        return readDocument(config, $ -> {
            if (config.baseurl() != null) {
                return $.parse(input, config.baseurl().toExternalForm());
            } else {
                return $.parse(input);
            }
        });
    }

    @NotNull
    private static Document readDocument(@NotNull XmlParserConfiguration config, @NotNull KFunction<DocumentBuilder, Document> parse) {
        var builder = newDocumentBuilder(config);
        try {
            var result    = parse.apply(builder);
            var domconfig = result.getDomConfig();
            config.parameters().forEach((k, v) -> k.set(domconfig, v));
            if (config.normalize()) {
                result.normalizeDocument();
            }
            return result;
        } catch (Exception ex) {
            throw KclException.wrap(ex);
        }
    }

    /**
     * Creates a {@link DocumentBuilder} instance for the supplied parser configuration.
     *
     * @param config
     *            A configuration for the xml parser.
     * @return The {@link DocumentBuilder} instance.
     * @throws KclException
     *             Configuring the builder failed for some reason.
     */
    @NotNull
    public static DocumentBuilder newDocumentBuilder(@NotNull XmlParserConfiguration config) {

        var factory = DocumentBuilderFactory.newInstance();

        factory.setNamespaceAware(config.xmlnamespaces());
        factory.setValidating(config.validate());

        try {
            Method method = factory.getClass().getMethod("XIncludeAware", Boolean.TYPE);
            method.invoke(factory, Boolean.valueOf(config.xincludes()));
        } catch (Exception ex) {
            // no effect here
        }

        try {

            var result = factory.newDocumentBuilder();

            if (config.satisfyUnknownSchemas()) {
                if (config.resolver() != null) {
                    result.setEntityResolver(new SatisfyingEntityResolver(config.resolver()));
                } else {
                    result.setEntityResolver(new SatisfyingEntityResolver());
                }
            } else {
                if (config.resolver() != null) {
                    result.setEntityResolver(config.resolver());
                }
            }

            if (config.handler() != null) {
                result.setErrorHandler(config.handler());
            } else {
                result.setErrorHandler(new XmlErrorHandler());
            }

            return result;

        } catch (Exception ex) {
            throw KclException.wrap(ex);
        }

    }

    private static String toDocumentMethod(@NotNull Node node) {
        var result   = "xml";
        var document = node.getOwnerDocument();
        if ((document != null) && (document.getDocumentElement() != null)) {
            if ("html".equalsIgnoreCase(document.getDocumentElement().getTagName())) {
                result = "html";
            }
        }
        return result;
    }

    private static Transformer toTransformer(@NotNull String method, @NotNull Encoding encoding) throws TransformerConfigurationException {

        var factory = newTransformerFactory();
        var result  = factory.newTransformer();

        result.setOutputProperty(OutputKeys.METHOD, method);
        result.setOutputProperty(OutputKeys.INDENT, "yes");
        result.setOutputProperty(OutputKeys.ENCODING, encoding.getEncoding());
        result.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        // a transformer can generate output in different formats, so it doesn't know
        // about the target format which means that we have to alter the pi by our own
        result.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        return result;
    }

    /**
     * Writes the XML content from a DOM tree into an OutputStream.
     *
     * @param output
     *            The OutputStream used to receive the content.
     * @param node
     *            The DOM tree which will be saved.
     * @param encoding
     *            The encoding to use while saving.
     * @throws KclException
     *             Saving the XML datastructure failed.
     */
    public static void writeDocument(@NotNull Path output, @NotNull Node node, Encoding encoding) {
        IoSupportFunctions.forWriterDo(output, encoding, $ -> writeDocument($, node, encoding));
    }

    /**
     * Writes the XML content from a DOM tree into an OutputStream.
     *
     * @param output
     *            The OutputStream used to receive the content.
     * @param node
     *            The DOM tree which will be saved.
     * @param encoding
     *            The encoding to use while saving.
     * @throws KclException
     *             Saving the XML datastructure failed.
     */
    public static void writeDocument(@NotNull File output, @NotNull Node node, Encoding encoding) {
        IoSupportFunctions.forWriterDo(output, encoding, $ -> writeDocument($, node, encoding));
    }

    /**
     * Writes the XML content from a DOM tree into an OutputStream.
     *
     * @param output
     *            The OutputStream used to receive the content.
     * @param node
     *            The DOM tree which will be saved.
     * @param encoding
     *            The encoding to use while saving.
     * @throws KclException
     *             Saving the XML datastructure failed.
     */
    public static void writeDocument(@NotNull OutputStream output, @NotNull Node node, Encoding encoding) {
        writeDocument(new StreamResult(output), node, encoding, ($x, $e) -> {
            output.write($e.encode($x));
            output.flush();
        });
    }

    /**
     * Writes the XML content from a DOM tree into an OutputStream.
     *
     * @param writer
     *            The Writer used to receive the content.
     * @param node
     *            The DOM tree which will be saved.
     * @param encoding
     *            The encoding to use while saving.
     * @throws KclException
     *             Saving the XML datastructure failed.
     */
    public static void writeDocument(@NotNull Writer writer, @NotNull Node node, Encoding encoding) {
        writeDocument(new StreamResult(writer), node, encoding, ($x, $e) -> {
            writer.write($x);
            writer.flush();
        });
    }

    private static void writeDocument(StreamResult streamResult, @NotNull Node node, Encoding encoding, KBiConsumer<String, Encoding> handleXmlDecl) {

        try {

            encoding = Encoding.getEncoding(encoding);
            var method      = toDocumentMethod(node);
            var transformer = toTransformer(method, encoding);
            var xmldecl     = "<?xml version=\"1.0\" encoding=\"%s\"?>\n".formatted(encoding.getEncoding());

            handleXmlDecl.accept(xmldecl, encoding);

            transformer.transform(new DOMSource(node), streamResult);

        } catch (Exception ex) {
            throw KclException.wrap(ex);
        }

    }

    /**
     * Sets up a new transformer from the supplied stylesheet file. This transformer can be used to
     * convert xml documents in various outcomes.
     *
     * @param xsl
     *            The xslt stylesheet.
     * @return The transformer if the stylesheet could be loaded properly.
     * @throws KclException
     *             if loading the stylesheet failed for some reason.
     */
    @NotNull
    public static Transformer newTransformer(@NotNull Path xsl) {
        return IoSupportFunctions.forInputStream(xsl, $ -> newTransformer($));
    }

    /**
     * Sets up a new transformer from the supplied stylesheet file. This transformer can be used to
     * convert xml documents in various outcomes.
     *
     * @param xsl
     *            The xslt stylesheet.
     * @return The transformer if the stylesheet could be loaded properly.
     * @throws KclException
     *             if loading the stylesheet failed for some reason.
     */
    @NotNull
    public static Transformer newTransformer(@NotNull URI xsl) {
        return IoSupportFunctions.forInputStream(xsl, $ -> newTransformer($));
    }

    /**
     * Sets up a new transformer from the supplied stylesheet file. This transformer can be used to
     * convert xml documents in various outcomes.
     *
     * @param xsl
     *            The xslt stylesheet.
     * @return The transformer if the stylesheet could be loaded properly.
     * @throws KclException
     *             if loading the stylesheet failed for some reason.
     */
    @NotNull
    public static Transformer newTransformer(@NotNull File xsl) {
        return IoSupportFunctions.forInputStream(xsl, $ -> newTransformer($));
    }

    /**
     * Sets up a new transformer from the supplied stylesheet resource. This transformer can be used to
     * convert xml documents in various outcomes.
     *
     * @param resource
     *            The xslt stylesheet resource.
     * @return The transformer if the stylesheet could be loaded properly.
     * @throws KclException
     *             if loading the stylesheet failed for some reason.
     */
    @NotNull
    public static Transformer newTransformer(@NotNull URL resource) {
        try (var instream = resource.openStream()) {
            return newTransformer(instream);
        } catch (Exception ex) {
            throw KclException.wrap(ex);
        }
    }

    /**
     * Sets up a new transformer from the supplied stylesheet InputStream. This transformer can be used
     * to convert xml documents in various outcomes.
     *
     * @param xslinstream
     *            The xslt stylesheet provided by an InputStream.
     * @return The transformer if the stylesheet could be loaded properly.
     * @throws KclException
     *             If loading the stylesheet failed for some reason.
     */
    @NotNull
    public static Transformer newTransformer(@NotNull InputStream xslinstream) {
        try {
            return newTransformerFactory().newTransformer(new StreamSource(xslinstream));
        } catch (Exception ex) {
            throw KclException.wrap(ex);
        }
    }

    /**
     * Makes sure that a child gets inserted at the first position of a parent.
     *
     * @param parent
     *            The parent which will be extended.
     * @param child
     *            The child which has to be inserted to the first position.
     */
    public static void insertFirst(@NotNull Element parent, @NotNull Node child) {
        if (parent.getFirstChild() != null) {
            parent.insertBefore(child, parent.getFirstChild());
        } else {
            parent.appendChild(child);
        }
    }

    /**
     * Simple helper function which allows to easily create an element.
     *
     * @param doc
     *            The document which will own the returned element.
     * @param tag
     *            The tag for the element.
     * @param content
     *            The textual content.
     * @param attrs
     *            A list of pairs representing the attributes.
     * @return An Element which contains all supplied informations.
     */
    @NotNull
    public static Element createElement(@NotNull Document doc, @NotBlank String tag, @NotNull String content, String ... attrs) {
        var result = doc.createElement(tag);
        if (content != null) {
            result.appendChild(doc.createTextNode(content));
        }
        if (attrs != null) {
            for (var i = 0; i < attrs.length; i += 2) {
                result.setAttribute(attrs[i], attrs[i + 1]);
            }
        }
        return result;
    }

    /**
     * Removes the supplied list of nodes.
     *
     * @param nodes
     *            A list of nodes which have to be removed from the DOM tree.
     */
    public static void removeNodes(NodeList nodes) {
        if ((nodes != null) && (nodes.getLength() > 0)) {
            var parent = nodes.item(0).getParentNode();
            for (var i = nodes.getLength() - 1; i >= 0; i--) {
                var current = nodes.item(i);
                parent.removeChild(current);
            }
        }
    }

    /**
     * Collects the child nodes from a parent using a specific name.
     *
     * @param parent
     *            The parent node which children have to be returned.
     * @param relevant
     *            A list of interesting element names. If null all elements will be returned.
     * @return A list with all matching elements.
     */
    @NotNull
    public static List<Element> getChildElements(@NotNull Node parent, String ... relevant) {
        var childnodes = parent.getChildNodes();
        if ((childnodes != null) && (childnodes.getLength() > 0)) {
            var               tagnames = new HashSet<String>(Arrays.asList(relevant));
            Predicate<String> validTag = tagnames.isEmpty() ? Predicates.acceptAll() : tagnames::contains;
            var               result   = new ArrayList<Element>(childnodes.getLength());
            for (var i = 0; i < childnodes.getLength(); i++) {
                var current = childnodes.item(i);
                if (current.getNodeType() == Node.ELEMENT_NODE) {
                    var element = (Element) current;
                    if (validTag.test(element.getTagName()) || validTag.test(element.getLocalName())) {
                        result.add(element);
                    }
                }
            }
            return result;
        }
        return Collections.emptyList();
    }

    @NotNull
    public static <T extends Node> List<T> getChildNodes(NodeList nodeList) {
        var result = Collections.<T> emptyList();
        if ((nodeList != null) && (nodeList.getLength() > 0)) {
            result = new ArrayList<>(nodeList.getLength());
            for (var i = 0; i < nodeList.getLength(); i++) {
                result.add((T) nodeList.item(i));
            }
        }
        return result;
    }

    public static <T extends Node> void forNodeDo(@NotNull Consumer<T> handler, @NotNull Predicate<Node> test, @NotNull Node parent, boolean recursive) {
        var childnodes = parent.getChildNodes();
        if ((childnodes != null) && (childnodes.getLength() > 0)) {
            for (var i = 0; i < childnodes.getLength(); i++) {
                var current = childnodes.item(i);
                if (test.test(current)) {
                    handler.accept((T) current);
                }
                if (recursive) {
                    forNodeDo(handler, test, current, recursive);
                }
            }
        }
    }

    public static Element findElement(@NotNull Element parent, @NotNull String tag) {
        var children = getChildElements(parent, tag);
        return (!children.isEmpty()) ? children.get(0) : null;
    }

    @NotNull
    public static Function<@NotNull Element, String> getElementText(@NotBlank String tag) {
        return $ -> getElementText($, tag);
    }

    public static String getElementText(@NotNull Element parent, @NotNull String tag) {
        var element = findElement(parent, tag);
        return element != null ? StringFunctions.cleanup(element.getTextContent()) : null;
    }

    /**
     * Returns a map with all attributes.
     *
     * @param node
     *            The node used to retrieve all attributes.
     * @param namespace
     *            <code>true</code> <=> Include the namespace in the attribute name.
     * @return A list with all matching elements.
     */
    @NotNull
    public static Map<String, Attr> getAttributes(@NotNull Node node, boolean namespace) {
        var attributes = node.getAttributes();
        if (attributes != null) {
            Function<Attr, String> toName = namespace ? XmlFunctions::attrFqName : XmlFunctions::attrName;
            var                    result = new TreeMap<String, Attr>();
            for (var i = 0; i < attributes.getLength(); i++) {
                var attribute = (Attr) attributes.item(i);
                result.put(toName.apply(attribute), attribute);
            }
            return result;
        } else {
            return Collections.emptyMap();
        }
    }

    private static String attrName(@NotNull Attr attribute) {
        return attribute.getName();
    }

    private static String attrFqName(Attr attribute) {
        var uri = attribute.getNamespaceURI();
        if (uri != null) {
            return "{%s}%s".formatted(uri, attribute.getLocalName());
        } else {
            return attribute.getName();
        }
    }

    /**
     * Decodes a String in place while replacing XML entities with their textual representation.
     *
     * @param source
     *            A String that may be modified.
     * @return An encoded String.
     */
    @NotNull
    public static String unescapeXml(@NotNull String source) {
        return unescapeXml(source, false);
    }

    /**
     * Decodes a String in place while replacing XML entities with their textual representation.
     *
     * @param source
     *            A String that may be modified.
     * @param lineEndings
     *            <code>true</code> <=> Escape line endings as well.
     * @return An encoded String.
     */
    @NotNull
    public static String unescapeXml(@NotNull String source, boolean lineEndings) {
        return StringFunctions.replaceAll(source, lineEndings ? XML2NORMAL_LE : XML2NORMAL);
    }

    /**
     * Encodes a String in place while replacing literals into corresponding XML entities.
     *
     * @param source
     *            A String that may be modified.
     * @return An encoded String.
     */
    @NotNull
    public static String escapeXml(@NotNull String source) {
        return escapeXml(source, false);
    }

    /**
     * Encodes a String in place while replacing literals into corresponding XML entities.
     *
     * @param source
     *            A String that may be modified.
     * @param lineEndings
     *            <code>true</code> <=> Escape line endings as well.
     * @return An encoded String.
     */
    @NotNull
    public static String escapeXml(@NotNull String source, boolean lineEndings) {
        return StringFunctions.replaceAll(source, lineEndings ? NORMAL2XML_LE : NORMAL2XML);
    }

    private static class SatisfyingEntityResolver implements EntityResolver {

        private EntityResolver resolver;

        public SatisfyingEntityResolver() {
            this(null);
        }

        public SatisfyingEntityResolver(EntityResolver entityResolver) {
            resolver = entityResolver;
        }

        @Override
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            InputSource result = null;
            if (resolver != null) {
                try {
                    result = resolver.resolveEntity(publicId, systemId);
                } catch (Exception ex) {
                    // we're not rethrowing exceptions
                }
            }
            if (result == null) {
                result = new InputSource(new ByteArrayInputStream(Empty.NO_BYTES));
            }
            return result;
        }

    } /* ENDCLASS */

} /* ENDCLASS */
