/**
 * Name........: PreconfiguredXmlCatalog
 * Description.: Specialisation of the XmlCatalog which provides preconfigured resources depending
 *               on the w3c.jar which should be on the classpath.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.xml;

import com.kasisoft.lgpl.libs.common.constants.*;

import com.kasisoft.lgpl.libs.common.base.*;
import com.kasisoft.lgpl.tools.diagnostic.*;

import java.net.*;

/**
 * Specialisation of the XmlCatalog which provides preconfigured resources depending on the w3c.jar 
 * which should be on the classpath. This catalog doesn't care about missing resources as they're
 * supposed to be made available afterwards.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class PreconfiguredXmlCatalog extends XmlCatalog {

  private static final String[] PRECONFIGURED = new String[] {
    "-//W3C//DTD XMLSCHEMA 200102//EN"        , "/dtds/XMLSchema.dtd"                 ,
    "-//WAPFORUM//DTD XHTML Mobile 1.0//EN"   , "/dtds/xhtml-mobile10-flat.dtd"       ,
    "-//W3C//DTD XHTML Basic 1.0//EN"         , "/dtds/xhtml-basic10-f.dtd"           ,
    "-//W3C//DTD XHTML 1.1//EN"               , "/dtds/xhtml11-flat.dtd"              ,
    "-//W3C//DTD XHTML 1.0 Transitional//EN"  , "/dtds/xhtml1-transitional.dtd"       ,
    "-//W3C//DTD XHTML 1.0 Strict//EN"        , "/dtds/xhtml1-strict.dtd"             ,
    "-//W3C//DTD XHTML 1.0 Frameset//EN"      , "/dtds/xhtml1-frameset.dtd"           ,
    "-//WAPFORUM//DTD WML 1.3//EN"            , "/dtds/wml13.dtd"                     ,
    "-//WAPFORUM//DTD WML 1.1//EN"            , "/dtds/wml11.dtd"                     ,
    "-//W3C//DTD HTML 4.01//EN"               , "/dtds/strict.dtd"                    ,
    "-//W3C//DTD HTML 4.01 Transitional//EN"  , "/dtds/loose.dtd"                     ,
    "-//W3C//DTD HTML 4.01 Frameset//EN"      , "/dtds/frameset.dtd"                  ,
    null                                      , "/xsd/xml.xsd"                        ,
    null                                      , "/xsd/wsdl-mime.xsd"                  ,
    null                                      , "/xsd/wsdl.xsd"                       ,
    null                                      , "/xsd/soapenc.xsd"                    ,
    null                                      , "/xsd/soap.xsd"                       ,
    null                                      , "/xsd/http.xsd"                       ,
  };

  /**
   * Initialises this catalog.
   */
  public PreconfiguredXmlCatalog() {
    this( false );
  }

  /**
   * Initialises this catalog.
   * 
   * @param failifmissing   <code>true</code> <=> In case a resource could not be found a 
   *                        FailureException is raised.
   *                        
   * @throws FailureException if a resource is missing and causing a failure has been enabled.
   */
  public PreconfiguredXmlCatalog( boolean failifmissing ) throws FailureException {
    for( int i = 0; i < PRECONFIGURED.length; i += 2 ) {
      String publicid = PRECONFIGURED[ i + 0 ];
      String resource = PRECONFIGURED[ i + 1 ];
      URL    url      = getClass().getResource( resource );
      if( url != null ) {
        if( publicid != null ) {
          registerPublicID( publicid, url );
        } else {
          registerSystemID( url );
        }
      } else if( failifmissing ) {
        throw new FailureException( FailureCode.MissingResource, resource );
      }
    }
  }

} /* ENDCLASS */
