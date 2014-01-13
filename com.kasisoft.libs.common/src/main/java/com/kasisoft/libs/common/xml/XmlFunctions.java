/**
 * Name........: XmlFunctions
 * Description.: Collection of xml related functions.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.io.*;
import com.kasisoft.libs.common.util.*;

import org.w3c.dom.*;
import org.xml.sax.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import java.util.*;

import java.net.*;

import java.io.*;

import java.lang.reflect.*;

import lombok.*;

/**
 * Collection of xml related functions.
 */
public final class XmlFunctions {

  private static final Map<String, String> XML2NORMAL = new Hashtable<String, String>();
  private static final Map<String, String> NORMAL2XML = new Hashtable<String, String>();
  
  static {
    
    XML2NORMAL.put( "\\n"    , "\n" );
    XML2NORMAL.put( "\\r"    , "\r" );
    XML2NORMAL.put( "&#10;"  , "\n" );
    XML2NORMAL.put( "&#13;"  , "\r" );
    XML2NORMAL.put( "&apos;" , "'"  );
    XML2NORMAL.put( "&gt;"   , ">"  );
    XML2NORMAL.put( "&lt;"   , "<"  );
    XML2NORMAL.put( "&amp;"  , "&"  );
    XML2NORMAL.put( "&quot;" , "\"" );
    
    NORMAL2XML.put( "\n" , "\\n"    );
    NORMAL2XML.put( "\r" , "\\r"    );
    NORMAL2XML.put( "\n" , "&#10;"  );
    NORMAL2XML.put( "\r" , "&#13;"  );
    NORMAL2XML.put( "'"  , "&apos;" );
    NORMAL2XML.put( ">"  , "&gt;"   );
    NORMAL2XML.put( "<"  , "&lt;"   );
    NORMAL2XML.put( "&"  , "&amp;"  );
    NORMAL2XML.put( "\"" , "&quot;" );
    
  }

  /**
   * Prevent this class from being instantiated.
   */
  private XmlFunctions() {
  }

  /**
   * Reads the content of the supplied File.
   * 
   * @param file            The File which provides the xml content. Not <code>null</code>.
   * @param validate        <code>true</code> <=> Validates the document if possible.
   * @param xmlnamespaces   <code>true</code> <=> Recognize XML namespaces.
   * 
   * @return   The Document node itself. Not <code>null</code>.
   * 
   * @throws FailureException   Loading the xml content failed for some reason.
   */
  public static Document readDocument( @NonNull File file, boolean validate, boolean xmlnamespaces ) throws FailureException {
    return readDocument( file, null, null, null, validate, xmlnamespaces, false );
  }

  /**
   * Reads the content of the supplied InputStream.
   * 
   * @param input           The stream which provides the xml content. Not <code>null</code>.
   * @param validate        <code>true</code> <=> Validates the document if possible.
   * @param xmlnamespaces   <code>true</code> <=> Recognize XML namespaces.
   * 
   * @return   The Document node itself. Not <code>null</code>.
   * 
   * @throws FailureException   Loading the xml content failed for some reason.
   */
  public static Document readDocument( @NonNull InputStream input, boolean validate, boolean xmlnamespaces ) throws FailureException {
    return readDocument( input, null, null, null, validate, xmlnamespaces, false );
  }

  /**
   * Reads the content of the supplied File.
   * 
   * @param file            The File which provides the xml content. Not <code>null</code>.
   * @param handler         The ErrorHandler to be used. Mabye <code>null</code>. 
   * @param baseurl         A base URL used for the resolving process. Maybe <code>null</code>.
   * @param resolver        Resolver for entities. Maybe <code>null</code>.
   * @param validate        <code>true</code> <=> Validates the document if possible.
   * @param xmlnamespaces   <code>true</code> <=> Recognize XML namespaces.
   * @param xincludes       <code>true</code> <=> Recognize XML includes.
   * 
   * @return   The Document node itself. Not <code>null</code>.
   * 
   * @throws FailureException   Loading the xml content failed for some reason.
   */
  public static Document readDocument( 
    @NonNull File    file, 
    ErrorHandler     handler,
    URL              baseurl,
    EntityResolver   resolver,
    boolean          validate, 
    boolean          xmlnamespaces, 
    boolean          xincludes 
  ) throws FailureException {
    InputStream input = null;
    try {
      input = IoFunctions.newInputStream( file );
      return readDocument( input, handler, baseurl, resolver, validate, xmlnamespaces, xincludes );
    } finally {
      MiscFunctions.close( input );
    }
  }

