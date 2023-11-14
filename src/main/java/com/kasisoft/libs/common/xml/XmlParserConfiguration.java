package com.kasisoft.libs.common.xml;

import org.xml.sax.*;

import jakarta.validation.constraints.*;

import java.util.*;

import java.net.*;

/**
 * Simple POJO used to configure an xml parser.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class XmlParserConfiguration {

    // The ErrorHandler to be used. Maybe null.
    private ErrorHandler                    handler;

    // A base URL used for the resolving process. Maybe null.
    private URL                             baseurl;

    // Resolver for entities. Maybe null.
    private EntityResolver                  resolver;

    // <code>true</code> <=> Validates the document if possible.
    private boolean                         validate;

    // <code>true</code> <=> Recognize XML namespaces.
    private boolean                         xmlnamespaces;

    // <code>true</code> <=> Recognize XML includes (only supported with JRE 1.7+ and may depend on the parser).
    private boolean                         xincludes;

    // run a normalization after a document has been loaded
    private boolean                         normalize;

    // <code>true</code> <=> Requested schemas that cannot be found will be delivered as empty files (effectively no rules).
    private boolean                         satisfyUnknownSchemas;

    private Map<DomConfigParameter, Object> parameters;

    XmlParserConfiguration() {
        parameters = new HashMap<>();
    }

    public static @NotNull XmlParserConfigurationBuilder builder() {
        return new XmlParserConfigurationBuilder();
    }

    /**
     * Builder for the XmlParserConfiguration.
     */
    public static class XmlParserConfigurationBuilder {

        XmlParserConfiguration result;

        XmlParserConfigurationBuilder() {
            result = new XmlParserConfiguration();
        }

        public @NotNull XmlParserConfigurationBuilder satisfyUnknownSchemas() {
            return satisfyUnknownSchemas(true);
        }

        public @NotNull XmlParserConfigurationBuilder satisfyUnknownSchemas(boolean satisfy) {
            result.setSatisfyUnknownSchemas(satisfy);
            return this;
        }

        public @NotNull XmlParserConfigurationBuilder baseurl(URL baseurl) {
            result.setBaseurl(baseurl);
            return this;
        }

        public @NotNull XmlParserConfigurationBuilder handler(ErrorHandler errorhandler) {
            result.setHandler(errorhandler);
            return this;
        }

        public @NotNull XmlParserConfigurationBuilder resolver(EntityResolver entityresolver) {
            result.setResolver(entityresolver);
            return this;
        }

        public @NotNull XmlParserConfigurationBuilder validate() {
            return validate(true);
        }

        public @NotNull XmlParserConfigurationBuilder validate(boolean validate) {
            result.setValidate(validate);
            return this;
        }

        public @NotNull XmlParserConfigurationBuilder xincludes() {
            return xincludes(true);
        }

        public @NotNull XmlParserConfigurationBuilder xincludes(boolean xincludes) {
            result.setXincludes(xincludes);
            return this;
        }

        public @NotNull XmlParserConfigurationBuilder xmlnamespaces() {
            return xmlnamespaces(true);
        }

        public @NotNull XmlParserConfigurationBuilder xmlnamespaces(boolean xmlnamespaces) {
            result.setXmlnamespaces(xmlnamespaces);
            return this;
        }

        public @NotNull XmlParserConfigurationBuilder parameter(@NotNull DomConfigParameter parameter, Object value) {
            result.getParameters().put(parameter, value);
            return this;
        }

        public @NotNull XmlParserConfigurationBuilder normalize() {
            return normalize(true);
        }

        public @NotNull XmlParserConfigurationBuilder normalize(boolean normalize) {
            result.setNormalize(normalize);
            return this;
        }

        public @NotNull XmlParserConfiguration build() {
            return result;
        }

    } /* ENDCLASS */

    public ErrorHandler getHandler() {
        return handler;
    }

    public void setHandler(ErrorHandler handler) {
        this.handler = handler;
    }

    public URL getBaseurl() {
        return baseurl;
    }

    public void setBaseurl(URL baseurl) {
        this.baseurl = baseurl;
    }

    public EntityResolver getResolver() {
        return resolver;
    }

    public void setResolver(EntityResolver resolver) {
        this.resolver = resolver;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public boolean isXmlnamespaces() {
        return xmlnamespaces;
    }

    public void setXmlnamespaces(boolean xmlnamespaces) {
        this.xmlnamespaces = xmlnamespaces;
    }

    public boolean isXincludes() {
        return xincludes;
    }

    public void setXincludes(boolean xincludes) {
        this.xincludes = xincludes;
    }

    public boolean isNormalize() {
        return normalize;
    }

    public void setNormalize(boolean normalize) {
        this.normalize = normalize;
    }

    public boolean isSatisfyUnknownSchemas() {
        return satisfyUnknownSchemas;
    }

    public void setSatisfyUnknownSchemas(boolean satisfyUnknownSchemas) {
        this.satisfyUnknownSchemas = satisfyUnknownSchemas;
    }

    public Map<DomConfigParameter, Object> getParameters() {
        return parameters;
    }

    @Override
    public int hashCode() {
        final int prime  = 31;
        int       result = 1;
        result = prime * result + ((baseurl == null) ? 0 : baseurl.hashCode());
        result = prime * result + ((handler == null) ? 0 : handler.hashCode());
        result = prime * result + (normalize ? 1231 : 1237);
        result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
        result = prime * result + ((resolver == null) ? 0 : resolver.hashCode());
        result = prime * result + (satisfyUnknownSchemas ? 1231 : 1237);
        result = prime * result + (validate ? 1231 : 1237);
        result = prime * result + (xincludes ? 1231 : 1237);
        result = prime * result + (xmlnamespaces ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        XmlParserConfiguration other = (XmlParserConfiguration) obj;
        if (baseurl == null) {
            if (other.baseurl != null)
                return false;
        } else if (!baseurl.equals(other.baseurl))
            return false;
        if (handler == null) {
            if (other.handler != null)
                return false;
        } else if (!handler.equals(other.handler))
            return false;
        if (normalize != other.normalize)
            return false;
        if (parameters == null) {
            if (other.parameters != null)
                return false;
        } else if (!parameters.equals(other.parameters))
            return false;
        if (resolver == null) {
            if (other.resolver != null)
                return false;
        } else if (!resolver.equals(other.resolver))
            return false;
        if (satisfyUnknownSchemas != other.satisfyUnknownSchemas)
            return false;
        if (validate != other.validate)
            return false;
        if (xincludes != other.xincludes)
            return false;
        if (xmlnamespaces != other.xmlnamespaces)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "XmlParserConfiguration [handler=" + handler + ", baseurl=" + baseurl + ", resolver=" + resolver
            + ", validate=" + validate + ", xmlnamespaces=" + xmlnamespaces + ", xincludes=" + xincludes
            + ", normalize=" + normalize + ", satisfyUnknownSchemas=" + satisfyUnknownSchemas + ", parameters="
            + parameters + "]";
    }

} /* ENDCLASS */
