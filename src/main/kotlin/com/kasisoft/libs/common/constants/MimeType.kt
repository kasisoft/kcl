package com.kasisoft.libs.common.constants

import com.kasisoft.libs.common.annotation.*

import java.util.function.*;

/**
 * Collection of supported mime types.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
@Specifications(arrayOf(
    Specification(value = "https://wiki.selfhtml.org/wiki/MIME-Type/%C3%9Cbersicht", date = "04-JUN-2020"),
    Specification(value = "http://www.ietf.org/rfc/rfc4627.txt", date = "04-JUN-2020"),
    Specification(value = "https://www.iana.org/assignments/media-types/media-types.xhtml", date = "04-JUN-2020"),
    Specification(value = "https://www.freeformatter.com/mime-types-list.html", date = "04-JUN-2020")
))
enum class MimeType(val mimeType : String, val suffices : List<String>) : Predicate<String?> {

    AdobePdf                    ("application/pdf"               , listOf("pdf"                              )),  // Adobe PDF
    Bitmap                      ("image/bmp"                     , listOf("bmp"                              )),  // BMP-Files
    Bzip2                       ("application/x-bzip"            , listOf("bz2"                              )),  // BZIP 2
    CascadingStylesheet         ("text/css"                      , listOf("css"                              )),  // cascading stylesheets
    CommaSeparatedValues        ("text/comma-separated-values"   , listOf("csv"                              )),  // comma separated values
    Dvi                         ("application/x-dvi"             , listOf("dvi"                              )),  // DVI
    EncapsulatedPostscript      ("application/eps"               , listOf("eps"                              )),  // Encapsulated Postscript
    Gif                         ("image/gif"                     , listOf("gif"                              )),  // GIF-Files
    GnuTar                      ("application/x-gtar"            , listOf("gtar"                             )),  // GNU Tar
    GZip                        ("application/gzip"              , listOf("gz"                               )),  // GNU Zip
    Html                        ("text/html"                     , listOf("htm", "html", "shtml"             )),  // HTML files
    Icon                        ("image/x-icon"                  , listOf("ico"                              )),  // Icon-Files
    JavaBytecode                ("application/java-vm"           , listOf("class"                            )),  // Java Bytecode
    Javascript                  ("text/javascript"               , listOf("js"                               )),  // JavaScript-Files
    Jpeg                        ("image/jpeg"                    , listOf("jpeg", "jpg", "jpe"               )),  // JPEG-Files
    Json                        ("application/json"              , listOf("json"                             )),  // JSON-files
    LaTeX                       ("application/x-latex"           , listOf("latex", "tex"                     )),  // LaTeX
    MicrosoftExcel              ("application/msexcel"           , listOf("xls", "xla"                       )),  // Microsoft Excel
    MicrosoftHelp               ("application/mshelp"            , listOf("hlp", "chm"                       )),  // Microsoft help files
    MicrosoftPowerpoint         ("application/mspowerpoint"      , listOf("ppt", "ppz", "pps", "pot"         )),  // Microsoft Powerpoint
    MicrosoftRTF                ("text/rtf"                      , listOf("rtf"                              )),  // Microsoft RTF
    MicrosoftWord               ("application/msword"            , listOf("doc", "dot"                       )),  // Microsoft Word
    OctetStream                 ("application/octet-stream"      , listOf("bin", "exe", "com", "dll", "class")),  // Executable
    Php                         ("application/x-httpd-php"       , listOf("php", "phtml"                     )),  // PHP
    Plaintext                   ("text/plain"                    , listOf("txt", "text"                      )),  // plain text files
    Png                         ("image/png"                     , listOf("png"                              )),  // PNG-Files
    Postscript                  ("application/postscript"        , listOf("ai", "eps", "ps"                  )),  // Adobe Postscript
    SevenZip                    ("application/x-7z-compressed"   , listOf("7z"                               )),  // 7z
    Sgml                        ("text/x-sgml"                   , listOf("sgm", "sgml"                      )),  // SGML-files
    Svg                         ("image/svg+xml"                 , listOf("svg"                              )),  // Scalable vector graphics
    TabSeparatedValues          ("text/tab-separated-values"     , listOf("tsv"                              )),  // tabulator-separated files
    Tar                         ("application/x-tar"             , listOf("tar"                              )),  // Tape-Archive
    TeX                         ("application/x-tex"             , listOf("tex"                              )),  // TeX
    Tiff                        ("image/tiff"                    , listOf("tiff", "tif"                      )),  // TIFF-Files
    Wave                        ("audio/x-wav"                   , listOf("wav"                              )),  // WAV-Files
    XhtmlXml                    ("application/xhtml+xml"         , listOf("htm", "html", "shtml", "xhtml"    )),  // XHTML
    Xml                         ("text/xml"                      , listOf("xml"                              )),  // xml files
    Zip                         ("application/zip"               , listOf("zip"                              )),  // ZIP archives
    ZLib                        ("application/x-compress"        , listOf("z"                                ));  // ZLib compressed files

    val primarySuffix : String
    val primarySuffixWithDot : String

    init {

        primarySuffix = suffices[0];
        primarySuffixWithDot = "." + primarySuffix

        synchronized(MimeTypeCache) {
            MimeTypeCache.valuebymimetype[mimeType] = this
            for (suffix in suffices) {
                if (MimeTypeCache.valuebysuffix[suffix] == null) {
                    MimeTypeCache.valuebysuffix[suffix] = mutableSetOf<MimeType>()
                }
                MimeTypeCache.valuebysuffix[suffix]?.add(this)
            }
        }

    }

    companion object {

        /**
         * Returns the MimeType constant that is associated with the supplied type.
         *
         * @param type   The current mime type which has to be identified.
         *
         * @return   The MimeType if it could be found or empty.
         */
        @JvmStatic
        fun findByMimeType(type : String) : MimeType?{
            var cleanedType = type;
            val idx = cleanedType.indexOf(';');
            if (idx != -1) {
                cleanedType = if(idx > 0) cleanedType.substring(0, idx) else ""
            }
            return MimeTypeCache.valuebymimetype.get(cleanedType)
        }

        /**
         * Returns a set of known mime types supporting the supplied suffix.
         *
         * @param suffix   The suffix used to identify a filetype.
         *
         * @return   A set of supporting mime types.
         */
        @JvmStatic
        fun findBySuffix(suffix : String) : Set<MimeType> {
            var result = MimeTypeCache.valuebysuffix.get(suffix.lowercase())
            if (result != null) {
                return result.toSet()
            }
            return emptySet<MimeType>()
        }

    } /* ENDOBJECT */

    override fun test(mime : String?) : Boolean {
        if (mime != null) {
          return mimeType.equals(mime, true)
        }
        return false;
    }

    /**
     * Returns <code>true</Code> if the supplied suffix is support by this mime type.
     *
     * @param suffix   The suffix that has to be tested.
     *
     * @return   <code>true</code> <=> The supplied suffix is supported by this mime type.
     */
    fun supportsSuffix(suffix : String) = suffices.contains(suffix.lowercase())

} /* ENDENUM */

private object MimeTypeCache {

    val valuebymimetype = mutableMapOf<String, MimeType>()
    val valuebysuffix   = mutableMapOf<String, MutableSet<MimeType>>()

} /* ENDOBJECT */
