package com.kasisoft.libs.common.xml;

import static com.kasisoft.libs.common.internal.Messages.*;

import org.xml.sax.*;

import org.w3c.dom.ls.*;

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;

import java.util.function.*;

import java.util.*;

import java.net.*;

import java.io.*;

/**
 * Basic data structure used to store entity ids together with the urls.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class XmlCatalog implements EntityResolver, LSResourceResolver, URIResolver, Predicate<String> {

    private Map<PublicId, byte[]> catalogdata;
    private Map<PublicId, String> systemIds;
    private Set<URL>              failures;
    private DOMImplementationLS   domimpl;

    public XmlCatalog() {
        this(false);
    }

    /**
     * Initialises this catalog.
     *
     * @param lsaware
     *            <code>true</code> Support the LSResourceResolver interface, too.
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
                if (docbuilder.getDOMImplementation() instanceof DOMImplementationLS value) {
                    domimpl = value;
                } else {
                    throw new KclException(error_dom_impl_without_ls);
                }

            } catch (Exception ex) {
                throw KclException.wrap(ex);
            }

        }

    }

    /**
     * Registers the supplied resource with a specified id.
     *
     * @param id
     *            The ID used for the resource.
     * @param url
     *            The resource that will be identified through the id.
     */
    public synchronized void registerPublicID(@NotNull String id, @NotNull URL url) {
        if (!failures.contains(url)) {
            var publicid = new PublicId(id);
            try {
                var data = IoSupportFunctions.loadAllBytes(url);
                catalogdata.put(publicid, data);
                systemIds.put(publicid, url.toExternalForm());
            } catch (Exception ex) {
                // we're ignoring this which means that we weren't capable to access the resource
                // but the resolving process still might succeed. to prevent subsequent failures
                // we just register this url as 'invalid'
                failures.add(url);
            }
        }
    }

    /**
     * Registers a system resource with this catalog.
     *
     * @param systemid
     *            The resource which has to be registered.
     */
    public synchronized void registerSystemID(@NotNull URL systemid) {
        if (!failures.contains(systemid)) {
            var publicid = new PublicId(systemid.toExternalForm());
            try {
                var data = IoSupportFunctions.loadAllBytes(systemid);
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
     * @param publicid
     *            The public id of the resource.
     * @return The content if it could be loaded.
     */
    public synchronized byte[] loadResource(@NotNull String publicid) {
        return catalogdata.get(new PublicId(publicid));
    }

    /**
     * Delivers the resource content associated with the supplied url.
     *
     * @param resource
     *            The location of the system resource.
     * @return The content if it could be loaded.
     */
    public synchronized byte[] loadResource(@NotNull URL resource) {
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
     * Loads the data associated with either the supplied public id or the system id if it could be
     * found.
     *
     * @param publicid
     *            The public id used for the resolving.
     * @param systemid
     *            The system id used for the resolving.
     * @return The loaded data or null.
     */
    private byte[] loadData(String publicid, String systemid) {
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
    public InputSource resolveEntity(String publicid, String systemid) throws SAXException, IOException {
        var result = loadData(publicid, systemid);
        if (result != null) {
            var pid         = new PublicId(publicid);
            var inputsource = new InputSource(new ByteArrayInputStream(result));
            inputsource.setSystemId(systemIds.get(pid));
            inputsource.setPublicId(pid.publicid());
            return inputsource;
        }
        return null;
    }

    @Override
    public LSInput resolveResource(String type, String namespaceuri, String publicid, String systemid, String baseuri) {
        if (domimpl != null) {
            if ((publicid != null) || (systemid != null)) {
                var result = loadData(publicid, systemid);
                if (result != null) {
                    var pid     = new PublicId(publicid);
                    var lsinput = domimpl.createLSInput();
                    lsinput.setBaseURI(baseuri);
                    lsinput.setByteStream(new ByteArrayInputStream(result));
                    lsinput.setSystemId(systemIds.get(pid));
                    lsinput.setPublicId(pid.publicid());
                    return lsinput;
                }
            }
        }
        return null;
    }

    /**
     * Returns the url represented through the supplied system id.
     *
     * @param systemid
     *            The system id used to represent the resource.
     * @return The url or null in case the system id could not be translated.
     */
    private URL toURL(String systemid) {
        if (systemid != null) {
            try {
                return URI.create(systemid).toURL();
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

    private static record PublicId(String publicid) implements Comparable<PublicId> {

        @Override
        public int compareTo(PublicId o) {
            return publicid().toLowerCase().compareTo(o.publicid().toLowerCase());
        }

    } /* ENDCLASS */

} /* ENDCLASS */
