/**
 * Name........: DomConfigParameter
 * Description.: Collection of parameters for the DOMConfiguration.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml;

import org.w3c.dom.*;

/**
 * Collection of parameters for the {@link DOMConfiguration} .
 * 
 * @see http://xerces.apache.org/xerces2-j/javadocs/api/org/w3c/dom/ls/LSSerializer.html [09-Dec-2012:KASI]
 * @see http://download.oracle.com/javase/1.5.0/docs/api/org/w3c/dom/DOMConfiguration.html [09-Dec-2012:KASI]
 */
public enum DomConfigParameter {

  CanonicalForm                         ( "canonical-form"                            , false ),
  CDATASections                         ( "cdata-sections"                            , false ),
  CheckCharacterNormalisation           ( "check-character-normalization"             , false ),
  Comments                              ( "comments"                                  , false ),
  DatatypeNormalisation                 ( "datatype-normalization"                    , false ),
  DiscardDefaultContent                 ( "discard-default-content"                   , true  ),
  ElementContentWhitespace              ( "element-content-whitespace"                , false ),
  Entities                              ( "entities"                                  , false ),
  ErrorHandler                          ( "error-handler"                             , false ),
  FormatPrettyPrint                     ( "format-pretty-print"                       , true  ),
  IgnoreUnknownCharacterDenormalisation ( "ignore-unknown-character-denormalizations" , true  ),
  Infoset                               ( "infoset"                                   , false ),
  NamespaceDeclarations                 ( "namespace-declarations"                    , false ),
  Namespaces                            ( "namespaces"                                , false ),
  NormaliseCharacters                   ( "normalize-characters"                      , false ),
  SchemaLocation                        ( "schema-location"                           , false ),
  SchemaType                            ( "schema-type"                               , false ),
  SplitCDATASections                    ( "split-cdata-sections"                      , false ),
  Validate                              ( "validate"                                  , false ),
  ValidateIfSchema                      ( "validate-if-schema"                        , false ),
  Wellformed                            ( "well-formed"                               , false ),
  XmlDeclaration                        ( "xml-declaration"                           , true  );

  private String    key;
  private boolean   xerces;
  
  DomConfigParameter( String param, boolean isxerces ) {
    key    = param;
    xerces = isxerces;
  }
  
  /**
   * Returns <code>true</code> if this parameter is not necessarily available as it's supported by Xerces.
   * 
   * @return   <code>true</code> <=> This parameter is not necessarily available as it's supported by Xerces.
   */
  public boolean isXerces() {
    return xerces;
  }
  
  /**
   * Sets this parameter for the supplied configuration.
   * 
   * @param config   The configuration which parameter has to be set. Not <code>null</code>.
   * @param value    The value which will be set.
   */
  public void set( DOMConfiguration config, Object value ) {
    config.setParameter( key, value );
  }
  
  /**
   * Returns the parameter of the supplied configuration.
   * 
   * @param config   The configuration used to get the parameter. Not <code>null</code>.
   * 
   * @return   The previously stored parameter.
   */
  public Object get( DOMConfiguration config ) {
    return config.getParameter( key );
  }
  
} /* ENDENUM */
