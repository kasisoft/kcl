package com.kasisoft.libs.common.constants;

import com.kasisoft.libs.common.annotation.*;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

import java.util.*;

/**
 * Collection of supported mime types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Specifications({
  @Specification(value = "http://de.selfhtml.org/diverses/mimetypen.htm", date = "10-Jun-2016"), 
  @Specification(value = "http://www.ietf.org/rfc/rfc4627.txt", date = "10-Jun-2016"), 
  @Specification(value = "http://www.iana.org/assignments/media-types", date = "10-Jun-2016") 
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum MimeType implements Predicate<String> {

  AdobePdf                    ( "application/pdf"               , "pdf"                               ),  // Adobe PDF
  Bitmap                      ( "image/bmp"                     , "bmp"                               ),  // BMP-Files
  Bzip2                       ( "application/x-bzip"            , "bz2"                               ),  // BZIP 2
  CascadingStylesheet         ( "text/css"                      , "css"                               ),  // cascading stylesheets
  CommaSeparatedValues        ( "text/comma-separated-values"   , "csv"                               ),  // comma separated values
  Dvi                         ( "application/x-dvi"             , "dvi"                               ),  // DVI
  EncapsulatedPostscript      ( "application/eps"               , "eps"                               ),  // Encapsulated Postscript
  Gif                         ( "image/gif"                     , "gif"                               ),  // GIF-Files
  GnuTar                      ( "application/x-gtar"            , "gtar"                              ),  // GNU Tar
  GZip                        ( "application/gzip"              , "gz"                                ),  // GNU Zip
  Html                        ( "text/html"                     , "htm", "html", "shtml"              ),  // HTML files
  Icon                        ( "image/x-icon"                  , "ico"                               ),  // Icon-Files
  Javascript                  ( "text/javascript"               , "js"                                ),  // JavaScript-Files
  Jpeg                        ( "image/jpeg"                    , "jpeg", "jpg", "jpe"                ),  // JPEG-Files
  Json                        ( "application/json"              , "json"                              ),  // JSON-files
  LaTeX                       ( "application/x-latex"           , "latex", "tex"                      ),  // LaTeX
  MicrosoftExcel              ( "application/msexcel"           , "xls", "xla"                        ),  // Microsoft Excel
  MicrosoftHelp               ( "application/mshelp"            , "hlp", "chm"                        ),  // Microsoft help files
  MicrosoftPowerpoint         ( "application/mspowerpoint"      , "ppt", "ppz", "pps", "pot"          ),  // Microsoft Powerpoint
  MicrosoftRTF                ( "text/rtf"                      , "rtf"                               ),  // Microsoft RTF
  MicrosoftWord               ( "application/msword"            , "doc", "dot"                        ),  // Microsoft Word
  OctetStream                 ( "application/octet-stream"      , "bin", "exe", "com", "dll", "class" ),  // Executable
  Php                         ( "application/x-httpd-php"       , "php", "phtml"                      ),  // PHP
  Plaintext                   ( "text/plain"                    , "txt", "text"                       ),  // plain text files
  Png                         ( "image/png"                     , "png"                               ),  // PNG-Files
  Postscript                  ( "application/postscript"        , "ai", "eps", "ps"                   ),  // Adobe Postscript
  SevenZip                    ( "application/x-7z-compressed"   , "7z"                                ),  // 7z
  Sgml                        ( "text/x-sgml"                   , "sgm", "sgml"                       ),  // SGML-files
  Svg                         ( "image/svg+xml"                 , "svg"                               ),  // Scalable vector graphics
  TabSeparatedValues          ( "text/tab-separated-values"     , "tsv"                               ),  // tabulator-separated files
  Tar                         ( "application/x-tar"             , "tar"                               ),  // Tape-Archive
  TeX                         ( "application/x-tex"             , "tex"                               ),  // TeX
  Tiff                        ( "image/tiff"                    , "tiff", "tif"                       ),  // TIFF-Files
  Wave                        ( "audio/x-wav"                   , "wav"                               ),  // WAV-Files
  XhtmlXml                    ( "application/xhtml+xml"         , "htm", "html", "shtml", "xhtml"     ),  // XHTML
  Xml                         ( "text/xml"                      , "xml"                               ),  // xml files
  Zip                         ( "application/zip"               , "zip"                               ),  // ZIP archives
  ZLib                        ( "application/x-compress"        , "z"                                 );  // ZLib compressed files 

  static {
    for( Map.Entry<String, Set<MimeType>> entry : LocalData.valuebysuffix.entrySet() ) {
      LocalData.valuebysuffix.put( entry.getKey(), Collections.unmodifiableSet( entry.getValue() ) );
    }
  }
  
  /** Neither <code>null</code> nor empty. */
  @Getter String         mimeType;
  
  /** Not <code>null</code>. */
  @Getter List<String>   suffices;
  
  MimeType( String type, String ... suffixlist ) {
    mimeType  = type;
    suffices  = Collections.unmodifiableList( Arrays.asList( suffixlist ) );
    LocalData.valuebymimetype.put( mimeType, this );
    for( String suffix : suffices ) {
      Set<MimeType> set = LocalData.valuebysuffix.get( suffix );
      if( set == null ) {
        set = new HashSet<>();
        LocalData.valuebysuffix.put( suffix, set );
      }
      set.add( this );
    }
  }
  
  @Override
  public boolean test( String mime ) {
    boolean result = false;
    if( mime != null ) {
      result = mimeType.equalsIgnoreCase( mime );
    }
    return result;
  }
  
  /**
   * Returns <code>true</Code> if the supplied suffix is support by this mime type.
   * 
   * @param suffix   The suffix that has to be tested. Neither <code>null</code> nor empty.
   * 
   * @return   <code>true</code> <=> The supplied suffix is supported by this mime type.
   */
  public boolean supportsSuffix( @NonNull String suffix ) {
    return suffices.contains( suffix.toLowerCase() );
  }
  
  /**
   * Returns the MimeType constant that is associated with the supplied type.
   * 
   * @param type   The current mime type which has to be identified. Neither <code>null</code> nor empty.
   * 
   * @return   The MimeType if it could be found or <code>null</code>.
   */
  public static MimeType valueByMimeType( @NonNull String type ) {
    int idx = type.indexOf(';');
    if( idx != -1 ) {
      type = idx > 0 ? type.substring( 0, idx ) : "";
    }
    return LocalData.valuebymimetype.get( type );
  }
  
  /**
   * Returns a set of known mime types supporting the supplied suffix.
   * 
   * @param suffix   The suffix used to identify a filetype. Neither <code>null</code> nor empty.
   * 
   * @return   A set of supporting mime types. Not <code>null</code>. [U]
   */
  public static Set<MimeType> valuesBySuffix( @NonNull String suffix ) {
    Set<MimeType> result = LocalData.valuebysuffix.get( suffix.toLowerCase() );
    if( result == null ) {
      result = Collections.emptySet();
    }
    return result;
  }
  
  private static class LocalData {
    
    static Map<String, MimeType>       valuebymimetype = new HashMap<>();
    static Map<String, Set<MimeType>>  valuebysuffix   = new HashMap<>();
    
  } /* ENDCLASS */
  
} /* ENDENUM */
