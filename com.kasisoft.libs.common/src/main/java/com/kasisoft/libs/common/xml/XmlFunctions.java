package com.kasisoft.libs.common.xml;

import org.xml.sax.*;

import org.w3c.dom.*;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.util.*;

import com.kasisoft.libs.common.base.*;

import com.kasisoft.libs.common.io.*;

import lombok.experimental.*;

import lombok.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import java.util.*;

import java.lang.reflect.*;

import java.net.*;

import java.nio.file.*;

import java.io.*;

/**
 * Collection of xml related functions.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class XmlFunctions {

  static final Map<String, String> XML2NORMAL = new HashMap<>();
  static final Map<String, String> NORMAL2XML = new HashMap<>();

  static final Map<String, String> XML2NORMAL_LE = new HashMap<>();
  static final Map<String, String> NORMAL2XML_LE = new HashMap<>();

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

  /**
   * Prevent this class from being instantiated.
   */
  private XmlFunctions() {
  }

  /**
   * Reads the content of the supplied stream.
   * 
   * @param input    The stream which provides the xml content. Not <code>null</code>.
   * @param config   A configuration for the xml parser. Not <code>null</code>.
   * 
   * @return   The Document node itself. Not <code>null</code>.
   * 
   * @throws FailureException   Loading the xml content failed for some reason.
   */
  public static Document readDocument( @NonNull InputStream input, @NonNull XmlParserConfiguration config ) throws FailureException {
    DocumentBuilder builder = newDocumentBuilder( config );
    Document        result  = null;
    try {
      if( config.getBaseurl() != null ) {
        result = builder.parse( input, config.getBaseurl().toExternalForm() );
      } else {
        result = builder.parse( input );
      }
    } catch( SAXException | IOException ex ) {
      throw FailureCode.XmlFailure.newException( ex );
    }
    return result;
  }

  /**
   * Reads the content of the supplied stream.
   * 
   * @param uri      The resource which provides the xml content. Not <code>null</code>.
   * @param config   A configuration for the xml parser. Not <code>null</code>.
   * 
   * @return   The Document node itself. Not <code>null</code>.
   * 
   * @throws FailureException   Loading the xml content failed for some reason.
   */
  public static Document readDocument( @NonNull URI uri, @NonNull XmlParserConfiguration config ) throws FailureException {
    return IoFunctions.forInputStream( uri, config, XmlFunctions::readDocument );
  }

  /**
   * Reads the content of the supplied stream.
   * 
   * @param path     The path which provides the xml content. Not <code>null</code>.
   * @param config   A configuration for the xml parser. Not <code>null</code>.
   * 
   * @return   The Document node itself. Not <code>null</code>.
   * 
   * @throws FailureException   Loading the xml content failed for some reason.
   */
  public static Document readDocument( @NonNull Path path, @NonNull XmlParserConfiguration config ) throws FailureException {
    return IoFunctions.forInputStream( path, config, XmlFunctions::readDocument );
  }
  
  /**
   * Reads the content of the supplied stream.
   * 
   * @param file     The file which provides the xml content. Not <code>null</code>.
   * @param config   A configuration for the xml parser. Not <code>null</code>.
   * 
   * @return   The Document node itself. Not <code>null</code>.
   * 
   * @throws FailureException   Loading the xml content failed for some reason.
   */
  public static Document readDocument( @NonNull File file, @NonNull XmlParserConfiguration config ) throws FailureException {
    return IoFunctions.forInputStream( file, config, XmlFunctions::readDocument );
  }
  
  /**
   * Creates a {@link DocumentBuilder} instance for the supplied parser configuration.
   * 
   * @param config   A configuration for the xml parser. Not <code>null</code>.
   * 
   * @return   The {@link DocumentBuilder} instance. Not <code>null</code>.
   * 
   * @throws FailureException   Configuring the builder failed for some reason.
   */
  public static DocumentBuilder newDocumentBuilder( @NonNull XmlParserConfiguration config ) {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory . setNamespaceAware ( config.isXmlnamespaces () );
    factory . setValidating     ( config.isValidate      () );
    try {
      Method method = factory.getClass().getMethod( "XIncludeAware", Boolean.TYPE );
      method.invoke( factory, Boolean.valueOf( config.isXincludes() ) );
    } catch( Exception ex ) {
      // no effect here
    }
    try {
      DocumentBuilder result = factory.newDocumentBuilder();
      if( config.getResolver() != null ) {
        result.setEntityResolver( config.getResolver() );
      }
      if( config.getHandler() != null ) {
        result.setErrorHandler( config.getHandler() );
      } else {
        result.setErrorHandler( new XmlErrorHandler() );
      }
      return result;
    } catch( ParserConfigurationException ex ) {
      throw FailureCode.XmlFailure.newException( ex );
    }
  }
  
  /**
   * Decodes a String that contains XML specific entities into a normal String.
   * 
   * @param source   A String that may contain XML entities. Not <code>null</code>.
   * 
   * @return   A decoded String. Not <code>null</code>.
   * 
   * @deprecated This function will be removed with version 2.2 . Use {@link #unescapeXml(CharSequence, boolean)} instead.
   */
  @Deprecated
  public static String decodeString( @NonNull String source ) {
    return StringFunctions.replace( source, XML2NORMAL_LE );
  }

  /**
   * Encodes a String while replacing literals into corresponding XML entities. Thi
   * 
   * @param source   A String that may be modified. Not <code>null</code>.
   * 
   * @return   An encoded String. Not <code>null</code>.
   * 
   * @deprecated This function will be removed with version 2.2 . Use {@link #unescapeXml(CharSequence, boolean)} instead.
   */
  @Deprecated
  public static String encodeString( @NonNull String source ) {
    return StringFunctions.replace( source, NORMAL2XML_LE );
  }
  
  /**
   * Decodes a String in place that contains XML specific entities.
   * 
   * @param source   A String that may contain XML entities. Not <code>null</code>.
   * 
   * @return   A decoded String. Not <code>null</code>.
   */
  public static <T extends CharSequence> T unescapeXml( @NonNull T source ) {
    return unescapeXml( source, false );
  }
  
  /**
   * Decodes a String in place that contains XML specific entities.
   * 
   * @param source        A String that may contain XML entities. Not <code>null</code>.
   * @param lineEndings   <code>true</code> <=> Unescape line endings as well.
   * 
   * @return   A decoded String. Not <code>null</code>.
   */
  public static <T extends CharSequence> T unescapeXml( @NonNull T source, boolean lineEndings ) {
    T result = null;
    if( source instanceof String ) {
      StringBuilder builder = new StringBuilder( source );
      StringFunctions.replace( builder, lineEndings ? XML2NORMAL_LE : XML2NORMAL );
      result = (T) builder.toString();
    } else {
      result = source;
      StringFunctions.replace( result, lineEndings ? XML2NORMAL_LE : XML2NORMAL );
    }
    return result;
  }

  /**
   * Encodes a String in place while replacing literals into corresponding XML entities.
   * 
   * @param source   A String that may be modified. Not <code>null</code>.
   * 
   * @return   An encoded String. Not <code>null</code>.
   */
  public static <T extends CharSequence> T escapeXml( @NonNull T source ) {
    return escapeXml( source, false );
  }
  
  /**
   * Encodes a String in place while replacing literals into corresponding XML entities.
   * 
   * @param source        A String that may be modified. Not <code>null</code>.
   * @param lineEndings   <code>true</code> <=> Escape line endings as well.
   * 
   * @return   An encoded String. Not <code>null</code>.
   */
  public static <T extends CharSequence> T escapeXml( @NonNull T source, boolean lineEndings ) {
    T result = null;
    if( source instanceof String ) {
      StringBuilder builder = new StringBuilder( source );
      StringFunctions.replace( builder, lineEndings ? NORMAL2XML_LE : NORMAL2XML );
      result = (T) builder.toString();
    } else {
      result = source;
      StringFunctions.replace( result, lineEndings ? NORMAL2XML_LE : NORMAL2XML );
    }
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
   * @throws FailureException   Saving the XML datastructure failed.
   */
  public static void writeDocument( @NonNull OutputStream output, @NonNull Node node, Encoding encoding ) throws FailureException {
    TransformerFactory factory = TransformerFactory.newInstance();
    try {
      if( encoding == null ) {
        encoding = Encoding.UTF8;
      }
      String      method      = "xml";
      Document    document    = node.getOwnerDocument();
      if( (document != null) && (document.getDocumentElement() != null) ) {
        if( "html".equalsIgnoreCase( document.getDocumentElement().getTagName() ) ) {
          method = "html";
        }
      }
      Transformer transformer = factory.newTransformer();
      transformer.setOutputProperty( OutputKeys.METHOD               , method                 );
      transformer.setOutputProperty( OutputKeys.INDENT               , "yes"                  );
      transformer.setOutputProperty( OutputKeys.ENCODING             , encoding.getEncoding() );
      // a transformer can generate output in different formats, so it doesn't know
      // about the target format which means that we have to alter the pi by our own
      transformer.setOutputProperty( OutputKeys.OMIT_XML_DECLARATION , "yes" );
      String xmldecl = String.format( "<?xml version=\"1.0\" encoding=\"%s\"?>%s", encoding.getEncoding(), SysProperty.LineSeparator.getValue( System.getProperties() ) );
      output.write( encoding.encode( xmldecl ) );
      output.flush();
      transformer.transform( new DOMSource( node ), new StreamResult( output ) );
    } catch( IOException          ex ) {
      throw FailureCode.IO.newException( ex );
    } catch( TransformerException ex ) {
      throw FailureCode.XmlFailure.newException( ex );
    }
  }
  
  /**
   * Writes the XML content from a DOM tree into a File.
   * 
   * @param destination   The destination File which will be overwritten if already existent. Not <code>null</code>.
   * @param node          The DOM tree which will be saved. Not <code>null</code>.
   * @param encoding      The encoding to use while saving. <code>null</code> or an empty value means that the default 
   *                      encoding is used. 
   *                       
   * @throws FailureException   Saving the XML datastructure failed.
   */
  public static void writeDocument( @NonNull File destination, @NonNull Node node, Encoding encoding ) throws FailureException {
    OutputStream output = null;
    try {
      output = IoFunctions.newOutputStream( destination );
      writeDocument( output, node, encoding );
    } finally {
      MiscFunctions.close( output );
    }
  }
  
  /**
   * Sets up a new transformer from the supplied stylesheet file. This transformer can be used to
   * convert xml documents in various outcomes.
   * 
   * @param xsl   The xslt stylesheet. Not <code>null</code> and must be a file.
   * 
   * @return The transformer if the stylesheet could be loaded properly. Not <code>null</code>.
   * 
   * @throws FailureException if loading the stylesheet failed for some reason.
   */
  public static Transformer newTransformer( @NonNull File xsl ) throws FailureException {
    InputStream         instream  = null;
    try {
      instream = IoFunctions.newInputStream( xsl );
      return newTransformer( instream );
    } finally {
      MiscFunctions.close( instream );
    }
  }

  /**
   * Sets up a new transformer from the supplied stylesheet resource. This transformer can be used to convert xml 
   * documents in various outcomes.
   * 
   * @param resource   The xslt stylesheet resource. Not <code>null</code>.
   * 
   * @return The transformer if the stylesheet could be loaded properly. Not <code>null</code>.
   * 
   * @throws FailureException if loading the stylesheet failed for some reason.
   */
  public static Transformer newTransformer( @NonNull URL resource ) throws FailureException {
    InputStream instream  = null;
    try {
      instream = resource.openStream();
      return newTransformer( instream );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( null, ex, resource );
    } finally {
      MiscFunctions.close( instream );
    }
  }

  /**
   * Sets up a new transformer from the supplied stylesheet InputStream. This transformer can be used to convert xml 
   * documents in various outcomes.
   * 
   * @param xslinstream   The xslt stylesheet provided by an InputStream. Not <code>null</code>.
   * 
   * @return The transformer if the stylesheet could be loaded properly. Not <code>null</code>.
   * 
   * @throws FailureException   If loading the stylesheet failed for some reason.
   */
  public static Transformer newTransformer( @NonNull InputStream xslinstream ) throws FailureException {
    TransformerFactory  factory   = TransformerFactory.newInstance();
    try {
      return factory.newTransformer( new StreamSource( xslinstream ) );
    } catch( TransformerConfigurationException ex ) {
      throw FailureCode.XmlFailure.newException( ex );
    }
  }

  /**
   * Makes sure that a child gets inserted at the first position of a parent.
   * 
   * @param parent   The parent which will be extended. Not <code>null</code>.
   * @param child    The child which has to be inserted to the first position. Not <code>null</code>.
   */
  public static void insertFirst( @NonNull Element parent, @NonNull Node child ) {
    if( parent.getFirstChild() != null ) {
      parent.insertBefore( child, parent.getFirstChild() );
    } else {
      parent.appendChild( child );
    }
  }
  
  /**
   * Simple helper function which allows to easily create an element.
   * 
   * @param doc       The document which will own the returned element. Not <code>null</code>.
   * @param tag       The tag for the element. Neither <code>null</code> nor empty.
   * @param content   The textual content. Maybe <code>null</code>.
   * @param attrs     A list of pairs representing the attributes. Maybe <code>null</code>.
   * 
   * @return   An Element which contains all supplied informations. Not <code>null</code>.
   */
  public static Element createElement( @NonNull Document doc, @NonNull String tag, String content, String ... attrs ) {
    Element result = doc.createElement( tag );
    if( content != null ) {
      result.appendChild( doc.createTextNode( content ) );
    }
    if( attrs != null ) {
      for( int i = 0; i < attrs.length; i += 2 ) {
        result.setAttribute( attrs[ i + 0 ], attrs[ i + 1 ] );
      }
    }
    return result;
  }

  /**
   * Removes the supplied list of nodes.
   * 
   * @param nodes   A list of nodes which have to be removed from the DOM tree. Maybe <code>null</code>.
   */
  public static void removeNodes( NodeList nodes ) {
    if( (nodes != null) && (nodes.getLength() > 0) ) {
      Node parent = nodes.item(0).getParentNode();
      for( int i = nodes.getLength() - 1; i >= 0; i-- ) {
        Node current = nodes.item(i);
        parent.removeChild( current );
      }
    }
  }
  
  /**
   * Collects the child nodes from a parent using a specific name.
   * 
   * @param parent     The parent node which children have to be returned. Not <code>null</code>.
   * @param relevant   A list of interesting element names. If <code>null</code> all elements will be returned.
   * 
   * @return   A list with all matching elements. Not <code>null</code>.
   */
  public static List<Element> getChildElements( @NonNull Node parent, String ... relevant ) {
    NodeList childnodes = parent.getChildNodes();
    if( (childnodes != null) && (childnodes.getLength() > 0) ) {
      Set<String>   tagnames = MiscFunctions.toSet( relevant );
      List<Element> result   = new ArrayList<>( childnodes.getLength() );
      for( int i = 0; i < childnodes.getLength(); i++ ) {
        Node current = childnodes.item(i);
        if( current.getNodeType() == Node.ELEMENT_NODE ) {
          Element element = (Element) current;
          if( tagnames.isEmpty() || contains( tagnames, element.getTagName() ) || contains( tagnames, element.getLocalName() ) ) {
            result.add( element );
          }
        }
      }
      return result;
    } else {
      return Collections.emptyList();
    }
  }

  private static boolean contains( Set<String> set, String candidate ) {
    if( candidate != null ) {
      return set.contains( candidate );
    } else {
      return false;
    }
  }
  
} /* ENDCLASS */