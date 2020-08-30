import com.kasisoft.libs.common.datatypes.BmpFileType;
import com.kasisoft.libs.common.datatypes.Bzip2FileType;
import com.kasisoft.libs.common.datatypes.FileType;
import com.kasisoft.libs.common.datatypes.GifFileType;
import com.kasisoft.libs.common.datatypes.GzipFileType;
import com.kasisoft.libs.common.datatypes.JavaClassFileType;
import com.kasisoft.libs.common.datatypes.JpegFileType;
import com.kasisoft.libs.common.datatypes.PdfFileType;
import com.kasisoft.libs.common.datatypes.PngFileType;
import com.kasisoft.libs.common.datatypes.SevenZipFileType;
import com.kasisoft.libs.common.datatypes.ZipFileType;

module com.kasisoft.libs.common {

  exports com.kasisoft.libs.common;
  exports com.kasisoft.libs.common.annotation;
  exports com.kasisoft.libs.common.comparator;
  exports com.kasisoft.libs.common.constants;
  exports com.kasisoft.libs.common.converters;
  exports com.kasisoft.libs.common.csv;
  exports com.kasisoft.libs.common.data;
  exports com.kasisoft.libs.common.datatypes;
  exports com.kasisoft.libs.common.functional;
  exports com.kasisoft.libs.common.graphics;
  exports com.kasisoft.libs.common.i18n;
  exports com.kasisoft.libs.common.io;
  exports com.kasisoft.libs.common.io.impl;
  exports com.kasisoft.libs.common.pools;
  exports com.kasisoft.libs.common.text;
  exports com.kasisoft.libs.common.tree;
  exports com.kasisoft.libs.common.types;
  exports com.kasisoft.libs.common.utils;
  exports com.kasisoft.libs.common.xml;
  
  provides FileType with 
    BmpFileType, 
    Bzip2FileType,
    GifFileType,
    GzipFileType,
    JavaClassFileType,
    JpegFileType,
    PdfFileType,
    PngFileType,
    SevenZipFileType,
    ZipFileType
    ;

  uses FileType;
  
  requires transitive java.desktop;
  requires transitive java.sql;
  requires transitive java.validation;
  requires static lombok;
  requires static org.mapstruct.processor;

} /* ENDMODULE */
