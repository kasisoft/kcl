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

import java.util.*;

import java.net.*;

/**
 * Basic data structure used to store entity ids together with the urls.
 */
public class XmlCatalog {

  private Map<String,byte[]>    catalogdata;
  
  /**
   * Initialises this catalog.
   */
  public XmlCatalog() {
    catalogdata = new Hashtable<String,byte[]>();
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
    try {
      byte[] data = IoFunctions.loadBytes( url, null );
      catalogdata.put( id, data );
    } catch( FailureException ex ) {
      // we're ignoring this which means that we weren't capable to access the resource
      // but the resolving process still might succeed
    }
  }

  /**
   * Registers a system resource with this catalog.
   * 
   * @param systemid   The resource which has to be registered. Not <code>null</code>.
   */
  public synchronized void registerSystemID( @KNotNull(name="systemid") URL systemid ) {
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
      // but the resolving process still might succeed
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
  
} /* ENDCLASS */
