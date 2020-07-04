package com.kasisoft.libs.common.constants;

import com.kasisoft.libs.common.annotation.Specification;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

import java.util.function.Predicate;

import java.util.Optional;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

/**
 * Collection of HTTP stastus codes.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Specification(value = "http://tools.ietf.org/html/rfc2616#section-6.1", date = "04-JUN-2020")
@Getter
@ToString(of = "name")
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum HttpStatusCode implements Predicate<Integer> {

  Accepted                      (202, "Accepted"                       ),
  BadGateway                    (502, "Bad Gateway"                    ),
  BadRequest                    (400, "Bad Request"                    ),
  Conflict                      (409, "Conflict"                       ),
  Continue                      (100, "Continue"                       ),
  Created                       (201, "Created"                        ),
  ExpectationFailed             (417, "Expectation Failed"             ),
  Forbidden                     (403, "Forbidden"                      ),
  Found                         (302, "Found"                          ),
  GatewayTimeOut                (504, "Gateway Time-out"               ),
  Gone                          (410, "Gone"                           ),
  HttpVersionNotSupported       (505, "HTTP Version not supported"     ),
  InternalServerError           (500, "Internal Server Error"          ),
  LengthRequired                (411, "Length Required"                ),
  MethodNotAllowed              (405, "Method Not Allowed"             ),
  MovedPermanently              (301, "Moved Permanently"              ),
  MultipleChoices               (300, "Multiple Choices"               ),
  NoContent                     (204, "No Content"                     ),
  NonAuthoritativeInformation   (203, "Non-Authoritative Information"  ),
  NotAcceptable                 (406, "Not Acceptable"                 ),
  NotFound                      (404, "Not Found"                      ),
  NotImplemented                (501, "Not Implemented"                ),
  NotModified                   (304, "Not Modified"                   ),
  Ok                            (200, "OK"                             ),
  PartialContent                (206, "Partial Content"                ),
  PaymentRequired               (402, "Payment Required"               ),
  PreconditionFailed            (412, "Precondition Failed"            ),
  ProxyAuthenticationRequired   (407, "Proxy Authentication Required"  ),
  RequestedRangeNotSatisfiable  (416, "Requested range not satisfiable"),
  RequestEntityTooLarge         (413, "Request Entity Too Large"       ),
  RequestTimeOut                (408, "Request Time-out"               ),
  RequestURITooLarge            (414, "Request-URI Too Large"          ),
  ResetContent                  (205, "Reset Content"                  ),
  SeeOther                      (303, "See Other"                      ),
  ServiceUnavailable            (503, "Service Unavailable"            ),
  SwitchingProtocols            (101, "Switching Protocols"            ),
  TemporaryRedirect             (307, "Temporary Redirect"             ),
  Unauthorized                  (401, "Unauthorized"                   ),
  UnsupportedMediaType          (415, "Unsupported Media Type"         ),
  UseProxy                      (305, "Use Proxy"                      );

  public static final String SM_ACCEPTED                          = "Accepted";
  public static final String SM_BAD_GATEWAY                       = "Bad Gateway";
  public static final String SM_BAD_REQUEST                       = "Bad Request";
  public static final String SM_CONFLICT                          = "Conflict";
  public static final String SM_CONTINUE                          = "Continue";
  public static final String SM_CREATED                           = "Created";
  public static final String SM_EXPECTATION_FAILED                = "Expectation Failed";
  public static final String SM_FORBIDDEN                         = "Forbidden";
  public static final String SM_FOUND                             = "Found";
  public static final String SM_GATEWAY_TIME_OUT                  = "Gateway Time-out";
  public static final String SM_GONE                              = "Gone";
  public static final String SM_HTTP_VERSION_NOT_SUPPORTED        = "HTTP Version not supported";
  public static final String SM_INTERNAL_SERVER_ERROR             = "Internal Server Error";
  public static final String SM_LENGHT_REQUIRED                   = "Length Required";
  public static final String SM_METHOD_NOT_ALLOWED                = "Method Not Allowed";
  public static final String SM_MOVED_PERMANENTLY                 = "Moved Permanently";
  public static final String SM_MULTIPLE_CHOICES                  = "Multiple Choices";
  public static final String SM_NO_CONTENT                        = "No Content";
  public static final String SM_NON_AUTHORITATIVE_INFORMATION     = "Non-Authoritative Information";
  public static final String SM_NOT_ACCEPTABLE                    = "Not Acceptable";
  public static final String SM_NOT_FOUND                         = "Not Found";
  public static final String SM_NOT_IMPLEMENTED                   = "Not Implemented";
  public static final String SM_NOT_MODIFIED                      = "Not Modified";
  public static final String SM_OK                                = "OK";
  public static final String SM_PARTIAL_CONTENT                   = "Partial Content";
  public static final String SM_PAYMENT_REQUIRED                  = "Payment Required";
  public static final String SM_PRECONDITION_FAILED               = "Precondition Failed";
  public static final String SM_PROXY_AUTHENTICATION_REQUIRED     = "Proxy Authentication Required";
  public static final String SM_REQUESTED_RANGE_NOT_SATISFIABLE   = "Requested range not satisfiable";
  public static final String SM_REQUEST_ENTITY_TOO_LARGE          = "Request Entity Too Large";
  public static final String SM_REQUEST_TIME_OUT                  = "Request Time-out";
  public static final String SM_REQUEST_URI_TOO_LARGE             = "Request-URI Too Large";
  public static final String SM_RESET_CONTENT                     = "Reset Content";
  public static final String SM_SEE_OTHER                         = "See Other";
  public static final String SM_SERVICE_UNAVAILABLE               = "Service Unavailable";
  public static final String SM_SWITCHING_PROTOCOLS               = "Switching Protocols";
  public static final String SM_TEMPORARY_REDIRECT                = "Temporary Redirect";
  public static final String SM_UNAUTHORIZED                      = "Unauthorized";
  public static final String SM_UNSUPPORTED_MEDIA_TYPE            = "Unsupported Media Type";
  public static final String SM_USE_PROXY                         = "Use Proxy";

  int      code;
  String   textualCode;
  String   name;
  
  HttpStatusCode(int statuscode, @NotBlank String text) {
    name        = text;
    code        = statuscode;
    textualCode = String.valueOf(code);
  }
  
  @Override
  public boolean test(@Null Integer statuscode) {
    return statuscode != null ? statuscode.intValue() == code : false;
  }
  
  /**
   * Returns the code associated with the supplied numerical value.
   * 
   * @param statuscode   The number used to identify the code.
   * 
   * @return   The code if it could be found.
   */
  public static Optional<HttpStatusCode> findByStatusCode(int statuscode) {
    for (var code : HttpStatusCode.values()) {
      if (code.code == statuscode) {
        return Optional.of(code);
      }
    }
    return Optional.empty();
  }

  /**
   * Returns the code associated with the supplied textual value.
   * 
   * @param statuscode   The textual value containing the status code.
   * 
   * @return   The code if it could be found or empty.
   */
  public static Optional<HttpStatusCode> findByStatusCode(@Null String statuscode) {
    if (statuscode != null) {
      try {
        return findByStatusCode(Integer.parseInt(statuscode));
      } catch (NumberFormatException ex) {
        // cannot be identified, so leave it
      }
    }
    return Optional.empty();
  }

} /* ENDENUM */
 