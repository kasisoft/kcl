package com.kasisoft.libs.common.constants;

import lombok.*;
import lombok.experimental.*;

/**
 * Collection of HTTP stastus codes.
 * 
 * @ks.spec [07-Dec-2014:KASI]   http://tools.ietf.org/html/rfc2616#section-6.1
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@ToString(of = "name")
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum HttpStatusCode {

  Accepted                      ( 202, "Accepted"                         ),
  BadGateway                    ( 502, "Bad Gateway"                      ),
  BadRequest                    ( 400, "Bad Request"                      ),
  Conflict                      ( 409, "Conflict"                         ),
  Continue                      ( 100, "Continue"                         ),
  Created                       ( 201, "Created"                          ),
  ExpectationFailed             ( 417, "Expectation Failed"               ),
  Forbidden                     ( 403, "Forbidden"                        ),
  Found                         ( 302, "Found"                            ),
  GatewayTimeOut                ( 504, "Gateway Time-out"                 ),
  Gone                          ( 410, "Gone"                             ),
  HttpVersionNotSupported       ( 505, "HTTP Version not supported"       ),
  InternalServerError           ( 500, "Internal Server Error"            ),
  LengthRequired                ( 411, "Length Required"                  ),
  MethodNotAllowed              ( 405, "Method Not Allowed"               ),
  MovedPermanently              ( 301, "Moved Permanently"                ),
  MultipleChoices               ( 300, "Multiple Choices"                 ),
  NoContent                     ( 204, "No Content"                       ),
  NonAuthoritativeInformation   ( 203, "Non-Authoritative Information"    ),
  NotAcceptable                 ( 406, "Not Acceptable"                   ),
  NotFound                      ( 404, "Not Found"                        ),
  NotImplemented                ( 501, "Not Implemented"                  ),
  NotModified                   ( 304, "Not Modified"                     ),
  Ok                            ( 200, "OK"                               ),
  PartialContent                ( 206, "Partial Content"                  ),
  PaymentRequired               ( 402, "Payment Required"                 ),
  PreconditionFailed            ( 412, "Precondition Failed"              ),
  ProxyAuthenticationRequired   ( 407, "Proxy Authentication Required"    ),
  RequestedRangeNotSatisfiable  ( 416, "Requested range not satisfiable"  ),
  RequestEntityTooLarge         ( 413, "Request Entity Too Large"         ),
  RequestTimeOut                ( 408, "Request Time-out"                 ),
  RequestURITooLarge            ( 414, "Request-URI Too Large"            ),
  ResetContent                  ( 205, "Reset Content"                    ),
  SeeOther                      ( 303, "See Other"                        ),
  ServiceUnavailable            ( 503, "Service Unavailable"              ),
  SwitchingProtocols            ( 101, "Switching Protocols"              ),
  TemporaryRedirect             ( 307, "Temporary Redirect"               ),
  Unauthorized                  ( 401, "Unauthorized"                     ),
  UnsupportedMediaType          ( 415, "Unsupported Media Type"           ),
  UseProxy                      ( 305, "Use Proxy"                        );
  
  @Getter int      code;
  
  /** Neither <code>null</code> nor empty. */
  @Getter String   textualCode;
  
  @Getter String   name;
  
  
  HttpStatusCode( int statuscode, String text ) {
    name        = text;
    code        = statuscode;
    textualCode = String.valueOf( code );
  }
  
  /**
   * Returns the code associated with the supplied numerical value.
   * 
   * @param statuscode   The number used to identify the code.
   * 
   * @return   The code if it could be found. <code>null</code> otherwise.
   */
  public static HttpStatusCode valueByStatusCode( int statuscode ) {
    for( HttpStatusCode code : HttpStatusCode.values() ) {
      if( code.code == statuscode ) {
        return code;
      }
    }
    return null;
  }

  /**
   * Returns the code associated with the supplied textual value.
   * 
   * @param statuscode   The textual value containing the status code. Maybe <code>null</code>.
   * 
   * @return   The code if it could be found. <code>null</code> otherwise.
   */
  public static HttpStatusCode valueByStatusCode( String statuscode ) {
    if( statuscode != null ) {
      try {
        return valueByStatusCode( Integer.parseInt( statuscode ) );
      } catch( NumberFormatException ex ) {
        // cannot be identified, so leave it
      }
    }
    return null;
  }

} /* ENDENUM */
 