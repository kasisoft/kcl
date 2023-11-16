package com.kasisoft.libs.common.xml;

import static com.kasisoft.libs.common.internal.Messages.*;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

/**
 * Specialisation of the XmlCatalog which provides preconfigured resources depending on the w3c.jar
 * which should be on the classpath. This catalog doesn't care about missing resources as they're
 * supposed to be made available afterwards.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class PreconfiguredXmlCatalog extends XmlCatalog {

    private static final String[] PRECONFIGURED = new String[] {"-//W3C//DTD XMLSCHEMA 200102//EN",
        "dtds/XMLSchema.dtd", "-//WAPFORUM//DTD XHTML Mobile 1.0//EN", "dtds/xhtml-mobile10-flat.dtd",
        "-//W3C//DTD XHTML Basic 1.0//EN", "dtds/xhtml-basic10-f.dtd", "-//W3C//DTD XHTML 1.1//EN",
        "dtds/xhtml11-flat.dtd", "-//W3C//DTD XHTML 1.0 Transitional//EN", "dtds/xhtml1-transitional.dtd",
        "-//W3C//DTD XHTML 1.0 Strict//EN", "dtds/xhtml1-strict.dtd", "-//W3C//DTD XHTML 1.0 Frameset//EN",
        "dtds/xhtml1-frameset.dtd", "-//WAPFORUM//DTD WML 1.3//EN", "dtds/wml13.dtd", "-//WAPFORUM//DTD WML 1.1//EN",
        "dtds/wml11.dtd", "-//W3C//DTD HTML 4.01//EN", "dtds/strict.dtd", "-//W3C//DTD HTML 4.01 Transitional//EN",
        "dtds/loose.dtd", "-//W3C//DTD HTML 4.01 Frameset//EN", "dtds/frameset.dtd",
        "-//W3C//ENTITIES Latin 1 for XHTML//EN", "dtds/xhtml-lat1.ent", "-//W3C//ENTITIES Symbols for XHTML//EN",
        "dtds/xhtml-symbol.ent", "-//W3C//ENTITIES Special for XHTML//EN", "dtds/xhtml-special.ent", "datatypes",
        "dtds/datatypes.dtd", null, "dtds/HTMLlat1.ent", null, "dtds/HTMLspecial.ent", null, "dtds/HTMLsymbol.ent",
        null, "xsd/xml.xsd", null, "xsd/wsdl-mime.xsd", null, "xsd/wsdl.xsd", null, "xsd/soapenc.xsd", null,
        "xsd/soap.xsd", null, "xsd/http.xsd",};

    /**
     * Initialises this catalog.
     */
    public PreconfiguredXmlCatalog() {
        this(false);
    }

    /**
     * Initialises this catalog.
     *
     * @param lsaware
     *            <code>true</code> <=> Support the LSResourceResolver interface, too.
     */
    public PreconfiguredXmlCatalog(boolean lsaware) {
        super(lsaware);
        for (var i = 0; i < PRECONFIGURED.length; i += 2) {
            registerResource(PRECONFIGURED[i + 0], PRECONFIGURED[i + 1]);
        }
    }

    /**
     * Registers a single resource with this catalog.
     *
     * @param publicid
     *            A public id.
     * @param resource
     *            The resource associated with the id or a system id itself.
     */
    protected void registerResource(@NotNull String publicid, @NotNull String resource) {
        var url = getClass().getClassLoader().getResource(resource);
        if (url != null) {
            if (publicid != null) {
                registerPublicID(publicid, url);
            } else {
                registerSystemID(url);
            }
        } else {
            throw new KclException(error_missing_resource.formatted(resource));
        }
    }

} /* ENDCLASS */
