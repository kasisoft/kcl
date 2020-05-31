package com.kasisoft.libs.common.old.xml;

import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

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

  // The ErrorHandler to be used. Maybe <code>null</code>.
  ErrorHandler                      handler;
  
  // A base URL used for the resolving process. Maybe <code>null</code>.
  URL                               baseurl;
  
  // Resolver for entities. Maybe <code>null</code>.
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
  
  public static XmlParserConfigurationBuilder builder() {
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
    
    public XmlParserConfigurationBuilder satisfyUnknownSchemas() {
      return satisfyUnknownSchemas( true );
    }
    
    public XmlParserConfigurationBuilder satisfyUnknownSchemas( boolean satisfy ) {
      result.setSatisfyUnknownSchemas( satisfy );
      return this;
    }
    
    public XmlParserConfigurationBuilder baseurl( URL baseurl ) {
      result.setBaseurl( baseurl );
      return this;
    }

    public XmlParserConfigurationBuilder handler( ErrorHandler errorhandler ) {
      result.setHandler( errorhandler );
      return this;
    }

    public XmlParserConfigurationBuilder resolver( EntityResolver entityresolver ) {
      result.setResolver( entityresolver );
      return this;
    }

    public XmlParserConfigurationBuilder validate() {
      return validate( true );
    }
    
    public XmlParserConfigurationBuilder validate( boolean validate ) {
      result.setValidate( validate );
      return this;
    }

    public XmlParserConfigurationBuilder xincludes() {
      return xincludes( true );
    }
    
    public XmlParserConfigurationBuilder xincludes( boolean xincludes ) {
      result.setXincludes( xincludes );
      return this;
    }

    public XmlParserConfigurationBuilder xmlnamespaces() {
      return xmlnamespaces( true );
    }
    
    public XmlParserConfigurationBuilder xmlnamespaces( boolean xmlnamespaces ) {
      result.setXmlnamespaces( xmlnamespaces );
      return this;
    }

    public XmlParserConfigurationBuilder parameter( DomConfigParameter parameter, Object value ) {
      result.getParameters().put( parameter, value );
      return this;
    }

    public XmlParserConfigurationBuilder normalize() {
      return normalize( true );
    }
    
    public XmlParserConfigurationBuilder normalize( boolean normalize ) {
      result.setNormalize( normalize );
      return this;
    }

    public XmlParserConfiguration build() {
      return result;
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
