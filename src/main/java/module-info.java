import com.kasisoft.libs.common.datatypes.*;

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

    provides FileType with BmpFileType, Bzip2FileType, GifFileType, GzipFileType, JavaClassFileType, JpegFileType,
        PdfFileType, PngFileType, SevenZipFileType, ZipFileType;

    uses FileType;

    requires transitive java.desktop;

    requires transitive java.sql;

    requires transitive jakarta.validation;

} /* ENDMODULE */
