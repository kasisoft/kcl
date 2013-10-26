/**
 * Name........: MimeType
 * Description.: Collection of mime types.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.constants;

import java.util.*;

/**
 * @ks.spec [09-Dec-2012:KASI]   http://de.selfhtml.org/diverses/mimetypen.htm
 * 
 * Alternate and more official: @source [09-Dec-2012:KASI]   http://www.iana.org/assignments/media-types
 * 
 * @ks.note [09-Dec-2012:KASI]   Not all types have been used here.
 */
public enum MimeType {

  GZip                        ( "application/gzip"              , "gz"                                ),  // GNU Zip
  MicrosoftExcel              ( "application/msexcel"           , "xls", "xla"                        ),  // Microsoft Excel
  MicrosoftHelp               ( "application/mshelp"            , "hlp", "chm"                        ),  // Microsoft help files
  MicrosoftPowerpoint         ( "application/mspowerpoint"      , "ppt", "ppz", "pps", "pot"          ),  // Microsoft Powerpoint
  MicrosoftWord               ( "application/msword"            , "doc", "dot"                        ),  // Microsoft Word
  OctetStream                 ( "application/octet-stream"      , "bin", "exe", "com", "dll", "class" ),  // Executable
  AdobePdf                    ( "application/pdf"               , "pdf"                               ),  // Adobe PDF
  EncapsulatedPostscript      ( "application/eps"               , "eps"                               ),  // Encapsulated Postscript
  Postscript                  ( "application/postscript"        , "ai", "eps", "ps"                   ),  // Adobe Postscript
  XhtmlXml                    ( "application/xhtml+xml"         , "htm", "html", "shtml", "xhtml"     ),  // XHTML
  ZLib                        ( "application/x-compress"        , "z"                                 ),  // ZLib compressed files 
  Dvi                         ( "application/x-dvi"             , "dvi"                               ),  // DVI
  GnuTar                      ( "application/x-gtar"            , "gtar"                              ),  // GNU Tar
  Php                         ( "application/x-httpd-php"       , "php", "phtml"                      ),  // PHP
  LaTeX                       ( "application/x-latex"           , "latex", "tex"                      ),  // LaTeX
  Tar                         ( "application/x-tar"             , "tar"                               ),  // Tape-Archive
  TeX                         ( "application/x-tex"             , "tex"                               ),  // TeX
  Zip                         ( "application/zip"               , "zip"                               ),  // ZIP archives
  Wave                        ( "audio/x-wav"                   , "wav"                               ),  // WAV-Files
  Bitmap                      ( "image/bmp"                     , "bmp"                               ),  // BMP-Files
  Gif                         ( "image/gif"                     , "gif"                               ),  // GIF-Files
  Jpeg                        ( "image/jpeg"                    , "jpeg", "jpg", "jpe"                ),  // JPEG-Files
  Png                         ( "image/png"                     , "png"                               ),  // PNG-Files
  Tiff                        ( "image/tiff"                    , "tiff", "tif"                       ),  // TIFF-Files
  Icon                        ( "image/x-icon"                  , "ico"                               ),  // Icon-Files
  Svg                         ( "image/svg+xml"                 , "svg"                               ),  // Scalable vector graphics
  CommaSeparatedValues        ( "text/comma-separated-values"   , "csv"                               ),  // comma separated values
  CascadingStylesheet         ( "text/css"                      , "css"                               ),  // cascading stylesheets
  Html                        ( "text/html"                     , "htm", "html", "shtml"              ),  // HTML files
  Javascript                  ( "text/javascript"               , "js"                                ),  // JavaScript-Files
  Plaintext                   ( "text/plain"                    , "txt", "text"                       ),  // plain text files
  MicrosoftRTF                ( "text/rtf"                      , "rtf"                               ),  // Microsoft RTF
  TabSeparatedValues          ( "text/tab-separated-values"     , "tsv"                               ),  // tabulator-separated files
  Xml                         ( "text/xml"                      , "xml"                               ),  // xml files
  Sgml                        ( "text/x-sgml"                   , "sgm", "sgml"                       );  // SGML-files

  private String         mimetype;
  private List<String>   suffices;
  
  MimeType( String type, String ... suffixlist ) {
    mimetype  = type;
    suffices  = Collections.unmodifiableList( Arrays.asList( suffixlist ) );
  }
  
  /**
   * Returns the mime type as it appears in internet related messages/documents.
   * 
   * @return   The mime type as it appears in internet related messages/documents. Neither <code>null</code> nor empty.
   */
  public String getMimeType() {
    return mimetype;
  }
  
  /**
   * Returns a list of suffices supported by this mime type. The suffices don't contain the dot
   * and are all lowercase.
   * 
   * @return   A list of suffices supported by this mime type. Not <code>null</code>.
   */
  public List<String> getSuffices() {
    return suffices;
  }
  
  /**
   * Returns <code>true</Code> if the supplied suffix is support by this mime type.
   * 
   * @param suffix   The suffix that has to be tested. Neither <code>null</code> nor empty.
   * 
   * @return   <code>true</code> <=> The supplied suffix is supported by this mime type.
   */
  public boolean supportsSuffix( String suffix ) {
    return suffices.contains( suffix.toLowerCase() );
  }
  
  /**
   * Returns the MimeType constant that is associated with the supplied type.
   * 
   * @param type   The current mime type which has to be identified. Neither <code>null</code> nor empty.
   * 
   * @return   The MimeType if it could be found or <code>null</code>.
   */
  public static MimeType valueByMimeType( String type ) {
    for( MimeType mime : MimeType.values() ) {
      if( type.equalsIgnoreCase( mime.mimetype ) ) {
        return mime;
      }
    }
    return null;
  }
  
  /**
   * Returns a set of known mime types supporting the supplied suffix.
   * 
   * @param suffix   The suffix used to identify a filetype. Neither <code>null</code> nor empty.
   * 
   * @return   A set of supporting mime types. Not <code>null</code>. [U]
   */
  public static Set<MimeType> valuesBySuffix( String suffix ) {
    Set<MimeType> result = new HashSet<MimeType>();
    for( MimeType mime : MimeType.values() ) {
      if( mime.supportsSuffix( suffix ) ) {
        result.add( mime );
      }
    }
    return result;
  }
  
} /* ENDENUM */
