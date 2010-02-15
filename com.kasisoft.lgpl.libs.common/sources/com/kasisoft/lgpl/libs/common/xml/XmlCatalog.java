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

import com.kasisoft.lgpl.tools.diagnostic.*;

import java.util.*;

import java.net.*;

import java.io.*;

/**
 * Basic data structure used to store entity ids together with the urls.
 */
public class XmlCatalog {

  private static final byte[] NO_DATA = new byte[0];
  
  private Map<String,URL>       catalogrefs;
  private Map<String,byte[]>    catalogdata;
  
  /**
   * Initialises this catalog.
   */
  public XmlCatalog() {
    catalogdata = new Hashtable<String,byte[]>();
    catalogrefs = new Hashtable<String,URL>();
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
    catalogrefs.put( id, url );
  }

  /**
   * Registers a system resource with this catalog.
   * 
   * @param systemid   The resource which has to be registered. Not <code>null</code>.
   */
  public synchronized void registerSystemID( @KNotNull(name="systemid") URL systemid ) {
    catalogrefs.put( systemid.toExternalForm(), systemid );
  }
  
  /**
   * Delivers the resource content associated with the supplied id.
   *  
   * @param publicid   The public id of the resource. Neither <code>null</code> nor empty.
   * 
   * @return   The content if it could be loaded. Maybe <code>null</code>.
   */
  public synchronized byte[] loadResource( @KNotEmpty(name="publicid") String publicid ) {
    return getResource( publicid );
  }

  /**
   * Delivers the resource content associated with the supplied url.
   * 
   * @param resource   The location of the system resource. Not <code>null</code>.
   * 
   * @return   The content if it could be loaded. Maybe <code>null</code>.
   */
  public synchronized byte[] loadResource( @KNotNull(name="resource") URL resource ) {
    return getResource( resource.toExternalForm() );
  }
  
  /**
   * Loads the content associated through the supplied resource.
   * 
   * @param key   The key used to access the resource content. Neither <code>null</code> nor empty.
   * 
   * @return   The data of the resource that has been loaded. Maybe <code>null</code>.
   */
  private byte[] getResource( String key ) {
    
    // check whether we've already got the data
    byte[] result = catalogdata.get( key );
    if( result != null ) {
      if( result == NO_DATA ) {
        // this is a placeholder to indicate a failure of a previous load, so
        // we don't need to fail over and over again
        return null;
      } else {
        return result;
      }
    }
    
    URL         resource = catalogrefs.get( key );
    if( resource == null ) {
      // no registration has taken place before
      return null;
    }
    InputStream instream = null;
    try {
      instream  = resource.openStream();
      result    = IoFunctions.loadBytes( instream, null );
      catalogdata.put( key, result );
    } catch( IOException ex ) {
      catalogdata.put( key, NO_DATA );
    } finally {
      IoFunctions.close( instream );
    }    
    return result;
    
  }
  
} /* ENDCLASS */
