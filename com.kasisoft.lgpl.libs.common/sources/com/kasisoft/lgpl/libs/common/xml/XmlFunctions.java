/**
 * Name........: XmlFunctions
 * Description.: Collection of xml related functions.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.xml;

import com.kasisoft.lgpl.libs.common.constants.*;

import com.kasisoft.lgpl.libs.common.util.*;

import com.kasisoft.lgpl.libs.common.io.*;

import com.kasisoft.lgpl.libs.common.base.*;
import com.kasisoft.lgpl.tools.diagnostic.*;

import org.w3c.dom.*;
import org.xml.sax.*;

import javax.xml.transform.stream.*;

import javax.xml.transform.dom.*;

import javax.xml.transform.*;

import javax.xml.parsers.*;

import java.util.*;

import java.net.*;

import java.io.*;

/**
 * Collection of xml related functions.
 */
@KDiagnostic
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
  public static final Document readDocument( 
    @KFile(name="file")   File      file, 
                          boolean   validate, 
                          boolean   xmlnamespaces 
  ) throws FailureException {
    return readDocument( file, null, null, null, validate, xmlnamespaces, false );
  }
  
  /**
   * Reads the content of the supplied File.
   * 
   * @param file            The File which provides the xml content. Not <code>null</code>.
   * @param handler         The ErrorHandler to be used.
   * @param baseurl         A base URL used for the resolving process.
   * @param resolver        Resolver for entities.
   * @param validate        <code>true</code> <=> Validates the document if possible.
   * @param xmlnamespaces   <code>true</code> <=> Recognize XML namespaces.
   * @param xincludes       <code>true</code> <=> Recognize XML includes.
   * 
   * @return   The Document node itself. Not <code>null</code>.
   * 
   * @throws FailureException   Loading the xml content failed for some reason.
   */
  public static final Document readDocument( 
    @KFile(name="file")   File             file, 
                          ErrorHandler     handler,
                          URL              baseurl,
                          EntityResolver   resolver,
                          boolean          validate, 
                          boolean          xmlnamespaces, 
                          boolean          xincludes 
  ) throws FailureException {
    InputStream input = null;
    try {
      input = new FileInputStream( file );
      return readDocument( input, handler, baseurl, resolver, validate, xmlnamespaces, xincludes );
    } catch( FileNotFoundException ex ) {
      throw new FailureException( FailureCode.XmlFailure, ex );
    } finally {
      IoFunctions.close( input );
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
   * @param xincludes       <code>true</code> <=> Recognize XML includes.
   * 
   * @return   The Document node itself. Not <code>null</code>.
   * 
   * @throws FailureException   Loading the xml content failed for some reason.
   */
  public static final Document readDocument( 
    @KNotNull(name="input")   InputStream      input, 
                              ErrorHandler     handler,
                              URL              baseurl,
                              EntityResolver   resolver,
                              boolean          validate, 
                              boolean          xmlnamespaces, 
                              boolean          xincludes 
  ) throws FailureException {
    try {
      DocumentBuilderFactory factory    = DocumentBuilderFactory.newInstance();
      factory . setNamespaceAware ( xmlnamespaces );
      factory . setValidating     ( validate      );
      factory . setXIncludeAware  ( xincludes     );
      DocumentBuilder        docbuilder = factory.newDocumentBuilder();
      SimpleErrorHandler     newhandler = null;
      if( resolver != null ) {
        docbuilder.setEntityResolver( resolver );
      }
      if( handler != null ) {
        docbuilder.setErrorHandler( handler );
      } else {
        newhandler  = new SimpleErrorHandler();
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
      XmlFault fault = new XmlFault( false, ex );
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
  public static final String decodeString( 
    @KNotNull(name="source")   String   source 
  ) {
    return StringFunctions.replace( source, XML2NORMAL );
  }

  /**
   * Encodes a String while replacing literals into corresponding XML entities.
   * 
   * @param source   A String that may be modified. Not <code>null</code>.
   * 
   * @return   An encoded String. Not <code>null</code>.
   */
  public static final String encodeString( 
    @KNotNull(name="source")   String   source 
  ) {
    return StringFunctions.replace( source, NORMAL2XML );
  }

  /**
   * Writes the XML content from a DOM tree into an OutputStream.
   * 
   * @param output     The OutputStream used to receive the content. Not <code>null</code>.
   * @param node       The DOM tree which will be saved. Not <code>null</code>.
   * @param encoding   The encoding to use while saving. <code>null</code> or an empty value
   *                   means that the default encoding is used. 
   *                       
   * @throws FailureException   Saving the XML datastructure failed.
   */
  public static final void writeDocument( 
    @KNotNull(name="output")   OutputStream   output, 
    @KNotNull(name="node")     Node           node, 
                               Encoding       encoding 
  ) throws FailureException {
    TransformerFactory factory = TransformerFactory.newInstance();
    try {
      if( encoding == null ) {
        encoding = Encoding.getDefault();
      }
      Transformer transformer = factory.newTransformer();
      transformer.setOutputProperty( OutputKeys.INDENT               , "yes" );
      transformer.setOutputProperty( OutputKeys.ENCODING             , encoding.getEncoding() );
      // a transformer can generate output in different formats, so it doesn't know
      // about the target format which means that we have to alter the pi by our own
      transformer.setOutputProperty( OutputKeys.OMIT_XML_DECLARATION , "yes" );
      String xmldecl = String.format( "<?xml version=\"1.0\" encoding=\"%s\"?>%s", encoding.getEncoding(), SystemProperty.LineSeparator );
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
   * @param encoding      The encoding to use while saving. <code>null</code> or an empty value
   *                      means that the default encoding is used. 
   *                       
   * @throws FailureException   Saving the XML datastructure failed.
   */
  public static final void writeDocument( 
    @KNotNull(name="destination")   File       destination, 
    @KNotNull(name="node")          Node       node, 
                                    Encoding   encoding 
  ) throws FailureException {
    OutputStream output = null;
    try {
      output = new FileOutputStream( destination );
      writeDocument( output, node, encoding );
    } catch( IOException ex ) {
      throw new FailureException( FailureCode.XmlFailure, ex );
    } finally {
      IoFunctions.close( output );
    }
  }
  
  /**
   * Sets up a new transformer from the supplied stylesheet file. This transformer can be useed to
   * convert xml documents in various outcomes.
   * 
   * @param xsl   The xslt stylesheet. Not <code>null</code> and must be a file.
   * 
   * @return The transformer if the stylesheet could be loaded properly. Not <code>null</code>.
   * 
   * @throws FailureException if loading the stylesheet failed for some reason.
   */
  public static final Transformer newTransformer( @KFile(name="xsl") File xsl ) throws FailureException {
    TransformerFactory  factory   = TransformerFactory.newInstance();
    InputStream         instream  = null;
    try {
      instream = new FileInputStream( xsl );
      return factory.newTransformer( new StreamSource( instream ) );
    } catch( IOException ex ) {
      throw new FailureException( FailureCode.IO, ex );
    } catch( TransformerConfigurationException ex ) {
      throw new FailureException( FailureCode.XmlFailure, ex );
    } finally {
      IoFunctions.close( instream );
    }
  }
  
  public static final void main( String[] args ) throws Exception {
    XmlFunctions.readDocument( new File( "simple.xml" ), true, true );
  }

} /* ENDCLASS */