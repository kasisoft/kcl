package com.kasisoft.libs.common.xml;

import org.xml.sax.*;

import lombok.experimental.*;

import lombok.*;

import java.net.*;

/**
 * Simple POJO used to configure an xml parser. 
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class XmlParserConfiguration {

  // The ErrorHandler to be used. Maybe <code>null</code>.
  ErrorHandler            handler;
  
  // A base URL used for the resolving process. Maybe <code>null</code>.
  URL                     baseurl;
  
  // Resolver for entities. Maybe <code>null</code>.
  EntityResolver          resolver;
  
  // <code>true</code> <=> Validates the document if possible.
  boolean                 validate;
  
  // <code>true</code> <=> Recognize XML namespaces.
  boolean                 xmlnamespaces;
  
  // <code>true</code> <=> Recognize XML includes (only supported with JRE 1.7+ and may depend on the parser).
  boolean                 xincludes ;

} /* ENDCLASS */