  /**
   * Reads the content of the supplied stream.
   * 
   * @param input           The stream which provides the xml content. Not <code>null</code>.
   * @param handler         The ErrorHandler to be used. Maybe <code>null</code>.
   * @param baseurl         A base URL used for the resolving process. Maybe <code>null</code>.
   * @param resolver        Resolver for entities. Maybe <code>null</code>.
   * @param validate        <code>true</code> <=> Validates the document if possible.
   * @param xmlnamespaces   <code>true</code> <=> Recognize XML namespaces.
   * @param xincludes       <code>true</code> <=> Recognize XML includes (only supported with JRE 1.7+).
   * 
   * @return   The Document node itself. Not <code>null</code>.
   * 
   * @throws FailureException   Loading the xml content failed for some reason.
   */
  public static Document readDocument( 
    @NonNull InputStream    input, 
    ErrorHandler            handler,
    URL                     baseurl,
    EntityResolver          resolver,
    boolean                 validate, 
    boolean                 xmlnamespaces, 
    boolean                 xincludes 
  ) throws FailureException {
    try {
      DocumentBuilderFactory factory    = DocumentBuilderFactory.newInstance();
      factory . setNamespaceAware ( xmlnamespaces );
      factory . setValidating     ( validate      );
      try {
        Method method = factory.getClass().getMethod( "XIncludeAware", Boolean.TYPE );
        method.invoke( factory, Boolean.valueOf( xincludes ) );
      } catch( Exception ex ) {
        // no effect here
      }
      DocumentBuilder        docbuilder = factory.newDocumentBuilder();
      XmlErrorHandler     newhandler = null;
      if( resolver != null ) {
        docbuilder.setEntityResolver( resolver );
      }
      if( handler != null ) {
        docbuilder.setErrorHandler( handler );
      } else {
        newhandler  = new XmlErrorHandler();
        docbuilder.setErrorHandler( newhandler );
      }
      Document document = null;
      if( baseurl != null ) {
        document = docbuilder.parse( input, baseurl.toExternalForm() );
      } else {
        document = docbuilder.parse( input );
      }
      if( (newhandler != null) && newhandler.hasErrors() ) {
        throw new FailureException( FailureCode.XmlFailure, newhandler.getFaultMessage() ); 
      }
      return document;
    } catch( ParserConfigurationException ex ) {
      throw new FailureException( FailureCode.XmlFailure, ex );
    } catch( SAXParseException             ex ) {
      XmlFault fault = new XmlFault( XmlFault.FaultType.fatal, ex );
      throw new FailureException( FailureCode.XmlFailure, fault.getFaultMessage() );
    } catch( SAXException                  ex ) {
      throw new FailureException( FailureCode.XmlFailure, ex );
    } catch( IOException                   ex ) {
      throw new FailureException( FailureCode.IO, ex );
    }
  }
  
  /**
   * Decodes a String that contains XML specific entities into a normal String.
   * 
   * @param source   A String that may contain XML entities. Not <code>null</code>.
   * 
   * @return   A decoded String. Not <code>null</code>.
   */
  public static String decodeString( @NonNull String source ) {
    return StringFunctions.replace( source, XML2NORMAL );
  }

  /**
   * Encodes a String while replacing literals into corresponding XML entities.
   * 
   * @param source   A String that may be modified. Not <code>null</code>.
   * 
   * @return   An encoded String. Not <code>null</code>.
   */
  public static String encodeString( @NonNull String source ) {
    return StringFunctions.replace( source, NORMAL2XML );
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
        encoding = Encoding.getDefault();
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
    } catch( IOException                       ex ) {
      throw new FailureException( FailureCode.IO, ex );
    } catch( TransformerConfigurationException ex ) {
      throw new FailureException( FailureCode.XmlFailure, ex );
    } catch( TransformerException ex ) {
      throw new FailureException( FailureCode.XmlFailure, ex );
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
      throw new FailureException( FailureCode.IO, ex );
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
   * @throws FailureException if loading the stylesheet failed for some reason.
   */
  public static Transformer newTransformer( @NonNull InputStream xslinstream ) throws FailureException {
    TransformerFactory  factory   = TransformerFactory.newInstance();
    try {
      return factory.newTransformer( new StreamSource( xslinstream ) );
    } catch( TransformerConfigurationException ex ) {
      throw new FailureException( FailureCode.XmlFailure, ex );
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
      List<Element> result   = new ArrayList<Element>( childnodes.getLength() );
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