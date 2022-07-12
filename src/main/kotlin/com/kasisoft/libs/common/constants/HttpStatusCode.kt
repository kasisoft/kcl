package com.kasisoft.libs.common.constants

import com.kasisoft.libs.common.annotation.*
import javax.validation.constraints.*
import java.util.function.*
import java.util.*

/**
 * Collection of HTTP stastus codes.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Specification(value = "http://tools.ietf.org/html/rfc2616#section-6.1", date = "04-JUN-2020")
enum class HttpStatusCode(val code: Int, val text: String) : Predicate<Int?> {

    Accepted(202, "Accepted"),
    BadGateway(502, "Bad Gateway"),
    BadRequest(400, "Bad Request"),
    Conflict(409, "Conflict"),
    Continue(100, "Continue"),
    Created(201, "Created"),
    ExpectationFailed(417, "Expectation Failed"),
    Forbidden(403, "Forbidden"),
    Found(302, "Found"),
    GatewayTimeOut(504, "Gateway Time-out"),
    Gone(410, "Gone"),
    HttpVersionNotSupported(505, "HTTP Version not supported"),
    InternalServerError(500, "Internal Server Error"),
    LengthRequired(411, "Length Required"),
    MethodNotAllowed(405, "Method Not Allowed"),
    MovedPermanently(301, "Moved Permanently"),
    MultipleChoices(300, "Multiple Choices"),
    NoContent(204, "No Content"),
    NonAuthoritativeInformation(203, "Non-Authoritative Information"),
    NotAcceptable(406, "Not Acceptable"),
    NotFound(404, "Not Found"),
    NotImplemented(501, "Not Implemented"),
    NotModified(304, "Not Modified"),
    Ok(200, "OK"),
    PartialContent(206, "Partial Content"),
    PaymentRequired(402, "Payment Required"),
    PreconditionFailed(412, "Precondition Failed"),
    ProxyAuthenticationRequired(407, "Proxy Authentication Required"),
    RequestedRangeNotSatisfiable(416, "Requested range not satisfiable"),
    RequestEntityTooLarge(413, "Request Entity Too Large"),
    RequestTimeOut(408, "Request Time-out"),
    RequestURITooLarge(414, "Request-URI Too Large"),
    ResetContent(205, "Reset Content"),
    SeeOther(303, "See Other"),
    ServiceUnavailable(503, "Service Unavailable"),
    SwitchingProtocols(101, "Switching Protocols"),
    TemporaryRedirect(307, "Temporary Redirect"),
    Unauthorized(401, "Unauthorized"),
    UnsupportedMediaType(415, "Unsupported Media Type"),
    UseProxy(305, "Use Proxy");

    val textualCode: String = code.toString()

    override fun test(statuscode: Int?) = if (statuscode != null) statuscode  == code else false

    companion object {
        val SM_ACCEPTED = "Accepted"
        val SM_BAD_GATEWAY = "Bad Gateway"
        val SM_BAD_REQUEST = "Bad Request"
        val SM_CONFLICT = "Conflict"
        val SM_CONTINUE = "Continue"
        val SM_CREATED = "Created"
        val SM_EXPECTATION_FAILED = "Expectation Failed"
        val SM_FORBIDDEN = "Forbidden"
        val SM_FOUND = "Found"
        val SM_GATEWAY_TIME_OUT = "Gateway Time-out"
        val SM_GONE = "Gone"
        val SM_HTTP_VERSION_NOT_SUPPORTED = "HTTP Version not supported"
        val SM_INTERNAL_SERVER_ERROR = "Internal Server Error"
        val SM_LENGHT_REQUIRED = "Length Required"
        val SM_METHOD_NOT_ALLOWED = "Method Not Allowed"
        val SM_MOVED_PERMANENTLY = "Moved Permanently"
        val SM_MULTIPLE_CHOICES = "Multiple Choices"
        val SM_NO_CONTENT = "No Content"
        val SM_NON_AUTHORITATIVE_INFORMATION = "Non-Authoritative Information"
        val SM_NOT_ACCEPTABLE = "Not Acceptable"
        val SM_NOT_FOUND = "Not Found"
        val SM_NOT_IMPLEMENTED = "Not Implemented"
        val SM_NOT_MODIFIED = "Not Modified"
        val SM_OK = "OK"
        val SM_PARTIAL_CONTENT = "Partial Content"
        val SM_PAYMENT_REQUIRED = "Payment Required"
        val SM_PRECONDITION_FAILED = "Precondition Failed"
        val SM_PROXY_AUTHENTICATION_REQUIRED = "Proxy Authentication Required"
        val SM_REQUESTED_RANGE_NOT_SATISFIABLE = "Requested range not satisfiable"
        val SM_REQUEST_ENTITY_TOO_LARGE = "Request Entity Too Large"
        val SM_REQUEST_TIME_OUT = "Request Time-out"
        val SM_REQUEST_URI_TOO_LARGE = "Request-URI Too Large"
        val SM_RESET_CONTENT = "Reset Content"
        val SM_SEE_OTHER = "See Other"
        val SM_SERVICE_UNAVAILABLE = "Service Unavailable"
        val SM_SWITCHING_PROTOCOLS = "Switching Protocols"
        val SM_TEMPORARY_REDIRECT = "Temporary Redirect"
        val SM_UNAUTHORIZED = "Unauthorized"
        val SM_UNSUPPORTED_MEDIA_TYPE = "Unsupported Media Type"
        val SM_USE_PROXY = "Use Proxy"

        /**
         * Returns the code associated with the supplied numerical value.
         *
         * @param statuscode The number used to identify the code.
         *
         * @return The code if it could be found.
         */
        @JvmStatic
        fun findByStatusCode(statuscode: Int): HttpStatusCode? {
            for (code in HttpStatusCode.values()) {
                if (code.code == statuscode) {
                    return code
                }
            }
            return null
        }

        /**
         * Returns the code associated with the supplied textual value.
         *
         * @param statuscode The textual value containing the status code.
         *
         * @return The code if it could be found or empty.
         */
        @JvmStatic
        fun findByStatusCode(statuscode: String?): HttpStatusCode? {
            if (statuscode != null) {
                try {
                    return findByStatusCode(Integer.parseInt(statuscode))
                } catch (ex: NumberFormatException) {
                    // cannot be identified, so leave it
                }
            }
            return null
        }

    } /* ENDOBJECT */

} /* ENDENUM */
