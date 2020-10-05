package com.kasisoft.libs.common.xml;

import org.w3c.dom.*;

import javax.validation.constraints.*;

import lombok.experimental.FieldDefaults;

import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Collection of parameters for the {@link DOMConfiguration} .
 * 
 * {@link "http://xerces.apache.org/xerces2-j/javadocs/api/org/w3c/dom/ls/LSSerializer.html"} [07-Jul-2016:KASI]
 * {@link "http://download.oracle.com/javase/1.5.0/docs/api/org/w3c/dom/DOMConfiguration.html"} [07-Jul-2016:KASI]
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum DomConfigParameter {

  CanonicalForm                         ("canonical-form"                           , false),
  CDATASections                         ("cdata-sections"                           , false),
  CheckCharacterNormalisation           ("check-character-normalization"            , false),
  Comments                              ("comments"                                 , false),
  DatatypeNormalisation                 ("datatype-normalization"                   , false),
  DiscardDefaultContent                 ("discard-default-content"                  , true ),
  ElementContentWhitespace              ("element-content-whitespace"               , false),
  Entities                              ("entities"                                 , false),
  ErrorHandler                          ("error-handler"                            , false),
  FormatPrettyPrint                     ("format-pretty-print"                      , true ),
  IgnoreUnknownCharacterDenormalisation ("ignore-unknown-character-denormalizations", true ),
  Infoset                               ("infoset"                                  , false),
  NamespaceDeclarations                 ("namespace-declarations"                   , false),
  Namespaces                            ("namespaces"                               , false),
  NormaliseCharacters                   ("normalize-characters"                     , false),
  SchemaLocation                        ("schema-location"                          , false),
  SchemaType                            ("schema-type"                              , false),
  SplitCDATASections                    ("split-cdata-sections"                     , false),
  Validate                              ("validate"                                 , false),
  ValidateIfSchema                      ("validate-if-schema"                       , false),
  Wellformed                            ("well-formed"                              , false),
  XmlDeclaration                        ("xml-declaration"                          , true );

  String    key;
  boolean   xerces;
  
  /**
   * Sets this parameter for the supplied configuration.
   * 
   * @param config   The configuration which parameter has to be set.
   * @param value    The value which will be set.
   */
  public void set(@NotNull DOMConfiguration config, Object value) {
    if (config.canSetParameter(key, value)) {
      config.setParameter(key, value);
    }
  }
  
  /**
   * Returns the parameter of the supplied configuration.
   * 
   * @param config   The configuration used to get the parameter.
   * 
   * @return   The previously stored parameter.
   */
  public Object get(@NotNull DOMConfiguration config) {
    return config.getParameter(key);
  }
  
} /* ENDENUM */
