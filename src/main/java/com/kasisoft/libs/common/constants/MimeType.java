package com.kasisoft.libs.common.constants;

import com.kasisoft.libs.common.annotation.*;

import javax.validation.constraints.*;

import java.util.function.*;

import java.util.*;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Collection of supported mime types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Specifications({
  @Specification(value = "https://wiki.selfhtml.org/wiki/MIME-Type/%C3%9Cbersicht", date = "04-JUN-2020"), 
  @Specification(value = "http://www.ietf.org/rfc/rfc4627.txt", date = "04-JUN-2020"), 
  @Specification(value = "https://www.iana.org/assignments/media-types/media-types.xhtml", date = "04-JUN-2020"),
  @Specification(value = "https://www.freeformatter.com/mime-types-list.html", date = "04-JUN-2020")
})
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum MimeType implements Predicate<String> {

  AdobePdf                    ("application/pdf"               , "pdf"                              ),  // Adobe PDF
  Bitmap                      ("image/bmp"                     , "bmp"                              ),  // BMP-Files
  Bzip2                       ("application/x-bzip"            , "bz2"                              ),  // BZIP 2
  CascadingStylesheet         ("text/css"                      , "css"                              ),  // cascading stylesheets
  CommaSeparatedValues        ("text/comma-separated-values"   , "csv"                              ),  // comma separated values
  Dvi                         ("application/x-dvi"             , "dvi"                              ),  // DVI
  EncapsulatedPostscript      ("application/eps"               , "eps"                              ),  // Encapsulated Postscript
  Gif                         ("image/gif"                     , "gif"                              ),  // GIF-Files
  GnuTar                      ("application/x-gtar"            , "gtar"                             ),  // GNU Tar
  GZip                        ("application/gzip"              , "gz"                               ),  // GNU Zip
  Html                        ("text/html"                     , "htm", "html", "shtml"             ),  // HTML files
  Icon                        ("image/x-icon"                  , "ico"                              ),  // Icon-Files
  JavaBytecode                ("application/java-vm"           , "class"                            ),  // Java Bytecode
  Javascript                  ("text/javascript"               , "js"                               ),  // JavaScript-Files
  Jpeg                        ("image/jpeg"                    , "jpeg", "jpg", "jpe"               ),  // JPEG-Files
  Json                        ("application/json"              , "json"                             ),  // JSON-files
  LaTeX                       ("application/x-latex"           , "latex", "tex"                     ),  // LaTeX
  MicrosoftExcel              ("application/msexcel"           , "xls", "xla"                       ),  // Microsoft Excel
  MicrosoftHelp               ("application/mshelp"            , "hlp", "chm"                       ),  // Microsoft help files
  MicrosoftPowerpoint         ("application/mspowerpoint"      , "ppt", "ppz", "pps", "pot"         ),  // Microsoft Powerpoint
  MicrosoftRTF                ("text/rtf"                      , "rtf"                              ),  // Microsoft RTF
  MicrosoftWord               ("application/msword"            , "doc", "dot"                       ),  // Microsoft Word
  OctetStream                 ("application/octet-stream"      , "bin", "exe", "com", "dll", "class"),  // Executable
  Php                         ("application/x-httpd-php"       , "php", "phtml"                     ),  // PHP
  Plaintext                   ("text/plain"                    , "txt", "text"                      ),  // plain text files
  Png                         ("image/png"                     , "png"                              ),  // PNG-Files
  Postscript                  ("application/postscript"        , "ai", "eps", "ps"                  ),  // Adobe Postscript
  SevenZip                    ("application/x-7z-compressed"   , "7z"                               ),  // 7z
  Sgml                        ("text/x-sgml"                   , "sgm", "sgml"                      ),  // SGML-files
  Svg                         ("image/svg+xml"                 , "svg"                              ),  // Scalable vector graphics
  TabSeparatedValues          ("text/tab-separated-values"     , "tsv"                              ),  // tabulator-separated files
  Tar                         ("application/x-tar"             , "tar"                              ),  // Tape-Archive
  TeX                         ("application/x-tex"             , "tex"                              ),  // TeX
  Tiff                        ("image/tiff"                    , "tiff", "tif"                      ),  // TIFF-Files
  Wave                        ("audio/x-wav"                   , "wav"                              ),  // WAV-Files
  XhtmlXml                    ("application/xhtml+xml"         , "htm", "html", "shtml", "xhtml"    ),  // XHTML
  Xml                         ("text/xml"                      , "xml"                              ),  // xml files
  Zip                         ("application/zip"               , "zip"                              ),  // ZIP archives
  ZLib                        ("application/x-compress"        , "z"                                );  // ZLib compressed files 

  static {
    for (var entry : LocalData.valuebysuffix.entrySet()) {
      LocalData.valuebysuffix.put(entry.getKey(), Collections.unmodifiableSet(entry.getValue()));
    }
  }
  
  String         mimeType;
  List<String>   suffices;
  String         primarySuffix;
  String         primarySuffixWithDot;
  
  
  MimeType(@NotBlank String type, @NotNull String ... suffixlist) {
    mimeType  = type;
    suffices  = Collections.unmodifiableList(Arrays.asList(suffixlist));
    LocalData.valuebymimetype.put(mimeType, this);
    for (var suffix : suffices) {
      LocalData.valuebysuffix.computeIfAbsent(suffix, $ -> new HashSet<>()).add(this);
    }
    primarySuffix        = suffices.get(0);
    primarySuffixWithDot = String.format(".%s", primarySuffix);
  }
  
  @Override
  public boolean test(String mime) {
    boolean result = false;
    if (mime != null) {
      result = mimeType.equalsIgnoreCase(mime);
    }
    return result;
  }
  
  /**
   * Returns <code>true</Code> if the supplied suffix is support by this mime type.
   * 
   * @param suffix   The suffix that has to be tested.
   * 
   * @return   <code>true</code> <=> The supplied suffix is supported by this mime type.
   */
  public boolean supportsSuffix(@NotNull String suffix) {
    return suffices.contains(suffix.toLowerCase());
  }
  
  /**
   * Returns the MimeType constant that is associated with the supplied type.
   * 
   * @param type   The current mime type which has to be identified.
   * 
   * @return   The MimeType if it could be found or empty.
   */
  public static @NotNull Optional<MimeType> findByMimeType(@NotNull String type) {
    var idx = type.indexOf(';');
    if (idx != -1) {
      type = idx > 0 ? type.substring(0, idx) : "";
    }
    return Optional.ofNullable(LocalData.valuebymimetype.get(type));
  }
  
  /**
   * Returns a set of known mime types supporting the supplied suffix.
   * 
   * @param suffix   The suffix used to identify a filetype.
   * 
   * @return   A set of supporting mime types.
   */
  public static @NotNull Set<MimeType> findBySuffix(@NotNull String suffix) {
    var result = LocalData.valuebysuffix.get(suffix.toLowerCase());
    if (result == null) {
      result = Collections.emptySet();
    }
    return result;
  }
  
  private static class LocalData {
    
    static Map<String, MimeType>       valuebymimetype = new HashMap<>();
    static Map<String, Set<MimeType>>  valuebysuffix   = new HashMap<>();
    
  } /* ENDCLASS */
  
} /* ENDENUM */
