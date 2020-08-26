package com.kasisoft.libs.common.xml;

import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

import java.net.URL;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * Simple POJO used to configure an xml parser. 
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class XmlParserConfiguration {

  // The ErrorHandler to be used. Maybe null.
  ErrorHandler                      handler;
  
  // A base URL used for the resolving process. Maybe null.
  URL                               baseurl;
  
  // Resolver for entities. Maybe null.
  EntityResolver                    resolver;
  
  // <code>true</code> <=> Validates the document if possible.
  boolean                           validate;
  
  // <code>true</code> <=> Recognize XML namespaces.
  boolean                           xmlnamespaces;
  
  // <code>true</code> <=> Recognize XML includes (only supported with JRE 1.7+ and may depend on the parser).
  boolean                           xincludes;
  
  // run a normalization after a document has been loaded
  boolean                           normalize;
  
  // <code>true</code> <=> Requested schemas that cannot be found will be delivered as empty files (effectively no rules).
  boolean                           satisfyUnknownSchemas;
  
  @Setter(AccessLevel.PRIVATE)
  Map<DomConfigParameter, Object>   parameters;
  
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
    
    XmlParserConfiguration   result;
    
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
  
} /* ENDCLASS */
