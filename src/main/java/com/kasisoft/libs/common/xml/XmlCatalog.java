package com.kasisoft.libs.common.xml;

import com.kasisoft.libs.common.io.IoFunctions;

import com.kasisoft.libs.common.KclException;

import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;

import java.util.function.Predicate;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import java.net.MalformedURLException;
import java.net.URL;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

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
  
  public XmlCatalog() {
    this(false);
  }
  
  /**
   * Initialises this catalog.
   * 
   * @param lsaware   <code>true</code> <=> Support the LSResourceResolver interface, too. If no appropriate DOM 
   *                  implementation can be found this could cause a FailureException.
   */
  public XmlCatalog(boolean lsaware) {
    
    catalogdata = new Hashtable<>();
    systemIds   = new Hashtable<>();
    failures    = new HashSet<>();
    domimpl     = null;
    
    if (lsaware) {
      
      try {
        
        var factory    = DocumentBuilderFactory.newInstance();
        var docbuilder = factory.newDocumentBuilder();
        if (docbuilder.getDOMImplementation() instanceof DOMImplementationLS) {
          domimpl = (DOMImplementationLS) docbuilder.getDOMImplementation();
        } else {
          throw new KclException("DOM Implementation doesn't support LS !");
        }
        
      } catch (Exception ex) {
        throw KclException.wrap(ex);
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
  public synchronized void registerPublicID(@NotNull String id, @NotNull URL url) {
    if (!failures.contains(url)) {
      var publicid = new PublicId(id);
      try {
        var data = IoFunctions.loadAllBytes(url);
        catalogdata.put(publicid, data);
        systemIds.put(publicid, url.toExternalForm());
      } catch (Exception ex) {
        // we're ignoring this which means that we weren't capable to access the resource
        // but the resolving process still might succeed. to prevent subsequent failures
        // we just register this url as 'invalid'
        failures.add( url);
      }
    }
  }
  
  /**
   * Registers a system resource with this catalog.
   * 
   * @param systemid   The resource which has to be registered. Not <code>null</code>.
   */
  public synchronized void registerSystemID(@NotNull URL systemid) {
    if (!failures.contains(systemid)) {
      var publicid = new PublicId(systemid.toExternalForm());
      try {
        var data = IoFunctions.loadAllBytes(systemid);
        catalogdata.put(publicid, data);
        // we're also registering the resource name which might also be used to match
        var path = systemid.getPath();
        var lidx = path.lastIndexOf('/');
        if (lidx != -1) {
          catalogdata.put(new PublicId(path.substring(lidx + 1)), data);
        } else {
          catalogdata.put(new PublicId(path), data);
        }
      } catch (Exception ex) {
        // we're ignoring this which means that we weren't capable to access the resource
        // but the resolving process still might succeed. to prevent subsequent failures
        // we just register this url as 'invalid'
        failures.add(systemid);
      }
    }
  }
  
  /**
   * Delivers the resource content associated with the supplied id.
   *  
   * @param publicid   The public id of the resource.
   * 
   * @return   The content if it could be loaded.
   */
  public synchronized @Null byte[] loadResource(@NotNull String publicid) {
    return catalogdata.get(new PublicId(publicid));
  }

  /**
   * Delivers the resource content associated with the supplied url.
   * 
   * @param resource   The location of the system resource.
   * 
   * @return   The content if it could be loaded.
   */
  public synchronized @Null byte[] loadResource(@NotNull URL resource) {
    var publicid = new PublicId(resource.toExternalForm());
    if (catalogdata.containsKey(publicid)) {
      return catalogdata.get(publicid);
    } else {
      var path = resource.getPath();
      var lidx = path.lastIndexOf('/');
      if (lidx != -1) {
        return catalogdata.get(new PublicId(path.substring(lidx + 1)));
      } else {
        return catalogdata.get(new PublicId(path));
      }
    }
  }
  
  /**
   * Loads the data associated with either the supplied public id or the system id if it could be found.
   * 
   * @param publicid   The public id used for the resolving.
   * @param systemid   The system id used for the resolving.
   * 
   * @return   The loaded data or <code>null</code>.
   */
  private @Null byte[] loadData(@Null String publicid, @Null String systemid) {
    byte[] result = null;
    var    url    = toURL(systemid);
    if (publicid != null) {
      result = loadResource(publicid);
      if (result == null) {
        if (failures.contains(url)) {
          // there's currently no way to access the resource, so finish here
          return null;
        } else {
          // register this resource and try to access it
          registerPublicID(publicid, url);
          result = loadResource(publicid);
        }
      }
    } else if (url != null) {
      // only the system id is available, so register it and try to access the content
      registerSystemID(url);
      result = loadResource(url);
    }
    return result;
  }
  
  @Override
  public @Null InputSource resolveEntity(@Null String publicid, @Null String systemid) throws SAXException, IOException {
    var result = loadData(publicid, systemid);
    if (result != null) {
      var pid         = new PublicId(publicid);
      var inputsource = new InputSource(new ByteArrayInputStream(result));
      inputsource.setSystemId(systemIds.get(pid));
      inputsource.setPublicId(pid.getId());
      return inputsource;
    }
    return null;
  }
  
  @Override
  public @Null LSInput resolveResource(String type, String namespaceuri, String publicid, String systemid, String baseuri) {
    if (domimpl != null) {
      if ((publicid != null) || (systemid != null)) {
        var result = loadData(publicid, systemid);
        if (result != null) {
          var pid     = new PublicId(publicid);
          var lsinput = domimpl.createLSInput();
          lsinput.setBaseURI(baseuri);
          lsinput.setByteStream(new ByteArrayInputStream(result));
          lsinput.setSystemId(systemIds.get(pid));
          lsinput.setPublicId(pid.getId());
          return lsinput;
        }
      }
    }
    return null;
  }
  
  /**
   * Returns the url represented through the supplied system id.
   * 
   * @param systemid   The system id used to represent the resource.
   * 
   * @return   The url or <code>null</code> in case the system id could not be translated.
   */
  private @Null URL toURL(@Null String systemid) {
    if (systemid != null) {
      try {
        return new URL(systemid);
      } catch (MalformedURLException ex) {
        // we don't care since we suspect the id to refer to a file
      }
      var file = new File(systemid);
      try {
        file = file.getCanonicalFile();
        return file.toURI().toURL();
      } catch (IOException ex) {
        // we just return null for this error case
      }
    }
    return null;
  }

  @Override
  public Source resolve(String href, String base) throws TransformerException {
    return null;
  }
  
  @Override
  public boolean test(String candidate) {
    var result = false;
    if (candidate != null) {
      var publicid = new PublicId(candidate);
      // public/system id/url is known
      result = catalogdata.keySet().contains(publicid); 
    }
    return result;
  }
  
  @FieldDefaults(level = AccessLevel.PRIVATE)
  private static class PublicId implements Comparable<PublicId> {
    
    @Getter
    String   id;
    String   lowerid;
    
    public PublicId(String publicid) {
      id      = publicid;
      lowerid = publicid.toLowerCase();
    }
    
    @Override
    public boolean equals(Object other) {
      if (other instanceof PublicId) {
        return lowerid.equals(((PublicId) other).lowerid);
      }
      return false;
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
    public int compareTo(PublicId o) {
      return lowerid.compareTo(o.lowerid);
    }
    
  } /* ENDCLASS */

} /* ENDCLASS */
