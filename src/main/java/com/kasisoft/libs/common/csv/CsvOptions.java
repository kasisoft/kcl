package com.kasisoft.libs.common.csv;

import com.kasisoft.libs.common.constants.*;

import java.util.*;

import lombok.experimental.*;

import lombok.*;

/**
 * A collection of options used for the csv processing.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter @ToString @EqualsAndHashCode
public final class CsvOptions {

  boolean           titleRow            = false;
  char              delimiter           = ',';
  boolean           disableCr           = true;
  boolean           fillMissingColumns  = false;
  boolean           consumeSingleQuotes = true;
  boolean           consumeDoubleQuotes = true;
  
  // Processing a simple formatted CSV is much faster as it's less robust but should be capable to read most CSV files.
  // A CSV file must fulfill the following constraints:
  // 
  //   * One record per row (no records spanning multiple lines)
  //   * If cell content is quoted it's not allowed to have whitespace outside of the quoted region
  //   * All records must have the same amount of cells
  // 
  boolean           simpleFormat        = false;
  // enable/disable whether the simple loading is allowed to mixup the record order
  boolean           orderedSimpleFormat = true;
  int               maxLines            = -1;
  Encoding          encoding            = Encoding.UTF8;
  List<CsvColumn>   columns             = new ArrayList<>();
  
  public CsvOptions() {
  }
  
  /**
   * Creates a deep copy of this instance.
   * 
   * @return   A deep copy of this instance. Not <code>null</code>.
   */
  public CsvOptions deepCopy() {
    CsvOptions result           = new CsvOptions();
    result.titleRow             = titleRow;
    result.delimiter            = delimiter;
    result.disableCr            = disableCr;
    result.fillMissingColumns   = fillMissingColumns;
    result.consumeSingleQuotes  = consumeSingleQuotes;
    result.consumeDoubleQuotes  = consumeDoubleQuotes;
    result.simpleFormat         = simpleFormat;
    result.orderedSimpleFormat  = orderedSimpleFormat;
    result.maxLines             = maxLines;
    result.encoding             = encoding;
    for( CsvColumn column : columns ) {
      if( column != null ) {
        result.columns.add( column.copy() );
      } else {
        result.columns.add( null );
      }
    }
    return result;
  }
  
  public static CsvOptionsBuilder builder() {
    return new CsvOptionsBuilder();
  }
  
  @FieldDefaults(level = AccessLevel.PRIVATE)
  public static class CsvOptionsBuilder {
    
    CsvOptions   instance = new CsvOptions();
    
    private CsvOptionsBuilder() {
    }

    public CsvOptionsBuilder simpleFormat() {
      instance.simpleFormat = true;
      return this;
    }
    
    public CsvOptionsBuilder maxLines( int maxLines ) {
      instance.maxLines = maxLines;
      return this;
    }

    public CsvOptionsBuilder singleQuotes( boolean consumeSingleQuotes ) {
      instance.consumeSingleQuotes = consumeSingleQuotes;
      return this;
    }

    public CsvOptionsBuilder doubleQuotes( boolean consumeDoubleQuotes ) {
      instance.consumeDoubleQuotes = consumeDoubleQuotes;
      return this;
    }

    public CsvOptionsBuilder encoding( @NonNull Encoding encoding ) {
      instance.encoding = encoding;
      return this;
    }

    public CsvOptionsBuilder delimiter( char delimiter ) {
      instance.delimiter = delimiter;
      return this;
    }
    
    public CsvOptionsBuilder fillMissingColumns() {
      return fillMissingColumns( true );
    }
    
    public CsvOptionsBuilder fillMissingColumns( boolean enable ) {
      instance.fillMissingColumns = enable;
      return this;
    }
    
    public CsvOptionsBuilder disableCr() {
      return disableCr( true );
    }
    
    public CsvOptionsBuilder disableCr( boolean enable ) {
      instance.disableCr = enable;
      return this;
    }
    
    public CsvOptionsBuilder titleRow() {
      return titleRow( true );
    }
    
    public CsvOptionsBuilder titleRow( boolean enable ) {
      instance.titleRow = enable;
      return this;
    }

    public CsvOptionsBuilder column( CsvColumn column ) {
      instance.columns.add( column );
      return this;
    }
    
    public CsvOptionsBuilder columns( @NonNull List<CsvColumn> columns ) {
      instance.columns.clear();
      instance.columns.addAll( columns );
      return this;
    }

    public CsvOptions build() {
      return instance;
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
