package com.kasisoft.libs.common.xml;

import com.kasisoft.libs.common.constants.Empty;
import com.kasisoft.libs.common.constants.Encoding;

import com.kasisoft.libs.common.io.IoFunctions;

import com.kasisoft.libs.common.KclException;
import com.kasisoft.libs.common.functional.KBiConsumer;
import com.kasisoft.libs.common.functional.KFunction;
import com.kasisoft.libs.common.functional.Predicates;
import com.kasisoft.libs.common.pools.Buckets;
import com.kasisoft.libs.common.text.StringFunctions;
import com.kasisoft.libs.common.utils.MiscFunctions;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import java.net.URI;
import java.net.URL;

import java.nio.file.Path;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import java.lang.reflect.Method;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Collection of xml related functions.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class XmlFunctions {

  static final Map<String, String> XML2NORMAL       = new HashMap<>();
  static final Map<String, String> XML2NORMAL_LE    = new HashMap<>();

  static final Map<String, String> NORMAL2XML       = new HashMap<>();
  static final Map<String, String> NORMAL2XML_LE    = new HashMap<>();
  
  static {
    
    XML2NORMAL.put( "&apos;" , "'"  );
    XML2NORMAL.put( "&gt;"   , ">"  );
    XML2NORMAL.put( "&lt;"   , "<"  );
    XML2NORMAL.put( "&amp;"  , "&"  );
    XML2NORMAL.put( "&quot;" , "\"" );
    
    XML2NORMAL_LE.putAll( XML2NORMAL );
    XML2NORMAL_LE.put( "\\n"    , "\n" );
    XML2NORMAL_LE.put( "\\r"    , "\r" );
    XML2NORMAL_LE.put( "&#10;"  , "\n" );
    XML2NORMAL_LE.put( "&#13;"  , "\r" );
    
    
    NORMAL2XML.put( "'"  , "&apos;" );
    NORMAL2XML.put( ">"  , "&gt;"   );
    NORMAL2XML.put( "<"  , "&lt;"   );
    NORMAL2XML.put( "&"  , "&amp;"  );
    NORMAL2XML.put( "\"" , "&quot;" );
    
    NORMAL2XML_LE.putAll( NORMAL2XML );
    NORMAL2XML_LE.put( "\n" , "\\n"    );
    NORMAL2XML_LE.put( "\r" , "\\r"    );
    NORMAL2XML_LE.put( "\n" , "&#10;"  );
    NORMAL2XML_LE.put( "\r" , "&#13;"  );
  
  }

  public static TransformerFactory newTransformerFactory() {
    return TransformerFactory.newInstance();
  }
  
  public static @NotNull Function<@NotNull Element, @Null String> getAttribute(@NotBlank String attribute) {
    return $ -> StringFunctions.cleanup($.getAttribute(attribute));
  }

  /**
   * Reads the content of the supplied stream.
   * 
   * @param input    The stream which provides the xml content. Not <code>null</code>.
   * @param config   A configuration for the xml parser. Not <code>null</code>.
   * 
   * @return   The Document node itself. Not <code>null</code>.
   */
  public static @NotNull Document readDocument(@NotNull URL input, @NotNull XmlParserConfiguration config) {
    return IoFunctions.forInputStream(input, $ -> readDocument($, config));
  }

  /**
   * Reads the content of the supplied stream.
   * 
   * @param input    The stream which provides the xml content. Not <code>null</code>.
   * @param config   A configuration for the xml parser. Not <code>null</code>.
   * 
   * @return   The Document node itself. Not <code>null</code>.
   */
  public static @NotNull Document readDocument(@NotNull URI input, @NotNull XmlParserConfiguration config) {
    return IoFunctions.forInputStream(input, $ -> readDocument($, config));
  }

  /**
   * Reads the content of the supplied stream.
   * 
   * @param input    The stream which provides the xml content. Not <code>null</code>.
   * @param config   A configuration for the xml parser. Not <code>null</code>.
   * 
   * @return   The Document node itself. Not <code>null</code>.
   */
  public static @NotNull Document readDocument(@NotNull File input, @NotNull XmlParserConfiguration config) {
    return IoFunctions.forInputStream(input, $ -> readDocument($, config));
  }

  /**
   * Reads the content of the supplied stream.
   * 
   * @param input    The stream which provides the xml content. Not <code>null</code>.
   * @param config   A configuration for the xml parser. Not <code>null</code>.
   * 
   * @return   The Document node itself. Not <code>null</code>.
   */
  public static @NotNull Document readDocument(@NotNull Path input, @NotNull XmlParserConfiguration config) {
    return IoFunctions.forInputStream(input, $ -> readDocument($, config));
  }
  
  /**
   * Reads the content of the supplied stream.
   * 
   * @param input    The stream which provides the xml content. Not <code>null</code>.
   * @param config   A configuration for the xml parser. Not <code>null</code>.
   * 
   * @return   The Document node itself. Not <code>null</code>.
   */
  public static @NotNull Document readDocument(@NotNull Reader input, @NotNull XmlParserConfiguration config) {
    return readDocument(config, $ -> {
      var insource = new InputSource(input);
      if (config.getBaseurl() != null) {
        insource.setSystemId(config.getBaseurl().toExternalForm());
      }
      return $.parse(insource);
    });
  }

  /**
   * Reads the content of the supplied stream.
   * 
   * @param input    The stream which provides the xml content. Not <code>null</code>.
   * @param config   A configuration for the xml parser. Not <code>null</code>.
   * 
   * @return   The Document node itself. Not <code>null</code>.
   */
  public static @NotNull Document readDocument(@NotNull InputStream input, @NotNull XmlParserConfiguration config) {
    return readDocument(config, $ -> {
      if (config.getBaseurl() != null) {
        return $.parse(input, config.getBaseurl().toExternalForm());
      } else {
        return $.parse(input);
      }
    });
  }
  
  private static @NotNull Document readDocument(@NotNull XmlParserConfiguration config, @NotNull KFunction<DocumentBuilder, Document> parse) {
    var builder = newDocumentBuilder(config);
    try {
      var result    = parse.apply(builder);
      var domconfig = result.getDomConfig();
      config.getParameters().forEach((k,v) -> k.set(domconfig, v));
      if (config.isNormalize()) {
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
   * @param config   A configuration for the xml parser. Not <code>null</code>.
   * 
   * @return   The {@link DocumentBuilder} instance. Not <code>null</code>.
   * 
   * @throws KclException   Configuring the builder failed for some reason.
   */
  public static @NotNull DocumentBuilder newDocumentBuilder(@NotNull XmlParserConfiguration config) {
    
    var factory = DocumentBuilderFactory.newInstance();
    
    factory.setNamespaceAware(config.isXmlnamespaces());
    factory.setValidating(config.isValidate());
    
    try {
      Method method = factory.getClass().getMethod("XIncludeAware", Boolean.TYPE);
      method.invoke( factory, Boolean.valueOf(config.isXincludes()));
    } catch (Exception ex) {
      // no effect here
    }
    
    try {
      
      var result = factory.newDocumentBuilder();
      
      if (config.isSatisfyUnknownSchemas()) {
        if (config.getResolver() != null) {
          result.setEntityResolver(new SatisfyingEntityResolver(config.getResolver()));
        } else {
          result.setEntityResolver(new SatisfyingEntityResolver());
        }
      } else {
        if (config.getResolver() != null) {
          result.setEntityResolver(config.getResolver());
        }
      }
      
      if (config.getHandler() != null) {
        result.setErrorHandler(config.getHandler());
      } else {
        result.setErrorHandler(new XmlErrorHandler());
      }
      
      return result;
      
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
    
  }
  
  private static String toDocumentMethod(@NotNull Node node) {
    var result      = "xml";
    var document    = node.getOwnerDocument();
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
    result.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION , "yes");
    
    return result;
  }

  /**
   * Writes the XML content from a DOM tree into an OutputStream.
   * 
   * @param output     The OutputStream used to receive the content. Not <code>null</code>.
   * @param node       The DOM tree which will be saved. Not <code>null</code>.
   * @param encoding   The encoding to use while saving. <code>null</code> or an empty value means that the default 
   *                   encoding is used. 
   *                       
   * @throws KclException   Saving the XML datastructure failed.
   */
  public static void writeDocument(@NotNull Path output, @NotNull Node node, @Null Encoding encoding) {
    IoFunctions.forWriterDo(output, encoding, $ -> writeDocument($, node, encoding));
  }

  /**
   * Writes the XML content from a DOM tree into an OutputStream.
   * 
   * @param output     The OutputStream used to receive the content. Not <code>null</code>.
   * @param node       The DOM tree which will be saved. Not <code>null</code>.
   * @param encoding   The encoding to use while saving. <code>null</code> or an empty value means that the default 
   *                   encoding is used. 
   *                       
   * @throws KclException   Saving the XML datastructure failed.
   */
  public static void writeDocument(@NotNull File output, @NotNull Node node, @Null Encoding encoding) {
    IoFunctions.forWriterDo(output, encoding, $ -> writeDocument($, node, encoding));
  }

  /**
   * Writes the XML content from a DOM tree into an OutputStream.
   * 
   * @param output     The OutputStream used to receive the content. Not <code>null</code>.
   * @param node       The DOM tree which will be saved. Not <code>null</code>.
   * @param encoding   The encoding to use while saving. <code>null</code> or an empty value means that the default 
   *                   encoding is used. 
   *                       
   * @throws KclException   Saving the XML datastructure failed.
   */
  public static void writeDocument(@NotNull OutputStream output, @NotNull Node node, @Null Encoding encoding) {
    writeDocument(new StreamResult(output), node, encoding, ($x, $e) -> {
      output.write($e.encode($x));
      output.flush();
    });
  }

  /**
   * Writes the XML content from a DOM tree into an OutputStream.
   * 
   * @param writer     The Writer used to receive the content. Not <code>null</code>.
   * @param node       The DOM tree which will be saved. Not <code>null</code>.
   * @param encoding   The encoding to use while saving. <code>null</code> or an empty value means that the default 
   *                   encoding is used. 
   *                       
   * @throws KclException   Saving the XML datastructure failed.
   */
  public static void writeDocument(@NotNull Writer writer, @NotNull Node node, @Null Encoding encoding) {
    writeDocument(new StreamResult(writer), node, encoding, ($x, $e) -> {
      writer.write($x);
      writer.flush();
    });
  }
  
  private static void writeDocument(StreamResult streamResult, @NotNull Node node, @Null Encoding encoding, KBiConsumer<String, Encoding> handleXmlDecl) {
    
    try {
      
      encoding          = Encoding.getEncoding(encoding);
      var method        = toDocumentMethod(node);
      var transformer   = toTransformer(method, encoding);
      var xmldecl       = String.format( "<?xml version=\"1.0\" encoding=\"%s\"?>\n", encoding.getEncoding());
      
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
   * @param xsl   The xslt stylesheet. Not <code>null</code> and must be a file.
   * 
   * @return The transformer if the stylesheet could be loaded properly.
   * 
   * @throws KclException if loading the stylesheet failed for some reason.
   */
  public static @NotNull Transformer newTransformer(@NotNull Path xsl) {
    return IoFunctions.forInputStream(xsl, $ -> newTransformer($));
  }

  /**
   * Sets up a new transformer from the supplied stylesheet file. This transformer can be used to
   * convert xml documents in various outcomes.
   * 
   * @param xsl   The xslt stylesheet.
   * 
   * @return The transformer if the stylesheet could be loaded properly.
   * 
   * @throws KclException if loading the stylesheet failed for some reason.
   */
  public static @NotNull Transformer newTransformer(@NotNull URI xsl) {
    return IoFunctions.forInputStream(xsl, $ -> newTransformer($));
  }


  /**
   * Sets up a new transformer from the supplied stylesheet file. This transformer can be used to
   * convert xml documents in various outcomes.
   * 
   * @param xsl   The xslt stylesheet.
   * 
   * @return The transformer if the stylesheet could be loaded properly.
   * 
   * @throws KclException if loading the stylesheet failed for some reason.
   */
  public static @NotNull Transformer newTransformer(@NotNull File xsl) {
    return IoFunctions.forInputStream(xsl, $ -> newTransformer($));
  }

  /**
   * Sets up a new transformer from the supplied stylesheet resource. This transformer can be used to convert xml 
   * documents in various outcomes.
   * 
   * @param resource   The xslt stylesheet resource.
   * 
   * @return The transformer if the stylesheet could be loaded properly.
   * 
   * @throws KclException if loading the stylesheet failed for some reason.
   */
  public static @NotNull Transformer newTransformer(@NotNull URL resource) {
    try (var instream = resource.openStream()) {
      return newTransformer(instream);
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  /**
   * Sets up a new transformer from the supplied stylesheet InputStream. This transformer can be used to convert xml 
   * documents in various outcomes.
   * 
   * @param xslinstream   The xslt stylesheet provided by an InputStream.
   * 
   * @return The transformer if the stylesheet could be loaded properly.
   * 
   * @throws KclException   If loading the stylesheet failed for some reason.
   */
  public static @NotNull Transformer newTransformer(@NotNull InputStream xslinstream) {
    try {
      return newTransformerFactory().newTransformer(new StreamSource(xslinstream));
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  /**
   * Makes sure that a child gets inserted at the first position of a parent.
   * 
   * @param parent   The parent which will be extended.
   * @param child    The child which has to be inserted to the first position.
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
   * @param doc       The document which will own the returned element.
   * @param tag       The tag for the element.
   * @param content   The textual content.
   * @param attrs     A list of pairs representing the attributes.
   * 
   * @return   An Element which contains all supplied informations.
   */
  public static @NotNull Element createElement(@NotNull Document doc, @NotBlank String tag, @NotNull String content, @Null String ... attrs) {
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
   * @param nodes   A list of nodes which have to be removed from the DOM tree.
   */
  public static void removeNodes(@Null NodeList nodes) {
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
   * @param parent     The parent node which children have to be returned.
   * @param relevant   A list of interesting element names. If <code>null</code> all elements will be returned.
   * 
   * @return   A list with all matching elements.
   */
  public static @NotNull List<Element> getChildElements(@NotNull Node parent, @Null String ... relevant) {
    var childnodes = parent.getChildNodes();
    if ((childnodes != null) && (childnodes.getLength() > 0)) {
      var               tagnames = MiscFunctions.toSet(relevant);
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
  
  public static <T extends Node> @NotNull List<T> getChildNodes(@Null NodeList nodeList) {
    var result = Collections.<T>emptyList();
    if ((nodeList != null) && (nodeList.getLength() > 0)) {
      result = new ArrayList<>(nodeList.getLength());
      for (var i = 0; i < nodeList.getLength(); i++) {
        result.add( (T) nodeList.item(i) );
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
  
  public static @Null Element findElement(@NotNull Element parent, @NotNull String tag) {
    var children = getChildElements(parent, tag);
    return (!children.isEmpty()) ? children.get(0) : null; 
  }

  public static @NotNull Function<@NotNull Element, @Null String> getElementText(@NotBlank String tag) {
    return $ -> getElementText($, tag);
  }

  public static @Null String getElementText(@NotNull Element parent, @NotNull String tag) {
    var element = findElement(parent, tag);
    return element != null ? StringFunctions.cleanup(element.getTextContent()) : null;
  }

  /**
   * Returns a map with all attributes.
   * 
   * @param node        The node used to retrieve all attributes.
   * @param namespace   <code>true</code> <=> Include the namespace in the attribute name.
   * 
   * @return   A list with all matching elements. Not <code>null</code>.
   */
  public static @NotNull Map<String, Attr> getAttributes(@NotNull Node node, boolean namespace) {
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
      return String.format("{%s}%s", uri, attribute.getLocalName());
    } else {
      return attribute.getName();
    }
  }

  /**
   * Decodes a String in place while replacing XML entities with their textual representation.
   * 
   * @param source   A String that may be modified.
   * 
   * @return   An encoded String.
   */
  public static @NotNull String unescapeXml(@NotNull String source) {
    return unescapeXml(source, false);
  }

  /**
   * Decodes a String in place while replacing XML entities with their textual representation.
   * 
   * @param source        A String that may be modified.
   * @param lineEndings   <code>true</code> <=> Escape line endings as well.
   * 
   * @return   An encoded String.
   */
  public static @NotNull String unescapeXml(@NotNull String source, boolean lineEndings) {
    return Buckets.bucketStringFBuilder().forInstance($ -> {
      $.append(source);
      $.replaceAll(lineEndings ? XML2NORMAL_LE : XML2NORMAL);
      return $.toString();
    });
  }
  
  /**
   * Encodes a String in place while replacing literals into corresponding XML entities.
   * 
   * @param source   A String that may be modified.
   * 
   * @return   An encoded String.
   */
  public static @NotNull String escapeXml(@NotNull String source) {
    return escapeXml(source, false);
  }

  /**
   * Encodes a String in place while replacing literals into corresponding XML entities.
   * 
   * @param source        A String that may be modified.
   * @param lineEndings   <code>true</code> <=> Escape line endings as well.
   * 
   * @return   An encoded String.
   */
  public static @NotNull String escapeXml(@NotNull String source, boolean lineEndings) {
    return Buckets.bucketStringFBuilder().forInstance($ -> {
      $.append(source);
      $.replaceAll(lineEndings ? NORMAL2XML_LE : NORMAL2XML);
      return $.toString();
    });
  }

  @FieldDefaults(level = AccessLevel.PRIVATE)
  private static class SatisfyingEntityResolver implements EntityResolver {

    EntityResolver    resolver;
    
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
        } catch( Exception ex ) {
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