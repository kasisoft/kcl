package com.kasisoft.libs.common.xml;

import org.xml.sax.*;

import jakarta.validation.constraints.*;

import java.util.*;

import java.net.*;

/**
 * Simple recprd used to configure an xml parser.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public record XmlParserConfiguration(

    // The ErrorHandler to be used. Maybe null.
    ErrorHandler                    handler,

    // A base URL used for the resolving process. Maybe null.
    URL                             baseurl,

    // Resolver for entities. Maybe null.
    EntityResolver                  resolver,

    // <code>true</code> <=> Validates the document if possible.
    boolean                         validate,

    // <code>true</code> <=> Recognize XML namespaces.
    boolean                         xmlnamespaces,

    // <code>true</code> <=> Recognize XML includes (only supported with JRE 1.7+ and may depend on the parser).
    boolean                         xincludes,

    // run a normalization after a document has been loaded
    boolean                         normalize,

    // <code>true</code> <=> Requested schemas that cannot be found will be delivered as empty files (effectively no rules).
    boolean                         satisfyUnknownSchemas,

    Map<DomConfigParameter, Object> parameters

) {

    @NotNull
    public static XmlParserConfigurationBuilder builder() {
        return new XmlParserConfigurationBuilder();
    }

    /**
     * Builder for the XmlParserConfiguration.
     */
    public static class XmlParserConfigurationBuilder {

        private ErrorHandler                    handler;
        private URL                             baseurl;
        private EntityResolver                  resolver;
        private boolean                         validate;
        private boolean                         xmlnamespaces;
        private boolean                         xincludes;
        private boolean                         normalize;
        private boolean                         satisfyUnknownSchemas;
        private Map<DomConfigParameter, Object> parameters;

        XmlParserConfigurationBuilder() {
            parameters = new HashMap<>();
        }

        @NotNull
        public XmlParserConfigurationBuilder satisfyUnknownSchemas() {
            return satisfyUnknownSchemas(true);
        }

        @NotNull
        public XmlParserConfigurationBuilder satisfyUnknownSchemas(boolean satisfyUnknownSchemas) {
            this.satisfyUnknownSchemas = satisfyUnknownSchemas;
            return this;
        }

        @NotNull
        public XmlParserConfigurationBuilder baseurl(URL baseurl) {
            this.baseurl = baseurl;
            return this;
        }

        @NotNull
        public XmlParserConfigurationBuilder handler(ErrorHandler handler) {
            this.handler = handler;
            return this;
        }

        @NotNull
        public XmlParserConfigurationBuilder resolver(EntityResolver resolver) {
            this.resolver = resolver;
            return this;
        }

        @NotNull
        public XmlParserConfigurationBuilder validate() {
            return validate(true);
        }

        @NotNull
        public XmlParserConfigurationBuilder validate(boolean validate) {
            this.validate = validate;
            return this;
        }

        @NotNull
        public XmlParserConfigurationBuilder xincludes() {
            return xincludes(true);
        }

        @NotNull
        public XmlParserConfigurationBuilder xincludes(boolean xincludes) {
            this.xincludes = xincludes;
            return this;
        }

        @NotNull
        public XmlParserConfigurationBuilder xmlnamespaces() {
            return xmlnamespaces(true);
        }

        @NotNull
        public XmlParserConfigurationBuilder xmlnamespaces(boolean xmlnamespaces) {
            this.xmlnamespaces = xmlnamespaces;
            return this;
        }

        @NotNull
        public XmlParserConfigurationBuilder parameter(@NotNull DomConfigParameter parameter, Object value) {
            parameters.put(parameter, value);
            return this;
        }

        @NotNull
        public XmlParserConfigurationBuilder normalize() {
            return normalize(true);
        }

        @NotNull
        public XmlParserConfigurationBuilder normalize(boolean normalize) {
            this.normalize = normalize;
            return this;
        }

        @NotNull
        public XmlParserConfiguration build() {
            return new XmlParserConfiguration(handler, baseurl, resolver, validate, xmlnamespaces, xincludes, normalize, satisfyUnknownSchemas, parameters);
        }

    } /* ENDCLASS */

} /* ENDRECORD */
