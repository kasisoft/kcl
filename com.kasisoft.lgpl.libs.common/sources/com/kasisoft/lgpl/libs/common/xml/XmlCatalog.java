/**
 * Name........: XmlCatalog
 * Description.: Basic data structure used to store entity ids together with the urls. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.xml;

import com.kasisoft.lgpl.libs.common.io.*;

import com.kasisoft.lgpl.libs.common.base.*;
import com.kasisoft.lgpl.tools.diagnostic.*;

import org.xml.sax.*;

import java.util.*;

import java.net.*;

import java.io.*;

/**
 * Basic data structure used to store entity ids together with the urls.
 */
@KDiagnostic
public class XmlCatalog implements EntityResolver {

  private Map<String,byte[]>    catalogdata;
  private Set<URL>              failures;
  
  /**
   * Initialises this catalog.
   */
  public XmlCatalog() {
    catalogdata = new Hashtable<String,byte[]>();
    failures    = new HashSet<URL>();
  }
  
  /**
   * Registers the supplied resource with a specified id.
   * 
   * @param id    The ID used for the resource. Neither <code>null</code> nor empty.
   * @param url   The resource that will be identified through the id. Not <code>null</code>.
   */
  public synchronized void registerPublicID( 
    @KNotEmpty(name="id")   String   id, 
    @KNotNull(name="url")   URL      url 
  ) {
    if( ! failures.contains( url ) ) {
      try {
        byte[] data = IoFunctions.loadBytes( url, null );
        catalogdata.put( id, data );
      } catch( FailureException ex ) {
        // we're ignoring this which means that we weren't capable to access the resource
        // but the resolving process still might succeed. to prevent subsequent failures
        // we just register this url as 'invalid'
        failures.add( url );
      }
    }
  }

  /**
   * Registers a system resource with this catalog.
   * 
   * @param systemid   The resource which has to be registered. Not <code>null</code>.
   */
  public synchronized void registerSystemID( @KNotNull(name="systemid") URL systemid ) {
    if( ! failures.contains( systemid ) ) {
      try {
        byte[]  data = IoFunctions.loadBytes( systemid, null );
        catalogdata.put( systemid.toExternalForm(), data );
        // we're also registering the resource name which might also be used to match
        String  path = systemid.getPath();
        int     lidx = path.lastIndexOf( '/' );
        if( lidx != -1 ) {
          catalogdata.put( path.substring( lidx + 1 ), data );
        } else {
          catalogdata.put( path, data );
        }
      } catch( FailureException ex ) {
        // we're ignoring this which means that we weren't capable to access the resource
        // but the resolving process still might succeed. to prevent subsequent failures
        // we just register this url as 'invalid'
        failures.add( systemid );
      }
    }
  }
  
  /**
   * Delivers the resource content associated with the supplied id.
   *  
   * @param publicid   The public id of the resource. Neither <code>null</code> nor empty.
   * 
   * @return   The content if it could be loaded. Maybe <code>null</code>.
   */
  public synchronized byte[] loadResource( @KNotEmpty(name="publicid") String publicid ) {
    return catalogdata.get( publicid );
  }

  /**
   * Delivers the resource content associated with the supplied url.
   * 
   * @param resource   The location of the system resource. Not <code>null</code>.
   * 
   * @return   The content if it could be loaded. Maybe <code>null</code>.
   */
  public synchronized byte[] loadResource( @KNotNull(name="resource") URL resource ) {
    if( catalogdata.containsKey( resource ) ) {
      return catalogdata.get( resource );
    } else {
      String path = resource.getPath();
      int    lidx = path.lastIndexOf( '/' );
      if( lidx != -1 ) {
        return catalogdata.get( path.substring( lidx + 1 ) );
      } else {
        return catalogdata.get( path );
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public InputSource resolveEntity( String publicid, String systemid ) throws SAXException, IOException {
    byte[] result = null;
    URL    url    = toURL( systemid );
    if( publicid != null ) {
      result = loadResource( publicid );
      if( result == null ) {
        if( failures.contains( url ) ) {
          // there's currently no way to access the resource, so finish here
          return null;
        } else {
          // register this resource and try to access it
          registerPublicID( publicid, url );
          result = loadResource( publicid );
        }
      }
    } else if( url != null ) {
      // only the system id is available, so register it and try to access the content
      registerSystemID( url );
      result = loadResource( url );
    }
    if( result != null ) {
      InputSource inputsource = new InputSource( new ByteArrayInputStream( result ) );
      inputsource.setSystemId( systemid );
      inputsource.setPublicId( publicid );
      return inputsource;
    } else {
      return null;
    }
  }
  
  /**
   * Returns the url represented through the supplied system id.
   * 
   * @param systemid   The system id used to represent the resource. Maybe <code>null</code>.
   * 
   * @return   The url or <code>null</code> in case the system id could not be translated.
   */
  private URL toURL( String systemid ) {
    if( systemid != null ) {
      try {
        return new URL( systemid );
      } catch( MalformedURLException ex ) {
        // we don't care since we suspect the id to refer to a file
      }
      File file = new File( systemid );
      try {
        file = file.getCanonicalFile();
        return file.toURI().toURL();
      } catch( IOException ex ) {
        // we just return null for this error case
      }
    }
    return null;
  }
  
} /* ENDCLASS */
