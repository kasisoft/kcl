package com.kasisoft.libs.common.xml;

import org.xml.sax.*;

import org.w3c.dom.ls.*;

import com.kasisoft.libs.common.base.*;

import com.kasisoft.libs.common.io.*;

import lombok.experimental.*;

import lombok.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;

import java.util.function.*;

import java.util.*;

import java.net.*;

import java.io.*;

/**
 * Basic data structure used to store entity ids together with the urls.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class XmlCatalog implements EntityResolver, LSResourceResolver, URIResolver, Predicate<String> {

  Map<PublicId, byte[]>   catalogdata;
  Map<PublicId, String>   systemIds;
  Set<URL>                failures;
  DOMImplementationLS     domimpl;
  
  /**
   * Initialises this catalog.
   */
  public XmlCatalog() {
    this( false );
  }
  
  /**
   * Initialises this catalog.
   * 
   * @param lsaware   <code>true</code> <=> Support the LSResourceResolver interface, too. If no appropriate DOM 
   *                  implementation can be found this could cause a FailureException.
   *                                
   * @throws FailureException   If <code>lsaware = true</code> and no DOMImplementationLS could be found.
   */
  public XmlCatalog( boolean lsaware ) {
    
    catalogdata = new Hashtable<>();
    systemIds   = new Hashtable<>();
    failures    = new HashSet<>();
    domimpl     = null;
    
    if( lsaware ) {
      try {
        DocumentBuilderFactory  factory    = DocumentBuilderFactory.newInstance();
        DocumentBuilder         docbuilder = factory.newDocumentBuilder();
        if( docbuilder.getDOMImplementation() instanceof DOMImplementationLS ) {
          domimpl = (DOMImplementationLS) docbuilder.getDOMImplementation();
        } else {
          throw FailureCode.XmlFailure.newException();
        }
      } catch( ParserConfigurationException ex ) {
        throw FailureCode.XmlFailure.newException( ex );
      }
    }
    
  }
  
  /**
   * Just a helper which is supposed to be overridden if necessary. This function opens an InputStream to the supplied 
   * URL.
   * 
   * @param url   The URL which shall be accessed. Not <code>null</code>.
   * 
   * @return   The InputStream allowing to access the supplied URL. Not <code>null</code>.
   * 
   * @throws IOException   Accessing the URL failed for some reason.
   */
  protected InputStream openStream( @NonNull URL url ) throws IOException {
    return url.openStream();
  }
  
  /**
   * Registers the supplied resource with a specified id.
   * 
   * @param id    The ID used for the resource. Neither <code>null</code> nor empty.
   * @param url   The resource that will be identified through the id. Not <code>null</code>.
   */
  public synchronized void registerPublicID( @NonNull String id, @NonNull URL url ) {
    if( ! failures.contains( url ) ) {
      PublicId publicid = new PublicId( id );
      try( InputStream instream = openStream( url ) ) {
        byte[] data = IoFunctions.loadBytes( instream, null );
        catalogdata.put( publicid, data );
        systemIds.put( publicid, url.toExternalForm() );
      } catch( IOException | FailureException ex ) {
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
  public synchronized void registerSystemID( @NonNull URL systemid ) {
    if( ! failures.contains( systemid ) ) {
      PublicId publicid = new PublicId( systemid.toExternalForm() );
      try( InputStream instream = openStream( systemid ) ) {
        byte[]  data = IoFunctions.loadBytes( instream, null );
        catalogdata.put( publicid, data );
        // we're also registering the resource name which might also be used to match
        String  path = systemid.getPath();
        int     lidx = path.lastIndexOf( '/' );
        if( lidx != -1 ) {
          catalogdata.put( new PublicId( path.substring( lidx + 1 ) ), data );
        } else {
          catalogdata.put( new PublicId( path ), data );
        }
      } catch( IOException | FailureException ex ) {
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
  public synchronized byte[] loadResource( @NonNull String publicid ) {
    return catalogdata.get( new PublicId( publicid ) );
  }

  /**
   * Delivers the resource content associated with the supplied url.
   * 
   * @param resource   The location of the system resource. Not <code>null</code>.
   * 
   * @return   The content if it could be loaded. Maybe <code>null</code>.
   */
  public synchronized byte[] loadResource( @NonNull URL resource ) {
    PublicId publicid = new PublicId( resource.toExternalForm() );
    if( catalogdata.containsKey( publicid ) ) {
      return catalogdata.get( publicid );
    } else {
      String path = resource.getPath();
      int    lidx = path.lastIndexOf( '/' );
      if( lidx != -1 ) {
        return catalogdata.get( new PublicId( path.substring( lidx + 1 ) ) );
      } else {
        return catalogdata.get( new PublicId( path ) );
      }
    }
  }

  /**
   * Loads the data associated with either the supplied public id or the system id if it could be found.
   * 
   * @param publicid   The public id used for the resolving. Maybe <code>null</code>.
   * @param systemid   The system id used for the resolving. Maybe <code>null</code>.
   * 
   * @return   The loaded data or <code>null</code>.
   */
  private byte[] loadData( String publicid, String systemid ) {
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
    return result;
  }
  
  @Override
  public InputSource resolveEntity( String publicid, String systemid ) throws SAXException, IOException {
    byte[] result = loadData( publicid, systemid );
    if( result != null ) {
      PublicId pid = new PublicId( publicid );
      InputSource inputsource = new InputSource( new ByteArrayInputStream( result ) );
      inputsource.setSystemId( systemIds.get( pid ) );
      inputsource.setPublicId( pid.getId() );
      return inputsource;
    } else {
      return null;
    }
  }
  
  @Override
  public LSInput resolveResource( String type, String namespaceuri, String publicid, String systemid, String baseuri ) {
    if( domimpl != null ) {
      if( (publicid != null) || (systemid != null) ) {
        byte[] result = loadData( publicid, systemid );
        if( result != null ) {
          PublicId pid = new PublicId( publicid );
          LSInput lsinput = domimpl.createLSInput();
          lsinput.setBaseURI( baseuri );
          lsinput.setByteStream( new ByteArrayInputStream( result ) );
          lsinput.setSystemId( systemIds.get( pid ) );
          lsinput.setPublicId( pid.getId() );
          return lsinput;
        } else {
          return null;
        }
      }
    }
    return null;
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

  @Override
  public Source resolve( String href, String base ) throws TransformerException {
    return null;
  }
  
  @Override
  public boolean test( String candidate ) {
    boolean result = false;
    if( candidate != null ) {
      PublicId publicid = new PublicId( candidate );
      result = catalogdata.keySet().contains( publicid ) || // public/system id is known
               systemIds.values().contains( publicid );     // the url is known
    }
    return result;
  }
  
  @FieldDefaults(level = AccessLevel.PRIVATE)
  private static class PublicId implements Comparable<PublicId> {
    
    @Getter
    String   id;
    String   lowerid;
    
    public PublicId( String publicid ) {
      id      = publicid;
      lowerid = publicid.toLowerCase();
    }
    
    @Override
    public boolean equals( Object other ) {
      if( other instanceof PublicId ) {
        return lowerid.equals( ((PublicId) other).lowerid );
      } else {
        return false;
      }
    }
    
    @Override
    public int hashCode() {
      return lowerid.hashCode();
    }
    
    @Override
    public String toString() {
      return id;
    }

    @Override
    public int compareTo( PublicId o ) {
      return lowerid.compareTo( o.lowerid );
    }
    
  } /* ENDCLASS */

} /* ENDCLASS */